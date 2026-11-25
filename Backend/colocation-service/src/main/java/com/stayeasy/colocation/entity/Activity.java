package com.stayeasy.colocation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sharedStayId;
    private String title;
    private LocalDateTime activityDate;
    private String location;
}
