package org.example.footballtournament.domain;

public sealed interface Match permits OrderedMatch, UnOrderedMatch {

    String homeTeam();
    String awayTeam();
}
