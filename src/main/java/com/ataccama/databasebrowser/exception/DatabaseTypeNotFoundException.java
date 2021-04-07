package com.ataccama.databasebrowser.exception;

public class DatabaseTypeNotFoundException extends RuntimeException {
    public DatabaseTypeNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    public DatabaseTypeNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
