package com.stayeasy.colocation.client;

import com.stayeasy.colocation.dto.AccommodationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "accommodation")
public interface AccommodationClient {

    @GetMapping("/api/accommodations/{id}")
    AccommodationDto get(@PathVariable("id") Long id);
}
