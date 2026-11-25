package com.stayeasy.colocation.controller;

import com.stayeasy.colocation.dto.JoinRequest;
import com.stayeasy.colocation.dto.MatchRequest;
import com.stayeasy.colocation.dto.MatchResponse;
import com.stayeasy.colocation.dto.SharedStayRequest;
import com.stayeasy.colocation.dto.SharedStayResponse;
import com.stayeasy.colocation.dto.StayMemberResponse;
import com.stayeasy.colocation.service.SharedStayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Sejours partages")
public class SharedStayController {

    private final SharedStayService service;

    public SharedStayController(SharedStayService service) {
        this.service = service;
    }

    @GetMapping("/shared-stays")
    public List<SharedStayResponse> all() {
        return service.findAll();
    }

    @GetMapping("/shared-stays/{id}")
    public SharedStayResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/shared-stays/{id}/members")
    public List<StayMemberResponse> members(@PathVariable Long id) {
        return service.findMembers(id);
    }

    @PostMapping("/shared-stays")
    @ResponseStatus(HttpStatus.CREATED)
    public SharedStayResponse create(@RequestBody SharedStayRequest request) {
        return service.create(request);
    }

    @PostMapping("/shared-stays/{id}/join")
    public SharedStayResponse join(@PathVariable Long id, @Valid @RequestBody JoinRequest request) {
        return service.join(id, request.getUserId());
    }

    @PostMapping("/matching/calculate")
    public MatchResponse match(@Valid @RequestBody MatchRequest request) {
        return service.match(request);
    }
}
