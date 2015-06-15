package com.flow.camel.servlet;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.ServletException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(immediate=true)
public class CamelRootServlet extends CamelHttpTransportServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	HttpService httpService;
	
	@Reference(service=HttpService.class,dynamic=true, multiple=false, optional=false)
	public void setBPMService(HttpService service){
		httpService = service;
	}
	public void unsetBPMService(HttpService service){
		httpService = null;
	}
	
	
	@Activate
	public void init(BundleContext context) throws ServletException {
		//Important: This name will be used to match registry
		this.setServletName("camel");
		
		final Dictionary<String, String> initParams = new Hashtable<String, String>();
		initParams.put("matchOnUriPrefix", "true");
		initParams.put("servlet-name", "camel");
		initParams.put("osgi.web.contextpath","/");
        try {
        	httpService.registerServlet("/", this, initParams, httpService.createDefaultHttpContext());
        	
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
   
}
