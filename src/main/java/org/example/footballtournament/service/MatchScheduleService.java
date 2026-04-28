package org.example.footballtournament.service;

import org.example.footballtournament.domain.*;
import org.example.footballtournament.dto.TeamRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MatchScheduleService {


    public StagePlan generateMatchSchedule(List<TeamRequest> teams) {

        Set<Match> spieltagSet = new LinkedHashSet<>();

        // 5 Spieltage
        IntStream.range(0, 5).forEach(i -> {
            System.out.println(i);

            var matchDay = generateMatchDay(teams, spieltagSet);
            spieltagSet.addAll(matchDay);
        });

        var matchDays = getMatchDays(spieltagSet);

        return new StagePlan(matchDays);
    }

    public static @NonNull ArrayList<MatchDay> getMatchDays(Set<Match> spieltagSet, LocalDateTime startDate) {
        LocalDateTime date = startDate == null ? LocalDateTime.parse("2020-10-17T17:00") : startDate;

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
        return matchDays;
    }

    public static @NonNull ArrayList<MatchDay> getMatchDays(Set<Match> spieltagSet) {
        return getMatchDays(spieltagSet, null);
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

            var match = new UnOrderedMatch(teamName.name(), teamName2.name());

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

        return teams.get(randomIndex);
    }

    public StagePlan swapSchedule(StagePlan stagePlan, LocalDateTime startDate) {

        LinkedHashSet<Match> matchDays = stagePlan.matchDays()
                .stream()
                .map(MatchDay::matches)
                .flatMap(Collection::stream)
                .map(OrderedMatch::swapTeams)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return new StagePlan(getMatchDays(matchDays, startDate));
    }
}
