import { CommonModule } from '@angular/common';
import {
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  signal,
  ViewChild,
} from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { Project } from '../../types/project';
import { ProjectsService } from '../../services/projects.service';
import {
  Timeline,
  TimelineOptions,
  TimelineOptionsItemCallbackFunction,
  TimelineItem,
} from 'vis-timeline';
import { DataSet } from 'vis-data';
import { UsersService } from '../../services/users.service';
import { InviteUserComponent } from '../invite-user/invite-user.component';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-project',
  imports: [CommonModule, RouterModule, TranslatePipe, InviteUserComponent],
  templateUrl: './project.component.html',
  styleUrl: './project.component.css',
})
export class ProjectComponent implements OnInit, OnDestroy {
  inviteModalTrigger$ = new Subject<void>();

  project?: Project;
  websocket: WebSocket | null = null;
  timeline!: Timeline;
  options: TimelineOptions = {
    editable: true,
    orientation: 'top',
    stack: true,
    start: 0,
    end: 10,
    itemsAlwaysDraggable: true,
    margin: {
      axis: 0,
    },
    showMajorLabels: true,
    onAdd: (item, callback) => this.syncTimelineEvent('add', item, callback),
    onMove: (item, callback) => this.syncTimelineEvent('move', item, callback),
    onUpdate: (item, callback) =>
      this.syncTimelineEvent('update', item, callback),
    onRemove: (item, callback) =>
      this.syncTimelineEvent('remove', item, callback),
  };
  data = new DataSet([
    {
      id: 1,
      group: 0,
      content: 'item 1',
      start: 0,
      end: 3,
    },
  ]);

  groups = new DataSet([
    {
      id: 0,
      content: 'Group 1',
    },
  ]);

  @ViewChild('timeline', { static: true }) timelineElement!: ElementRef;

  activeUsers: string[] = [];
  currentUser: string = '';

  constructor(
    private route: ActivatedRoute,
    private projectsService: ProjectsService,
    private usersService: UsersService,
  ) {}

  ngOnInit(): void {
    const projectId = this.route.snapshot.paramMap.get('projectId');
    if (projectId) {
      this.projectsService.getProjectById(projectId).subscribe((project) => {
        this.project = project;

        this.timeline = new Timeline(
          this.timelineElement.nativeElement,
          this.data,
          this.groups,
          this.options,
        );

        this.connectWebSocket();
      });
    }
  }

  ngOnDestroy(): void {
    if (this.websocket) {
      this.websocket.close();
    }
  }

  connectWebSocket(): void {
    const token = localStorage.getItem('token');

    if (!this.project?.id) {
      console.log(this.project);

      console.error(
        'Project ID is not available, cannot connect to WebSocket.',
      );
      return;
    }
    this.websocket = new WebSocket(
      `/ws/projects/${this.project?.id}?token=${token}`,
    );

    this.websocket.onopen = () => {
      console.log('WebSocket connection opened');
    };

    this.websocket.onclose = () => {
      console.log('WebSocket connection closed');
    };

    this.websocket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };

    this.websocket.onmessage = (event) => {
      console.log('Received from server:', event.data);
      try {
        const message = JSON.parse(event.data);

        if (message.type === 'connection_success') {
          this.currentUser = message.username;
          this.activeUsers = message.activeUsers || [];
          console.log(
            'Connected successfully. Active users:',
            this.activeUsers,
          );
        } else if (
          message.type === 'user_joined' ||
          message.type === 'user_left'
        ) {
          this.activeUsers = message.activeUsers || [];
          console.log(
            `User ${message.type === 'user_joined' ? 'joined' : 'left'}: ${message.username}`,
          );
          console.log('Active users:', this.activeUsers);
        } else if (message.type && message.data) {
          switch (message.type) {
            case 'add':
              this.data.add(message.data);
              break;
            case 'update':
              this.data.update(message.data);
              break;
            case 'move':
              this.data.update(message.data);
              break;
            case 'remove':
              this.data.remove(message.data.id);
              break;
            default:
              console.warn('Unknown event type:', message.type);
          }
        }
      } catch (error) {
        console.error('Error processing WebSocket message:', error);
      }
    };
  }

  syncTimelineEvent(
    type: string,
    item: TimelineItem,
    callback: (item: TimelineItem | null) => void,
  ): void {
    console.log(`Timeline event: ${type}`, item);

    if (this.websocket && this.websocket.readyState === WebSocket.OPEN) {
      const payload = JSON.stringify({ type, data: item });
      this.websocket.send(payload);
    } else {
      console.warn('WebSocket connection not open, cannot send event.');
    }
    callback(item);
  }

  openNameModal(): void {
    console.log('Opening invite modal');

    this.inviteModalTrigger$.next();
  }
}
