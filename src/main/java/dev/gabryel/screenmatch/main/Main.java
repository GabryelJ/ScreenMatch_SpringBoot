package dev.gabryel.screenmatch.main;

import dev.gabryel.screenmatch.model.episode.Episode;
import dev.gabryel.screenmatch.model.season.SeasonData;
import dev.gabryel.screenmatch.model.serie.Serie;
import dev.gabryel.screenmatch.model.serie.SerieData;
import dev.gabryel.screenmatch.repository.SerieRepository;
import dev.gabryel.screenmatch.service.APIConsumption;
import dev.gabryel.screenmatch.service.PlotTranslator;
import dev.gabryel.screenmatch.service.dataparser.DataParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Main {
    private final String ADDRESS = "http://www.omdbapi.com/?t=";
    private final String API_KEY;

    private final Scanner input = new Scanner(System.in);
    private final APIConsumption apiConsumption;
    private final DataParser dataParser;
    private final PlotTranslator plotTranslator;
    private final SerieRepository serieRepository;

    private List<Serie> series = new ArrayList<>();

    public Main(@Value("${omdb.api.key}") String apiKey, PlotTranslator plotTranslator, APIConsumption apiConsumption, DataParser dataParser, SerieRepository serieRepository) {
        this.API_KEY = apiKey;
        this.plotTranslator = plotTranslator;
        this.apiConsumption = apiConsumption;
        this.dataParser = dataParser;
        this.serieRepository = serieRepository;
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
        Serie serie = new Serie(data);
        serie.setPlot(plotTranslator.translate(serie.getPlot()));
        serieRepository.save(serie);
        System.out.println(serie);
    }

    private SerieData getSerieData() {
        System.out.println("Digite o nome da série para busca");
        var serieName = input.nextLine();
        var json = apiConsumption.fetchData(ADDRESS + serieName.replace(" ", "+") + '&' + "apikey=" + API_KEY);
        SerieData data = dataParser.getData(json, SerieData.class);
        return data;
    }

    private void searchEpisodeForSerie(){
//        SerieData serieData = getSerieData();
        showSearchedSeries();
        System.out.println("Escolha uma série pelo nome: ");
        var serieName = input.nextLine();

        Optional<Serie> firstSerie = series.stream()
                .filter(serie -> serie.getTitle().toLowerCase().contains(serieName.toLowerCase()))
                .findFirst();

        if (firstSerie.isPresent()){
            var foundSerie = firstSerie.get();
            List<SeasonData> seasons = new ArrayList<>();

            for (int i = 1; i <= foundSerie.getTotalSeasons(); i++) {
                var json = apiConsumption.fetchData(ADDRESS + foundSerie.getTitle().replace(" ", "+") + "&season=" + i + '&' + "apikey=" + API_KEY);
                SeasonData dadosTemporada = dataParser.getData(json, SeasonData.class);
                seasons.add(dadosTemporada);
            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                            .flatMap(seasonData -> seasonData.episodes().stream()
                                    .map(episodeData -> new Episode(episodeData.number(), episodeData)))
                            .collect(Collectors.toList());

            foundSerie.setEpisodes(episodes);
            serieRepository.save(foundSerie);
        }else {
            System.out.println("Série não encontrada: ");
        }
    }

    private void showSearchedSeries(){
        System.out.println("\nSeries buscadas: ");
        series = serieRepository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenre))
                .forEach(System.out::println);
    }
}
