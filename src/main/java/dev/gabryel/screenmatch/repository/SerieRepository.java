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
            "SELECT s FROM Serie s " +
            "WHERE s.totalSeasons <= :totalSeasons AND s.rating >= :rating "
    )
    List<Serie> serieBySeasonAndRating(int totalSeasons, double rating);

    @Query("SELECT ep FROM Serie s " +
            "JOIN s.episodes ep " +
            "WHERE ep.title ILIKE %:namePart% ")
    List<Episode> episodeNameContaining(String namePart);

    @Query("SELECT ep FROM Serie s " +
            "JOIN s.episodes ep " +
            "WHERE s = :serie ORDER BY ep.rating DESC LIMIT 5 ")
    List<Episode> topEpisodesBySerie(Serie serie);


    @Query("SELECT ep FROM Serie s " +
            "JOIN s.episodes ep " +
            "WHERE s = :serie AND YEAR(ep.releaseDate) >= :releaseDate ")
    List<Episode> episodesBySerieAndYear(Serie searchSerie, int releaseYear);

    @Query("SELECT s FROM Serie s " +
            "JOIN s.episodes e " +
            "GROUP BY s " +
            "ORDER BY MAX(e.releaseDate) DESC LIMIT 5 ")
    List<Serie> recentReleases();

    @Query("SELECT e FROM Serie s JOIN s.episodes e WHERE s.id = :id AND e.season = :number")
    List<Episode> findEpisodeBySeason(Long id, Long number);
}
