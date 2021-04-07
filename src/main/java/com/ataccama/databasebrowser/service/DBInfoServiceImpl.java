package com.ataccama.databasebrowser.service;

import com.ataccama.databasebrowser.exception.CannotConnectToDBException;
import com.ataccama.databasebrowser.exception.DatabaseNotFoundException;
import com.ataccama.databasebrowser.exception.TableNotFoundException;
import com.ataccama.databasebrowser.model.*;
import com.ataccama.databasebrowser.repository.DBInfoRepository;
import com.ataccama.databasebrowser.repository.DBStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DBInfoServiceImpl implements DBInfoService {

    @Autowired
    private DBInfoRepository dbInfoRepository;

    @Autowired
    private DBStatisticsRepository dbStatisticsRepository;

    @Autowired
    private DatasourceManagerService dbManagerService;

    @Override
    public List<Schema> getSchemas(Long connectionId) {
        try {
            return dbInfoRepository.getSchemas(dbManagerService.getJdbcTemplate(connectionId));
        } catch (CannotGetJdbcConnectionException e) {
            throw new CannotConnectToDBException("Can't connect to database", e);
        }
    }

    @Override
    public List<Table> getSchemaTables(Long connectionId, String schemaName) {

        if (!schemaExists(connectionId, schemaName)) {
            throw new DatabaseNotFoundException("Schema " + schemaName + " not found");
        }

        try {
            return dbInfoRepository.getSchemaTables(dbManagerService.getJdbcTemplate(connectionId), schemaName);
        } catch (CannotGetJdbcConnectionException e) {
            throw new CannotConnectToDBException("Can't connect to database", e);
        }
    }

    @Override
    public List<Column> getTableColumns(Long connectionId, String schemaName, String tableName) {

        if (!tableExists(connectionId, schemaName, tableName)) {
            throw new TableNotFoundException("Table " + tableName + " not found in schema " + schemaName);
        }

        try {
            return dbInfoRepository.getTableColumns(dbManagerService.getJdbcTemplate(connectionId), schemaName, tableName);
        } catch (CannotGetJdbcConnectionException e) {
            throw new CannotConnectToDBException("Can't connect to database", e);
        }
    }

    @Override
    public List<Map<String, Object>> getTablePreview(Long connectionId, String schemaName, String tableName) {

        if (!tableExists(connectionId, schemaName, tableName)) {
            throw new TableNotFoundException("Table " + tableName + " not found in schema " + schemaName);
        }

        try {
            return dbInfoRepository.getTablePreview(dbManagerService.getJdbcTemplate(connectionId), schemaName, tableName);
        } catch (CannotGetJdbcConnectionException e) {
            throw new CannotConnectToDBException("Can't connect to database", e);
        }
    }

    @Override
    public TableStats getTableStatistics(Long connectionId, String schemaName, String tableName) {

        if (!tableExists(connectionId, schemaName, tableName)) {
            throw new TableNotFoundException("Table " + tableName + " not found in schema " + schemaName);
        }

        try {
            return dbStatisticsRepository.getTableStatistics(dbManagerService.getJdbcTemplate(connectionId), schemaName, tableName);
        } catch (CannotGetJdbcConnectionException e) {
            throw new CannotConnectToDBException("Can't connect to database", e);
        }
    }

    @Override
    public ColumnStats getColumnStatistics(Long connectionId, String schemaName, String tableName, String columnName) {

        if (!tableExists(connectionId, schemaName, tableName)) {
            throw new TableNotFoundException("Table " + tableName + " not found in schema " + schemaName);
        }

        try {
            return dbStatisticsRepository.getColumnStatistics(dbManagerService.getJdbcTemplate(connectionId), schemaName, tableName, columnName);
        } catch (CannotGetJdbcConnectionException e) {
            throw new CannotConnectToDBException("Can't connect to database", e);
        }
    }

    private boolean schemaExists(Long connectionId, String schemaName) {
        List<Schema> schemas = getSchemas(connectionId);
        for (Schema schema : schemas) {
            if (schema.getName().equals(schemaName)) {
                return true;
            }
        }
        return false;
    }

    private boolean tableExists(Long connectionId, String schemaName, String tableName) {
        List<Table> tables = getSchemaTables(connectionId, schemaName);
        for (Table table : tables) {
            if (table.getName().equals(tableName)) {
                return true;
            }
        }
        return false;
    }
}
