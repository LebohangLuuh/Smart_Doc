import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  imports: [RouterLink, CommonModule, ReactiveFormsModule],
  templateUrl: './register.html',
})
export class Register {
  registerForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  

  private apiUrl = 'http://localhost:8080/api/v1/users';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private http: HttpClient
  ) {
    this.registerForm = this.formBuilder.group({
      fullname: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      role: ['patient', Validators.required],
      practiceNumber: [''], // Will be validated conditionally
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  // Custom validator to check if passwords match
  passwordMatchValidator(form: FormGroup) {
    const password = form.get('password');
    const confirmPassword = form.get('confirmPassword');
    
    if (password && confirmPassword && password.value !== confirmPassword.value) {
      confirmPassword.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    } else {
      if (confirmPassword && confirmPassword.errors) {
        delete confirmPassword.errors['passwordMismatch'];
        if (Object.keys(confirmPassword.errors).length === 0) {
          confirmPassword.setErrors(null);
        }
      }
      return null;
    }
  }

  isDoctorSelected(): boolean {
    return this.registerForm.get('role')?.value === 'doctor';
  }

register() {
  if (this.registerForm.valid) {
    if (this.isDoctorSelected() && !this.registerForm.get('practiceNumber')?.value) {
      Swal.fire({
        icon: 'error',
        title: 'Validation Error',
        text: 'Practice number is required for doctors',
      });
      return;
    }

    this.isLoading = true;
    const formData = this.registerForm.value;

    this.http.post<any>(`${this.apiUrl}/registerUser`, formData)
      .subscribe({
        next: (response) => {
          this.isLoading = false;

          if (response.success) {
            Swal.fire({
              icon: 'success',
              title: 'Registration Successful',
              text: 'You can now login to your account.',
              timer: 2000,
              showConfirmButton: false
            });

            setTimeout(() => {
              this.router.navigate(['/login']);
            }, 2000);
          } else {
            Swal.fire({
              icon: 'error',
              title: 'Registration Failed',
              text: response.message || 'Something went wrong.',
            });
          }
        },
        error: (error: HttpErrorResponse) => {
          this.isLoading = false;

          let message = 'Registration failed. Please try again.';
          if (error.status === 400 && error.error?.message) {
            if (error.error.data && typeof error.error.data === 'object') {
              message = Object.values(error.error.data).join(', ');
            } else {
              message = error.error.message;
            }
          } else if (error.status === 0) {
            message = 'Unable to connect to server. Please check your internet connection.';
          }

          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: message,
          });
        }
      });
  } else {
    this.markFormGroupTouched();
    Swal.fire({
      icon: 'warning',
      title: 'Invalid Form',
      text: 'Please fill all required fields correctly.',
    });
  }
}


  // Helper method to mark all form fields as touched (for validation display)
  private markFormGroupTouched() {
    Object.keys(this.registerForm.controls).forEach(key => {
      const control = this.registerForm.get(key);
      control?.markAsTouched();
    });
  }

  // Helper methods for template validation display
  isFieldInvalid(fieldName: string): boolean {
    const field = this.registerForm.get(fieldName);
    return !!(field && field.invalid && field.touched);
  }

  getFieldError(fieldName: string): string {
    const field = this.registerForm.get(fieldName);
    if (field && field.errors && field.touched) {
      if (field.errors['required']) {
        return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} is required`;
      }
      if (field.errors['email']) {
        return 'Please enter a valid email address';
      }
      if (field.errors['minlength']) {
        return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} must be at least ${field.errors['minlength'].requiredLength} characters`;
      }
      if (field.errors['passwordMismatch']) {
        return 'Passwords do not match';
      }
    }
    return '';
  }
}