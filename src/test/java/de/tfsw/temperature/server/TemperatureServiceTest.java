package de.tfsw.temperature.server;

import static de.tfsw.temperature.server.TestHelper.BEDROOM;
import static de.tfsw.temperature.server.TestHelper.BEDROOM1;
import static de.tfsw.temperature.server.TestHelper.BEDROOM2;
import static de.tfsw.temperature.server.TestHelper.BEDROOM3;
import static de.tfsw.temperature.server.TestHelper.LIVINGROOM;
import static de.tfsw.temperature.server.TestHelper.LIVINGROOM1;
import static de.tfsw.temperature.server.TestHelper.LIVINGROOM2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class TemperatureServiceTest {

	private TemperatureService service;
	
	private TemperatureRepository repoMock;
	
	@Before
	public void setUp() {
		this.repoMock = mock(TemperatureRepository.class);
		this.service = new TemperatureService(repoMock);
	}
	
	@Test
	public void testAddMeasurement() {
		final String name = BEDROOM;
		final double value = 31.345;
		
		service.addMeasurement(name, value);
		
		verifySave(name, value, Unit.CELSIUS);
	}
	
	@Test
	public void testAddMeasurementWithUnit() {
		final String name = LIVINGROOM;
		final double value = 4.2;
		
		service.addMeasurement(name, value, Unit.FAHRENHEIT);
		
		verifySave(name, value, Unit.FAHRENHEIT);
	}
	
	@Test
	public void testGetAll() {
		List<TemperatureMeasurement> list = Arrays.asList(
				BEDROOM1,
				BEDROOM2,
				BEDROOM3,
				LIVINGROOM1,
				LIVINGROOM2);

		when(repoMock.findAll()).thenReturn(list);
		
		List<TemperatureMeasurement> fromService = service.getAllMeasurements();
		
		assertEquals(list, fromService);
		
		verify(repoMock).findAll();
	}
	
	@Test
	public void testGetCurrent() {
		when(repoMock.findAllNames()).thenReturn(Arrays.asList(BEDROOM, LIVINGROOM));
		when(repoMock.findFirstByNameOrderByTimestampDesc(LIVINGROOM)).thenReturn(LIVINGROOM2);
		when(repoMock.findFirstByNameOrderByTimestampDesc(BEDROOM)).thenReturn(BEDROOM3);
		
		List<TemperatureMeasurement> fromService = service.getCurrentMeasurements();
		assertEquals(2, fromService.size());
		assertTrue(fromService.contains(BEDROOM3));
		assertTrue(fromService.contains(LIVINGROOM2));
		
		verify(repoMock).findAllNames();
		verify(repoMock).findFirstByNameOrderByTimestampDesc(LIVINGROOM);
		verify(repoMock).findFirstByNameOrderByTimestampDesc(BEDROOM);
	}
	
	@Test
	public void testGetOverview() {
		Instant until = Instant.now();
		Instant from = until.minus(Duration.ofHours(12));
		
		final String singleName = "single";
		final double value = 12;
		
		final TemperatureMeasurement singleMeasurement = new TemperatureMeasurement(singleName, value);
		
		when(repoMock.findAllNames()).thenReturn(Arrays.asList(BEDROOM, LIVINGROOM, singleName));
		when(repoMock.findByNameAndTimestampBetween(singleName, from, until)).thenReturn(Collections.singletonList(singleMeasurement));
		when(repoMock.findByNameAndTimestampBetween(BEDROOM, from, until)).thenReturn(Arrays.asList(BEDROOM1, BEDROOM2, BEDROOM3));
		when(repoMock.findByNameAndTimestampBetween(LIVINGROOM, from, until)).thenReturn(Arrays.asList(LIVINGROOM1, LIVINGROOM2));
		
		List<MeasurementSummary> summary = service.getSummary(from, until);
		assertEquals(3, summary.size());
		
		summary.forEach(s -> {
			assertEquals(from, s.getFrom());
			assertEquals(until, s.getUntil());
			
			if (BEDROOM.equals(s.getName())) {
				assertEquals(BEDROOM1.getValue(), s.getLow(), 0);
				assertEquals(BEDROOM2.getValue(), s.getHigh(), 0);
				assertEquals(BEDROOM3.getValue(), s.getCurrent(), 0);
				assertEquals(13.417, s.getAverage(), 0);
			} else if (LIVINGROOM.equals(s.getName())) {
				assertEquals(LIVINGROOM1.getValue(), s.getLow(), 0);
				assertEquals(LIVINGROOM2.getValue(), s.getHigh(), 0);
				assertEquals(LIVINGROOM2.getValue(), s.getCurrent(), 0);
				assertEquals(18.2, s.getAverage(), 0);
			} else if (singleName.equals(s.getName())) {
				assertEquals(value, s.getLow(), 0);
				assertEquals(value, s.getHigh(), 0);
				assertEquals(value, s.getCurrent(), 0);
				assertEquals(value, s.getAverage(), 0);
			}
			else {
				fail("Unexpected measurement summary name: " + s.getName());
			}
		});
		
		verify(repoMock).findAllNames();
		verify(repoMock).findByNameAndTimestampBetween(singleName, from, until);
		verify(repoMock).findByNameAndTimestampBetween(BEDROOM, from, until);
		verify(repoMock).findByNameAndTimestampBetween(LIVINGROOM, from, until);
	}
	
	private void verifySave(final String name, final double value, final Unit unit) {
		ArgumentCaptor<TemperatureMeasurement> captor = ArgumentCaptor.forClass(TemperatureMeasurement.class);
		verify(repoMock).save(captor.capture());
		
		TemperatureMeasurement m = captor.getValue();
		
		assertNull(m.getId()); // this will be set by the actual persistence implementation
		assertEquals(name, m.getName());
		assertEquals(value, m.getValue(), 0);
		assertEquals(unit, m.getUnit());
		assertNotNull(m.getTimestamp());
	}
}
