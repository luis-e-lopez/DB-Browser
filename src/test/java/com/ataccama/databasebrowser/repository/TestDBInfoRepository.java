package com.ataccama.databasebrowser.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@JdbcTest
public class TestDBInfoRepository {

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testSuccessfulReturnAllSchemas_usingMySQLServer() throws Exception {
        DBInfoRepository dbInfoRepository = new DBInfoMySQLRepositoryImpl();
        when(jdbcTemplate.queryForList(DBInfoMySQLRepositoryImpl.ALL_SCHEMAS_QUERY, String.class))
                .thenReturn(Arrays.asList("schema1", "schema2", "schema3"));

        assertEquals(Arrays.asList("schema1", "schema2", "schema3"), dbInfoRepository.getSchemas(jdbcTemplate));
    }

    @Test
    public void testSuccessfulReturnAllTablesFromSchema_usingMySQLServer() throws Exception {
        DBInfoRepository dbInfoRepository = new DBInfoMySQLRepositoryImpl();
        when(jdbcTemplate.queryForList(String.format(DBInfoMySQLRepositoryImpl.ALL_SCHEMA_TABLES_QUERY, "test"), String.class))
                .thenReturn(Arrays.asList("table1", "table2", "table3"));

        assertEquals(Arrays.asList("table1", "table2", "table3"), dbInfoRepository.getSchemaTables(jdbcTemplate, "test"));
    }

    @Test
    public void testSuccessfulReturnAllColumnsFromTable_usingMySQLServer() throws Exception {
        DBInfoRepository dbInfoRepository = new DBInfoMySQLRepositoryImpl();
        when(jdbcTemplate.queryForList(String.format(DBInfoMySQLRepositoryImpl.ALL_TABLE_COLUMNS_QUERY, "table1", "test")))
                .thenReturn(Arrays.asList(Map.of("COLUMN_NAME", "col1", "COLUMN_TYPE", "int", "COLUMN_KEY", "PRI"),
                        Map.of("COLUMN_NAME", "col2", "COLUMN_TYPE", "varchar", "COLUMN_KEY", "")));

        assertEquals(2, dbInfoRepository.getTableColumns(jdbcTemplate, "test", "table1").size());
    }

    @Test
    public void testSuccessfulReturnPreviewRowsFromTable_usingMySQLServer() throws Exception {
        DBInfoRepository dbInfoRepository = new DBInfoMySQLRepositoryImpl();
        when(jdbcTemplate.queryForList(String.format(DBInfoMySQLRepositoryImpl.TABLE_PREVIEW_QUERY, "test", "table1")))
                .thenReturn(Arrays.asList(Map.of("col1", "value1", "col2", "value2"),
                        Map.of("col1", "row 2 col 1", "col2", "row 2 col 2")));

        assertEquals(2, dbInfoRepository.getTablePreview(jdbcTemplate, "test", "table1").size());
    }
}
