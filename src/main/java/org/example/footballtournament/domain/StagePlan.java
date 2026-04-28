package org.example.footballtournament.domain;

import java.util.List;

public record StagePlan(
        List<MatchDay> matchDays
) {
}
