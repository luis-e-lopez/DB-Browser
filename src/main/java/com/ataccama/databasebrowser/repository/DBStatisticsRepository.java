package com.ataccama.databasebrowser.repository;

import com.ataccama.databasebrowser.model.ColumnStats;
import com.ataccama.databasebrowser.model.TableStats;
import org.springframework.jdbc.core.JdbcTemplate;


public interface DBStatisticsRepository {
    TableStats getTableStatistics(JdbcTemplate jdbcTemplate, String schemaName, String tableName);
    ColumnStats getColumnStatistics(JdbcTemplate jdbcTemplate, String schemaName, String tableName, String columnName);
}
