package com.flow.camel.osgi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RoutesDefinition;
import org.osgi.framework.BundleContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Modified;

import com.flow.shellx.api.IShellBusRouteFactoy;



@Component(factory="roue-def-shell-factory", name="shellapproutedef", properties={"runscope=shellrunfactory"})
public class ShellAppRouteDefinition implements IShellBusRouteFactoy{
	

	public final String FACTORY_ID= "roue-def-shell-factory";
	public AtomicReference<String> runscope = new AtomicReference<String>();

	
	InputStream xmlFileStream;
	
	List<Dictionary<String, String>> configs = new CopyOnWriteArrayList<Dictionary<String, String>>();
	
	@Activate
	@Modified
	public void activate(BundleContext context,Map<String,String> props){
		//TODO: load xml file from config location
	}
	
	@Override
	public String getRunnerScope() {
		return runscope.get();
	}

	@Override
	public RouteBuilder getRouteBuilder() {
		return null;
	}

	@Override
	public RoutesDefinition getRouteDefinition(final CamelContext context) {
		try {
			
			if(xmlFileStream!=null)
				return context.loadRoutesDefinition(xmlFileStream);
			else
				return null;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	

}
