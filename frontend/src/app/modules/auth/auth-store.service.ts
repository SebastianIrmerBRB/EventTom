import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import {AuthResponse} from "./interfaces";

@Injectable({
  providedIn: 'root'
})
export class AuthStoreService {
  private currentUserSubject = new BehaviorSubject<AuthResponse | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor() {
    this.loadStoredUser();
  }

  private loadStoredUser(): void {
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      this.currentUserSubject.next(JSON.parse(savedUser));
    }
  }

  setCurrentUser(user: AuthResponse | null): void {
    if (user) {
      localStorage.setItem('currentUser', JSON.stringify(user));
    } else {
      localStorage.removeItem('currentUser');
    }
    this.currentUserSubject.next(user);
  }

  get currentUser(): AuthResponse | null {
    return this.currentUserSubject.value;
  }
}
