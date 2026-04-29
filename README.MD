# Football Tournament Schedule Generator

A Spring Boot REST service that generates two-stage round-robin football match schedules for a given set of teams.

## How it works

1. **First stage:** All teams play each other once (home/away determined by team order), one match per week starting from a configurable date.
2. **Second stage:** Same matchups with home and away teams swapped. Starts 3 weeks after the first stage ends.

For `n` teams, `n*(n-1)/2` matches are generated per stage.

## Tech Stack

- Java 21
- Spring Boot 4.0.6
- Maven

## Prerequisites

- Java 21+
- Maven (or use the included Maven Wrapper)

## Build & Run

```bash
# Build
./mvnw clean package

# Run as REST server (default)
./mvnw spring-boot:run

# Run as CLI application
./mvnw spring-boot:run -Dspring-boot.run.mainClass=org.example.footballtournament.MyCliApp
```

The REST server starts on port **8080**.

## Configuration

The tournament start date is configurable in `src/main/resources/application.yaml`:

```yaml
app:
  tournament:
    start-date: "2020-10-17T17:00"
```

## API

### `POST /generate-random-matches`

Generates a two-stage match schedule for the given teams.

**Request body:**

```json
{
  "league": "smartlab league",
  "country": "Germany",
  "teams": [
    { "name": "Best marketing", "founding_date": "1998" },
    { "name": "Hot operations", "founding_date": "2000" },
    { "name": "Curious devops" },
    { "name": "Roaming partners", "founding_date": "2001" },
    { "name": "Selfish sales", "founding_date": "2002" },
    { "name": "Whatever comes next" }
  ]
}
```

> `founding_date` is optional.

**Response:**

```json
{
  "firstStage": {
    "matches": [
      {
        "homeTeam": "Best marketing",
        "awayTeam": "Hot operations",
        "matchTime": "2020-10-17T17:00:00"
      }
    ]
  },
  "secondStage": {
    "matches": [
      {
        "homeTeam": "Hot operations",
        "awayTeam": "Best marketing",
        "matchTime": "2021-02-20T17:00:00"
      }
    ]
  }
}
```

A full example request is in `src/main/resources/teams.json`, and a full example response in `src/main/resources/response.json`.

**Example with curl:**

```bash
curl -X POST http://localhost:8080/generate-random-matches \
  -H "Content-Type: application/json" \
  -d @src/main/resources/teams.json
```

## Domain Model

```
Match (sealed interface)
├── OrderedMatch   – match with scheduled date (homeTeam, awayTeam, matchTime)
└── UnOrderedMatch – unscheduled match used during schedule generation

Gameplan
├── firstStage  : StagePlan (List<OrderedMatch>)
└── secondStage : StagePlan (List<OrderedMatch>)
```

## Tests

```bash
./mvnw test
```

| Test | Description |
|------|-------------|
| `test_generateMatchSchedule` | 6 teams → 15 matches per stage; verifies home/away swap between stages |
| `test_generateStagePlan` | 6 teams → 15 matches with correct weekly scheduling |
| `test_generateMatchDay` | Random match day generates 3 matches using all 6 teams exactly once |
| `test_swapSchedule` | Home/away teams are correctly swapped; match dates increase by one week each |
