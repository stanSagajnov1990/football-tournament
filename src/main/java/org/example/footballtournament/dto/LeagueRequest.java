package org.example.footballtournament.dto;

public record LeagueRequest(String league, String country, TeamRequest[] teams) {


}
