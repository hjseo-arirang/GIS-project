package com.gisproject.skydatageo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 스케줄러 활성화 (실시간 작업을 위해 필수)
public class GisDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(GisDataApplication.class, args);
	}

}
