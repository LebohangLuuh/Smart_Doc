import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RegisterRequest {
  fullname: string;
  email: string;
  role: string;
  practiceNumber?: string;
  password: string;
  confirmPassword: string;
}

export interface ApiResponse<T> {
  status_code: number;
  message: string;
  data: T;
  success: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/v1/users';

  constructor(private http: HttpClient) {}

  register(userData: RegisterRequest): Observable<ApiResponse<any>> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<ApiResponse<any>>(`${this.apiUrl}/registerUser`, userData, { headers });
  }

  login(credentials: { email: string; password: string }): Observable<ApiResponse<any>> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.post<ApiResponse<any>>(`${this.apiUrl}/login`, credentials, { headers });
  }
}