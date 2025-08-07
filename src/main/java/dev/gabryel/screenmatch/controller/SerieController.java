package dev.gabryel.screenmatch.controller;

import dev.gabryel.screenmatch.dto.SerieDTO;
import dev.gabryel.screenmatch.service.SerieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SerieController {
    private SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }

    @GetMapping("/series")
    public List<SerieDTO> obterSeries(){
        return serieService.getAllSeries();
    }

}
