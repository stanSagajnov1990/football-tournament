package org.example.footballtournament.domain;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UnorderedMatchTest {

    @Test
    void testEquals() {
        var match1 = new UnorderedMatch("Team A", "Team B");
        var match2 = new UnorderedMatch("Team A", "Team B");

        assertEquals(match1, match2);
    }

    @Test
    void testEquals_swappedTeams() {
        var match1 = new UnorderedMatch("Team A", "Team B");
        var match2 = new UnorderedMatch("Team B", "Team A");

        assertEquals(match1, match2);
    }

    @Test
    void testEquals_differentTeams() {
        var match1 = new UnorderedMatch("Team A", "Team B");
        var match2 = new UnorderedMatch("Team C", "Team A");

        assertNotEquals(match1, match2);
    }

    @Test
    void testHashcode() {
        HashSet<UnorderedMatch> matches = new HashSet<>();

        matches.add(new UnorderedMatch("Team A", "Team B"));

        assertTrue(matches.contains(new UnorderedMatch("Team B", "Team A")));
    }

    @Test
    void testHashcode_differentTeams() {
        HashSet<UnorderedMatch> matches = new HashSet<>();

        matches.add(new UnorderedMatch("Team A", "Team B"));

        assertFalse(matches.contains(new UnorderedMatch("Team C", "Team A")));
    }
}
