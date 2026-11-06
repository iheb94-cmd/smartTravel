import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Updatepassword } from './auth/update-password/update-password';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'updatepassword', component: Updatepassword },

  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];