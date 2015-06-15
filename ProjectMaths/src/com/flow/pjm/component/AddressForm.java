package com.flow.pjm.component;

import java.io.Serializable;
import java.util.logging.Logger;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TextField;

public class AddressForm extends CustomLayout implements Serializable, ClickListener, ValueChangeListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AddressForm.class.getName());
	private TextField houseTxtField;
	private TextField streeTtxtField;
	private TextField cityTxtField;
	private ComboBox countryCombo;

	private boolean isMandatory = false;
	
	public AddressForm() {
		super("address");
		log.info("address called");
		
		houseTxtField = new TextField("Address:");
		houseTxtField.setInputPrompt("Building");
		houseTxtField.setNullRepresentation("");
		streeTtxtField = new TextField();
		streeTtxtField.setInputPrompt("Street");
		streeTtxtField.setNullRepresentation("");
		cityTxtField = new TextField();
		cityTxtField.setInputPrompt("City");
		cityTxtField.setNullRepresentation("");
		countryCombo = new ComboBox();
		countryCombo.setInputPrompt("Country");
		countryCombo.addValueChangeListener(this);
		
		addComponent(houseTxtField, "addrln1");
		addComponent(streeTtxtField, "addrln2");
		addComponent(cityTxtField, "city");
		addComponent(countryCombo, "country");
		log.info("address form load end");
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
	}
	
	@SuppressWarnings("deprecation")
	private void loadData() {

	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
	
	}
	
	
	
	public boolean isMandatory() {
		return isMandatory;
	}
	
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
		countryCombo.setRequired(isMandatory);
	}
}
