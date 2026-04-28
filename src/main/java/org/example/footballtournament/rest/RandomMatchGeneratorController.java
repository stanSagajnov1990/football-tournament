package org.example.footballtournament.rest;

import jakarta.validation.Valid;
import org.example.footballtournament.domain.Gameplan;
import org.example.footballtournament.dto.LeagueRequest;
import org.example.footballtournament.service.MatchScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

@RestController
public class RandomMatchGeneratorController {

    Logger logger = LoggerFactory.getLogger(RandomMatchGeneratorController.class);

    private ObjectMapper objectMapper;

    private MatchScheduleService matchScheduleService;

    public RandomMatchGeneratorController(ObjectMapper objectMapper, MatchScheduleService matchScheduleService) {
        this.objectMapper = objectMapper;
        this.matchScheduleService = matchScheduleService;
    }

    @PostMapping("/generate-random-matches")
    public ResponseEntity<Gameplan> generateRandomMatches(@Valid @RequestBody LeagueRequest leagueRequest) {
        logger.info("Received request to generate random matches for league: {}", leagueRequest.league());

        var gameplan = matchScheduleService.generateMatchSchedule(leagueRequest.teams());


        return ResponseEntity.ok(gameplan);
    }

}
