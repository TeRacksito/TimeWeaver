import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { toApiUrl } from '../utils/api';
import { User } from '../types/user';

@Injectable({
  providedIn: 'root',
})
export class UsersService {
  constructor(private http: HttpClient) {}

  userExists(username: string): Observable<boolean> {
    return this.http.get<boolean>(toApiUrl(`/users/${username}/exists`));
  }

  getUserByUsername(username: string): Observable<any> {
    return this.http.get<User>(toApiUrl(`/users/${username}/user`));
  }
}
