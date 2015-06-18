package com.flow.osgi.commons.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.persistence.Entity;

import org.apache.felix.cm.PersistenceManager;
import org.osgi.framework.Constants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.flow.model.osgi.commons.ConfigValue;
import com.flow.model.osgi.commons.ConfigEntity;
import com.flow.osgi.commons.config.dao.ConfigEntityDao;
import com.flow.osgi.commons.config.util.ConfigurationUtil;
import com.flow.shellx.api.IPersistenceProvider;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;

@Component(immediate=true, properties={Constants.SERVICE_RANKING+":Integer=10"})
public class ConfigAdminDBService implements PersistenceManager {
	
	
	ConfigurationAdmin configAdmin;
	
	IPersistenceProvider jdoPersitancePovider;
	PersistenceManager filePersistanceManager;

	ConcurrentHashMap<String,Dictionary<String, String>> fileConfigs = new ConcurrentHashMap<String,Dictionary<String, String>>();
	
	public ConfigAdminDBService(){
		filePersistanceManager = new FilePersistenceManager("");
	}
	
	@Reference(optional=false,dynamic=true, multiple=false)
	public void setConfigAdmin(ConfigurationAdmin configadmin){
		configAdmin = configadmin;
	}
	public void unsetConfigAdmin(ConfigurationAdmin configadmin){
		configAdmin=null;
	}
	
	@Reference(optional=true,dynamic=true, multiple=false)
	public void setDBProvider(IPersistenceProvider dbprovider){
		jdoPersitancePovider = dbprovider;
		persistConfigsToDb();
		loadConfigsFromDb();
	}
	public void unsetDBProvider(IPersistenceProvider dbprovider){
		jdoPersitancePovider=null;
	}
	
	
	@Activate
	@Modified
	public void init(){
		if(this.jdoPersitancePovider!=null){
			persistConfigsToDb();
			loadConfigsFromDb();
		}
	}
	
	@Override
	public void delete(String pid) throws IOException {
		//TODO: implement delete query
	}

	@Override
	public boolean exists(String pid) {
		return false;
	}

	@Override
	public Enumeration getDictionaries() throws IOException {
		Enumeration configs =this.filePersistanceManager.getDictionaries();
		return  configs;
	
	}

	@Override
	public Dictionary load(String pid) throws IOException {
		return null;
	}

	@Override
	public void store(String pid, Dictionary conf) throws IOException {
		//Check if it is new empty template configuration or real file configuration
		if(conf.get(ConfigurationUtil.FELIX_NEW_CONFIG_KEY)==null){
			fileConfigs.put(pid,conf);
			ConfigEntity cfg = ConfigurationUtil.converToConfig(pid, fileConfigs.get(pid));
			try{
				if(this.jdoPersitancePovider!=null)
					saveConfiguration(cfg);
			}catch(Exception ex){
				throw new IOException(ex.getMessage());
			}
		}
		
	}

	private void persistConfigsToDb(){
		for(Entry<String, Dictionary<String, String>> entry:this.fileConfigs.entrySet()){
			ConfigEntity cfg =  ConfigurationUtil.converToConfig(entry.getKey(),entry.getValue());
			try{
				saveConfiguration(cfg);
			}catch(Exception ex){
				//all ready logged
			}
		}
	}
	
	 private void saveConfiguration(final ConfigEntity cfg) throws Exception {
		ConfigEntityDao cfgDao = new ConfigEntityDao(this.jdoPersitancePovider);
		cfgDao.saveOrUpdate(cfg);
	 }
	 
	 private void loadConfigsFromDb(){
		 javax.jdo.PersistenceManager pm = jdoPersitancePovider.getPersistenceManager();
		 try{
			 Query query = pm.newQuery(ConfigEntity.class);
			 List<ConfigEntity> configs = (List<ConfigEntity>) query.execute();
			 
			 if(configs!= null){
				for(ConfigEntity config : configs){
					
					Configuration configuration =null;
					
					Dictionary<String, String> props = ConfigurationUtil.converToMap(config);
					
					if(config.getFactoryId()!=null)
						configuration = configAdmin.createFactoryConfiguration(config.getFactoryId());
					else
						configuration = this.configAdmin.getConfiguration(config.getServiceProcessId());
					
					configuration.update(props);
				}
			 }
			 
		 }catch(Exception ex){
			 //TODO: Log it 
		 }finally{
			 if(null!=pm & !pm.isClosed())
					pm.close();
		}
	 }

	public class EmptyEnumeration implements Enumeration
	 {

		@Override
		public boolean hasMoreElements() {
			return false;
		}

		@Override
		public Object nextElement() {
			return null;
		}
		 
	 }
}
