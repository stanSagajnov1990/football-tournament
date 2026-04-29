package org.example.footballtournament.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record OrderedMatch(
        String homeTeam,
        String awayTeam,
        LocalDateTime matchTime
) implements Match {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public String toString() {
        if (matchTime == null) {
            return "%s;%s".formatted(homeTeam, awayTeam);
        }

        return "%s;%s;%s".formatted(DATE_FORMATTER.format(matchTime), homeTeam, awayTeam);
    }

    public OrderedMatch swapTeams() {
        return new OrderedMatch(awayTeam, homeTeam, matchTime);
    }
}