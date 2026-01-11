package com.gisproject.skydatageo.model.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity // Repository 와 연동되게 하기 위해선 필요한 어노테이션
@Table(name = "aircraft_history")
@SuperBuilder
@NoArgsConstructor
public class AircraftHistory extends BaseAircraft {
    // 사용하진 않지만, DB 저장용 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_id")
    private String flightId; // 비행편명 (PK)
}