package com.flow.shellx.api;

import javax.persistence.EntityManager;

public interface IEntityManagerProvider {

	
	EntityManager getEntityManager();
	
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
