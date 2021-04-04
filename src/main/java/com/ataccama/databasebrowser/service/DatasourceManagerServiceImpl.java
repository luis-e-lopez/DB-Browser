package com.ataccama.databasebrowser.service;

import com.ataccama.databasebrowser.model.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.net.PortUnreachableException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DatasourceManagerServiceImpl implements DatasourceManagerService {

    private Map<Long, JdbcTemplate> jdbcConnections;

    public static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    public static final String MYSQL_SERVER_URL = "jdbc:mysql://%s:%s/%s";

    @Autowired
    private ConnectionService connectionService;

    @Override
    public JdbcTemplate getJdbcTemplate(long connectionId) {
        if (jdbcConnections == null) {
            jdbcConnections = new HashMap<>();
        }

        if (jdbcConnections.containsKey(connectionId)) {
            return jdbcConnections.get(connectionId);
        }

        Connection connection = connectionService.findById(connectionId);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(connection));
        jdbcConnections.put(connectionId, jdbcTemplate);
        return jdbcTemplate;
    }

    private DataSource getDataSource(Connection connection) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(MYSQL_DRIVER_CLASS_NAME);
        dataSource.setUrl(String.format(MYSQL_SERVER_URL,
                connection.getHostname(),
                connection.getPort(),
                connection.getDatabaseName()));
        dataSource.setUsername(connection.getUsername());
        dataSource.setPassword(connection.getPassword());
        return dataSource;
    }
}
