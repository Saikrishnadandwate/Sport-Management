package com.sports.model;

/**
 * Data carrier class for tournament standings and points table calculation.
 * Implements Comparable to enable automatic ranking sorted by points.
 */
public class PointsRecord implements Comparable<PointsRecord> {
    private String teamId;
    private String teamName;
    private int played;
    private int won;
    private int drawn;
    private int lost;
    private int points; // Win = 3, Draw = 1, Loss = 0

    public PointsRecord(String teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.played = 0;
        this.won = 0;
        this.drawn = 0;
        this.lost = 0;
        this.points = 0;
    }

    public void addWin() {
        this.played++;
        this.won++;
        this.points += 3;
    }

    public void addDraw() {
        this.played++;
        this.drawn++;
        this.points += 1;
    }

    public void addLoss() {
        this.played++;
        this.lost++;
        // 0 points for a loss
    }

    public String getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getPlayed() {
        return played;
    }

    public int getWon() {
        return won;
    }

    public int getDrawn() {
        return drawn;
    }

    public int getLost() {
        return lost;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public int compareTo(PointsRecord other) {
        // Higher points first
        if (this.points != other.points) {
            return Integer.compare(other.points, this.points);
        }
        // Then more wins
        if (this.won != other.won) {
            return Integer.compare(other.won, this.won);
        }
        // Then alphabetical team name
        return this.teamName.compareToIgnoreCase(other.teamName);
    }
}
