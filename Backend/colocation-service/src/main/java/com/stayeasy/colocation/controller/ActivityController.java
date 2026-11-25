package com.stayeasy.colocation.controller;

import com.stayeasy.colocation.dto.ActivityRequest;
import com.stayeasy.colocation.dto.ActivityResponse;
import com.stayeasy.colocation.service.ActivityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activities")
@Tag(name = "Activites")
public class ActivityController {

    private final ActivityService service;

    public ActivityController(ActivityService service) {
        this.service = service;
    }

    @GetMapping
    public List<ActivityResponse> all(@RequestParam(required = false) Long sharedStayId) {
        return service.findAll(sharedStayId);
    }

    @GetMapping("/{id}")
    public ActivityResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityResponse create(@RequestBody ActivityRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public ActivityResponse update(@PathVariable Long id, @RequestBody ActivityRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
