package com.flow.pjm.component;

import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;

import com.flow.pjm.model.Contact;
import com.flow.shell.component.BaseShellComponent;
import com.flow.shellx.api.IEntityManagerProvider;
import com.flow.shellx.api.IPersistenceProvider;
import com.flow.shellx.api.IShellComponent;
import com.flow.shellx.api.IShellView;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

@Component(factory="CompContactFactory",name="ContactForm", properties={"viewScopes=contactview","template=contactform","labelScope=contactform"})
public class ContactForm extends BaseShellComponent implements ClickListener{

	private static final long serialVersionUID = 1L;
	
	BeanItem<Contact> contactBean;

	
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
	TextArea message;
	Button 	 submitBtn;
	Label submitError;
	
	@Override
	public void loadView(){
		
		contactBean = new BeanItem<Contact>(new Contact());
		
		firstName = new TextField("First Name",contactBean.getItemProperty("firstName"));
		firstName.addValidator(new NullValidator("*", false));
		firstName.setValidationVisible(false);
		firstName.setNullRepresentation("");
		
		
		lastName = new TextField("Last Name",contactBean.getItemProperty("lastName"));
		lastName.addValidator(new NullValidator("*", false));
		lastName.setValidationVisible(false);
		lastName.setNullRepresentation("");

		email = new TextField("Email Address",contactBean.getItemProperty("email"));
		email.addValidator(new NullValidator("*", false));
		email.setValidationVisible(false);
		email.setNullRepresentation("");

		
		phone = new TextField("Phone Number",contactBean.getItemProperty("phone"));
		phone.setNullRepresentation("");

		message = new TextArea("Message",contactBean.getItemProperty("message"));
		message.setWidth("250px");
		message.setNullRepresentation("");
		
		submitBtn = new Button("Submit");
		submitBtn.addClickListener(this);
		
		submitError = new Label();
		
		addComponent(firstName, "firstName");
		addComponent(lastName, "lastName");
		addComponent(email, "email");
		addComponent(phone, "phone");
		addComponent(message, "message");
		addComponent(submitBtn, "submit");
		addComponent(submitError, "error");

		this.setWidth("600px");
	}
	
	
	@Override
	public void buttonClick(ClickEvent event) {
		if(this.validate()){
			PersistenceManager pm =null;
			Transaction tr =null;
			try{
				pm = this.persistanceProfider.getPersistenceManager();
				tr = pm.currentTransaction();
				tr.begin();
				pm.makePersistent(this.contactBean.getBean());
				tr.commit();
				
			}catch(Exception ex){
				tr.rollback();
			}finally{
				if(tr.isActive())
					tr.rollback();
				if(null!=pm & !pm.isClosed())
					pm.close();
				
				clearForm();
			}
		}
		
	}
	private void clearForm() {
		this.submitError.setCaption("");
		submitError.setValue("Thanks for contacting ProjectMaths Academy. We will contact you as soon as possible.");
		submitError.setVisible(true);
		firstName.setVisible(false);
		lastName.setVisible(false);
		email.setVisible(false);
		phone.setVisible(false);
		message.setVisible(false);
		submitBtn.setVisible(false);
		
	}
	private boolean validate() {
			if(this.firstName.isValid() &&
					this.firstName.isValid()&&
					this.lastName.isValid()&&
					this.email.isValid()){
				this.submitError.setVisible(false);
				return true;
			}else{
				this.submitError.setVisible(true);
				this.submitError.setCaption("Missing required fields.");
				return false;
			}
	}
	
}
