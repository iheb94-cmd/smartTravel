import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth';

// Routes publiques du backend : pas de jeton
const PUBLIC_PATHS = ['/auth/', '/pass/'];

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const isPublic = PUBLIC_PATHS.some((p) => req.url.includes(p));
  const token = auth.getToken();

  const authReq =
    token && !isPublic
      ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
      : req;

  return next(authReq).pipe(
    catchError((err) => {
      // jeton expiré ou absent : retour au login
      if (err.status === 401 && !isPublic) {
        auth.logout();
      }
      return throwError(() => err);
    }),
  );
};