package com.stayeasy.maintenance.controller;

import com.stayeasy.maintenance.dto.AssignmentRequest;
import com.stayeasy.maintenance.dto.MaintenanceRequest;
import com.stayeasy.maintenance.dto.MaintenanceResponse;
import com.stayeasy.maintenance.dto.StatusRequest;
import com.stayeasy.maintenance.service.MaintenanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
@Tag(name = "Maintenances")
public class MaintenanceController {

    private final MaintenanceService service;

    public MaintenanceController(MaintenanceService service) {
        this.service = service;
    }

    @GetMapping
    public List<MaintenanceResponse> all(@RequestParam(required = false) Long roomId,
                                         @RequestParam(required = false) String status,
                                         @RequestParam(required = false) String priority) {
        return service.findAll(roomId, status, priority);
    }

    @GetMapping("/{id}")
    public MaintenanceResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceResponse create(@Valid @RequestBody MaintenanceRequest request) {
        return service.create(request);
    }

    @PatchMapping("/{id}/assignment")
    public MaintenanceResponse assign(@PathVariable Long id, @RequestBody AssignmentRequest request) {
        return service.assign(id, request.getTechnicianId());
    }

    @PatchMapping("/{id}/status")
    public MaintenanceResponse status(@PathVariable Long id, @RequestBody StatusRequest request) {
        return service.changeStatus(id, request.getStatus());
    }
}
