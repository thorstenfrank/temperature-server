package de.tfsw.temperature.server;

public enum Unit {

	CELSIUS("C"), FAHRENHEIT("F");
	
	private String shortName;

	/**
	 * @param shortName
	 */
	private Unit(String shortName) {
		this.shortName = shortName;
	}

	public String getShortName() {
		return shortName;
	}
	
	
}
