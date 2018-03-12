package de.tfsw.temp.temperature.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TemperatureEndpointTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private TemperatureRepository repo;
	
	@Before
	public void setUp() {
		TestHelper.setupDatabase(repo);
	}
	
	@Test
	public void testAddMeasurement() throws Exception {
		final String name = "Kitchen";
		final double value = 23.75;
		
		mvc
			.perform(post("/temperature/" + name + "?value=" + value))
			.andExpect(status().isOk());
		
		TemperatureMeasurement saved = repo.findFirstByNameOrderByTimestampDesc(name);
		assertEquals(value, saved.getValue(), 0);
		assertEquals(Unit.CELSIUS, saved.getUnit());
		
		assertEquals(3, repo.findAllNames().size());
		
		repo.delete(saved);
		
		assertEquals(2, repo.findAllNames().size());
	}
	
	@Test
	public void testAddMeasurementFahrenheit() throws Exception {
		final String name = "Outside";
		final double value = 66.342;
		
		mvc
			.perform(post("/temperature/" + name + "?value=" + value + "&unit=FAHRENHEIT"))
			.andExpect(status().isOk());
		
		TemperatureMeasurement saved = repo.findFirstByNameOrderByTimestampDesc(name);
		assertEquals(value, saved.getValue(), 0);
		assertEquals(Unit.FAHRENHEIT, saved.getUnit());
	}
	
	@Test
	public void testGetAll() throws Exception {
		MvcResult result = mvc.perform(get("/temperature")).andExpect(status().isOk()).andReturn();
		TemperatureMeasurement[] measurements = TestHelper.jsonMapper().readValue(
				result.getResponse().getContentAsString(), TemperatureMeasurement[].class);
		assertEquals(5, measurements.length);
	}
	
	@Test
	public void testGetCurrent() throws Exception {
		MvcResult result = mvc.perform(get("/temperature/current")).andExpect(status().isOk()).andReturn();
		TemperatureMeasurement[] measurements = TestHelper.jsonMapper().readValue(
				result.getResponse().getContentAsString(), TemperatureMeasurement[].class);
		assertEquals(2, measurements.length);
		
		Arrays.stream(measurements).forEach(this::assertReturnedMeasurement);
	}
	
	private void assertReturnedMeasurement(final TemperatureMeasurement measurement) {
		if (TestHelper.BEDROOM.equals(measurement.getName())) {
			assertMeasurementEquals(TestHelper.BEDROOM3, measurement);
		} else if (TestHelper.LIVINGROOM.equals(measurement.getName())) {
			assertMeasurementEquals(TestHelper.LIVINGROOM2, measurement);
		} else {
			fail("Unknown measurement returned, expected 'Bedroom' or 'Living' Room only: " + measurement);
		}
	}
	
	private void assertMeasurementEquals(final TemperatureMeasurement expected, final TemperatureMeasurement actual) {
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getTimestamp(), actual.getTimestamp());
		assertEquals(expected.getValue(), actual.getValue(), 0);
		assertEquals(expected.getUnit(), actual.getUnit());
	}
}
