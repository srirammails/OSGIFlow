package com.flow.shellx.api;

import javax.jdo.PersistenceManager;

public interface IPersistenceProvider {

	public PersistenceManager getPersistenceManager();
	
	
	 public interface Config{
		  String ConnectionURL();
		  String ConnectionDriverName();
		  String ConnectionUserName();
		  String ConnectionPassword();
		  String autoCreateSchema();
		  String autoCreateColumns();
		  String modelClassPath();
		  String modelBundle();
	 }
}
