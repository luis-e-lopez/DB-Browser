package com.ataccama.databasebrowser.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DBInfoMySQLRepositoryImpl implements DBInfoRepository {

    public static final String ALL_SCHEMAS_QUERY = "SELECT SCHEMA_NAME FROM information_schema.schemata WHERE SCHEMA_NAME NOT IN ('information_schema', 'performance_schema', 'mysql')";
    public static final String ALL_SCHEMA_TABLES_QUERY = "SELECT TABLE_NAME FROM information_schema.tables WHERE table_type = 'base table' AND table_schema= '%s'";
    public static final String ALL_TABLE_COLUMNS_QUERY = "SELECT COLUMN_NAME, COLUMN_TYPE, COLUMN_KEY FROM information_schema.columns WHERE table_name = '%s' AND table_schema= '%s'";
    public static final String TABLE_PREVIEW_QUERY = "SELECT * FROM %s.%s LIMIT 10";

    @Override
    public List<String> getSchemas(JdbcTemplate jdbcTemplate) {
        List<String> schemas = jdbcTemplate.queryForList(ALL_SCHEMAS_QUERY, String.class);
        return schemas;
    }

    @Override
    public List<String> getSchemaTables(JdbcTemplate jdbcTemplate, String schemaName) {
        List<String> tables = jdbcTemplate.queryForList(String.format(ALL_SCHEMA_TABLES_QUERY, schemaName), String.class);
        return tables;
    }

    @Override
    public List<Map<String, Object>> getTableColumns(JdbcTemplate jdbcTemplate, String schemaName, String tableName) {
        List<Map<String, Object>> columns = jdbcTemplate.queryForList(String.format(ALL_TABLE_COLUMNS_QUERY, tableName, schemaName));
        return columns;
    }

    @Override
    public List<Map<String, Object>> getTablePreview(JdbcTemplate jdbcTemplate, String schemaName, String tableName) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(String.format(TABLE_PREVIEW_QUERY, schemaName, tableName));
        return rows;
    }



}
