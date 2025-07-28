package dev.gabryel.screenmatch;

import dev.gabryel.screenmatch.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
	private final Main main;

	@Autowired
	public ScreenmatchApplication(Main main) {
		this.main = main;
	}

	public static void main(String[] args) {
			SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		main.showMenu();
	}
}
