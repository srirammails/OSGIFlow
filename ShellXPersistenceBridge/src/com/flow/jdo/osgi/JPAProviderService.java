package com.flow.jdo.osgi;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.datanucleus.api.jpa.PersistenceProviderImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;

import com.flow.shellx.api.IEntityManagerProvider;
import com.flow.shellx.proxy.LogServiceProxy;

@Component(immediate=true,provide={IEntityManagerProvider.class},designate=IEntityManagerProvider.Config.class)
public class JPAProviderService implements IEntityManagerProvider{

Config config;
	
	LogService logger = new LogServiceProxy()  ;
	BundleContext bundleContext;
	
	String modelBundle;
	String modelClassPath;
	ClassLoader modelClassLoader;
	
	EntityManagerFactory persistenceFactory;
	
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
		Map<String, Object> overrideProps = new HashMap<String, Object>();
        overrideProps.put("datanucleus.plugin.pluginRegistryClassName", "org.datanucleus.plugin.OSGiPluginRegistry");
        overrideProps.put("datanucleus.primaryClassLoader", modelClassLoader);
        overrideProps.put("datanucleus.schema.autoCreateSchema", config.autoCreateSchema());
        overrideProps.put("datanucleus.autoCreateColumns", config.autoCreateColumns());
        overrideProps.put("javax.persistence.jdbc.url", config.ConnectionURL());
        overrideProps.put("javax.persistence.jdbc.driver", config.ConnectionDriverName());
        overrideProps.put("javax.persistence.jdbc.user", config.ConnectionUserName());
        overrideProps.put("javax.persistence.jdbc.password", config.ConnectionPassword());


		try{
					
			PersistenceProviderImpl imp = new PersistenceProviderImpl();
			
			ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(modelClassLoader);
			
			persistenceFactory = imp.createEntityManagerFactory("PJM", overrideProps);
			
			Thread.currentThread().setContextClassLoader(currentClassLoader);
			
			EntityManager em=  persistenceFactory.createEntityManager();
			logger.log(LogService.LOG_INFO,"JDO:OSGi.init PMF initialized");
			
		}catch(Exception ex){
			logger.log(bundlecontext.getServiceReference(this.getClass()), LogService.LOG_ERROR, 
					ex.getMessage(),ex);
		}
	}
	
	@Deactivate
	protected void shutdown(){
		 
		logger.log(LogService.LOG_INFO,"JDO:OSGi.stop Closing PMF");
        
		if (persistenceFactory != null && !persistenceFactory.isOpen())
        {
        	persistenceFactory.close();
        }
	}
	
	
	@Override
	public EntityManager getEntityManager() {
		return persistenceFactory.createEntityManager();
	}
	
	
	private ClassLoader getModelClassLoader() {
		ClassLoader classloader = null;
		Bundle[] bundles = bundleContext.getBundles();

		for (int x = 0; x < bundles.length; x++) {
			if (bundles[x].getSymbolicName().startsWith(modelBundle)) {
				try {
					classloader = bundles[x]
							.loadClass(modelClassPath).getClassLoader();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				break;
			}
		}

		return classloader;
	}
	
	private ClassLoader getClassLoader() {
		ClassLoader classloader = null;
		Bundle[] bundles = bundleContext.getBundles();

		for (int x = 0; x < bundles.length; x++) {
			if (bundles[x].getSymbolicName().startsWith("org.datanucleus.api.jpa")) {
				try {
					classloader = bundles[x]
							.loadClass("org.datanucleus.api.jpa.PersistenceProviderImpl")
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
