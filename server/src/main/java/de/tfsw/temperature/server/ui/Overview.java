package de.tfsw.temperature.server.ui;

import com.vaadin.navigator.View;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.tfsw.temperature.server.MeasurementSummary;
import de.tfsw.temperature.server.TemperatureService;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;

@SpringView(name = "overview")
@Slf4j
public class Overview extends VerticalLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static final String NAME = "OVERVIEW";

	private TemperatureService service;

	private HorizontalLayout layout;
	
	/**
	 * @param service
	 */
	public Overview(TemperatureService service) {
		this.service = service;

        buildContent();
	}
	
	private void buildContent() {
		
		HorizontalLayout header = new HorizontalLayout();
		header.setSizeUndefined();
		
		Label headerLabel = new Label("CURRENT TEMPERATURES");
		headerLabel.addStyleName(ValoTheme.LABEL_H1);
		header.addComponent(headerLabel);
		
		addComponent(header);
		
		layout = new HorizontalLayout();
		layout.setSizeUndefined();
		addComponent(layout);
		
		log.info("Getting all measurement names...");
		
		final Instant now = Instant.now();
		final Instant from = now.minus(Duration.ofHours(24));

		service.getSummary(from, now).forEach(this::addMeasurement);

		Label naviationInfoLabel = new Label("Click on a panel for details...");
		naviationInfoLabel.setStyleName(ValoTheme.LABEL_LIGHT);
		addComponent(naviationInfoLabel);
	}
	
	private void addMeasurement(final MeasurementSummary summary) {
		log.info("Adding measurement summary for {}", summary.getName());

		Panel panel = new Panel(); //summary.getName());
		panel.setSizeUndefined();
		addComponent(panel);

		VerticalLayout panelLayout = new VerticalLayout();
		panelLayout.setSizeUndefined();
		panelLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

		Label nameLabel = new Label(summary.getName());
		nameLabel.addStyleName(ValoTheme.LABEL_HUGE);
		panelLayout.addComponent(nameLabel);

		Label currentTemp = new Label(Double.toString(summary.getCurrent()));
		panelLayout.addComponent(currentTemp);
		
		Label lowAvgHigh = new Label(
				"High <b>" + Double.toString(summary.getHigh()) + "</b>&nbsp;&nbsp;&nbsp;"
				+ "Avg <b>" + Double.toString(summary.getAverage()) + "</b>&nbsp;&nbsp;&nbsp;"
				+ "Low <b>" + Double.toString(summary.getLow()) + "</b>",
				ContentMode.HTML);

		lowAvgHigh.addStyleName(ValoTheme.LABEL_TINY);
		lowAvgHigh.addStyleName(ValoTheme.LABEL_LIGHT);
        lowAvgHigh.setSizeUndefined();
		panelLayout.addComponent(lowAvgHigh);

		panel.setContent(panelLayout);

		panel.addClickListener(e -> getUI().getNavigator().navigateTo(DetailGridView.NAME + "/" + summary.getName()));

		layout.addComponent(panel);
	}
}
