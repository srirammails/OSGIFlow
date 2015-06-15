package com.flow.shellx.api;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

public interface IShellUIProvider  {

	public abstract Class<? extends UI> getUIClass(UIClassSelectionEvent event);

	public abstract UI createInstance(UICreateEvent event);
	
	public abstract UIProvider getUIProvider();
	
	public String getName();
	
	public interface Config {
		String name();
		String factoryScope();
	}
}