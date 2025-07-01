package dev.gabryel.screenmatch.main;

import dev.gabryel.screenmatch.model.Episode;
import dev.gabryel.screenmatch.model.EpisodeData;
import dev.gabryel.screenmatch.model.SeasonData;
import dev.gabryel.screenmatch.model.SerieData;
import dev.gabryel.screenmatch.service.APIConsumption;
import dev.gabryel.screenmatch.service.DataParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private final String ADDRESS = "http://www.omdbapi.com/?t=";
    private final String API_KEY;

    private Scanner input = new Scanner(System.in);
    private APIConsumption apiConsumption = new APIConsumption();
    private DataParser dataParser = new DataParser();

    public Main(String apiKey) {
        this.API_KEY = apiKey;
    }

    public void showMenu(){
        System.out.println("Digite o nome da serie: ");
        var serieName = input.nextLine();
        var json = apiConsumption.fetchData(ADDRESS + serieName.replace(' ', '+')+ "&apikey=" + API_KEY);
        SerieData data = dataParser.fetchData(json, SerieData.class);
        System.out.println(data);

        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i < data.totalSeasons(); i++){
            json = apiConsumption.fetchData("http://www.omdbapi.com/?t=" + serieName.replace(" ", "+") + '&' + "season=" + i + '&' + "apikey=" + API_KEY);
            SeasonData seasonData = dataParser.fetchData(json, SeasonData.class);
            seasons.add(seasonData);
        }

        seasons.forEach(System.out::println);

        seasons.forEach(season -> season.episodes()
                .forEach(episode -> System.out.println(episode.title())));


        List<EpisodeData> episodesData = seasons.stream()
                .flatMap(season -> season.episodes().stream())
                .collect(Collectors.toList());


        System.out.println("Top 5 episódios: ");
        episodesData.stream()
                .filter(episodeData -> !Objects.equals(episodeData.rating(), "N/A"))
                .sorted(Comparator.comparing(EpisodeData::rating).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(season -> season.episodes().stream()
                        .map(episodeData -> new Episode(season.number(), episodeData))
                )
                .collect(Collectors.toList());

        episodes.forEach(System.out::println);


        System.out.println("Top 5 episodios com temporada: ");
        episodes.stream()
                .sorted(Comparator.comparing((Episode::getRating)).reversed())
                .limit(5)
                .forEach(System.out::println);

        System.out.println("A partir de que ano você deseja ver os episodios?");
        var year = input.nextInt();

        LocalDate searchDate = LocalDate.of(year, 1, 1);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodes.stream()
                .filter(episode -> episode.getReleaseDate() != null && episode.getReleaseDate().isAfter(searchDate))
                .forEach(episode -> System.out.println(
                        "Temporada: " + episode.getSeason() +
                                " Episodio: " + episode.getTitle() +
                                " Data lancamento: " + episode.getReleaseDate().format(dateTimeFormatter)
                ));
    }
}
