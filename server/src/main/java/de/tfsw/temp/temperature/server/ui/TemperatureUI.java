package de.tfsw.temp.temperature.server.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

import de.tfsw.temp.temperature.server.TemperatureService;

/**
 * Vaadin UI.
 * 
 * @author thorsten
 *
 */
@SpringUI
@Theme("temperature")
@Title("Temperature Measurements")
public class TemperatureUI extends UI {
	
	private static final long serialVersionUID = 1L;

	private TemperatureService service;
	
	/**
	 * 
	 * @param repo
	 */
	public TemperatureUI(TemperatureService service) {
		this.service = service;
	}

	@Override
    protected void init(VaadinRequest request) {
		setSizeFull();
		//addStyleName(ValoTheme.UI_WITH_MENU);
		
		buildMainLayout();
    }
    
	private void buildMainLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		setContent(layout);
		
		layout.setSizeFull();
		layout.setSpacing(false);
		
		layout.addComponent(new Overview(service));
	}
}
