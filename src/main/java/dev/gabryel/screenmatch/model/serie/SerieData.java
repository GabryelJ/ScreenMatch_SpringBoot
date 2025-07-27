package dev.gabryel.screenmatch.model.serie;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SerieData(
        @JsonAlias("Title") String title,
        @JsonAlias("totalSeasons") Integer totalSeasons,
        @JsonAlias("imdbRating") String rating,
        @JsonAlias("Genre") String genre,
        @JsonAlias("Poster") String poster,
        @JsonAlias("Actors") String actors,
        @JsonAlias("Plot") String plot
        ) {
}
