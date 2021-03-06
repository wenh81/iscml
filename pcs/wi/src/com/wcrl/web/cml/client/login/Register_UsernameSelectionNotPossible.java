/*
 * File: JobDetails.java

Purpose: To display the job attributes and provide links to input and output files.
**********************************************************/

package com.wcrl.web.cml.client.login;

import java.util.HashMap;
import java.util.Map;
import com.allen_sauer.gwt.log.client.Log;
import com.claudiushauptmann.gwt.recaptcha.client.RecaptchaWidget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.wcrl.web.cml.client.custom.PromptedTextBox;
import com.wcrl.web.cml.client.loginService.RegisterService;
import com.wcrl.web.cml.client.loginService.RegisterServiceAsync;
import com.wcrl.web.cml.client.loginService.RegistrationRequestEmailService;
import com.wcrl.web.cml.client.loginService.RegistrationRequestEmailServiceAsync;

public class Register_UsernameSelectionNotPossible extends Composite implements ClickHandler 
{
	private VerticalPanel panel;
	private HorizontalPanel buttonPanel, registerPanel;
	private FlexTable table;	
	private Button btnSave, btnCancel;	
	private HTML txtWarnings;	
	//private TextBox txtFirstName;
	//private TextBox txtLastName;
	private PromptedTextBox txtFirstName;
	private PromptedTextBox txtLastName;
	private TextBox txtUserName;
	private TextBox txtEmail;
	private TextBox txtConfirmEmail;
	private TextBox txtCountry;
	private TextBox txtOrganization;
	private TextBox txtJobTitle;
	private RecaptchaWidget rw;
	private Anchor loginAnchor;
	
	public Register_UsernameSelectionNotPossible() 
	{
		panel = new VerticalPanel();
		buttonPanel = new HorizontalPanel();
		registerPanel = new HorizontalPanel();
		table = new FlexTable();
		loginAnchor = new Anchor("<<Login");
		loginAnchor.addClickHandler(this);
		
		sinkEvents(Event.ONPASTE);
		
		//txtFirstName = new TextBox();
		//txtLastName = new TextBox();
		txtFirstName = new PromptedTextBox("First", "prompt");
		txtLastName = new PromptedTextBox("Last", "prompt");
		txtUserName = new TextBox();
		txtCountry = new TextBox();
		txtEmail = new TextBox();
		txtConfirmEmail = new TextBox();
		txtOrganization = new TextBox();
		txtJobTitle = new TextBox();
		
		txtFirstName.setName("firstName");
		txtLastName.setName("lastName");
		txtUserName.setName("userName");
		txtCountry.setName("country");
		txtEmail.setName("email");
		txtConfirmEmail.setName("confirmEmail");
		txtOrganization.setName("organization");
		txtJobTitle.setName("jobTitle");
						
		btnSave = new Button("Register");
		btnCancel = new Button("Cancel");

		txtWarnings = new HTML();

		table.setCellSpacing(5);
		
		table.setCellPadding(0);
		table.setWidth("100%");
		registerPanel.setSize("100%", "100%");

		//txtWarnings.setVisible(false);

		btnSave.addClickHandler(this);
		btnCancel.addClickHandler(this);
				
		buttonPanel.add(btnSave);
		buttonPanel.add(btnCancel);

		setTableRowWidget(0, 0, new HTML("<b>Name:</b>&nbsp;&nbsp;&nbsp;"));
		//setTableRowWidget(1, 0, new HTML("<b>Username:</b>&nbsp;&nbsp;&nbsp;"));
		setTableRowWidget(2, 0, new HTML("<b>Email:</b>&nbsp;&nbsp;&nbsp;"));
		setTableRowWidget(3, 0, new HTML("<b>Confirm Email:</b>&nbsp;&nbsp;&nbsp;"));
		setTableRowWidget(4, 0, new HTML("<b>Organization:</b>&nbsp;&nbsp;&nbsp;"));
		setTableRowWidget(5, 0, new HTML("<b>Professional Title:</b>&nbsp;&nbsp;&nbsp;"));
		setTableRowWidget(6, 0, new HTML("<b>Country:</b>&nbsp;&nbsp;&nbsp;"));
		setTableRowWidget(7, 0, new HTML("<b>Prove you are a human:</b>&nbsp;&nbsp;&nbsp;"));
				
		HorizontalPanel namePanel = new HorizontalPanel();
		namePanel.add(txtFirstName);
		namePanel.add(new HTML("&nbsp;&nbsp;&nbsp;"));
		namePanel.add(txtLastName);
		
		rw = new RecaptchaWidget("6LeoPwYAAAAAAEgl-99fWvVvzRQObu5UoTPoQtg1");
		
		setTableRowWidgetVal(0, 1, namePanel);
		//setTableRowWidgetVal(1, 1, txtUserName);
		setTableRowWidgetVal(2, 1, txtEmail);
		setTableRowWidgetVal(3, 1, txtConfirmEmail);
		setTableRowWidgetVal(4, 1, txtOrganization);
		setTableRowWidgetVal(5, 1, txtJobTitle);
		setTableRowWidgetVal(6, 1, txtCountry);
		setTableRowWidgetVal(7 , 1, rw);
		setTableRowWidgetVal(8, 1, buttonPanel);
				
		panel.add(loginAnchor);
		panel.add(txtWarnings);
		txtWarnings.setStylePrimaryName("progressbar-text");
		txtWarnings.setStylePrimaryName("warnings");	
		panel.add(table);
		registerPanel.add(panel);
		registerPanel.setCellHorizontalAlignment(panel, HasHorizontalAlignment.ALIGN_CENTER);
		
		initWidget(registerPanel);

		setStyleName("mail-Detail");
	}
	
