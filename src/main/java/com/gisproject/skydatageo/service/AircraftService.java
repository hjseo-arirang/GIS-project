package com.gisproject.skydatageo.service;

import com.gisproject.skydatageo.model.dto.AircraftDTO;
import com.gisproject.skydatageo.model.entity.Aircraft;
import com.gisproject.skydatageo.model.entity.AircraftHistory;
import com.gisproject.skydatageo.provider.FlightDataProvider;
import com.gisproject.skydatageo.repository.AircraftRepository;
import com.gisproject.skydatageo.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AircraftService {

    private final AircraftRepository currentRepository;
    private final HistoryRepository historyRepository;
    private final FlightDataProvider flightDataProvider;
    private final Random random = new Random();
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    @Transactional
    public void updateAircraftLocations() { // 서버에서 주기적으로 실행하게 될 함수 (DB 저장용)
        List<Aircraft> aircrafts = new ArrayList<>();
        List<AircraftHistory> aircraftHistories = new ArrayList<>();

        List<AircraftDTO> aircraftDTOs = flightDataProvider.fetchAircraftData();

        aircraftDTOs.forEach(aircraftDTO -> {

            currentRepository.findById(aircraftDTO.icao24()).ifPresentOrElse(
                    existAircraft -> {
                        // current_aircraft 테이블에 존재하는, 이미 식별된 비행기일 경우
                        existAircraft.updateFromDTO(aircraftDTO);
                        aircrafts.add(existAircraft);
                    },
                    () -> {
                        // 처음 등장하는 신규 비행기
                        Aircraft newAircraft = Aircraft.builder().flightId(aircraftDTO.icao24()).build();
                        newAircraft.updateFromDTO(aircraftDTO);
                        aircrafts.add(newAircraft);
                    }
            );

            AircraftHistory aircraftHistory = AircraftHistory.builder().flightId(aircraftDTO.icao24()).build();
            aircraftHistories.add(aircraftHistory);
        });

        // save()는 PK가 있으면 Update, 없으면 Insert (Upsert 동작)
        currentRepository.saveAll(aircrafts);
        historyRepository.saveAll(aircraftHistories);
    }

    @Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
    @Transactional
    public void normalizedAircraftPath() {

    }
}