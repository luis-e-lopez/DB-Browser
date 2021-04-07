package com.ataccama.databasebrowser.service;

import com.ataccama.databasebrowser.model.*;

import java.util.List;
import java.util.Map;

public interface DBInfoService {
    List<Schema> getSchemas(Long connectionId);
    List<Table> getSchemaTables(Long connectionId, String schemaName);
    List<Column> getTableColumns(Long connectionId, String schemaName, String tableName);
    List<Map<String, Object>> getTablePreview(Long connectionId, String schemaName, String tableName);
    TableStats getTableStatistics(Long connectionId, String schemaName, String tableName);
    ColumnStats getColumnStatistics(Long connectionId, String schemaName, String tableName, String columnName);
}
