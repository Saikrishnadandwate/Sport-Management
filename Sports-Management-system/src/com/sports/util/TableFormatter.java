package com.sports.util;

import com.sports.model.*;

import java.util.List;
import java.util.Map;

/**
 * Utility class to display console outputs in clean, formatted ASCII tables.
 */
public class TableFormatter {

    public static void printPlayersTable(List<Player> players, Map<String, Team> teamMap) {
        if (players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }

        String border = "+------------+----------------------+-----+-----------------+----------------------+";
        System.out.println(border);
        System.out.printf("| %-10s | %-20s | %-3s | %-15s | %-20s |%n",
                "PLAYER ID", "NAME", "AGE", "SPORT", "TEAM");
        System.out.println(border);

        for (Player p : players) {
            String teamDisplay = "Free Agent";
            if (!"UNASSIGNED".equalsIgnoreCase(p.getTeamId())) {
                Team t = teamMap.get(p.getTeamId().toUpperCase());
                teamDisplay = (t != null) ? t.getTeamName() + " (" + t.getTeamId() + ")" : p.getTeamId();
            }
            System.out.printf("| %-10s | %-20s | %-3d | %-15s | %-20s |%n",
                    truncate(p.getId(), 10),
                    truncate(p.getName(), 20),
                    p.getAge(),
                    truncate(p.getSport(), 15),
                    truncate(teamDisplay, 20));
        }
        System.out.println(border);
        System.out.println("Total Players: " + players.size());
    }

    public static void printTeamsTable(List<Team> teams) {
        if (teams.isEmpty()) {
            System.out.println("No teams found.");
            return;
        }

        String border = "+------------+----------------------+-----------------+----------------------+--------------+";
        System.out.println(border);
        System.out.printf("| %-10s | %-20s | %-15s | %-20s | %-12s |%n",
                "TEAM ID", "TEAM NAME", "SPORT", "HEAD COACH", "ROSTER SIZE");
        System.out.println(border);

        for (Team t : teams) {
            System.out.printf("| %-10s | %-20s | %-15s | %-20s | %-12d |%n",
                    truncate(t.getTeamId(), 10),
                    truncate(t.getTeamName(), 20),
                    truncate(t.getSport(), 15),
                    truncate(t.getCoachName(), 20),
                    t.getRosterSize());
        }
        System.out.println(border);
        System.out.println("Total Teams: " + teams.size());
    }

    public static void printTeamRoster(Team team, List<Player> rosterPlayers) {
        System.out.println("\n==================================================================");
        System.out.printf("  ROSTER: %s (ID: %s) | Sport: %s | Coach: %s%n",
                team.getTeamName(), team.getTeamId(), team.getSport(), team.getCoachName());
        System.out.println("==================================================================");

        if (rosterPlayers.isEmpty()) {
            System.out.println("  No players currently registered in this team.");
            return;
        }

        String border = "+------------+----------------------+-----+-----------------+";
        System.out.println(border);
        System.out.printf("| %-10s | %-20s | %-3s | %-15s |%n",
                "PLAYER ID", "NAME", "AGE", "SPORT");
        System.out.println(border);

        for (Player p : rosterPlayers) {
            System.out.printf("| %-10s | %-20s | %-3d | %-15s |%n",
                    truncate(p.getId(), 10),
                    truncate(p.getName(), 20),
                    p.getAge(),
                    truncate(p.getSport(), 15));
        }
        System.out.println(border);
        System.out.println("Total Squad Members: " + rosterPlayers.size());
    }

    public static void printTournamentsTable(List<Tournament> tournaments) {
        if (tournaments.isEmpty()) {
            System.out.println("No tournaments found.");
            return;
        }

        String border = "+---------------+---------------------------+-----------------+-----------------+---------------+";
        System.out.println(border);
        System.out.printf("| %-13s | %-25s | %-15s | %-15s | %-13s |%n",
                "TOURNAMENT ID", "NAME", "SPORT", "FORMAT", "TEAMS ENROLLED");
        System.out.println(border);

        for (Tournament t : tournaments) {
            System.out.printf("| %-13s | %-25s | %-15s | %-15s | %-13d |%n",
                    truncate(t.getTournamentId(), 13),
                    truncate(t.getName(), 25),
                    truncate(t.getSport(), 15),
                    truncate(t.getFormat(), 15),
                    t.getTeamCount());
        }
        System.out.println(border);
        System.out.println("Total Tournaments: " + tournaments.size());
    }

