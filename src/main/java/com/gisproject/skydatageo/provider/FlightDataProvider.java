package com.gisproject.skydatageo.provider;

import com.gisproject.skydatageo.model.dto.AircraftDTO;

import java.util.List;

public interface FlightDataProvider {

    /**
     * 최신 Aircraft 정보를 가져오는 함수
     * @return AricraftDTO의 리스트. 가져오는 데이터 형식에 맞춰서 관리
     */
    List<AircraftDTO> fetchAircraftData();
}
