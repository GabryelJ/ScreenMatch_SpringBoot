package dev.gabryel.screenmatch.controller;

import dev.gabryel.screenmatch.dto.SerieDTO;
import dev.gabryel.screenmatch.model.serie.Serie;
import dev.gabryel.screenmatch.repository.SerieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {
    private SerieRepository serieRepository;

    public SerieController(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    @GetMapping("/series")
    public List<SerieDTO> obterSeries(){
        return serieRepository.findAll()
                .stream()
                .map(serie -> new SerieDTO(serie.getId(), serie.getTitle(),
                        serie.getTotalSeasons(), serie.getRating(),
                        serie.getGenre(), serie.getPoster(),
                        serie.getActors(), serie.getPlot()))
                .collect(Collectors.toList());
    }

}
