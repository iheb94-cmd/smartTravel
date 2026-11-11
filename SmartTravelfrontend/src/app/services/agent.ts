
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { CreateUserDto, UserDto } from '../user';

@Injectable({
  providedIn: 'root'
})
export class AgentService {

  private apiUrl = 'http://localhost:8081/api/admin/agents';

  constructor(private http: HttpClient) {}

  getAllAgents(): Observable<UserDto[]> {
    return this.http.get<UserDto[]>(this.apiUrl);
  }

  createAgent(agent: CreateUserDto): Observable<UserDto> {
    return this.http.post<UserDto>(this.apiUrl, agent);
  }

  getAgentById(id: number): Observable<UserDto> {
    return this.http.get<UserDto>(`${this.apiUrl}/${id}`);
  }

  updateAgent(id: number, agent: CreateUserDto): Observable<UserDto> {
    return this.http.put<UserDto>(`${this.apiUrl}/${id}`, agent);
  }

  deleteAgent(id: number): Observable<string> {
    return this.http.delete(`${this.apiUrl}/${id}`, {
      responseType: 'text'
    });
  }
}
