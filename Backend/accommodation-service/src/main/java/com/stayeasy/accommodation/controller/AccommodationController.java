package com.stayeasy.accommodation.controller;

import com.stayeasy.accommodation.dto.AccommodationRequest;
import com.stayeasy.accommodation.dto.AccommodationResponse;
import com.stayeasy.accommodation.service.AccommodationService;
import com.stayeasy.accommodation.dto.RoomRequest;
import com.stayeasy.accommodation.dto.RoomResponse;
import com.stayeasy.accommodation.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
@Tag(name = "Hebergements")
public class AccommodationController {

    private final AccommodationService service;
    private final RoomService roomService;

    public AccommodationController(AccommodationService service, RoomService roomService) {
        this.service = service;
        this.roomService = roomService;
    }

    @GetMapping
    public List<AccommodationResponse> all(@RequestParam(required = false) String city, @RequestParam(required = false) String type) {
        return service.findAll(city, type);
    }

    @GetMapping("/{id}")
    public AccommodationResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccommodationResponse create(@RequestBody AccommodationRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public AccommodationResponse update(@PathVariable Long id, @RequestBody AccommodationRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}/rooms")
    public List<RoomResponse> rooms(@PathVariable Long id) {
        return roomService.findByAccommodation(id);
    }

    @PostMapping("/{id}/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse addRoom(@PathVariable Long id, @RequestBody RoomRequest request) {
        return roomService.addToAccommodation(id, request);
    }
}
