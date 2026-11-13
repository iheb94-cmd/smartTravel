import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ProchainDepart {
  idOffre: number;
  nomOffre: string;
  destination: string;
  dateDebut: string;
  prixTotal: number;
  nombrePlaces: number;
  statut: string;
}

export interface DashboardStats {
  totalOffres: number;
  totalCircuits: number;
  totalHotels: number;
  totalVols: number;
  totalUsers: number;
  totalAgents: number;
  totalVoyageurs: number;
  prixMoyenOffre: number;
  placesTotalesOffres: number;
  offresParStatut: Record<string, number>;
  circuitsParStatut: Record<string, number>;
  hotelsParStatut: Record<string, number>;
  volsParStatut: Record<string, number>;
  usersParRole: Record<string, number>;
  circuitsParPays: Record<string, number>;
  offresParAgent: Record<string, number>;
  offresParMois: Record<string, number>;
  prochainsDeparts: ProchainDepart[];
}

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private http = inject(HttpClient);
  private readonly url = 'http://localhost:8081/api/dashboard/stats';

  getStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(this.url);
  }
}