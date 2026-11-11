
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Offre } from './offre.model';

@Injectable({
  providedIn: 'root'
})
export class OffreService {

  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8081/agent/offres';

  // Récupérer toutes les offres
  getAll(): Observable<Offre[]> {
    return this.http.get<Offre[]>(this.apiUrl);
  }

  // Récupérer une offre par ID
  getById(id: number): Observable<Offre> {
    return this.http.get<Offre>(`${this.apiUrl}/${id}`);
  }

  // Ajouter une offre
  create(offre: Offre): Observable<Offre> {
    return this.http.post<Offre>(this.apiUrl, offre);
  }

  // Modifier une offre
  update(id: number, offre: Offre): Observable<Offre> {
    return this.http.put<Offre>(`${this.apiUrl}/${id}`, offre);
  }

  // Supprimer une offre
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
