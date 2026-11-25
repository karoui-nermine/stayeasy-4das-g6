package com.stayeasy.accommodation.repository;

import com.stayeasy.accommodation.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
}
