package de.tfsw.temp.temperature.server.ui;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.tfsw.temp.temperature.server.TemperatureMeasurement;
import de.tfsw.temp.temperature.server.TemperatureRepository;
import lombok.extern.slf4j.Slf4j;

@SpringView(name = "overview")
@Slf4j
public class Overview extends Panel implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TemperatureRepository repo;

	private HorizontalLayout layout;
	
	/**
	 * @param repo
	 */
	public Overview(TemperatureRepository repo) {
		this.repo = repo;
        buildContent();
	}
	
	private void buildContent() {
		VerticalLayout root = new VerticalLayout();
		setContent(root);
		root.setSizeFull();
		
		HorizontalLayout header = new HorizontalLayout();
		header.setSizeFull();
		header.addComponent(new Label("CURRENT TEMPERATURES"));
		
		root.addComponent(header);
		
		layout = new HorizontalLayout();
		layout.setSizeFull();
		root.addComponent(layout);
		
		log.info("Getting all measurement names...");
		
		repo.findAllNames().forEach(this::addMeasurement);
	}
	
	private void addMeasurement(final String name) {
		log.info("Getting current measurement for {}", name);
		TemperatureMeasurement measurement = repo.findFirstByNameOrderByTimestampDesc(name);
		
		VerticalLayout measurementLayout = new VerticalLayout();
		measurementLayout.setSizeUndefined();
		
		Label nameLabel = new Label(measurement.getName());
		measurementLayout.addComponent(nameLabel);
		
		Label currentTemp = new Label(measurement.getValue() + " " + measurement.getUnit().getShortName());
		measurementLayout.addComponent(currentTemp);
		
		layout.addComponent(measurementLayout);
	}
}
