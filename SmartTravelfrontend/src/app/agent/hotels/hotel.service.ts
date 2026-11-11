
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Hotel } from './hotel.model';

@Injectable({
  providedIn: 'root'
})
export class HotelService {

  private http = inject(HttpClient);

  private url = 'http://localhost:8081/agent/hotels';

  // Afficher tous les hôtels ou rechercher
  getAll(q?: string): Observable<Hotel[]> {

    let params = new HttpParams();

    if (q && q.trim()) {
      params = params.set('q', q.trim());
    }

    return this.http.get<Hotel[]>(this.url, { params });
  }

  // Consulter un hôtel
  getById(id: number): Observable<Hotel> {
    return this.http.get<Hotel>(`${this.url}/${id}`);
  }

  // Ajouter un hôtel
  create(hotel: Hotel): Observable<Hotel> {
    return this.http.post<Hotel>(this.url, hotel);
  }

  // Modifier un hôtel
  update(id: number, hotel: Hotel): Observable<Hotel> {
    return this.http.put<Hotel>(`${this.url}/${id}`, hotel);
  }

  // Supprimer un hôtel
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
