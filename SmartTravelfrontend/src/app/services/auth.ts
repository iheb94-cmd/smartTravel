
import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginResponse, UpdatePasswordDto, User } from '../user';
@Injectable({
  providedIn: 'root',
})

export class AuthService {
  private url: string = 'http://localhost:8081/auth';
  isLoggedIn = signal(false);
  constructor(private http: HttpClient, private router: Router) {
  const token = localStorage.getItem('token');
  this.isLoggedIn.set(!!token);
}

  login(email: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.url + `/login`, { email, password });
  }

  updatePassword(dto: UpdatePasswordDto): Observable<void> {
    return this.http.put<void>(this.url + `/updatepassword`, dto);
  }
  saveUserData(data: LoginResponse) {
    this.isLoggedIn.set(true);
    localStorage.setItem('USER_INFO', JSON.stringify(data.user))
    localStorage.setItem('token', data.token)
  }
  logout() {
    this.isLoggedIn.set(false);
    localStorage.clear();
    this.router.navigate(['/login']);
  }
  getUserRole(): string | null {
    const userInfoAsString = localStorage.getItem('USER_INFO');
    if (userInfoAsString !== null) {
      const user: User = JSON.parse(userInfoAsString);
      return user.role;
    }
    return null;
  }
  isAgent(): boolean {
    const role=this.getUserRole();
    if(role!=null){
      return role ==='AGENT_AGENCE';
    }
    return false;
  }
  isAdmin(): boolean {
    const role=this.getUserRole();
    if(role!=null){
      return role ==='ADMIN';
    }
    return false;
  }
getCurrentUserEmail(): string | null {
    const userInfoAsString = localStorage.getItem('USER_INFO');
    if (userInfoAsString !== null) {
      const user: User = JSON.parse(userInfoAsString);
      return user.email;
    }   
    return null;
  }
  getConnectedUserId(): number {
    const userInfoAsString = localStorage.getItem('USER_INFO');
    if (userInfoAsString !== null) {
      const user: User = JSON.parse(userInfoAsString);
      return user.id
    }
    return -1;
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
  
}

