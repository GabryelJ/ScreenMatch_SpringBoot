package dev.gabryel.screenmatch.service;

import org.springframework.stereotype.Service;

@Service
public class PlotTranslator {

    private final GeminiService geminiService;

    PlotTranslator(GeminiService geminiService){
        this.geminiService = geminiService;
    }

    public String translate(String plot){
        return geminiService.getTranslation(plot);
    }
}
