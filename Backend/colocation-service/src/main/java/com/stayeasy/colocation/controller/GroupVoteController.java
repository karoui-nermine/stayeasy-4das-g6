package com.stayeasy.colocation.controller;

import com.stayeasy.colocation.dto.GroupVoteRequest;
import com.stayeasy.colocation.dto.GroupVoteResponse;
import com.stayeasy.colocation.service.GroupVoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/votes")
@Tag(name = "Votes de groupe")
public class GroupVoteController {

    private final GroupVoteService service;

    public GroupVoteController(GroupVoteService service) {
        this.service = service;
    }

    @GetMapping
    public List<GroupVoteResponse> all(@RequestParam(required = false) Long sharedStayId) {
        return service.findAll(sharedStayId);
    }

    @GetMapping("/{id}")
    public GroupVoteResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupVoteResponse create(@RequestBody GroupVoteRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public GroupVoteResponse update(@PathVariable Long id, @RequestBody GroupVoteRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
