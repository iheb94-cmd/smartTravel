import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize, forkJoin } from 'rxjs';

import { OffreService } from './offre.service';
import { Offre } from './offre.model';

import { VolService } from '../vols/vol.service';
import { HotelService } from '../hotels/hotel.service';
import { CircuitService } from '../circuits/circuit.service';

@Component({
  selector: 'app-offre-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './offre-details.html',
  styleUrl: './offre-details.css'
})
export class OffreDetails implements OnInit {

  offre = signal<Offre | null>(null);
  vol = signal<any | null>(null);
  hotel = signal<any | null>(null);
  circuit = signal<any | null>(null);

  loading = signal(false);
  errorMessage = signal('');

  constructor(
    private offreService: OffreService,
    private volService: VolService,
    private hotelService: HotelService,
    private circuitService: CircuitService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (!id || isNaN(id)) {
      this.errorMessage.set('Identifiant de l’offre invalide.');
      return;
    }
    this.loadOffre(id);
  }

  loadOffre(id: number): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.offreService.getById(id).subscribe({
      next: (offre) => {
        this.offre.set(offre);

        forkJoin({
          vol: this.volService.getById(offre.volId),
          hotel: this.hotelService.getById(offre.hotelId),
          circuit: this.circuitService.getById(offre.circuitId)
        })
        .pipe(finalize(() => this.loading.set(false)))
        .subscribe({
          next: (res) => {
            this.vol.set(res.vol);
            this.hotel.set(res.hotel);
            this.circuit.set(res.circuit);
          },
          error: (err) => {
            console.error('Erreur détails associés :', err);
            this.errorMessage.set('Impossible de charger le vol, l’hôtel ou le circuit.');
          }
        });
      },
      error: (err) => {
        console.error('Erreur chargement offre :', err);
        this.errorMessage.set('Impossible de charger les détails de cette offre.');
        this.loading.set(false);
      }
    });
  }

  modifierOffre(): void {
    const o = this.offre();
    if (o?.idOffre) this.router.navigate(['/offres', o.idOffre, 'modifier']);
  }

  retourListe(): void {
    this.router.navigate(['/offres']);
  }
}