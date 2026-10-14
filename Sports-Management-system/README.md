# 🏆 Sports Management System

![Java Version](https://img.shields.io/badge/Java-21%20LTS-orange.svg)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Dependencies](https://img.shields.io/badge/Dependencies-Zero%20(Core%20Java%20Only)-success.svg)

A clean, menu-driven **Sports Management System** built exclusively with **Core Java**. This application manages players, sports squads, tournaments, match scheduling, scoring, and points table calculation with file-based persistence.

No frameworks, no databases, and no external libraries are used—making it ideal for academic submissions, college mini-projects, and demonstrating fundamental Object-Oriented Programming (OOP) concepts.

---

## 📖 Table of Contents
- [Project Overview](#-project-overview)
- [Key Features](#-key-features)
- [Core Java Concepts Demonstrated](#-core-java-concepts-demonstrated)
- [Project Directory Structure](#-project-directory-structure)
- [Prerequisites](#-prerequisites)
- [How to Compile and Run](#-how-to-compile-and-run)
- [Sample Console Output](#-sample-console-output)
- [How Data Persistence Works](#-how-data-persistence-works)
- [Future Improvements](#-future-improvements)
- [License](#-license)

---

## 📌 Project Overview

In athletic departments, collegiate sports, and local leagues, administrators require an efficient mechanism to register players, assign them to teams, organize tournaments, record fixtures, and dynamically compute standings. 

This project provides a robust, menu-driven console application designed according to strict OOP principles, standard naming conventions, and fail-safe console input handling.

---

## ⚡ Key Features

1. **Player Management**
   - Register athletes with unique Player ID, Name, Age (5–100), Sport, and Assigned Team / Free Agent status.
   - View, search (by ID, Name, or Sport), update, and delete player profiles.
   - Automatically synchronizes roster changes with team rosters upon deletion or updates.

2. **Team Management**
   - Create and organize sports teams with unique Team ID, Name, Sport category, and Head Coach.
   - View all registered teams with current squad counts.
   - View team rosters with full squad details.
   - Add/remove athletes to and from rosters (automatically manages Free Agent status).

3. **Tournament Management**
   - Create tournaments with customizable formats (Round Robin, League, Knockout).
   - Enroll teams and inspect all tournament participants.

4. **Match Scheduling & Automatic Scoring**
   - Schedule fixtures between any two participating teams with date, time, and stadium venue.
   - Input match scores with automatic winner calculation or draw/tie detection.
   - Displays match status (`SCHEDULED` vs. `COMPLETED`) and full fixture history.

5. **Dynamic Points Table & Standings**
   - Dynamically calculates rankings for any tournament:
     - **Win**: 3 Points
     - **Draw / Tie**: 1 Point
     - **Loss**: 0 Points
   - Automatically sorts standings using points, wins, and alphabetical tie-breakers.

6. **Data Persistence**
   - Automatically saves and reloads system state using plain-text file storage (`java.io`).
   - Automatically detects and reloads existing records on startup so data is never lost.
   - Includes an optional one-click Demo Data Seeder for instant evaluation.

---

## 🧩 Core Java Concepts Demonstrated

| Concept | Implementation in this Project |
| :--- | :--- |
| **Abstraction** | Abstract class `Person` declares common identity fields and abstract methods (`getRole()`, `displayDetails()`, `toDataString()`) that concrete subclasses must implement. |
| **Inheritance** | `Player` extends `Person`, inheriting state while specializing behavior. Custom exceptions inherit from `SportsException`, which extends `java.lang.Exception`. |
| **Encapsulation** | Private and protected fields, validated getters/setters, and defensive unmodifiable collections (`Collections.unmodifiableList`). |
| **Polymorphism** | Runtime method overriding of `displayDetails()`, `toDataString()`, and `toString()`. `PointsRecord` implements `Comparable<PointsRecord>` for polymorphic sorting. |
| **Collections Framework** | `HashMap<String, Player>`, `HashMap<String, Team>`, and `HashMap<String, Tournament>` for instant $O(1)$ key lookups; `ArrayList` for rosters and fixtures; `Collections.sort()` for sorting. |
| **Exception Handling** | Custom checked exception hierarchy (`SportsException`, `EntityNotFoundException`, `DuplicateEntityException`, `InvalidInputException`) coupled with graceful `try-catch` recovery in UI inputs. |
| **File I/O Streams** | `BufferedReader`, `BufferedWriter`, `FileReader`, and `FileWriter` for persistent local storage in `data/*.txt`. |

---

## 📂 Project Directory Structure

```text
SportsManagementSystem/
│
├── data/                                 # Runtime data persistence directory
│   ├── .gitkeep                          # Tracks directory structure in Git
│   ├── players.txt                       # Persisted player records
│   ├── teams.txt                         # Persisted team & roster records
│   ├── tournaments.txt                   # Persisted tournament records
│   └── matches.txt                       # Persisted fixtures and match results
│
├── src/
│   └── com/
│       └── sports/
│           ├── model/
│           │   ├── Person.java           # Abstract base class
│           │   ├── Player.java           # Player entity extending Person
│           │   ├── Team.java             # Team squad entity
│           │   ├── Tournament.java       # Tournament entity
│           │   ├── Match.java            # Match entity with auto winner detection
│           │   └── PointsRecord.java     # Standings record implementing Comparable
│           │
│           ├── exception/
│           │   ├── SportsException.java          # Base checked exception
│           │   ├── EntityNotFoundException.java  # Entity not found exception
│           │   ├── DuplicateEntityException.java # Duplicate identifier exception
│           │   └── InvalidInputException.java    # Business validation exception
│           │
│           ├── service/
│           │   ├── SportsService.java            # Core business logic service
│           │   └── FileStorageService.java       # File persistence service (java.io)
│           │
│           ├── util/
│           │   ├── InputValidator.java           # Safe Scanner console reader
│           │   └── TableFormatter.java           # Clean ASCII table renderer
│           │
│           └── Main.java                         # Application entry & menu UI
│
├── .gitignore                            # Excludes bytecode, temp files, and runtime data
├── compile.bat                           # One-click Windows compile script
├── run.bat                               # One-click Windows run script
├── LICENSE                               # MIT Open-Source License
└── README.md                             # Project documentation
```

---

## ⚙️ Prerequisites

- **Java Development Kit (JDK)**: Version 8 or higher (tested on JDK 17, 21 LTS).
- **Git**: Installed for version control.
- **Operating System**: Windows, macOS, or Linux.

Verify your Java installation:
```bash
java -version
javac -version
```

---

## 🚀 How to Compile and Run

### Option 1: Terminal / Command Prompt / PowerShell

1. **Clone or navigate to the project directory:**
   ```bash
   cd Sports-Management-system
   ```

2. **Compile all Java source files:**
   ```bash
   javac -d bin src/com/sports/model/*.java src/com/sports/exception/*.java src/com/sports/util/*.java src/com/sports/service/*.java src/com/sports/Main.java
   ```

3. **Run the application:**
   ```bash
   java -cp bin com.sports.Main
   ```

---

### Option 2: Windows Batch Scripts (Quick Launch)

- Double-click **`compile.bat`** to compile all `.java` files into the `bin/` directory.
- Double-click **`run.bat`** to launch the console application directly.

---

### Option 3: IntelliJ IDEA or Eclipse

- **IntelliJ IDEA**: Open the folder -> right-click `src` directory -> **Mark Directory as > Sources Root** -> open `src/com/sports/Main.java` -> click **Run**.
- **Eclipse**: File > Open Projects from File System -> select the directory -> Run `Main.java` as a Java Application.

---

## 🖥️ Sample Console Output

### Main Menu
```text
===============================================================================
               SPORTS MANAGEMENT SYSTEM (CORE JAVA MINI PROJECT)               
===============================================================================

+---------------------------------------+
|           MAIN CONTROL MENU           |
+---------------------------------------+
| 1. Player Management                  |
| 2. Team Management                    |
| 3. Tournament Management              |
| 4. Match Scheduling & Scoring         |
| 5. View Points Table & Standings      |
| 6. Save Data to Files                 |
| 7. Load Sample / Demo Records         |
| 8. Save & Exit                        |
+---------------------------------------+
Enter your choice (1-8): 
```

### Players Directory
```text
+------------+----------------------+-----+-----------------+----------------------+
| PLAYER ID  | NAME                 | AGE | SPORT           | TEAM                 |
+------------+----------------------+-----+-----------------+----------------------+
| P101       | Aarav Sharma         | 22  | Cricket         | Thunderbolts (T101)  |
| P102       | Rohan Verma          | 24  | Cricket         | Thunderbolts (T101)  |
| P103       | Ishan Kishan         | 23  | Cricket         | Thunderbolts (T101)  |
| P104       | Virat Rao            | 25  | Cricket         | Golden Eagles (T102) |
| P111       | Lucas Perez          | 20  | Football        | Free Agent           |
+------------+----------------------+-----+-----------------+----------------------+
Total Players: 5
```

### Tournament Points Table & Standings
```text
==========================================================================================
  OFFICIAL STANDINGS / POINTS TABLE: Inter-College Cricket Cup (Sport: Cricket)
  Scoring Rule: Win = 3 Pts | Draw = 1 Pt | Loss = 0 Pts
==========================================================================================
+------+------------+----------------------+--------+-----+------+------+--------+
| POS  | TEAM ID    | TEAM NAME            | PLAYED | WON | DRAW | LOST | POINTS |
+------+------------+----------------------+--------+-----+------+------+--------+
| 1    | T101       | Thunderbolts         | 2      | 2   | 0    | 0    | 6      |
| 2    | T102       | Golden Eagles        | 2      | 0   | 0    | 2    | 0      |
+------+------------+----------------------+--------+-----+------+------+--------+
```

---

## 💾 How Data Persistence Works

The system maintains persistence without any external database dependencies:
1. Data files are located in the `data/` folder:
   - `players.txt`: `ID|Name|Age|Sport|TeamID`
   - `teams.txt`: `TeamID|TeamName|Sport|CoachName|RosterPlayerIDs`
   - `tournaments.txt`: `TournamentID|Name|Sport|Format|RegisteredTeamIDs`
   - `matches.txt`: `MatchID|TournamentID|Team1ID|Team2ID|Score1|Score2|WinnerID|Status|Date|Venue`
2. **On Application Startup**: [SportsService](file:///c:/Users/Saheel/OneDrive/Desktop/Sports-Management-system/src/com/sports/service/SportsService.java) checks if files exist. If found, records are parsed and restored into the in-memory `HashMap` and `ArrayList` collections.
3. **On Application Exit**: Users are prompted to save changes, which safely writes all entities using buffered I/O streams.
4. **Git Safety**: The `.gitignore` file ignores dynamic `.txt` data files while maintaining the `data/` directory using `.gitkeep`.

---

## 🔮 Future Improvements

- Add player-specific performance statistics (runs, wickets, goals scored, assists).
- Support automatic tournament bracket generation for Knockout formats.
- Export match summaries and points tables directly to PDF or CSV reports.
- Implement role-based administrative login (e.g., Coach vs. Tournament Organizer).

---

## 📄 License

This project is licensed under the [MIT License](LICENSE) - see the LICENSE file for details.
