package com.flow.vaadin.osgi.api.base;


import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.component.ComponentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;

import com.flow.shellx.api.IShellUI;
import com.flow.shellx.api.IShellView;
import com.flow.vaadin.osgi.api.IDisposable;
import com.flow.vaadin.osgi.api.ShellUIConfig;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

@PreserveOnRefresh
@Component(factory="shelluifacory", name="shellui",properties={"uiproviderscope=shellapp"})
public  class ShellUI extends UI implements SessionDestroyListener, IShellUI{

	private static final long serialVersionUID = 1L;
	
	//Vaadin Specific
	protected int uiId = -1;
	private String embedId;
	
	ShellUIConfig config;

	List<Dictionary<String, String>> configs;
	
	AtomicReference<String> name = new AtomicReference<String>();
	AtomicReference<String> providerScope = new AtomicReference<String>();
	AtomicReference<String> themeName = new AtomicReference<String>();

	AtomicReference<ComponentFactory> viewfacory = new AtomicReference<ComponentFactory>();
	
	private Logger logger = LoggerFactory.getLogger(ShellUI.class);
	
	protected  Navigator uiNavigator;

	List<IShellView> views = new CopyOnWriteArrayList<IShellView>();
	List<UriFragmentChangedListener> listners = new CopyOnWriteArrayList<UriFragmentChangedListener>();
	
	
	public ShellUI(){
		super();
		
		configs = new ArrayList<Dictionary<String,String>>();
		Hashtable<String, String> homeuiconfig = new Hashtable<String,String>();
		homeuiconfig.put("name", "homeview");
		homeuiconfig.put("navToken", "");
		homeuiconfig.put("template", "home");
		configs.add((Dictionary<String, String>) homeuiconfig);
		
		Hashtable<String, String> forumuiconfig = new Hashtable<String,String>();
		forumuiconfig.put("name", "forumview");
		forumuiconfig.put("navToken", "forum");
		forumuiconfig.put("template", "forum");
		configs.add((Dictionary<String, String>) forumuiconfig);
		
		Hashtable<String, String> qnauiconfig = new Hashtable<String,String>();
		qnauiconfig.put("name", "problemsolverview");
		qnauiconfig.put("navToken", "problemsolver");
		qnauiconfig.put("template", "problem");
		configs.add((Dictionary<String, String>) qnauiconfig);
		
		Hashtable<String, String> htutoruiconfig = new Hashtable<String,String>();
		htutoruiconfig.put("name", "hometutoringview");
		htutoruiconfig.put("navToken", "home-tutoring");
		htutoruiconfig.put("template", "hometutoring");
		configs.add((Dictionary<String, String>) htutoruiconfig);
		
		Hashtable<String, String> atutoruiconfig = new Hashtable<String,String>();
		atutoruiconfig.put("name", "academytutoringview");
		atutoruiconfig.put("navToken", "academy-tutoring");
		atutoruiconfig.put("template", "academytutoring");
		configs.add((Dictionary<String, String>) atutoruiconfig);
		
		Hashtable<String, String> caluiconfig = new Hashtable<String,String>();
		caluiconfig.put("name", "calendarview");
		caluiconfig.put("navToken", "academic-calendar");
		caluiconfig.put("template", "academiccalendar");
		configs.add((Dictionary<String, String>) caluiconfig);
		
		Hashtable<String, String> contactuiconfig = new Hashtable<String,String>();
		contactuiconfig.put("name", "contactview");
		contactuiconfig.put("navToken", "contact");
		contactuiconfig.put("template", "contact");
		configs.add((Dictionary<String, String>) contactuiconfig);
		
		Hashtable<String, String> tutorsviewuiconfig = new Hashtable<String,String>();
		tutorsviewuiconfig.put("name", "tutorsview");
		tutorsviewuiconfig.put("navToken", "tutors");
		tutorsviewuiconfig.put("template", "tutors");
		configs.add((Dictionary<String, String>) tutorsviewuiconfig);
		
		Hashtable<String, String> loginviewconfig = new Hashtable<String,String>();
		loginviewconfig.put("name", "loginview");
		loginviewconfig.put("navToken", "login");
		loginviewconfig.put("template", "login");
		configs.add((Dictionary<String, String>) loginviewconfig);
		
		Hashtable<String, String> registeruiconfig = new Hashtable<String,String>();
		registeruiconfig.put("name", "registerview");
		registeruiconfig.put("navToken", "register");
		registeruiconfig.put("template", "register");
		configs.add((Dictionary<String, String>) registeruiconfig);
	}
	
	
	@Reference(type='*',target = "(component.factory=shellviewfactory)",name="shellview")
	protected void setUI(ComponentFactory factory,Map<String, Object> properties) {
		viewfacory.set(factory);
	}
	protected void unsetUI(ComponentFactory factory){
		viewfacory.set(null);
	}
	
