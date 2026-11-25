package com.stayeasy.colocation.controller;

import com.stayeasy.colocation.dto.FeedbackRequest;
import com.stayeasy.colocation.dto.FeedbackResponse;
import com.stayeasy.colocation.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedback")
@Tag(name = "Feedbacks")
public class FeedbackController {

    private final FeedbackService service;

    public FeedbackController(FeedbackService service) {
        this.service = service;
    }

    @GetMapping
    public List<FeedbackResponse> all(@RequestParam(required = false) Long sharedStayId) {
        return service.findAll(sharedStayId);
    }

    @GetMapping("/{id}")
    public FeedbackResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FeedbackResponse create(@RequestBody FeedbackRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public FeedbackResponse update(@PathVariable Long id, @RequestBody FeedbackRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
