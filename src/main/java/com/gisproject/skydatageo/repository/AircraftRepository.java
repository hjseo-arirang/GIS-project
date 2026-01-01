package com.gisproject.skydatageo.repository;

import com.gisproject.skydatageo.model.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// <Entity 클래스, PK의 타입>
@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, String> {
    // save(), saveAll(), findById(), delete()
}
