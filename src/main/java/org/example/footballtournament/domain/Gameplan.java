package org.example.footballtournament.domain;

import java.util.List;

public record Gameplan(
        List<MatchDay> matchDays
) {
}
