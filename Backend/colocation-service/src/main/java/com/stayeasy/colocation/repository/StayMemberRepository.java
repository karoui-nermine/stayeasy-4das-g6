package com.stayeasy.colocation.repository;

import com.stayeasy.colocation.entity.StayMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StayMemberRepository extends JpaRepository<StayMember, Long> {
    List<StayMember> findBySharedStayId(Long sharedStayId);

    boolean existsBySharedStayIdAndUserId(Long sharedStayId, String userId);
}
