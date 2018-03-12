package de.tfsw.temp.temperature.server;

import static de.tfsw.temp.temperature.server.TestHelper.BEDROOM;
import static de.tfsw.temp.temperature.server.TestHelper.BEDROOM1;
import static de.tfsw.temp.temperature.server.TestHelper.BEDROOM2;
import static de.tfsw.temp.temperature.server.TestHelper.BEDROOM3;
import static de.tfsw.temp.temperature.server.TestHelper.LIVINGROOM;
import static de.tfsw.temp.temperature.server.TestHelper.LIVINGROOM2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemperatureRepositoryTest {

	@Autowired
	private TemperatureRepository repo;
	
	@Before
	public void setUp() {
		TestHelper.setupDatabase(repo);
	}
	
	@Test
	public void testFindAllNames() {
		List<String> names = repo.findAllNames();
		assertEquals(2, names.size());
		assertTrue(names.contains(BEDROOM));
		assertTrue(names.contains(LIVINGROOM));
	}
	
	@Test
	public void testFindByName() {
		assertEquals(3, repo.findByName(BEDROOM).size());
		assertEquals(2, repo.findByName(LIVINGROOM).size());
		assertTrue(repo.findByName("anothername").isEmpty());
	}
	
	@Test
	public void testFindFirstByName() {
		TemperatureMeasurement recent1 = repo.findFirstByNameOrderByTimestampDesc(LIVINGROOM);
		assertEquals(LIVINGROOM, recent1.getName());
		assertEquals(LIVINGROOM2.getValue(), recent1.getValue(), 0);
		
		TemperatureMeasurement recent2 = repo.findFirstByNameOrderByTimestampDesc(BEDROOM);
		assertEquals(BEDROOM, recent2.getName());
		assertEquals(BEDROOM3.getValue(), recent2.getValue(), 0);
	}
	
	@Test
	public void testFindByNameAndTimestampBetween() {
		Instant now = Instant.now();
		doTestFindByNameAndTimestampBetween(BEDROOM, now.minus(Duration.ofHours(24)), now, BEDROOM1, BEDROOM2, BEDROOM3);
		doTestFindByNameAndTimestampBetween(BEDROOM, BEDROOM1.getTimestamp().plusSeconds(1), now, BEDROOM2, BEDROOM3);
		doTestFindByNameAndTimestampBetween(BEDROOM, BEDROOM2.getTimestamp().plusSeconds(1), now, BEDROOM3);
		doTestFindByNameAndTimestampBetween(BEDROOM, BEDROOM3.getTimestamp().plusSeconds(1), now);
		doTestFindByNameAndTimestampBetween(BEDROOM, BEDROOM1.getTimestamp(), BEDROOM2.getTimestamp(), BEDROOM1, BEDROOM2);
	}
	
	private void doTestFindByNameAndTimestampBetween(String name, Instant from, Instant until, TemperatureMeasurement... expectedMeasurements) {
		List<TemperatureMeasurement> expected = Arrays.asList(expectedMeasurements);
		List<TemperatureMeasurement> actual = repo.findByNameAndTimestampBetween(name, from, until);
		assertEquals(expected.size(), actual.size());
		// TODO check the actual contents of the returned collection
	}
}
