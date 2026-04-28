package org.example.footballtournament.domain;

public record UnOrderedMatch (String homeTeam, String awayTeam) implements Match {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnOrderedMatch(String homeTeam, String awayTeam1)) {
            return this.homeTeam.equals(homeTeam)
                    || this.homeTeam.equals(awayTeam1)
                    || this.awayTeam.equals(awayTeam1)
                    || this.awayTeam.equals(homeTeam);
        }

        return false;
    }
}
