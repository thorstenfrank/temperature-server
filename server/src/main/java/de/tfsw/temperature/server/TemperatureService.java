package de.tfsw.temperature.server;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemperatureService {

	private TemperatureRepository repo;

	/**
	 * @param repo
	 */
	@Autowired
	public TemperatureService(TemperatureRepository repo) {
		this.repo = repo;
	}
	
	public void addMeasurement(final String name, final double value) {
		repo.save(new TemperatureMeasurement(name, value));
	}
	
	public void addMeasurement(final String name, final double value, final Unit unit) {
		repo.save(new TemperatureMeasurement(name, value, unit));
	}
	
	public List<TemperatureMeasurement> getAllMeasurements() {
		return repo.findAll();
	}

	public List<TemperatureMeasurement> getMeasurements(String name, Instant from, Instant until) {
		return repo.findByNameAndTimestampBetween(name, from, until);
	}

	public List<TemperatureMeasurement> getCurrentMeasurements() {
		return repo.findAllNames().stream()
				.map(name -> repo.findFirstByNameOrderByTimestampDesc(name))
				.collect(Collectors.toList());
	}
	
	public List<MeasurementSummary> getSummary(final Instant from, final Instant until) {
		List<MeasurementSummary> summaries = new ArrayList<>();
		
		repo.findAllNames().forEach(name -> {
			MeasurementSummary summary = buildSummary(repo.findByNameAndTimestampBetween(name, from, until), from);
			summary.setName(name);
			summary.setFrom(from);
			summary.setUntil(until);
			summaries.add(summary);
		});
		
		return summaries;
	}
	
	private MeasurementSummary buildSummary(List<TemperatureMeasurement> measurements, Instant from) {
		MeasurementSummary summary = new MeasurementSummary();
		Instant mostRecent = from.minusSeconds(1);
		
		if (measurements.size() > 0) {
			summary.setLow(measurements.get(0).getValue());
			
			double total = 0;
			int numberOfMeasurements = 0;
			
			for (TemperatureMeasurement m : measurements) {
				total += m.getValue();
				numberOfMeasurements++;
				if (m.getValue() < summary.getLow()) {
					summary.setLow(m.getValue());
				}
				if (m.getValue() > summary.getHigh()) {
					summary.setHigh(m.getValue());
				}
				if (m.getTimestamp().isAfter(mostRecent)) {
					summary.setCurrent(m.getValue());
				}
			}
			
			double avg = total / numberOfMeasurements;
			
			
			summary.setAverage(BigDecimal.valueOf(avg).setScale(3, RoundingMode.HALF_UP).doubleValue());
		}
		return summary;
	}
}
