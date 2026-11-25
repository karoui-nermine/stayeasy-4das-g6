package com.stayeasy.user;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User,Long> { Optional<User> findByKeycloakId(String keycloakId); boolean existsByKeycloakId(String keycloakId); }