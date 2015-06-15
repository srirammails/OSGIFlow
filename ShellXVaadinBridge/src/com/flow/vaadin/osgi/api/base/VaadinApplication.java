package com.flow.vaadin.osgi.api.base;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;

import com.flow.shellx.api.IShellUIApplication;
import com.flow.shellx.api.IShellUIProvider;
import com.flow.vaadin.osgi.HttpServiceTracker;
import com.flow.vaadin.osgi.api.AppException;
import com.flow.vaadin.osgi.api.VaadinApplicationConfig;
import com.flow.vaadin.osgi.api.VaadinConstants;
import com.flow.vaadin.osgi.api.VaadinStatusCodes;
import com.flow.vaadin.osgi.servlet.VaadinOSGiServlet;
import com.flow.vaadin.osgi.servlet.WebResourcesHttpContext;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(immediate=true,designateFactory=VaadinApplicationConfig.class)
public class VaadinApplication implements IShellUIApplication {

	private Logger logger = LoggerFactory.getLogger(VaadinApplication.class);
	
	VaadinApplicationConfig config;
	
	private  String resourceBase = "/VAADIN";

	private ComponentContext context;
	protected HttpService httpService;
	private HttpServiceTracker serviceTracker;

	protected String id;
	protected Integer status;
	protected AtomicReference<String> name = new AtomicReference<String>();
	protected String host;
	protected String path;
	protected String uiAlias;
	protected String widgetset;
	protected boolean productionMode;
	protected String httpApplication;

	protected boolean started;
	protected final Lock accessLock = new ReentrantLock();

	private VaadinOSGiServlet servlet;
	private WebResourcesHttpContext defaultContext;

	protected List<IShellUIProvider> uiProviders = new CopyOnWriteArrayList<IShellUIProvider>();	

	
	@Reference(type='*',target = "(component.factory=shelluiprovider)",name="shellapp")
	protected void setUIProvider(ComponentFactory factory,Map<String, Object> properties) {
		ComponentInstance compoentinstace = factory.newInstance(null);
		IShellUIProvider uiprovider = (IShellUIProvider)compoentinstace.getInstance();
		logger.info("UIProvider received with app name:"+uiprovider.getName());		
		uiProviders.add(uiprovider);
	}
	protected void unsetUIProvider(ComponentFactory factory){
		uiProviders.set(0, null);
	}
	
	
	@Activate
	protected void activate(ComponentContext context,Map<String, Object> properties) {
		logger.info("VAADIN APPLICATION STARTED::"+getName());
		
		this.context = context;
		this.id = UUID.randomUUID().toString();
		
		loadConfiguration(properties);
		start();
		modified(context, properties);
		
	}
	
	@Deactivate
	protected void deactivate(ComponentContext context,
			Map<String, Object> properties) {
		destroy();
		this.context = null;
		logger.info("VAADIN APPLICATION STOPPED"+getName());
	}

	@Modified
	protected void modified(ComponentContext context,Map<String, Object> properties) {
		this.context =context;
		loadConfiguration(properties);
	}
	
	private void loadConfiguration(Map<String, Object> properties){
		try{
			accessLock.lock();
			config = Configurable.createConfigurable(VaadinApplicationConfig.class, properties);
			this.name.set(config.name());
			this.host = config.hostName();
			this.path = config.path();
			this.uiAlias = config.uiAlias();
			
			this.resourceBase = config.resourceBase();
			this.widgetset = config.widgetset();
			this.productionMode = config.productionMode();
			
		}finally{
			accessLock.unlock();
			logger.info("Config loaded with name:"+this.name.get());
		}
	}
	
	public void destroy() {
		try {
			accessLock.lock();
			if (isStarted()) {
				stop();
			}
			uiProviders = null;
		} finally {
			accessLock.unlock();
		}
	}
	
	
	@Override
	public Integer getStatus() {
		return status != null ? status : 0;
	}
	@Override
	public String getId() {
		return id;
	}

	
	public String getName() {
		return name.get();
	}
	protected void setName(String name) {
		String value = isStringValid(name) ? name: VaadinConstants.DEFAULT_APPLICATION_NAME;
		this.name.set(value);
	}

	@Override
	public String getUIAlias() {
		return uiAlias;
	}
	protected void setUIAlias(String uiAlias) {
		this.uiAlias = isStringValid(uiAlias) ? uiAlias
				: VaadinConstants.DEFAULT_UI_ALIAS;
	}

	@Override
	public String getHttpApplication() {
		return httpApplication;
	}
	protected void setHttpApplication(String httpApplication) {
		this.httpApplication = isStringValid(httpApplication) ? httpApplication
				: "";
	}

	@Override
	public String getWidgetSetName() {
		return widgetset;
	}	
	protected void setWidgetSetName(String widgetset) {
		//this.widgetset = isStringValid(widgetset) ? widgetset : "";
	}

	@Override
	public boolean isProductionMode() {
		return productionMode;
	}
	protected void setProductionMode(boolean productionMode) {
		this.productionMode = productionMode;
	}

	
	private boolean isStringValid(String value) {
		return value != null && !value.trim().equals("");
	}

	public boolean isStarted() {
		return started;
	}

