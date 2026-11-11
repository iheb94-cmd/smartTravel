
import { Component, OnInit, inject, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';

import { Hotel, StatutOffre } from './hotel.model';
import { HotelService } from './hotel.service';
import { getErrorMessage } from '../error-message';

@Component({
  selector: 'app-hotel-list',
  standalone: true,
  imports: [RouterLink, DecimalPipe],
  templateUrl: './hotel-list.html',
  styleUrls: ['./hotel-list.css'],
})
export class HotelList implements OnInit {

  private hotelService = inject(HotelService);

  hotels = signal<Hotel[]>([]);
  loading = signal(false);
  error = signal('');
  success = signal('');
  expandedHotelId = signal<number | null>(null);

toggleDetails(hotel: Hotel): void {
  if (this.expandedHotelId() === hotel.idHotel) {
    this.expandedHotelId.set(null);
  } else {
    this.expandedHotelId.set(hotel.idHotel ?? null);
  }
}

  readonly statutLabels: Record<StatutOffre, string> = {
    DISPONIBLE: 'Disponible',
    COMPLET: 'Complet',
    ANNULE: 'Annulé',
    INACTIF: 'Inactif',
  };

  private search$ = new Subject<string>();

  constructor() {
    this.search$
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        takeUntilDestroyed()
      )
      .subscribe(q => this.load(q));
  }

  ngOnInit(): void {
    this.load();
  }

  // Recherche d'un hôtel
  onSearch(value: string): void {
    this.search$.next(value);
  }

  // Charger les hôtels
  load(q?: string): void {

    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    this.hotelService.getAll(q).subscribe({

      next: (hotels) => {
        this.hotels.set(hotels);
        this.loading.set(false);
      },

      error: (err) => {
        this.error.set(getErrorMessage(err));
        this.loading.set(false);
      }

    });
  }

  // Supprimer un hôtel
  remove(hotel: Hotel): void {

    if (!hotel.idHotel) return;

    const ok = confirm(
      `Voulez-vous vraiment supprimer l'hôtel ${hotel.nom} ?`
    );

    if (!ok) return;

    this.hotelService.delete(hotel.idHotel).subscribe({

      next: () => {

        this.hotels.update(list =>
          list.filter(h => h.idHotel !== hotel.idHotel)
        );

        this.success.set('Hôtel supprimé avec succès.');
        this.error.set('');
      },

      error: (err) => {
        this.error.set(getErrorMessage(err));
      }

    });
  }
}
