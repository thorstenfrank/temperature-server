package de.tfsw.temp.temperature.server;

import org.springframework.util.StringUtils;

import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Vaadin UI.
 * 
 * @author thorsten
 *
 */
@SpringUI
public class TemperatureUI extends UI {
	
	private static final long serialVersionUID = 1L;

	private TemperatureRepository repo;
	
	private Grid<TemperatureMeasurement> grid;
	
	private TextField filter;
	
	public TemperatureUI(TemperatureRepository repo) {
		super();
		this.repo = repo;
		this.grid = new Grid<>(TemperatureMeasurement.class);
		this.filter = new TextField();
	}

	@Override
    protected void init(VaadinRequest request) {
		HorizontalLayout actions = new HorizontalLayout(filter);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid);
		setContent(mainLayout);
		
		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("name", "timestamp", "value");
		
		filter.setPlaceholder("Filter by name");
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listCustomers(e.getValue()));
		
	    listCustomers(null);
    }
    
	void listCustomers(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(repo.findAll());
		}
		else {
			grid.setItems(repo.findByName(filterText));
		}
	}
}
