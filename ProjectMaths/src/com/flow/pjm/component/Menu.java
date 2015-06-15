package com.flow.pjm.component;

import java.util.ArrayList;
import java.util.List;
import aQute.bnd.annotation.component.Component;

import com.flow.shellx.api.IShellComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;

@Component(factory="CompMenufactory",properties={"viewScopes=*"})
public class Menu extends MenuBar implements IShellComponent{

	private static final long serialVersionUID = 1L;


	MenuItem home;
	MenuItem forum;
	MenuItem poblemSolver; 
	MenuItem homeTution; 
	MenuItem academyTution;
	MenuItem calendar;
	MenuItem contact;
	
	MenuItem tutorReg; 
	MenuItem logIn; 
	
	
	public Menu(){
		super();
		loadMenuItems();
		this.setAutoOpen(true);
		this.setHtmlContentAllowed(true);

	}
	


	private void loadMenuItems() {
		home = this.addItem("Home", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
			 	UI.getCurrent().getNavigator().navigateTo("");
			}
		});
		forum = this.addItem("Forum", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
			 	UI.getCurrent().getNavigator().navigateTo("forum");
			}
		});
		poblemSolver = this.addItem("Problem Solver", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
			 	UI.getCurrent().getNavigator().navigateTo("problemsolver");
			}
		});
		homeTution = this.addItem("Home Tutoring", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
			 	UI.getCurrent().getNavigator().navigateTo("home-tutoring");
			}
		});
		academyTution = this.addItem("Academy Tutoring", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
			 	UI.getCurrent().getNavigator().navigateTo("academy-tutoring");
			}
		});
		calendar =  this.addItem("Academic Calendar", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
			 	UI.getCurrent().getNavigator().navigateTo("academic-calendar");
			}
		});
		
		contact = this.addItem("Contact Us", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
			 	UI.getCurrent().getNavigator().navigateTo("contact");
			}
		});
	
		
		logIn =this.addItem("<i class='fa fa-user fa-2x'></i>&nbsp;Login", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
			   UI.getCurrent().getNavigator().navigateTo("login");
			}
		});
		logIn.setStyleName("menuright");
		
		tutorReg =this.addItem("<i class='fa fa-book fa-2x'></i>&nbsp;Tutors", new Command() {
			private static final long serialVersionUID = 1L;
			@Override
			public void menuSelected(MenuItem selectedItem) {
			   UI.getCurrent().getNavigator().navigateTo("tutors");
			}
		});
		tutorReg.setStyleName("menuright");
	}

	@Override
	public String getName() {
		return "menu";
	}

	@Override
	public List<String> getViewScopes() {
		List<String> scopes = new ArrayList<String>();
		scopes.add("*");
		return scopes;
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
