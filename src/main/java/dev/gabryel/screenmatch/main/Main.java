package dev.gabryel.screenmatch.main;

import dev.gabryel.screenmatch.model.Category;
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

    private Optional<Serie> searchSerie;

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
                1 - Buscar séries;
                2 - Buscar episódios;
                3 - Listar séries buscada;
                4 - Buscar série por titulo;
                5 - Buscar série por ator;
                6 - Buscar top 5 séries;
                7 - Buscar séries por genero;
                8 - Filtrar séries;
                9 - Buscar episódio por trecho de nome;
                10 - Buscar top 5 episódios por série;
                11 - Buscar episódios a partir de uma data;
                
                0 - Sair.
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
                case 4:
                    searchSerieByTitle();
                    break;
                case 5:
                    searchSerieByActor();
                    break;
                case 6:
                    searchTop5Series();
                    break;
                case 7:
                    searchSerieByCategory();
                    break;
                case 8:
                    searchSerieByTotalSeasonsAndRating();
                    break;
                case 9:
                    searchEpisodeByContainingWord();
                    break;
                case 10:
                    searchTopEpisodesBySerie();
                    break;
                case 11:
                    searchEpisodeFromADate();
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
        showSearchedSeries();
        System.out.println("Escolha uma série pelo nome: ");
        var serieName = input.nextLine();

        Optional<Serie> firstSerie = serieRepository.findByTitleContainingIgnoreCase(serieName);

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

    private void searchSerieByTitle() {
        showSearchedSeries();
        System.out.println("Escolha uma série pelo nome: ");
        var serieName = input.nextLine();

        searchSerie = serieRepository.findByTitleContainingIgnoreCase(serieName);
        if (searchSerie.isPresent()){
            System.out.println("Dados da série: " + searchSerie.get());
        }else{
            System.out.println("Série não encontrada");
        }
    }

    private void searchSerieByActor() {
        System.out.println("Qual o nome do ator que deseja buscar?");
        var actorName = input.nextLine();
        System.out.println("A partir de qual nota deseja obter os resultados?");
        Double rating = input.nextDouble();
        List<Serie> foundSeries = serieRepository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(actorName, rating);
        System.out.println("Séries em que " + actorName + " atuou: ");
        foundSeries.forEach(serie -> System.out.println(serie.getTitle() + "Avaliação : " + serie.getRating()));
    }

    private void searchTop5Series() {
        List<Serie> topSeries = serieRepository.findTop5ByOrderByRatingDesc();
        topSeries.forEach(serie -> System.out.println(serie.getTitle() + "Avaliação : " + serie.getRating()));
    }

    private void searchSerieByCategory() {
        System.out.println("Qual o genero de série desejado?");
        var genreName = input.nextLine();
        Category category = Category.fromPortuguese(genreName);
        List<Serie> seriesByGenre = serieRepository.findByGenre(category);
        System.out.println("Séries da categoria" + genreName);
        seriesByGenre
                .forEach(serie -> System.out.println(serie.getTitle() + "Avaliação : " + serie.getRating()));
    }

    private void searchSerieByTotalSeasonsAndRating() {
        System.out.println("Filtrar séries até quantas temporadas? ");
        var totalSeasons = input.nextInt();
        input.nextLine();
        System.out.println("Com avaliação a partir de que valor? ");
        var rating = input.nextDouble();
        input.nextLine();   
        List<Serie> filteredSeries = serieRepository.serieBySeasonAndRating(totalSeasons, rating);
        System.out.println("*** Séries filtradas ***");
        filteredSeries.forEach(serie ->
                System.out.println(serie.getTitle() + "  - avaliação: " + serie.getRating()));
    }

    private void searchEpisodeByContainingWord() {
        System.out.println("Qual o trecho do nome do episódio que deseja buscar?");
        String namePart = input.nextLine();
        List<Episode> foundEpisodes = serieRepository.episodeNameContaining(namePart);
        foundEpisodes.forEach(episode ->
                System.out.printf("Série: %s Temporada: %s - Episódio %s - %s%n",
                episode.getSerie().getTitle(), episode.getSeason(),
                episode.getNumber(), episode.getTitle()));
    }


    private void searchTopEpisodesBySerie() {
        searchSerieByTitle();
        if (searchSerie.isPresent()){
            Serie serie = searchSerie.get();
            List<Episode> topEpisodes = serieRepository.topEpisodesBySerie(serie);
            topEpisodes.forEach(episode ->
                    System.out.printf("Série: %s Temporada: %s - Episódio %s - %s - Avaliação %s %n",
                            episode.getSerie().getTitle(), episode.getSeason(),
                            episode.getNumber(), episode.getTitle(), episode.getRating()));

            searchSerie = Optional.empty();
        }
    }

    private void searchEpisodeFromADate() {
        searchSerieByTitle();
        if (searchSerie.isPresent()) {
            Serie serie = searchSerie.get();
            System.out.println("Digite o ano limite de lançamento: ");
            var releaseYear = input.nextInt();
            input.nextLine();

            List<Episode> yearEpisodes = serieRepository.episodesBySerieAndYear(serie, releaseYear);
            yearEpisodes.forEach(System.out::println);
        }
    }

}
