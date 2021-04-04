package com.ataccama.databasebrowser.exception;

public class CannotConnectToDBException extends RuntimeException {
    public CannotConnectToDBException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
