
import { Component, OnInit, inject, signal } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';

import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Hotel } from './hotel.model';
import { HotelService } from './hotel.service';
import { getErrorMessage } from '../error-message';

// Validation de cohérence du formulaire
function hotelCoherence(group: AbstractControl): ValidationErrors | null {

  const capacite = Number(group.get('capacite')?.value);
  const chambres = Number(group.get('nombreChambres')?.value);

  const errors: ValidationErrors = {};

  if (capacite > 0 && chambres === 0) {
    errors['chambresIncoherentes'] = true;
  }

  return Object.keys(errors).length ? errors : null;
}

@Component({
  selector: 'app-hotel-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './hotel-form.html',
  styleUrls: ['./hotel-form.css'],
})
export class HotelForm implements OnInit {

  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private hotelService = inject(HotelService);

  id: number | null = null;

  loading = signal(false);
  saving = signal(false);
  error = signal('');

  readonly typesHebergement = [
    'Hôtel',
    'Résidence',
    'Auberge',
    'Maison d’hôtes',
    'Resort',
    'Appartement',
  ];

  readonly statuts = [
    { value: 'DISPONIBLE', label: 'Disponible' },
    { value: 'COMPLET', label: 'Complet' },
    { value: 'INACTIF', label: 'Inactif' },
  ];

  form = this.fb.nonNullable.group(
    {
      nom: ['', [Validators.required, Validators.maxLength(150)]],
      pays: ['', [Validators.required, Validators.maxLength(100)]],
      ville: ['', [Validators.required, Validators.maxLength(100)]],
      adresse: ['', [Validators.required, Validators.maxLength(255)]],
      categorie: [5, [Validators.required, Validators.min(1), Validators.max(5)]],
      prixParNuit: [
        null as number | null,
        [Validators.required, Validators.min(0.01)],
      ],
      typeHebergement: ['Hôtel', Validators.required],
      capacite: [
        null as number | null,
        [Validators.required, Validators.min(1), Validators.pattern(/^[0-9]+$/)],
      ],
      nombreChambres: [
        null as number | null,
        [Validators.required, Validators.min(0), Validators.pattern(/^[0-9]+$/)],
      ],
      services: [''],
      description: [''],
      image: [''],
      statut: ['DISPONIBLE', Validators.required],
    },
    { validators: hotelCoherence }
  );

  private readonly messages: Record<string, string> = {
    nom: "Le nom de l'hôtel est obligatoire",
    pays: 'Le pays est obligatoire',
    ville: 'La ville est obligatoire',
    adresse: "L'adresse est obligatoire",
    categorie: 'La catégorie doit être comprise entre 1 et 5 étoiles',
    prixParNuit: 'Le prix par nuit est obligatoire',
    typeHebergement: "Le type d'hébergement est obligatoire",
    capacite: 'La capacité est obligatoire',
    nombreChambres: 'Le nombre de chambres est obligatoire',
    statut: 'Le statut est obligatoire',
  };

  get isEdit(): boolean {
    return this.id !== null;
  }

  ngOnInit(): void {

    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {

      this.id = Number(idParam);

      if (!Number.isInteger(this.id) || this.id <= 0) {
        this.error.set('Identifiant hôtel invalide.');
        return;
      }

      this.loading.set(true);

      this.hotelService.getById(this.id).subscribe({

        next: (hotel) => {
          this.form.patchValue({
            nom: hotel.nom,
            pays: hotel.pays,
            ville: hotel.ville,
            adresse: hotel.adresse,
            categorie: hotel.categorie,
            prixParNuit: hotel.prixParNuit,
            typeHebergement: hotel.typeHebergement,
            capacite: hotel.capacite,
            nombreChambres: hotel.nombreChambres,
            services: hotel.services ?? '',
            description: hotel.description ?? '',
            image: hotel.image ?? '',
            statut: hotel.statut,
          });

          this.loading.set(false);
        },

        error: (err) => {
          this.error.set(getErrorMessage(err));
          this.loading.set(false);
        },

      });
    }
  }

  // Affichage des erreurs de validation
  fieldError(name: string): string {

    const control = this.form.get(name);

    if (!control || !control.errors || !(control.touched || control.dirty)) {
      return '';
    }

    if (control.errors['min']) {
      if (name === 'prixParNuit') {
        return 'Le prix doit être supérieur à 0';
      }

      if (name === 'capacite') {
        return 'La capacité minimale est 1';
      }

      return 'La valeur ne peut pas être négative';
    }

    if (control.errors['max']) {
      return 'La catégorie maximale est 5 étoiles';
    }

    if (control.errors['pattern']) {
      return 'Entrez un nombre entier positif';
    }

    return this.messages[name] ?? 'Champ invalide';
  }

  get formErrors(): string[] {

    if (!this.form.touched) return [];

    const errors: string[] = [];

    if (this.form.errors?.['chambresIncoherentes']) {
      errors.push(
        'Le nombre de chambres doit être supérieur à zéro.'
      );
    }

    return errors;
  }

  // Ajouter ou modifier un hôtel
  save(): void {

    this.form.markAllAsTouched();

    if (this.form.invalid) return;

    const v = this.form.getRawValue();

    const hotel: Hotel = {
      nom: v.nom.trim(),
      pays: v.pays.trim(),
      ville: v.ville.trim(),
      adresse: v.adresse.trim(),
      categorie: Number(v.categorie),
      prixParNuit: Number(v.prixParNuit),
      typeHebergement: v.typeHebergement,
      capacite: Number(v.capacite),
      nombreChambres: Number(v.nombreChambres),
      services: v.services.trim(),
      description: v.description.trim(),
      image: v.image.trim(),
      statut: v.statut as Hotel['statut'],
    };

    this.saving.set(true);
    this.error.set('');

    const request = this.id !== null
      ? this.hotelService.update(this.id, hotel)
      : this.hotelService.create(hotel);

    request.subscribe({

      next: () => {
        this.router.navigate(['/hotels']);
      },

      error: (err) => {
        this.error.set(getErrorMessage(err));
        this.saving.set(false);
      },

    });
  }
}
