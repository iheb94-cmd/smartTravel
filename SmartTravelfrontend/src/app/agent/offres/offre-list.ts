import { Component, OnInit, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { OffreService } from './offre.service';
import { Offre } from './offre.model';

@Component({
  selector: 'app-offre-list',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './offre-list.html',
  styleUrl: './offre-list.css'
})
export class OffreList implements OnInit {

  offres = signal<Offre[]>([]);
  loading = signal(false);
  errorMessage = signal('');
  searchTerm = signal('');

  filteredOffres = computed(() => {
    const term = this.normalize(this.searchTerm());
    if (!term) {
      return this.offres();
    }

    const words = term.split(/\s+/);

    return this.offres().filter(o => {
      const haystack = this.normalize(
        [
          o.nomOffre,
          o.nomVol,
          o.nomHotel,
          o.nomCircuit,
          o.prixTotal,
          o.dateDebut,
          o.dateFin,
          o.nombrePlaces,
          o.statut
        ]
          .filter(v => v !== null && v !== undefined)
          .join(' ')
      );
      return words.every(w => haystack.includes(w));
    });
  });

  constructor(private offreService: OffreService) {}

  ngOnInit(): void {
    this.loadOffres();
  }

  // Charger toutes les offres
  loadOffres(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.offreService.getAll().subscribe({
      next: (data) => {
        this.offres.set(data);
        this.loading.set(false);
      },
      error: (error) => {
        console.error('Erreur lors du chargement des offres', error);
        this.errorMessage.set('Impossible de charger les offres.');
        this.loading.set(false);
      }
    });
  }

  onSearch(value: string): void {
    this.searchTerm.set(value);
  }

  private normalize(value: string | number | null | undefined): string {
    return (value ?? '')
      .toString()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .toLowerCase()
      .trim();
  }

  // Supprimer une offre
  deleteOffre(id: number): void {
    const confirmation = confirm('Voulez-vous vraiment supprimer cette offre ?');

    if (!confirmation) {
      return;
    }

    this.offreService.delete(id).subscribe({
      next: () => {
        this.offres.update(
          liste => liste.filter(offre => offre.idOffre !== id)
        );
      },
      error: (error) => {
        console.error('Erreur lors de la suppression', error);
        this.errorMessage.set('Impossible de supprimer cette offre.');
      }
    });
  }
}