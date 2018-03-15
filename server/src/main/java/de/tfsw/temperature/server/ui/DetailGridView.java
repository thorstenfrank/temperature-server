package de.tfsw.temperature.server.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import de.tfsw.temperature.server.TemperatureMeasurement;
import de.tfsw.temperature.server.TemperatureService;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;

@SpringView(name = "overview")
@Slf4j
public class DetailGridView extends VerticalLayout implements View {

    static final String NAME = "detailgrid";

    private TemperatureService service;

    private Label nameLabel;
    private Grid<TemperatureMeasurement> grid;

    public DetailGridView(TemperatureService service) {
        this.service = service;
        buildStaticContent();
    }

    private void buildStaticContent() {
        nameLabel = new Label("Undefined");
        addComponent(nameLabel);

        grid = new Grid<>();
        grid.addColumn(TemperatureMeasurement::getValue).setCaption("Value");
        grid.addColumn(TemperatureMeasurement::getTimestamp).setCaption("Timestamp");
        grid.setSizeUndefined();
        addComponent(grid);

        Button back = new Button("Back to overview...");
        back.addClickListener(e -> getUI().getNavigator().navigateTo(Overview.NAME));
        addComponent(back);
    }

    private void changeContent(String name) {
        nameLabel.setValue(name);

        Instant now = Instant.now();
        Instant from = now.minus(Duration.ofHours(24));

        grid.setItems(service.getMeasurements(name, from, now));
        grid.recalculateColumnWidths();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        changeContent(event.getParameters());
    }
}