    public static void printMatchesTable(List<Match> matches, Map<String, Team> teamMap) {
        if (matches.isEmpty()) {
            System.out.println("No matches found.");
            return;
        }

        String border = "+------------+---------------+-----------------------------------------------+-------------+-----------------+-------------+----------------------+";
        System.out.println(border);
        System.out.printf("| %-10s | %-13s | %-45s | %-11s | %-15s | %-11s | %-20s |%n",
                "MATCH ID", "TOURNAMENT", "TEAMS CONTEST", "SCORE", "WINNER", "STATUS", "VENUE / DATE");
        System.out.println(border);

        for (Match m : matches) {
            String t1Name = teamMap.containsKey(m.getTeam1Id().toUpperCase()) ?
                    teamMap.get(m.getTeam1Id().toUpperCase()).getTeamName() : m.getTeam1Id();
            String t2Name = teamMap.containsKey(m.getTeam2Id().toUpperCase()) ?
                    teamMap.get(m.getTeam2Id().toUpperCase()).getTeamName() : m.getTeam2Id();
            String contest = t1Name + " vs " + t2Name;

            String scoreStr = m.isCompleted() ? (m.getTeam1Score() + " - " + m.getTeam2Score()) : "Pending";

            String winnerStr = "TBD";
            if (m.isCompleted()) {
                if (Match.RESULT_DRAW.equalsIgnoreCase(m.getWinnerId())) {
                    winnerStr = "TIE / DRAW";
                } else if (teamMap.containsKey(m.getWinnerId().toUpperCase())) {
                    winnerStr = teamMap.get(m.getWinnerId().toUpperCase()).getTeamName();
                } else {
                    winnerStr = m.getWinnerId();
                }
            }

            String venueDate = m.getVenue() + " (" + m.getMatchDate() + ")";

            System.out.printf("| %-10s | %-13s | %-45s | %-11s | %-15s | %-11s | %-20s |%n",
                    truncate(m.getMatchId(), 10),
                    truncate(m.getTournamentId(), 13),
                    truncate(contest, 45),
                    truncate(scoreStr, 11),
                    truncate(winnerStr, 15),
                    truncate(m.getStatus(), 11),
                    truncate(venueDate, 20));
        }
        System.out.println(border);
        System.out.println("Total Matches: " + matches.size());
    }

    public static void printPointsTable(Tournament tournament, List<PointsRecord> standings) {
        System.out.println("\n==========================================================================================");
        System.out.printf("  OFFICIAL STANDINGS / POINTS TABLE: %s (Sport: %s)%n", tournament.getName(), tournament.getSport());
        System.out.println("  Scoring Rule: Win = 3 Pts | Draw = 1 Pt | Loss = 0 Pts");
        System.out.println("==========================================================================================");

        if (standings.isEmpty()) {
            System.out.println("  No teams or matches recorded for this tournament yet.");
            return;
        }

        String border = "+------+------------+----------------------+--------+-----+------+------+--------+";
        System.out.println(border);
        System.out.printf("| %-4s | %-10s | %-20s | %-6s | %-3s | %-4s | %-4s | %-6s |%n",
                "POS", "TEAM ID", "TEAM NAME", "PLAYED", "WON", "DRAW", "LOST", "POINTS");
        System.out.println(border);

        int rank = 1;
        for (PointsRecord r : standings) {
            System.out.printf("| %-4d | %-10s | %-20s | %-6d | %-3d | %-4d | %-4d | %-6d |%n",
                    rank++,
                    truncate(r.getTeamId(), 10),
                    truncate(r.getTeamName(), 20),
                    r.getPlayed(),
                    r.getWon(),
                    r.getDrawn(),
                    r.getLost(),
                    r.getPoints());
        }
        System.out.println(border);
    }

    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 2) + "..";
    }
}
