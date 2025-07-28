package dev.gabryel.screenmatch.model.serie;

import dev.gabryel.screenmatch.model.Category;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTotalSeasons() {
        return totalSeasons;
    }

    public void setTotalSeasons(Integer totalSeasons) {
        this.totalSeasons = totalSeasons;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Category getGenre() {
        return genre;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Override
    public String toString() {
        return "Genero=" + genre +
                ", Titulo='" + title + '\'' +
                ", Temporadas=" + totalSeasons +
                ", Classificação=" + rating +
                ", Poster='" + poster + '\'' +
                ", Atores='" + actors + '\'' +
                ", Sinopse='" + plot + '\'';
    }
}
