import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Login } from './auth/login/login';
import { NavbarComponent } from './navbar/navbar/navbar';

@Component({
  imports: [RouterOutlet, NavbarComponent],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly title = signal('SmartTravelfrontend');
  isLoggedInUser(): boolean {
    const token = localStorage.getItem('token');
    return !!token;
  }
}
