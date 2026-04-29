package org.example.footballtournament.domain;

import java.util.List;

public record Gameplan(StagePlan firstStage, StagePlan secondStage) {

    public record StagePlan(
            List<OrderedMatch> matches
    ) { }


}
