package com.flow.pjm.component;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class QnANew extends CustomLayout implements ClickListener, ValueChangeListener{

	
	private static final long serialVersionUID = 1L;

	private TextField questionTxt;
	private TextArea descriprionTxt;
	private TextField tagsTxt;
	
	Button 	 submitBtn;
	Label submitError;
	
	
	public QnANew() {
		super("newqna");
		loadView();
	}


	private void loadView() {
		questionTxt = new TextField("Question Title");
		questionTxt.setWidth("500px");
		descriprionTxt = new TextArea("Question");
		descriprionTxt.setColumns(50);
		descriprionTxt.setHeight("200px");

		submitBtn = new Button("Submit");
		submitError = new Label();
		
		addComponent(questionTxt, "questiontitle");
		addComponent(descriprionTxt, "question");

		addComponent(submitBtn, "submit");		
		addComponent(submitError, "error");

		
	}


	@Override
	public void buttonClick(ClickEvent event) {
	
		
	}


	@Override
	public void valueChange(ValueChangeEvent event) {
		
	}
}
