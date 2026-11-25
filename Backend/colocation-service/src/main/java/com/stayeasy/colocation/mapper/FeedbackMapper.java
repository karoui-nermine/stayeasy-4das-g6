package com.stayeasy.colocation.mapper;

import com.stayeasy.colocation.dto.FeedbackRequest;
import com.stayeasy.colocation.dto.FeedbackResponse;
import com.stayeasy.colocation.entity.Feedback;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    public Feedback toEntity(FeedbackRequest r) {
        Feedback e = new Feedback();
        updateEntity(e, r);
        return e;
    }

    /** Copie uniquement les champs renseignes (les valeurs par defaut de l'entite sont conservees). */
    public void updateEntity(Feedback e, FeedbackRequest r) {
        if (r.getSharedStayId() != null) e.setSharedStayId(r.getSharedStayId());
        if (r.getUserId() != null) e.setUserId(r.getUserId());
        if (r.getRating() != null) e.setRating(r.getRating());
        if (r.getComment() != null) e.setComment(r.getComment());
    }

    public FeedbackResponse toResponse(Feedback e) {
        FeedbackResponse r = new FeedbackResponse();
        r.setId(e.getId());
        r.setSharedStayId(e.getSharedStayId());
        r.setUserId(e.getUserId());
        r.setRating(e.getRating());
        r.setComment(e.getComment());
        return r;
    }
}
