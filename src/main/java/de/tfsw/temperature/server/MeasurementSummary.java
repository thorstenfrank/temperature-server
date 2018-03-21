package de.tfsw.temperature.server;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeasurementSummary {

	private Instant from;
	
	private Instant until;
	
	private String name;
	
	private double current;
	
	private double high;
	
	private double low;
	
	private double average;
}
