# StayEasy

> **Réserver un séjour. Trouver une colocation.**

Plateforme de gestion des hébergements, des réservations et des services associés, conçue en **architecture microservices** (Spring Boot, RabbitMQ, Keycloak, Docker Compose).

Projet du module **Architecture Web Distribuée (AWD) 2026–2027**, voir le [rapport de conception](Documentation/StayEasy_Rapport_de_conception_v2.pdf).

**Équipe** : Molka Kammoun · Isra Khlifi · Mohamed Khouni · Samar Masmoudi · Nermine Karoui · Lina Kazdaghli
**Encadrement** : Linda Ourfelli · Oumayma Ibn El Fekih

---

## Sommaire

1. [Contexte et objectifs](#1-contexte-et-objectifs)
2. [Acteurs](#2-acteurs)
3. [Architecture](#3-architecture)
4. [Microservices](#4-microservices)
5. [Communication entre services](#5-communication-entre-services)
6. [Sécurité](#6-sécurité)
7. [Technologies](#7-technologies)
8. [Structure du dépôt](#8-structure-du-dépôt)
9. [Lancer le projet](#9-lancer-le-projet)
10. [Conventions d'API](#10-conventions-dapi)
11. [Risques et mesures](#11-risques-et-mesures)
12. [Organisation et planning](#12-organisation-et-planning)

---

## 1. Contexte et objectifs

StayEasy centralise dans un espace unique des opérations aujourd'hui dispersées : recherche de logement, réservation, paiement, suivi de la maintenance, colocation et communication entre les acteurs.

**Problématique** : comment aider les personnes en quête de repos ou d'une expérience unique à trouver un lieu calme proche de la nature, tout en donnant de la visibilité aux maisons d'hôtes isolées ?

**Périmètre**

- Rechercher, consulter et réserver un hébergement, avec un **paiement simulé**.
- Offrir aux propriétaires des outils de gestion des hébergements, chambres, réservations et annonces.
- Proposer un parcours de **colocation** : séjours partagés, matching de compatibilité, activités et votes de groupe.
- Suivre les interventions techniques et l'état opérationnel des chambres.
- Informer les utilisateurs par des notifications **sans bloquer** les opérations principales.
- Sécuriser l'accès par rôles grâce à une authentification centralisée.

## 2. Acteurs

| Acteur | Fonctionnalités principales |
|---|---|
| **Voyageur / Étudiant** | Recherche d'hébergements, consultation des chambres, réservation, paiement simulé, notifications, demande pour rejoindre un séjour partagé, activités, votes et retours d'expérience |
| **Propriétaire** | Publication et gestion des hébergements et chambres, gestion des réservations, annonces de colocation, traitement des candidatures |
| **Équipe technique** | Suivi des interventions, mise à jour de l'état opérationnel des chambres |

Leurs droits sont portés par des rôles gérés dans Keycloak.

## 3. Architecture

Sept microservices autonomes, **une base de données dédiée par service**, un point d'entrée unique (API Gateway) et un bus d'événements.

```
        Voyageur / Étudiant      Propriétaire      Équipe technique
                       \              |              /
                        Frontend React (proposé)
                                  |
   Eureka  <------>  API Gateway (Spring Cloud Gateway)  <------>  Keycloak (OIDC)
 (découverte)                     |                              (connexion, rôles)
        ┌────────┬────────┬───────┴┬────────┬────────┬────────┐
       MS1      MS2      MS3      MS4      MS5      MS6      MS7
      :8081    :8082    :8083    :8084    :8085    :8086    :8087
                                  |
              RabbitMQ : bus d'événements métier (asynchrone)

        Docker Compose : orchestration locale de tous les conteneurs
```

**Principes de découpage**

- **Autonomie** : un service = un domaine métier, développé, testé et déployé indépendamment.
- **Base par service** : aucune base partagée, chaque service est seul maître de ses données.
- **Contrats communs** : les services dialoguent uniquement par API REST ou événements documentés.

## 4. Microservices

| Réf. | Service | Responsabilité | Base | Port |
|---|---|---|---|---|
| MS1 | **Accommodation** | Hébergements, chambres et équipements | H2 | 8081 |
| MS2 | **Reservation** | Réservations et calendrier | H2 | 8082 |
| MS3 | **Maintenance** | Interventions techniques | H2 | 8083 |
| MS4 | **Colocation** | Séjours partagés, annonces et candidatures | MySQL | 8084 |
| MS5 | **Payment** | Paiements simulés et remboursements | H2 | 8085 |
| MS6 | **Notification** | Messages et historique | MySQL | 8086 |
| MS7 | **User** (commun) | Profils métier | PostgreSQL | 8087 |

### Règle clé de chaque service

- **MS1 Accommodation** : seul le propriétaire d'un hébergement peut le modifier ; une chambre `EN_MAINTENANCE` ou `HORS_SERVICE` n'est jamais proposée à la réservation.
- **MS2 Reservation** : les nuits sont bloquées de façon atomique à la création pour éviter la double réservation.
- **MS3 Maintenance** : une chambre ne peut pas être mise en maintenance sur une période couverte par une réservation confirmée.
- **MS4 Colocation** : un séjour ne dépasse pas `maxParticipants` ; l'adhésion passe par une candidature (`PENDING`) traitée par l'organisateur.
- **MS5 Payment** : deux demandes avec la même clé `idempotencyKey` ne produisent qu'un seul paiement ; la somme des remboursements ne dépasse jamais le montant payé.
- **MS6 Notification** : un échec d'envoi ne remonte jamais à l'émetteur ; chaque événement est dédupliqué grâce à son identifiant de message.
- **MS7 User** : aucun mot de passe n'est stocké par StayEasy ; l'identité reste dans Keycloak, le lien se fait par `keycloakId`.

### Données par service

| Service | Entités | Énumérations |
|---|---|---|
| Accommodation | Hebergement, Chambre, Equipement | TypeHebergement, StatutChambre (`DISPONIBLE`, `EN_MAINTENANCE`, `HORS_SERVICE`, `PARTAGEE`) |
| Reservation | Reservation, NuitBloquee | StatutReservation, MotifAnnulation |
| Maintenance | Intervention, CommentaireIntervention | TypeIntervention, PrioriteIntervention |
| Colocation | SharedStay, StayMember, Activity… | ExperienceType, StayStatus |
| Payment | Paiement, Remboursement | MethodePaiement (`CARTE_BANCAIRE`, `VIREMENT`, `PORTEFEUILLE`), StatutPaiement |
| Notification | Notification, ModeleMessage, EvenementTraite | CanalNotification, TypeNotification |
| User | Utilisateur, ProfilVoyageur, ProfilProprietaire | RoleUtilisateur, StatutCompte, TypeExperience |

Chaque service suit l'organisation **Controller · Service · Repository**.

## 5. Communication entre services

Deux modes : **synchrone** (REST / OpenFeign) quand une réponse immédiate est nécessaire, **asynchrone** (événements RabbitMQ) pour informer sans bloquer.

### Appels synchrones (REST / OpenFeign)

| De | Vers | Objet |
|---|---|---|
| Reservation | Accommodation | Vérifier la chambre : `GET /api/chambres/{id}/disponibilite` |
| Reservation | Payment | Paiement ou remboursement : `POST /api/paiements`, `POST /api/paiements/{id}/remboursements` |
| Colocation | Accommodation | Infos de la maison d'hôtes : `GET /api/hebergements/{id}` |
| Colocation | User | Identité et profils des participants : `GET /api/users/{id}/profil` |
| Colocation | Reservation | Réservations de groupe : `POST /api/reservations` |
| Maintenance | Accommodation | Vérifier et changer l'état de la chambre : `GET /api/chambres/{id}`, `PATCH /api/chambres/{id}/statut` |
| Maintenance | Reservation | Absence de réservation sur la période : `GET /api/reservations/hebergement/{id}/periode` |
| Maintenance | User | Vérifier le technicien : `GET /api/users/{id}` |
| Accommodation | User | Vérifier le propriétaire : `GET /api/users/{id}` |
| Notification | User | Coordonnées et préférences du destinataire : `GET /api/users/{id}` |

### Événements asynchrones (RabbitMQ)

| Émetteur | Événements | Consommateur |
|---|---|---|
| Reservation | `reservation.created` · `reservation.confirmed` · `reservation.cancelled` | Notification |
| Payment | `payment.succeeded` · `payment.failed` · `payment.refunded` | Notification (et Colocation pour le paiement collectif) |
| Maintenance | `intervention.created` · `intervention.assigned` · `intervention.closed` | Notification |
| Colocation | `sharedstay.created` · `member.requested` · `member.accepted` · `vote.created` | Notification |
| Colocation | `colocation.payment.requested` | Payment |
| Accommodation | `chambre.status.changed` | Notification |
| User | `user.registered` | Notification |

## 6. Sécurité

- Authentification centralisée par **Keycloak** (protocole **OIDC**), un seul point de connexion.
- La **Gateway** est le seul point d'entrée : les services ne sont pas exposés directement. Elle contrôle les jetons **JWT**.
- Les rôles (voyageur / étudiant, propriétaire, équipe technique) déterminent les opérations autorisées.

## 7. Technologies

| Backend | Données et accès | Intégration |
|---|---|---|
| Java / Spring Boot | H2 · MySQL · PostgreSQL | React (frontend proposé) |
| Spring Cloud Gateway · Eureka | Keycloak : identité, rôles | RabbitMQ |
| Swagger / OpenAPI | | Docker Compose |

Chaque service expose sa documentation **Swagger / OpenAPI**, qui sert de contrat partagé entre les membres de l'équipe.

## 8. Structure du dépôt

```
StayEasy/
├── Backend/
│   ├── eureka-server/
│   ├── api-gateway/
│   ├── accommodation-service/     MS1
│   ├── reservation-service/       MS2
│   ├── maintenance-service/       MS3
│   ├── colocation-service/        MS4
│   ├── payment-service/           MS5
│   ├── notification-service/      MS6
│   └── user-service/              MS7
├── Frontend/
├── Documentation/                 (rapport, Presentation/, Diagrams/)
├── docker/                        (Dockerfile.spring, init.sql MySQL, routes Gateway)
├── keycloak/                      (realm stayeasy)
├── docker-compose.yml
└── README.md
```

## 9. Lancer le projet

**Prérequis** : Docker et Docker Compose.

```bash
# Tout lancer (construit les images, démarre infrastructure + services)
docker compose up -d --build

# Suivre l'état
docker compose ps
docker compose logs -f api-gateway

# Arrêter (ajouter -v pour supprimer les volumes MySQL / PostgreSQL)
docker compose down
```

Mode développement (accès direct à Swagger sur chaque service) :

```bash
docker compose -f docker-compose.yml -f docker-compose.dev.yml up -d --build
```

**Accès** (ports par défaut du fichier Compose, modifiables)

| Composant | URL |
|---|---|
| API Gateway (entrée unique) | http://localhost:8080 |
| Eureka | http://localhost:8761 |
| Keycloak | http://localhost:8180 |
| Console RabbitMQ | http://localhost:15672 |

Les identifiants de développement sont dans le fichier `.env` : à changer en dehors d'un usage local. Les bases H2 sont en mémoire : les données disparaissent au redémarrage du service.

## 10. Conventions d'API

Toutes les API échangent du **JSON**, passent par la Gateway et exigent un **jeton JWT** émis par Keycloak.

| Code | Signification |
|---|---|
| 200 | Requête réussie |
| 201 | Ressource créée |
| 400 | Données invalides |
| 401 | Jeton absent ou invalide |
| 403 | Rôle ou propriétaire non autorisé |
| 404 | Ressource introuvable |
| 409 | Conflit métier (chambre indisponible, séjour complet, paiement déjà traité…) |
| 500 | Erreur interne du serveur |
| 503 | Service dépendant indisponible |

**Préfixes principaux** : `/api/hebergements`, `/api/chambres` (MS1) · `/api/reservations` (MS2) · `/api/interventions` (MS3) · `/api/v1/…` (MS4, ex. `/api/v1/shared-stays`) · `/api/paiements` (MS5) · `/api/notifications`, `/api/modeles` (MS6) · `/api/users` (MS7).

## 11. Risques et mesures

| # | Risque | Mesure |
|---|---|---|
| 01 | Double réservation | Blocage atomique des nuits |
| 02 | Paiement incertain | Idempotence et réconciliation |
| 03 | Message perdu | Reprise et déduplication |
| 04 | Retard d'intégration | Contrats partagés et priorités |
| 05 | Service dépendant indisponible | Délais d'attente et reprises sur OpenFeign ; réponse 503 explicite |
| 06 | Écart entre services | Références par identifiants, événements idempotents, réconciliation périodique |

## 12. Organisation et planning

**Répartition** : un service par étudiant (E1 Accommodation, E2 Reservation, E3 Maintenance, E4 Colocation, E5 Payment, E6 Notification). Tout le groupe se partage le service User, le frontend et l'intégration, Keycloak / Gateway / Eureka, RabbitMQ et Docker, ainsi que la documentation et la démonstration.

**Méthode** : tâche attribuée (GitHub Projects) → branche dédiée par fonctionnalité → pull request avec relecture collective → fusion sur `main`, qui reste stable.

| Séances | Phase | Contenu |
|---|---|---|
| S4 | Conception | Architecture et contrats |
| S5/6 | Développement | Services, Gateway et API REST |
| S7/8 | Intégration | Événements, sécurité, frontend |
| S9/10 | Livraison | Docker, tests, soutenance |
