package de.tfsw.temperature.server.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import de.tfsw.temperature.server.TemperatureMeasurement;
import de.tfsw.temperature.server.TemperatureService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;

@SpringView(name = "overview")
@Slf4j
public class DetailGridView extends VerticalLayout implements View {

    static final String NAME = "detailgrid";

    private static final TimeFrame[] TIMEFRAMES = {
            new TimeFrame("24 hours", Duration.ofHours(24)),
            new TimeFrame("12 hours", Duration.ofHours(12)),
            new TimeFrame("6 hours", Duration.ofHours(6)),
            new TimeFrame("3 hours", Duration.ofHours(3)),
            new TimeFrame("2 hours", Duration.ofHours(2)),
            new TimeFrame("Last hour", Duration.ofHours(1)),
            new TimeFrame("30 minutes", Duration.ofMinutes(30)),
            new TimeFrame("15 minutest", Duration.ofMinutes(15)),
    };

    private TemperatureService service;

    private Label nameLabel;
    private ComboBox<TimeFrame> predefinedTimeframes;
    private Grid<TemperatureMeasurement> grid;

    public DetailGridView(TemperatureService service) {
        this.service = service;
        buildStaticContent();
    }

    private void buildStaticContent() {
        setSizeUndefined();
        nameLabel = new Label("Undefined");
        addComponent(nameLabel);

        predefinedTimeframes = new ComboBox<>("TimeFrame");
        predefinedTimeframes.setItems(TIMEFRAMES);
        predefinedTimeframes.setSelectedItem(TIMEFRAMES[0]);
        predefinedTimeframes.setItemCaptionGenerator(TimeFrame::getName);
        predefinedTimeframes.addValueChangeListener(e ->  updateGrid(e.getValue()));
        addComponent(predefinedTimeframes);

        grid = new Grid<>(TemperatureMeasurement.class);
//        grid.addColumn(TemperatureMeasurement::getValue).setCaption("Value");
//        grid.addColumn(TemperatureMeasurement::getTimestamp).setCaption("Timestamp");
        grid.setColumns("value", "timestamp");
        addComponent(grid);

        Button back = new Button("Back to overview...");
        back.addClickListener(e -> getUI().getNavigator().navigateTo(Overview.NAME));
        addComponent(back);
    }

    private void updateGrid(TimeFrame timeFrame) {
        final Instant now = Instant.now();
        final Instant from = now.minus(timeFrame.getDeduction());
        final String name = nameLabel.getValue();
        log.info("Gettings items for {} from {} until {}", name, from, now);

        final List<TemperatureMeasurement> measurements = service.getMeasurements(name, from, now);
        log.info("Number of items found: {}", measurements.size());
        measurements.forEach(m -> log.info("Measurement: {}", m.getTimestamp()));
        grid.setItems(measurements);
    }

    private void changeContent(String name) {
        nameLabel.setValue(name);
        TimeFrame timeFrame = predefinedTimeframes.getSelectedItem().orElse(TIMEFRAMES[0]);
        log.info("Updating grid for timeframe {}", timeFrame.getName());
        updateGrid(timeFrame);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        changeContent(event.getParameters());
    }

    @AllArgsConstructor
    @Getter
    static final class TimeFrame {
        private String name;
        private TemporalAmount deduction;
    }
}