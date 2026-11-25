package com.stayeasy.notification.mapper;

import com.stayeasy.notification.dto.NotificationResponse;
import com.stayeasy.notification.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification e) {
        NotificationResponse r = new NotificationResponse();
        r.setId(e.getId());
        r.setUserId(e.getUserId());
        r.setType(e.getType());
        r.setTitle(e.getTitle());
        r.setMessage(e.getMessage());
        r.setChannel(e.getChannel());
        r.setStatus(e.getStatus());
        r.setCreatedAt(e.getCreatedAt());
        r.setReadAt(e.getReadAt());
        return r;
    }
}
