package org.example.footballtournament.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppConfigProperties (Tournament tournament)
{

    public record Tournament(String startDate) {}
}
