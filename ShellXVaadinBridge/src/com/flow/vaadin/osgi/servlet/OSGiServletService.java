package com.flow.vaadin.osgi.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;

import com.vaadin.server.ConnectorResourceHandler;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.ServiceException;
import com.vaadin.server.UnsupportedBrowserHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.communication.FileUploadHandler;
import com.vaadin.server.communication.HeartbeatHandler;
import com.vaadin.server.communication.PublishedFileHandler;
import com.vaadin.server.communication.PushRequestHandler;
import com.vaadin.server.communication.ServletBootstrapHandler;
import com.vaadin.server.communication.ServletUIInitHandler;
import com.vaadin.server.communication.SessionRequestHandler;
import com.vaadin.server.communication.UidlRequestHandler;

@SuppressWarnings("serial")
public class OSGiServletService extends VaadinServletService {

	private final IVaadinSessionFactory factory;

	public OSGiServletService(VaadinServlet servlet,
			DeploymentConfiguration deploymentConfiguration,
			IVaadinSessionFactory factory) throws ServiceException {
		super(servlet, deploymentConfiguration);
		this.factory = factory;
	}

	@Override
	public String getConfiguredWidgetset(VaadinRequest request) {
		return super.getConfiguredWidgetset(request);
	}

	@Override
	protected VaadinSession createVaadinSession(VaadinRequest request)
			throws ServiceException {
		VaadinSession session = factory.createSession(request, getCurrentServletRequest());
		return session;
	}

	@Override
	protected List<RequestHandler> createRequestHandlers()
			throws ServiceException {
		ArrayList<RequestHandler> handlers = new ArrayList<RequestHandler>();
	    handlers.add(new SessionRequestHandler());
	    handlers.add(new PublishedFileHandler());
	    handlers.add(new HeartbeatHandler());
	    handlers.add(new FileUploadHandler());
	    handlers.add(new UidlRequestHandler());
	    handlers.add(new UnsupportedBrowserHandler());
	    handlers.add(new ConnectorResourceHandler());
	    handlers.add(0, new ServletBootstrapHandler());
	    handlers.add(new OSGIServletInitHandler());
		return handlers;
	}

	/**
	 * Creates new instances of vaadin sessions.
	 */
	public interface IVaadinSessionFactory {
		/**
		 * Returns a new instance of a vaadin session.
		 * 
		 * @param request
		 * @param httpServletRequest
		 * 
		 * @return
		 */
		VaadinSession createSession(VaadinRequest request,
				HttpServletRequest httpServletRequest);
	}
}