	public boolean isDeployed() {
		return httpService != null;
	}

	
	public void start() {
		if (started) {
			return;
		}
		accessLock.lock();
		try {
			try {
				serviceTracker = new HttpServiceTracker(context.getBundleContext(), this);
				serviceTracker.open();
			} catch (InvalidSyntaxException e) {
				//LOGGER.error("{}", e);
				setStatus(VaadinStatusCodes.createHttpServiceTracker(e));

				// stop the application
				stop(true);
				return;
			}

			// add all yet existing handler from the tracker
			HttpService httpService = serviceTracker.getService();
			if (httpService != null) {
				try {
					setHttpService(httpService);
				} catch (AppException e) {
//					LOGGER.error(String
//							.format("Stopping vaadin application %s since setting http service caused a problem.",
//									getName()));
					setStatus(VaadinStatusCodes.createSettingHttpService(e));

					// stop the application
					stop(true);
					return;
				}
			}

			started = true;
		} finally {
			accessLock.unlock();
		}

		setStatus(VaadinStatusCodes.OK_STATUS);
	}

	/**
	 * Is called by the service tracker to add a new http service.
	 * 
	 * @param httpService
	 */
	public void httpServiceAdded(HttpService httpService) {
		if (!isStarted()) {
			return;
		}

		// ensure that only one threads manipulates the contents
		accessLock.lock();
		try {
			setHttpService(httpService);
		} catch (AppException e) {
//			LOGGER.error(String
//					.format("Stopping vaadin application %s since setting http service caused a problem.",
//							getName()));
			setStatus(VaadinStatusCodes.createSettingHttpService(e));

			// stop the application
			stop();
			return;

		} finally {
			accessLock.unlock();
		}

		setStatus(VaadinStatusCodes.OK_STATUS);
	}

	/**
	 * Is called by the service tracker to remove a registered http service.
	 * 
	 * @param httpService
	 */
	public void httpServiceRemoved(HttpService httpService) {
		if (!isStarted()) {
			return;
		}

		// ensure that only one threas manipulates the contents
		accessLock.lock();
		try {
			unsetHttpService(httpService);
		} finally {
			accessLock.unlock();
		}
	}

	protected void setHttpService(HttpService httpService)throws AppException {
		
		if (this.httpService != null) {
			//LOGGER.error("HttpService already present. Abort operation!");
			return;
		}

		this.httpService = httpService;

		registerWebArtifacts();
	}
	
	protected void registerWebArtifacts() throws AppException {
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties.put(VaadinConstants.EXTERNAL_PID, getId());
		properties.put(VaadinConstants.APPLICATION_PATH, this.path);
		properties.put(VaadinConstants.WIDGETSET, getWidgetSetName());
		properties.put("widgetset", getWidgetSetName());
		properties.put("productionMode", Boolean.toString(isProductionMode()));
   
		servlet = new VaadinOSGiServlet(this);
		
		defaultContext = new WebResourcesHttpContext(this.context.getBundleContext().getBundle());

		try {
			registerResource(httpService);
			httpService.registerServlet(path, servlet, properties,defaultContext);
		} catch (ServletException e) {
			//logger.error("{}", e);
			throw new AppException(e);
		} catch (NamespaceException e) {
			//LOGGER.error("{}", e);
			throw new AppException(e);
		}
	}

	public void registerResource(HttpService httpservice){
		try{
			httpService.registerResources(resourceBase, resourceBase,defaultContext);
			httpService.registerResources("_ah/start", resourceBase, defaultContext);
			httpService.registerResources("_ah/health", resourceBase, defaultContext);
		}catch(Exception ex){
			
		}
	}
	
	private void setStatus(Integer status) {
		this.status = status;

		if (status != null && status != VaadinStatusCodes.OK_STATUS) {
			//LOGGER.warn("Status was set to vaadin application {}: {}",getName(), status);
		}
	}

	protected void unregisterWebArtifacts() {
		try {
			if (uiAlias != null) {
				httpService.unregister(uiAlias);
			}
		} catch (Exception e) {
			// May throw exception if http service was stopped by
			// tracker.close()
			//LOGGER.info("{}", e.getMessage());
		} finally {
			uiAlias = null;// Load from config admin
			servlet = null;
		}

		try {
			httpService.unregister(resourceBase);
		} catch (Exception e) {
			//LOGGER.info("{}", e.getMessage());
		} finally {
		}
	}

	protected void unsetHttpService(HttpService httpService) {
		if (this.httpService == null) {
			return;
		}
		if (this.httpService != httpService) {
			return;
		}
		unregisterWebArtifacts();
		this.httpService = null;
	}

	public void stop() {
		stop(false);
	}

	protected void stop(boolean force) {
		if (!force && !started) {
			//LOGGER.debug("HttpApplication {} not started", getName());
			return;
		}

		accessLock.lock();
		try {
			if (serviceTracker != null) {
				serviceTracker.close();
				serviceTracker = null;
			}

		} finally {
			accessLock.unlock();
			started = false;
		}
	}

	private boolean isActive() {
		return context != null;
	}

	@Override
	public List<IShellUIProvider> getUiProviders() {
		return Collections.unmodifiableList(uiProviders);
	}
	
}
