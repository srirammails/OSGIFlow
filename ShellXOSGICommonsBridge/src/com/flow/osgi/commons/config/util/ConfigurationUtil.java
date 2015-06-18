package com.flow.osgi.commons.config.util;

import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.flow.model.osgi.commons.ConfigEntity;
import com.flow.model.osgi.commons.ConfigValue;

public class ConfigurationUtil {

	public static final String FELIX_NEW_CONFIG_KEY = "_felix_.cm.newConfiguration";
	
	public static ConfigEntity converToConfig(final String pid, final Dictionary<String, String> conf){
		 synchronized (conf) {
				ConfigEntity config = new ConfigEntity();
				config.setServiceProcessId(pid);
					
				    List<ConfigValue> configValues = new ArrayList<ConfigValue>();
					Enumeration<String> keys = conf.keys();
					
					while (keys.hasMoreElements()){
						String key =keys.nextElement();
						if(key.equals("name")){
							config.setName(conf.get(key));
						}else{
							ConfigValue cfgval = new ConfigValue(key,conf.get(key));
							configValues.add(cfgval);
						}
					}
					
					config.setConfigValues(configValues);
					return config;
		 }
	 
	}
	
	public static Dictionary<String, String> converToMap(final ConfigEntity config) {
		synchronized (config) {
			
			Dictionary<String, String> props = new Hashtable();
			
			props.put("name", config.getName());
			
			for(ConfigValue prop:config.getConfigValues()){
				props.put(prop.getKey(), prop.value);
			}
			
			return props;
		}
	}

}
