import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private token: string | null = null;
  public isAuthenticated$ = new BehaviorSubject<boolean>(false);

  constructor() {
    const storedToken = localStorage.getItem('token');
    if (storedToken) {
      this.token = storedToken;
      this.isAuthenticated$.next(true);
    }
  }

  login(credentials: {
    username: string;
    password: string;
  }): Observable<{ accessToken: string }> {
    return this.http
      .post<{ accessToken: string }>('/api/v1/auth/login', credentials)
      .pipe(
        tap((response) => {
          this.token = response.accessToken;
          localStorage.setItem('token', this.token);
          this.isAuthenticated$.next(true);
        })
      );
  }

  logout(): void {
    this.token = null;
    localStorage.removeItem('token');
    this.isAuthenticated$.next(false);
  }

  getToken(): string | null {
    return this.token;
  }

  isLoggedIn(): boolean {
    return !!this.token;
  }
}
