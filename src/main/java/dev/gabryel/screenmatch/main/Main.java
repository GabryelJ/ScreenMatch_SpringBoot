package dev.gabryel.screenmatch.main;

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

    public void showMenu() {
        var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                
                0 - Sair                                 
                """;

        System.out.println(menu);
        var opcao = input.nextInt();
        input.nextLine();

        switch (opcao) {
            case 1:
                searchWebSerie();
                break;
            case 2:
                searchEpisodeForSerie();
                break;
            case 0:
                System.out.println("Saindo...");
                break;
            default:
                System.out.println("Opção inválida");
        }
    }

    private void searchWebSerie() {
        SerieData data = getSerieData();
        System.out.println(data);
    }

    private SerieData getSerieData() {
        System.out.println("Digite o nome da série para busca");
        var serieName = input.nextLine();
        var json = apiConsumption.fetchData(ADDRESS + serieName.replace(" ", "+") + '&' + "apikey=" + API_KEY);
        SerieData data = dataParser.getData(json, SerieData.class);
        return data;
    }

    private void searchEpisodeForSerie(){
        SerieData serieData = getSerieData();
        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= serieData.totalSeasons(); i++) {
            var json = apiConsumption.fetchData(ADDRESS + serieData.title().replace(" ", "+") + "&season=" + i + '&' + "apikey=" + API_KEY);
            SeasonData dadosTemporada = dataParser.getData(json, SeasonData.class);
            seasons.add(dadosTemporada);
        }
        seasons.forEach(System.out::println);
    }
}
