package com.sports.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Team class representing a sports squad/club.
 * Manages player roster and team information using Collections (ArrayList).
 */
public class Team {
    private String teamId;
    private String teamName;
    private String sport;
    private String coachName;
    // Encapsulated collection of player IDs belonging to this team
    private List<String> playerIds;

    public Team() {
        this.playerIds = new ArrayList<>();
    }

    public Team(String teamId, String teamName, String sport, String coachName) {
        this.teamId = teamId.trim();
        this.teamName = teamName.trim();
        this.sport = sport.trim();
        this.coachName = coachName.trim();
        this.playerIds = new ArrayList<>();
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        if (teamId == null || teamId.trim().isEmpty()) {
            throw new IllegalArgumentException("Team ID cannot be empty");
        }
        this.teamId = teamId.trim();
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        if (teamName == null || teamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Team Name cannot be empty");
        }
        this.teamName = teamName.trim();
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

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName != null ? coachName.trim() : "N/A";
    }

    // Unmodifiable view to maintain encapsulation
    public List<String> getPlayerIds() {
        return Collections.unmodifiableList(playerIds);
    }

    public int getRosterSize() {
        return playerIds.size();
    }

    public boolean addPlayer(String playerId) {
        if (playerId == null || playerId.trim().isEmpty()) {
            return false;
        }
        String cleanId = playerId.trim();
        if (!playerIds.contains(cleanId)) {
            playerIds.add(cleanId);
            return true;
        }
        return false;
    }

    public boolean removePlayer(String playerId) {
        if (playerId == null) {
            return false;
        }
        return playerIds.remove(playerId.trim());
    }

    public boolean hasPlayer(String playerId) {
        return playerId != null && playerIds.contains(playerId.trim());
    }

    // Format for file persistence: TeamID|TeamName|Sport|CoachName|p1,p2,p3
    public String toDataString() {
        String rosterStr = String.join(",", playerIds);
        return teamId + "|" + teamName + "|" + sport + "|" + coachName + "|" + rosterStr;
    }

    public static Team fromDataString(String dataLine) {
        String[] parts = dataLine.split("\\|", -1);
        if (parts.length >= 4) {
            String id = parts[0].trim();
            String name = parts[1].trim();
            String sport = parts[2].trim();
            String coach = parts[3].trim();
            Team team = new Team(id, name, sport, coach);
            if (parts.length >= 5 && !parts[4].trim().isEmpty()) {
                String[] pIds = parts[4].split(",");
                for (String pId : pIds) {
                    if (!pId.trim().isEmpty()) {
                        team.addPlayer(pId.trim());
                    }
                }
            }
            return team;
        }
        throw new IllegalArgumentException("Invalid team data line: " + dataLine);
    }

    @Override
    public String toString() {
        return String.format("Team[ID=%s, Name=%s, Sport=%s, Coach=%s, PlayersCount=%d]",
                teamId, teamName, sport, coachName, playerIds.size());
    }
}
