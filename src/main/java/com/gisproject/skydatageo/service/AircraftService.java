package com.gisproject.skydatageo.service;

import com.gisproject.skydatageo.model.dto.AircraftDTO;
import com.gisproject.skydatageo.model.entity.Aircraft;
import com.gisproject.skydatageo.provider.FlightDataProvider;
import com.gisproject.skydatageo.repository.AircraftRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AircraftService {

    private final AircraftRepository repository;
    private final FlightDataProvider flightDataProvider;
    /*
    /* DB 요구 포맷으로 변환하기 위한 준비
     */
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private final Random random = new Random();

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    @Transactional
    public void updateAircraftLocations() { // 서버에서 주기적으로 실행하게 될 함수 (DB 저장용)
        List<Aircraft> aircrafts = new ArrayList<>();

        List<AircraftDTO> aircraftDTOs = flightDataProvider.fetchAircraftData();

        aircraftDTOs.forEach(aircraftDTO -> {
            Point point = geometryFactory.createPoint(new Coordinate(aircraftDTO.longitude(), aircraftDTO.latitude()));
            LocalDateTime lastSeen = LocalDateTime.ofInstant(Instant.ofEpochSecond(aircraftDTO.lastContactTime().longValue()),
                                                                ZoneId.systemDefault());
            String callSign = aircraftDTO.callSign() != null ? aircraftDTO.callSign().trim() : "N/A";

            Aircraft aircraft = Aircraft.builder()
                    .flightId(aircraftDTO.icao24())
                    .callSign(callSign)
                    .velocity(aircraftDTO.velocity())
                    .heading(aircraftDTO.trueTrack())
                    .location(point)
                    .lastSeen(lastSeen)
                    .build();

            aircrafts.add(aircraft);

        });

        // save()는 PK가 있으면 Update, 없으면 Insert (Upsert 동작)
        repository.saveAll(aircrafts);
    }
}