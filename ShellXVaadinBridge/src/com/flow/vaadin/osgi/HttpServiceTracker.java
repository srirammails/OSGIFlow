package com.flow.vaadin.osgi;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flow.shellx.api.IShellUIApplication;


public class HttpServiceTracker extends ServiceTracker<HttpService, HttpService> {

	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(HttpServiceTracker.class);
	
	 private IShellUIApplication webApplication =null;

	public HttpServiceTracker(BundleContext ctx, IShellUIApplication webapplication) throws InvalidSyntaxException {
		super(ctx,HttpService.class, null);
		this.webApplication  = webapplication;
	}

	@Override
	public HttpService addingService(ServiceReference<HttpService> reference) {
		HttpService service = super.addingService(reference);

		this.webApplication.httpServiceAdded(service);

		return service;
	}

	@Override
	public void removedService(ServiceReference<HttpService> reference,HttpService service) {
		super.removedService(reference, service);

		this.webApplication.httpServiceRemoved(service);
	}
}