	public void onBrowserEvent(Event event)
	{
		super.onBrowserEvent(event);
		switch(event.getTypeInt())
		{
			case Event.ONPASTE :
			{
				event.stopPropagation();
				event.preventDefault();
				break;
			}
		}
	}  

	public void onClick(ClickEvent event) 
	{
		Widget widget = (Widget) event.getSource();	

		if (widget == btnSave) 
		{			
			txtWarnings.setText("");
			txtFirstName.setStyleName("warnings", false);
			txtLastName.setStyleName("warnings", false);
			txtCountry.setStyleName("warnings", false);
			txtOrganization.setStyleName("warnings", false);
			txtJobTitle.setStyleName("warnings", false);
			txtEmail.setStyleName("warnings", false);
			txtConfirmEmail.setStyleName("warnings", false);
			txtUserName.setStyleName("warnings", false);			
			
			//String usernameRegex = "^[A-Za-z][A-Za-z0-9._]+";
			String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
			String regex = "^[A-Za-z][A-Za-z0-9._ ]+";
			Map<String, String> data = getDataAsMap();
			boolean bool = true;
			//if((txtFirstName.getText().trim().length() <= 0) || (txtLastName.getText().trim().length() <= 0) || (txtUserName.getText().trim().length() <= 0) || (txtCountry.getText().trim().length() <= 0) || (txtEmail.getText().trim().length() <= 0) || (txtConfirmEmail.getText().trim().length() <= 0) || (txtOrganization.getText().trim().length() <= 0) || (txtJobTitle.getText().trim().length() <= 0))
			if((txtFirstName.getText().trim().length() <= 0) || (txtLastName.getText().trim().length() <= 0) || (txtCountry.getText().trim().length() <= 0) || (txtEmail.getText().trim().length() <= 0) || (txtConfirmEmail.getText().trim().length() <= 0) || (txtOrganization.getText().trim().length() <= 0) || (txtJobTitle.getText().trim().length() <= 0))
			{
				txtWarnings.setText("Please enter all the fields.");
				bool = false;
			}
			else
			{
				if(!txtFirstName.getValue().matches(regex))
				{
					txtFirstName.addStyleName("warnings");
					bool = false;
				}
				if(!txtLastName.getValue().matches(regex))
				{
					txtLastName.addStyleName("warnings");
					bool = false;
				}
				if(!txtCountry.getValue().matches(regex))
				{
					txtCountry.addStyleName("warnings");
					bool = false;
				}
				if(!txtOrganization.getValue().matches(regex))
				{
					txtOrganization.addStyleName("warnings");
					bool = false;
				}
				if(!txtJobTitle.getValue().matches(regex))
				{
					txtJobTitle.addStyleName("warnings");
					bool = false;
				}
				if(!txtEmail.getValue().matches(emailRegex))
				{
					txtEmail.addStyleName("warnings");
					bool = false;
				}
				if(!txtConfirmEmail.getValue().matches(emailRegex))
				{
					txtConfirmEmail.addStyleName("warnings");
					bool = false;
				}
				if(!txtConfirmEmail.getText().trim().equals(txtEmail.getText().trim()))
				{
					txtWarnings.setText("Email doesn't match.");
					bool = false;
				}
				/*if(!txtUserName.getValue().matches(usernameRegex) || (!(txtUserName.getText().trim().length() >= 6 && txtUserName.getText().trim().length() <= 16)))
				{
					txtUserName.addStyleName("warnings");
					bool = false;
				}*/
				if(bool)
				{		
					Log.info("Client challenge " + rw.getChallenge() + " Response: " + rw.getResponse());
					RegisterServiceAsync service = RegisterService.Util.getInstance();					
					service.register(data, rw.getChallenge(), rw.getResponse(), registerCallback);
				}
			}			
		}
		if (widget == btnCancel) 
		{
			Login login = new Login();
			login.displayLoginBox();
		}		
		if (widget == loginAnchor) 
		{
			Login login = new Login();
			login.displayLoginBox();
		}
	}
	
