package com.stayeasy.colocation.mapper;

import com.stayeasy.colocation.dto.ActivityRequest;
import com.stayeasy.colocation.dto.ActivityResponse;
import com.stayeasy.colocation.entity.Activity;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {

    public Activity toEntity(ActivityRequest r) {
        Activity e = new Activity();
        updateEntity(e, r);
        return e;
    }

    /** Copie uniquement les champs renseignes (les valeurs par defaut de l'entite sont conservees). */
    public void updateEntity(Activity e, ActivityRequest r) {
        if (r.getSharedStayId() != null) e.setSharedStayId(r.getSharedStayId());
        if (r.getTitle() != null) e.setTitle(r.getTitle());
        if (r.getActivityDate() != null) e.setActivityDate(r.getActivityDate());
        if (r.getLocation() != null) e.setLocation(r.getLocation());
    }

    public ActivityResponse toResponse(Activity e) {
        ActivityResponse r = new ActivityResponse();
        r.setId(e.getId());
        r.setSharedStayId(e.getSharedStayId());
        r.setTitle(e.getTitle());
        r.setActivityDate(e.getActivityDate());
        r.setLocation(e.getLocation());
        return r;
    }
}
