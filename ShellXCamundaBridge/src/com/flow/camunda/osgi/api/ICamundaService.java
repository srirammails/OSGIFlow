package com.flow.camunda.osgi.api;

import org.camunda.bpm.engine.ProcessEngine;

public interface ICamundaService {
	
	public ProcessEngine getProcessEngine();

}
