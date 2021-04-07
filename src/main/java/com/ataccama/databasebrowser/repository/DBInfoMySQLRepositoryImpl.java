package com.ataccama.databasebrowser.repository;

import com.ataccama.databasebrowser.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DBInfoMySQLRepositoryImpl implements DBInfoRepository, DBStatisticsRepository {

    public static final String ALL_SCHEMAS_QUERY = "SELECT SCHEMA_NAME FROM information_schema.schemata WHERE SCHEMA_NAME NOT IN ('information_schema', 'performance_schema', 'mysql')";
    public static final String ALL_SCHEMA_TABLES_QUERY = "SELECT TABLE_NAME FROM information_schema.tables WHERE table_type = 'base table' AND table_schema= '%s'";
    public static final String ALL_TABLE_COLUMNS_QUERY = "SELECT COLUMN_NAME, COLUMN_TYPE, COLUMN_KEY FROM information_schema.columns WHERE table_name = '%s' AND table_schema= '%s'";
    public static final String TABLE_PREVIEW_QUERY = "SELECT * FROM %s.%s LIMIT 10";
    public static final String TABLE_NUMBER_OF_COLUMNS = "SELECT COUNT(*) FROM information_schema.columns WHERE table_name = '%s' AND table_schema= '%s'";
    public static final String TABLE_NUMBER_OF_RECORDS = "SELECT COUNT(*) FROM %s.%s";
    public static final String COLUMN_MAX_MIN_AVG = "SELECT MAX(%s) AS MaxVal, AVG(%s) AS AvgVal, MIN(%s) AS MinVal FROM %s.%s";

    @Override
    public List<Schema> getSchemas(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query(ALL_SCHEMAS_QUERY, (rs, rowNum) -> new Schema(rs.getString("SCHEMA_NAME")));
    }

    @Override
    public List<Table> getSchemaTables(JdbcTemplate jdbcTemplate, String schemaName) {
        return jdbcTemplate.query(String.format(ALL_SCHEMA_TABLES_QUERY, schemaName),
                (rs, rowNum) -> new Table(rs.getString("TABLE_NAME")));
    }

    @Override
    public List<Column> getTableColumns(JdbcTemplate jdbcTemplate, String schemaName, String tableName) {
        return jdbcTemplate.query(String.format(ALL_TABLE_COLUMNS_QUERY, tableName, schemaName),
                (rs, rowNum) -> new Column(
                        rs.getString("COLUMN_NAME"),
                        rs.getString("COLUMN_TYPE"),
                        rs.getString("COLUMN_KEY")
                ));
    }

    @Override
    public List<Map<String, Object>> getTablePreview(JdbcTemplate jdbcTemplate, String schemaName, String tableName) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(String.format(TABLE_PREVIEW_QUERY, schemaName, tableName));
        return rows;
    }


    @Override
    public TableStats getTableStatistics(JdbcTemplate jdbcTemplate, String schemaName, String tableName) {
        long columnsCount = jdbcTemplate.queryForObject(String.format(TABLE_NUMBER_OF_COLUMNS, tableName, schemaName), Long.class);
        long recordsCount = jdbcTemplate.queryForObject(String.format(TABLE_NUMBER_OF_RECORDS, schemaName, tableName), Long.class);
        return new TableStats(columnsCount, recordsCount);
    }

    @Override
    public ColumnStats getColumnStatistics(JdbcTemplate jdbcTemplate, String schemaName, String tableName, String columnName) {
        return jdbcTemplate.queryForObject(String.format(COLUMN_MAX_MIN_AVG, columnName, columnName, columnName, schemaName, tableName),
                (rs, rowNum) -> new ColumnStats(
                        rs.getString("MaxVal"),
                        rs.getString("MinVal"),
                        rs.getString("AvgVal"),
                        ""
                ));
    }
}
