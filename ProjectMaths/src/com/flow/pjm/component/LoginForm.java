package com.flow.pjm.component;

import java.util.Map;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Modified;
import com.flow.shell.component.BaseShellComponent;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.BaseTheme;

@Component(factory="CompLoginForm",name="LoginForm", properties={"viewScopes=*","template=loginform","labelScope=loginform"})
public class LoginForm extends BaseShellComponent implements ClickListener{

	private static final long serialVersionUID = 1L;
	

//	AbstractCamelRunner camelService;
//	
//	@Reference(optional=true, dynamic=true, multiple=false)
//	public void setLogger(AbstractCamelRunner configadmin){
//		camelService = configadmin;
//	}
//	public void unsetLogger(AbstractCamelRunner configadmin){
//		camelService=null;
//	}
	
//	@Reference(optional=false, dynamic=true, multiple=true)
//	public void setShellView(IShellView view){
//		super.setShellView(view);
//	}
//	public void unsetShellView(IShellView view){
//		super.unsetShellView(view);
//	}
	
	@Activate
	@Modified
	public void activate(Map<String,String>props){
		super.activate(props);
	}
	
	/**
	 * VIEW DEFINITIONS
	**/
	
	TextField usernamefield;
	PasswordField passwordfiled;
	Button loginbutton;
	Button reminderlink;
	
	Label loginerror;
	
	@Override
	public void loadView(){
		
		usernamefield = new TextField("User Name");
		usernamefield.setRequired(true);
		usernamefield.addValidator(new NullValidator("Must not be null", false));
		usernamefield.setNullRepresentation("");
		usernamefield.setValidationVisible(true);
		usernamefield.focus();
		
		passwordfiled = new PasswordField("Password");
		passwordfiled.setRequired(true);
		passwordfiled.setNullRepresentation("");
		passwordfiled.addValidator(new NullValidator("Must not be null", false));
		passwordfiled.setValidationVisible(true);
				
		loginerror = new Label("lblerr");
		loginerror.setVisible(false);
		
		loginbutton = new Button("Login");
		loginbutton.setClickShortcut(KeyCode.ENTER);
		loginbutton.addClickListener(this);
		
		reminderlink = new Button("Forgot Username/Passowrd?");
		reminderlink.setStyleName(BaseTheme.BUTTON_LINK);
		
		addComponent(usernamefield, "usernamefield");
		addComponent(passwordfiled, "passwordfiled");
		addComponent(loginbutton, "loginbutton");
		addComponent(reminderlink, "reminderlink");
		addComponent(loginerror, "loginerror");
		
		this.setWidth("570px");
		this.setHeight("250px");
	}
	
	
	@Override
	public void buttonClick(ClickEvent event) {
		
	}
	
	
	
//	ModelCamelContext context = camelService.getContext();
//	ProducerTemplate template = context.createProducerTemplate();
//	Object response = template.requestBody("direct:vaadintest", "test");
//	if(response!=null){
//		loginerror.setValue((String)response);;
//		loginerror.setVisible(true);
//	}
	
//	try {
//		  
//		Configuration config =configAdmin.createFactoryConfiguration("com.flow.vaadin.osgi.api.base.VaadinApplication",null);
//		
//		Dictionary props = new Hashtable();
//
//		props.put("service.pid","adminpage");
//		props.put("name","shell");
//		props.put("hostName","localhost");
//		props.put("path","/admin");
//		props.put("uiAlias","");
//		props.put("resourceBase","/VAADIN");
//		props.put("widgetset","com.vaadin.DefaultWidgetSet");
//		props.put("productionMode","false");
//		props.put("httpApplication","false");
//		props.put("offline","false");
//		
//		if(config!=null)
//			config.update(props);
//	
//				
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
//	


}
