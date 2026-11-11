import { Routes } from '@angular/router';

import { Login } from './auth/login/login';
import { Updatepassword } from './auth/update-password/update-password';
import { RoleGuard } from './guards/auth-guard';

import { AgentList } from './agent/agent-list/agent-list';
import { AgentForm } from './agent/agent-form/agent-form';
import { AGENT_ROUTES } from './agent/agent.routes';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'updatepassword', component: Updatepassword },

  // Admin : gestion des comptes agents (Iheb)
  { path: 'agents', component: AgentList, canActivate: [RoleGuard], data: { roles: ['ADMIN'] } },
  { path: 'agents/add', component: AgentForm, canActivate: [RoleGuard], data: { roles: ['ADMIN'] } },
  { path: 'agents/edit/:id', component: AgentForm, canActivate: [RoleGuard], data: { roles: ['ADMIN'] } },

  // Agent : vols, hôtels, circuits (Karim)
  ...AGENT_ROUTES,

  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' },
];