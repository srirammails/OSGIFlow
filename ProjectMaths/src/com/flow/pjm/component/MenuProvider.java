package com.flow.pjm.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;
import com.flow.shellx.api.IShellComponent;
import com.flow.shellx.api.IShellView;
import com.flow.shellx.proxy.LogServiceProxy;

//@Component(immediate=true)
public class MenuProvider implements IShellComponent{

	private static final long serialVersionUID = 1L;
	
	AtomicReference<List<String>> viewScopes = new AtomicReference<List<String>>();

	Menu  menu;
	
	LogService logger  = new LogServiceProxy();

	List<IShellView> shellViews = new CopyOnWriteArrayList<IShellView>();
	protected List<IShellView> tmpShellViews = new CopyOnWriteArrayList<IShellView>();	
	
	public MenuProvider(){
		List<String> views = new ArrayList<String>();
		views.add("");
		views.add("homeview");
		views.add("forumview");
		views.add("problemsolverview");
		views.add("hometutoringview");
		views.add("academytutoringview");
		views.add("calendarview");
		views.add("contactview");
		views.add("tutorsview");
		views.add("loginview");
		views.add("regitserview");

		viewScopes.set(views);
	}
	
	
	//@Reference(optional=true, dynamic=true, multiple=false)
	public void setLogger(LogService logsrv){
		this.logger = logsrv;
	}
	public void unsetLogger(LogService logsrv){
		this.logger = new LogServiceProxy();
	}

	//@Reference(optional=false, dynamic=true, multiple=true)
//	public void setShellView(IShellView view){
//		if(viewScopes.get()!=null&&viewScopes.get().contains(view.getName()))
//			this.shellViews.add(view);
//		else
//			this.tmpShellViews.add(view);
//			bindToView();
//		
//	}
//	public void unsetShellView(IShellView view){
//		this.shellViews.remove(view);
//	}
//	
//	//@Activate
//	public void activate(Map<String,String>props){
//		bindToView();
//	}
//	
	
//	protected void bindToView(){
//		if(!this.tmpShellViews.isEmpty()){
//			for(IShellView view : this.tmpShellViews){
//				if(this.viewScopes.get()!=null && this.viewScopes.get().contains(view.getName())){
//					this.shellViews.add(view);
//				}
//			}
//		}
//		
//		if(!this.shellViews.isEmpty()){
//			for(IShellView view: this.shellViews){
//				view.setComponent(new Menu(viewScopes));
//			}
//		}
//		
//	}

	@Override
	public String getName() {
		return "menu";
	}

	@Override
	public List<String> getViewScopes() {
		return this.viewScopes.get();
	}

	@Override
	public String getLabelScope() {
		return "menu";
	}

	@Override
	public String getTemplate() {
		return null;
	}

}
