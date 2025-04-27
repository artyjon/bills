# Utility Bills Calculator

A JavaFX desktop application for calculating monthly utility payments with tariff management and historical data tracking.
<img width="1008" alt="Screenshot 2025-04-28 at 01 12 42" src="https://github.com/user-attachments/assets/da202ab2-3884-4165-8be9-f00ba45475a1" />
<img width="1012" alt="Screenshot 2025-04-28 at 01 13 07" src="https://github.com/user-attachments/assets/223943fd-dc6c-4e70-90ce-6451152ed472" />

## Features
- **Tariff Management**: Store and edit utility tariffs (cold/hot water, sewerage, electricity)
- **Monthly Calculation**: 
  - Input monthly consumption metrics
  - Automatic cost calculation
  - Quick field reset functionality
- **History Tracking**: 
  - SQLite database storage
  - Tabular display of previous months' data
- **Self-contained**: No external database required

## Technologies
- **Core**: Java 17, JavaFX 21.0.7
- **UI**: ControlsFX 11.1.2, FormsFX 11.6.0, BootstrapFX 0.4.0
- **Database**: SQLite (embedded)
- **Validation**: ValidatorFX 0.5.0
- **Logging**: SLF4J 2.0.12 + Logback 1.5.13
- **Testing**: JUnit 5.10.2, TestFX 4.0.16-alpha, Mockito 5.14.2
- **Build**: Maven

## Project Structure
```
├── src
│   ├── main
│   │   ├── java/org/markproject/bills
│   │   │   ├── alerts           # Notification handling
│   │   │   ├── connection       # Database operations
│   │   │   ├── controllers      # JavaFX controllers
│   │   │   ├── dto              # Data Transfer Objects
│   │   │   ├── initializer      # Application initialization
│   │   │   └── tabs             # UI tabs
│   │   └── resources
│   │       └── META-INF         # Project metadata
│   │
│   └── test
│       └── java/org/markproject/bills
│           ├── alerts           # Notification tests
│           └── connection       # Connection tests
│
├── pom.xml                      # Maven configuration
└── README.md                    # Documentation
```

## Installation & Usage
1. **Requirements**: Java 17+, Maven
2. **Build**:
   ```bash
   mvn clean install
## Run
```bash
   mvn javafx:run