	@Reference(optional=true, dynamic=true, multiple=false)
	public void setUIFragmentChnageListner(UriFragmentChangedListener listner){
		logger.info("UriFragmentChangedListener Referance:"+ listner.getClass());
		this.listners.add(listner);
		loadEventListners();
	}
	public void unsetUIFragmentChnageListner(UriFragmentChangedListener listner){
		this.listners.remove(listner);
	}
	
	@Activate
	@Modified
	public void activate(Map<String,String> props){
		config = Configurable.createConfigurable(ShellUIConfig.class,props);
		
		if(null!= config.getName())
			this.name.set(config.getName());
		
		if(null!= config.getProviderScope())
			this.providerScope.set(config.getProviderScope());
		
		if(null!= config.getThemeName()){
			this.themeName.set(config.getThemeName());
		}
		
		logger.info("ShellUI activate with name :"+ this.name.get());
		
		
	}
	
	@Override
	public void doInit(VaadinRequest request, int uiId, String embedId) {
        if (this.getUIId() == -1) {
        	this.uiId = uiId;
        	this.embedId = embedId;
        	super.doInit(request, uiId, embedId);
        }
        else{
	        // Call the init overridden by the application developer
        	this.uiId = uiId;
        	this.embedId = embedId;
        	
	        init(request);
        }
    }
	
	@Override
	protected void init(VaadinRequest request) {
		  setTheme(this.themeName.get());
		  loadNavigatior();
		  loadEventListners();
		  VaadinSession.getCurrent().getSession().setMaxInactiveInterval(300);
	}
	
	
	
	protected void loadNavigatior(){
		this.uiNavigator = new Navigator(this, this);
		for(Dictionary<String, String> config: configs){
			IShellView view = (IShellView)this.viewfacory.get().newInstance(config).getInstance();
			this.views.add(view);
			uiNavigator.addView(view.getNavToken(), (View)view);
		}
		if(!listners.isEmpty())
			uiNavigator.addViewChangeListener((ViewChangeListener)listners.get(0));
	}
	
	protected void loadEventListners(){
		for(UriFragmentChangedListener listner: this.listners){
			Page page = this.getPage();
			page.addUriFragmentChangedListener(listner);
		}
	}
	
	@Override
	public void setSession(VaadinSession session) {
		 if (session == null && this.getSession() == null) {
	            throw new IllegalStateException(
	                    "Session should never be set to null when UI.session is already null");
	        } else if (session != null && this.getSession() != null) {
	        	super.setSession(null);
	        	super.setSession(session);
	            
	        } else {
	           super.setSession(session);
	        }

	       
	}

	@Override
	public void attach() {
		super.attach();
		getSession().getService().addSessionDestroyListener(this);
	}

	public void sessionDestroy(SessionDestroyEvent event) {
		dispose();
	}

	
	public void notifyDisposed(IDisposable notifier) {
		if (!isClosing()) {
			close();
		}
	}

	@Override
	public void detach() {
		getSession().getService().removeSessionDestroyListener(this);
		super.detach();
		dispose();
	}

	protected void dispose() {
		this.uiNavigator =null;
	}
	
	@Override
	public String name() {
		return this.name.get();
	}
	@Override
	public String providerScope() {
		return this.providerScope();
	}
	
	
	public void setEmbedId(String embedId) {
		this.embedId = embedId;
	}	
	@Override
	public String getEmbedId() {
		return this.embedId;
	}
	
	public void setUiId(int uiId) {
		this.uiId = uiId;
	}
	@Override
	public int getUIId() {
		return this.uiId;
	}

}
