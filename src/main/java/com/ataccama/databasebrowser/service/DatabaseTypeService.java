package com.ataccama.databasebrowser.service;

import com.ataccama.databasebrowser.model.DatabaseType;

import java.util.List;

public interface DatabaseTypeService {
    List<DatabaseType> findAll();
    DatabaseType findById(Long id);
}
