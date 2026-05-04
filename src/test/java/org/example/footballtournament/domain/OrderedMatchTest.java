package org.example.footballtournament.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderedMatchTest {

    @Test
    void swapTeams() {
        // Arrange
        OrderedMatch match = new OrderedMatch("Team A", "Team B", null);

        // Act
        var result = match.swapTeams();

        // Assert
        assertEquals("Team B", result.homeTeam());
        assertEquals("Team A", result.awayTeam());
    }

}
