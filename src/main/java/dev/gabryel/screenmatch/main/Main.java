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


        List<Episode> episodes = seasons.stream()
                .flatMap(season -> season.episodes().stream()
                        .map(episodeData -> new Episode(season.number(), episodeData))
                )
                .collect(Collectors.toList());

        episodes.forEach(System.out::println);


        System.out.println("Top 10 episodios com temporada: ");
        episodes.stream()
                .sorted(Comparator.comparing((Episode::getRating)).reversed())
                .limit(10)
                .map(episode -> episode.getTitle().toUpperCase())
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


        System.out.println("Para descobrir a temporada de um episódio que você não se lembra do nome completo, digite um trecho do titulo do episódio: ");

        input.nextLine();
        var titlePart = input.nextLine();

        Optional<Episode> foundEpisode = episodes.stream()
                    .filter(episode -> episode.getTitle().toUpperCase().contains(titlePart.toUpperCase()))
                    .findFirst();

        if (foundEpisode.isPresent()){
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + foundEpisode.get().getSeason());
        }else{
            System.out.println("Episódio não encontrado!");
        }

        Map<Integer, Double> ratingPerSeason = episodes.stream()
                .filter(episode -> episode.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason, Collectors.averagingDouble(Episode::getRating)));

        System.out.println(ratingPerSeason);

        DoubleSummaryStatistics doubleSummaryStatistics = episodes.stream()
                .filter(episode -> episode.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRating));

        System.out.println("Média: " + doubleSummaryStatistics.getAverage());
        System.out.println("Melhor episódio: " + doubleSummaryStatistics.getMax());
        System.out.println("Pior episódio: " + doubleSummaryStatistics.getMin());
        System.out.println("Quantidade de episódios:  " + doubleSummaryStatistics.getCount());
    }
}
