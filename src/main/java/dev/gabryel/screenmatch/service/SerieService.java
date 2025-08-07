package dev.gabryel.screenmatch.service;

import dev.gabryel.screenmatch.dto.SerieDTO;
import dev.gabryel.screenmatch.repository.SerieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {
    private SerieRepository serieRepository;

    public SerieService(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public List<SerieDTO> getAllSeries(){
        return serieRepository.findAll()
                .stream()
                .map(serie -> new SerieDTO(serie.getId(), serie.getTitle(),
                        serie.getTotalSeasons(), serie.getRating(),
                        serie.getGenre(), serie.getPoster(),
                        serie.getActors(), serie.getPlot()))
                .collect(Collectors.toList());
    }
}
