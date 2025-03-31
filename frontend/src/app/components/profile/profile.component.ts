import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { TranslatePipe } from '@ngx-translate/core';
import { NotificationService } from '../../services/notification.service';
import { InvitationsService } from '../../services/invitations.service';
import { NotificationEvent } from '../../types/notification';
import { ProjectInvitation } from '../../types/project';

export enum NotificationType {
  MESSAGE = 'message',
  NOTIFICATION = 'notification',
  PROJECT_INVITATION = 'project_invitation',
  INVITATION_ACCEPTED = 'invitation_accepted',
  INVITATION_DECLINED = 'invitation_declined',
}

@Component({
  selector: 'app-profile',
  imports: [CommonModule, TranslatePipe],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class ProfileComponent implements OnInit {
  private authService = inject(AuthService);
  private router = inject(Router);
  private notificationService = inject(NotificationService);
  private invitationsService = inject(InvitationsService);

  notifications: NotificationEvent[] = [];

  ngOnInit(): void {
    this.notificationService.getNotifications().subscribe((notifications) => {
      this.notifications = notifications;
    });

    this.notificationService.markAllAsRead();
  }

  isNotificationTypeProjectInvitation(
    notification: NotificationEvent,
  ): boolean {
    return (
      notification.type === NotificationType.PROJECT_INVITATION &&
      this.isProjectInvitation(notification.data)
    );
  }

  isNotificationTypeInvitationAccepted(
    notification: NotificationEvent,
  ): boolean {
    return (
      notification.type === NotificationType.INVITATION_ACCEPTED &&
      this.isProjectInvitation(notification.data)
    );
  }

  isNotificationTypeInvitationDeclined(
    notification: NotificationEvent,
  ): boolean {
    return (
      notification.type === NotificationType.INVITATION_DECLINED &&
      this.isProjectInvitation(notification.data)
    );
  }

  isProjectInvitation(data: any): data is ProjectInvitation {
    return (
      data &&
      typeof data === 'object' &&
      'project' in data &&
      'inviterUser' in data &&
      'invitedUser' in data &&
      'projectRole' in data
    );
  }

  getProjectInvitation(
    notification: NotificationEvent,
  ): ProjectInvitation | null {
    if (this.isProjectInvitation(notification.data)) {
      return notification.data;
    }
    return null;
  }

  getTranslationKey(notificationType: NotificationType): string {
    switch (notificationType) {
      case NotificationType.PROJECT_INVITATION:
        return 'PROJECT_INVITATION';
      case NotificationType.INVITATION_ACCEPTED:
        return 'INVITATION_ACCEPTED';
      case NotificationType.INVITATION_DECLINED:
        return 'INVITATION_DECLINED';
      default:
        return '';
    }
  }

  get username(): string | null {
    return this.authService.getUsername();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  acceptInvitation(notification: NotificationEvent): void {
    const invitation = this.getProjectInvitation(notification);
    if (invitation) {
      this.invitationsService.acceptInvitation(invitation).subscribe({
        next: () => {
          this.notifications = this.notifications.filter(
            (n) => n !== notification,
          );
        },
        error: (error) => {
          console.error('Error accepting invitation:', error);
        },
      });
    }
  }

  declineInvitation(notification: NotificationEvent): void {
    const invitation = this.getProjectInvitation(notification);
    if (invitation) {
      this.invitationsService.declineInvitation(invitation).subscribe({
        next: () => {
          this.notifications = this.notifications.filter(
            (n) => n !== notification,
          );
        },
        error: (error) => {
          console.error('Error declining invitation:', error);
        },
      });
    }
  }
}
