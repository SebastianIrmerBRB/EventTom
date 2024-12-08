import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthResponse, RegisterRequest, EmployeeRegisterRequest } from './interfaces';
import { AuthStoreService } from './auth-store.service';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  private readonly API_URL = `http://localhost:8080/api/registration`;

  constructor(
    private http: HttpClient,
    private authStore: AuthStoreService
  ) {}

  registerCustomer(registerData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/customer`, registerData)
      .pipe(
        tap(response => this.authStore.setCurrentUser(response))
      );
  }

  registerEmployee(registerData: EmployeeRegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/employee`, registerData);
  }
}
