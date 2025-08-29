import { CommonModule } from '@angular/common';
import { Component, NgModule } from '@angular/core';
import { Router, RouterLink, } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [CommonModule, RouterLink],
  templateUrl: './login.html',
})
export class Login {

  constructor(private router: Router) {}
  loginForm = {
    email: '',
    password: ''
  }
  login() {
    console.log(
      "logging in with email: " + this.loginForm.email + " and password: " + this.loginForm.password
    )
  }

}


