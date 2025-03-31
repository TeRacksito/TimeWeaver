import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import {
  LoginCredentials,
  LoginResponse,
  RegisterCredentials,
} from '../types/auth';
import { User } from '../types/user';
import { toApiUrl } from '../utils/api';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private token: LoginResponse['accessToken'] | null = null;
  public isAuthenticated$ = new BehaviorSubject<boolean>(false);

  constructor() {
    const storedToken = localStorage.getItem('token');
    if (storedToken) {
      this.token = storedToken;
      this.isAuthenticated$.next(true);
    }
  }

  login(credentials: LoginCredentials): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(toApiUrl('/auth/login'), credentials)
      .pipe(
        tap((response) => {
          this.token = response.accessToken;
          localStorage.setItem('token', this.token);
          this.isAuthenticated$.next(true);
        }),
      );
  }

  register(credentials: RegisterCredentials) {
    return this.http
      .post<User>(toApiUrl('/users'), credentials)
      .pipe(tap((response) => {}));
  }

  isUsernameAvailable(username: string): Observable<boolean> {
    return this.http.get<boolean>(
      toApiUrl(`/users/available?username=${username}`),
    );
  }

  logout(): void {
    this.token = null;
    localStorage.removeItem('token');
    this.isAuthenticated$.next(false);
  }

  cleanUp(): void {
    this.logout();
  }

  getToken(): LoginResponse['accessToken'] | null {
    return this.token;
  }

  isLoggedIn(): boolean {
    return !!this.token && !this.isTokenExpired();
  }

  isTokenExpired(): boolean {
    if (!this.token) {
      return true;
    }

    try {
      const payload = this.token.split('.')[1];
      const decodedPayload = JSON.parse(atob(payload));
      return decodedPayload.exp * 1000 < Date.now();
    } catch (error) {
      return true;
    }
  }

  getUsername(): string | null {
    if (!this.token) {
      return null;
    }

    try {
      const payload = this.token.split('.')[1];
      const decodedPayload = JSON.parse(atob(payload));
      return decodedPayload.sub || null;
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  }

  getUserId(): number | null {
    if (!this.token) {
      return null;
    }

    try {
      const payload = this.token.split('.')[1];
      const decodedPayload = JSON.parse(atob(payload));
      return decodedPayload.userId || null;
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  }
}
