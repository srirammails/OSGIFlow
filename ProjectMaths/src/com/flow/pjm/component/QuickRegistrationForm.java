package com.flow.pjm.component;

import java.util.Map;

import javax.validation.Validation;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;

import com.flow.model.pjm.Contact;
import com.flow.model.pjm.Registration;
import com.flow.shell.component.BaseShellComponent;
import com.flow.shellx.api.IPersistenceProvider;
import com.flow.shellx.api.IShellComponent;
import com.flow.shellx.api.IShellView;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@Component(factory="CompQuickegFactory",name="Quickeg", properties={"viewScopes=homeview","template=quickregform","labelScope=quickregform"})
public class QuickRegistrationForm extends BaseShellComponent implements ClickListener{

private static final long serialVersionUID = 1L;
	
	
	BeanItem<Registration> registrationBean;
	
//	@Reference(optional=true, dynamic=true, multiple=true)
//	public void setShellView(IShellView view){
//		super.setShellView(view);
//	}
//	public void unsetShellView(IShellView view){
//		super.unsetShellView(view);
//	}
//	
	@Reference(optional=false, dynamic=false, multiple=false)
	public void setPersistencProvider(IPersistenceProvider provider){
		super.setPersistencProvider(provider);
	}
	public void unsetPersistenceProvider(IPersistenceProvider provider){
		super.unsetPersistenceProvider(provider);
	}

	@Activate
	@Modified
	public void activate(Map<String,String>props){
		super.activate(props);
	}
	
	
	/**
	 * VIEW DEFINITIONS
	**/
	
	TextField firstName;
	TextField lastName;
	TextField email;
	TextField phone;
	
	private ComboBox deliveryTypeCmb;
	private ComboBox courseCmb;
	private ComboBox termCmb;
	
	Button 	 submitBtn;
	Label submitError;
	
	
	
	@Override
	public void loadView(){
		
		registrationBean = new BeanItem<Registration>(new Registration());
		
		firstName = new TextField(registrationBean.getItemProperty("firstName"));
		firstName.addValidator(new NullValidator("First Name is required", false));
		firstName.setValidationVisible(false);
		firstName.setNullRepresentation("");
		firstName.setInputPrompt("First Name");
		
		
		lastName = new TextField(registrationBean.getItemProperty("lastName"));
		lastName.addValidator(new NullValidator("Last Name is required", false));
		lastName.setValidationVisible(false);
		lastName.setNullRepresentation("");
		lastName.setInputPrompt("Last Name");

		email = new TextField(registrationBean.getItemProperty("email"));
		email.setInputPrompt("Email Address");
		email.addValidator(new NullValidator("Email is required", false));
		email.addValidator(new EmailValidator("Not a valid email"));
		email.setValidationVisible(false);
		email.setNullRepresentation("");

		
		phone = new TextField(registrationBean.getItemProperty("phone"));
		phone.setInputPrompt("Phone Number");
		phone.addValidator(new RegexpValidator("\\d+", false, "Only numbers allowed"));
		phone.setValidationVisible(false);
		phone.setNullRepresentation("");

		deliveryTypeCmb = new ComboBox();
		deliveryTypeCmb.setWidth("200px");
		deliveryTypeCmb.setInputPrompt("Select Teaching Method");
		deliveryTypeCmb.addItem("ACADEMY");
		deliveryTypeCmb.addItem("HOME TUTORING");
		deliveryTypeCmb.setPropertyDataSource(registrationBean.getItemProperty("courseDelivery"));
		deliveryTypeCmb.addValidator(new NullValidator("Teaching method is required", false));
		deliveryTypeCmb.setValidationVisible(false);

		
		courseCmb = new ComboBox();
		courseCmb.setWidth("200px");
		courseCmb.setInputPrompt("Select Course");
		courseCmb.addItem("Junior Cycle Ordinary Level");
		courseCmb.addItem("Junior Cycle Higher Level");
		courseCmb.addItem("Leaving Certificate Ordinary Level");
		courseCmb.addItem("Leaving Certificate Higher Level");
		courseCmb.setPropertyDataSource(registrationBean.getItemProperty("course"));
		courseCmb.addValidator(new NullValidator("Course is required", false));
		courseCmb.setValidationVisible(false);

		
		termCmb = new ComboBox();
		termCmb.setWidth("200px");
		termCmb.setInputPrompt("Select Term");
		termCmb.addItem("SEP-OCT");
		termCmb.addItem("NOV-DEC");
		termCmb.addItem("JAN-MAR");
		termCmb.addItem("MAR-APR");
		termCmb.setPropertyDataSource(registrationBean.getItemProperty("courseTerm"));
		termCmb.addValidator(new NullValidator("Term is required", false));
		termCmb.setValidationVisible(false);

		
		submitBtn = new Button("Submit");
		submitBtn.addClickListener(this);
		
		submitError = new Label();
		
		addComponent(firstName, "firstName");
		addComponent(lastName, "lastName");
		addComponent(email, "email");
		addComponent(phone, "phone");
		addComponent(deliveryTypeCmb, "type");
		addComponent(courseCmb, "course");
		addComponent(termCmb, "term");
		addComponent(submitBtn, "submit");
		addComponent(submitError, "error");

		this.setWidth("300px");
	}
	
	
	@Override
	public void buttonClick(ClickEvent event) {
		if(validate()&& event.getButton().equals(this.submitBtn)){
			if(registrationBean.getBean().courseDelivery.equals("ACADEMY")){
				UI.getCurrent().getNavigator().navigateTo("register");
			}else{
				UI.getCurrent().getNavigator().navigateTo("register");
			}
		}
	}
	
	private boolean validate(){
		try{
			firstName.validate();
			lastName.validate();
			email.validate();
			if(null!=phone.getValue())
				phone.validate();
			
			deliveryTypeCmb.validate();
			courseCmb.validate();
			termCmb.validate();
			
			return true;
			
		}catch(Exception ex){
			Notification.show(ex.getMessage(),Type.WARNING_MESSAGE);
			return false; 

		}
	}
	
}
