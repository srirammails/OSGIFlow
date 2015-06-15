package com.flow.vaadin.osgi.api.base;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;

import com.flow.shellx.api.IShellComponent;
import com.flow.shellx.api.IShellView;
import com.flow.shellx.proxy.LogServiceProxy;
import com.vaadin.ui.CustomLayout;

public abstract class ShellComponent extends CustomLayout implements IShellComponent {

	
private static final long serialVersionUID = 1L;
	
	IShellComponent.Config config;
	
	AtomicReference<String> name = new AtomicReference<String>();
	AtomicReference<List<String>> viewScopes = new AtomicReference<List<String>>();
	AtomicReference<String> labelScope = new AtomicReference<String>();
	AtomicReference<String> template = new AtomicReference<String>();

	LogService logger  = new LogServiceProxy();

	List<IShellView> shellViews = new CopyOnWriteArrayList<IShellView>();
	protected List<IShellView> tmpShellViews = new CopyOnWriteArrayList<IShellView>();	

	
	@Reference(optional=true, dynamic=true, multiple=false)
	public void setLogger(LogService logsrv){
		this.logger = logsrv;
	}
	public void unsetLogger(LogService logsrv){
		this.logger = new LogServiceProxy();
	}
	
	@Reference(optional=false, dynamic=true, multiple=true)
	public void setShellView(IShellView view){
		if(viewScopes.get().contains(view.getName()))
			this.shellViews.add(view);
		else
			this.tmpShellViews.add(view);
		
		bindToView();
	}
	public void unsetShellView(IShellView view){
			this.shellViews.remove(view);
	}
	
	@Activate
	@Modified
	public void activate(Map<String,String>props){
		config = Configurable.createConfigurable(IShellComponent.Config.class, props);
		
		if(null!= config.name())
			this.name.set(config.name());
		
		if(null!= config.viewScopes())
			this.viewScopes.set(config.viewScopes());
		
		if(null!= config.labelScope())
			this.labelScope.set(config.labelScope());
		
		if(null!=config.template())
			this.template.set(config.template());
	
		setLayout();
		loadView();
		bindToView();
	}
	
	protected void bindToView(){
		if(!this.tmpShellViews.isEmpty()){
			for(IShellView view : this.tmpShellViews){
				if(this.viewScopes.get().contains(view.getName())){
					this.shellViews.add(view);
				}
			}
		}
		
		if(!this.shellViews.isEmpty()){
			for(IShellView view: this.shellViews){
				view.setComponent(this);
			}
		}
	}
	
	@Deactivate
	protected void deactivate(){
		if(!this.shellViews.isEmpty()){
			for(IShellView view: this.shellViews){
				view.setComponent(this);
			}
		}
	}
	
	protected void loadView(){
		
	}

	protected void setLayout() {
		this.setTemplateName(this.template.get());
	}

	
	
	@Override
	public String getName() {
		return this.name.get();
	}
	@Override
	public List<String> getViewScopes() {
		return this.viewScopes.get();
	}
	@Override
	public String getLabelScope() {
		return this.labelScope.get();
	}
	@Override
	public String getTemplate() {
		return this.template.get();
	}

}
