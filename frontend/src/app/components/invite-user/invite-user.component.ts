import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  signal,
} from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
  AsyncValidatorFn,
} from '@angular/forms';
import { Subject, Subscription, Observable, map, catchError, of } from 'rxjs';
import { UsersService } from '../../services/users.service';
import { AuthService } from '../../services/auth.service';
import { ProjectsService } from '../../services/projects.service';
import { ProjectRole } from '../../types/project';
import { debounce } from '../../utils/debounce';
import { TranslatePipe } from '@ngx-translate/core';
import { InvitationsService } from '../../services/invitations.service';

@Component({
  selector: 'app-invite-user',
  imports: [CommonModule, ReactiveFormsModule, TranslatePipe],
  templateUrl: './invite-user.component.html',
  styleUrl: './invite-user.component.css',
})
export class InviteUserComponent implements OnInit, OnDestroy {
  @Input() openModalTrigger!: Subject<void>;
  @Input() projectId!: number;
  @Output() inviteSent = new EventEmitter<{
    username: string;
    roleId: string;
  }>();

  isUserLoading = signal(false);
  isRolesLoading = signal(false);
  isInviting = signal(false);
  isOpen = signal(false);
  inviteForm: FormGroup;
  currentUsername: string = '';
  projectRoles: ProjectRole[] = [];

  private subscription!: Subscription;

  constructor(
    private fb: FormBuilder,
    private usersService: UsersService,
    private authService: AuthService,
    private projectsService: ProjectsService,
    private invitationsService: InvitationsService,
  ) {
    this.inviteForm = this.fb.group({
      username: ['', [Validators.required], [this.usernameValidator()]],
      roleId: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    if (this.openModalTrigger) {
      this.subscription = this.openModalTrigger.subscribe(() => {
        this.openModal();
      });
    }

    this.currentUsername = this.authService.getUsername() || '';
    this.loadProjectRoles();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  get usernameControl() {
    return this.inviteForm.get('username');
  }

  get roleControl() {
    return this.inviteForm.get('roleId');
  }

  loadProjectRoles(): void {
    this.isRolesLoading.set(true);
    this.projectsService.getAllProjectRoles().subscribe({
      next: (roles) => {
        this.projectRoles = roles;
        this.isRolesLoading.set(false);
      },
      error: () => {
        this.isRolesLoading.set(false);
      },
    });
  }

  openModal(): void {
    this.inviteForm.reset();
    this.isOpen.set(true);
  }

  closeModal(): void {
    this.isOpen.set(false);
  }

  submitName(): void {
    if (this.inviteForm.valid) {
      const username = this.inviteForm.value.username;
      const roleId = this.inviteForm.value.roleId;

      this.isInviting.set(true);

      this.usersService
        .getUserByUsername(username)
        .pipe(
          map((user) => {
            return this.invitationsService.createProjectInvitation(
              Number(this.projectId),
              Number(user.id),
              Number(roleId),
            );
          }),
          catchError((error) => {
            console.error('Error fetching user:', error);
            this.isInviting.set(false);
            return of(null);
          }),
        )
        .subscribe({
          next: (invitationObservable) => {
            if (invitationObservable) {
              invitationObservable.subscribe({
                next: () => {
                  this.inviteSent.emit({ username, roleId });
                  this.isInviting.set(false);
                  this.closeModal();
                },
                error: (error) => {
                  console.error('Error creating invitation:', error);
                  this.isInviting.set(false);
                },
              });
            }
          },
        });
    } else {
      this.inviteForm.markAllAsTouched();
    }
  }

  usernameValidator(): AsyncValidatorFn {
    const debouncedCheckUsername = debounce(
      (
        value: string,
        callback: (
          result:
            | { invalidUsername: boolean }
            | { usernameNotFound: boolean }
            | null,
        ) => void,
      ) => {
        if (value === this.currentUsername) {
          this.isUserLoading.set(false);
          return callback({ invalidUsername: true });
        }

        this.usersService
          .userExists(value)
          .pipe(
            map((exists) => {
              this.isUserLoading.set(false);
              return callback(exists ? null : { usernameNotFound: true });
            }),
            catchError(() => {
              this.isUserLoading.set(false);
              callback(null);
              return of(null);
            }),
          )
          .subscribe();
      },
      500,
    );

    return (control) => {
      if (!control.value || control.value.trim() === '') {
        return of(null);
      }

      this.isUserLoading.set(true);

      return new Observable<any>((observer) => {
        debouncedCheckUsername(control.value, (result) => {
          observer.next(result);
          observer.complete();
        });
      });
    };
  }
}
