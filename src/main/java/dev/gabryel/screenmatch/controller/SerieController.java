package dev.gabryel.screenmatch.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SerieController {
    @GetMapping("/series")
    public String obterSeries(){
        return "<div style= background-color:red;>Aqui vão ser listadas as séries.</div>";
    }

}
