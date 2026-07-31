package com.wandersync.backend.exception;

public class SeatConflictException extends RuntimeException {
    public SeatConflictException(String message) {
        super(message);
    }
}
