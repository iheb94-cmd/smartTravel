
import {
  Component,
  OnInit,
  signal,
  inject,
  DestroyRef
} from '@angular/core';

import { RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { Subject, debounceTime, distinctUntilChanged } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { Circuit, StatutOffre } from './circuit.model';
import { CircuitService } from './circuit.service';
import { getErrorMessage } from '../error-message';

@Component({
  selector: 'app-circuit-list',
  standalone: true,
  imports: [RouterLink, DecimalPipe],
  templateUrl: './circuit-list.html',
  styleUrl: './circuit-list.css'
})
export class CircuitList implements OnInit {

  private circuitService = inject(CircuitService);
  private destroyRef = inject(DestroyRef);

  circuits = signal<Circuit[]>([]);
  loading = signal(false);
  error = signal('');
  success = signal('');

  expandedCircuitId = signal<number | null>(null);

  private searchSubject = new Subject<string>();

  statutLabels: Record<StatutOffre, string> = {
    DISPONIBLE: 'Disponible',
    COMPLET: 'Complet',
    ANNULE: 'Annulé',
    INACTIF: 'Inactif'
  };

  ngOnInit(): void {

    this.load();

    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(q => {
      this.load(q);
    });
  }

  onSearch(value: string): void {
    this.searchSubject.next(value);
  }

  load(q?: string): void {

    this.loading.set(true);
    this.error.set('');

    this.circuitService.getAll(q).subscribe({

      next: data => {
        this.circuits.set(data);
        this.loading.set(false);
      },

      error: err => {
        this.error.set(getErrorMessage(err));
        this.loading.set(false);
      }

    });
  }

  toggleDetails(circuit: Circuit): void {

    if (this.expandedCircuitId() === circuit.idCircuit) {
      this.expandedCircuitId.set(null);
    } else {
      this.expandedCircuitId.set(circuit.idCircuit ?? null);
    }
  }

  remove(circuit: Circuit): void {

    const confirmation = confirm(
      `Voulez-vous vraiment supprimer le circuit "${circuit.titre}" ?`
    );

    if (!confirmation || circuit.idCircuit == null) {
      return;
    }

    this.circuitService.delete(circuit.idCircuit).subscribe({

      next: () => {
        this.success.set('Circuit supprimé avec succès.');
        this.expandedCircuitId.set(null);
        this.load();
      },

      error: err => {
        this.error.set(getErrorMessage(err));
        this.success.set('');
      }

    });
  }
}
