package dev.gabryel.screenmatch.model;

import java.util.OptionalDouble;

public class Serie {
    private String title;
    private Integer totalSeasons;
    private Double rating;
    private Category genre;
    private String poster;
    private String actors;
    private String plot;

    public Serie(SerieData serieData){
        this.title = serieData.title();
        this.totalSeasons = serieData.totalSeasons();
        this.rating = OptionalDouble.of(Double.valueOf(serieData.rating())).orElse(0);
        this.genre = Category.fromString(serieData.genre().split(",")[0].trim());
        this.poster = serieData.poster();
        this.actors = serieData.actors();
        this.plot = serieData.plot();
    }
}
