package com.gisproject.skydatageo.repository;

import com.gisproject.skydatageo.model.entity.AircraftHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository  extends JpaRepository<AircraftHistory, String> {
}
