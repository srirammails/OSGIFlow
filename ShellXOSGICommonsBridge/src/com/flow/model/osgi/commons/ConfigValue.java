package com.flow.model.osgi.commons;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class ConfigValue {

	public String key;
	public String value;
	
	public ConfigValue(String propName, String propValue) {
		this.key = propName;
		this.value = propValue;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(this.key.equals(((ConfigValue)obj).key) &&this.value.equals(((ConfigValue)obj).value))
			return true;
		else			
			return false;
	}
	
}
