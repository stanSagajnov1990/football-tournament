package org.example.footballtournament.service;

import org.assertj.core.api.Assertions;
import org.example.footballtournament.dto.TeamRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchScheduleServiceTest {

    @Test
    void generateMatchSchedule() {
        // Arrange
        List<TeamRequest> teams = new ArrayList<>();
        teams.add(new TeamRequest("Team A", null));
        teams.add(new TeamRequest("Team B", null));
        teams.add(new TeamRequest("Team C", null));
        teams.add(new TeamRequest("Team D", null));
        teams.add(new TeamRequest("Team E", null));
        teams.add(new TeamRequest("Team F", null));

        // Act
        MatchScheduleService matchScheduleService = new MatchScheduleService();
        var result = matchScheduleService.generateMatchSchedule(teams);

        // Assert

//        Assertions.assertThat(result.matches).hasSize(5);
    }

    @Test
    void generateMatchDay() {
        // Arrange
        List<TeamRequest> teams = new ArrayList<>();
        teams.add(new TeamRequest("Team A", null));
        teams.add(new TeamRequest("Team B", null));
        teams.add(new TeamRequest("Team C", null));
        teams.add(new TeamRequest("Team D", null));
        teams.add(new TeamRequest("Team E", null));
        teams.add(new TeamRequest("Team F", null));

        // Act
        MatchScheduleService matchScheduleService = new MatchScheduleService();
        var result = matchScheduleService.generateMatchDay(teams);

        // Assert
        Assertions.assertThat(result).hasSize(3);

        var allReturnedTeams = result.stream()
                .map(match -> { return List.of(match.homeTeam(), match.awayTeam()); })
                .flatMap(Collection::stream)
                .sorted()
                .toList();

        Assertions.assertThat(teams.stream().map(TeamRequest::name)).isEqualTo(allReturnedTeams);

    }
}