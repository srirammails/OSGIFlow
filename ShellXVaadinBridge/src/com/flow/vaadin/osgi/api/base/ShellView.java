package com.flow.vaadin.osgi.api.base;

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

import com.flow.shellx.api.IShellComponent;
import com.flow.shellx.api.IShellView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomLayout;


@Component(factory="shellviewfactory", name="shellview", properties={"uiscope=shellui"})
public class ShellView extends CustomLayout implements IShellView, View{


	
	private static final long serialVersionUID = 1L;
	
	IShellView.Config config;
	
	AtomicReference<String> name = new AtomicReference<String>();
	AtomicReference<String> uiScope = new AtomicReference<String>();
	AtomicReference<String> navToken = new AtomicReference<String>();
	AtomicReference<String> template = new AtomicReference<String>();
	AtomicReference<Boolean> isSecure = new AtomicReference<Boolean>();

	List<ComponentFactory> componetFactories = new CopyOnWriteArrayList<ComponentFactory>();
	List<IShellComponent> components = new CopyOnWriteArrayList<IShellComponent>();
	
	private Logger logger = LoggerFactory.getLogger(ShellView.class);
	
	public ShellView(){
		super();
		
	}
	
	
	@Reference(type='*',target = "(component.factory=Comp*)", multiple=true)
	protected void setUI(ComponentFactory factory,Map<String, Object> properties) {
		componetFactories.add(factory);
		components.add((IShellComponent)factory.newInstance(null).getInstance());
	}
	protected void unsetUI(ComponentFactory factory){
		componetFactories.remove(factory);
		components.remove(factory.newInstance(null).getInstance());
	}
	
	
	@Activate
	@Modified
	public void activate(Map<String,String> props){
		config = Configurable.createConfigurable(IShellView.Config.class, props);
		
		if(null!=config.name())
			this.name.set(config.name());
		
		if(null!=config.uiScope())
			this.uiScope.set(config.uiScope());
		
		if(null!=config.navToken())
			this.navToken.set(config.navToken());
		
		if(null!=config.template())
			this.template.set(config.template());
		
		if(null!=config.issecure())
			this.isSecure.set(config.issecure());

		logger.info("ShellView activated with  name:"+this.name.get());

		setLayout();
		loadComponents();
		bindComponents();
	}
	

	private void setLayout() {
		this.setTemplateName(this.template.get());
	}

	private void loadComponents() {
		for (IShellComponent comp : this.components) {
				this.components.add(comp);
		}
	}

	private void bindComponents() {
		for(IShellComponent comp: this.components){
			if(comp.getViewScopes().contains("*")||comp.getViewScopes().contains(this.name.get())){
				addComponent((com.vaadin.ui.Component) comp, comp.getLabelScope());
				logger.info("Component:"+comp.getName()+" add to ShellView:"+this.name.get());
			}
		}
	}
	
	@Override
	public void bindComponent(Object component, String location) {
		addComponent((com.vaadin.ui.Component) component, location);
	}

	
	@Override
	public void enter(ViewChangeEvent event) {
		
		//TODO enable configurable view change event handle
	}

	@Override
	public String getName() {
		return this.name.get();
	}
	@Override
	public String getUIScope() {
		return this.uiScope.get();
	}
	@Override
	public String getNavToken() {
		return this.navToken.get();
	}
	@Override
	public boolean checkClearence() {
		return true;
	}


	@Override
	public void setComponent(IShellComponent component) {
		
	}
	@Override
	public void unsetComponent(IShellComponent component) {
		
	}



	

}
