package com.stayeasy.colocation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class StayMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sharedStayId;
    private String userId;
    private String role = "MEMBER";
    private String membershipStatus = "ACCEPTED";
    private LocalDate joinDate;
}
