package org.example.footballtournament.service;

import org.example.footballtournament.domain.Gameplan;
import org.example.footballtournament.domain.Match;
import org.example.footballtournament.dto.TeamRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class MatchScheduleService {


    public Gameplan generateMatchSchedule(List<TeamRequest> teams) {

        Set<String> matchSet = new HashSet<>();

        Set<Match> spieltagSet = new HashSet<>();

        var random = new Random();


        while (spieltagSet.size() < 2) {

            var teamName = generateRandomTeam(teams, random);
            var teamName2 = generateRandomTeam(teams, random);

            if (teamName.equals(teamName2)) {
                continue;
            }

            var match = new Match(teamName, teamName2);

            if (spieltagSet.contains(match)) {
                continue;
            }

            matchSet.add(teamName);

            System.out.println(teamName);

        }

        teams.forEach(team -> {
        });

        return new Gameplan(null);
    }

    public Set<Match> generateMatchDay(List<TeamRequest> teams) {

        Set<String> teamSet = new HashSet<>();
        Set<Match> matchSet = new HashSet<>();
        var random = new Random();

        while (matchSet.size() <= 2) {

            var teamName = generateRandomTeam(teams, random);
            var teamName2 = generateRandomTeam(teams, random);

            if (teamName.equals(teamName2) || (teamSet.contains(teamName) || teamSet.contains(teamName2))) {
                continue;
            }

            var match = new Match(teamName, teamName2);

            if (matchSet.contains(match)) {
                continue;
            }

            matchSet.add(match);
            teamSet.add(teamName);
            teamSet.add(teamName2);

            System.out.println(teamName);

        }

        return matchSet;
    }

    private static String generateRandomTeam(List<TeamRequest> teams, Random random) {
        var randomIndex = random.nextInt(6);

        var teamName = teams.get(randomIndex).name();
        return teamName;
    }
}
