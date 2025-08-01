package dev.gabryel.screenmatch.repository;

import dev.gabryel.screenmatch.model.serie.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie>  findByTitleContainingIgnoreCase(String serieName);
}
