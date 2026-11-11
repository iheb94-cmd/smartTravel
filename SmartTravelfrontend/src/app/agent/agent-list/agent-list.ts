
import { Component, OnInit, signal } from '@angular/core';
import { UserDto } from '../../user';
import { AgentService } from '../../services/agent';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-agent-list',
  standalone: true,
  imports: [MatIconModule],
  templateUrl: './agent-list.html',
  styleUrl: './agent-list.css',
})
export class AgentList implements OnInit {

  agents = signal<UserDto[]>([]);
  loading = signal(false);
  errorMessage = signal('');

  constructor(
    private agentService: AgentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadAgents();
  }

  loadAgents(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.agentService.getAllAgents()
      .pipe(
        finalize(() => this.loading.set(false))
      )
      .subscribe({
        next: (data) => {
          console.log('Agents reçus :', data);
          this.agents.set(data);
        },
        error: (err) => {
          console.error('Erreur :', err);
          this.errorMessage.set(
            'Impossible de charger la liste des agents.'
          );
        }
      });
  }

  navigateToAdd(): void {
    this.router.navigate(['/agents/add']);
  }

  navigateToEdit(id: number): void {
    this.router.navigate(['/agents/edit', id]);
  }

  deleteAgent(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet agent ?')) {
      this.agentService.deleteAgent(id).subscribe({
        next: () => this.loadAgents(),
        error: (err) => console.error('Erreur suppression :', err)
      });
    }
  }
}
