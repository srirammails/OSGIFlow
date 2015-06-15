
package com.flow.vaadin.osgi.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.flow.shellx.api.IShellUIApplication;
import com.flow.shellx.api.IShellUIProvider;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;

/**
 * Used to create instances of applications that have been registered with the
 * container via a component factory.
 * 
 */
public class VaadinOSGiServlet extends VaadinServlet {

	private static final long serialVersionUID = 1L;

	private IShellUIApplication webApplication;

	/**
	 * Default constructor.
	 * 
	 * @param webApplication
	 *            The vaadin web application.
	 */
	public VaadinOSGiServlet(IShellUIApplication webApplication) {
		this.webApplication = webApplication;
	}

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		try {
			super.init(servletConfig);
		} catch (ServletException e) {
			if (e.getMessage().equals(
					"Application not specified in servlet parameters")) {
				// Ignore if application is not specified to allow the same
				// servlet to be used for only push in portals
			} else {
				throw e;
			}
		}
	}

	@Override
	protected OSGiServletService createServletService(
			DeploymentConfiguration deploymentConfiguration)
			throws ServiceException {

		// create the servlet service initialized with the ui provider
		OSGiServletService service = new OSGiServletService(this,
				deploymentConfiguration,
				new OSGiServletService.IVaadinSessionFactory() {
					@Override
					public VaadinSession createSession(VaadinRequest request,
							HttpServletRequest httpServletRequest) {
						VaadinSession session = new VaadinSession(request.getService());
						for (IShellUIProvider provider : webApplication.getUiProviders()) {
							session.addUIProvider(provider.getUIProvider());
						}
						return session;
					}
				});
		service.init();
		return service;
	}

	@Override
	public void destroy() {
		super.destroy();
	}

}
