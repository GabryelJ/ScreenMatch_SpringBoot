package dev.gabryel.screenmatch;

import dev.gabryel.screenmatch.model.EpisodeData;
import dev.gabryel.screenmatch.model.SeasonData;
import dev.gabryel.screenmatch.model.SerieData;
import dev.gabryel.screenmatch.service.APIConsumption;
import dev.gabryel.screenmatch.service.DataParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
	@Value("${api.key}")
	private String apiKey;

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		APIConsumption apiConsumption = new APIConsumption();
		String mediaName = "The Office";
		String address = "http://www.omdbapi.com/?t=" + mediaName.replace(" ", "+") + '&' + "apikey=" + apiKey;
		var json = apiConsumption.fetchData(address);
		DataParser dataParser = new DataParser();
		SerieData data = dataParser.fetchData(json, SerieData.class);
		System.out.println(data);
		json = apiConsumption.fetchData("http://www.omdbapi.com/?t=" + mediaName.replace(" ", "+") + '&' + "season=1" + '&' + "episode=1" + '&' + "apikey=" + apiKey);
		EpisodeData episodeData = dataParser.fetchData(json, EpisodeData.class);
		System.out.println(episodeData);

		List<SeasonData> seasons = new ArrayList<>();

		for (int i = 1; i < data.totalSeasons(); i++){
			json = apiConsumption.fetchData("http://www.omdbapi.com/?t=" + mediaName.replace(" ", "+") + '&' + "season=" + i + '&' + "apikey=" + apiKey);
			SeasonData seasonData = dataParser.fetchData(json, SeasonData.class);
			seasons.add(seasonData);
		}

		seasons.forEach(System.out::println);
	}
}
