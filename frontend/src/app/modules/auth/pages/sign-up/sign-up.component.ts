import { Component, OnInit } from '@angular/core';
import { AngularSvgIconModule } from 'angular-svg-icon';
import { RouterLink, Router } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
  AbstractControl,
  ValidationErrors
} from '@angular/forms';
import { ButtonComponent } from 'src/app/shared/components/button/button.component';
import { CommonModule } from '@angular/common';
import {AuthService} from "../../auth.service";

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss'],
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterLink, AngularSvgIconModule, ButtonComponent],
})
export class SignUpComponent implements OnInit {
  signUpForm: FormGroup;
  showPassword = false;
  passwordStrength = 0;
  isLoading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.signUpForm = this.fb.nonNullable.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required],
      acceptTerms: [false, Validators.requiredTrue]
    }, {
      validators: [this.passwordMatchValidator]
    });
  }

  ngOnInit(): void {
    // Watch password changes to update strength meter
    this.signUpForm.get('password')?.valueChanges.subscribe(password => {
      this.updatePasswordStrength(password);
    });
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  updatePasswordStrength(password: string): void {
    let strength = 0;
    if (password.length >= 8) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/[0-9]/.test(password)) strength++;
    if (/[^A-Za-z0-9]/.test(password)) strength++;
    this.passwordStrength = strength;
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');

    if (password && confirmPassword && password.value !== confirmPassword.value) {
      return { passwordMismatch: true };
    }
    return null;
  }

  async signUpWithGoogle() {
    // Implement Google Sign-in logic here
    console.log('Google sign-up clicked');
  }

  onSubmit() {
    if (this.signUpForm.invalid) {
      return;
    }

    this.isLoading = true;
    this.error = null;

    const signUpData = {
      email: this.signUpForm.value.email,
      password: this.signUpForm.value.password,
      firstName: 'Test',
      lastName: '123'
    };

    this.authService.signUp(signUpData).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']).then(r => {});
      },
      error: (err) => {
        this.error = err.error?.message || 'An error occurred during sign up';
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  get emailErrors() {
    const email = this.signUpForm.get('email');
    if (email?.errors && email.touched) {
      if (email.errors['required']) return 'Email is required';
      if (email.errors['email']) return 'Please enter a valid email';
    }
    return null;
  }

  get passwordErrors() {
    const password = this.signUpForm.get('password');
    if (password?.errors && password.touched) {
      if (password.errors['required']) return 'Password is required';
      if (password.errors['minlength']) return 'Password must be at least 8 characters';
    }
    return null;
  }
}
