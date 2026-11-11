import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot,CanActivate, Router}from '@angular/router';
import { AuthService } from '../services/auth';
@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {

    const expectedRoles = route.data['roles'];
    const userRole = this.authService.getUserRole();

    if (!this.authService.getToken()) {
      this.router.navigate(['/login']);
      return false;
    }

    if (expectedRoles.includes(userRole)) {
      return true;
    }

    this.router.navigate(['/']);
    return false;
  }
}