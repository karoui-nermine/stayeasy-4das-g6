package com.stayeasy.colocation.service;

import com.stayeasy.colocation.dto.GroupVoteRequest;
import com.stayeasy.colocation.dto.GroupVoteResponse;
import com.stayeasy.colocation.entity.GroupVote;
import com.stayeasy.colocation.exception.ResourceNotFoundException;
import com.stayeasy.colocation.mapper.GroupVoteMapper;
import com.stayeasy.colocation.repository.GroupVoteRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupVoteService {

    private final GroupVoteRepository repository;
    private final GroupVoteMapper mapper;

    public GroupVoteService(GroupVoteRepository repository, GroupVoteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<GroupVoteResponse> findAll(Long sharedStayId) {
        GroupVote probe = new GroupVote();
        probe.setSharedStayId(sharedStayId);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("id", "title", "deadline", "status", "options");
        return repository.findAll(Example.of(probe, matcher)).stream().map(mapper::toResponse).toList();
    }

    public GroupVoteResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    public GroupVote getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GroupVote introuvable : " + id));
    }

    public GroupVoteResponse create(GroupVoteRequest request) {
        GroupVote entity = mapper.toEntity(request);
        entity.setId(null);
        return mapper.toResponse(repository.save(entity));
    }

    public GroupVoteResponse update(Long id, GroupVoteRequest request) {
        GroupVote entity = getEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    public void delete(Long id) {
        getEntity(id);
        repository.deleteById(id);
    }
}
