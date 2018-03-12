package de.tfsw.temp.temperature.server;

import java.time.Instant;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class TestHelper {

	static final String BEDROOM = "Bedroom";
	static final String LIVINGROOM = "Living room";
	
	static final TemperatureMeasurement BEDROOM1 = new TemperatureMeasurement(BEDROOM, 12.5);
	static final TemperatureMeasurement BEDROOM2 = new TemperatureMeasurement(BEDROOM, 14);
	static final TemperatureMeasurement BEDROOM3 = new TemperatureMeasurement(BEDROOM, 13.75);
	static final TemperatureMeasurement LIVINGROOM1 = new TemperatureMeasurement(LIVINGROOM, 17.87);
	static final TemperatureMeasurement LIVINGROOM2 = new TemperatureMeasurement(LIVINGROOM, 18.53);
	
	private static final ObjectMapper MAPPER;
	
	static {
		BEDROOM1.setTimestamp(Instant.now().minusSeconds(30));
		BEDROOM2.setTimestamp(Instant.now().minusSeconds(20));
		BEDROOM3.setTimestamp(Instant.now().minusSeconds(10));
		
		LIVINGROOM1.setTimestamp(Instant.now().minusSeconds(100));
		LIVINGROOM2.setTimestamp(Instant.now().minusSeconds(99));
		
		MAPPER = new ObjectMapper();
		MAPPER.registerModule(new Jdk8Module());
		MAPPER.registerModule(new JavaTimeModule());
		MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		MAPPER.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	static void setupDatabase(final TemperatureRepository repo) {
		repo.deleteAll();
		repo.save(BEDROOM1);
		repo.save(BEDROOM2);
		repo.save(BEDROOM3);
		
		repo.save(LIVINGROOM1);
		repo.save(LIVINGROOM2);
	}
	
	 
	
	static ObjectMapper jsonMapper() {
		return MAPPER;
	}
}
