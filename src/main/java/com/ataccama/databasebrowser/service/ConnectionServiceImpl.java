package com.ataccama.databasebrowser.service;

import com.ataccama.databasebrowser.exception.ConnectionNotFoundException;
import com.ataccama.databasebrowser.model.Connection;
import com.ataccama.databasebrowser.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Override
    @Transactional
    public Connection save(Connection connection) {
        return connectionRepository.save(connection);
    }

    @Override
    public List<Connection> findAll() {
        return connectionRepository.findAll();
    }

    @Override
    public Connection findById(Long id) {
        return connectionRepository.findById(id)
                .orElseThrow(() -> new ConnectionNotFoundException("Connection with ID " + id + " not found"));
    }

    @Override
    @Transactional
    public void delete(Connection connection) {
        connectionRepository.delete(connection);
    }
}
