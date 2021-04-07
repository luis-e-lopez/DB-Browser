package com.ataccama.databasebrowser.controller;

import com.ataccama.databasebrowser.exception.CannotConnectToDBException;
import com.ataccama.databasebrowser.exception.ConnectionNotFoundException;
import com.ataccama.databasebrowser.exception.DatabaseNotFoundException;
import com.ataccama.databasebrowser.exception.TableNotFoundException;
import com.ataccama.databasebrowser.model.Column;
import com.ataccama.databasebrowser.model.Schema;
import com.ataccama.databasebrowser.model.Table;
import com.ataccama.databasebrowser.service.DBInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebMvcTest(controllers = DBInfoController.class)
public class TestDBInfoController {

    @MockBean
    private DBInfoService dbInfoService;

    @Autowired
    private MockMvc mockMvc;

    // Endpoint: /schemas

    @Test
    public void testSuccessfulFindAllSchemas() throws Exception {
        when(dbInfoService.getSchemas(5l)).thenReturn(Arrays.asList(new Schema("schema1"), new Schema("schema2")));
        mockMvc.perform(get("/schemas?connectionId=5")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.schemas", hasSize(2)));
    }

    @Test
    public void testMissingParam_FindAllSchemasByConnectionId() throws Exception {
        mockMvc.perform(get("/schemas"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
        verify(dbInfoService, times(0)).getSchemas(any());
    }

    @Test
    public void testNonExistingConnectionIdParam_FindAllSchemas() throws Exception {
        when(dbInfoService.getSchemas(5l))
                .thenThrow(new ConnectionNotFoundException("Connection with ID 5 not found"));
        mockMvc.perform(get("/schemas?connectionId=5"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Connection with ID 5 not found"))
                .andExpect(content().string(""));
    }

    @Test
    public void testNoConnectionToDBServer_FindAllSchemas() throws Exception {
        when(dbInfoService.getSchemas(5l))
                .thenThrow(new CannotConnectToDBException("Can't connect to database", null));
        mockMvc.perform(get("/schemas?connectionId=5"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Can't connect to database"))
                .andExpect(content().string(""));
    }

    // Endpoint: /tables

    @Test
    public void testSuccessfulFindAllTablesFromSchema() throws Exception {
        when(dbInfoService.getSchemaTables(5l, "test")).thenReturn(Arrays.asList(new Table("table1"), new Table("table2")));
        mockMvc.perform(get("/tables?connectionId=5&schema=test")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.tables", hasSize(2)));
    }

    @Test
    public void testMissingParamConnectionId_FindAllTablesFromSchema() throws Exception {
        mockMvc.perform(get("/tables?schema=test"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
        verify(dbInfoService, times(0)).getSchemaTables(any(), anyString());
    }

    @Test
    public void testMissingParamSchema_FindAllTablesFromSchema() throws Exception {
        mockMvc.perform(get("/tables?connectionId=5"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
        verify(dbInfoService, times(0)).getSchemaTables(any(), anyString());
    }

    @Test
    public void testNonExistingConnectionIdParam_FindAllTablesFromSchema() throws Exception {
        when(dbInfoService.getSchemaTables(5l, "test"))
                .thenThrow(new ConnectionNotFoundException("Connection with ID 5 not found"));
        mockMvc.perform(get("/tables?connectionId=5&schema=test"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Connection with ID 5 not found"))
                .andExpect(content().string(""));
    }

    @Test
    public void testNonExistingSchemaParam_FindAllTablesFromSchema() throws Exception {
        when(dbInfoService.getSchemaTables(5l, "test"))
                .thenThrow(new DatabaseNotFoundException("Schema test not found"));
        mockMvc.perform(get("/tables?connectionId=5&schema=test"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Schema test not found"))
                .andExpect(content().string(""));
    }

    @Test
    public void testNoConnectionToDBServer_FindAllTablesFromSchema() throws Exception {
        when(dbInfoService.getSchemaTables(5l, "test"))
                .thenThrow(new CannotConnectToDBException("Can't connect to database", null));
        mockMvc.perform(get("/tables?connectionId=5&schema=test"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Can't connect to database"))
                .andExpect(content().string(""));
    }

    // Endpoint: /columns

    @Test
    public void testSuccessfulFindAllColumnsFromTable() throws Exception {
        when(dbInfoService.getTableColumns(5l, "test", "table1"))
                .thenReturn(Arrays.asList(
                        new Column("column1","int","PRI"),
                        new Column("column2","varchar","")));
        mockMvc.perform(get("/columns?connectionId=5&schema=test&table=table1")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.columns", hasSize(2)));
    }

    @Test
    public void testMissingParamConnectionId_FindAllColumnsFromTable() throws Exception {
        mockMvc.perform(get("/columns?&schema=test&table=table1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
        verify(dbInfoService, times(0)).getTableColumns(any(), anyString(), anyString());
    }

    @Test
    public void testMissingParamSchema_FindAllColumnsFromTable() throws Exception {
        mockMvc.perform(get("/columns?connectionId=5&table=table1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
        verify(dbInfoService, times(0)).getTableColumns(any(), anyString(), anyString());
    }

    @Test
    public void testMissingParamTable_FindAllColumnsFromTable() throws Exception {
        mockMvc.perform(get("/columns?connectionId=5&schema=test"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
        verify(dbInfoService, times(0)).getTableColumns(any(), anyString(), anyString());
    }

    @Test
    public void testNonExistingConnectionIdParam_FindAllColumnsFromTable() throws Exception {
        when(dbInfoService.getTableColumns(5l, "test", "table1"))
                .thenThrow(new ConnectionNotFoundException("Connection with ID 5 not found"));
        mockMvc.perform(get("/columns?connectionId=5&schema=test&table=table1"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Connection with ID 5 not found"))
                .andExpect(content().string(""));
    }

    @Test
    public void testNonExistingSchemaParam_FindAllColumnsFromTable() throws Exception {
        when(dbInfoService.getTableColumns(5l, "test", "table1"))
                .thenThrow(new DatabaseNotFoundException("Schema test not found"));
        mockMvc.perform(get("/columns?connectionId=5&schema=test&table=table1"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Schema test not found"))
                .andExpect(content().string(""));
    }

    @Test
    public void testNonExistingTableParam_FindAllColumnsFromTable() throws Exception {
        when(dbInfoService.getTableColumns(5l, "test", "table1"))
                .thenThrow(new TableNotFoundException("Table table1 not found in schema test"));
        mockMvc.perform(get("/columns?connectionId=5&schema=test&table=table1"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Table table1 not found in schema test"))
                .andExpect(content().string(""));
    }

    @Test
    public void testNoConnectionToDBServer_FindAllColumnsFromTable() throws Exception {
        when(dbInfoService.getTableColumns(5l, "test", "table1"))
                .thenThrow(new CannotConnectToDBException("Can't connect to database", null));
        mockMvc.perform(get("/columns?connectionId=5&schema=test&table=table1"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Can't connect to database"))
                .andExpect(content().string(""));
    }

    // Endpoint: /tablePreview

    @Test
    public void testSuccessfulFindPreviewRowsFromTable() throws Exception {
        when(dbInfoService.getTablePreview(5l, "test", "table1"))
                .thenReturn(Arrays.asList(
                        Map.of("column1", "some value here", "column2", "hello"),
                        Map.of("column1", "I am in second row", "column2", "second column and second row")));
        when(dbInfoService.getTableColumns(5l, "test", "table1"))
                .thenReturn(Arrays.asList(
                        new Column("column1","int","PRI"),
                        new Column("column2","varchar","")));
        mockMvc.perform(get("/tablePreview?connectionId=5&schema=test&table=table1")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.columns", hasSize(2)))
                .andExpect(jsonPath("$.rows", hasSize(2)));
    }

    @Test
    public void testMissingParamConnectionId_FindPreviewRowsFromTable() throws Exception {
        mockMvc.perform(get("/tablePreview?&schema=test&table=table1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
        verify(dbInfoService, times(0)).getTablePreview(any(), anyString(), anyString());
        verify(dbInfoService, times(0)).getTableColumns(any(), anyString(), anyString());
    }

    @Test
    public void testMissingParamSchema_FindPreviewRowsFromTable() throws Exception {
        mockMvc.perform(get("/tablePreview?connectionId=5&table=table1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
        verify(dbInfoService, times(0)).getTablePreview(any(), anyString(), anyString());
        verify(dbInfoService, times(0)).getTableColumns(any(), anyString(), anyString());
    }

    @Test
    public void testMissingParamTable_FindPreviewRowsFromTable() throws Exception {
        mockMvc.perform(get("/tablePreview?connectionId=5&schema=test"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
        verify(dbInfoService, times(0)).getTablePreview(any(), anyString(), anyString());
        verify(dbInfoService, times(0)).getTableColumns(any(), anyString(), anyString());
    }

    @Test
    public void testNonExistingConnectionIdParam_FindPreviewRowsFromTable() throws Exception {
        when(dbInfoService.getTablePreview(5l, "test", "table1"))
                .thenThrow(new ConnectionNotFoundException("Connection with ID 5 not found"));
        mockMvc.perform(get("/tablePreview?connectionId=5&schema=test&table=table1"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Connection with ID 5 not found"))
                .andExpect(content().string(""));
    }

    @Test
    public void testNonExistingSchemaParam_FindPreviewRowsFromTable() throws Exception {
        when(dbInfoService.getTablePreview(5l, "test", "table1"))
                .thenThrow(new DatabaseNotFoundException("Schema test not found"));
        mockMvc.perform(get("/tablePreview?connectionId=5&schema=test&table=table1"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Schema test not found"))
                .andExpect(content().string(""));
    }

    @Test
    public void testNonExistingTableParam_FindPreviewRowsFromTable() throws Exception {
        when(dbInfoService.getTablePreview(5l, "test", "table1"))
                .thenThrow(new TableNotFoundException("Table table1 not found in schema test"));
        mockMvc.perform(get("/tablePreview?connectionId=5&schema=test&table=table1"))
                .andExpect(status().is4xxClientError())
                .andExpect(status().reason("Table table1 not found in schema test"))
                .andExpect(content().string(""));
    }

    @Test
    public void testNoConnectionToDBServer_FindPreviewRowsFromTable() throws Exception {
        when(dbInfoService.getTablePreview(5l, "test", "table1"))
                .thenThrow(new CannotConnectToDBException("Can't connect to database", null));
        mockMvc.perform(get("/tablePreview?connectionId=5&schema=test&table=table1"))
                .andExpect(status().is5xxServerError())
                .andExpect(status().reason("Can't connect to database"))
                .andExpect(content().string(""));
    }
}
