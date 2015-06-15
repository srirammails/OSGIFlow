package com.flow.shellx.api;

public interface IShellView {
	
	String getName();
	String getUIScope();
	String getNavToken();
	void setComponent(IShellComponent component);
	void unsetComponent(IShellComponent component);
	void bindComponent(Object component, String location);
	boolean checkClearence();
	
	public interface Config{
	
		String name();
		String uiScope();
		String navToken();
		String template();
		Boolean issecure();

	}

	
}
