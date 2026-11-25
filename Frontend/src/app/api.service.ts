import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly base = 'http://localhost:8080';
  constructor(private http: HttpClient) {}
  get<T = any>(path: string): Promise<T> { return firstValueFrom(this.http.get<T>(this.base + path)); }
  post<T = any>(path: string, body: unknown): Promise<T> { return firstValueFrom(this.http.post<T>(this.base + path, body)); }
  put<T = any>(path: string, body: unknown): Promise<T> { return firstValueFrom(this.http.put<T>(this.base + path, body)); }
  delete<T = any>(path: string): Promise<T> { return firstValueFrom(this.http.delete<T>(this.base + path)); }
}