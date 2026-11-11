import { Routes } from '@angular/router';
import { Login } from './auth/login/login';
import { Updatepassword } from './auth/update-password/update-password';
import { agentGuard } from './agent/agent.guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'updatepassword', component: Updatepassword },

  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
  path: 'agent',
  canActivate: [agentGuard],
  loadChildren: () => import('./agent/agent.routes').then(m => m.AGENT_ROUTES),
},
  { path: '**', redirectTo: 'login' }
];