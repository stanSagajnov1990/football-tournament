package org.example.footballtournament.domain;

public sealed interface Match permits OrderedMatch, UnorderedMatch {

    String homeTeam();
    String awayTeam();
}
