package org.example.footballtournament;

import org.example.footballtournament.dto.LeagueRequest;
import org.example.footballtournament.service.MatchScheduleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class MyCliRunner implements CommandLineRunner {

    private final MatchScheduleService matchScheduleService;

    private ResourceLoader resourceLoader;

    private ObjectMapper objectMapper;

    public MyCliRunner(MatchScheduleService matchScheduleService, ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.matchScheduleService = matchScheduleService;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        // runs on startup after context is loaded

        Resource resource = resourceLoader.getResource("classpath:teams.json");
        LeagueRequest league = objectMapper.readValue(
                resource.getInputStream(),
                LeagueRequest.class
        );

        matchScheduleService.generateMatchSchedule(league.teams());
    }
}