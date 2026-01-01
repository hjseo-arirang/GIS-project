package com.gisproject.skydatageo.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point; // JTS 라이브러리 사용

@Entity
@Table(name = "current_aircrafts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aircraft {

    //TODO 추후 마이그레이션을 고려한 필드 추가 필요
    @Id
    @Column(name = "flight_id")
    private String flightId; // 비행편명 (PK)
    private String callSign; // 비행편 이름

    private Double velocity; // 속도
    private Double heading;  // 방향 (0~360도)

    // PostGIS의 Geometry 컬럼과 매핑 (SRID 4326 명시)
    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;

    // 데이터 갱신 시간
    private java.time.LocalDateTime lastSeen;
}