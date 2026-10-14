package com.sports.exception;

/**
 * Thrown when attempting to create an entity with an ID or unique attribute that already exists.
 */
public class DuplicateEntityException extends SportsException {
    public DuplicateEntityException(String message) {
        super(message);
    }
}
