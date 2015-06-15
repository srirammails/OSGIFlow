package com.flow.pjm.component;

import aQute.bnd.annotation.component.Component;

import com.flow.pjm.model.Registration;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class SignUpForm extends CustomLayout implements ClickListener, ValueChangeListener{

	
	private static final long serialVersionUID = 1L;

	BeanItem<Registration> registrationBean;
	
	private Label formtypeLbl;
	
	private TextField firstNameTxt;
	private TextField lastNameTxt;
	private TextField emailTxt;
	private TextField phoneTxt;
	
	private ComboBox deliveryTypeCmb;
	private ComboBox locationCmb;
	private ComboBox courseCmb;
	private ComboBox termCmb;
	
	private AddressForm address;
	
	Label quotationLbl;
	
	Button 	 submitBtn;
	Button 	 payBtn;

	Label submitError;
	
	String formType;
	
	public SignUpForm(String template, String type) {
		super(template);
		this.formType = type;
		loadView();
	}


	private void loadView() {
		
		registrationBean = new BeanItem<Registration>(new Registration());

		formtypeLbl = new Label(this.formType);
		
		firstNameTxt = new TextField("First Name",registrationBean.getItemProperty("firstName"));
		firstNameTxt.addValidator(new NullValidator("First Name is required", false));
		firstNameTxt.setNullRepresentation("");
		firstNameTxt.setValidationVisible(false);
		
		lastNameTxt = new TextField("Last Name",registrationBean.getItemProperty("lastName"));
		lastNameTxt.addValidator(new NullValidator("Last Name is required", false));
		lastNameTxt.setValidationVisible(false);
		lastNameTxt.setNullRepresentation("");
		
		emailTxt =  new TextField("Email Address",registrationBean.getItemProperty("email"));
		emailTxt.addValidator(new NullValidator("Email is required", false));
		emailTxt.addValidator(new EmailValidator("Not a valid email"));
		emailTxt.setValidationVisible(false);
		emailTxt.setNullRepresentation("");
		
		phoneTxt =  new TextField("Phone Number",registrationBean.getItemProperty("phone"));
		phoneTxt.addValidator(new RegexpValidator("\\d+", false, "Only numbers allowed"));
		phoneTxt.setValidationVisible(false);
		phoneTxt.setNullRepresentation("");
		
		deliveryTypeCmb = new ComboBox("Course Delivery");
		deliveryTypeCmb.addValueChangeListener(this);
		deliveryTypeCmb.setWidth("250px");
		deliveryTypeCmb.setPropertyDataSource(registrationBean.getItemProperty("courseDelivery"));
		deliveryTypeCmb.addValidator(new NullValidator("Teaching method is required", false));
		deliveryTypeCmb.setValidationVisible(false);
		
		if(this.formType.equals("Home"))
			deliveryTypeCmb.addItem("HOME TUTORING");
		else
			deliveryTypeCmb.addItem("ACADEMY");	
		
		locationCmb = new ComboBox("Location");
		locationCmb.addItem("CORK");
		locationCmb.addItem("DUBLIN-SWORDS");
		locationCmb.addItem("DUBLIN-TALLAGHT");
		locationCmb.addItem("GALWAY");
		locationCmb.addItem("LIMERICK");
		locationCmb.addItem("WATERFORD");
		locationCmb.addItem("WEXFORD");
		
		if(this.formType.equals("Home"))
			locationCmb.setVisible(false);
		else 
			locationCmb.setVisible(true);
		
		address = new AddressForm();
		
		if(this.formType.equals("Academy"))
			address.setVisible(false);
		else 
			address.setVisible(true);
		
		courseCmb = new ComboBox("Course");
		courseCmb.addItem("Junior Cycle Ordinary Level");
		courseCmb.addItem("Junior Cycle Higher Level");
		courseCmb.addItem("Leaving Certificate Ordinary Level");
		courseCmb.addItem("Leaving Certificate Higher Level");
		courseCmb.setPropertyDataSource(registrationBean.getItemProperty("course"));
		courseCmb.addValidator(new NullValidator("Course is required", false));
		courseCmb.setValidationVisible(false);
		
		termCmb = new ComboBox("Term");
		termCmb.addItem("SEP-OCT");
		termCmb.addItem("NOV-DEC");
		termCmb.addItem("JAN-MAR");
		termCmb.addItem("MAR-APR");
		termCmb.setPropertyDataSource(registrationBean.getItemProperty("courseTerm"));
		termCmb.addValidator(new NullValidator("Term is required", false));
		termCmb.setValidationVisible(false);


		
		submitBtn = new Button("Register");
		submitBtn.addClickListener(this);
		
		quotationLbl = new Label();
		submitError = new Label();
		
		addComponent(formtypeLbl, "formtype");
		addComponent(firstNameTxt, "firstname");
		addComponent(lastNameTxt, "lastname");
		addComponent(emailTxt, "emailaddr");
		addComponent(phoneTxt, "phonenumbr");
		
		addComponent(deliveryTypeCmb, "deliverytype");
		addComponent(locationCmb, "locationselect");
		addComponent(address, "locationaddr");
		addComponent(courseCmb, "selectcourse");
		addComponent(termCmb, "selectterm");
		
		addComponent(submitBtn, "submit");
		
		addComponent(quotationLbl, "quotedisplay");
		addComponent(submitError, "error");

		
	}


	@Override
	public void buttonClick(ClickEvent event) {
		if(validate()&& event.getButton().equals(this.submitBtn)){
			UI.getCurrent().getNavigator().navigateTo("register");
		}
	}


	private boolean validate() {
		try{
			firstNameTxt.validate();
			lastNameTxt.validate();
			emailTxt.validate();
			if(null!=phoneTxt.getValue())
				phoneTxt.validate();
			
			deliveryTypeCmb.validate();
			courseCmb.validate();
			termCmb.validate();
			
			return true;
			
		}catch(Exception ex){
			Notification.show(ex.getMessage(),Type.WARNING_MESSAGE);
			return false; 

		}
	}


	@Override
	public void valueChange(ValueChangeEvent event) {
		String selectedDelivery = (String) ((ComboBox) event.getProperty()).getValue();
		if(selectedDelivery.equals("ACADEMY")){
			termCmb.setVisible(true);
			locationCmb.setVisible(true);
			address.setVisible(false);

			
		}else if(selectedDelivery.equals("HOME TUTORING")){
			address.setVisible(true);
			termCmb.setVisible(false);
			locationCmb.setVisible(false);
		}
	}
}
