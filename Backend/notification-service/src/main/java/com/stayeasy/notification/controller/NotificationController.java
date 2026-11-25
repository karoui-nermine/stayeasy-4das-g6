package com.stayeasy.notification.controller;

import com.stayeasy.notification.dto.NotificationResponse;
import com.stayeasy.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @GetMapping
    public List<NotificationResponse> all(@RequestParam(required = false) String userId) {
        return service.findAll(userId);
    }

    @GetMapping("/{id}")
    public NotificationResponse one(@PathVariable Long id) {
        return service.findById(id);
    }

    @PatchMapping("/{id}/read")
    public NotificationResponse read(@PathVariable Long id) {
        return service.markAsRead(id);
    }

    @PostMapping("/{id}/retry")
    public NotificationResponse retry(@PathVariable Long id) {
        return service.retry(id);
    }
}
