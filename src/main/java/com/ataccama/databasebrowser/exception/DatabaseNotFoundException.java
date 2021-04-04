package com.ataccama.databasebrowser.exception;

public class DatabaseNotFoundException extends RuntimeException {
    public DatabaseNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    public DatabaseNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
