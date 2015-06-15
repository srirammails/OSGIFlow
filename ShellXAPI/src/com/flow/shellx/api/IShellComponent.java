package com.flow.shellx.api;

import java.util.List;

public interface IShellComponent {

	String getName();
	List<String> getViewScopes();
	String getLabelScope();
	String getTemplate();
	
	public interface Config{
		String name();
		String labelScope();
		String template();
		List<String> viewScopes();
	}

}
