package dev.gabryel.screenmatch.service;

import dev.gabryel.screenmatch.dto.EpisodeDTO;
import dev.gabryel.screenmatch.dto.SerieDTO;
import dev.gabryel.screenmatch.model.Category;
import dev.gabryel.screenmatch.model.episode.Episode;
import dev.gabryel.screenmatch.model.serie.Serie;
import dev.gabryel.screenmatch.repository.SerieRepository;
import jakarta.persistence.Id;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
    private SerieRepository serieRepository;

    public SerieService(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public List<SerieDTO> getAllSeries(){
        return parseSerieToDTOSerie(serieRepository.findAll());
    }

    public List<SerieDTO> getTop5Series() {
        return parseSerieToDTOSerie(serieRepository.findTop5ByOrderByRatingDesc());
    }

    public List<SerieDTO> getReleases() {
    return parseSerieToDTOSerie(serieRepository.recentReleases());
    }

    private List<SerieDTO> parseSerieToDTOSerie(List<Serie> series){
        return series.stream()
                .map(serie -> new SerieDTO(serie.getId(), serie.getTitle(),
                serie.getTotalSeasons(), serie.getRating(),
                serie.getGenre(), serie.getPoster(),
                serie.getActors(), serie.getPlot()))
                .collect(Collectors.toList());
    }

    public SerieDTO getSerie(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);
        if (serie.isPresent()){
            Serie seriePresent = serie.get();
            return new SerieDTO(seriePresent.getId(), seriePresent.getTitle(),
                    seriePresent.getTotalSeasons(), seriePresent.getRating(),
                    seriePresent.getGenre(), seriePresent.getPoster(),
                    seriePresent.getActors(), seriePresent.getPlot());
        }
        return null;
    }

    public List<EpisodeDTO> getAllSeasons(Long id) {
        Optional<Serie> serie = serieRepository.findById(id);
        if (serie.isPresent()){
            Serie seriePresent = serie.get();
            return seriePresent.getEpisodes().stream()
                    .map(episode -> new EpisodeDTO(episode.getSeason(),
                    episode.getNumber(), episode.getTitle()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodeDTO> getSeasonByNumber(Long id, Long numeroTemporada) {
        return serieRepository.findEpisodeBySeason(id, numeroTemporada).stream()
                .map(episode -> new EpisodeDTO(episode.getSeason(),
                        episode.getNumber(), episode.getTitle()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> getSerieByGenre(String genero) {
        Category category = Category.fromPortuguese(genero);
        var series = serieRepository.findByGenre(category);
        return parseSerieToDTOSerie(series);
    }
}

