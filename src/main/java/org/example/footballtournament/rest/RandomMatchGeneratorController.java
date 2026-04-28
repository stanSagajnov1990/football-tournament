package org.example.footballtournament.rest;

import jakarta.validation.Valid;
import org.example.footballtournament.domain.Gameplan;
import org.example.footballtournament.domain.StagePlan;
import org.example.footballtournament.dto.LeagueRequest;
import org.example.footballtournament.service.MatchScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RandomMatchGeneratorController {

    private static final Logger logger = LoggerFactory.getLogger(RandomMatchGeneratorController.class);

    private MatchScheduleService matchScheduleService;

    public RandomMatchGeneratorController(MatchScheduleService matchScheduleService) {
        this.matchScheduleService = matchScheduleService;
    }

    @PostMapping("/generate-random-matches")
    public ResponseEntity<Gameplan> generateRandomMatches(@Valid @RequestBody LeagueRequest leagueRequest) {
        logger.info("Received request to generate random matches for league: {}", leagueRequest.league());

        var firstStage = matchScheduleService.generateMatchSchedule(leagueRequest.teams());

        var matchTime = firstStage.matchDays().getLast()
                .matches().getLast().matchTime()
                .plusWeeks(3);
        var secondStage = matchScheduleService.swapSchedule(firstStage, matchTime);

        return ResponseEntity.ok(new Gameplan(firstStage, secondStage));
    }

}
