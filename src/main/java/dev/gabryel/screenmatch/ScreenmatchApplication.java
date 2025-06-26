package dev.gabryel.screenmatch;

import dev.gabryel.screenmatch.model.SerieData;
import dev.gabryel.screenmatch.service.APIConsumption;
import dev.gabryel.screenmatch.service.DataParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
		System.out.println(json);
		DataParser dataParser = new DataParser();
		SerieData data = dataParser.fetchData(json, SerieData.class);
		System.out.println(data);
	}
}
