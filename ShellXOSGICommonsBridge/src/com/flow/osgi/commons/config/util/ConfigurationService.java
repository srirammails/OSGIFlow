package com.flow.osgi.commons.config.util;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.osgi.service.cm.ConfigurationAdmin;

import com.flow.model.osgi.commons.ConfigEntity;
import com.flow.osgi.commons.config.dao.ConfigEntityDao;
import com.flow.shellx.api.IConfigurationService;
import com.flow.shellx.api.IPersistenceProvider;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(immediate=true)
public class ConfigurationService implements IConfigurationService {
	
	ConfigurationAdmin configAdmin;
	IPersistenceProvider jdoPersitancePovider;

	
	@Reference(optional=false,dynamic=true, multiple=false)
	public void setConfigAdmin(ConfigurationAdmin configadmin){
		configAdmin = configadmin;
	}
	public void unsetConfigAdmin(ConfigurationAdmin configadmin){
		configAdmin=null;
	}
	
	@Reference(optional=false,dynamic=true, multiple=false)
	public void setDBProvider(IPersistenceProvider dbprovider){
		jdoPersitancePovider = dbprovider;
	}
	public void unsetDBProvider(IPersistenceProvider dbprovider){
		jdoPersitancePovider=null;
	}
	/* (non-Javadoc)
	 * @see com.flow.osgi.commons.config.util.IConfigurationService#getScopedConfigurations(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Dictionary<String, String>> getScopedConfigurations(String scopename, String scopevalue) {
		
		List<Dictionary<String, String>> props = null;
		
		ConfigEntityDao cfgDao = new ConfigEntityDao(this.jdoPersitancePovider);
		
		try {
			 List<ConfigEntity> cfgs = cfgDao.getConigsByProperty(scopename, scopevalue);
			 if(null!= cfgs && cfgs.size()>0){
				 props = new ArrayList<Dictionary<String, String>>();
				 for(ConfigEntity cfg:cfgs){
					 props.add(ConfigurationUtil.converToMap(cfg));
				 }
			 }
			
		} catch (Exception e) {}
		
		 return props;
	}
	
}
