package com.stayeasy.accommodation.mapper;

import com.stayeasy.accommodation.dto.RoomRequest;
import com.stayeasy.accommodation.dto.RoomResponse;
import com.stayeasy.accommodation.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public Room toEntity(RoomRequest r) {
        Room e = new Room();
        updateEntity(e, r);
        return e;
    }

    /** Copie uniquement les champs renseignes (les valeurs par defaut de l'entite sont conservees). */
    public void updateEntity(Room e, RoomRequest r) {
        if (r.getNumber() != null) e.setNumber(r.getNumber());
        if (r.getType() != null) e.setType(r.getType());
        if (r.getCapacity() != null) e.setCapacity(r.getCapacity());
        if (r.getPrice() != null) e.setPrice(r.getPrice());
        if (r.getStatus() != null) e.setStatus(r.getStatus());
        if (r.getAccommodationId() != null) e.setAccommodationId(r.getAccommodationId());
    }

    public RoomResponse toResponse(Room e) {
        RoomResponse r = new RoomResponse();
        r.setId(e.getId());
        r.setNumber(e.getNumber());
        r.setType(e.getType());
        r.setCapacity(e.getCapacity());
        r.setPrice(e.getPrice());
        r.setStatus(e.getStatus());
        r.setAccommodationId(e.getAccommodationId());
        return r;
    }
}
