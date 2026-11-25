package com.stayeasy.user;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
@Entity @Table(name="users")
public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="keycloak_id", unique=true, nullable=false) private String keycloakId;
 private String nom, prenom, email, telephone;
 @Column(nullable=false) private String role;
 private String statut="ACTIF";
 @Column(name="date_inscription") private LocalDateTime dateInscription=LocalDateTime.now();
 @JdbcTypeCode(SqlTypes.JSON) @Column(name="profil_voyageur",columnDefinition="jsonb") private JsonNode profilVoyageur;
 @JdbcTypeCode(SqlTypes.JSON) @Column(name="profil_proprietaire",columnDefinition="jsonb") private JsonNode profilProprietaire;
 public Long getId(){return id;} public String getKeycloakId(){return keycloakId;} public void setKeycloakId(String v){keycloakId=v;}
 public String getNom(){return nom;} public void setNom(String v){nom=v;} public String getPrenom(){return prenom;} public void setPrenom(String v){prenom=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;} public String getTelephone(){return telephone;} public void setTelephone(String v){telephone=v;}
 public String getRole(){return role;} public void setRole(String v){role=v;} public String getStatut(){return statut;} public void setStatut(String v){statut=v;}
 public LocalDateTime getDateInscription(){return dateInscription;} public JsonNode getProfilVoyageur(){return profilVoyageur;} public void setProfilVoyageur(JsonNode v){profilVoyageur=v;}
 public JsonNode getProfilProprietaire(){return profilProprietaire;} public void setProfilProprietaire(JsonNode v){profilProprietaire=v;}
}