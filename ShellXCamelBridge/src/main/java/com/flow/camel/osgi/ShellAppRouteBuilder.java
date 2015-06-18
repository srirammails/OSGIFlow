package com.flow.camel.osgi;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RoutesDefinition;
import org.osgi.framework.BundleContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Modified;

import com.flow.shellx.api.IShellBusRouteFactoy;

@Component(factory="roue-shell-factory", name="shellapproute", properties={"runscope=shellrunfactory"})
public class ShellAppRouteBuilder extends RouteBuilder implements IShellBusRouteFactoy {

	public final String FACTORY_ID= "roue-shell-factory";
	
	public AtomicReference<String> runscope = new AtomicReference<String>();
	
	List<Dictionary<String, String>> configs = new CopyOnWriteArrayList<Dictionary<String, String>>();

	Map<String,String> configProps;
	
	@Override
	public void configure() throws Exception {
		
		from(configProps.get("from"))
		.routeId(configProps.get("routeid"))
		.process(new Processor() {
			
			@Override
			public void process(Exchange exc) throws Exception {
				exc.getOut().setBody("test message");
			}
		})
		.end();
	}
	
	@Activate
	@Modified
	public synchronized void init(BundleContext context,Map<String,String> props){
		runscope.set(props.get("runscope"));
		this.configProps =props;
	}

	@Override
	public String getRunnerScope() {
		return runscope.get();
	}

	
	@Override
	public RouteBuilder getRouteBuilder() {
		return this;
	}

	@Override
	public RoutesDefinition getRouteDefinition(final CamelContext context) {
		return null;
	}
	
}



