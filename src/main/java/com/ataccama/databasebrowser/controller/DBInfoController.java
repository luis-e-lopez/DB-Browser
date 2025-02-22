package com.ataccama.databasebrowser.controller;

import com.ataccama.databasebrowser.exception.CannotConnectToDBException;
import com.ataccama.databasebrowser.exception.ConnectionNotFoundException;
import com.ataccama.databasebrowser.exception.DatabaseNotFoundException;
import com.ataccama.databasebrowser.exception.TableNotFoundException;
import com.ataccama.databasebrowser.model.*;
import com.ataccama.databasebrowser.service.DBInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
public class DBInfoController {

    @Autowired
    private DBInfoService dbInfoService;

    @ApiOperation(value = "Get the list of schemas from the provided connection id")
    @GetMapping(value = "/schemas")
    public Map<String, List<Schema>> getSchemas(@RequestParam(value = "connectionId") Long connectionId) {
        try {
            List<Schema> schemas = dbInfoService.getSchemas(connectionId);
            return Map.of("schemas", schemas);
        } catch (ConnectionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (CannotConnectToDBException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @ApiOperation(value = "Get the list of tables from the schema")
    @GetMapping(value = "/tables")
    public Map<String, List<Table>> getTables(@RequestParam(value = "connectionId") Long connectionId,
                                  @RequestParam(value = "schema") String schema) {
        try {
            List<Table> tables = dbInfoService.getSchemaTables(connectionId, schema);
            return Map.of("tables", tables);
        } catch (DatabaseNotFoundException | ConnectionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (CannotConnectToDBException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

    }

    @ApiOperation(value = "Get the list of columns from the table")
    @GetMapping(value = "/columns")
    public Map<String, List<Column>> getColumns(@RequestParam(value = "connectionId") Long connectionId,
                                                @RequestParam(value = "schema") String schema,
                                                @RequestParam(value = "table") String table) {
        try {
            return Map.of("columns", dbInfoService.getTableColumns(connectionId, schema, table));
        } catch (DatabaseNotFoundException | TableNotFoundException | ConnectionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (CannotConnectToDBException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @ApiOperation(value = "Get a max of 10 rows as a preview of the table")
    @GetMapping(value = "/tablePreview")
    public Map<String, List> getTablePreview(@RequestParam(value = "connectionId") Long connectionId,
                                                     @RequestParam(value = "schema") String schema,
                                                     @RequestParam(value = "table") String table) {
        try {
            return Map.of("rows", dbInfoService.getTablePreview(connectionId, schema, table),
                "columns", dbInfoService.getTableColumns(connectionId, schema, table));
        } catch (DatabaseNotFoundException | TableNotFoundException | ConnectionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (CannotConnectToDBException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

    }

    // Statistics Endpoints

    @ApiOperation(value = "Get the table statistics")
    @GetMapping(value = "/tableStats")
    public Map<String, TableStats> getTableStatistics(@RequestParam(value = "connectionId") Long connectionId,
                                                      @RequestParam(value = "schema") String schema,
                                                      @RequestParam(value = "table") String table) {
        try {
            return Map.of("stats", dbInfoService.getTableStatistics(connectionId, schema, table));
        } catch (DatabaseNotFoundException | TableNotFoundException | ConnectionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (CannotConnectToDBException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

    }

    @ApiOperation(value = "Get the column statistics")
    @GetMapping(value = "/columnStats")
    public Map<String, ColumnStats> getColumnStatistics(@RequestParam(value = "connectionId") Long connectionId,
                                                        @RequestParam(value = "schema") String schema,
                                                        @RequestParam(value = "table") String table,
                                                        @RequestParam(value = "column") String column) {
        try {
            return Map.of("stats", dbInfoService.getColumnStatistics(connectionId, schema, table, column));
        } catch (DatabaseNotFoundException | TableNotFoundException | ConnectionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (CannotConnectToDBException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

    }
}
