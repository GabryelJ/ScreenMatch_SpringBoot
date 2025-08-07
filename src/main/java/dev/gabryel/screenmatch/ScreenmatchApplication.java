package dev.gabryel.screenmatch;

import dev.gabryel.screenmatch.main.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication{

	public ScreenmatchApplication(Main main) {
	}

	public static void main(String[] args) {
			SpringApplication.run(ScreenmatchApplication.class, args);
	}
}
