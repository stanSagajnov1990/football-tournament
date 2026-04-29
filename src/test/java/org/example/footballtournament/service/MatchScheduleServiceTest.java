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
    void test_generateSimpleMatchSchedule() {
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
        assertThat(result.matches()).hasSize(15);

        for (var i = 0; i < result.matches().size(); i++) {
            var match = result.matches().get(i);
            System.out.println(match);

            //            System.out.println("Spieltag " + (i + 1));

//            matchDay.matches().forEach(match -> {
//            });
        }

        System.out.println("----------------------------");


        System.out.println(result.matches());
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
                new OrderedMatch("Team A", "Team B", null),
                new OrderedMatch("Team C", "Team D", null),
                new OrderedMatch("Team E", "Team F", null),
                new OrderedMatch("Team G", "Team H", null),
                new OrderedMatch("Team I", "Team J", null),
                new OrderedMatch("Team K", "Team L", null)
        )
        );
        LocalDateTime startDate = LocalDateTime.now();

        // Act
        var result = sut.swapSchedule(stagePlan, startDate);


        assertThat(result.matches()).hasSize(6);

        assertThat(result.matches().getFirst().homeTeam()).isEqualTo("Team B");
        assertThat(result.matches().getFirst().awayTeam()).isEqualTo("Team A");

        assertThat(result.matches().get(1).homeTeam()).isEqualTo("Team D");
        assertThat(result.matches().get(1).awayTeam()).isEqualTo("Team C");

        assertThat(result.matches().get(2)).isEqualTo(new OrderedMatch("Team F", "Team E", null));

        assertThat(result.matches().get(3)).isEqualTo(new OrderedMatch("Team H", "Team G", null));

        assertThat(result.matches().get(4)).isEqualTo(new OrderedMatch("Team J", "Team I", null));

        assertThat(result.matches().getLast()).isEqualTo(new OrderedMatch("Team L", "Team K", null));
    }
}