package com.flow.shell.component;

import java.util.Map;

import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;

import com.flow.shellx.api.IShellComponent;
import com.flow.shellx.api.IShellView;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;

@Component(immediate=true,provide={IShellComponent.class},designateFactory=EditableImage.Config.class)
public class EditableImage  extends BaseShellComponent{

	private static final long serialVersionUID = 1L;
	
	EditableImage.Config config;

	String imageResource;
	String width;
	String height;

	@Reference(optional=true, dynamic=true, multiple=false)
	public void setLogger(LogService logsrv){
		super.setLogger(logsrv);
	}
	public void unsetLogger(LogService logsrv){
		super.unsetLogger(logsrv);
	}
	
	@Reference(optional=false, dynamic=true, multiple=true)
	public void setShellView(IShellView view){
		super.setShellView(view);
	}
	public void unsetShellView(IShellView view){
		super.unsetShellView(view);
	}
	
	@Activate
	@Modified
	@Override
	public void activate(Map<String,String>props){
		config = Configurable.createConfigurable(EditableImage.Config.class, props);
		this.imageResource = config.imageName();
		this.width =config.width();
		this.height =config.height();
		
		super.config = this.config;
		
		loadConfig();
		setLayout();
		loadView();
		bindToView();
	}
	
	/**
	 * VIEW DEFINITIONS
	**/
	
	public Image image;
	
	@Override
	public void loadView(){
		image = new Image(null,new ThemeResource(this.imageResource));
		image.setWidth(this.width);
		image.setWidth(this.height);
		addComponent(image,config.labelScope());
		
	}
	
	
	public interface Config extends IShellComponent.Config{
		String imageName();
		String width();
		String height();
	}
}
