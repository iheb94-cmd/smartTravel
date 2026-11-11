
export type StatutOffre =
  | 'DISPONIBLE'
  | 'COMPLET'
  | 'ANNULE'
  | 'INACTIF';

export interface Hotel {
  idHotel?: number;
  nom: string;
  pays: string;
  ville: string;
  adresse: string;
  categorie: number;
  prixParNuit: number;
  typeHebergement: string;
  capacite: number;
  nombreChambres: number;
  services?: string;
  description?: string;
  image?: string;
  statut: StatutOffre;
  agentId?: number;
  agentNom?: string;
}
