package com.flow.camunda.osgi;


import org.camunda.bpm.application.PostDeploy;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.extension.osgi.application.OSGiProcessApplication;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.osgi.framework.Bundle;
import org.osgi.service.blueprint.container.BlueprintContainer;



@org.camunda.bpm.application.ProcessApplication("TestProApp")
public class ProcessApplication extends OSGiProcessApplication{

	public ProcessApplication(Bundle bundle,BlueprintContainer blueprintContainer) {
		super(bundle, blueprintContainer);
	}
	
	 @PostDeploy
	  public void sayHello(ProcessEngine processEngine) {
		 processEngine.getRuntimeService().activateProcessInstanceByProcessDefinitionKey("TestProApp");
	 }

	  @Override
	  public void createDeployment(String processArchiveName, DeploymentBuilder deploymentBuilder) {
	    BpmnModelInstance bpmnModelInstance = Bpmn.createExecutableProcess("foo")
	      .startEvent()
	      .scriptTask()
	        .scriptText("println('Yoyo');")
	        .scriptFormat("Javascript")
	      .endEvent()
	    .done();

	    deploymentBuilder.addModelInstance("process.bpmn", bpmnModelInstance);
	  }

}
