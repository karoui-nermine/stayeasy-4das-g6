package com.stayeasy.accommodation.service;

import com.stayeasy.accommodation.dto.AccommodationRequest;
import com.stayeasy.accommodation.dto.AccommodationResponse;
import com.stayeasy.accommodation.entity.Accommodation;
import com.stayeasy.accommodation.exception.ResourceNotFoundException;
import com.stayeasy.accommodation.mapper.AccommodationMapper;
import com.stayeasy.accommodation.repository.AccommodationRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccommodationService {

    private final AccommodationRepository repository;
    private final AccommodationMapper mapper;

    public AccommodationService(AccommodationRepository repository, AccommodationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<AccommodationResponse> findAll(String city, String type) {
        Accommodation probe = new Accommodation();
        probe.setCity(city);
        probe.setType(type);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "name", "description", "address", "maxCapacity", "pricePerNight", "ownerId", "status");
        return repository.findAll(Example.of(probe, matcher)).stream().map(mapper::toResponse).toList();
    }

    public AccommodationResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    public Accommodation getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation introuvable : " + id));
    }

    public AccommodationResponse create(AccommodationRequest request) {
        Accommodation entity = mapper.toEntity(request);
        entity.setId(null);
        return mapper.toResponse(repository.save(entity));
    }

    public AccommodationResponse update(Long id, AccommodationRequest request) {
        Accommodation entity = getEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        getEntity(id);
        repository.deleteById(id);
    }
}
