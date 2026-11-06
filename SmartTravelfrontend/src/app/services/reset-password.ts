import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UpdatePasswordRequest } from '../user';

@Injectable({
  providedIn: 'root',
})
export class Resetpassword {
  private url = 'http://localhost:8081/pass';

  constructor(private http: HttpClient) {}

 sendCode(email: string): Observable<boolean> {
  return this.http.post<boolean>(
    this.url + '/send-code',
    { email }
  );
}

  updatePassword(request: UpdatePasswordRequest): Observable<boolean> {
    return this.http.post<boolean>(this.url + '/update-password', request);
  }

  changePassword(data: any): Observable<boolean> {
    return this.http.post<boolean>(this.url + '/change-password', data);
  }
}