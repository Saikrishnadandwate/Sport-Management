package com.sports.exception;

/**
 * Thrown when an input violates business rules (e.g., negative score, invalid age, empty fields).
 */
public class InvalidInputException extends SportsException {
    public InvalidInputException(String message) {
        super(message);
    }
}
