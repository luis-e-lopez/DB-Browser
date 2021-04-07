package com.ataccama.databasebrowser.service;

import com.ataccama.databasebrowser.model.Connection;
import com.ataccama.databasebrowser.model.DatabaseType;
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
        DatabaseType databaseType = connection.getDatabaseType();
        dataSource.setDriverClassName(databaseType.getDriverClassName());
        dataSource.setUrl(databaseType.getServerURL()
                .replace("[hostname]", connection.getHostname())
                .replace("[port]", String.valueOf(connection.getPort()))
                .replace("[dbname]", connection.getDatabaseName())
        );
        dataSource.setUsername(connection.getUsername());
        dataSource.setPassword(connection.getPassword());
        return dataSource;
    }
}
