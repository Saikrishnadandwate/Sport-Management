package com.sports.model;

/**
 * Player class representing an athlete in the sports system.
 * Demonstrates Inheritance (extends Person) and Polymorphism.
 */
public class Player extends Person {
    private String sport;
    private String teamId; // "UNASSIGNED" if player is a free agent

    public Player() {
        super();
        this.teamId = "UNASSIGNED";
    }

    public Player(String id, String name, int age, String sport, String teamId) {
        super(id, name, age);
        this.sport = sport != null ? sport.trim() : "General";
        this.teamId = (teamId != null && !teamId.trim().isEmpty()) ? teamId.trim() : "UNASSIGNED";
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        if (sport == null || sport.trim().isEmpty()) {
            throw new IllegalArgumentException("Sport cannot be empty");
        }
        this.sport = sport.trim();
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = (teamId != null && !teamId.trim().isEmpty()) ? teamId.trim() : "UNASSIGNED";
    }

    // Implementing abstract method from Person
    @Override
    public String getRole() {
        return "Athlete / Player";
    }

    // Polymorphic display of player details
    @Override
    public String displayDetails() {
        return String.format("Player[ID=%s, Name=%s, Age=%d, Sport=%s, Team=%s]",
                id, name, age, sport, teamId);
    }

    // Format for file persistence: ID|Name|Age|Sport|TeamID
    @Override
    public String toDataString() {
        return id + "|" + name + "|" + age + "|" + sport + "|" + teamId;
    }

    // Static factory method to parse from file line
    public static Player fromDataString(String dataLine) {
        String[] parts = dataLine.split("\\|", -1);
        if (parts.length >= 5) {
            String id = parts[0].trim();
            String name = parts[1].trim();
            int age = Integer.parseInt(parts[2].trim());
            String sport = parts[3].trim();
            String teamId = parts[4].trim();
            return new Player(id, name, age, sport, teamId);
        }
        throw new IllegalArgumentException("Invalid player data line: " + dataLine);
    }

    @Override
    public String toString() {
        return displayDetails();
    }
}
