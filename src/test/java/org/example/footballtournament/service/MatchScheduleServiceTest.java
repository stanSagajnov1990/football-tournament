package org.example.footballtournament.service;

import org.example.footballtournament.domain.Gameplan;
import org.example.footballtournament.domain.OrderedMatch;
import org.example.footballtournament.dto.TeamRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MatchScheduleServiceTest {

    MatchScheduleService sut = new MatchScheduleService();

    @Test
    void test_generateMatchSchedule() {
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

    @Test
    void test_swapSchedule() {
        // Arrange
        var stagePlan = new Gameplan.StagePlan(List.of(
                new Gameplan.MatchDay(List.of(
                        new OrderedMatch("Team A", "Team B", null),
                        new OrderedMatch("Team C", "Team D", null),
                        new OrderedMatch("Team E", "Team F", null)
                )),
                new Gameplan.MatchDay(
                        List.of(
                                new OrderedMatch("Team G", "Team H", null),
                                new OrderedMatch("Team I", "Team J", null),
                                new OrderedMatch("Team K", "Team L", null)
                        )
                )
        )
        );
        LocalDateTime startDate = LocalDateTime.now();

        // Act
        var result = sut.swapSchedule(stagePlan, startDate);


        assertThat(result.matchDays()).hasSize(2);
        assertThat(result.matchDays().getFirst().matches()).hasSize(3);

        assertThat(result.matchDays().getFirst().matches().getFirst().homeTeam()).isEqualTo("Team B");
        assertThat(result.matchDays().getFirst().matches().getFirst().awayTeam()).isEqualTo("Team A");
        assertThat(result.matchDays().getFirst().matches().getFirst().matchTime()).isEqualTo(startDate);

        assertThat(result.matchDays().getFirst().matches().getLast().homeTeam()).isEqualTo("Team F");
        assertThat(result.matchDays().getFirst().matches().getLast().awayTeam()).isEqualTo("Team E");
        assertThat(result.matchDays().getFirst().matches().getLast().matchTime()).isEqualTo(startDate.plusWeeks(2));

        assertThat(result.matchDays().getLast().matches().getFirst().homeTeam()).isEqualTo("Team H");
        assertThat(result.matchDays().getLast().matches().getFirst().awayTeam()).isEqualTo("Team G");
        assertThat(result.matchDays().getLast().matches().getFirst().matchTime()).isEqualTo(startDate.plusWeeks(3));

        assertThat(result.matchDays().getLast().matches().getLast().homeTeam()).isEqualTo("Team L");
        assertThat(result.matchDays().getLast().matches().getLast().awayTeam()).isEqualTo("Team K");
        assertThat(result.matchDays().getLast().matches().getLast().matchTime()).isEqualTo(startDate.plusWeeks(5));
    }
}