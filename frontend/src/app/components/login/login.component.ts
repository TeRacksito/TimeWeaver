import { CommonModule } from '@angular/common';
import { Component, inject, signal, WritableSignal } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { AuthService } from '../../services/auth.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    TranslatePipe,
    FontAwesomeModule,
  ],
  templateUrl: './login.component.html',
})
export class LoginComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private fb = inject(FormBuilder);

  readonly USERNAME_MIN_LENGTH = 4;
  readonly PASSWORD_MIN_LENGTH = 8;

  loginForm: FormGroup;
  isLoading = signal(false);
  errorMessage: WritableSignal<string | null> = signal(null);

  constructor() {
    this.loginForm = this.fb.group({
      username: [
        '',
        [Validators.required, Validators.minLength(this.USERNAME_MIN_LENGTH)],
      ],
      password: [
        '',
        [Validators.required, Validators.minLength(this.PASSWORD_MIN_LENGTH)],
      ],
    });
  }

  get usernameControl() {
    return this.loginForm.get('username');
  }
  get passwordControl() {
    return this.loginForm.get('password');
  }

  login() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set(null);

    const { username, password } = this.loginForm.value;

    this.authService.login({ username, password }).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate([
          this.route.snapshot.queryParams['returnUrl'] || '/',
        ]);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.message || 'Login failed');
      },
    });
  }
}
