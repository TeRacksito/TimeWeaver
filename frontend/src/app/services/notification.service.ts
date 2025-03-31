import { NotificationEvent } from '../types/notification';
import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { toApiUrl } from '../utils/api';
import { AuthService } from './auth.service';
import { EventSourcePolyfill } from 'event-source-polyfill';
import { ProjectInvitation } from '../types/project';

import { InvitationsService } from './invitations.service';

export enum NotificationType {
  MESSAGE = 'message',
  NOTIFICATION = 'notification',
  PROJECT_INVITATION = 'project_invitation',
  INVITATION_ACCEPTED = 'invitation_accepted',
  INVITATION_DECLINED = 'invitation_declined',
}

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private authService = inject(AuthService);
  private invitationsService = inject(InvitationsService);
  private eventSource: EventSource | null = null;
  private notifications = new BehaviorSubject<NotificationEvent[]>([]);
  private unreadCount = new BehaviorSubject<number>(0);

  constructor() {
    if (this.authService.isLoggedIn()) {
      this.connect();

      this.invitationsService
        .getAllProjectInvitations(this.authService.getUserId()?.toString()!)
        .subscribe((invitations) => {
          invitations.forEach((invitation) => {
            this.addNotification({
              data: invitation,
              type: NotificationType.PROJECT_INVITATION,
              read: false,
              createdAt: new Date().toISOString(),
            } as NotificationEvent);
          });
        });
    }

    this.authService.isAuthenticated$.subscribe((isAuthenticated) => {
      if (isAuthenticated) {
        this.connect();
      } else {
        this.disconnect();
      }
    });
  }

  connect(): void {
    if (this.eventSource) {
      return;
    }

    const token = this.authService.getToken();
    if (!token) {
      console.error('Cannot connect to SSE: No auth token available');
      return;
    }

    this.eventSource = new EventSourcePolyfill(
      `${toApiUrl('/notifications/subscribe')}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        heartbeatTimeout: 120_000,
      },
    );

    this.eventSource.onopen = () => {
      console.log('SSE connection established');
    };

    this.eventSource.onmessage = (event) => {
      this.handleNotificationEvent('message', event);
    };

    this.eventSource.addEventListener('notification', (event) => {
      this.handleNotificationEvent('notification', event);
    });

    this.eventSource.addEventListener('project_invitation', (event) => {
      this.handleProjectInvitationEvent(
        NotificationType.PROJECT_INVITATION,
        event,
      );
    });

    this.eventSource.addEventListener('invitation_accepted', (event) => {
      this.handleProjectInvitationEvent(
        NotificationType.INVITATION_ACCEPTED,
        event,
      );
    });

    this.eventSource.addEventListener('invitation_declined', (event) => {
      this.handleProjectInvitationEvent(
        NotificationType.INVITATION_DECLINED,
        event,
      );
    });

    this.eventSource.onerror = (error) => {
      console.error('SSE connection error:', error);
      this.reconnect();
    };
  }

  private handleProjectInvitationEvent(
    type: NotificationType,
    event: MessageEvent,
  ): void {
    try {
      const projectInvitation = JSON.parse(event.data) as ProjectInvitation;
      this.addNotification({
        data: projectInvitation,
        type,
        read: false,
        createdAt: new Date().toISOString(),
      } as NotificationEvent);
    } catch (error) {
      console.error('Error parsing SSE message:', error);
    }
  }

  private handleNotificationEvent(type: string, event: MessageEvent): void {
    try {
      const notification = JSON.parse(event.data) as NotificationEvent;
      this.addNotification(notification);
      console.log('Notification received:', notification);
    } catch (error) {
      console.error('Error parsing SSE message:', error);
    }
  }

  private reconnect(): void {
    this.disconnect();

    const delay = 500;

    setTimeout(() => {
      if (this.authService.isLoggedIn()) {
        this.connect();
      }
    }, delay);
  }

  disconnect(): void {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
    }
  }

  addNotification(notification: NotificationEvent): void {
    const currentNotifications = this.notifications.value;
    const updatedNotifications = [notification, ...currentNotifications];
    this.notifications.next(updatedNotifications);
    this.updateUnreadCount();
  }

  markAllAsRead(): void {
    const currentNotifications = this.notifications.value;
    const updatedNotifications = currentNotifications.map((notification) => ({
      ...notification,
      read: true,
    }));
    this.notifications.next(updatedNotifications);
    this.unreadCount.next(0);
  }

  private updateUnreadCount(): void {
    const count = this.notifications.value.filter((n) => !n.read).length;
    this.unreadCount.next(count);
  }

  getNotifications(): Observable<NotificationEvent[]> {
    return this.notifications.asObservable();
  }

  getUnreadCount(): Observable<number> {
    return this.unreadCount.asObservable();
  }

  ngOnDestroy(): void {
    this.disconnect();
  }
}
