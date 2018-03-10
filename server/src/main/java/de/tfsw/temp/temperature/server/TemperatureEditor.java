package de.tfsw.temp.temperature.server;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.VerticalLayout;

@SpringComponent
@UIScope
public class TemperatureEditor extends VerticalLayout {

	private TemperatureRepository temperatureRepository;

	private TemperatureMeasurement current;
	
	@Autowired
	public TemperatureEditor(TemperatureRepository temperatureRepository) {
		super();
		this.temperatureRepository = temperatureRepository;
	}
}
