
export interface Offre {
  idOffre?: number;
  nomOffre: string;

  volId: number;
  hotelId: number;
  circuitId: number;

  prixTotal: number;
  dateDebut: string;
  dateFin: string;
  nombrePlaces: number;

  statut: 'DISPONIBLE' | 'COMPLET' | 'ANNULE' | 'INACTIF';

  // Informations retournées par le backend
  nomVol?: string;
  nomHotel?: string;
  nomCircuit?: string;
  agentNom?: string;
  agentId?: number;
}
