import {
  Component,
  inject,
  OnInit,
  signal,
  WritableSignal,
} from '@angular/core';
import { Project } from '../../types/project';
import { ProjectsService } from '../../services/projects.service';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { TranslatePipe } from '@ngx-translate/core';
import { RouterModule } from '@angular/router';
import { getApiError } from '../../utils/api';
import { Subject } from 'rxjs';
import { CreateProjectComponent } from '../create-project/create-project.component';

@Component({
  selector: 'app-projects',
  imports: [CommonModule, RouterModule, TranslatePipe, CreateProjectComponent],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.css',
})
export class ProjectsComponent implements OnInit {
  private authService = inject(AuthService);
  createProjectModalTrigger$ = new Subject<void>();

  projects: Project[] = [];
  isLoading = signal(false);
  errorMessage: WritableSignal<string | null> = signal(null);

  constructor(private projectsService: ProjectsService) {}

  ngOnInit(): void {
    this.loadProjects();
  }

  loadProjects(): void {
    this.isLoading.set(true);
    this.projectsService
      .getProjects(this.authService.getUserId()?.toString() || '0')
      .subscribe({
        next: (data) => {
          this.projects = data;
          this.isLoading.set(false);
          this.errorMessage.set(null);
        },
        error: (err) => {
          this.isLoading.set(false);
          this.errorMessage.set(getApiError(err, 'error loading projects'));
        },
      });
  }

  openCreateProjectModal(): void {
    this.createProjectModalTrigger$.next();
  }

  onProjectCreated(): void {
    this.loadProjects();
  }

  getIndexArray(length: number): number[] {
    return Array.from({ length }, (_, index) => index);
  }
}
