package com.stayeasy.accommodation.service;

import com.stayeasy.accommodation.dto.RoomRequest;
import com.stayeasy.accommodation.dto.RoomResponse;
import com.stayeasy.accommodation.entity.Room;
import com.stayeasy.accommodation.exception.ResourceNotFoundException;
import com.stayeasy.accommodation.mapper.RoomMapper;
import com.stayeasy.accommodation.repository.RoomRepository;
import com.stayeasy.accommodation.exception.BadRequestException;
import com.stayeasy.accommodation.repository.AccommodationRepository;
import java.util.Set;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository repository;
    private final RoomMapper mapper;
    private final AccommodationRepository accommodationRepository;

    public RoomService(RoomRepository repository, RoomMapper mapper, AccommodationRepository accommodationRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.accommodationRepository = accommodationRepository;
    }

    public List<RoomResponse> findAll(Long accommodationId) {
        Room probe = new Room();
        probe.setAccommodationId(accommodationId);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "number", "type", "capacity", "price", "status");
        return repository.findAll(Example.of(probe, matcher)).stream().map(mapper::toResponse).toList();
    }

    public RoomResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    public Room getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room introuvable : " + id));
    }

    public RoomResponse create(RoomRequest request) {
        Room entity = mapper.toEntity(request);
        entity.setId(null);
        return mapper.toResponse(repository.save(entity));
    }

    public RoomResponse update(Long id, RoomRequest request) {
        Room entity = getEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        getEntity(id);
        repository.deleteById(id);
    }

    private static final Set<String> STATUSES =
            Set.of("DISPONIBLE", "OCCUPEE", "EN_MAINTENANCE", "HORS_SERVICE", "PARTAGEE");

    public List<RoomResponse> findByAccommodation(Long accommodationId) {
        checkAccommodation(accommodationId);
        return repository.findByAccommodationId(accommodationId).stream().map(mapper::toResponse).toList();
    }

    public RoomResponse addToAccommodation(Long accommodationId, RoomRequest request) {
        checkAccommodation(accommodationId);
        Room room = mapper.toEntity(request);
        room.setId(null);
        room.setAccommodationId(accommodationId);
        return mapper.toResponse(repository.save(room));
    }

    public RoomResponse updateStatus(Long id, String status) {
        Room room = getEntity(id);
        if (status == null || !STATUSES.contains(status)) {
            throw new BadRequestException("Statut invalide");
        }
        room.setStatus(status);
        return mapper.toResponse(repository.save(room));
    }

    private void checkAccommodation(Long id) {
        if (!accommodationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hébergement introuvable : " + id);
        }
    }
}
