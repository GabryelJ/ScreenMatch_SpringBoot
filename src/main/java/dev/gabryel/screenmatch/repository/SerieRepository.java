package dev.gabryel.screenmatch.repository;

import dev.gabryel.screenmatch.model.Category;
import dev.gabryel.screenmatch.model.episode.Episode;
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

    @Query("SELECT ep FROM Serie s\n" +
            "\tJOIN s.episodes ep" +
            "WHERE ep.title ILIKE %:namePart% ")
    List<Episode> episodeNameContaining(String namePart);

    @Query("SELECT ep FROM Serie s\n +" +
            "\tJOIN s.episodes ep" +
            "WHERE s = :serie ORDER BY ep.rating DESC LIMIT 5")
    List<Episode> topEpisodesBySerie(Serie serie);


    @Query("SELECT ep FROM Serie s" +
            "\tJOIN s.episodes ep" +
            "WHERE s = :serie AND YEAR(ep.releaseYear) >= :releaseYear")
    List<Episode> episodesBySerieAndYear(Serie searchSerie, int releaseYear);
}
