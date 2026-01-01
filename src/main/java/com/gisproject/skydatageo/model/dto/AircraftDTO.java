package com.gisproject.skydatageo.model.dto;

public record AircraftDTO(
        String icao24,
        String callSign,
        Double longitude,
        Double latitude,
        Double velocity,
        Double trueTrack,
        Double lastContactTime
) {
}