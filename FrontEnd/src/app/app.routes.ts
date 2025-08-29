import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login';
import { Register } from './features/auth/register/register';
import { LandingPage } from './features/landing-page/landing-page';
import { DoctorDash } from './components/doctor-dash/doctor-dash';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'register', component: Register },
  { path: 'login', component: Login },
  { path: 'home', component: LandingPage},
  { path: 'doctorDash', component: DoctorDash},
  { path: '**', redirectTo: 'login', pathMatch: 'full' },
];
