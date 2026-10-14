package com.sports.exception;

/**
 * Base custom exception for the Sports Management System.
 * Demonstrates Core Java Exception handling with inheritance.
 */
public class SportsException extends Exception {
    public SportsException(String message) {
        super(message);
    }

    public SportsException(String message, Throwable cause) {
        super(message, cause);
    }
}
