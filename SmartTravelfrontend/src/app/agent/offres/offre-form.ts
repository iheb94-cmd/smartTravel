
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { OffreService } from './offre.service';
import { Offre } from './offre.model';

import { VolService } from '../vols/vol.service';
import { HotelService } from '../hotels/hotel.service';
import { CircuitService } from '../circuits/circuit.service';

@Component({
  selector: 'app-offre-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './offre-form.html',
  styleUrl: './offre-form.css'
})
export class OffreForm implements OnInit {

  isEdit = false;
  offreId?: number;
  loading = false;
  errorMessage = '';

  vols: any[] = [];
  hotels: any[] = [];
  circuits: any[] = [];

  offreForm: ReturnType<FormBuilder['group']>;

  constructor(
    private fb: FormBuilder,
    private offreService: OffreService,
    private volService: VolService,
    private hotelService: HotelService,
    private circuitService: CircuitService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    // Initialisation du formulaire après la création de FormBuilder
    this.offreForm = this.fb.group({
      nomOffre: ['', Validators.required],
      volId: [null as number | null, Validators.required],
      hotelId: [null as number | null, Validators.required],
      circuitId: [null as number | null, Validators.required],
      prixTotal: [
        null as number | null,
        [Validators.required, Validators.min(0.01)]
      ],
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required],
      nombrePlaces: [1, [Validators.required, Validators.min(1)]],
      statut: ['DISPONIBLE', Validators.required]
    });
  }

  ngOnInit(): void {

    this.loadVols();
    this.loadHotels();
    this.loadCircuits();

    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.isEdit = true;
      this.offreId = Number(id);
      this.loadOffre(this.offreId);
    }
  }

  // Charger les vols
  loadVols(): void {
    this.volService.getAll().subscribe({
      next: (data) => this.vols = data,
      error: (error) => console.error('Erreur chargement vols', error)
    });
  }

  // Charger les hôtels
  loadHotels(): void {
    this.hotelService.getAll().subscribe({
      next: (data) => this.hotels = data,
      error: (error) => console.error('Erreur chargement hôtels', error)
    });
  }

  // Charger les circuits
  loadCircuits(): void {
    this.circuitService.getAll().subscribe({
      next: (data) => this.circuits = data,
      error: (error) => console.error('Erreur chargement circuits', error)
    });
  }

  // Charger une offre pour la modification
  loadOffre(id: number): void {
    this.loading = true;

    this.offreService.getById(id).subscribe({
      next: (offre) => {
        this.offreForm.patchValue({
          nomOffre: offre.nomOffre,
          volId: offre.volId,
          hotelId: offre.hotelId,
          circuitId: offre.circuitId,
          prixTotal: offre.prixTotal,
          dateDebut: offre.dateDebut,
          dateFin: offre.dateFin,
          nombrePlaces: offre.nombrePlaces,
          statut: offre.statut
        });

        this.loading = false;
      },
      error: (error) => {
        console.error('Erreur chargement offre', error);
        this.errorMessage = 'Impossible de charger cette offre.';
        this.loading = false;
      }
    });
  }

  // Enregistrer l'offre
  onSubmit(): void {

    if (this.offreForm.invalid) {
      this.offreForm.markAllAsTouched();
      return;
    }

    const formValue = this.offreForm.getRawValue();

    // Vérifier les dates
    const dateDebut = formValue.dateDebut;
    const dateFin = formValue.dateFin;

    if (!dateDebut || !dateFin || dateFin < dateDebut) {
      this.errorMessage =
        'Veuillez saisir des dates valides. La date de fin doit être après la date de début.';
      return;
    }

    this.errorMessage = '';
    this.loading = true;

    const offre = formValue as Offre;

    const request = this.isEdit
      ? this.offreService.update(this.offreId!, offre)
      : this.offreService.create(offre);

    request.subscribe({
      next: () => {
        this.router.navigate(['/offres']);
      },
      error: (error) => {
        console.error('Erreur enregistrement offre', error);
        this.errorMessage =
          'Une erreur est survenue lors de l’enregistrement.';
        this.loading = false;
      }
    });
  }

  // Retour à la liste
  cancel(): void {
    this.router.navigate(['/offres']);
  }
}
