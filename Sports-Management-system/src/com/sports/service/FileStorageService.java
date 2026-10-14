package com.sports.service;

import com.sports.model.Match;
import com.sports.model.Player;
import com.sports.model.Team;
import com.sports.model.Tournament;

import java.io.*;
import java.util.*;

/**
 * Service class handling Data Persistence using Core Java File I/O.
 * Uses BufferedReader, BufferedWriter, FileReader, and FileWriter.
 */
public class FileStorageService {
    private final String dataDirectory;
    private final String playersFile;
    private final String teamsFile;
    private final String tournamentsFile;
    private final String matchesFile;

    public FileStorageService() {
        this("data");
    }

    public FileStorageService(String dataDir) {
        this.dataDirectory = dataDir;
        this.playersFile = dataDir + File.separator + "players.txt";
        this.teamsFile = dataDir + File.separator + "teams.txt";
        this.tournamentsFile = dataDir + File.separator + "tournaments.txt";
        this.matchesFile = dataDir + File.separator + "matches.txt";

        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Saves all system entities to their respective text files.
     */
    public void saveAllData(Map<String, Player> players,
                            Map<String, Team> teams,
                            Map<String, Tournament> tournaments,
                            List<Match> matches) throws IOException {
        ensureDirectoryExists();
        savePlayers(players.values());
        saveTeams(teams.values());
        saveTournaments(tournaments.values());
        saveMatches(matches);
    }

    public void savePlayers(Collection<Player> players) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(playersFile))) {
            for (Player player : players) {
                writer.write(player.toDataString());
                writer.newLine();
            }
        }
    }

    public void saveTeams(Collection<Team> teams) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(teamsFile))) {
            for (Team team : teams) {
                writer.write(team.toDataString());
                writer.newLine();
            }
        }
    }

    public void saveTournaments(Collection<Tournament> tournaments) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tournamentsFile))) {
            for (Tournament tournament : tournaments) {
                writer.write(tournament.toDataString());
                writer.newLine();
            }
        }
    }

    public void saveMatches(List<Match> matches) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(matchesFile))) {
            for (Match match : matches) {
                writer.write(match.toDataString());
                writer.newLine();
            }
        }
    }

    /**
     * Loads players from the text file into a Map.
     */
    public Map<String, Player> loadPlayers() {
        Map<String, Player> players = new HashMap<>();
        File file = new File(playersFile);
        if (!file.exists()) {
            return players;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    Player p = Player.fromDataString(line);
                    players.put(p.getId().toUpperCase(), p);
                } catch (Exception e) {
                    System.err.println("[Warning] Skipping malformed player line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("[Error] Could not read players file: " + e.getMessage());
        }
        return players;
    }

    /**
     * Loads teams from the text file into a Map.
     */
    public Map<String, Team> loadTeams() {
        Map<String, Team> teams = new HashMap<>();
        File file = new File(teamsFile);
        if (!file.exists()) {
            return teams;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    Team t = Team.fromDataString(line);
                    teams.put(t.getTeamId().toUpperCase(), t);
                } catch (Exception e) {
                    System.err.println("[Warning] Skipping malformed team line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("[Error] Could not read teams file: " + e.getMessage());
        }
        return teams;
    }

    /**
     * Loads tournaments from the text file into a Map.
     */
    public Map<String, Tournament> loadTournaments() {
        Map<String, Tournament> tournaments = new HashMap<>();
        File file = new File(tournamentsFile);
        if (!file.exists()) {
            return tournaments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    Tournament t = Tournament.fromDataString(line);
                    tournaments.put(t.getTournamentId().toUpperCase(), t);
                } catch (Exception e) {
                    System.err.println("[Warning] Skipping malformed tournament line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("[Error] Could not read tournaments file: " + e.getMessage());
        }
        return tournaments;
    }

    /**
     * Loads matches from the text file into a List.
     */
    public List<Match> loadMatches() {
        List<Match> matches = new ArrayList<>();
        File file = new File(matchesFile);
        if (!file.exists()) {
            return matches;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    Match m = Match.fromDataString(line);
                    matches.add(m);
                } catch (Exception e) {
                    System.err.println("[Warning] Skipping malformed match line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("[Error] Could not read matches file: " + e.getMessage());
        }
        return matches;
    }

    public boolean hasExistingData() {
        return new File(playersFile).exists() ||
               new File(teamsFile).exists() ||
               new File(tournamentsFile).exists() ||
               new File(matchesFile).exists();
    }
}
