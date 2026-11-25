package com.stayeasy.maintenance.service;

import com.stayeasy.maintenance.client.AccommodationClient;
import com.stayeasy.maintenance.dto.MaintenanceRequest;
import com.stayeasy.maintenance.dto.MaintenanceResponse;
import com.stayeasy.maintenance.dto.RoomStatusRequest;
import com.stayeasy.maintenance.entity.Maintenance;
import com.stayeasy.maintenance.exception.BadRequestException;
import com.stayeasy.maintenance.exception.ConflictException;
import com.stayeasy.maintenance.exception.ResourceNotFoundException;
import com.stayeasy.maintenance.mapper.MaintenanceMapper;
import com.stayeasy.maintenance.repository.MaintenanceRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class MaintenanceService {

    private static final Set<String> NEW_STATUSES = Set.of("IN_PROGRESS", "COMPLETED", "CANCELLED");

    private final MaintenanceRepository repository;
    private final MaintenanceMapper mapper;
    private final AccommodationClient accommodationClient;
    private final EventPublisher events;

    public MaintenanceService(MaintenanceRepository repository, MaintenanceMapper mapper,
                              AccommodationClient accommodationClient, EventPublisher events) {
        this.repository = repository;
        this.mapper = mapper;
        this.accommodationClient = accommodationClient;
        this.events = events;
    }

    public List<MaintenanceResponse> findAll(Long roomId, String status, String priority) {
        Maintenance probe = new Maintenance();
        probe.setRoomId(roomId);
        probe.setStatus(status);
        probe.setPriority(priority);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths(
                "id", "title", "description", "reportDate", "assignedTechnicianId", "reporterId", "roomSyncPending");
        return repository.findAll(Example.of(probe, matcher)).stream().map(mapper::toResponse).toList();
    }

    public MaintenanceResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    public MaintenanceResponse create(MaintenanceRequest request) {
        Maintenance entity = mapper.toEntity(request);
        entity.setId(null);
        entity.setStatus("OPEN");
        entity.setReportDate(LocalDateTime.now());
        Maintenance saved = repository.save(entity);
        syncRoom(saved, "EN_MAINTENANCE");
        return mapper.toResponse(saved);
    }

    public MaintenanceResponse assign(Long id, String technicianId) {
        Maintenance m = getEntity(id);
        m.setAssignedTechnicianId(technicianId);
        return mapper.toResponse(repository.save(m));
    }

    public MaintenanceResponse changeStatus(Long id, String status) {
        Maintenance m = getEntity(id);
        if (status == null || !NEW_STATUSES.contains(status)) {
            throw new BadRequestException("Statut invalide");
        }
        if ("COMPLETED".equals(m.getStatus()) || "CANCELLED".equals(m.getStatus())) {
            throw new ConflictException("Intervention clôturée");
        }
        m.setStatus(status);
        repository.save(m);
        if (!"IN_PROGRESS".equals(status)) {
            syncRoom(m, "DISPONIBLE");
        }
        if ("COMPLETED".equals(status) && m.getReporterId() != null) {
            publishCompleted(m);
        }
        return mapper.toResponse(m);
    }

    /** Met a jour le statut de la chambre ; en cas d'echec, la synchro est marquee "en attente". */
    private void syncRoom(Maintenance m, String roomStatus) {
        try {
            accommodationClient.setStatus(m.getRoomId(), new RoomStatusRequest(roomStatus));
            m.setRoomSyncPending(false);
        } catch (Exception e) {
            m.setRoomSyncPending(true);
        }
        repository.save(m);
    }

    private void publishCompleted(Maintenance m) {
        Map<String, Object> ev = new HashMap<>();
        ev.put("eventId", UUID.randomUUID().toString());
        ev.put("type", "maintenance.completed");
        ev.put("occurredAt", LocalDateTime.now().toString());
        ev.put("maintenanceId", m.getId());
        ev.put("roomId", m.getRoomId());
        ev.put("userId", m.getReporterId());
        events.publish("maintenance.completed", ev);
    }

    private Maintenance getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance introuvable : " + id));
    }
}
