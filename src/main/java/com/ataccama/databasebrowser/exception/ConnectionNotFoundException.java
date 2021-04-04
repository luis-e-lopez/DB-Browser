package com.ataccama.databasebrowser.exception;

public class ConnectionNotFoundException extends RuntimeException {
    public ConnectionNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
    public ConnectionNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
