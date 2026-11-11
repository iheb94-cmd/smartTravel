
export type StatutOffre =
  | 'DISPONIBLE'
  | 'COMPLET'
  | 'ANNULE'
  | 'INACTIF';

export interface Circuit {
  idCircuit?: number;
  titre: string;
  pays: string;
  villeDepart: string;
  destinations?: string;
  dateDepart: string;
  dateRetour: string;
  duree: number;
  prix: number;
  nombrePlaces: number;
  typeCircuit: string;
  programme?: string;
  servicesInclus?: string;
  description?: string;
  image?: string;
  statut: StatutOffre;
  agentId?: number;
  agentNom?: string;
}
