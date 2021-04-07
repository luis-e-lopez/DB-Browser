package com.ataccama.databasebrowser.repository;

import com.ataccama.databasebrowser.model.DatabaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseTypeRepository extends JpaRepository<DatabaseType, Long> {
}
