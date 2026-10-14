package com.sports.service;

import com.sports.exception.DuplicateEntityException;
import com.sports.exception.EntityNotFoundException;
import com.sports.exception.InvalidInputException;
import com.sports.model.*;

import java.io.IOException;
import java.util.*;

/**
 * Core Service orchestrating all Sports Management business logic.
 * Demonstrates extensive usage of Java Collections Framework (ArrayList, HashMap)
 * and Custom Exception Handling.
 */
public class SportsService {
    // Primary storage using Collections Framework
    private final Map<String, Player> players;         // Key: Player ID (Uppercase)
    private final Map<String, Team> teams;             // Key: Team ID (Uppercase)
    private final Map<String, Tournament> tournaments; // Key: Tournament ID (Uppercase)
    private final List<Match> matches;                 // List of scheduled and completed matches

    private final FileStorageService fileStorageService;

    public SportsService() {
        this(new FileStorageService());
    }

    public SportsService(FileStorageService storageService) {
        this.players = new HashMap<>();
        this.teams = new HashMap<>();
        this.tournaments = new HashMap<>();
        this.matches = new ArrayList<>();
        this.fileStorageService = storageService;
    }

    // =========================================================================
    // PLAYER MANAGEMENT
    // =========================================================================

    public void addPlayer(Player player) throws DuplicateEntityException, InvalidInputException {
        if (player == null || player.getId() == null) {
            throw new InvalidInputException("Player and Player ID cannot be null.");
        }
        String key = player.getId().toUpperCase();
        if (players.containsKey(key)) {
            throw new DuplicateEntityException("Player with ID '" + player.getId() + "' already exists.");
        }

        // If player is assigned to an existing team, sync with the team roster
        if (player.getTeamId() != null && !"UNASSIGNED".equalsIgnoreCase(player.getTeamId())) {
            Team team = teams.get(player.getTeamId().toUpperCase());
            if (team != null) {
                team.addPlayer(player.getId());
            }
        }

        players.put(key, player);
    }

    public Player getPlayer(String playerId) throws EntityNotFoundException {
        if (playerId == null) {
            throw new EntityNotFoundException("Player ID cannot be null.");
        }
        Player player = players.get(playerId.trim().toUpperCase());
        if (player == null) {
            throw new EntityNotFoundException("Player not found with ID: " + playerId);
        }
        return player;
    }

    public List<Player> getAllPlayers() {
        List<Player> list = new ArrayList<>(players.values());
        list.sort(Comparator.comparing(Player::getId));
        return list;
    }

    public void updatePlayer(String id, String newName, int newAge, String newSport, String newTeamId)
            throws EntityNotFoundException, InvalidInputException {
        Player player = getPlayer(id);

        if (newName != null && !newName.trim().isEmpty()) {
            player.setName(newName.trim());
        }
        if (newAge > 0) {
            player.setAge(newAge);
        }
        if (newSport != null && !newSport.trim().isEmpty()) {
            player.setSport(newSport.trim());
        }

        // Handle team reassignment
        if (newTeamId != null) {
            String oldTeamId = player.getTeamId();
            String cleanNewTeamId = newTeamId.trim();

            if (!oldTeamId.equalsIgnoreCase(cleanNewTeamId)) {
                // Remove from old team
                if (!"UNASSIGNED".equalsIgnoreCase(oldTeamId)) {
                    Team oldTeam = teams.get(oldTeamId.toUpperCase());
                    if (oldTeam != null) {
                        oldTeam.removePlayer(player.getId());
                    }
                }

                // Add to new team if specified
                if (!cleanNewTeamId.isEmpty() && !"UNASSIGNED".equalsIgnoreCase(cleanNewTeamId)) {
                    Team newTeam = teams.get(cleanNewTeamId.toUpperCase());
                    if (newTeam == null) {
                        throw new EntityNotFoundException("Cannot assign player to non-existent team ID: " + cleanNewTeamId);
                    }
                    newTeam.addPlayer(player.getId());
                    player.setTeamId(newTeam.getTeamId());
                } else {
                    player.setTeamId("UNASSIGNED");
                }
            }
        }
    }

    public void deletePlayer(String playerId) throws EntityNotFoundException {
        Player player = getPlayer(playerId);
        // Remove from team roster if assigned
        if (!"UNASSIGNED".equalsIgnoreCase(player.getTeamId())) {
            Team team = teams.get(player.getTeamId().toUpperCase());
            if (team != null) {
                team.removePlayer(player.getId());
            }
        }
        players.remove(player.getId().toUpperCase());
    }

