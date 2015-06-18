package com.flow.pjm.component;

import java.io.OutputStream;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.flow.model.pjm.Registration;
import com.flow.model.pjm.Tutor;
import com.flow.shell.component.BaseShellComponent;
import com.flow.shell.component.UploadReceiver;
import com.flow.shellx.api.IPersistenceProvider;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload;

@Component(factory="CompTutorFactory",name="TutorSignupForm", properties={"viewScopes=tutorview","template=tutorform","labelScope=tutorform"})
public class TutorSignUp extends BaseShellComponent implements ClickListener {

	private static final long serialVersionUID = 1L;

	BeanItem<Tutor> registrationBean;

	private TextField firstNameTxt;
	private TextField lastNameTxt;
	private TextField emailTxt;
	private TextField phoneTxt;
	private TextArea  message;
	
	private Label uploadLbl;
	private Upload cvUpload;
	private Button submitBtn;
	Label submitError;
	


	@Reference(optional=false, dynamic=false, multiple=false)
	public void setPersistencProvider(IPersistenceProvider provider){
		super.setPersistencProvider(provider);
	}
	public void unsetPersistenceProvider(IPersistenceProvider provider){
		super.unsetPersistenceProvider(provider);
	}
	
	
	public TutorSignUp(String template) {
		super(template);
		loadView();
	}
	
	@Override
	protected void loadView() {
		registrationBean = new BeanItem<Tutor>(new Tutor());
		
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
		
		uploadLbl = new Label();
		uploadLbl.setValue("");
		
		cvUpload = new Upload("Upload CV", new UploadReceiver(){

			private static final long serialVersionUID = 1L;
			@Override
			public void uploadSucceeded(SucceededEvent event) {
				uploadLbl.setValue(event.getFilename());
				uploadLbl.setVisible(true);
			}
		});
		cvUpload.setWidth("800px");
		
		message = new TextArea("Message",registrationBean.getItemProperty("message"));
		message.setWidth("250px");
		message.setNullRepresentation("");
		
		
		submitBtn = new Button("Submit");
		submitBtn.addClickListener(this);
		
		addComponent(firstNameTxt, "firstname");
		addComponent(lastNameTxt, "lastname");
		addComponent(emailTxt, "emailaddr");
		addComponent(phoneTxt, "phonenumbr");
		addComponent(cvUpload, "cv");
		addComponent(uploadLbl, "filename");
		addComponent(message, "message");
		
		addComponent(submitBtn, "submit");
		
		this.setWidth("800px");
		
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if(event.getButton().equals(this.submitBtn)){
			if(this.validate()){
				PersistenceManager pm =null;
				Transaction tr =null;
				try{
					pm = this.persistanceProfider.getPersistenceManager();
					tr = pm.currentTransaction();
					tr.begin();
					pm.makePersistent(this.registrationBean.getBean());
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
	}
	private void clearForm() {
		
		firstNameTxt.setVisible(false);
		lastNameTxt.setVisible(false);
		emailTxt.setVisible(false);
		phoneTxt.setVisible(false);
		message.setVisible(false);
		submitBtn.setVisible(false);
		cvUpload.setVisible(false);
		
		this.submitError.setCaption("");
		submitError.setValue("Thanks for contacting ProjectMaths Academy. We will contact you as soon as possible.");
		submitError.setVisible(true);
		
	}
	private boolean validate() {
		return true;
	}

}
