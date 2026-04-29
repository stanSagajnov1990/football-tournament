package org.example.footballtournament.service;

import org.example.footballtournament.domain.*;
import org.example.footballtournament.dto.TeamRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class MatchScheduleService {


    public Gameplan generateMatchSchedule(List<TeamRequest> teams) {

        var startDate = LocalDateTime.parse("2020-10-17T17:00");

        Gameplan.StagePlan firstStage = generateStagePlan(teams, startDate);

        var matchTime = firstStage.matches().getLast()
                .matchTime()
                .plusWeeks(3);

        var secondStage = swapSchedule(firstStage, matchTime);

        return new Gameplan(firstStage, secondStage);
    }

    public Gameplan.StagePlan generateStagePlan(List<TeamRequest> teams, LocalDateTime startDate) {
        AtomicReference<LocalDateTime> gameStartDate = new AtomicReference<>(LocalDateTime.parse("2020-10-17T17:00"));

        Set<OrderedMatch> spieltagSet = new LinkedHashSet<>();
        AtomicInteger index = new AtomicInteger(0);
        for (TeamRequest team : teams) {
            int currentIndex = index.incrementAndGet();

            var restOfTeam = teams.subList(currentIndex, teams.size());


            restOfTeam.forEach(team1 -> {
                var match = new OrderedMatch(team.name(), team1.name(), gameStartDate.get());
                spieltagSet.add(match);

                // increase game start date by one week every game
                gameStartDate.set(gameStartDate.get().plusWeeks(1));
            });
        }

        return new Gameplan.StagePlan(spieltagSet.stream().toList());
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

    public Gameplan.StagePlan swapSchedule(Gameplan.StagePlan stagePlan, LocalDateTime startDate) {

        LinkedHashSet<OrderedMatch> matchDays = stagePlan.matches()
                .stream()
//                .map(Gameplan.MatchDay::matches)
//                .flatMap(Collection::stream)
                .map(OrderedMatch::swapTeams)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return new Gameplan.StagePlan(setMatchDates(matchDays.stream().toList(), startDate));
    }

    private List<OrderedMatch> setMatchDates(List<OrderedMatch> list, LocalDateTime startDate) {
        AtomicReference<LocalDateTime> matchTime = new AtomicReference<>(startDate);

        return list.stream()
                .map(match -> {
                    matchTime.set(matchTime.get().plusWeeks(1));

                    return new OrderedMatch(match.homeTeam(), match.awayTeam(), matchTime.get());
                })
                .collect(Collectors.toList());
    }


    private static @NonNull OrderedMatch createOrderedMatch(Match match, LocalDateTime matchTime) {
        return new OrderedMatch(match.homeTeam(), match.awayTeam(), matchTime);
    }

    private static TeamRequest generateRandomTeam(List<TeamRequest> teams, Random random) {
        var randomIndex = random.nextInt(teams.size());

        return teams.get(randomIndex);
    }
}
