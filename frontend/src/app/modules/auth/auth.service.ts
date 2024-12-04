// src/app/core/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';

export interface SignUpRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface AuthResponse {
  id: number;
  email: string;
  role: string;
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<AuthResponse | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  private readonly API_URL = `http://localhost:8080/api/auth`;

  constructor(private http: HttpClient) {
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      this.currentUserSubject.next(JSON.parse(savedUser));
    }
  }

  signUp(signUpData: SignUpRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/register`, signUpData)
      .pipe(
        tap(response => {
          this.currentUserSubject.next(response);
          localStorage.setItem('currentUser', JSON.stringify(response));
        })
      );
  }

  signIn(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, { email, password })
      .pipe(
        tap(response => {
          this.currentUserSubject.next(response);
          localStorage.setItem('currentUser', JSON.stringify(response));
        })
      );
  }

  signOut(): void {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }

  get currentUser(): AuthResponse | null {
    return this.currentUserSubject.value;
  }
}
