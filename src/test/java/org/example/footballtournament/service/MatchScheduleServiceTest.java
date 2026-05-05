package org.example.footballtournament.service;

import org.example.footballtournament.dto.TeamRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MatchScheduleServiceTest {

    MatchScheduleService sut = new MatchScheduleService();


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
        var result = sut.generateMatchSchedule(teams);

        // Assert
        assertThat(result.matchDays()).hasSize(5);

        for (var i = 0; i < result.matchDays().size(); i++) {
            var matchDay = result.matchDays().get(i);
            System.out.println("Spieltag " + (i + 1));

            matchDay.matches().forEach(match -> {
                System.out.println(match);
            });
        }

        System.out.println(result.matchDays());
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
        var result = sut.generateMatchDay(teams, Set.of());

        // Assert
        assertThat(result).hasSize(3);

        var allReturnedTeams = result.stream()
                .map(match -> { return List.of(match.homeTeam(), match.awayTeam()); })
                .flatMap(Collection::stream)
                .sorted()
                .toList();

        assertThat(teams.stream().map(TeamRequest::name)).isEqualTo(allReturnedTeams);

    }

    @Test
    void test_generateMatchDay() {
        // Arrange
        List<TeamRequest> teams = new ArrayList<>();
        teams.add(new TeamRequest("Team A", null));
        teams.add(new TeamRequest("Team B", null));
        teams.add(new TeamRequest("Team C", null));
        teams.add(new TeamRequest("Team D", null));
        teams.add(new TeamRequest("Team E", null));
        teams.add(new TeamRequest("Team F", null));

        // Act
        var result = sut.generateMatchDay(teams, Set.of());

        // Assert
        assertThat(result).hasSize(3);

        var allReturnedTeams = result.stream()
                .map(match -> {
                    return List.of(match.homeTeam(), match.awayTeam());
                })
                .flatMap(Collection::stream)
                .sorted()
                .toList();

        assertThat(teams.stream().map(TeamRequest::name)).isEqualTo(allReturnedTeams);
    }
}
