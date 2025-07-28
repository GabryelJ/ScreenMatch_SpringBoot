package dev.gabryel.screenmatch.main;

import dev.gabryel.screenmatch.model.season.SeasonData;
import dev.gabryel.screenmatch.model.serie.Serie;
import dev.gabryel.screenmatch.model.serie.SerieData;
import dev.gabryel.screenmatch.service.APIConsumption;
import dev.gabryel.screenmatch.service.GeminiService;
import dev.gabryel.screenmatch.service.dataparser.DataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Main {

    private final String ADDRESS = "http://www.omdbapi.com/?t=";
    private final String API_KEY;

    private final Scanner input = new Scanner(System.in);
    private final APIConsumption apiConsumption;
    private final DataParser dataParser;
    private final GeminiService geminiService;

    private List<SerieData> seriesDatas = new ArrayList<>();

    @Autowired
    public Main(@Value("${omdb.api.key}") String apiKey, GeminiService geminiService, APIConsumption apiConsumption, DataParser dataParser) {
        this.API_KEY = apiKey;
        this.geminiService = geminiService;
        this.apiConsumption = apiConsumption;
        this.dataParser = dataParser;
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
                .map(serie -> {
                    serie.setPlot(geminiService.getTranslation(serie.getPlot()));
                    return serie;
                })
                .collect(Collectors.toList());

        System.out.println("\nSeries buscadas: ");
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }
}
