package org.example.footballtournament.domain;

import java.util.List;

public record Gameplan(StagePlan firstStage, StagePlan secondStage) {

    public record StagePlan(
            List<MatchDay> matchDays
    ) { }

    public record MatchDay(List<OrderedMatch> matches) { }
}
