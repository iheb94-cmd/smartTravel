
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { Circuit, StatutOffre } from './circuit.model';
import { CircuitService } from './circuit.service';
import { getErrorMessage } from '../error-message';

function dateCoherence(control: AbstractControl): ValidationErrors | null {
  const depart = control.get('dateDepart')?.value;
  const retour = control.get('dateRetour')?.value;

  if (depart && retour && retour < depart) {
    return { datesIncoherentes: true };
  }

  return null;
}

@Component({
  selector: 'app-circuit-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './circuit-form.html',
  styleUrl: './circuit-form.css'
})
export class CircuitForm implements OnInit {

  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private circuitService = inject(CircuitService);

  isEdit = signal(false);
  circuitId: number | null = null;

  loading = signal(false);
  error = signal('');
  success = signal('');

  types = [
    'Culturel',
    'Aventure',
    'Détente',
    'Découverte',
    'Nature',
    'Gastronomique',
    'Historique'
  ];

  statuts: StatutOffre[] = [
    'DISPONIBLE',
    'COMPLET',
    'INACTIF'
  ];

  form = this.fb.group({
    titre: ['', [Validators.required, Validators.maxLength(150)]],
    pays: ['', [Validators.required, Validators.maxLength(100)]],
    villeDepart: ['', [Validators.required, Validators.maxLength(100)]],
    destinations: [''],

    dateDepart: ['', Validators.required],
    dateRetour: ['', Validators.required],
    duree: [1, [Validators.required, Validators.min(1)]],

    prix: [0.01, [Validators.required, Validators.min(0.01)]],
    nombrePlaces: [1, [Validators.required, Validators.min(1)]],

    typeCircuit: ['Culturel'],
    programme: [''],
    servicesInclus: [''],
    description: [''],
    image: [''],

    statut: ['DISPONIBLE' as StatutOffre, Validators.required]
  }, { validators: dateCoherence });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.isEdit.set(true);
      this.circuitId = Number(id);
      this.loadCircuit(this.circuitId);
    }
  }

  loadCircuit(id: number): void {
    this.loading.set(true);

    this.circuitService.getById(id).subscribe({
      next: circuit => {
        this.form.patchValue({
          titre: circuit.titre,
          pays: circuit.pays,
          villeDepart: circuit.villeDepart,
          destinations: circuit.destinations ?? '',
          dateDepart: circuit.dateDepart,
          dateRetour: circuit.dateRetour,
          duree: circuit.duree,
          prix: circuit.prix,
          nombrePlaces: circuit.nombrePlaces,
          typeCircuit: circuit.typeCircuit,
          programme: circuit.programme ?? '',
          servicesInclus: circuit.servicesInclus ?? '',
          description: circuit.description ?? '',
          image: circuit.image ?? '',
          statut: circuit.statut
        });

        this.loading.set(false);
      },
      error: err => {
        this.error.set(getErrorMessage(err));
        this.loading.set(false);
      }
    });
  }

  fieldError(field: string): boolean {
    const control = this.form.get(field);
    return !!control && control.invalid && (control.touched || control.dirty);
  }

  save(): void {
    this.error.set('');
    this.success.set('');

    this.form.markAllAsTouched();

    if (this.form.invalid) {
      this.error.set('Veuillez vérifier les champs du formulaire.');
      return;
    }

    const circuit = this.form.getRawValue() as Circuit;

    this.loading.set(true);

    const request = this.isEdit() && this.circuitId !== null
      ? this.circuitService.update(this.circuitId, circuit)
      : this.circuitService.create(circuit);

    request.subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/circuits']);
      },
      error: err => {
        this.error.set(getErrorMessage(err));
        this.loading.set(false);
      }
    });
  }
}
