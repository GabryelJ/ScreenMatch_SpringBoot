package dev.gabryel.screenmatch.dto;

import dev.gabryel.screenmatch.model.Category;

public record SerieDTO(
        Long id,
        String title,
        Integer totalSeasons,
        Double rating,
        Category genre,
        String poster,
        String actors,
        String plot) {
}
