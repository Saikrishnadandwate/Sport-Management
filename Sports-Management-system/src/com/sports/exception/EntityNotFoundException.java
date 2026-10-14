package com.sports.exception;

/**
 * Thrown when an entity (Player, Team, Tournament, Match) is not found.
 */
public class EntityNotFoundException extends SportsException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
