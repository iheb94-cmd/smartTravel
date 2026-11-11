import { Routes } from '@angular/router';
import { VolList } from './vols/vol-list';
import { VolForm } from './vols/vol-form';
import { HotelList } from './hotels/hotel-list';
import { CircuitList } from './circuits/circuit-list';

export const AGENT_ROUTES: Routes = [
  { path: '', redirectTo: 'vols', pathMatch: 'full' },
  { path: 'vols', component: VolList },
  { path: 'vols/nouveau', component: VolForm },
  { path: 'vols/:id/modifier', component: VolForm },
  { path: 'hotels', component: HotelList },
  { path: 'circuits', component: CircuitList },
];