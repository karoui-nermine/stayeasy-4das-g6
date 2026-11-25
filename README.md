# StayEasy — Angular + Spring Boot

Plateforme de réservation de séjours reposants, d'hébergements et de colocations temporaires.

## Technologies
- **Frontend :** Angular 17 (TypeScript, composants standalone, HttpClient, Keycloak JS).
- **Backend :** Spring Boot 3.2.5 / Java 17 pour les microservices métier ; le microservice **User reste en Node.js / Express**, comme dans le projet d'origine.
- **Infrastructure :** Spring Cloud Gateway, Eureka, Keycloak, RabbitMQ, PostgreSQL, MySQL et Docker Compose.

## Démarrage recommandé
Prérequis : Docker Desktop démarré. Le premier lancement télécharge les images et compile les applications ; il peut prendre plusieurs minutes.

Depuis le dossier `stayeasy` (celui qui contient `docker-compose.yml`) :
```powershell
docker compose up --build
```
Arrêter :
```powershell
docker compose down
```
Réinitialiser les bases (supprime les données Docker) :
```powershell
docker compose down -v
```

## URLs locales
| Élément | URL |
|---|---|
| Application Angular | http://localhost:3000 |
| API Gateway | http://localhost:8080 |
| Eureka | http://localhost:8761 |
| Keycloak | http://localhost:8180 |
| RabbitMQ | http://localhost:15672 |
| Swagger microservices | http://localhost:8081/swagger-ui/index.html à 8086 |

Comptes Keycloak de démonstration : `voyageur/voyageur`, `proprio/proprio`, `tech/tech`.

## Développement sans Docker
Frontend :
```powershell
cd frontend
npm install
npm start
```
Backend : chaque microservice a son propre `pom.xml`. Java 17 et Maven sont nécessaires pour le lancer directement, par exemple :
```powershell
cd backend/reservation-service
mvn spring-boot:run
```
Le service Utilisateur reste en Node.js / Express ; il utilise PostgreSQL, RabbitMQ et Eureka.

## Organisation
- `frontend/` : application Angular 17.
- `backend/` : microservices Spring Boot (Accommodation, Reservation, Maintenance, Colocation, Facturation, Notification) et `user-service/` en Node.js / Express.
- `infra/` : Gateway, Eureka, scripts SQL et configuration Keycloak.
- `docker-compose.yml` : orchestration de l'environnement complet.

## Architecture interne des microservices Spring Boot
Chaque microservice métier suit la même répartition en couches (modèle du projet `candidat`) :
```
com.stayeasy.<service>
├── config/       configuration (RabbitMQ, OpenAPI)
├── controller/   endpoints REST
├── dto/          objets Request / Response
├── entity/       entités JPA
├── exception/    exceptions métier + GlobalExceptionHandler
├── mapper/       conversion entité <-> DTO
├── repository/   interfaces Spring Data JPA
├── service/      logique métier (+ EventPublisher RabbitMQ)
├── client/       clients Feign vers les autres services (si besoin)
├── listener/     consommateurs RabbitMQ (si besoin)
└── <Service>Application.java
```
Chaque service utilise `src/main/resources/application.properties` (Swagger, Actuator, Eureka, info.app.*).

## Remarque
Les microservices communiquent via la Gateway et Eureka ; RabbitMQ transporte les événements métier. Les identifiants de démonstration sont réservés à un environnement local de cours : ne pas les utiliser en production.
