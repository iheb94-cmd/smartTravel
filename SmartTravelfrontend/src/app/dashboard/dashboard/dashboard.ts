import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { DashboardService, DashboardStats } from '../../services/dashboard';

interface Bar {
  label: string;
  value: number;
  pct: number;
  color: string;
}

interface ChartBlock {
  title: string;
  type: 'donut' | 'bars';
  bars: Bar[];
  total: number;
  gradient: string;
}

const PALETTE = ['#2563eb', '#0d9488', '#d97706', '#7c3aed', '#db2777', '#0891b2', '#64748b'];

@Component({
  selector: 'app-dashboard',
  imports: [DatePipe, DecimalPipe],
  styleUrl: './dashboard.css',
  templateUrl: './dashboard.html',
})
export class DashboardComponent implements OnInit {
  private service = inject(DashboardService);

  today = new Date();
  stats = signal<DashboardStats | null>(null);
  loading = signal(true);
  error = signal<string | null>(null);

  charts = computed<ChartBlock[]>(() => {
    const s = this.stats();
    if (!s) return [];
    return [
      this.block('Offres par statut', s.offresParStatut, 'donut'),
      this.block('Circuits par statut', s.circuitsParStatut, 'donut'),
      this.block('Hôtels par statut', s.hotelsParStatut, 'donut'),
      this.block('Vols par statut', s.volsParStatut, 'donut'),
      this.block('Utilisateurs par rôle', s.usersParRole, 'donut'),
      this.block('Offres par mois (date début)', s.offresParMois, 'bars'),
      this.block('Offres par agent', s.offresParAgent, 'bars'),
      this.block('Circuits par pays', s.circuitsParPays, 'bars'),
    ];
  });

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set(null);
    this.service.getStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        this.today = new Date();
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Impossible de charger le dashboard');
        this.loading.set(false);
      },
    });
  }

  private block(
    title: string,
    map: Record<string, number> | undefined,
    type: 'donut' | 'bars',
  ): ChartBlock {
    const entries = Object.entries(map ?? {});
    const total = entries.reduce((sum, [, v]) => sum + v, 0);
    const max = Math.max(...entries.map(([, v]) => v), 1);

    const bars: Bar[] = entries.map(([label, value], i) => ({
      label,
      value,
      pct: Math.round((value / max) * 100),
      color: type === 'bars' ? '#2563eb' : PALETTE[i % PALETTE.length],
    }));

    let gradient = '';
    if (type === 'donut' && total > 0) {
      let acc = 0;
      const parts = bars.map((b) => {
        const start = (acc / total) * 360;
        acc += b.value;
        const end = (acc / total) * 360;
        return `${b.color} ${start}deg ${end}deg`;
      });
      gradient = `conic-gradient(${parts.join(', ')})`;
    }

    return { title, type, bars, total, gradient };
  }
}