import { CommonModule } from '@angular/common';
import { Component, inject, signal, WritableSignal } from '@angular/core';
import {
  AsyncValidatorFn,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { getApiError } from '../../utils/api';
import { UsersService } from '../../services/users.service';
import { of, map, catchError, Observable } from 'rxjs';
import { debounce } from '../../utils/debounce';

@Component({
  selector: 'app-register',
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    TranslatePipe,
    FontAwesomeModule,
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private fb = inject(FormBuilder);
  private usersService = inject(UsersService);

  readonly USERNAME_MIN_LENGTH = 4;
  readonly USERNAME_MAX_LENGTH = 15;
  readonly USERNAME_PATTERN = /^[a-zA-Z0-9_]*$/;

  readonly PASSWORD_MIN_LENGTH = 8;
  readonly PASSWORD_MAX_LENGTH = 30;
  readonly PASSWORD_PATTERN =
    /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[@$!%*?&]).*$/;

  loginForm: FormGroup;
  isLoading = signal(false);
  isUsernameLoading = signal(false);
  errorMessage: WritableSignal<string | null> = signal(null);

  constructor() {
    this.loginForm = this.fb.group({
      username: [
        '',
        [
          Validators.required,
          Validators.minLength(this.USERNAME_MIN_LENGTH),
          Validators.maxLength(this.USERNAME_MAX_LENGTH),
          Validators.pattern(this.USERNAME_PATTERN),
        ],
        [this.usernameExistsValidator()],
      ],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(this.PASSWORD_MIN_LENGTH),
          Validators.maxLength(this.PASSWORD_MAX_LENGTH),
          Validators.pattern(this.PASSWORD_PATTERN),
        ],
      ],
    });
  }

  get usernameControl() {
    return this.loginForm.get('username');
  }
  get passwordControl() {
    return this.loginForm.get('password');
  }

  register() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);

    const { username, password } = this.loginForm.value;

    this.authService.register({ username, password }).subscribe({
      complete: () => {
        this.isLoading.set(false);

        this.authService.login({ username, password }).subscribe({
          next: () => {
            this.isLoading.set(false);
            this.router.navigate([
              this.route.snapshot.queryParams['returnUrl'] || '/',
            ]);
          },
          error: (err) => {
            this.isLoading.set(false);
            console.log(err);

            this.errorMessage.set(getApiError(err, 'Login failed'));
          },
        });

        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.isLoading.set(false);
        console.log(err);

        this.errorMessage.set(getApiError(err, 'Registration failed'));
      },
    });
  }

  usernameExistsValidator(): AsyncValidatorFn {
    const debouncedCheckUsername = debounce(
      (
        value: string,
        callback: (result: { usernameExists: boolean } | null) => void,
      ) => {
        this.usersService
          .userExists(value)
          .pipe(
            map((exists) => {
              this.isUsernameLoading.set(false);
              callback(exists ? { usernameExists: true } : null);
            }),
            catchError(() => {
              this.isUsernameLoading.set(false);
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

      this.isUsernameLoading.set(true);

      return new Observable<any>((observer) => {
        debouncedCheckUsername(control.value, (result) => {
          observer.next(result);
          observer.complete();
        });
      });
    };
  }
}
