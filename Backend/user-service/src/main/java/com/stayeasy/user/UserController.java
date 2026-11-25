package com.stayeasy.user;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/users")
public class UserController {
 private static final Set<String> ROLES=Set.of("VOYAGEUR_ETUDIANT","PROPRIETAIRE","EQUIPE_TECHNIQUE");
 private static final Set<String> STATUTS=Set.of("ACTIF","SUSPENDU","DESACTIVE");
 private final UserRepository repo; private final RabbitTemplate rabbit;
 public UserController(UserRepository repo, RabbitTemplate rabbit){this.repo=repo;this.rabbit=rabbit;}
 public record CreateUser(@NotBlank String keycloakId,String nom,String prenom,@Email @NotBlank String email,String telephone,@NotBlank String role){}
 private User find(Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));}
 private Map<String,Object> view(User u){
  Map<String,Object> m=new LinkedHashMap<>(); m.put("id",u.getId());m.put("keycloakId",u.getKeycloakId());m.put("nom",u.getNom());m.put("prenom",u.getPrenom());m.put("email",u.getEmail());m.put("telephone",u.getTelephone());m.put("role",u.getRole());m.put("statut",u.getStatut());m.put("dateInscription",u.getDateInscription());return m;
 }
 @PostMapping public ResponseEntity<?> create(@Valid @RequestBody CreateUser b){
  if(!ROLES.contains(b.role())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Rôle invalide");
  if(repo.existsByKeycloakId(b.keycloakId())) throw new ResponseStatusException(HttpStatus.CONFLICT,"Utilisateur existant");
  User u=new User();u.setKeycloakId(b.keycloakId());u.setNom(b.nom());u.setPrenom(b.prenom());u.setEmail(b.email());u.setTelephone(b.telephone());u.setRole(b.role());u=repo.save(u);
  try{rabbit.convertAndSend("stayeasy.events","user.registered",Map.of("userId",b.keycloakId(),"email",b.email(),"role",b.role()));}catch(Exception ignored){}
  return ResponseEntity.status(201).body(view(u));
 }
 @GetMapping("/keycloak/{kid}") public Map<String,Object> byKeycloak(@PathVariable String kid){return view(repo.findByKeycloakId(kid).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND)));}
 @GetMapping("/{id}") public Map<String,Object> get(@PathVariable Long id){return view(find(id));}
 @PutMapping("/{id}") public Map<String,Object> update(@PathVariable Long id,@RequestBody Map<String,Object> b){
  User u=find(id); if(b.containsKey("role")){String role=String.valueOf(b.get("role"));if(!ROLES.contains(role))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Rôle invalide");u.setRole(role);}
  if(b.containsKey("nom"))u.setNom((String)b.get("nom"));if(b.containsKey("prenom"))u.setPrenom((String)b.get("prenom"));if(b.containsKey("email"))u.setEmail((String)b.get("email"));if(b.containsKey("telephone"))u.setTelephone((String)b.get("telephone"));return view(repo.save(u));
 }
 @GetMapping("/{id}/profil") public Map<String,Object> profile(@PathVariable Long id){User u=find(id);Map<String,Object> m=view(u);m.put("profilVoyageur",u.getProfilVoyageur());m.put("profilProprietaire",u.getProfilProprietaire());return m;}
 @PutMapping("/{id}/profil-voyageur") public Object traveller(@PathVariable Long id,@RequestBody JsonNode body){User u=find(id);u.setProfilVoyageur(body);repo.save(u);return body;}
 @PutMapping("/{id}/profil-proprietaire") public Object owner(@PathVariable Long id,@RequestBody JsonNode body){User u=find(id);u.setProfilProprietaire(body);repo.save(u);return body;}
 @PutMapping("/{id}/statut") public Map<String,Object> status(@PathVariable Long id,@RequestBody Map<String,String> body){String s=body.get("statut");if(!STATUTS.contains(s))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Statut invalide");User u=find(id);u.setStatut(s);return view(repo.save(u));}
}