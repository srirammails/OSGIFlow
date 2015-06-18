package com.flow.camel.osgi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.netty.http.NettyHttpComponent;
import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.scr.AbstractCamelRunner;
import org.apache.camel.spi.ComponentResolver;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentFactory;

import com.flow.camel.servlet.CamelServletComponent;

import com.flow.shellx.api.IConfigurationService;
import com.flow.shellx.api.IShellBusRouteFactoy;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;


@Component(immediate=true,provide=AbstractCamelRunner.class,properties={"name=shellrunner","camelContextId=shell-core","active=true"})
public class CamelApplication extends AbstractCamelRunner{
	
	
	public  static final String RUN_SCOPE_KEY ="runscope";
	
	BundleContext context;
	Map<String,String> props;
	
	String name;
	
	List<ComponentFactory> routeFactories = new CopyOnWriteArrayList<ComponentFactory>();
	Multimap<Object, Object> routesBuilders = Multimaps.synchronizedMultimap(ArrayListMultimap.create());
	
	IConfigurationService configurationService;
	
	@Reference(optional=false,dynamic=true, multiple=false)
	public void setConfigAdmin(IConfigurationService configsrv){
		configurationService = configsrv;
	}
	public void unsetConfigAdmin(IConfigurationService configsrv){
		configurationService=null;
	}
	
	@Reference(type='*',target = "(component.factory=roue-*)",multiple=true,dynamic=true)
	public void setRouteBuilderFactory(ComponentFactory routesfactory){
		routeFactories.add(routesfactory);
		loadRouteBuilders();
	}
	public void unsetRouteBuilderFactory(ComponentFactory routesfactory){
		synchronized (routesBuilders) {
			routeFactories.remove(routesfactory);
			routesBuilders.removeAll(routesfactory);
			loadRouteBuilders();
		}
	}
	
	@Reference(service=ComponentResolver.class, dynamic=true,multiple=true)
	public void setComponentResolver(ServiceReference compresref){
	    gotCamelComponent(compresref);
	}
	public void unsetComponentResolver(ServiceReference compresref){
		lostCamelComponent(compresref);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected synchronized List<RoutesBuilder> getRouteBuilders() {
		ArrayList<RoutesBuilder> routeBuilders=  new ArrayList<RoutesBuilder>((Collection<? extends RoutesBuilder>) routesBuilders.values());
        return routeBuilders;
	}
	
	@Activate
	@Modified
	public synchronized void init(BundleContext context,Map<String,String> props){
		try {
			name= props.get("name");
			this.context =context;
			this.props = props;
			activate(context, props);
			loadRouteBuilders();
			super.context.addComponent(CamelServletComponent.COMPONENT_NAME, new CamelServletComponent());
			super.context.addComponent("netty-http", new NettyHttpComponent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadRouteBuilders() {
		 try{
			if(this.name !=null){
				super.context.getRoutes().clear();
				for(ComponentFactory factory: routeFactories){
					
					List<Dictionary<String, String>> configs = configurationService.getScopedConfigurations(RUN_SCOPE_KEY,this.name);
					for(Dictionary<String, String> props:configs){
						
						IShellBusRouteFactoy factoryInstance = (IShellBusRouteFactoy)factory.newInstance(props).getInstance();
				
						RouteBuilder routebuilder = factoryInstance.getRouteBuilder();
						RoutesDefinition routedef = factoryInstance.getRouteDefinition(super.context);
						
						if(routebuilder!=null){
							super.context.addRoutes(configure(super.context,routebuilder, log));
							routesBuilders.put(factory, routebuilder);
						}
						if(routedef!=null){
							super.context.addRouteDefinitions(routedef.getRoutes());
							routesBuilders.put(factory, routedef);
						}
					}
				}
			}
		 }catch(Exception ex){
			 ex.printStackTrace();
		 }
	}

}
