package dev.gabryel.screenmatch.model.episode;

import dev.gabryel.screenmatch.model.serie.Serie;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name="episodios")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer season;
    private String title;
    private Integer number;
    private Double rating;
    private LocalDate releaseDate;

    @ManyToOne
    private Serie serie;

    public Episode() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
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
