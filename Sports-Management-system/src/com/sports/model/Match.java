package com.sports.model;

import com.sports.exception.InvalidInputException;

/**
 * Match class representing a contest between two teams within a tournament.
 * Encapsulates scoring, status tracking, and automatic winner determination.
 */
public class Match {
    public static final String STATUS_SCHEDULED = "SCHEDULED";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String RESULT_DRAW = "DRAW";
    public static final String RESULT_PENDING = "PENDING";

    private String matchId;
    private String tournamentId;
    private String team1Id;
    private String team2Id;
    private int team1Score;
    private int team2Score;
    private String winnerId; // TeamId, "DRAW", or "PENDING"
    private String status;   // "SCHEDULED" or "COMPLETED"
    private String matchDate;
    private String venue;

    public Match() {
        this.status = STATUS_SCHEDULED;
        this.winnerId = RESULT_PENDING;
        this.team1Score = -1;
        this.team2Score = -1;
    }

    public Match(String matchId, String tournamentId, String team1Id, String team2Id, String matchDate, String venue) {
        if (team1Id.equalsIgnoreCase(team2Id)) {
            throw new IllegalArgumentException("A team cannot play against itself.");
        }
        this.matchId = matchId.trim();
        this.tournamentId = tournamentId.trim();
        this.team1Id = team1Id.trim();
        this.team2Id = team2Id.trim();
        this.matchDate = (matchDate != null && !matchDate.trim().isEmpty()) ? matchDate.trim() : "TBD";
        this.venue = (venue != null && !venue.trim().isEmpty()) ? venue.trim() : "Main Stadium";
        this.status = STATUS_SCHEDULED;
        this.winnerId = RESULT_PENDING;
        this.team1Score = -1;
        this.team2Score = -1;
    }

    /**
     * Record match scores and determine the winner automatically.
     */
    public void recordResult(int score1, int score2) throws InvalidInputException {
        if (score1 < 0 || score2 < 0) {
            throw new InvalidInputException("Scores cannot be negative. Provided: " + score1 + ", " + score2);
        }
        this.team1Score = score1;
        this.team2Score = score2;
        this.status = STATUS_COMPLETED;

        if (score1 > score2) {
            this.winnerId = team1Id;
        } else if (score2 > score1) {
            this.winnerId = team2Id;
        } else {
            this.winnerId = RESULT_DRAW;
        }
    }

    public boolean isCompleted() {
        return STATUS_COMPLETED.equalsIgnoreCase(this.status);
    }

    // Getters and Setters
    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(String team1Id) {
        this.team1Id = team1Id;
    }

    public String getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(String team2Id) {
        this.team2Id = team2Id;
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public void setTeam1Score(int team1Score) {
        this.team1Score = team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    public void setTeam2Score(int team2Score) {
        this.team2Score = team2Score;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    // Format for file persistence: MatchID|TournamentID|Team1ID|Team2ID|Team1Score|Team2Score|WinnerID|Status|Date|Venue
    public String toDataString() {
        return matchId + "|" + tournamentId + "|" + team1Id + "|" + team2Id + "|" +
                team1Score + "|" + team2Score + "|" + winnerId + "|" + status + "|" +
                matchDate + "|" + venue;
    }

    public static Match fromDataString(String dataLine) {
        String[] parts = dataLine.split("\\|", -1);
        if (parts.length >= 10) {
            String mId = parts[0].trim();
            String tId = parts[1].trim();
            String t1 = parts[2].trim();
            String t2 = parts[3].trim();
            int s1 = Integer.parseInt(parts[4].trim());
            int s2 = Integer.parseInt(parts[5].trim());
            String winner = parts[6].trim();
            String status = parts[7].trim();
            String date = parts[8].trim();
            String venue = parts[9].trim();

            Match match = new Match(mId, tId, t1, t2, date, venue);
            match.setTeam1Score(s1);
            match.setTeam2Score(s2);
            match.setWinnerId(winner);
            match.setStatus(status);
            return match;
        }
        throw new IllegalArgumentException("Invalid match data line: " + dataLine);
    }

    @Override
    public String toString() {
        String scoreDisplay = isCompleted() ? String.format("%d - %d", team1Score, team2Score) : "VS";
        return String.format("Match[ID=%s, Tournament=%s, %s %s %s, Status=%s, Winner=%s]",
                matchId, tournamentId, team1Id, scoreDisplay, team2Id, status, winnerId);
    }
}
