package de.tfsw.temp.temperature.server;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Temperature measurement point.
 * 
 * @author thorsten
 *
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class TemperatureMeasurement {
	
	/** */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/** */
	private Instant timestamp = Instant.now();

	/** */
	private String name;
	
	/** */
	private double value;
	
	/** TEmperature unit - defaults to {@link Unit#CELSIUS}. */
	private Unit unit = Unit.CELSIUS;
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public TemperatureMeasurement(String name, double value) {
		this(name, value, Unit.CELSIUS);
	}

	/**
	 * @param name
	 * @param value
	 * @param unit
	 */
	public TemperatureMeasurement(String name, double value, Unit unit) {
		this.name = name;
		this.value = value;
		this.unit = unit;
	}
}
