import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private client = new Keycloak({
    url: 'http://localhost:8180',
    realm: 'stayeasy',
    clientId: 'stayeasy-frontend'
  });
  initialized = false;

  async init(): Promise<void> {
    if (this.initialized) return;
    await this.client.init({ onLoad: 'login-required', pkceMethod: 'S256', checkLoginIframe: false });
    this.initialized = true;
  }
  get token(): string | undefined { return this.client.token; }
  get username(): string { return this.client.tokenParsed?.['preferred_username'] ?? 'Utilisateur'; }
  get roles(): string[] { return this.client.tokenParsed?.['realm_access']?.['roles'] ?? []; }
  hasRole(role: string): boolean { return this.roles.includes(role); }
  async logout(): Promise<void> { await this.client.logout({ redirectUri: window.location.origin }); }
  async refresh(): Promise<void> { if (this.initialized) await this.client.updateToken(30); }
}