package de.tfsw.temperature.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Instant;

@SpringBootApplication
public class TempserverApplication {

	public static void main(final String[] args) {
		SpringApplication.run(TempserverApplication.class, args);
	}

	@Autowired
	private TemperatureRepository repo;

	@Bean
	public CommandLineRunner testDataCreator() {
		return args -> {
			Instant now = Instant.now();

			repo.save(new TemperatureMeasurement("Wohnzimmer", 21.232, now.minusMillis(180123)));
			repo.save(new TemperatureMeasurement("Wohnzimmer", 21.3, now.minusMillis(167823)));
			repo.save(new TemperatureMeasurement("Wohnzimmer", 21.345, now.minusMillis(134053)));
			repo.save(new TemperatureMeasurement("Wohnzimmer", 21.54, now.minusMillis(123945)));
			repo.save(new TemperatureMeasurement("Wohnzimmer", 21.564, now.minusMillis(99235)));
			repo.save(new TemperatureMeasurement("Wohnzimmer", 21.645, now.minusMillis(84023)));
			repo.save(new TemperatureMeasurement("Wohnzimmer", 22.456, now.minusMillis(75083)));
			repo.save(new TemperatureMeasurement("Wohnzimmer", 22.683, now.minusMillis(67834)));
			repo.save(new TemperatureMeasurement("Wohnzimmer", 23.021, now.minusMillis(34563)));
			repo.save(new TemperatureMeasurement("Wohnzimmer", 23.143, now.minusMillis(12393)));


			repo.save(new TemperatureMeasurement("Schlafzimmer", 18.234, now.minusSeconds(180)));
			repo.save(new TemperatureMeasurement("Schlafzimmer", 18.43, now.minusSeconds(120)));
			repo.save(new TemperatureMeasurement("Schlafzimmer", 18.123, now.minusSeconds(60)));
			repo.save(new TemperatureMeasurement("Schlafzimmer", 17.934, now.minusSeconds(5)));
		};
	}
}
