package org.example.footballtournament.service;

import org.example.footballtournament.config.AppConfigProperties;
import org.example.footballtournament.domain.Gameplan;
import org.example.footballtournament.domain.OrderedMatch;
import org.example.footballtournament.domain.UnOrderedMatch;
import org.example.footballtournament.dto.TeamRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class MatchScheduleServiceTest {

    AppConfigProperties appConfigProperties = new AppConfigProperties(new AppConfigProperties.Tournament("2020-10-17T17:00"));

    MatchScheduleService sut = new MatchScheduleService(appConfigProperties);


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
        assertThat(result.firstStage().matches()).hasSize(15);
        assertThat(result.secondStage().matches()).hasSize(15);

        // assert that the teams are swapped
        for (var i = 0; i < result.firstStage().matches().size(); i++) {
            var teamFromFirstStage = result.firstStage().matches().get(i);
            var teamFromSecondStage = result.secondStage().matches().get(i);

            assertThat(teamFromFirstStage.homeTeam()).isEqualTo(teamFromSecondStage.awayTeam());
            assertThat(teamFromFirstStage.awayTeam()).isEqualTo(teamFromSecondStage.homeTeam());
        }
    }

    @Test
    void test_generateStagePlan() {
        // Arrange
        List<TeamRequest> teams = new ArrayList<>();
        teams.add(new TeamRequest("Team A", null));
        teams.add(new TeamRequest("Team B", null));
        teams.add(new TeamRequest("Team C", null));
        teams.add(new TeamRequest("Team D", null));
        teams.add(new TeamRequest("Team E", null));
        teams.add(new TeamRequest("Team F", null));

        var startDate = LocalDateTime.parse("2020-10-17T17:00");

        // Act
        var result = sut.generateStagePlan(teams, startDate);

        // Assert
        assertThat(result.matches()).hasSize(15);

        for (var i = 0; i < result.matches().size(); i++) {
            var match = result.matches().get(i);
            System.out.println(match);
        }

        System.out.println("----------------------------");

        System.out.println(result.matches());

        // convert to list
        List<OrderedMatch> matches = result.matches();

        var unorderedSet = matches.stream().map(match -> {
            return new UnOrderedMatch(match.homeTeam(), match.awayTeam());
        }).collect(Collectors.toList());

        // assert that the matches are unique
        assertThat(unorderedSet).hasSize(15);
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
        assertThat(result.matches().getFirst().matchTime()).isEqualTo(startDate.plusWeeks(1));

        assertThat(result.matches().get(1).homeTeam()).isEqualTo("Team D");
        assertThat(result.matches().get(1).awayTeam()).isEqualTo("Team C");
        assertThat(result.matches().get(1).matchTime()).isEqualTo(startDate.plusWeeks(2));

        assertThat(result.matches().get(2).homeTeam()).isEqualTo("Team F");
        assertThat(result.matches().get(2).awayTeam()).isEqualTo("Team E");
        assertThat(result.matches().get(2).matchTime()).isEqualTo(startDate.plusWeeks(3));

        assertThat(result.matches().get(3).homeTeam()).isEqualTo("Team H");
        assertThat(result.matches().get(3).awayTeam()).isEqualTo("Team G");
        assertThat(result.matches().get(3).matchTime()).isEqualTo(startDate.plusWeeks(4));

        assertThat(result.matches().get(4).homeTeam()).isEqualTo("Team J");
        assertThat(result.matches().get(4).awayTeam()).isEqualTo("Team I");
        assertThat(result.matches().get(4).matchTime()).isEqualTo(startDate.plusWeeks(5));

        assertThat(result.matches().get(5).homeTeam()).isEqualTo("Team L");
        assertThat(result.matches().get(5).awayTeam()).isEqualTo("Team K");
        assertThat(result.matches().get(5).matchTime()).isEqualTo(startDate.plusWeeks(6));

    }
}