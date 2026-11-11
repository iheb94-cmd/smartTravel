import { HttpErrorResponse } from '@angular/common/http';

export function getErrorMessage(err: HttpErrorResponse): string {
  if (err.status === 0) return 'Impossible de joindre le serveur.';
  if (err.status === 401) return 'Session expirée, reconnectez-vous.';
  if (err.status === 403) return 'Accès refusé.';
  return err.error?.message ?? 'Une erreur est survenue.';
}