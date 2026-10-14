package com.sports;

import com.sports.exception.DuplicateEntityException;
import com.sports.exception.EntityNotFoundException;
import com.sports.exception.InvalidInputException;
import com.sports.model.*;
import com.sports.service.SportsService;
import com.sports.util.InputValidator;
import com.sports.util.TableFormatter;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Main Application Class for the Sports Management System.
 * Provides an interactive, menu-driven console interface.
 * Core Java Only (OOP, Collections, Exception Handling, File I/O).
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SportsService sportsService = new SportsService();

    public static void main(String[] args) {
        printBanner();

        // Check and load existing persisted data on startup
        if (sportsService.hasSavedData()) {
            sportsService.loadData();
            System.out.println(">> Existing system records successfully loaded from data files.");
            System.out.printf(">> Loaded: %d Players, %d Teams, %d Tournaments, %d Matches.%n%n",
                    sportsService.getAllPlayers().size(),
                    sportsService.getAllTeams().size(),
                    sportsService.getAllTournaments().size(),
                    sportsService.getAllMatches().size());
        } else {
            System.out.println(">> No existing saved data found.");
            boolean seed = InputValidator.readYesNo(scanner, ">> Would you like to load sample demo records for quick testing?");
            if (seed) {
                sportsService.seedSampleData();
                System.out.println(">> Demo records loaded successfully!\n");
            }
        }

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = InputValidator.readInt(scanner, "Enter your choice (1-8): ", 1, 8);

            switch (choice) {
                case 1 -> handlePlayerMenu();
                case 2 -> handleTeamMenu();
                case 3 -> handleTournamentMenu();
                case 4 -> handleMatchMenu();
                case 5 -> handlePointsTableMenu();
                case 6 -> handleSaveData();
                case 7 -> handleLoadSampleData();
                case 8 -> {
                    handleExit();
                    running = false;
                }
            }
        }
        scanner.close();
    }

    private static void printBanner() {
        System.out.println("===============================================================================");
        System.out.println("               SPORTS MANAGEMENT SYSTEM (CORE JAVA MINI PROJECT)               ");
        System.out.println("===============================================================================");
        System.out.println("   * Object-Oriented Programming (Inheritance, Polymorphism, Encapsulation)     ");
        System.out.println("   * Collections Framework (ArrayList, HashMap, Collections.sort)              ");
        System.out.println("   * Custom Exception Handling & File I/O Data Persistence                     ");
        System.out.println("===============================================================================\n");
    }

    private static void printMainMenu() {
        System.out.println("\n+---------------------------------------+");
        System.out.println("|           MAIN CONTROL MENU           |");
        System.out.println("+---------------------------------------+");
        System.out.println("| 1. Player Management                  |");
        System.out.println("| 2. Team Management                    |");
        System.out.println("| 3. Tournament Management              |");
        System.out.println("| 4. Match Scheduling & Scoring         |");
        System.out.println("| 5. View Points Table & Standings      |");
        System.out.println("| 6. Save Data to Files                 |");
        System.out.println("| 7. Load Sample / Demo Records         |");
        System.out.println("| 8. Save & Exit                        |");
        System.out.println("+---------------------------------------+");
    }

    // =========================================================================
    // 1. PLAYER MANAGEMENT
    // =========================================================================

    private static void handlePlayerMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- [ PLAYER MANAGEMENT ] ---");
            System.out.println("1. Add New Player");
            System.out.println("2. View All Players");
            System.out.println("3. Search Player (by ID, Name, or Sport)");
            System.out.println("4. Update Player Details");
            System.out.println("5. Delete Player");
            System.out.println("6. Back to Main Menu");

            int choice = InputValidator.readInt(scanner, "Choose an option (1-6): ", 1, 6);
            switch (choice) {
                case 1 -> addPlayer();
                case 2 -> viewAllPlayers();
                case 3 -> searchPlayer();
                case 4 -> updatePlayer();
                case 5 -> deletePlayer();
                case 6 -> back = true;
            }
        }
    }

    private static void addPlayer() {
        System.out.println("\n>> ADD NEW PLAYER");
        String id = InputValidator.readString(scanner, "Enter Player ID (e.g. P101): ", true);
        String name = InputValidator.readString(scanner, "Enter Player Full Name: ", true);
        int age = InputValidator.readInt(scanner, "Enter Player Age (5 - 100): ", 5, 100);
        String sport = InputValidator.readString(scanner, "Enter Sport (e.g. Cricket, Football): ", true);

        System.out.println("Available Teams: ");
        List<Team> teams = sportsService.getAllTeams();
        if (teams.isEmpty()) {
            System.out.println("  (No teams created yet. Player will be added as Free Agent)");
        } else {
            for (Team t : teams) {
                System.out.printf("  - %s: %s (%s)%n", t.getTeamId(), t.getTeamName(), t.getSport());
            }
        }
        String teamId = InputValidator.readString(scanner, "Enter Team ID (or press Enter for UNASSIGNED): ", false);
        if (teamId.isEmpty()) {
            teamId = "UNASSIGNED";
        }

        try {
            Player player = new Player(id, name, age, sport, teamId);
            sportsService.addPlayer(player);
            System.out.println(">> Success: Player added successfully: " + player.getName());
        } catch (DuplicateEntityException | InvalidInputException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    private static void viewAllPlayers() {
        System.out.println("\n>> REGISTERED PLAYERS DIRECTORY");
        List<Player> players = sportsService.getAllPlayers();
        TableFormatter.printPlayersTable(players, sportsService.getTeamsMap());
    }

    private static void searchPlayer() {
        System.out.println("\n>> SEARCH PLAYERS");
        String query = InputValidator.readString(scanner, "Enter search keyword (ID, Name, or Sport): ", true);
        List<Player> results = sportsService.searchPlayers(query);
        System.out.println("\nSearch Results for '" + query + "':");
        TableFormatter.printPlayersTable(results, sportsService.getTeamsMap());
    }

    private static void updatePlayer() {
        System.out.println("\n>> UPDATE PLAYER");
        String id = InputValidator.readString(scanner, "Enter Player ID to update: ", true);
        try {
            Player p = sportsService.getPlayer(id);
            System.out.println("Current Details: " + p.displayDetails());

            System.out.println("(Press Enter to keep existing value)");
            String newName = InputValidator.readString(scanner, "New Name [" + p.getName() + "]: ", false);
            String ageStr = InputValidator.readString(scanner, "New Age [" + p.getAge() + "]: ", false);
            int newAge = ageStr.isEmpty() ? p.getAge() : Integer.parseInt(ageStr);
            String newSport = InputValidator.readString(scanner, "New Sport [" + p.getSport() + "]: ", false);
            String newTeam = InputValidator.readString(scanner, "New Team ID [" + p.getTeamId() + "]: ", false);

            sportsService.updatePlayer(id,
                    newName.isEmpty() ? p.getName() : newName,
                    newAge,
                    newSport.isEmpty() ? p.getSport() : newSport,
                    newTeam.isEmpty() ? p.getTeamId() : newTeam);
            System.out.println(">> Success: Player details updated successfully!");
        } catch (EntityNotFoundException | InvalidInputException e) {
            System.out.println(">> Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(">> Error: Invalid age entered. Update cancelled.");
        }
    }

    private static void deletePlayer() {
        System.out.println("\n>> DELETE PLAYER");
        String id = InputValidator.readString(scanner, "Enter Player ID to delete: ", true);
        try {
            Player p = sportsService.getPlayer(id);
            boolean confirm = InputValidator.readYesNo(scanner, "Are you sure you want to delete " + p.getName() + "?");
            if (confirm) {
                sportsService.deletePlayer(id);
                System.out.println(">> Success: Player removed from system.");
            } else {
                System.out.println(">> Deletion cancelled.");
            }
        } catch (EntityNotFoundException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // 2. TEAM MANAGEMENT
    // =========================================================================

    private static void handleTeamMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- [ TEAM MANAGEMENT ] ---");
            System.out.println("1. Create New Team");
            System.out.println("2. View All Teams");
            System.out.println("3. View Team Roster (Squad Players)");
            System.out.println("4. Add Player to Team");
            System.out.println("5. Remove Player from Team");
            System.out.println("6. Delete Team");
            System.out.println("7. Back to Main Menu");

            int choice = InputValidator.readInt(scanner, "Choose an option (1-7): ", 1, 7);
            switch (choice) {
                case 1 -> createTeam();
                case 2 -> viewAllTeams();
                case 3 -> viewTeamRoster();
                case 4 -> addPlayerToTeam();
                case 5 -> removePlayerFromTeam();
                case 6 -> deleteTeam();
                case 7 -> back = true;
            }
        }
    }

    private static void createTeam() {
        System.out.println("\n>> CREATE NEW TEAM");
        String id = InputValidator.readString(scanner, "Enter Team ID (e.g. T101): ", true);
        String name = InputValidator.readString(scanner, "Enter Team Name: ", true);
        String sport = InputValidator.readString(scanner, "Enter Sport (e.g. Cricket, Football): ", true);
        String coach = InputValidator.readString(scanner, "Enter Head Coach / Captain Name: ", true);

        try {
            Team team = new Team(id, name, sport, coach);
            sportsService.createTeam(team);
            System.out.println(">> Success: Team created: " + team.getTeamName());
        } catch (DuplicateEntityException | InvalidInputException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    private static void viewAllTeams() {
        System.out.println("\n>> ALL REGISTERED TEAMS");
        List<Team> teams = sportsService.getAllTeams();
        TableFormatter.printTeamsTable(teams);
    }

    private static void viewTeamRoster() {
        System.out.println("\n>> VIEW TEAM ROSTER");
        String teamId = InputValidator.readString(scanner, "Enter Team ID: ", true);
        try {
            Team team = sportsService.getTeam(teamId);
            List<Player> roster = sportsService.getTeamRoster(teamId);
            TableFormatter.printTeamRoster(team, roster);
        } catch (EntityNotFoundException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    private static void addPlayerToTeam() {
        System.out.println("\n>> ADD PLAYER TO TEAM");
        String teamId = InputValidator.readString(scanner, "Enter Target Team ID: ", true);
        String playerId = InputValidator.readString(scanner, "Enter Player ID to add: ", true);
        try {
            sportsService.addPlayerToTeam(teamId, playerId);
            System.out.println(">> Success: Player added to team squad.");
        } catch (EntityNotFoundException | InvalidInputException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    private static void removePlayerFromTeam() {
        System.out.println("\n>> REMOVE PLAYER FROM TEAM");
        String teamId = InputValidator.readString(scanner, "Enter Team ID: ", true);
        String playerId = InputValidator.readString(scanner, "Enter Player ID to remove: ", true);
        try {
            sportsService.removePlayerFromTeam(teamId, playerId);
            System.out.println(">> Success: Player removed from team and designated as Free Agent.");
        } catch (EntityNotFoundException | InvalidInputException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    private static void deleteTeam() {
        System.out.println("\n>> DELETE TEAM");
        String teamId = InputValidator.readString(scanner, "Enter Team ID to delete: ", true);
        try {
            Team t = sportsService.getTeam(teamId);
            boolean confirm = InputValidator.readYesNo(scanner,
                    "Are you sure you want to delete " + t.getTeamName() + "? Roster members will become Free Agents.");
            if (confirm) {
                sportsService.deleteTeam(teamId);
                System.out.println(">> Success: Team deleted.");
            } else {
                System.out.println(">> Deletion cancelled.");
            }
        } catch (EntityNotFoundException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // 3. TOURNAMENT MANAGEMENT
    // =========================================================================

    private static void handleTournamentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- [ TOURNAMENT MANAGEMENT ] ---");
            System.out.println("1. Create New Tournament");
            System.out.println("2. View All Tournaments");
            System.out.println("3. Register Team into Tournament");
            System.out.println("4. View Tournament Participants");
            System.out.println("5. Back to Main Menu");

            int choice = InputValidator.readInt(scanner, "Choose an option (1-5): ", 1, 5);
            switch (choice) {
                case 1 -> createTournament();
                case 2 -> viewAllTournaments();
                case 3 -> registerTeamToTournament();
                case 4 -> viewTournamentParticipants();
                case 5 -> back = true;
            }
        }
    }

    private static void createTournament() {
        System.out.println("\n>> CREATE NEW TOURNAMENT");
        String id = InputValidator.readString(scanner, "Enter Tournament ID (e.g. TRN-01): ", true);
        String name = InputValidator.readString(scanner, "Enter Tournament Name: ", true);
        String sport = InputValidator.readString(scanner, "Enter Sport: ", true);
        String format = InputValidator.readString(scanner, "Enter Format (e.g. Round Robin, Knockout, League): ", true);

        try {
            Tournament tournament = new Tournament(id, name, sport, format);
            sportsService.createTournament(tournament);
            System.out.println(">> Success: Tournament created: " + tournament.getName());
        } catch (DuplicateEntityException | InvalidInputException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    private static void viewAllTournaments() {
        System.out.println("\n>> TOURNAMENTS DIRECTORY");
        List<Tournament> tournaments = sportsService.getAllTournaments();
        TableFormatter.printTournamentsTable(tournaments);
    }

    private static void registerTeamToTournament() {
        System.out.println("\n>> REGISTER TEAM TO TOURNAMENT");
        String tourneyId = InputValidator.readString(scanner, "Enter Tournament ID: ", true);
        String teamId = InputValidator.readString(scanner, "Enter Team ID to register: ", true);

        try {
            sportsService.registerTeamToTournament(tourneyId, teamId);
            System.out.println(">> Success: Team registered to tournament successfully!");
        } catch (EntityNotFoundException | InvalidInputException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    private static void viewTournamentParticipants() {
        System.out.println("\n>> VIEW TOURNAMENT PARTICIPANTS");
        String tourneyId = InputValidator.readString(scanner, "Enter Tournament ID: ", true);
        try {
            Tournament t = sportsService.getTournament(tourneyId);
            List<Team> registeredTeams = sportsService.getTournamentTeams(tourneyId);
            System.out.printf("%nParticipants for Tournament: %s (ID: %s, Sport: %s)%n",
                    t.getName(), t.getTournamentId(), t.getSport());
            TableFormatter.printTeamsTable(registeredTeams);
        } catch (EntityNotFoundException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // 4. MATCH MANAGEMENT
    // =========================================================================

    private static void handleMatchMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- [ MATCH MANAGEMENT ] ---");
            System.out.println("1. Schedule New Match");
            System.out.println("2. View All Matches");
            System.out.println("3. Record / Enter Match Scores");
            System.out.println("4. View Matches by Tournament");
            System.out.println("5. Back to Main Menu");

            int choice = InputValidator.readInt(scanner, "Choose an option (1-5): ", 1, 5);
            switch (choice) {
                case 1 -> scheduleMatch();
                case 2 -> viewAllMatches();
                case 3 -> recordMatchScores();
                case 4 -> viewMatchesByTournament();
                case 5 -> back = true;
            }
        }
    }

    private static void scheduleMatch() {
        System.out.println("\n>> SCHEDULE NEW MATCH");
        String matchId = InputValidator.readString(scanner, "Enter Match ID (e.g. M101): ", true);
        String tournamentId = InputValidator.readString(scanner, "Enter Tournament ID: ", true);
        String team1Id = InputValidator.readString(scanner, "Enter Team 1 ID: ", true);
        String team2Id = InputValidator.readString(scanner, "Enter Team 2 ID: ", true);
        String date = InputValidator.readString(scanner, "Enter Match Date (YYYY-MM-DD): ", false);
        String venue = InputValidator.readString(scanner, "Enter Venue Stadium: ", false);

        try {
            Match match = new Match(matchId, tournamentId, team1Id, team2Id, date, venue);
            sportsService.scheduleMatch(match);
            System.out.println(">> Success: Match scheduled successfully between " + team1Id + " and " + team2Id);
        } catch (DuplicateEntityException | EntityNotFoundException | InvalidInputException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    private static void viewAllMatches() {
        System.out.println("\n>> ALL FIXTURES & MATCH HISTORY");
        List<Match> matches = sportsService.getAllMatches();
        TableFormatter.printMatchesTable(matches, sportsService.getTeamsMap());
    }

    private static void recordMatchScores() {
        System.out.println("\n>> RECORD MATCH RESULT & SCORES");
        String matchId = InputValidator.readString(scanner, "Enter Match ID: ", true);
        try {
            Match match = sportsService.getMatch(matchId);
            System.out.printf("Match Details: %s vs %s (Status: %s)%n",
                    match.getTeam1Id(), match.getTeam2Id(), match.getStatus());

            int score1 = InputValidator.readInt(scanner, "Enter score for " + match.getTeam1Id() + ": ", 0, 9999);
            int score2 = InputValidator.readInt(scanner, "Enter score for " + match.getTeam2Id() + ": ", 0, 9999);

            sportsService.recordMatchScore(matchId, score1, score2);

            System.out.println(">> Success: Scores recorded!");
            String winnerText = Match.RESULT_DRAW.equalsIgnoreCase(match.getWinnerId()) ?
                    "Result is a DRAW / TIE" :
                    "Winner: " + match.getWinnerId();
            System.out.println(">> Automatic Winner Determination: " + winnerText);
        } catch (EntityNotFoundException | InvalidInputException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    private static void viewMatchesByTournament() {
        System.out.println("\n>> VIEW MATCHES BY TOURNAMENT");
        String tourneyId = InputValidator.readString(scanner, "Enter Tournament ID: ", true);
        List<Match> tourneyMatches = sportsService.getMatchesByTournament(tourneyId);
        TableFormatter.printMatchesTable(tourneyMatches, sportsService.getTeamsMap());
    }

    // =========================================================================
    // 5. POINTS TABLE & STANDINGS
    // =========================================================================

    private static void handlePointsTableMenu() {
        System.out.println("\n>> TOURNAMENT STANDINGS & POINTS TABLE");
        List<Tournament> tournaments = sportsService.getAllTournaments();
        if (tournaments.isEmpty()) {
            System.out.println("No tournaments available to display standings.");
            return;
        }

        System.out.println("Available Tournaments:");
        for (Tournament t : tournaments) {
            System.out.printf("  - %s: %s (%s)%n", t.getTournamentId(), t.getName(), t.getSport());
        }

        String tourneyId = InputValidator.readString(scanner, "Enter Tournament ID to view Standings: ", true);
        try {
            Tournament tournament = sportsService.getTournament(tourneyId);
            List<PointsRecord> standings = sportsService.calculateStandings(tourneyId);
            TableFormatter.printPointsTable(tournament, standings);
        } catch (EntityNotFoundException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    // =========================================================================
    // 6. SAVE DATA
    // =========================================================================

    private static void handleSaveData() {
        try {
            sportsService.saveData();
            System.out.println(">> Success: All player, team, tournament, and match records saved to files in 'data/' directory.");
        } catch (IOException e) {
            System.out.println(">> Error saving data: " + e.getMessage());
        }
    }

    // =========================================================================
    // 7. LOAD SAMPLE DATA
    // =========================================================================

    private static void handleLoadSampleData() {
        boolean confirm = InputValidator.readYesNo(scanner, "Load demo records (Cricket & Football Tournaments)?");
        if (confirm) {
            sportsService.seedSampleData();
            System.out.println(">> Demo records populated! You can now check Teams, Players, Matches, and Standings.");
        }
    }

    // =========================================================================
    // 8. EXIT
    // =========================================================================

    private static void handleExit() {
        boolean save = InputValidator.readYesNo(scanner, "Would you like to save changes before exiting?");
        if (save) {
            try {
                sportsService.saveData();
                System.out.println(">> Data successfully persisted.");
            } catch (IOException e) {
                System.out.println(">> Warning: Could not save data: " + e.getMessage());
            }
        }
        System.out.println("\nThank you for using the Sports Management System. Goodbye!");
    }
}
