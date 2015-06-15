//package com.flow.vaadin.osgi;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import org.osgi.framework.BundleActivator;
//import org.osgi.framework.BundleContext;
//import org.osgi.framework.InvalidSyntaxException;
//import org.osgi.framework.ServiceReference;
//import org.osgi.service.log.LogService;
//
//import com.flow.shellx.api.IShellUIApplication;
//
//public class Activator implements BundleActivator {
//
//	public static final String BUNDLE_NAME = "com.flow.vaadin.osgi";
//
//	private LogService logService;
//
//	private static BundleContext bundleContext;
//
//	public static BundleContext getBundleContext() {
//		return bundleContext;
//	}
//
//	protected void bindLogService(BundleContext context) {
//		ServiceReference<LogService> ref = context
//				.getServiceReference(LogService.class);
//		logService = context.getService(ref);
//
//		logService.log(LogService.LOG_DEBUG, "Vaaidn Bunlde Binded LogService.");
//	}
//
//	/**
//	 * Returns the vaadin web application with the given name.
//	 * 
//	 * @param name
//	 * @return
//	 */
//	protected static Collection<IShellUIApplication> getVaadinWebApplications() {
//		List<IShellUIApplication> services = new ArrayList<IShellUIApplication>();
//		try {
//			for (ServiceReference<IShellUIApplication> reference : bundleContext
//					.getServiceReferences(IShellUIApplication.class, null)) {
//				services.add(bundleContext.getService(reference));
//			}
//		} catch (InvalidSyntaxException e) {
//			throw new RuntimeException(e);
//		}
//		return services;
//	}
//
//	/**
//	 * Returns the vaadin web application with the given name.
//	 * 
//	 * @param name
//	 * @return
//	 */
//	public static IShellUIApplication getVaadinWebApplication(String name) {
//		try {
//			Collection<ServiceReference<IShellUIApplication>> refs = bundleContext
//					.getServiceReferences(IShellUIApplication.class,
//							"(component.name=" + name + ")");
//			if (refs.size() > 0) {
//				ServiceReference<IShellUIApplication> ref = refs.iterator()
//						.next();
//				return bundleContext.getService(ref);
//			}
//		} catch (InvalidSyntaxException e) {
//			throw new RuntimeException(e);
//		}
//
//		return null;
//	}
//
//	protected LogService getLogService() {
//		return logService;
//	}
//
//	@Override
//	public void start(BundleContext context) throws Exception {
//
//		bundleContext = context;
//
//		// bind the log service
//		bindLogService(context);
//
//	}
//
//	@Override
//	public void stop(BundleContext context) throws Exception {
//
//		logService = null;
//	}
//
//}
