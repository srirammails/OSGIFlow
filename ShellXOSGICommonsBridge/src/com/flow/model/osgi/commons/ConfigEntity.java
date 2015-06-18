package com.flow.model.osgi.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.ElementCollection;
import javax.persistence.Id;

@PersistenceCapable
@FetchGroup(name="allfields", members={@Persistent(name="configValues")})
public class ConfigEntity {

	@Id
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	public long id;
	
	@PrimaryKey
	public String name;
	public String descripion;
	public String factoryId;
	public String serviceProcessId;
	
	
	public long configCreatedBy;
	public Date configCreatedFrom;
	
	@Element(column="config",dependent="true")
	public List<ConfigValue> configValues;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescripion() {
		return descripion;
	}

	public void setDescripion(String descripion) {
		this.descripion = descripion;
	}

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	public String getServiceProcessId() {
		return serviceProcessId;
	}

	public void setServiceProcessId(String serviceProcessId) {
		this.serviceProcessId = serviceProcessId;
	}

	public long getConfigCreatedBy() {
		return configCreatedBy;
	}

	public void setConfigCreatedBy(long configCreatedBy) {
		this.configCreatedBy = configCreatedBy;
	}

	public Date getConfigCreatedFrom() {
		return configCreatedFrom;
	}

	public void setConfigCreatedFrom(Date configCreatedFrom) {
		this.configCreatedFrom = configCreatedFrom;
	}

	public List<ConfigValue> getConfigValues() {
		return configValues;
	}

	public void setConfigValues(List<ConfigValue> configValues) {
		this.configValues = configValues;
	}

	public long getId() {
		return id;
	}
	

	
	
	
	
}
