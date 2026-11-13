
import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth';
import { ToastrService } from '@relynn/ngx-toastr';
import { MatIconModule } from '@angular/material/icon';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink,RouterLink, MatIconModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css'],
})
export class Login {
  email = '';
  password = '';
  showPassword = false;


  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

login(): void {
  if (!this.email || !this.password) {
    alert('Veuillez remplir tous les champs.');
    return;
  }

  this.authService.login(this.email, this.password).subscribe({
    next: (response) => {
      this.authService.saveUserData(response);
      alert('Connexion réussie !');

      if (this.authService.isAgent()) {
        this.router.navigate(['/vols']);
      }
      else if (this.authService.isAdmin()) {
        this.router.navigate(['/agents']);
      }
      else {
        this.router.navigate(['/']);
      }
    },
    error: () => {
      alert('Email ou mot de passe incorrect');
    },
  });
}
}
