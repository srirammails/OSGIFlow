package com.flow.vaadin.osgi.api.base;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;

import com.flow.shellx.api.IShellUI;
import com.flow.shellx.api.IShellUIProvider;
import com.flow.vaadin.osgi.api.ShellUIConfig;
import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

@SuppressWarnings("unchecked")
@Component(factory="shelluiprovider", name="shellapp")
public class ShellUIProvider extends UIProvider implements IShellUIProvider {

	private static final long serialVersionUID = 1L;
	
	ConfigurationAdmin configAdmin;
	List<Dictionary<String, String>> configs;
	
	private Class<? extends UI> uiClass = ShellUI.class;
	
	private AtomicReference<String> name = new AtomicReference<String>();
	
	protected List<ComponentFactory> shellUIFactory = new CopyOnWriteArrayList<ComponentFactory>();

	private Logger logger = LoggerFactory.getLogger(ShellUIProvider.class);
	
	public ShellUIProvider() {
		super();
		
		configs = new ArrayList<Dictionary<String,String>>();
		Hashtable<String, String> homeuiconfig = new Hashtable<String,String>();
		homeuiconfig.put("getName", "homeui");
		homeuiconfig.put("getProviderScope", "shellapp");
		homeuiconfig.put("getThemeName", "pjm");
		configs.add((Dictionary<String, String>) homeuiconfig);
	}
	
	@Reference(optional=false, dynamic=true, multiple=false)
	public void setConfigAdmin(ConfigurationAdmin configadmin){
		this.configAdmin = configadmin;
	}
	public void unsetConfigAdmin(ConfigurationAdmin configadmin){
		this.configAdmin = configadmin;
	}
	
	@Reference(type='*',target = "(component.factory=shelluifacory)",name="shellui")
	protected void setUI(ComponentFactory factory,Map<String, Object> properties) {
		shellUIFactory.add(factory);
	}
	protected void unsetUI(ComponentFactory factory){
		shellUIFactory.remove(factory);
	}
	
	
	public void setUIClass( Class<? extends UI> uiclass){
		this.uiClass =uiclass;
	}
	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		return uiClass;
	}

	
	@Activate
	@Modified
	public void activate(Map<String,String> props){
		
		
	}
	
	@Override
	public UI createInstance(UICreateEvent event) {
		IShellUI ui = null;
		for(Dictionary<String, String> config: configs){
			ShellUIConfig uiConfig =Configurable.createConfigurable(ShellUIConfig.class, config);
			String s= uiConfig.getProviderScope().toString();
			if(s.equals("shellapp")){
				ComponentInstance compoentinstace = shellUIFactory.get(0).newInstance(config);
				ui = (IShellUI)compoentinstace.getInstance();
			}
		}
		
		logger.info("UI activate with name:"+this.name.get());
		return(UI)ui;

	}

	@Override
	public UIProvider getUIProvider() {
		return this;
	}

	@Override
	public String getName() {
		return this.name.get();
	}

}
