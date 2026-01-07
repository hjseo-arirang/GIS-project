package com.gisproject.skydatageo.provider;

import com.gisproject.skydatageo.model.dto.AircraftDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/*
 AI로 생성한, 가짜 비행 데이터 (구현할 때 사용)
 */
@Service
@Profile("local")
public class DummyFlightDataProvider implements FlightDataProvider {
    // 내부 상태 관리를 위한 가변 클래스 (비행 시뮬레이션용)
    private static class FlightState {
        String icao24;
        String callSign;
        double longitude;
        double latitude;
        double velocity; // m/s
        double trueTrack; // 0~360도

        public FlightState(String icao24, String callSign, double lat, double lon, double velocity, double track) {
            this.icao24 = icao24;
            this.callSign = callSign;
            this.latitude = lat;
            this.longitude = lon;
            this.velocity = velocity;
            this.trueTrack = track;
        }

        // 간단한 이동 로직: 현재 속도와 방향으로 위치 업데이트
        public void move() {
            // 1도당 약 111km 가정. 매우 단순화된 계산입니다.
            // 0.00001도 ≒ 1.1m
            double distanceFactor = 0.00001;

            // 방향(track)에 따라 위도/경도 변경 (라디안 변환 필요)
            double rad = Math.toRadians(trueTrack);

            // 위도 변화 (Y축): cos
            this.latitude += (velocity * Math.cos(rad)) * distanceFactor;
            // 경도 변화 (X축): sin
            this.longitude += (velocity * Math.sin(rad)) * distanceFactor;

            // 약간의 랜덤성을 추가하여 자연스러운 경로 변경 (선택 사항)
            // 방향을 -1 ~ +1도 사이로 살짝 틈
            this.trueTrack += (Math.random() * 2.0) - 1.0;
        }
    }

    private final List<FlightState> simulatedFlights = new ArrayList<>();
    private final Random random = new Random();

    public DummyFlightDataProvider() {
        // 초기 더미 데이터 생성 (인천 공항 및 서울 근교 좌표 기준)
        simulatedFlights.add(new FlightState("71c001", "KAL001", 37.460, 126.440, 250.0, 45.0));  // 인천 출발 북동쪽
        simulatedFlights.add(new FlightState("71c002", "AAR202", 37.500, 126.800, 220.0, 270.0)); // 서울 상공 서쪽으로
        simulatedFlights.add(new FlightState("71c003", "JNA303", 37.200, 127.000, 200.0, 0.0));   // 경기 남부에서 북쪽으로
        simulatedFlights.add(new FlightState("71c004", "TWB404", 37.450, 126.400, 150.0, 180.0)); // 인천 근처 남쪽으로
        simulatedFlights.add(new FlightState("71c005", "EZE505", 37.600, 126.900, 100.0, 90.0));  // 서울 북부 동쪽으로
    }

    @Override
    public List<AircraftDTO> fetchAircraftData() {
        long currentTimestamp = Instant.now().getEpochSecond();

        return simulatedFlights.stream()
                .peek(FlightState::move) // 데이터를 가져갈 때마다 비행기를 이동시킴
                .map(flight -> new AircraftDTO(
                        flight.icao24,
                        flight.callSign,
                        flight.longitude,
                        flight.latitude,
                        flight.velocity,
                        flight.trueTrack,
                        (double) currentTimestamp // 마지막 연락 시간 갱신
                ))
                .collect(Collectors.toList());
    }
}
