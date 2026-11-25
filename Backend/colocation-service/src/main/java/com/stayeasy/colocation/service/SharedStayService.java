package com.stayeasy.colocation.service;

import feign.FeignException;
import com.stayeasy.colocation.client.AccommodationClient;
import com.stayeasy.colocation.dto.AccommodationDto;
import com.stayeasy.colocation.dto.MatchRequest;
import com.stayeasy.colocation.dto.MatchResponse;
import com.stayeasy.colocation.dto.SharedStayRequest;
import com.stayeasy.colocation.dto.SharedStayResponse;
import com.stayeasy.colocation.dto.StayMemberResponse;
import com.stayeasy.colocation.entity.SharedStay;
import com.stayeasy.colocation.entity.StayMember;
import com.stayeasy.colocation.exception.ConflictException;
import com.stayeasy.colocation.exception.ResourceNotFoundException;
import com.stayeasy.colocation.exception.ServiceUnavailableException;
import com.stayeasy.colocation.mapper.SharedStayMapper;
import com.stayeasy.colocation.repository.SharedStayRepository;
import com.stayeasy.colocation.repository.StayMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SharedStayService {

    private final SharedStayRepository stays;
    private final StayMemberRepository members;
    private final SharedStayMapper mapper;
    private final AccommodationClient accommodationClient;
    private final EventPublisher events;

    public SharedStayService(SharedStayRepository stays, StayMemberRepository members, SharedStayMapper mapper,
                             AccommodationClient accommodationClient, EventPublisher events) {
        this.stays = stays;
        this.members = members;
        this.mapper = mapper;
        this.accommodationClient = accommodationClient;
        this.events = events;
    }

    public List<SharedStayResponse> findAll() {
        return stays.findAll().stream().map(mapper::toResponse).toList();
    }

    public SharedStayResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    public List<StayMemberResponse> findMembers(Long id) {
        getEntity(id);
        return members.findBySharedStayId(id).stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public SharedStayResponse create(SharedStayRequest request) {
        fetchAccommodation(request.getAccommodationId());
        SharedStay stay = mapper.toEntity(request);
        stay.setId(null);
        stay.setStatus("OPEN");
        stay.setCurrentParticipants(1);
        SharedStay saved = stays.save(stay);

        StayMember organizer = new StayMember();
        organizer.setSharedStayId(saved.getId());
        organizer.setUserId(request.getCreatorId());
        organizer.setRole("ORGANIZER");
        organizer.setJoinDate(LocalDate.now());
        members.save(organizer);
        return mapper.toResponse(saved);
    }

    @Transactional
    public SharedStayResponse join(Long id, String userId) {
        SharedStay stay = getEntity(id);
        if (!"OPEN".equals(stay.getStatus()) || members.existsBySharedStayIdAndUserId(id, userId)) {
            throw new ConflictException("Impossible de rejoindre ce séjour");
        }
        StayMember member = new StayMember();
        member.setSharedStayId(id);
        member.setUserId(userId);
        member.setJoinDate(LocalDate.now());
        members.save(member);

        stay.setCurrentParticipants(stay.getCurrentParticipants() + 1);
        if (stay.getMaxParticipants() != null && stay.getCurrentParticipants() >= stay.getMaxParticipants()) {
            stay.setStatus("FULL");
        }
        stays.save(stay);

        Map<String, Object> ev = new HashMap<>();
        ev.put("userId", userId);
        ev.put("sharedStayId", id);
        ev.put("title", stay.getTitle());
        events.publish("colocation.joined", ev);
        return mapper.toResponse(stay);
    }

    /** Score de compatibilite : 60 pts si le type d'experience est prefere, 40 pts si le budget couvre le prix. */
    public MatchResponse match(MatchRequest request) {
        SharedStay stay = getEntity(request.getSharedStayId());
        List<String> prefs = request.getPreferences() != null ? request.getPreferences() : List.of();
        double budget = request.getBudgetMax() != null ? request.getBudgetMax() : 0;
        double price = fetchAccommodation(stay.getAccommodationId()).getPricePerNight().doubleValue();
        int score = (prefs.contains(stay.getExperienceType()) ? 60 : 0) + (budget >= price ? 40 : 0);
        return new MatchResponse(stay.getId(), score);
    }

    private AccommodationDto fetchAccommodation(Long accommodationId) {
        try {
            return accommodationClient.get(accommodationId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Hébergement introuvable");
        } catch (Exception e) {
            throw new ServiceUnavailableException("Service Accommodation indisponible");
        }
    }

    private SharedStay getEntity(Long id) {
        return stays.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Séjour partagé introuvable : " + id));
    }
}
