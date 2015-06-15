package com.flow.vaadin.osgi.api;

public interface VaadinApplicationConfig {
	
	String name();
	String hostName();
	String path();
	String uiAlias();
	String resourceBase();
	String widgetset();
	Boolean productionMode();
	String httpApplication();
	
	Boolean offline();
	Boolean maintence();
	
	
}
