package de.tfsw.temp.temperature.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.Instant;
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
		assertTrue(names.contains(TestHelper.BEDROOM));
		assertTrue(names.contains(TestHelper.LIVINGROOM));
	}
	
	@Test
	public void testFindByName() {
		assertEquals(3, repo.findByName(TestHelper.BEDROOM).size());
		assertEquals(2, repo.findByName(TestHelper.LIVINGROOM).size());
		assertTrue(repo.findByName("anothername").isEmpty());
	}
	
	@Test
	public void testFindFirstByName() {
		TemperatureMeasurement recent1 = repo.findFirstByNameOrderByTimestampDesc(TestHelper.LIVINGROOM);
		assertEquals(TestHelper.LIVINGROOM, recent1.getName());
		assertEquals(TestHelper.LIVINGROOM2.getValue(), recent1.getValue(), 0);
		
		TemperatureMeasurement recent2 = repo.findFirstByNameOrderByTimestampDesc(TestHelper.BEDROOM);
		assertEquals(TestHelper.BEDROOM, recent2.getName());
		assertEquals(TestHelper.BEDROOM3.getValue(), recent2.getValue(), 0);
	}
	
	@Test
	public void testFindByNameAfterTimestamp() {
		assertEquals(3, repo.findByNameAndTimestampAfter(TestHelper.BEDROOM, Instant.now().minus(Duration.ofHours(24))).size());
		assertEquals(2, repo.findByNameAndTimestampAfter(TestHelper.BEDROOM, TestHelper.BEDROOM1.getTimestamp()).size());
		assertEquals(1, repo.findByNameAndTimestampAfter(TestHelper.BEDROOM, TestHelper.BEDROOM2.getTimestamp()).size());
		assertEquals(0, repo.findByNameAndTimestampAfter(TestHelper.BEDROOM, TestHelper.BEDROOM3.getTimestamp()).size());
	}
}
