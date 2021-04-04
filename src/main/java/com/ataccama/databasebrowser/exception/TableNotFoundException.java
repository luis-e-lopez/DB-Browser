package com.ataccama.databasebrowser.exception;

public class TableNotFoundException extends RuntimeException {
    public TableNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    public TableNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
