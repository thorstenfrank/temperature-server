package de.tfsw.temp.temperature.server;

import static org.junit.Assert.assertEquals;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;

public class TemperatureMeasurementTest {

	private static final String JSON  = "{\"id\":66,\"timestamp\":\"2018-03-10T15:27:29.877Z\",\"name\":\"junit\",\"value\":24.56}";
	
	private TemperatureMeasurement measurement;
	
	@Before
	public void setUp() {
		measurement = new TemperatureMeasurement();
		measurement.setId(Long.valueOf(66));
		measurement.setName("junit");
		measurement.setTimestamp(Instant.parse("2018-03-10T15:27:29.877Z"));
		measurement.setValue(24.56);
	}
	
	@Test
	public void testJsonMarshalling() throws Exception {
		assertEquals(JSON, TestHelper.jsonMapper().writeValueAsString(measurement));
	}
	
	@Test
	public void testJsonUnmarshalling() throws Exception {
		TemperatureMeasurement unmarshalled = TestHelper.jsonMapper().readValue(JSON, TemperatureMeasurement.class);
		
		assertEquals(measurement.getId(), unmarshalled.getId());
		assertEquals(measurement.getName(), unmarshalled.getName());
		assertEquals(measurement.getTimestamp(), unmarshalled.getTimestamp());
		assertEquals(measurement.getValue(), unmarshalled.getValue(), 0);
	}
}
