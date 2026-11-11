import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Vol } from './vol.model';

@Injectable({ providedIn: 'root' })
export class VolService {
  private http = inject(HttpClient);
  private url = 'http://localhost:8081/agent/vols';

  getAll(q?: string): Observable<Vol[]> {
    let params = new HttpParams();
    if (q && q.trim()) params = params.set('q', q.trim());
    return this.http.get<Vol[]>(this.url, { params });
  }

  getById(id: number): Observable<Vol> {
    return this.http.get<Vol>(`${this.url}/${id}`);
  }

  create(vol: Vol): Observable<Vol> {
    return this.http.post<Vol>(this.url, vol);
  }

  update(id: number, vol: Vol): Observable<Vol> {
    return this.http.put<Vol>(`${this.url}/${id}`, vol);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}