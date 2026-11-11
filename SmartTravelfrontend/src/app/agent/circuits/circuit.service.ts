
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Circuit } from './circuit.model';

@Injectable({
  providedIn: 'root'
})
export class CircuitService {

  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8081/agent/circuits';

  // Afficher tous les circuits ou rechercher
  getAll(q?: string): Observable<Circuit[]> {
    let params = new HttpParams();

    if (q && q.trim()) {
      params = params.set('q', q.trim());
    }

    return this.http.get<Circuit[]>(this.apiUrl, { params });
  }

  // Consulter un circuit
  getById(id: number): Observable<Circuit> {
    return this.http.get<Circuit>(`${this.apiUrl}/${id}`);
  }

  // Ajouter un circuit
  create(circuit: Circuit): Observable<Circuit> {
    return this.http.post<Circuit>(this.apiUrl, circuit);
  }

  // Modifier un circuit
  update(id: number, circuit: Circuit): Observable<Circuit> {
    return this.http.put<Circuit>(
      `${this.apiUrl}/${id}`,
      circuit
    );
  }

  // Supprimer un circuit
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
