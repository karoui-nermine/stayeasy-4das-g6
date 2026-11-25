package com.stayeasy.reservation.controller;

import com.stayeasy.reservation.dto.ReservationRequest;
import com.stayeasy.reservation.dto.ReservationResponse;
import com.stayeasy.reservation.dto.ReservationUpdateRequest;
import com.stayeasy.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservationResponse> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ReservationResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/user/{userId}")
    public List<ReservationResponse> byUser(@PathVariable String userId) {
        return service.findByUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse create(@Valid @RequestBody ReservationRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ReservationResponse update(@PathVariable Long id, @RequestBody ReservationUpdateRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/confirm")
    public ReservationResponse confirm(@PathVariable Long id) {
        return service.confirm(id);
    }

    @PatchMapping("/{id}/cancel")
    public ReservationResponse cancel(@PathVariable Long id) {
        return service.cancel(id);
    }
}
