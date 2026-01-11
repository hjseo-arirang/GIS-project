package com.gisproject.skydatageo.model.entity;

import com.gisproject.skydatageo.model.dto.AircraftDTO;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@MappedSuperclass // 상속받는 자식 entity에 필드만 전달
@SuperBuilder // Lombok에서 몰래 전체 인자 생성자를 만들기 때문에, NoArgs 생성자를 명시적으로 줘야 함
@NoArgsConstructor
public abstract class BaseAircraft {
    protected String callSign; // 비행편 이름

    protected Double velocity; // 속도
    protected Double heading;  // 방향 (0~360도)

    // PostGIS의 Geometry 컬럼과 매핑 (SRID 4326 명시)
    @Column(columnDefinition = "geometry(Point, 4326)")
    protected Point location;

    // 데이터 갱신 시간
    protected java.time.LocalDateTime lastSeen;


    // DB 요구 포맷으로 변환하기 위한 준비
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public void updateFromDTO(AircraftDTO dto) {
        Point point = geometryFactory.createPoint(new Coordinate(dto.longitude(), dto.latitude()));
        LocalDateTime lastSeen = LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.lastContactTime().longValue()),
                ZoneId.systemDefault());
        String callSign = dto.callSign() != null ? dto.callSign().trim() : "N/A";

        this.callSign = callSign;
        this.velocity = dto.velocity();
        this.heading = dto.trueTrack();
        this.location = point;
        this.lastSeen = lastSeen;
    }

}
