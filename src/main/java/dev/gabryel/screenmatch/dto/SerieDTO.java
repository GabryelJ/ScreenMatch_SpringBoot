package dev.gabryel.screenmatch.dto;

import dev.gabryel.screenmatch.model.Category;

public record SerieDTO(
        Long id,
        String titulo,
        Integer temporadas,
        Double avaliacao,
        Category genero,
        String poster,
        String atores,
        String sinopse) {
}
