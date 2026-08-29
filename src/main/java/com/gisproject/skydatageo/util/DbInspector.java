package com.gisproject.skydatageo.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class DbInspector implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DbInspector(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("====== [DbInspector] Checking Database Tables ======");
        
        // 현재 연결된 DB 이름 확인
        String dbName = jdbcTemplate.queryForObject("SELECT current_database()", String.class);
        System.out.println("Connected Database: " + dbName);
        
        // 현재 스키마 확인
        String schema = jdbcTemplate.queryForObject("SELECT current_schema()", String.class);
        System.out.println("Current Schema: " + schema);

        // 테이블 목록 조회
        List<Map<String, Object>> tables = jdbcTemplate.queryForList(
                "SELECT table_schema, table_name FROM information_schema.tables " +
                "WHERE table_schema NOT IN ('information_schema', 'pg_catalog') " +
                "AND table_type = 'BASE TABLE'"
        );

        if (tables.isEmpty()) {
            System.out.println("No tables found in user schemas!");
        } else {
            System.out.println("Found tables:");
            for (Map<String, Object> row : tables) {
                System.out.println(" - " + row.get("table_schema") + "." + row.get("table_name"));
            }
        }
        System.out.println("====================================================");
    }
}
