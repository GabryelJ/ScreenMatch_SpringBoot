package dev.gabryel.screenmatch.main;

import dev.gabryel.screenmatch.model.season.SeasonData;
import dev.gabryel.screenmatch.model.serie.Serie;
import dev.gabryel.screenmatch.model.serie.SerieData;
import dev.gabryel.screenmatch.service.APIConsumption;
import dev.gabryel.screenmatch.service.dataparser.DataParser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    private final String ADDRESS = "http://www.omdbapi.com/?t=";
    private final String API_KEY;

    private Scanner input = new Scanner(System.in);
    private APIConsumption apiConsumption = new APIConsumption();
    private DataParser dataParser = new DataParser();
    private List<SerieData> seriesDatas = new ArrayList<>();

    public Main(String apiKey) {
        this.API_KEY = apiKey;
    }

    public void showMenu() {
        var option = -1;
        var menu = """
                \n
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar series buscada
                0 - Sair                                 
                """;
        while(option != 0){
            System.out.println(menu);
            option = input.nextInt();
            input.nextLine();

            switch (option) {
                case 1:
                    searchWebSerie();
                    break;
                case 2:
                    searchEpisodeForSerie();
                    break;
                case 3:
                    showSearchedSeries();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void searchWebSerie() {
        SerieData data = getSerieData();
        seriesDatas.add(data);
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

    private void showSearchedSeries(){
        List<Serie> series = new ArrayList<>();
        series = seriesDatas.stream()
                .map(serieData -> new Serie(serieData))
                        .collect(Collectors.toList());

        System.out.println("\nSeries buscadas: ");
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }
}
