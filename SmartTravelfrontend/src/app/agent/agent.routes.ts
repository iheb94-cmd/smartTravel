
import { Routes } from '@angular/router';

import { RoleGuard } from '../guards/auth-guard';

import { VolList } from './vols/vol-list';
import { VolForm } from './vols/vol-form';

import { HotelList } from './hotels/hotel-list';
import { HotelForm } from './hotels/hotel-form';

import { CircuitList } from './circuits/circuit-list';
import { CircuitForm } from './circuits/circuit-form';

import { OffreList } from './offres/offre-list';
import { OffreForm } from './offres/offre-form';
import { OffreDetails } from './offres/offre-details';

const agentOnly = {
  canActivate: [RoleGuard],
  data: { roles: ['AGENT_AGENCE'] }
};

export const AGENT_ROUTES: Routes = [

  // Gestion des vols
  { path: 'vols', component: VolList, ...agentOnly },
  { path: 'vols/nouveau', component: VolForm, ...agentOnly },
  { path: 'vols/:id/modifier', component: VolForm, ...agentOnly },

  // Gestion des hôtels
  { path: 'hotels', component: HotelList, ...agentOnly },
  { path: 'hotels/nouveau', component: HotelForm, ...agentOnly },
  { path: 'hotels/:id/modifier', component: HotelForm, ...agentOnly },

  // Gestion des circuits
  { path: 'circuits', component: CircuitList, ...agentOnly },
  { path: 'circuits/nouveau', component: CircuitForm, ...agentOnly },
  { path: 'circuits/:id/modifier', component: CircuitForm, ...agentOnly },

  // Gestion des offres
  { path: 'offres', component: OffreList, ...agentOnly },
  { path: 'offres/nouveau', component: OffreForm, ...agentOnly },
  { path: 'offres/:id', component: OffreDetails, ...agentOnly },
  { path: 'offres/:id/modifier', component: OffreForm, ...agentOnly }

];
