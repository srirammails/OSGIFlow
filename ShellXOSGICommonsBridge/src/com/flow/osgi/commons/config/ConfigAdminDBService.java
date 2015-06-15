package com.flow.osgi.commons.config;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.felix.cm.PersistenceManager;
import org.osgi.framework.Constants;

import com.flow.osgi.commons.config.FilePersistenceManager.DictionaryEnumeration;
import com.flow.shellx.api.IPersistenceProvider;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import aQute.libg.map.MAP;

@Component(immediate=true, properties={Constants.SERVICE_RANKING+":Integer=10"})
public class ConfigAdminDBService implements PersistenceManager {

	
	javax.jdo.PersistenceManager jdoPersitanceManager;
	
	PersistenceManager filePersistanceManager;
	
	public ConfigAdminDBService(){
		filePersistanceManager = new FilePersistenceManager("");
	}
	
	@Reference(optional=true,dynamic=true, multiple=false)
	public void setDBProvider(IPersistenceProvider dbprovider){
		jdoPersitanceManager = dbprovider.getPersistenceManager();
	}
	public void unsetDBProvider(IPersistenceProvider dbprovider){
		jdoPersitanceManager=null;
	}

	
	
	
	@Activate
	public void init(){
		if(this.jdoPersitanceManager!=null){
			
		}
		
	}
	
	@Override
	public void delete(String pid) throws IOException {
		
	}

	@Override
	public boolean exists(String pid) {
		return false;
	}

	@Override
	public Enumeration getDictionaries() throws IOException {
		if(jdoPersitanceManager==null){
			Enumeration c =this.filePersistanceManager.getDictionaries();
			return  c;
		}else{
			return null;
		}
	}

	@Override
	public Dictionary load(String pid) throws IOException {
		return null;
	}

	@Override
	public void store(String pid, Dictionary config) throws IOException {
		
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
