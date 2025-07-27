package dev.gabryel.screenmatch.model.season;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.gabryel.screenmatch.model.episode.EpisodeData;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonData(
        @JsonAlias("Season") Integer number,
        @JsonAlias("Episodes")List<EpisodeData> episodes
) {
}
