package com.flow.pjm.component;

import java.io.Serializable;
import java.util.logging.Logger;

import com.flow.pjm.component.TbCells.TableCellListner;
import com.flow.shell.component.PagedTable;
import com.vaadin.ui.Table;

public class QnATable extends PagedTable implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(QnATable.class.getName());

	final TableCellListner Listner;
	
	public QnATable() {
		this(null);
	}

	public QnATable(TableCellListner listner){
			super();
			
			this.Listner = listner;
					
			this.setWidth("100%");
			
			this.setPrimaryStyleName("flat-table");
						
			this.addGeneratedColumn("Employee", new ColumnGenerator() {
				private static final long serialVersionUID = 1L;
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
//					@SuppressWarnings("unchecked")
//					BeanItem<Employee> item = (BeanItem<Employee>) source.getItem(itemId);					
//					Employee emp = (Employee) item.getBean();
					return new QnACell(null,itemId, Listner);
				}
			});
			
			this.addGeneratedColumn("Employment", new ColumnGenerator() {
				private static final long serialVersionUID = 1L;
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
//					BeanItem<Employee> item = (BeanItem<Employee>) source.getItem(itemId);					
//					Employee emp = (Employee) item.getBean();
					return new EmploymentCell(null);
				}
			});
			
			this.addGeneratedColumn("Contact", new ColumnGenerator() {
				private static final long serialVersionUID = 1L;
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
//					BeanItem<Employee> item = (BeanItem<Employee>) source.getItem(itemId);					
//					Employee emp = (Employee) item.getBean();
					return new ContactCell(null);
				}
			});
			
			this.addGeneratedColumn("Role", new ColumnGenerator() {
				private static final long serialVersionUID = 1L;
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
//					BeanItem<Employee> item = (BeanItem<Employee>) source.getItem(itemId);					
//					Employee emp = (Employee) item.getBean();
					return new RoleCell(null);
				}
			});
			
			this.addGeneratedColumn("Status", new ColumnGenerator() {
				private static final long serialVersionUID = 1L;
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
//					BeanItem<Employee> item = (BeanItem<Employee>) source.getItem(itemId);					
//					Employee emp = (Employee) item.getBean();
					return new StatusCell(null);
				}
			});
			
			//this.setColumnHeaders(new String[] { "Employee", "Employment","Contact","Role","Status"});
			this.setVisibleColumns(new String[] {"Employee", "Employment","Contact","Role","Status"});
			
			this.setColumnWidth("Employee", 300);
			this.setColumnWidth("Employment", 250);
			this.setColumnWidth("Contact", 250);
			this.setColumnWidth("Role", 125);
			this.setColumnWidth("Status", 135);
		
			this.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
			this.setPageLength(5);
			
	}

	public void setColumns() {
		this.setVisibleColumns(new String[] {"Employee", "Employment","Contact","Role","Status"});		
	}

		
	

}
