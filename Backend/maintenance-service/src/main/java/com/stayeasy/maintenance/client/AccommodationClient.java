package com.stayeasy.maintenance.client;

import com.stayeasy.maintenance.dto.RoomStatusRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "accommodation")
public interface AccommodationClient {

    @PutMapping("/api/rooms/{id}/status")
    Map<String, Object> setStatus(@PathVariable("id") Long id, @RequestBody RoomStatusRequest body);
}
