// src/app/core/services/authentication.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { environment } from '../../../environments/environment';
import {AuthStoreService} from "./auth-store.service";
import {AuthResponse, LoginRequest} from "./interfaces";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private readonly API_URL = `http://localhost:8080/api/auth`;

  constructor(
    private http: HttpClient,
    private authStore: AuthStoreService
  ) {}

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials)
      .pipe(
        tap(response => this.authStore.setCurrentUser(response))
      );
  }

  logout(): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/logout`, {})
      .pipe(
        tap(() => this.authStore.setCurrentUser(null))
      );
  }
}