    public List<Player> searchPlayers(String query) {
        List<Player> results = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return results;
        }
        String q = query.trim().toLowerCase();
        for (Player p : players.values()) {
            if (p.getId().toLowerCase().contains(q) ||
                p.getName().toLowerCase().contains(q) ||
                p.getSport().toLowerCase().contains(q)) {
                results.add(p);
            }
        }
        results.sort(Comparator.comparing(Player::getId));
        return results;
    }

    // =========================================================================
    // TEAM MANAGEMENT
    // =========================================================================

    public void createTeam(Team team) throws DuplicateEntityException, InvalidInputException {
        if (team == null || team.getTeamId() == null) {
            throw new InvalidInputException("Team and Team ID cannot be null.");
        }
        String key = team.getTeamId().toUpperCase();
        if (teams.containsKey(key)) {
            throw new DuplicateEntityException("Team with ID '" + team.getTeamId() + "' already exists.");
        }
        teams.put(key, team);
    }

    public Team getTeam(String teamId) throws EntityNotFoundException {
        if (teamId == null) {
            throw new EntityNotFoundException("Team ID cannot be null.");
        }
        Team team = teams.get(teamId.trim().toUpperCase());
        if (team == null) {
            throw new EntityNotFoundException("Team not found with ID: " + teamId);
        }
        return team;
    }

    public List<Team> getAllTeams() {
        List<Team> list = new ArrayList<>(teams.values());
        list.sort(Comparator.comparing(Team::getTeamId));
        return list;
    }

    public void addPlayerToTeam(String teamId, String playerId)
            throws EntityNotFoundException, InvalidInputException {
        Team team = getTeam(teamId);
        Player player = getPlayer(playerId);

        // Check if player is already in another team
        if (!"UNASSIGNED".equalsIgnoreCase(player.getTeamId()) &&
            !player.getTeamId().equalsIgnoreCase(team.getTeamId())) {
            // Remove from old team
            Team oldTeam = teams.get(player.getTeamId().toUpperCase());
            if (oldTeam != null) {
                oldTeam.removePlayer(player.getId());
            }
        }

        team.addPlayer(player.getId());
        player.setTeamId(team.getTeamId());
    }

    public void removePlayerFromTeam(String teamId, String playerId)
            throws EntityNotFoundException, InvalidInputException {
        Team team = getTeam(teamId);
        Player player = getPlayer(playerId);

        if (!team.hasPlayer(player.getId())) {
            throw new InvalidInputException("Player " + playerId + " is not in team " + teamId);
        }

        team.removePlayer(player.getId());
        player.setTeamId("UNASSIGNED");
    }

    public List<Player> getTeamRoster(String teamId) throws EntityNotFoundException {
        Team team = getTeam(teamId);
        List<Player> roster = new ArrayList<>();
        for (String pId : team.getPlayerIds()) {
            Player p = players.get(pId.toUpperCase());
            if (p != null) {
                roster.add(p);
            }
        }
        roster.sort(Comparator.comparing(Player::getName));
        return roster;
    }

    public void deleteTeam(String teamId) throws EntityNotFoundException {
        Team team = getTeam(teamId);

        // Unassign all players belonging to this team
        for (String pId : team.getPlayerIds()) {
            Player p = players.get(pId.toUpperCase());
            if (p != null) {
                p.setTeamId("UNASSIGNED");
            }
        }

        // Remove team from any registered tournaments
        for (Tournament tourney : tournaments.values()) {
            tourney.removeTeam(team.getTeamId());
        }

        teams.remove(team.getTeamId().toUpperCase());
    }

    // =========================================================================
    // TOURNAMENT MANAGEMENT
    // =========================================================================

    public void createTournament(Tournament tournament) throws DuplicateEntityException, InvalidInputException {
        if (tournament == null || tournament.getTournamentId() == null) {
            throw new InvalidInputException("Tournament and Tournament ID cannot be null.");
        }
        String key = tournament.getTournamentId().toUpperCase();
        if (tournaments.containsKey(key)) {
            throw new DuplicateEntityException("Tournament with ID '" + tournament.getTournamentId() + "' already exists.");
        }
        tournaments.put(key, tournament);
    }

    public Tournament getTournament(String tournamentId) throws EntityNotFoundException {
        if (tournamentId == null) {
            throw new EntityNotFoundException("Tournament ID cannot be null.");
        }
        Tournament tourney = tournaments.get(tournamentId.trim().toUpperCase());
        if (tourney == null) {
            throw new EntityNotFoundException("Tournament not found with ID: " + tournamentId);
        }
        return tourney;
    }

    public List<Tournament> getAllTournaments() {
        List<Tournament> list = new ArrayList<>(tournaments.values());
        list.sort(Comparator.comparing(Tournament::getTournamentId));
        return list;
    }

    public void registerTeamToTournament(String tournamentId, String teamId)
            throws EntityNotFoundException, InvalidInputException {
        Tournament tourney = getTournament(tournamentId);
        Team team = getTeam(teamId);

        if (tourney.isTeamRegistered(team.getTeamId())) {
            throw new InvalidInputException("Team '" + team.getTeamName() + "' is already registered in this tournament.");
        }

        tourney.registerTeam(team.getTeamId());
    }

    public List<Team> getTournamentTeams(String tournamentId) throws EntityNotFoundException {
        Tournament tourney = getTournament(tournamentId);
        List<Team> registered = new ArrayList<>();
        for (String tId : tourney.getRegisteredTeamIds()) {
            Team t = teams.get(tId.toUpperCase());
            if (t != null) {
                registered.add(t);
            }
        }
        return registered;
    }

    // =========================================================================
    // MATCH MANAGEMENT
    // =========================================================================

    public void scheduleMatch(Match match) throws DuplicateEntityException, EntityNotFoundException, InvalidInputException {
        if (match == null || match.getMatchId() == null) {
            throw new InvalidInputException("Match and Match ID cannot be null.");
        }

        // Check duplicate match ID
        for (Match m : matches) {
            if (m.getMatchId().equalsIgnoreCase(match.getMatchId())) {
                throw new DuplicateEntityException("Match with ID '" + match.getMatchId() + "' already exists.");
            }
        }

        // Verify tournament exists
        Tournament tourney = getTournament(match.getTournamentId());

        // Verify both teams exist
        Team team1 = getTeam(match.getTeam1Id());
        Team team2 = getTeam(match.getTeam2Id());

        if (team1.getTeamId().equalsIgnoreCase(team2.getTeamId())) {
            throw new InvalidInputException("A team cannot play against itself.");
        }

        // Auto-register teams to tournament if not registered yet
        if (!tourney.isTeamRegistered(team1.getTeamId())) {
            tourney.registerTeam(team1.getTeamId());
        }
        if (!tourney.isTeamRegistered(team2.getTeamId())) {
            tourney.registerTeam(team2.getTeamId());
        }

        matches.add(match);
    }

    public Match getMatch(String matchId) throws EntityNotFoundException {
        if (matchId == null) {
            throw new EntityNotFoundException("Match ID cannot be null.");
        }
        for (Match m : matches) {
            if (m.getMatchId().equalsIgnoreCase(matchId.trim())) {
                return m;
            }
        }
        throw new EntityNotFoundException("Match not found with ID: " + matchId);
    }

    public List<Match> getAllMatches() {
        return Collections.unmodifiableList(matches);
    }

    public List<Match> getMatchesByTournament(String tournamentId) {
        List<Match> tourneyMatches = new ArrayList<>();
        for (Match m : matches) {
            if (m.getTournamentId().equalsIgnoreCase(tournamentId.trim())) {
                tourneyMatches.add(m);
            }
        }
        return tourneyMatches;
    }

    public void recordMatchScore(String matchId, int score1, int score2)
            throws EntityNotFoundException, InvalidInputException {
        Match match = getMatch(matchId);
        match.recordResult(score1, score2);
    }

    // =========================================================================
    // POINTS TABLE & STANDINGS
    // =========================================================================

    /**
     * Calculates rankings for a tournament.
     * Award 3 points for a win, 1 point for a draw, and 0 for a loss.
     */
    public List<PointsRecord> calculateStandings(String tournamentId) throws EntityNotFoundException {
        Tournament tourney = getTournament(tournamentId);

        // Initialize points records for all registered teams
        Map<String, PointsRecord> tableMap = new HashMap<>();
        for (String tId : tourney.getRegisteredTeamIds()) {
            Team team = teams.get(tId.toUpperCase());
            String teamName = (team != null) ? team.getTeamName() : tId;
            tableMap.put(tId.toUpperCase(), new PointsRecord(tId, teamName));
        }

        // Process all completed matches for this tournament
        for (Match m : matches) {
            if (m.getTournamentId().equalsIgnoreCase(tournamentId) && m.isCompleted()) {
                String t1Key = m.getTeam1Id().toUpperCase();
                String t2Key = m.getTeam2Id().toUpperCase();

                PointsRecord rec1 = tableMap.get(t1Key);
                PointsRecord rec2 = tableMap.get(t2Key);

                if (rec1 == null) {
                    Team team1 = teams.get(t1Key);
                    rec1 = new PointsRecord(m.getTeam1Id(), team1 != null ? team1.getTeamName() : m.getTeam1Id());
                    tableMap.put(t1Key, rec1);
                }
                if (rec2 == null) {
                    Team team2 = teams.get(t2Key);
                    rec2 = new PointsRecord(m.getTeam2Id(), team2 != null ? team2.getTeamName() : m.getTeam2Id());
                    tableMap.put(t2Key, rec2);
                }

                if (m.getTeam1Score() > m.getTeam2Score()) {
                    rec1.addWin();
                    rec2.addLoss();
                } else if (m.getTeam2Score() > m.getTeam1Score()) {
                    rec2.addWin();
                    rec1.addLoss();
                } else {
                    rec1.addDraw();
                    rec2.addDraw();
                }
            }
        }

        // Sort entries by points (descending) using PointsRecord.compareTo
        List<PointsRecord> standings = new ArrayList<>(tableMap.values());
        Collections.sort(standings);
        return standings;
    }

    // =========================================================================
    // DATA PERSISTENCE & SAMPLE DATA
    // =========================================================================

    public void saveData() throws IOException {
        fileStorageService.saveAllData(players, teams, tournaments, matches);
    }

    public void loadData() {
        Map<String, Player> loadedPlayers = fileStorageService.loadPlayers();
        Map<String, Team> loadedTeams = fileStorageService.loadTeams();
        Map<String, Tournament> loadedTournaments = fileStorageService.loadTournaments();
        List<Match> loadedMatches = fileStorageService.loadMatches();

        players.clear();
        players.putAll(loadedPlayers);

        teams.clear();
        teams.putAll(loadedTeams);

        tournaments.clear();
        tournaments.putAll(loadedTournaments);

        matches.clear();
        matches.addAll(loadedMatches);
    }

    public boolean hasSavedData() {
        return fileStorageService.hasExistingData();
    }

    public Map<String, Team> getTeamsMap() {
        return Collections.unmodifiableMap(teams);
    }

    /**
     * Seeds initial rich demonstration data (teams, players, tournaments, and matches).
     */
    public void seedSampleData() {
        try {
            // 1. Teams
            Team t1 = new Team("T101", "Thunderbolts", "Cricket", "Coach Rahul");
            Team t2 = new Team("T102", "Golden Eagles", "Cricket", "Coach Vikram");
            Team t3 = new Team("T103", "Strikers FC", "Football", "Coach Carlos");
            Team t4 = new Team("T104", "Blaze United", "Football", "Coach Steve");

            createTeam(t1);
            createTeam(t2);
            createTeam(t3);
            createTeam(t4);

            // 2. Players
            addPlayer(new Player("P101", "Aarav Sharma", 22, "Cricket", "T101"));
            addPlayer(new Player("P102", "Rohan Verma", 24, "Cricket", "T101"));
            addPlayer(new Player("P103", "Ishan Kishan", 23, "Cricket", "T101"));
            addPlayer(new Player("P104", "Virat Rao", 25, "Cricket", "T102"));
            addPlayer(new Player("P105", "Hardik Patel", 26, "Cricket", "T102"));
            addPlayer(new Player("P106", "Surya Kumar", 24, "Cricket", "T102"));

            addPlayer(new Player("P107", "Marcus Silva", 21, "Football", "T103"));
            addPlayer(new Player("P108", "David Miller", 23, "Football", "T103"));
            addPlayer(new Player("P109", "Leo Gomez", 22, "Football", "T104"));
            addPlayer(new Player("P110", "Kevin De Bruyne", 24, "Football", "T104"));
            addPlayer(new Player("P111", "Lucas Perez", 20, "Football", "UNASSIGNED")); // Free Agent

            // 3. Tournaments
            Tournament tourney1 = new Tournament("TRN-01", "Inter-College Cricket Cup", "Cricket", "Round Robin");
            Tournament tourney2 = new Tournament("TRN-02", "State Football Championship", "Football", "League");

            createTournament(tourney1);
            createTournament(tourney2);

            registerTeamToTournament("TRN-01", "T101");
            registerTeamToTournament("TRN-01", "T102");

            registerTeamToTournament("TRN-02", "T103");
            registerTeamToTournament("TRN-02", "T104");

            // 4. Matches
            Match m1 = new Match("M101", "TRN-01", "T101", "T102", "2026-10-18", "Eden Grounds");
            m1.recordResult(185, 172); // Thunderbolts won
            matches.add(m1);

            Match m2 = new Match("M102", "TRN-01", "T102", "T101", "2026-10-22", "National Oval");
            // Scheduled (pending)
            matches.add(m2);

            Match m3 = new Match("M103", "TRN-02", "T103", "T104", "2026-10-19", "City Arena");
            m3.recordResult(2, 2); // Draw
            matches.add(m3);

            Match m4 = new Match("M104", "TRN-02", "T104", "T103", "2026-10-25", "Blaze Stadium");
            m4.recordResult(3, 1); // Blaze United won
            matches.add(m4);

        } catch (Exception e) {
            System.err.println("Sample data seed error: " + e.getMessage());
        }
    }
}
