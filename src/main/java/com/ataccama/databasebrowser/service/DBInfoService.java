package com.ataccama.databasebrowser.service;


import java.util.List;
import java.util.Map;

public interface DBInfoService {
    List<String> getSchemas(Long connectionId);
    List<String> getSchemaTables(Long connectionId, String schemaName);
    List<Map<String, Object>> getTableColumns(Long connectionId, String schemaName, String tableName);
    List<Map<String, Object>> getTablePreview(Long connectionId, String schemaName, String tableName);
}
