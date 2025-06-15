package dev.gabryel.screenmatch;

import dev.gabryel.screenmatch.service.APIConsumption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	private final APIConsumption apiConsumption;

	@Autowired
	public ScreenmatchApplication(APIConsumption apiConsumption) {
		this.apiConsumption = apiConsumption;
	}

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String mediaName = "The Office";
		var json = apiConsumption.fetchData(mediaName);
		System.out.println(json);
	}
}
