package com.ataccama.databasebrowser.repository;

import com.ataccama.databasebrowser.model.Column;
import com.ataccama.databasebrowser.model.Schema;
import com.ataccama.databasebrowser.model.Table;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public interface DBInfoRepository {
    List<Schema> getSchemas(JdbcTemplate jdbcTemplate);
    List<Table> getSchemaTables(JdbcTemplate jdbcTemplate, String schemaName);
    List<Column> getTableColumns(JdbcTemplate jdbcTemplate, String schemaName, String tableName);
    List<Map<String, Object>> getTablePreview(JdbcTemplate jdbcTemplate, String schemaName, String tableName);
}
