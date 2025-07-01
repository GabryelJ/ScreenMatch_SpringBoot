package dev.gabryel.screenmatch.model;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episode {
    private Integer season;
    private String title;
    private Integer number;
    private Double rating;
    private LocalDate releaseDate;

    public Episode(Integer seasonNumber, EpisodeData episodeData) {
        this.season = seasonNumber;
        this.title = episodeData.title();
        this.number = episodeData.number();
        try{
            this.rating = Double.valueOf(episodeData.rating());
        }catch (NumberFormatException exception){
            this.rating = 0.0;
        }
        try{
            this.releaseDate = LocalDate.parse(episodeData.releaseDate());
        }catch (DateTimeParseException exception){
            this.releaseDate = null;
        }
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    @Override
    public String toString() {
        return "Temporada: " + season +
                ", Titulo: " + title +
                ", Numero: " + number +
                ", Avaliacao: " + rating +
                ", Data de lancamento: " + releaseDate;
    }
}
