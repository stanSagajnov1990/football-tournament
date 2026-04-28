package org.example.footballtournament.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record TeamRequest(

        @NotNull
        @JsonProperty("name")
        String name,

        @Nullable
        @JsonProperty("founding_date")
        String foundingDate


) {
}
