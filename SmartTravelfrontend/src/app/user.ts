export interface User {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  role: string;
}

export interface UserDto {
  id: number;
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  role: string;
}

export interface CreateUserDto {
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  password: string;
  role?: string;
}

export interface UpdatePasswordDto {
  email: string;
  oldPassword: string;
  newPassword: string;
}

export interface UpdatePasswordRequest {
  email: string;
  confirmCode: string;
  newPassword: string;
}

export interface LoginResponse {
  user: User;
  token: string;
}