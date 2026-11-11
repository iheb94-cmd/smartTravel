
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CreateUserDto} from '../../user';
import { AgentService } from '../../services/agent';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-agent-form',
  standalone: true,
  imports: [FormsModule, MatIconModule],
  templateUrl: './agent-form.html',
  styleUrl: './agent-form.css',
})
export class AgentForm implements OnInit {

  agentData: CreateUserDto = {
    nom: '',
    prenom: '',
    email: '',
    password: '',
    telephone: '',
    role: 'AGENT_AGENCE'
  };

  isEditing = false;
  editingId: number | null = null;
  loading = false;
  errorMessage = '';

 

constructor(
  private agentService: AgentService,
  private route: ActivatedRoute,
  private router: Router,
  private cdr: ChangeDetectorRef
) {}

  ngOnInit(): void {
  this.route.paramMap.subscribe(params => {

    const idParam = params.get('id');

    console.log('ID reçu depuis URL :', idParam);

    if (idParam) {
      this.isEditing = true;
      this.editingId = Number(idParam);

      this.loadAgentData(this.editingId);
    } else {
      this.isEditing = false;
      this.editingId = null;
    }
  });
}

  loadAgentData(id: number): void {
    

  this.agentService.getAllAgents().subscribe({

    next: (agents) => {

      console.log('Agents reçus :', agents);

      const found = agents.find(
        agent => Number(agent.id) === Number(id)
      );

      console.log('Agent trouvé :', found);

      if (found) {

        this.agentData = {
          nom: found.nom || '',
          prenom: found.prenom || '',
          email: found.email || '',
          telephone: found.telephone || '',
          password: '',
          role: 'AGENT_AGENCE'
        };
          this.cdr.markForCheck();  


        console.log('Données du formulaire :', this.agentData);

      } else {
        console.error('Agent introuvable avec ID :', id);
      }
    },

    error: (err) => {
      console.error('Erreur lors du chargement :', err);
    }
  });
}

  saveAgent(): void {

    if (this.isEditing && this.editingId !== null) {

      this.agentService.updateAgent(this.editingId, this.agentData).subscribe({

        next: () => {
          this.router.navigate(['/agents']);
        },

        error: (err) => {
          console.error('Erreur lors de la mise à jour :', err);
        }
      });

    } else {

      this.agentService.createAgent(this.agentData).subscribe({

        next: () => {
          this.router.navigate(['/agents']);
        },

        error: (err) => {
          console.error('Erreur lors de la création :', err);
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/agents']);
  }

  onSubmit(form: NgForm): void {

    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }

    this.saveAgent();
  }
}
