package com.flow.pjm.component;

import java.io.Serializable;
import java.util.logging.Logger;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class TbCells extends CustomLayout implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TbCells(String layout){
		super(layout);
	}
	
	public interface TableCellListner extends Listener{
		public void onCellClick(CellClickType clicktype, Object itemid);
	}
	
	public enum CellClickType{
		DELETE,
		UPDATE,
		EDIT,
		VIEWDETAIL
	}
}
	
	
	

	//Employee cell
	class QnACell extends TbCells implements Serializable, ClickListener{

		private static final long serialVersionUID = 1L;
		
		private static final Logger log = Logger.getLogger(QnACell.class.getName());

		Image employeeImg;
		Button employeeviewBtn;
		Button employeeSummaryBtn;
		
		Button employeeSetupBtn;
		Button employeeDeleteBtn;
		
		Label empFullNameLbl;
		
		Object rowiIemId;
		TableCellListner Listner;
		
		public QnACell(Object emp, final Object itemId, final TableCellListner listner){
			super("qnacell");
			
			Listner = listner;
			rowiIemId = itemId;
			
			employeeImg = new Image();
			employeeImg.setSource(new ThemeResource("img/user_placeholder.png"));
			employeeImg.setWidth("65px");
			employeeImg.setHeight("65px");
			
//			if(emp!=null && emp.getUserId()!=null){
//				String userFullName = emp.getUserId().getUserFirsName()!=null ?emp.getUserId().getUserFirsName():"";
//				String lastname =  emp.getUserId().getUserLastName()!=null? emp.getUserId().getUserLastName():"";
//				userFullName = userFullName + " "+ lastname;
//				empFullNameLbl = new Label(userFullName);
//				empFullNameLbl.setWidth("200px");
//			}
//			else
//			{	
//				empFullNameLbl = new Label("--");
//			}
//			this.addComponent(employeeImg, "userplaceholder");
//			this.addComponent(empFullNameLbl, "empnamelbl");
//			
//			if(emp.getIsSet()!=null && emp.getIsSet()){
//				employeeviewBtn = new Button("View", this);
//				employeeviewBtn.setStyleName(BaseTheme.BUTTON_LINK);
//				employeeSummaryBtn  = new  Button("Summary", this);
//				employeeSummaryBtn.setStyleName(BaseTheme.BUTTON_LINK);
//				
//				this.addComponent(employeeviewBtn,"viewbtn");
//				this.addComponent(employeeSummaryBtn, "summarybtn");
//			}
//			else{
//				employeeSetupBtn = new Button("Setup", this);
//				employeeSetupBtn.setStyleName(BaseTheme.BUTTON_LINK);
//				employeeDeleteBtn  = new  Button("Delete", this);
//				employeeDeleteBtn.setStyleName(BaseTheme.BUTTON_LINK);
//				
//				this.addComponent(employeeSetupBtn,"viewbtn");
//				this.addComponent(employeeDeleteBtn, "summarybtn");
//			}
		}

		@Override
		public void buttonClick(ClickEvent event) {

			if(event.getButton().equals(this.employeeSetupBtn)){
				log.info("Table cell setup clicked");
				Listner.onCellClick(CellClickType.EDIT,rowiIemId);
				
			}else if (event.getButton().equals(this.employeeDeleteBtn)){
				log.info("Table cell delete clicked");
				Listner.onCellClick(CellClickType.DELETE,rowiIemId);
				
				
			}else if (event.getButton().equals(this.employeeSummaryBtn)){
				log.info("Table cell view clicked");
				Listner.onCellClick(CellClickType.VIEWDETAIL,rowiIemId);
				
			}else if (event.getButton().equals(this.employeeviewBtn)){
				log.info("Table cell view clicked");
				Listner.onCellClick(CellClickType.EDIT,rowiIemId);
			}
			
		}
	}
	
	//Employment cell
	class EmploymentCell extends VerticalLayout implements Serializable, ItemClickListener{


		private static final long serialVersionUID = 1L;

		Label roleLbl;
		Label deptLbl;
		Label locationLbl;
		
		public EmploymentCell(Object emp){
			
			roleLbl = new Label("--");
			
//			if(emp.getEmployeeDocuments()!=null)
//				this.deptLbl = new Label(emp.getEmployeeDocuments().getDepartmentName());
//			else
				this.deptLbl = new Label("--");
			
//			if(emp.getEmployeeLocations()!=null && emp.getEmployeeLocations().size()!=0 ){
//				for(EmployeeLocation office: emp.getEmployeeLocations()){
//					if(office.getIsPrimaryLocation())
//						this.locationLbl = new Label(office.getCompanyLocation()!=null?office.getCompanyLocation().getCompanyOfficeName():"--");
//				}
//			}else{
//				this.locationLbl = new Label("--");
//			}
			
			this.addComponent(roleLbl);
			this.addComponent(deptLbl);
			this.addComponent(locationLbl);
		}
		
		@Override
		public void itemClick(ItemClickEvent event) {
			
		}
	}
	
	//Contact cell
	class ContactCell extends VerticalLayout implements Serializable, ItemClickListener{

		private static final long serialVersionUID = 1L;
		
		Label emailLbl;
		Label phoneLbl;
		Label imLbl;
		
		public ContactCell(Object emp){
//			String email = emp.getUserId().getUserPrimaryEmail()!=null?emp.getUserId().getUserPrimaryEmail():"--";
			//String phone = emp.!=null?emp.getUserId().getUserPrimaryEmail():"--";
//			emailLbl = new Label(Icon.envelope+"&nbsp;"+email,ContentMode.HTML);
//			phoneLbl = new Label(Icon.phone+"&nbsp;"+"--",ContentMode.HTML);
//			imLbl = new Label(Icon.headphones+"&nbsp;"+"--",ContentMode.HTML);
			
			this.addComponent(emailLbl);
			this.addComponent(phoneLbl);
			this.addComponent(imLbl);
		}

		@Override
		public void itemClick(ItemClickEvent event) {
			
		}
	}
	
	//Role cell
	class RoleCell extends VerticalLayout implements Serializable, ItemClickListener{

		private static final long serialVersionUID = 1L;
		
		Label roleLbl;
		
		public RoleCell(Object emp){
			roleLbl = new Label("--");
			addComponent(roleLbl);
		}
		
		@Override
		public void itemClick(ItemClickEvent event) {
				
		}
	}
	
	//Status cell
	class StatusCell extends VerticalLayout implements Serializable, ItemClickListener{

		private static final long serialVersionUID = 1L;
		Button orgchartBtn;
		Button historyBtn;
		Button tasksBtn;
		
		public StatusCell(Object emp){
//			orgchartBtn = new Button(Icon.sitemap+"");
//			orgchartBtn.setStyleName(BaseTheme.BUTTON_LINK);
//			orgchartBtn.setHtmlContentAllowed(true);
//			
//			historyBtn = new Button(Icon.time+"");
//			historyBtn.setStyleName(BaseTheme.BUTTON_LINK);
//			historyBtn.setHtmlContentAllowed(true);
//			
//			tasksBtn = new Button(Icon.exclamation_sign+"");
//			tasksBtn.setStyleName(BaseTheme.BUTTON_LINK);
//			tasksBtn.setHtmlContentAllowed(true);
			
			addComponent(orgchartBtn);
			addComponent(historyBtn);
			addComponent(tasksBtn);

		}
		
		@Override
		public void itemClick(ItemClickEvent event) {
			
		}
	}

