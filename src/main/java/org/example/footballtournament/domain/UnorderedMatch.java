package org.example.footballtournament.domain;

public record UnorderedMatch(String homeTeam, String awayTeam) implements Match {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnorderedMatch(String homeTeam1, String awayTeam1)) {
            return this.homeTeam.equals(homeTeam1)
                    && this.awayTeam.equals(awayTeam1)
                    || (this.awayTeam.equals(homeTeam1)
                    && this.homeTeam.equals(awayTeam1));
        }

        return false;
    }

    @Override
    public int hashCode() {
        return homeTeam.hashCode() + awayTeam.hashCode();
    }
}