	public Map<String, String> getDataAsMap()
	{		
		Map<String, String> data = new HashMap<String, String>();
		
		data.put(txtFirstName.getName(), txtFirstName.getText().trim());
		data.put(txtLastName.getName(), txtLastName.getText().trim());
		String email = txtEmail.getText().trim();
		String[] tokens = email.split("@");
		String username = tokens[0].trim();
		//userName
		data.put("userName", username);
		//data.put(txtUserName.getName(), txtUserName.getText().trim());
		data.put(txtCountry.getName(), txtCountry.getText().trim());		
		data.put(txtEmail.getName(), txtEmail.getText().trim());
		data.put(txtOrganization.getName(), txtOrganization.getText().trim());
		data.put(txtJobTitle.getName(), txtJobTitle.getText().trim());
		return data;		
	}

	private void setTableRowWidget(int row, int column, Widget widget)
	{
		table.setWidget(row, column, widget);
		table.getCellFormatter().setHorizontalAlignment(row, column, HasHorizontalAlignment.ALIGN_RIGHT);
		table.getCellFormatter().setVerticalAlignment(row, column, HasVerticalAlignment.ALIGN_TOP);		
	}
	
	private void setTableRowWidgetVal(int row, int column, Widget widget)
	{
		table.setWidget(row, column, widget);
		table.getCellFormatter().setHorizontalAlignment(row, column, HasHorizontalAlignment.ALIGN_LEFT);
		table.getCellFormatter().setVerticalAlignment(row, column, HasVerticalAlignment.ALIGN_TOP);		
	}
	
	AsyncCallback<Integer> registerCallback = new AsyncCallback<Integer>()
	{
		public void onFailure(Throwable caught) 
		{
			Log.info("AddProject addProjectCallback: " + caught.toString());
		}
		public void onSuccess(Integer result) 
		{			
			int value = (Integer)result;
			if(value == 0)
			{
				RegistrationRequestEmailServiceAsync service = RegistrationRequestEmailService.Util.getInstance();					
				//service.sendEmail(txtFirstName.getText().trim(), txtLastName.getText().trim(), txtEmail.getText().trim(), sendEmailCallback);				
			}
			if(value == 1)
			{
				txtUserName.setText("");
				rw.reload();
				txtWarnings.setText("The username is not available. Please provide a different email address.");
			}
			if(value == 2)
			{
				rw.reload();
				txtWarnings.setText("Please enter the correct text.");
			}
			if(value == 3)
			{
				txtUserName.setText("");
				rw.reload();
				txtWarnings.setText("Please enter the correct text and provide a different email address.");
			}									
		}	
	};
	
	AsyncCallback<Boolean> sendEmailCallback = new AsyncCallback<Boolean>()
	{
		public void onFailure(Throwable caught) 
		{
			Log.info("AddProject addProjectCallback: " + caught.toString());
		}
		public void onSuccess(Boolean result) 
		{		
			if(result)
			{
				txtWarnings.setText("Request submitted. Once your username is approved a confirmation email will be sent to you.");
			}
			else
			{
				txtWarnings.setText("Error is creating reuqest. Please try again later.");
			}
			/*Login login = new Login();
			login.displayLoginBox();*/
		}
	};
}