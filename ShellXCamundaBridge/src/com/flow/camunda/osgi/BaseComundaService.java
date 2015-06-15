package com.flow.camunda.osgi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.osgi.service.jdbc.DataSourceFactory;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.extension.osgi.el.OSGiExpressionManager;
import org.camunda.bpm.extension.osgi.engine.ProcessEngineFactoryWithELResolver;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;

import com.flow.camunda.osgi.api.ICamundaService;
import com.flow.osgi.mysql.MysqlDataSourceFactory;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(immediate=false)
public class BaseComundaService implements ICamundaService{
	
	private BundleContext ctx;
	
	private ConfigurationAdmin configAdmin;

	private ProcessEngineFactoryWithELResolver factory;
	
	private ProcessEngine engine;
	
	private DataSource dataSource;
	
	
	@Reference
	public void setDataSource(DataSourceFactory datasourcefact){
		Properties props = new Properties();
		props.put("url", "jdbc:mysql://localhost:3306/pjm");
		props.put("user", "root");
		props.put("password", "D0llerD0ller$$");
		try {
			dataSource =  datasourcefact.createDataSource(props);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Reference(optional=true)
	public void setBundleConfigadmin(ConfigurationAdmin configadmin) throws SQLException{
		this.configAdmin= configadmin;
	}
	
	@Activate
	public void init(BundleContext context) throws IOException, InterruptedException{
		this.ctx = context;
		ProcessEngineConfiguration conf = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();// StandaloneProcessEngineConfiguration();
		conf.setDataSource(dataSource);
		conf.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE);
		factory = new ProcessEngineFactoryWithELResolver();
		factory.setBundle(context.getBundle());
		factory.setExpressionManager(new OSGiExpressionManager());
		factory.setProcessEngineConfiguration(conf);
		
//		try {
//			conf.setClassLoader(getClassLoader());
//		} finally {
//		}
//		
		factory.init();
	
	    engine = factory.getObject();
	    
	    RepositoryService service = engine.getRepositoryService();
		DeploymentBuilder builder = service.createDeployment(); 
		
		BpmnModelInstance bpmnModelInstance = Bpmn.createExecutableProcess("foo")
				 .startEvent()
			      .scriptTask()
			        .scriptText("println('Yoyo');")
			        .scriptFormat("Javascript")
			         .serviceTask()
			         .camundaClass("com.flow.camunda.osgi.ProcessRequestDelegate")
			      //   .serviceTask("${camel.sendTo('direct:vaadin')}")
			      .endEvent()
			    .done();

		builder.addModelInstance("process.bpmn", bpmnModelInstance);
		builder.deploy();
		//engine.getRuntimeService().startProcessInstanceByKey("foo");
  }
	
	private ClassLoader getClassLoader() {
		ClassLoader classloader = null;
		Bundle[] bundles = ctx.getBundles();

		for (int x = 0; x < bundles.length; x++) {
			if (bundles[x].getSymbolicName().startsWith(
					"org.datanucleus.api.jdo")) {
				try {
					classloader = bundles[x]
							.loadClass(
									"org.camunda.bpm.engine.ProcessEngineConfiguration")
							.getClassLoader();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
			}
		}

		return classloader;
	}
	
	@Override
	public ProcessEngine getProcessEngine(){
		return this.engine;
	}

}
