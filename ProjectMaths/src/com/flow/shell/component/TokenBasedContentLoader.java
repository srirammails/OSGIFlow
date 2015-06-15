package com.flow.shell.component;

import java.util.Map;

import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;

import com.flow.pjm.component.QnANew;
import com.flow.pjm.component.RegistrationForm;
import com.flow.pjm.component.SignUpForm;
import com.flow.pjm.component.TutorSignUp;
import com.flow.shellx.api.IShellComponent;
import com.flow.shellx.api.IShellUI;
import com.flow.shellx.api.IShellView;
import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.UI;


@Component(immediate=true,provide={UriFragmentChangedListener.class},designateFactory=TokenBasedContentLoader.Config.class)
public class TokenBasedContentLoader extends BaseShellComponent implements UriFragmentChangedListener,ViewChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Config config;
	
	SignUpForm signup;
	
	
	@Activate
	@Modified
	public void activate(Map<String,String>props){
		config = Configurable.createConfigurable(Config.class, props);
		super.config = this.config;
		
		loadConfig();
		setLayout();
		loadView();
		bindToView();
	}
	
	@Override
	protected void loadView() {
		
		
	}
	
	@Override
	public void uriFragmentChanged(UriFragmentChangedEvent event) {
		try{
			
			
		}catch (Exception e) {}
	}
	
	@Override
	public boolean beforeViewChange(ViewChangeEvent event) {
		if(checkSecure(event)){
			return true;
		}
		return false;
	}
	private boolean checkSecure(ViewChangeEvent event) {
		if(((IShellView)event.getNewView()).checkClearence()){
			return true;
		}
		else{
			event.getNavigator().navigateTo("login");
			return false;
		}
		
	}
	@Override
	public void afterViewChange(ViewChangeEvent event) {
		if(((IShellView)event.getNewView()).getName().contains("academytutoringview")){
			SignUpForm signupForm = new SignUpForm("signup", "Academy");
		    ((CustomLayout)event.getNewView()).addComponent(signupForm, "staticcontent");
			
		}else if(((IShellView)event.getNewView()).getName().contains("hometutoringview")){
			SignUpForm signupForm = new SignUpForm("signup", "Home");
		    ((CustomLayout)event.getNewView()).addComponent(signupForm, "staticcontent");
			
		}else if(((IShellView)event.getNewView()).getName().contains("registerview")){
			RegistrationForm registrationForm = new RegistrationForm("registrationform");
		    ((CustomLayout)event.getNewView()).addComponent(registrationForm, "registrationform");
			
		}else if(((IShellView)event.getNewView()).getName().contains("tutorsview")){
			TutorSignUp tutorSignUp = new TutorSignUp("tutorregform");
		    ((CustomLayout)event.getNewView()).addComponent(tutorSignUp, "tutorregform");
			
		}else if(((IShellView)event.getNewView()).getName().contains("registerview")){
			RegistrationForm registrationForm = new RegistrationForm("registerform");
		    ((CustomLayout)event.getNewView()).addComponent(registrationForm, "registrationform");
			
		}
	}
	

	interface Config extends IShellComponent.Config{
		
	}


	
}
