package de.tfsw.temperature.server.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

import de.tfsw.temperature.server.TemperatureService;

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
		//setSizeFull();
		//addStyleName(ValoTheme.UI_WITH_MENU);
		
		//buildMainLayout();

		Navigator navigator = new Navigator(this, this);
		navigator.addView(Overview.NAME, new Overview(service));
		navigator.addView(DetailGridView.NAME, new DetailGridView(service));
		navigator.navigateTo(Overview.NAME);
    }
}
