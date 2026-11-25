package com.stayeasy.accommodation.mapper;

import com.stayeasy.accommodation.dto.AccommodationRequest;
import com.stayeasy.accommodation.dto.AccommodationResponse;
import com.stayeasy.accommodation.entity.Accommodation;
import org.springframework.stereotype.Component;

@Component
public class AccommodationMapper {

    public Accommodation toEntity(AccommodationRequest r) {
        Accommodation e = new Accommodation();
        updateEntity(e, r);
        return e;
    }

    /** Copie uniquement les champs renseignes (les valeurs par defaut de l'entite sont conservees). */
    public void updateEntity(Accommodation e, AccommodationRequest r) {
        if (r.getName() != null) e.setName(r.getName());
        if (r.getDescription() != null) e.setDescription(r.getDescription());
        if (r.getAddress() != null) e.setAddress(r.getAddress());
        if (r.getCity() != null) e.setCity(r.getCity());
        if (r.getType() != null) e.setType(r.getType());
        if (r.getMaxCapacity() != null) e.setMaxCapacity(r.getMaxCapacity());
        if (r.getPricePerNight() != null) e.setPricePerNight(r.getPricePerNight());
        if (r.getOwnerId() != null) e.setOwnerId(r.getOwnerId());
        if (r.getStatus() != null) e.setStatus(r.getStatus());
    }

    public AccommodationResponse toResponse(Accommodation e) {
        AccommodationResponse r = new AccommodationResponse();
        r.setId(e.getId());
        r.setName(e.getName());
        r.setDescription(e.getDescription());
        r.setAddress(e.getAddress());
        r.setCity(e.getCity());
        r.setType(e.getType());
        r.setMaxCapacity(e.getMaxCapacity());
        r.setPricePerNight(e.getPricePerNight());
        r.setOwnerId(e.getOwnerId());
        r.setStatus(e.getStatus());
        return r;
    }
}
