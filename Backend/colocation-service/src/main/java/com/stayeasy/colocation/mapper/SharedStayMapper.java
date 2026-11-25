package com.stayeasy.colocation.mapper;

import com.stayeasy.colocation.dto.SharedStayRequest;
import com.stayeasy.colocation.dto.SharedStayResponse;
import com.stayeasy.colocation.dto.StayMemberResponse;
import com.stayeasy.colocation.entity.SharedStay;
import com.stayeasy.colocation.entity.StayMember;
import org.springframework.stereotype.Component;

@Component
public class SharedStayMapper {

    public SharedStay toEntity(SharedStayRequest r) {
        SharedStay e = new SharedStay();
        e.setTitle(r.getTitle());
        e.setDescription(r.getDescription());
        e.setStartDate(r.getStartDate());
        e.setEndDate(r.getEndDate());
        e.setMaxParticipants(r.getMaxParticipants());
        e.setExperienceType(r.getExperienceType());
        e.setAccommodationId(r.getAccommodationId());
        e.setCreatorId(r.getCreatorId());
        return e;
    }

    public SharedStayResponse toResponse(SharedStay e) {
        SharedStayResponse r = new SharedStayResponse();
        r.setId(e.getId());
        r.setTitle(e.getTitle());
        r.setDescription(e.getDescription());
        r.setStartDate(e.getStartDate());
        r.setEndDate(e.getEndDate());
        r.setMaxParticipants(e.getMaxParticipants());
        r.setCurrentParticipants(e.getCurrentParticipants());
        r.setExperienceType(e.getExperienceType());
        r.setStatus(e.getStatus());
        r.setAccommodationId(e.getAccommodationId());
        r.setCreatorId(e.getCreatorId());
        return r;
    }

    public StayMemberResponse toResponse(StayMember e) {
        StayMemberResponse r = new StayMemberResponse();
        r.setId(e.getId());
        r.setSharedStayId(e.getSharedStayId());
        r.setUserId(e.getUserId());
        r.setRole(e.getRole());
        r.setMembershipStatus(e.getMembershipStatus());
        r.setJoinDate(e.getJoinDate());
        return r;
    }
}
