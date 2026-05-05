package org.example.footballtournament.service;

import org.example.footballtournament.domain.*;
import org.example.footballtournament.dto.TeamRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MatchScheduleService {

    public Gameplan generateGameplan(List<TeamRequest> teams) {
        var firstStage = generateMatchSchedule(teams);

        var secondStageStart = firstStage.matchDays().getLast().matches().getLast().matchTime().plusWeeks(3);

        var secondStage = swapSchedule(firstStage, secondStageStart);

        return new Gameplan(firstStage, secondStage);
    }

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
        var random = new Random();

        while (true) {
            var remaining = new ArrayList<>(teams);
            Collections.shuffle(remaining, random);

            Set<Match> matchSet = new LinkedHashSet<>();
            boolean stuck = false;

            while (!remaining.isEmpty()) {
                var team1 = remaining.remove(0);

                TeamRequest opponent = null;
                for (var candidate : remaining) {
                    var match = new UnorderedMatch(team1.name(), candidate.name());
                    if (!spieltagSet.contains(match) && !matchSet.contains(match)) {
                        opponent = candidate;
                        break;
                    }
                }

                if (opponent == null) {
                    stuck = true;
                    break;
                }

                matchSet.add(new UnorderedMatch(team1.name(), opponent.name()));
                remaining.remove(opponent);
            }

            if (!stuck) {
                return matchSet;
            }
        }
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

}
