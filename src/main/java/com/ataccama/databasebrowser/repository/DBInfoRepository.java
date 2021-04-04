package com.ataccama.databasebrowser.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public interface DBInfoRepository {
    List<String> getSchemas(JdbcTemplate jdbcTemplate);
    List<String> getSchemaTables(JdbcTemplate jdbcTemplate, String schemaName);
    List<Map<String, Object>> getTableColumns(JdbcTemplate jdbcTemplate, String schemaName, String tableName);
    List<Map<String, Object>> getTablePreview(JdbcTemplate jdbcTemplate, String schemaName, String tableName);
}
