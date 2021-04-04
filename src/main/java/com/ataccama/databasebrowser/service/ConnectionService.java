package com.ataccama.databasebrowser.service;

import com.ataccama.databasebrowser.model.Connection;

import java.util.List;

public interface ConnectionService {

    Connection save(Connection connection);
    List<Connection> findAll();
    Connection findById(Long id);
    void delete(Connection connection);
}
