package dev.gabryel.screenmatch.dto;

import dev.gabryel.screenmatch.model.serie.Serie;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

public record EpisodeDTO(
        Integer temporada,
        Integer numeroEpisodio,
        String titulo
) {

}
