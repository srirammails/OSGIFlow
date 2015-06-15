package com.flow.camel.osgi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.component.netty.NettyComponent;
import org.apache.camel.component.netty.http.NettyHttpComponent;
import org.apache.camel.scr.AbstractCamelRunner;
import org.apache.camel.spi.ComponentResolver;
//import org.camunda.bpm.camel.component.CamundaBpmComponent;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.flow.camel.servlet.CamelServletComponent;
//import com.flow.camunda.osgi.api.ICamundaService;

//import com.flow.vaadin.osgi.api.IVaadinApplication;





import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;


@Component(immediate=true,provide=AbstractCamelRunner.class,
			properties={"camelContextId=shell-core",
						"active=true"})
public class BaseCamleService extends AbstractCamelRunner{
	
	List<RoutesBuilder> routesBuilders = new ArrayList<RoutesBuilder>();

	
	@Reference(service=ComponentResolver.class, dynamic=true,multiple=true)
	public void setComponentResolver(ServiceReference compresref){
	    gotCamelComponent(compresref);
	}
	public void unsetComponentResolver(ServiceReference compresref){
		lostCamelComponent(compresref);
	}
	
	@Override
	protected synchronized List<RoutesBuilder> getRouteBuilders() {
		routesBuilders.add(new BaseServletRoute());
        return routesBuilders;
	}
	
	@Activate
	public synchronized void init(BundleContext context){
		try {
			Map<String, String> prop = new HashMap<String,String>();
			prop.put("camelContextId", "camel-scr-example");
			prop.put("active","true");
			activate(context, prop);
			this.context.addComponent(CamelServletComponent.COMPONENT_NAME, new CamelServletComponent());
			this.context.addComponent("netty-http", new NettyHttpComponent());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
