package com.stayeasy.colocation.mapper;

import com.stayeasy.colocation.dto.GroupVoteRequest;
import com.stayeasy.colocation.dto.GroupVoteResponse;
import com.stayeasy.colocation.entity.GroupVote;
import org.springframework.stereotype.Component;

@Component
public class GroupVoteMapper {

    public GroupVote toEntity(GroupVoteRequest r) {
        GroupVote e = new GroupVote();
        updateEntity(e, r);
        return e;
    }

    /** Copie uniquement les champs renseignes (les valeurs par defaut de l'entite sont conservees). */
    public void updateEntity(GroupVote e, GroupVoteRequest r) {
        if (r.getSharedStayId() != null) e.setSharedStayId(r.getSharedStayId());
        if (r.getTitle() != null) e.setTitle(r.getTitle());
        if (r.getDeadline() != null) e.setDeadline(r.getDeadline());
        if (r.getStatus() != null) e.setStatus(r.getStatus());
        if (r.getOptions() != null) e.setOptions(r.getOptions());
    }

    public GroupVoteResponse toResponse(GroupVote e) {
        GroupVoteResponse r = new GroupVoteResponse();
        r.setId(e.getId());
        r.setSharedStayId(e.getSharedStayId());
        r.setTitle(e.getTitle());
        r.setDeadline(e.getDeadline());
        r.setStatus(e.getStatus());
        r.setOptions(e.getOptions());
        return r;
    }
}
