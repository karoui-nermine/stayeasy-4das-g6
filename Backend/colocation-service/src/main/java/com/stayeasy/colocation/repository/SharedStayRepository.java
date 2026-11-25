package com.stayeasy.colocation.repository;

import com.stayeasy.colocation.entity.SharedStay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedStayRepository extends JpaRepository<SharedStay, Long> {
}
