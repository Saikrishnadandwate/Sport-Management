package com.sports.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tournament class representing a competitive sports event.
 * Tracks registered teams and tournament metadata.
 */
public class Tournament {
    private String tournamentId;
    private String name;
    private String sport;
    private String format; // e.g. "League", "Round Robin", "Knockout"
    private List<String> registeredTeamIds;

    public Tournament() {
        this.registeredTeamIds = new ArrayList<>();
    }

    public Tournament(String tournamentId, String name, String sport, String format) {
        this.tournamentId = tournamentId.trim();
        this.name = name.trim();
        this.sport = sport.trim();
        this.format = format != null && !format.trim().isEmpty() ? format.trim() : "Round Robin";
        this.registeredTeamIds = new ArrayList<>();
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        if (tournamentId == null || tournamentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Tournament ID cannot be empty");
        }
        this.tournamentId = tournamentId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tournament Name cannot be empty");
        }
        this.name = name.trim();
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format != null && !format.trim().isEmpty() ? format.trim() : "Round Robin";
    }

    public List<String> getRegisteredTeamIds() {
        return Collections.unmodifiableList(registeredTeamIds);
    }

    public int getTeamCount() {
        return registeredTeamIds.size();
    }

    public boolean registerTeam(String teamId) {
        if (teamId == null || teamId.trim().isEmpty()) {
            return false;
        }
        String cleanId = teamId.trim();
        if (!registeredTeamIds.contains(cleanId)) {
            registeredTeamIds.add(cleanId);
            return true;
        }
        return false;
    }

    public boolean removeTeam(String teamId) {
        if (teamId == null) {
            return false;
        }
        return registeredTeamIds.remove(teamId.trim());
    }

    public boolean isTeamRegistered(String teamId) {
        return teamId != null && registeredTeamIds.contains(teamId.trim());
    }

    // Format for file persistence: TournamentID|Name|Sport|Format|t1,t2,t3
    public String toDataString() {
        String teamsStr = String.join(",", registeredTeamIds);
        return tournamentId + "|" + name + "|" + sport + "|" + format + "|" + teamsStr;
    }

    public static Tournament fromDataString(String dataLine) {
        String[] parts = dataLine.split("\\|", -1);
        if (parts.length >= 4) {
            String id = parts[0].trim();
            String name = parts[1].trim();
            String sport = parts[2].trim();
            String format = parts[3].trim();
            Tournament tournament = new Tournament(id, name, sport, format);
            if (parts.length >= 5 && !parts[4].trim().isEmpty()) {
                String[] tIds = parts[4].split(",");
                for (String tId : tIds) {
                    if (!tId.trim().isEmpty()) {
                        tournament.registerTeam(tId.trim());
                    }
                }
            }
            return tournament;
        }
        throw new IllegalArgumentException("Invalid tournament data line: " + dataLine);
    }

    @Override
    public String toString() {
        return String.format("Tournament[ID=%s, Name=%s, Sport=%s, Format=%s, TeamsCount=%d]",
                tournamentId, name, sport, format, registeredTeamIds.size());
    }
}
