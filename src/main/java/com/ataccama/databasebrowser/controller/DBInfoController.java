package com.ataccama.databasebrowser.controller;

import com.ataccama.databasebrowser.exception.CannotConnectToDBException;
import com.ataccama.databasebrowser.exception.ConnectionNotFoundException;
import com.ataccama.databasebrowser.exception.DatabaseNotFoundException;
import com.ataccama.databasebrowser.exception.TableNotFoundException;
import com.ataccama.databasebrowser.service.DBInfoService;
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

    @GetMapping(value = "/schemas")
    public Map<String, List<String>> getSchemas(@RequestParam(value = "connectionId") Long connectionId) {
        try {
            List<String> schemas = dbInfoService.getSchemas(connectionId);
            return Map.of("schemas", schemas);
        } catch (ConnectionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (CannotConnectToDBException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @GetMapping(value = "/tables")
    public Map<String, List<String>> getTables(@RequestParam(value = "connectionId") Long connectionId,
                                  @RequestParam(value = "schema") String schema) {
        try {
            List<String> tables = dbInfoService.getSchemaTables(connectionId, schema);
            return Map.of("tables", tables);
        } catch (DatabaseNotFoundException | ConnectionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (CannotConnectToDBException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

    }

    @GetMapping(value = "/columns")
    public Map<String, List> getColumns(@RequestParam(value = "connectionId") Long connectionId,
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
}
