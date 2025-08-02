package dev.gabryel.screenmatch.repository;

import dev.gabryel.screenmatch.model.Category;
import dev.gabryel.screenmatch.model.serie.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie>  findByTitleContainingIgnoreCase(String serieName);

    List<Serie> findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String actorName, Double rating);

    List<Serie> findTop5ByOrderByRatingDesc();

    List<Serie> findByGenre(Category category);

    List<Serie> findByTotalSeasonsLessThanEqualAndRatingGreaterThanEqual(int totalSeasons, double rating);

    @Query(
            "SELECT s FROM Serie s\n" +
            "\tWHERE s.totalSeasons <= :totalSeasons AND s.rating >= :rating"
    )
    List<Serie> serieBySeasonAndRating(int totalSeasons, double rating);
}
