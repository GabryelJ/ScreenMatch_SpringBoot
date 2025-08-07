package dev.gabryel.screenmatch.controller;

import dev.gabryel.screenmatch.dto.SerieDTO;
import dev.gabryel.screenmatch.service.SerieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    private SerieService serieService;

    public SerieController(SerieService serieService) {
        this.serieService = serieService;
    }


    @GetMapping("")
    public List<SerieDTO> obterSeries(){
        return serieService.getAllSeries();
    }
    @GetMapping("/top5")
    public List<SerieDTO> obterTop5Series(){
        return serieService.getTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos(){
        return serieService.getReleases();
    }

    @GetMapping("/{id}")
    public SerieDTO obterPorId(@PathVariable Long id){
        return serieService.getSerie(id);
    }
}
