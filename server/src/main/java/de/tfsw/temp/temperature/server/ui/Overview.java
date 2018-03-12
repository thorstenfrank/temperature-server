package de.tfsw.temp.temperature.server.ui;

import java.time.Duration;
import java.time.Instant;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.tfsw.temp.temperature.server.MeasurementSummary;
import de.tfsw.temp.temperature.server.TemperatureService;
import lombok.extern.slf4j.Slf4j;

@SpringView(name = "overview")
@Slf4j
public class Overview extends Panel implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TemperatureService service;

	private HorizontalLayout layout;
	
	/**
	 * @param repo
	 */
	public Overview(TemperatureService service) {
		this.service = service;
        buildContent();
	}
	
	private void buildContent() {
		VerticalLayout root = new VerticalLayout();
		setContent(root);
		root.setSizeFull();
		
		HorizontalLayout header = new HorizontalLayout();
		header.setSizeFull();
		
		Label headerLabel = new Label("CURRENT TEMPERATURES");
		headerLabel.addStyleName(ValoTheme.LABEL_H1);
		header.addComponent(headerLabel);
		
		root.addComponent(header);
		
		layout = new HorizontalLayout();
		layout.setSizeFull();
		root.addComponent(layout);
		
		log.info("Getting all measurement names...");
		
		final Instant now = Instant.now();
		final Instant from = now.minus(Duration.ofHours(24));
		service.getSummary(from, now).forEach(this::addMeasurement);
	}
	
	private void addMeasurement(final MeasurementSummary summary) {
		log.info("Adding measurement summary for {}", summary.getName());
		
		VerticalLayout measurementLayout = new VerticalLayout();
		measurementLayout.setSizeUndefined();
		measurementLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		
		Label nameLabel = new Label(summary.getName());
		
		measurementLayout.addComponent(nameLabel);
		
		Label currentTemp = new Label(Double.toString(summary.getCurrent()));
		nameLabel.addStyleName(ValoTheme.LABEL_HUGE);
		measurementLayout.addComponent(currentTemp);
		
		Label lowAvgHigh = new Label(
				"High <b>" + Double.toString(summary.getHigh()) + "</b>&nbsp;&nbsp;&nbsp;"
				+ "Avg <b>" + Double.toString(summary.getAverage()) + "</b>&nbsp;&nbsp;&nbsp;"
				+ "Low <b>" + Double.toString(summary.getLow()) + "</b>",
				ContentMode.HTML);

		lowAvgHigh.addStyleName(ValoTheme.LABEL_TINY);
		lowAvgHigh.addStyleName(ValoTheme.LABEL_LIGHT);
        lowAvgHigh.setSizeUndefined();
		measurementLayout.addComponent(lowAvgHigh);
		
		layout.addComponent(measurementLayout);
	}
}
