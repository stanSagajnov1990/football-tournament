package org.example.footballtournament.service;

import org.example.footballtournament.domain.Gameplan;
import org.example.footballtournament.domain.Match;
import org.example.footballtournament.domain.MatchDay;
import org.example.footballtournament.domain.OrderedMatch;
import org.example.footballtournament.dto.TeamRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class MatchScheduleService {


    public Gameplan generateMatchSchedule(List<TeamRequest> teams) {

        Set<Match> spieltagSet = new LinkedHashSet<>();

        // 5 Spieltage
        IntStream.range(0, 5).forEach(i -> {
            System.out.println(i);

            var matchDay = generateMatchDay(teams, spieltagSet);
            spieltagSet.addAll(matchDay);
        });

        LocalDateTime date = LocalDateTime.parse("2020-10-17T17:00");

        var matchDays = new ArrayList<MatchDay>();
        for (var match : spieltagSet) {
            if (!matchDays.isEmpty()) {
                // start incrementing weeks after the first match
                date = date.plusDays(7);
            }

            if (matchDays.isEmpty() || matchDays.getLast().matches().size() == 3) {
                var matches = new ArrayList<OrderedMatch>();
                matches.add(createOrderedMatch(match, date));
                matchDays.add(new MatchDay(matches));

                continue;
            }

            matchDays.getLast().matches().add(createOrderedMatch(match, date));
        }


        return new Gameplan(matchDays);
    }

    public Set<Match> generateMatchDay(List<TeamRequest> teams, Set<Match> spieltagSet) {
        var incomingTeams = new ArrayList<>(teams);

        Set<String> teamSet = new LinkedHashSet<>();
        Set<Match> matchSet = new LinkedHashSet<>();
        var random = new Random();

        while (matchSet.size() <= 2) {

            var teamName = generateRandomTeam(incomingTeams, random);
            var teamName2 = generateRandomTeam(incomingTeams, random);

            if (teamName.equals(teamName2) || (teamSet.contains(teamName.name()) || teamSet.contains(teamName2.name()))) {
                continue;
            }

            var match = new Match(teamName.name(), teamName2.name());

            if (matchSet.contains(match) || spieltagSet.contains(match)) {
                continue;
            }

            matchSet.add(match);
            teamSet.add(teamName.name());
            teamSet.add(teamName2.name());

            System.out.println(teamName);

            incomingTeams.remove(teamName);
            incomingTeams.remove(teamName2);

        }

        return matchSet;
    }

    private static @NonNull OrderedMatch createOrderedMatch(Match match, LocalDateTime matchTime) {
        return new OrderedMatch(match.homeTeam(), match.awayTeam(), matchTime);
    }

    private static TeamRequest generateRandomTeam(List<TeamRequest> teams, Random random) {
        var randomIndex = random.nextInt(teams.size());

        var teamName = teams.get(randomIndex);
        return teamName;
    }
}
