import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { Resetpassword } from '../../services/reset-password';
import {  ChangeDetectorRef } from '@angular/core';
@Component({
  selector: 'app-update-password',
  standalone: true,
  imports: [FormsModule, MatIconModule],
  styleUrl: './update-password.css',
  templateUrl: './update-password.html',
})
export class Updatepassword {
  private resetPassword = inject(Resetpassword);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);

  step = 1;
  showPassword = false;
  email = '';
  confirmCode = '';
  newPassword = '';
  confirmPassword = '';

  sendingCode = false;
  updating = false;

sendCode(): void {
  if (this.sendingCode || !this.email.trim()) return;

  this.sendingCode = true;

  this.resetPassword.sendCode(this.email.trim()).subscribe({
    next: (response) => {
      this.step = 2;
      this.sendingCode = false;
      alert('Code envoyé avec succès');

this.cdr.detectChanges();
    },
    error: (error) => {
      this.sendingCode = false;
      alert('Erreur serveur.');
    },
  });
}

resendCode(): void {
  this.step = 1;
}

  updatePassword(): void {
  if (this.updating) return;

  if (!this.confirmCode.trim() || !this.newPassword || !this.confirmPassword) {
    return;
  }

  if (this.newPassword !== this.confirmPassword) {
    return;
  }

  this.updating = true;

  const request = {
    email: this.email.trim(),
    confirmCode: this.confirmCode.trim(),
    newPassword: this.newPassword,
  };

  this.resetPassword.updatePassword(request).subscribe({
    next: (success) => {
      this.updating = false;

      if (success) {
        alert('Mot de passe modifié avec succès.');
        this.router.navigate(['/login']);
      } else {
        alert('Code incorrect ou expiré.');
      }
    },
    error: (error) => {
      this.updating = false;
      console.error(error);
      alert('Erreur serveur.');
    },
  });
}

  login(): void {
    this.router.navigate(['/login']);
  }
}