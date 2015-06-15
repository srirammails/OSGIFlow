package com.flow.shellx.api;

import java.util.List;

import org.osgi.service.http.HttpService;

public interface IShellUIApplication {

	Object  getStatus();

	String getId();

	String getName();

	String getUIAlias();

	String getHttpApplication();

	String getWidgetSetName();

	boolean isProductionMode();

	List<IShellUIProvider> getUiProviders();

	boolean isStarted();

	boolean isDeployed();

	void start();

	void stop();

	void httpServiceAdded(HttpService service);

	void httpServiceRemoved(HttpService service);

}
