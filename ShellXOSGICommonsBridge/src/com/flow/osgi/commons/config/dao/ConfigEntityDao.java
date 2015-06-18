package com.flow.osgi.commons.config.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Query;
import javax.jdo.Transaction;

import com.flow.model.osgi.commons.ConfigEntity;
import com.flow.model.osgi.commons.ConfigValue;
import com.flow.shellx.api.IPersistenceProvider;

public class ConfigEntityDao {

	private IPersistenceProvider jdoPersitancePovider;
	
	public ConfigEntityDao(IPersistenceProvider povider){
		this.jdoPersitancePovider =povider;
	}
	
	public void saveOrUpdate(ConfigEntity cfg) throws Exception{
		
		 if(jdoPersitancePovider!=null){
			 	javax.jdo.PersistenceManager pm = jdoPersitancePovider.getPersistenceManager();
				Transaction tr =null;
				try{
					tr = pm.currentTransaction();
					tr.begin();
					Query query =pm.newQuery(ConfigEntity.class);
					query.setFilter("name=='"+cfg.getName()+"'");
					List entities =  (List) query.execute();
					if(entities.size()<=0)
						pm.makePersistent(cfg);
					else
						((ConfigEntity) entities.get(0)).setConfigValues(cfg.getConfigValues());
					
					tr.commit();
				}catch(Exception ex){
					//Log this exception (We may need to send reason to user)
					tr.rollback();
					throw ex;
				}finally{
					if(tr.isActive())
						tr.rollback();
					if(null!=pm & !pm.isClosed())
						pm.close();
				}
		}else{
			throw new Exception("No valid data provider found");
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ConfigEntity> getConigsByProperty(String propName, String propValue) throws Exception{
			
			List<ConfigEntity> configs = null;
		
			if(jdoPersitancePovider!=null){
			 	javax.jdo.PersistenceManager pm = jdoPersitancePovider.getPersistenceManager();
				Transaction tr =null;
				try{
					pm.getFetchPlan().addGroup("allfields");
					Query query =pm.newQuery(ConfigEntity.class);
					query.setFilter("configValues.contains(cfgval) && (cfgval.key=='"+propName+"' || cfgval.value=='"+propValue +"')");
					List results = (List) query.execute();
					configs =  (List<ConfigEntity>) pm.detachCopyAll(results);
					return configs;
					
				}catch(Exception ex){
					throw ex;
				}finally{
					if(null!=pm & !pm.isClosed())
						pm.close();
				}
		}else{
			throw new Exception("No valid data provider found");
		}
	}
	
	
	
}
