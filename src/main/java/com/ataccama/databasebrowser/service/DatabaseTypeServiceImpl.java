package com.ataccama.databasebrowser.service;

import com.ataccama.databasebrowser.exception.DatabaseTypeNotFoundException;
import com.ataccama.databasebrowser.model.DatabaseType;
import com.ataccama.databasebrowser.repository.DatabaseTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseTypeServiceImpl implements DatabaseTypeService {

    @Autowired
    private DatabaseTypeRepository databaseTypeRepository;

    @Override
    public List<DatabaseType> findAll() {
        return databaseTypeRepository.findAll();
    }

    @Override
    public DatabaseType findById(Long id) {
        return databaseTypeRepository.findById(id)
                .orElseThrow(() -> new DatabaseTypeNotFoundException("Database type with ID " + id + " not found"));
    }
}
