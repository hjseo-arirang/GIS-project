package com.gisproject.skydatageo.provider;

import com.gisproject.skydatageo.model.dto.AircraftDTO;

import java.util.List;

public interface FlightDataProvider {
    List<AircraftDTO> fetchAircraftData();
}
