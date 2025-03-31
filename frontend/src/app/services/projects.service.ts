import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Project, ProjectRole } from '../types/project';
import { toApiUrl } from '../utils/api';

@Injectable({
  providedIn: 'root',
})
export class ProjectsService {
  constructor(private http: HttpClient) {}

  getProjects(userId: string): Observable<Project[]> {
    return this.http.get<Project[]>(toApiUrl(`/users/${userId}/projects`));
  }

  getProjectById(projectId: string): Observable<Project> {
    return this.http.get<Project>(toApiUrl(`/projects/${projectId}`));
  }

  getAllProjectRoles(): Observable<ProjectRole[]> {
    return this.http.get<ProjectRole[]>(toApiUrl('/project-roles'));
  }

  getProjectRoleByUser(
    projectId: string,
    userId: string,
  ): Observable<ProjectRole> {
    return this.http.get<ProjectRole>(
      toApiUrl(`/projects/${projectId}/users-role/${userId}`),
    );
  }

  isManager(projectId: string, userId: string): Observable<boolean> {
    return this.getProjectRoleByUser(projectId, userId).pipe(
      map((role) => role.name === 'ROLE_PROJECT_MANAGER'),
    );
  }

  createProject(
    projectName: string,
    projectDescription: string,
    userId: string,
  ): Observable<Project> {
    return this.http.post<Project>(toApiUrl(`/users/${userId}/projects`), {
      name: projectName,
      description: projectDescription,
    });
  }
}
