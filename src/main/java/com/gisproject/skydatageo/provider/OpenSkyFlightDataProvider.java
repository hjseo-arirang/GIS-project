package com.gisproject.skydatageo.provider;

import com.gisproject.skydatageo.model.dto.AircraftDTO;
import org.opensky.api.OpenSkyApi;
import org.opensky.model.OpenSkyStates;
import org.opensky.model.StateVector;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Profile("real_server")
public class OpenSkyFlightDataProvider implements FlightDataProvider {
    private final OpenSkyApi api = new OpenSkyApi("아이디", "비번"); //TODO 아이디/비번 업데이트

    private final String[] KOREA_LOCATION_DATA = {
            "33.0", // min latitude
            "124.0", // min longitude
            "38.0", // max latitude
            "132.0" // max longitude
    };

    @Override
    public List<AircraftDTO> fetchAircraftData() {
        List<AircraftDTO> aircraftDTOList = new ArrayList<>();

        OpenSkyStates openSkyStates = null;
        try {
            // getStates(최소경도, 최소위도, 최대경도, 최대위도)
            // 라이브러리 스펙: getStates(time, icao24, lamin, lomin, lamax, lomax)
            // time: 0 인 경우 가장 최근의 데이터를 가져오는 모양
            openSkyStates = api.getStates(0, KOREA_LOCATION_DATA);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (openSkyStates != null && openSkyStates.getStates() != null) {
            // 3. 변환 작업
            aircraftDTOList.addAll(
                    openSkyStates.getStates()
                            .stream()
                            .map(this::convertToDTO)
                            .toList()); //FIXME unmodified
        }

        return aircraftDTOList;
    }

    private AircraftDTO convertToDTO(StateVector stateVector) {
        if (stateVector.getLongitude() == null || stateVector.getLatitude() == null) {
            return null;
        }

        return new AircraftDTO(
                stateVector.getIcao24(),
                stateVector.getCallsign(),
                stateVector.getLongitude(),
                stateVector.getLatitude(),
                stateVector.getVelocity(),
                stateVector.getHeading(),
                stateVector.getLastPositionUpdate()
        );
    }
}
