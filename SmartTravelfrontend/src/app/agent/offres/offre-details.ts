
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

import { OffreService } from './offre.service';
import { Offre } from './offre.model';

@Component({
  selector: 'app-offre-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './offre-details.html',
  styleUrl: './offre-details.css'
})
export class OffreDetails implements OnInit {

  offre: Offre | null = null;
  loading = false;
  errorMessage = '';

  constructor(
    private offreService: OffreService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id || isNaN(id)) {
      this.errorMessage = 'Identifiant de l’offre invalide.';
      return;
    }

    this.loadOffre(id);
  }

  // Charger les détails de l'offre
  
loadOffre(id: number): void {
  this.loading = true;
  this.errorMessage = '';

  console.log('Chargement de l’offre ID :', id);

  this.offreService.getById(id).subscribe({
    next: (data) => {
      console.log('Données reçues du backend :', data);

      this.offre = data;
      this.loading = false;

      console.log('État loading :', this.loading);
      console.log('Offre chargée :', this.offre);
    },
    error: (error) => {
      console.error('Erreur chargement offre :', error);

      this.errorMessage = 'Impossible de charger les détails de cette offre.';
      this.loading = false;
    }
  });
}


  // Modifier l'offre
  modifierOffre(): void {
    if (this.offre?.idOffre) {
      this.router.navigate(['/offres', this.offre.idOffre, 'modifier']);
    }
  }

  // Retour à la liste
  retourListe(): void {
    this.router.navigate(['/offres']);
  }
}
