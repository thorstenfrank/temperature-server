package de.tfsw.temp.temperature.server;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemperatureEndpoint {

	private TemperatureRepository repo;
		
	@Autowired
	public TemperatureEndpoint(TemperatureRepository repo) {
		this.repo = repo;
	}
	
	/**
	 * 
	 * @param measurement
	 * @return
	 */
	@RequestMapping(
			value = "/temperature/{name}",
			method = RequestMethod.POST)
	public void addMeasurement(
			@PathVariable("name") final String name, 
			@RequestParam("value") final double value,
			@RequestParam(name = "unit", required = false) final Unit unit) {
		
		TemperatureMeasurement addMe = new TemperatureMeasurement(name, value);
		if (unit != null) {
			addMe.setUnit(unit);
		}
		
		repo.save(addMe);
	}
	
	/**
	 * 
	 * @return all known measurements
	 */
	@RequestMapping(
			value = "/temperature",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public List<TemperatureMeasurement> getAllMeasurements() {
		return repo.findAll();
	}
	
	/**
	 * 
	 * @return the most recent measurement for each name
	 */
	@RequestMapping(
			value = "/temperature/current",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public List<TemperatureMeasurement> getCurrentMeasurements() {
		return repo.findAllNames().stream()
			.map(name -> repo.findFirstByNameOrderByTimestampDesc(name))
			.collect(Collectors.toList());
	}
}
