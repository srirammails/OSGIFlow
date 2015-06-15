package com.flow.camunda.osgi;

import java.util.Map;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


public class ProcessRequestDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String businesskey= execution.getBusinessKey();
		String instanceid =execution.getActivityInstanceId();
		String activitiname = execution.getCurrentActivityName();
		Map<String, Object> variables =execution.getVariables();
		System.out.println("Test form BPMN");
		
		
	}

}
