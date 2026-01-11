package com.gisproject.skydatageo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "current_aircrafts")
@SuperBuilder
@NoArgsConstructor
public class Aircraft extends BaseAircraft {
    @Id
    @Column(name = "flight_id")
    private String flightId; // 비행편명 (PK)
    //TODO 추후 마이그레이션을 고려한 필드 추가 필요

}