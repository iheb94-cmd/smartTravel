export type StatutOffre = 'DISPONIBLE' | 'COMPLET' | 'ANNULE' | 'INACTIF';

export interface Vol {
  idVol?: number;
  numeroVol: string;
  villeDepart: string;
  villeArrivee: string;
  dateDepart: string;   // ISO : 2026-12-15T08:30:00
  dateArrivee: string;
  compagnie: string;
  prix: number;
  classe: string;
  nombrePlaces: number;
  bagageInclus: boolean;
  description?: string;
  image?: string;
  statut: StatutOffre;
  agentId?: number;
  agentNom?: string;
}