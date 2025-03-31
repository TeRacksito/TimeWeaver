import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { ProjectInvitation } from '../types/project';
import { toApiUrl } from '../utils/api';

@Injectable({
  providedIn: 'root',
})
export class InvitationsService {
  private authService = inject(AuthService);
  private http = inject(HttpClient);

  constructor() {}

  getAllProjectInvitations(userId: string): Observable<ProjectInvitation[]> {
    return this.http.get<ProjectInvitation[]>(
      toApiUrl(`/users/${userId}/project-invitations`),
    );
  }

  createProjectInvitation(
    projectId: number,
    invitedUserId: number,
    projectRoleId: number,
  ): Observable<ProjectInvitation> {
    return this.http.put<ProjectInvitation>(
      toApiUrl(
        `/projects/${projectId}/users/${invitedUserId}/roles/${projectRoleId}/invite`,
      ),
      {},
    );
  }

  declineInvitation(projectInvitation: ProjectInvitation): Observable<void> {
    return this.http.delete<void>(
      toApiUrl(
        `/projects/${projectInvitation.project.id}/users/${projectInvitation.invitedUser.id}/roles/${projectInvitation.projectRole.id}/invite`,
      ),
    );
  }

  acceptInvitation(projectInvitation: ProjectInvitation): Observable<void> {
    return this.http.put<void>(
      toApiUrl(
        `/projects/${projectInvitation.project.id}/users/${projectInvitation.invitedUser.id}/roles/${projectInvitation.projectRole.id}`,
      ),
      {},
    );
  }
}
