export interface AuthResponse {
  id: number;
  email: string;
  roles: string[];
  message: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface EmployeeRegisterRequest extends RegisterRequest {
  roles: string[];
}
