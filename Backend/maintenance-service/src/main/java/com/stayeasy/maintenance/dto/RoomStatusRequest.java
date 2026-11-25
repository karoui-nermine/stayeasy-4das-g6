package com.stayeasy.maintenance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Corps envoye a accommodation-service pour changer le statut d'une chambre. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomStatusRequest {
    private String status;
}
