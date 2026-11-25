package com.stayeasy.colocation.repository;

import com.stayeasy.colocation.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
