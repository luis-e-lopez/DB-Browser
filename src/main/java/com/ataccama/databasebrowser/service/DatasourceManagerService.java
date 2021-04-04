package com.ataccama.databasebrowser.service;

import org.springframework.jdbc.core.JdbcTemplate;

public interface DatasourceManagerService {
    JdbcTemplate getJdbcTemplate(long connectionId);
}
