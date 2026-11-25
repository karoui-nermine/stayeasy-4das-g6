import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from './auth.service';
import { ApiService } from './api.service';

type Section = 'dashboard'|'hebergements'|'reservations'|'factures'|'notifications'|'colocation'|'maintenance';
@Component({
  selector: 'app-root', standalone: true, imports: [CommonModule, FormsModule],
  template: `
  <div class="layout" *ngIf="ready; else loading">
    <aside class="sidebar">
      <div class="brand" role="img" aria-label="StayEasy">Stay<span>Easy</span></div>
      <p class="muted">ESPACE PERSONNEL</p>
      <button *ngFor="let item of menu" [class.active]="section===item.id" (click)="open(item.id)">{{item.label}}</button>
      <div class="user"><b>{{auth.username}}</b><small>Connecté via Keycloak</small><button class="logout" (click)="auth.logout()">Déconnexion</button></div>
    </aside>
    <main>
      <header><div><small>PLATEFORME DE SÉJOURS & BIEN-ÊTRE</small><h1>{{title}}</h1></div><span class="pill">StayEasy</span></header>
      <p class="error" *ngIf="error">{{error}}</p>
      <section class="stats" *ngIf="section==='dashboard'">
        <article class="stat"><small>Hébergements</small><strong>{{counts.accommodations}}</strong></article>
        <article class="stat"><small>Réservations</small><strong>{{counts.reservations}}</strong></article>
        <article class="stat"><small>Factures</small><strong>{{counts.invoices}}</strong></article>
        <article class="stat"><small>Notifications</small><strong>{{counts.notifications}}</strong></article>
      </section>
      <section class="panel">
        <div class="panel-head"><div><h2>{{title}}</h2><p>Consultez les informations disponibles depuis les microservices StayEasy.</p></div>
          <button class="primary" (click)="load()">Actualiser</button></div>
        <div class="table-wrap" *ngIf="rows.length; else empty">
          <table><thead><tr><th *ngFor="let col of columns">{{col}}</th></tr></thead>
          <tbody><tr *ngFor="let row of rows"><td *ngFor="let col of keys">{{display(row[col])}}</td></tr></tbody></table>
        </div>
        <ng-template #empty><div class="empty">Aucune donnée à afficher pour cette rubrique, ou service indisponible.</div></ng-template>
      </section>
      <footer>StayEasy · Prenez le temps de vous ressourcer.</footer>
    </main>
  </div>
  <ng-template #loading><div class="loading">Connexion à StayEasy…</div></ng-template>`,
})
export class AppComponent {
  ready = false; section: Section = 'dashboard'; rows: any[] = []; error = '';
  counts = { accommodations: 0, reservations: 0, invoices: 0, notifications: 0 };
  menu: {id: Section; label: string}[] = [
    {id:'dashboard',label:'Tableau de bord'},{id:'hebergements',label:'Hébergements'},
    {id:'reservations',label:'Réservations'},{id:'factures',label:'Factures'},
    {id:'notifications',label:'Notifications'},{id:'colocation',label:'Colocation'},
    {id:'maintenance',label:'Maintenance'}
  ];
  constructor(public auth: AuthService, private api: ApiService) {}
  async ngOnInit(): Promise<void> {
    try { await this.auth.init(); this.ready = true; await this.load(); }
    catch (e) { this.error = 'Connexion impossible. Vérifiez que Keycloak est démarré sur le port 8180.'; this.ready = true; }
  }
  get title(): string { return this.menu.find(x => x.id === this.section)?.label ?? 'StayEasy'; }
  get columns(): string[] { return this.rows.length ? Object.keys(this.rows[0]).slice(0,6) : []; }
  get keys(): string[] { return this.columns; }
  display(value: unknown): string { return value === null || value === undefined ? '—' : typeof value === 'object' ? JSON.stringify(value) : String(value); }
  open(section: Section): void { this.section = section; void this.load(); }
  async load(): Promise<void> {
    this.error = ''; this.rows = [];
    const paths: Record<Section,string> = {
      dashboard:'/api/accommodations', hebergements:'/api/accommodations',
      reservations:'/api/reservations', factures:'/api/invoices',
      notifications:'/api/notifications', colocation:'/api/v1/shared-stays',
      maintenance:'/api/maintenances'
    };
    try {
      await this.auth.refresh();
      const result = await this.api.get<any>(paths[this.section]);
      this.rows = Array.isArray(result) ? result : (result ? [result] : []);
      if (this.section === 'dashboard') {
        this.counts.accommodations = this.rows.length;
        const [r,i,n] = await Promise.all([
          this.api.get<any[]>('/api/reservations'), this.api.get<any[]>('/api/invoices'),
          this.api.get<any[]>('/api/notifications')
        ]);
        this.counts.reservations = r.length; this.counts.invoices = i.length; this.counts.notifications = n.length;
      }
    } catch (e) { this.error = 'Impossible de charger les données. Vérifiez la Gateway et les microservices.'; }
  }
}