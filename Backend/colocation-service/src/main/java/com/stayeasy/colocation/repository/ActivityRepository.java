package com.stayeasy.colocation.repository;

import com.stayeasy.colocation.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
}
