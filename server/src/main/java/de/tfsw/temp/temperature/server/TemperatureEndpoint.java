package de.tfsw.temp.temperature.server;

import java.util.List;

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

	private TemperatureService service;
		
	@Autowired
	public TemperatureEndpoint(TemperatureService service) {
		this.service = service;
	}
	
	@RequestMapping(
			value = "/temperature/{name}",
			method = RequestMethod.POST)
	public void addMeasurement(
			@PathVariable("name") final String name, 
			@RequestParam("value") final double value,
			@RequestParam(name = "unit", required = false) final Unit unit) {
		
		if (unit == null) {
			service.addMeasurement(name, value);
		} else {
			service.addMeasurement(name, value, unit);
		}
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
		return service.getAllMeasurements();
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
		return service.getCurrentMeasurements();
	}
}
