package dev.gabryel.screenmatch;

import dev.gabryel.screenmatch.main.Main;
import dev.gabryel.screenmatch.repository.SerieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
	private final Main main;
	private final SerieRepository serieRepository;


	public ScreenmatchApplication(Main main, SerieRepository serieRepository) {
		this.main = main;
		this.serieRepository = serieRepository;
	}

	public static void main(String[] args) {
			SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		main.showMenu();
	}
}
