package com.stayeasy.accommodation.controller;

import com.stayeasy.accommodation.dto.RoomRequest;
import com.stayeasy.accommodation.dto.RoomResponse;
import com.stayeasy.accommodation.service.RoomService;
import com.stayeasy.accommodation.dto.RoomStatusRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Chambres")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @GetMapping
    public List<RoomResponse> all(@RequestParam(required = false) Long accommodationId) {
        return service.findAll(accommodationId);
    }

    @GetMapping("/{id}")
    public RoomResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse create(@RequestBody RoomRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public RoomResponse update(@PathVariable Long id, @RequestBody RoomRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PutMapping("/{id}/status")
    public RoomResponse status(@PathVariable Long id, @RequestBody RoomStatusRequest request) {
        return service.updateStatus(id, request.getStatus());
    }
}
