package com.stayeasy.colocation.service;

import com.stayeasy.colocation.dto.FeedbackRequest;
import com.stayeasy.colocation.dto.FeedbackResponse;
import com.stayeasy.colocation.entity.Feedback;
import com.stayeasy.colocation.exception.ResourceNotFoundException;
import com.stayeasy.colocation.mapper.FeedbackMapper;
import com.stayeasy.colocation.repository.FeedbackRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository repository;
    private final FeedbackMapper mapper;

    public FeedbackService(FeedbackRepository repository, FeedbackMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<FeedbackResponse> findAll(Long sharedStayId) {
        Feedback probe = new Feedback();
        probe.setSharedStayId(sharedStayId);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "userId", "rating", "comment");
        return repository.findAll(Example.of(probe, matcher)).stream().map(mapper::toResponse).toList();
    }

    public FeedbackResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    public Feedback getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback introuvable : " + id));
    }

    public FeedbackResponse create(FeedbackRequest request) {
        Feedback entity = mapper.toEntity(request);
        entity.setId(null);
        return mapper.toResponse(repository.save(entity));
    }

    public FeedbackResponse update(Long id, FeedbackRequest request) {
        Feedback entity = getEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        getEntity(id);
        repository.deleteById(id);
    }
}
