package dev.gabryel.screenmatch.main;

import dev.gabryel.screenmatch.model.EpisodeData;
import dev.gabryel.screenmatch.model.SeasonData;
import dev.gabryel.screenmatch.model.SerieData;
import dev.gabryel.screenmatch.service.APIConsumption;
import dev.gabryel.screenmatch.service.DataParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    }
}
