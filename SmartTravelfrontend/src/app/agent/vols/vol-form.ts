import { Component, OnInit, inject, signal } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Vol } from './vol.model';
import { VolService } from './vol.service';
import { getErrorMessage } from '../error-message';

// Contrôles qui portent sur plusieurs champs à la fois
function volCoherence(group: AbstractControl): ValidationErrors | null {
  const depart = String(group.get('villeDepart')?.value ?? '').trim().toLowerCase();
  const arrivee = String(group.get('villeArrivee')?.value ?? '').trim().toLowerCase();
  const dd = group.get('dateDepart')?.value;
  const da = group.get('dateArrivee')?.value;

  const errors: ValidationErrors = {};
  if (depart && arrivee && depart === arrivee) errors['memeVille'] = true;
  if (dd && da && new Date(da) <= new Date(dd)) errors['datesIncoherentes'] = true;
  return Object.keys(errors).length ? errors : null;
}

@Component({
  selector: 'app-vol-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './vol-form.html',
  styleUrls: ['./vol-form.css'],
})
export class VolForm implements OnInit {
  private fb = inject(FormBuilder);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private volService = inject(VolService);

  id: number | null = null;
  loading = signal(false);
  saving = signal(false);
  error = signal('');

  readonly classes = ['Économique', 'Premium économique', 'Business', 'Première'];
  readonly statuts = [
    { value: 'DISPONIBLE', label: 'Disponible' },
    { value: 'COMPLET', label: 'Complet' },
    { value: 'ANNULE', label: 'Annulé' },
  ];

  form = this.fb.nonNullable.group(
    {
      numeroVol: ['', [Validators.required, Validators.maxLength(20)]],
      compagnie: ['', Validators.required],
      villeDepart: ['', Validators.required],
      villeArrivee: ['', Validators.required],
      dateDepart: ['', Validators.required],
      dateArrivee: ['', Validators.required],
      prix: [null as number | null, [Validators.required, Validators.min(0.01)]],
      classe: ['Économique', Validators.required],
      nombrePlaces: [null as number | null, [Validators.required, Validators.min(0), Validators.pattern(/^\d+$/)]],
      bagageInclus: [false],
      statut: ['DISPONIBLE', Validators.required],
      description: [''],
      image: [''],
    },
    { validators: volCoherence },
  );

  private readonly messages: Record<string, string> = {
    numeroVol: 'Le numéro de vol est obligatoire',
    compagnie: 'La compagnie est obligatoire',
    villeDepart: 'La ville de départ est obligatoire',
    villeArrivee: "La ville d'arrivée est obligatoire",
    dateDepart: 'La date de départ est obligatoire',
    dateArrivee: "La date d'arrivée est obligatoire",
    prix: 'Le prix est obligatoire',
    classe: 'La classe est obligatoire',
    nombrePlaces: 'Le nombre de places est obligatoire',
    statut: 'Le statut est obligatoire',
  };

  get isEdit(): boolean {
    return this.id !== null;
  }

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.id = Number(idParam);
      this.loading.set(true);
      this.volService.getById(this.id).subscribe({
        next: (vol) => {
          this.form.patchValue({
            ...vol,
            dateDepart: vol.dateDepart?.slice(0, 16), // format attendu par datetime-local
            dateArrivee: vol.dateArrivee?.slice(0, 16),
            description: vol.description ?? '',
            image: vol.image ?? '',
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

  // Message d'erreur d'un champ (vide si le champ est valide ou pas encore touché)
  fieldError(name: string): string {
    const c = this.form.get(name);
    if (!c || !c.errors || !(c.touched || c.dirty)) return '';
    if (c.errors['min']) {
      return name === 'prix' ? 'Le prix doit être supérieur à 0' : 'Valeur négative impossible';
    }
    if (c.errors['pattern']) return 'Entrez un nombre entier';
    if (c.errors['maxlength']) return 'Maximum 20 caractères';
    return this.messages[name] ?? 'Champ invalide';
  }

  get formErrors(): string[] {
    if (!this.form.touched) return [];
    const list: string[] = [];
    if (this.form.errors?.['memeVille']) list.push("La ville d'arrivée doit être différente de la ville de départ");
    if (this.form.errors?.['datesIncoherentes']) list.push("La date d'arrivée doit être après la date de départ");
    return list;
  }

  save(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const v = this.form.getRawValue();
    const vol: Vol = {
      numeroVol: v.numeroVol.trim(),
      compagnie: v.compagnie.trim(),
      villeDepart: v.villeDepart.trim(),
      villeArrivee: v.villeArrivee.trim(),
      dateDepart: v.dateDepart,
      dateArrivee: v.dateArrivee,
      prix: Number(v.prix),
      classe: v.classe,
      nombrePlaces: Number(v.nombrePlaces),
      bagageInclus: v.bagageInclus,
      statut: v.statut as Vol['statut'],
      description: v.description.trim(),
      image: v.image.trim(),
    };

    this.saving.set(true);
    this.error.set('');

    const request = this.id !== null
      ? this.volService.update(this.id, vol)
      : this.volService.create(vol);

    request.subscribe({
      next: () => this.router.navigate(['/vols']),
      error: (err) => {
        this.error.set(getErrorMessage(err));
        this.saving.set(false);
      },
    });
  }
}