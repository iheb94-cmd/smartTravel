import { Component, OnInit, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { DatePipe, DecimalPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { Vol } from './vol.model';
import { VolService } from './vol.service';
import { getErrorMessage } from '../error-message';

@Component({
  selector: 'app-vol-list',
  standalone: true,
  imports: [RouterLink, DatePipe, DecimalPipe],
  templateUrl: './vol-list.html',
  styleUrls: ['./vol-list.css'],
})
export class VolList implements OnInit {
  private volService = inject(VolService);

  vols = signal<Vol[]>([]);
  loading = signal(false);
  error = signal('');
  success = signal('');
  expandedId = signal<number | null>(null);

  toggle(vol: Vol): void {
    const id = vol.idVol ?? null;
    this.expandedId.update((current) => (current === id ? null : id));
  }

  readonly statutLabels: Record<string, string> = {
    DISPONIBLE: 'Disponible',
    COMPLET: 'Complet',
    ANNULE: 'Annulé',
    INACTIF: 'Inactif',
  };

  private search$ = new Subject<string>();

  constructor() {
    // attend 300 ms après la dernière frappe avant d'appeler le backend
    this.search$
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntilDestroyed())
      .subscribe((q) => this.load(q));
  }

  ngOnInit(): void {
    this.load();
  }

  onSearch(value: string): void {
    this.search$.next(value);
  }

  load(q?: string): void {
    this.loading.set(true);
    this.error.set('');
    this.success.set('');
    this.volService.getAll(q).subscribe({
      next: (vols) => {
        this.vols.set(vols);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(getErrorMessage(err));
        this.loading.set(false);
      },
    });
  }

  remove(vol: Vol): void {
    if (!vol.idVol) return;
    const ok = confirm(`Supprimer le vol ${vol.numeroVol} (${vol.villeDepart} - ${vol.villeArrivee}) ?`);
    if (!ok) return;

    this.volService.delete(vol.idVol).subscribe({
      next: () => {
        this.vols.update((list) => list.filter((v) => v.idVol !== vol.idVol));
        this.success.set('Vol supprimé.');
        this.error.set('');
      },
      error: (err) => this.error.set(getErrorMessage(err)),
    });
  }
}