package com.flow.jdo.osgi;

import java.util.HashMap;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.log.LogService;






import com.flow.shellx.api.IPersistenceProvider;
import com.flow.shellx.proxy.LogServiceProxy;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;


@Component(immediate=true,provide={IPersistenceProvider.class},designate=IPersistenceProvider.Config.class)
public class JDOProviderService implements IPersistenceProvider{
	
	Config config;
	
	LogService logger = new LogServiceProxy()  ;
	
	BundleContext bundleContext;
	
	String modelBundle;
	String modelClassPath;
	ClassLoader modelClassLoader;
	
	PersistenceManagerFactory persistenceFactory;
	
	
	
	
	@Reference(optional=true)
	public void setLogService(LogService logservice){
		
		logger=logservice;
		
		if(logger!=null)
			logger.log(LogService.LOG_INFO, "Logservice acquired");
	}

	@Activate
	@Modified
	protected void init(BundleContext bundlecontext,Map<String,Object> properties){
		
		this.bundleContext =bundlecontext;
		
		config = Configurable.createConfigurable(Config.class,properties);
		
		modelBundle = config.modelBundle();
		modelClassPath = config.modelClassPath();
		modelClassLoader = getModelClassLoader();
		
		ClassLoader datanukeClassLoader = getClassLoader();
		
		Map<String, Object> props = new HashMap<String, Object>();
        props.put("javax.jdo.PersistenceManagerFactoryClass", "org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
        props.put("datanucleus.plugin.pluginRegistryClassName", "org.datanucleus.plugin.OSGiPluginRegistry");
        props.put("datanucleus.primaryClassLoader", modelClassLoader);
        props.put("javax.jdo.option.ConnectionURL", config.ConnectionURL());
        props.put("javax.jdo.option.ConnectionDriverName", config.ConnectionDriverName());
        props.put("javax.jdo.option.ConnectionUserName", config.ConnectionUserName());
        props.put("javax.jdo.option.ConnectionPassword", config.ConnectionPassword());
        props.put("datanucleus.autoCreateSchema", config.autoCreateSchema());
        props.put("datanucleus.autoCreateColumns", config.autoCreateColumns());
		
		try{
			
			persistenceFactory = JDOHelper.getPersistenceManagerFactory(props, datanukeClassLoader);
			
			logger.log(LogService.LOG_INFO,"JDO:OSGi.init PMF initialized");
			
		}catch(Exception ex){
			logger.log(bundlecontext.getServiceReference(this.getClass()), LogService.LOG_ERROR, 
					ex.getMessage(),ex);
			throw ex;
		}
	}
	
	@Deactivate
	protected void shutdown(){
		 
		logger.log(LogService.LOG_INFO,"JDO:OSGi.stop Closing PMF");
        
		if (persistenceFactory != null && !persistenceFactory.isClosed())
        {
        	persistenceFactory.close();
        }
	}
	
	
	@Override
	public PersistenceManager getPersistenceManager() {
		if (persistenceFactory != null)
			return persistenceFactory.getPersistenceManager();
		else
			return null;
		
	}

	private ClassLoader getModelClassLoader() {
		ClassLoader classloader = null;
		Bundle[] bundles = bundleContext.getBundles();

		for (int x = 0; x < bundles.length; x++) {
			try{
				if (bundles[x].getHeaders().get("DataNucleus-Model")!=null) {
					classloader = bundles[x].adapt(BundleWiring.class).getClassLoader();
					break;
				}
			}catch(NullPointerException nex){
				
			}
			
		}

		return classloader;
	}
	
	private ClassLoader getClassLoader() {
		ClassLoader classloader = null;
		Bundle[] bundles = bundleContext.getBundles();

		for (int x = 0; x < bundles.length; x++) {
			if (bundles[x].getSymbolicName().startsWith("org.datanucleus.api.jdo")) {
				try {
					classloader = bundles[x]
							.loadClass("org.datanucleus.api.jdo.JDOPersistenceManagerFactory")
							.getClassLoader();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
			}
		}

		return classloader;
	}
	 
	 
	
	
}
