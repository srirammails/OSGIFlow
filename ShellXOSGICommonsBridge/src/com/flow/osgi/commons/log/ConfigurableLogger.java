package com.flow.osgi.commons.log;

import java.util.Date;
import java.util.Map;

import org.joda.time.DateTime;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;

@Component(immediate=true,provide={LogListener.class},
				designate=ConfigurableLogger.Config.class)
public class ConfigurableLogger implements LogListener{
	
	Config config;
	
	LogReaderService logReader;
	LogService logger;
	
	
	@Reference(optional=false)
	public void setLogReader(LogReaderService logreader){
		logReader = logreader;
		logReader.addLogListener(this);
	}
	
	@Reference
	public void setLogService(LogService log){
		this.logger =log;
	}
	
	@Activate
	public void init(Map<String,Object> props){
		config = Configurable.createConfigurable(Config.class,props);
		logger.log(LogService.LOG_INFO, config.message());
	}
	
	@Override
	public void logged(LogEntry entry) {
		
		System.out.println(new DateTime(entry.getTime()).toString("H:m:s.SSS")+"-["+
						logLevel(entry.getLevel())+"]-"+entry.getBundle()+
						entry.getServiceReference()!=null?("-"+entry.getServiceReference()):"-"+entry.getMessage());
	}
	
	@Deactivate
	public void shutdown(){
		logReader.removeLogListener(this);
	}
	
	@Modified
	void modified( Map<String,Object> props) {
		init(props);
	}
	
	private String logLevel(int level){
		switch (level) {
		case 1:
			return "ERROR";
		case 2: 
			return "WARNING";
		case 3: 
			return "INFO";
		case 4: 
			return "DEBUG";
		default:
			return "";
		}
		
	}
	
	public interface Config{
		String message();
	}
}
