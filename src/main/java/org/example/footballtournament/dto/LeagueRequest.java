package org.example.footballtournament.dto;

import java.util.List;

public record LeagueRequest(String league, String country, List<TeamRequest> teams) {


}
