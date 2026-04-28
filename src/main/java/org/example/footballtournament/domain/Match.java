package org.example.footballtournament.domain;

public record Match (

    String homeTeam,
    String awayTeam
    ) {


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Match match) {
            return this.homeTeam.equals(match.homeTeam)
                    || this.homeTeam.equals(match.awayTeam)
                    || this.awayTeam.equals(match.awayTeam)
                    || this.awayTeam.equals(match.homeTeam);
        }

        return false;
    }
}
