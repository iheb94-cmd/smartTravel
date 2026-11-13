
import { Routes } from '@angular/router';

import { Login } from './auth/login/login';
import { Updatepassword } from './auth/update-password/update-password';
import { RoleGuard } from './guards/auth-guard';

// Gestion des agents (ADMIN)
import { AgentList } from './agent/agent-list/agent-list';
import { AgentForm } from './agent/agent-form/agent-form';

// Gestion des vols (AGENT)
import { VolList } from './agent/vols/vol-list';
import { VolForm } from './agent/vols/vol-form';

// Gestion des hôtels (AGENT)
import { HotelList } from './agent/hotels/hotel-list';
import { HotelForm } from './agent/hotels/hotel-form';

// Gestion des circuits (AGENT)
import { CircuitList } from './agent/circuits/circuit-list';
import { CircuitForm } from './agent/circuits/circuit-form';

// Gestion des offres (AGENT)
import { OffreList } from './agent/offres/offre-list';
import { OffreForm } from './agent/offres/offre-form';
import { OffreDetails } from './agent/offres/offre-details';
import { DashboardComponent } from './dashboard/dashboard/dashboard';

export const routes: Routes = [

  // AUTHENTIFICATION
  { path: 'login', component: Login },
  { path: 'updatepassword', component: Updatepassword },

  // ADMIN : GESTION DES AGENTS
  {
    path: 'agents',
    component: AgentList,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'agents/add',
    component: AgentForm,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'agents/edit/:id',
    component: AgentForm,
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN'] }
  },

  // AGENT : GESTION DES VOLS
  {
    path: 'vols',
    component: VolList,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },
  {
    path: 'vols/nouveau',
    component: VolForm,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },
  {
    path: 'vols/:id/modifier',
    component: VolForm,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },

  // AGENT : GESTION DES HÔTELS
  {
    path: 'hotels',
    component: HotelList,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },
  {
    path: 'hotels/nouveau',
    component: HotelForm,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },
  {
    path: 'hotels/:id/modifier',
    component: HotelForm,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },

  // AGENT : GESTION DES CIRCUITS
  {
    path: 'circuits',
    component: CircuitList,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },
  {
    path: 'circuits/nouveau',
    component: CircuitForm,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },
  {
    path: 'circuits/:id/modifier',
    component: CircuitForm,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN' ] }
  },

  // AGENT : GESTION DES OFFRES
  {
    path: 'offres',
    component: OffreList,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },
  {
    path: 'offres/nouveau',
    component: OffreForm,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },
  {
    path: 'offres/:id',
    component: OffreDetails,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },
  {
    path: 'offres/:id/modifier',
    component: OffreForm,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [RoleGuard],
    data: { roles: ['AGENT_AGENCE','ADMIN'] }
  },

  // REDIRECTIONS
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }

];
