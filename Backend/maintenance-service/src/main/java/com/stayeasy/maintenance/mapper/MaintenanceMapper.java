package com.stayeasy.maintenance.mapper;

import com.stayeasy.maintenance.dto.MaintenanceRequest;
import com.stayeasy.maintenance.dto.MaintenanceResponse;
import com.stayeasy.maintenance.entity.Maintenance;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceMapper {

    public Maintenance toEntity(MaintenanceRequest r) {
        Maintenance e = new Maintenance();
        e.setTitle(r.getTitle());
        e.setDescription(r.getDescription());
        e.setRoomId(r.getRoomId());
        e.setAssignedTechnicianId(r.getAssignedTechnicianId());
        e.setReporterId(r.getReporterId());
        if (r.getPriority() != null) e.setPriority(r.getPriority());
        return e;
    }

    public MaintenanceResponse toResponse(Maintenance e) {
        MaintenanceResponse r = new MaintenanceResponse();
        r.setId(e.getId());
        r.setTitle(e.getTitle());
        r.setDescription(e.getDescription());
        r.setReportDate(e.getReportDate());
        r.setStatus(e.getStatus());
        r.setPriority(e.getPriority());
        r.setRoomId(e.getRoomId());
        r.setAssignedTechnicianId(e.getAssignedTechnicianId());
        r.setReporterId(e.getReporterId());
        r.setRoomSyncPending(e.getRoomSyncPending());
        return r;
    }
}
