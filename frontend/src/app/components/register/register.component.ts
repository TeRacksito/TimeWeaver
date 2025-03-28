import { CommonModule } from '@angular/common';
import { Component, inject, signal, WritableSignal } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

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
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.isLoading.set(false);
        console.log(err);

        this.errorMessage.set(err.error?.message || 'Registration failed');
      },
    });
  }
}
