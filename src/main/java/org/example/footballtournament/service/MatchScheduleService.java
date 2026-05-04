package org.example.footballtournament.service;

import org.example.footballtournament.config.AppConfigProperties;
import org.example.footballtournament.domain.*;
import org.example.footballtournament.dto.TeamRequest;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class MatchScheduleService {

    private final static Logger log = getLogger(MatchScheduleService.class);

    private AppConfigProperties appConfigProperties;

    public MatchScheduleService(AppConfigProperties appConfigProperties) {
        this.appConfigProperties = appConfigProperties;
    }

    public Gameplan generateMatchSchedule(List<TeamRequest> teams) {
        // start date requited by business
        var startDate = LocalDateTime.parse(appConfigProperties.tournament().startDate());

        StagePlan firstStage = generateStagePlan(teams, startDate);

        var matchTime = firstStage.matches().getLast()
                .matchTime()
                .plusWeeks(3);

        var secondStage = swapSchedule(firstStage, matchTime);

        Gameplan gameplan = new Gameplan(firstStage, secondStage);

        log.info("Generated gameplan - first stage: " );
        for (var match : gameplan.firstStage().matches()) {
            System.out.println(match.toString());
        }

        log.info("Generated gameplan - second stage: ");
        for (var match : gameplan.secondStage().matches()) {
            System.out.println(match.toString());
        }
        return gameplan;
    }

    public StagePlan generateStagePlan(List<TeamRequest> teams, LocalDateTime startDate) {
        AtomicReference<LocalDateTime> gameStartDate = new AtomicReference<>(startDate);

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

        return new StagePlan(spieltagSet.stream().toList());
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

            var match = new UnorderedMatch(teamName.name(), teamName2.name());

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

    public StagePlan swapSchedule(StagePlan stagePlan, LocalDateTime startDate) {

        LinkedHashSet<OrderedMatch> matchDays = stagePlan.matches()
                .stream()
//                .map(Gameplan.MatchDay::matches)
//                .flatMap(Collection::stream)
                .map(OrderedMatch::swapTeams)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return new StagePlan(setMatchDates(matchDays.stream().toList(), startDate));
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
