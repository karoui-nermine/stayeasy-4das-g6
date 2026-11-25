package com.stayeasy.reservation.client;

import com.stayeasy.reservation.dto.RoomDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "accommodation")
public interface AccommodationClient {

    @GetMapping("/api/rooms/{id}")
    RoomDto getRoom(@PathVariable("id") Long id);
}
