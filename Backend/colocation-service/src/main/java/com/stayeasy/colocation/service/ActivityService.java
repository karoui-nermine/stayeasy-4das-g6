package com.stayeasy.colocation.service;

import com.stayeasy.colocation.dto.ActivityRequest;
import com.stayeasy.colocation.dto.ActivityResponse;
import com.stayeasy.colocation.entity.Activity;
import com.stayeasy.colocation.exception.ResourceNotFoundException;
import com.stayeasy.colocation.mapper.ActivityMapper;
import com.stayeasy.colocation.repository.ActivityRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository repository;
    private final ActivityMapper mapper;

    public ActivityService(ActivityRepository repository, ActivityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<ActivityResponse> findAll(Long sharedStayId) {
        Activity probe = new Activity();
        probe.setSharedStayId(sharedStayId);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "title", "activityDate", "location");
        return repository.findAll(Example.of(probe, matcher)).stream().map(mapper::toResponse).toList();
    }

    public ActivityResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    public Activity getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity introuvable : " + id));
    }

    public ActivityResponse create(ActivityRequest request) {
        Activity entity = mapper.toEntity(request);
        entity.setId(null);
        return mapper.toResponse(repository.save(entity));
    }

    public ActivityResponse update(Long id, ActivityRequest request) {
        Activity entity = getEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        getEntity(id);
        repository.deleteById(id);
    }
}
