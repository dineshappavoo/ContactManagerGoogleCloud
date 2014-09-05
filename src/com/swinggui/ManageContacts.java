package com.swinggui;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 *@author Dinesh Appavoo
 */
public class ManageContacts extends JFrame {

    public JLabel firstName;
    public JLabel lastName;
    public JLabel middleInitial;
    public JLabel addressLine1;
    public JLabel addressLine2;
    public JLabel city;
    public JLabel state;
    public JLabel zipCode;
    public JLabel phoneNo;
    
    public JTextField firstNameText;
    public JTextField lastNameText;
    public JTextField middleInitialText;
    public JTextField addressLine1Text;
    public JTextField addressLine2Text;
    public JTextField cityText;
    public JComboBox stateCombo;
    public JTextField zipCodeText;
    public JTextField phoneNoText;

    public JLabel resultLabel;
    public JButton searchButton;
    public JButton deleteButton;
    public JButton addButton;
    public JButton updateButton;

    public JLabel infoLabel;
    
    String bookId="", borrowerName="", cardNo="";
    ArrayList<ContactBean> contactBean=new ArrayList<ContactBean>();

    
    public final JTable table;

    public ManageContacts() {

        super("CONTACT MANAGER");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 0));
        setMinimumSize(new Dimension(1000, 800));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.white);
    	
    	infoLabel=DisplayUtil.addInfoLabel("Tool Tip : Contact Manager helps to create new contacts,  " +
    			"modify an existing record and write it back to the file, delete a record from the file. All contacts are maintained in the file ");
    	
    	firstName = new JLabel("First Name :");
    	firstName.setBounds(GUIConstants._XPOS_LEFT, GUIConstants._YPOS, 100, 20);
    	firstNameText = new JTextField(20);
    	firstNameText.setBounds(GUIConstants._XPOS_RIGHT, GUIConstants._YPOS, 150, 20);
    	firstNameText.setColumns(20);
    	firstNameText.setToolTipText("First Name");

    	
    	middleInitial = new JLabel(" MI :");
        middleInitial.setBounds(GUIConstants._XPOS_LEFT+280, GUIConstants._YPOS, 40, 20);
        middleInitialText = new JTextField(2);
        middleInitialText.setBounds(GUIConstants._XPOS_RIGHT+200, GUIConstants._YPOS, 50, 20);
        middleInitialText.setColumns(1);
        middleInitialText.setToolTipText("Middle Name");

        lastName = new JLabel("Last Name :");
        lastName.setBounds(GUIConstants._XPOS_LEFT+380, GUIConstants._YPOS, 100, 20);
        lastNameText = new JTextField(20);
        lastNameText.setBounds(GUIConstants._XPOS_RIGHT+350, GUIConstants._YPOS, 150, 20);
        lastNameText.setColumns(20);  
        lastNameText.setToolTipText("Last Name");

        
        addressLine1 = new JLabel("Address Line 1 :");
        addressLine1.setBounds(GUIConstants._XPOS_LEFT, GUIConstants._YPOS+30, 120, 20);
        addressLine1Text = new JTextField(20);
        addressLine1Text.setBounds(GUIConstants._XPOS_RIGHT, GUIConstants._YPOS+30, 200, 20);
        addressLine1Text.setColumns(35);
        addressLine1Text.setToolTipText("Address Line 1");

        
        addressLine2 = new JLabel("Address Line 2 :");
        addressLine2.setBounds(GUIConstants._XPOS_LEFT, GUIConstants._YPOS+60, 120, 20);
        addressLine2Text = new JTextField(20);
        addressLine2Text.setBounds(GUIConstants._XPOS_RIGHT, GUIConstants._YPOS+60, 200, 20);
        addressLine2Text.setColumns(35);
        addressLine2Text.setToolTipText("Address Line 2");

        
        city = new JLabel("City :");
        city.setBounds(GUIConstants._XPOS_LEFT, GUIConstants._YPOS+90, 100, 20);
        cityText = new JTextField(20);
        cityText.setBounds(GUIConstants._XPOS_RIGHT, GUIConstants._YPOS+90, 180, 20);
        cityText.setColumns(20);
        cityText.setToolTipText("City");

        
        state = new JLabel("State :");
        state.setBounds(GUIConstants._XPOS_LEFT, GUIConstants._YPOS+120, 100, 20);
        stateCombo = new JComboBox(GUIConstants.stateList);
        stateCombo.setBounds(GUIConstants._XPOS_RIGHT, GUIConstants._YPOS+120, 180, 20);
        stateCombo.setToolTipText("State");

        
        zipCode = new JLabel("Zip Code :");
        zipCode.setBounds(GUIConstants._XPOS_LEFT, GUIConstants._YPOS+150, 100, 20);
        zipCodeText = new JTextField(20);
        zipCodeText.setBounds(GUIConstants._XPOS_RIGHT, GUIConstants._YPOS+150, 180, 20);
        zipCodeText.setColumns(9);
        zipCodeText.setToolTipText("Zip code");

        
        phoneNo = new JLabel("Phone Number :");
        phoneNo.setBounds(GUIConstants._XPOS_LEFT, GUIConstants._YPOS+180, 100, 20);
        phoneNoText = new JTextField(20);
        phoneNoText.setBounds(GUIConstants._XPOS_RIGHT, GUIConstants._YPOS+180, 180, 20);
        phoneNoText.setColumns(21);
        phoneNoText.setToolTipText("Phone Number");


        searchButton = new JButton("Search");
        searchButton.setBounds(GUIConstants._XPOS_LEFT+40, GUIConstants._YPOS+230, 120, 20);

            
        addButton = new JButton("Add");
        addButton.setBounds(GUIConstants._XPOS_LEFT, GUIConstants._YPOS+540, 120, 20);
        
        updateButton = new JButton("Update");
        updateButton.setBounds(GUIConstants._XPOS_LEFT+150, GUIConstants._YPOS+540, 120, 20);
        
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(GUIConstants._XPOS_LEFT+300, GUIConstants._YPOS+540, 100, 20);

        table = new JTable();

        table.setVisible(true);
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				
				


                int rowCount = table.getSelectedRow();
                if(rowCount >= 0)
                {
                
                ContactBean selectedMinimalObject=new ContactBean();
                
                selectedMinimalObject.setFirstName((String)table.getValueAt(rowCount, 0));
                selectedMinimalObject.setLastName((String)table.getValueAt(rowCount, 1));
                selectedMinimalObject.setMiddleInitial((String)table.getValueAt(rowCount, 2));
                
                ContactBean selectedObjectBean = ContactFileOperations.getSelectedContact(selectedMinimalObject);
                
                firstNameText.setText(selectedObjectBean.getFirstName());
                lastNameText.setText(selectedObjectBean.getLastName());
                middleInitialText.setText(selectedObjectBean.getMiddleInitial());
                addressLine1Text.setText(selectedObjectBean.getAddressLine1());
                addressLine2Text.setText(selectedObjectBean.getAddressLine2());
                cityText.setText(selectedObjectBean.getCity());
                stateCombo.setSelectedItem(selectedObjectBean.getState());
                zipCodeText.setText(selectedObjectBean.getZip());
                phoneNoText.setText(selectedObjectBean.getPhoneNo());
                
                }

			}
		});
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(GUIConstants._XPOS_LEFT, GUIConstants._YPOS+300, 600, 200);
        add(scrollPane);
        
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	table.clearSelection();
            	ContactBean contact=new ContactBean();
            	contact.setFirstName(firstNameText.getText());
            	contact.setLastName(lastNameText.getText());
            	contact.setMiddleInitial(middleInitialText.getText());
            	contact.setAddressLine1(addressLine1Text.getText());
            	contact.setAddressLine2(addressLine2Text.getText());
            	contact.setCity(cityText.getText());
            	contact.setState(stateCombo.getSelectedItem().toString());
            	contact.setZip(zipCodeText.getText());
            	contact.setPhoneNo(phoneNoText.getText());
            	
            	
            	//searchAction();
            	ContactFileOperations cf = new ContactFileOperations(table, contact);
            	try {
					cf.bindResultsetWithTable();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	
            }
        });
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	
            	ContactBean contact=new ContactBean();
            	contact.setFirstName(firstNameText.getText());
            	contact.setLastName(lastNameText.getText());
            	contact.setMiddleInitial(middleInitialText.getText());
            	contact.setAddressLine1(addressLine1Text.getText());
            	contact.setAddressLine2(addressLine2Text.getText());
            	contact.setCity(cityText.getText());
            	contact.setState(stateCombo.getSelectedItem().toString());
            	contact.setZip(zipCodeText.getText());
            	contact.setPhoneNo(phoneNoText.getText());
            	
            	
            	if(!(ContactFileOperations.isContactExists(contact)))
            	{
            	ContactFileOperations.addObjectToFile(contact);
            	init();
            	
            	}else
            	{
            		AlertUtil.msgbox("Contact already exists");
            	}
            	
            	
            	
            	
            }
        });
        
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
               int rowNum = table.getSelectedRow();
               
               if(rowNum>=0)
               {
                
                ContactBean tableBean=new ContactBean();
                
                tableBean.setFirstName((String)table.getValueAt(rowNum,0));
                tableBean.setLastName((String)table.getValueAt(rowNum, 1));
                tableBean.setMiddleInitial((String)table.getValueAt(rowNum, 2));
                
                ContactBean textBean = new ContactBean();
                
                textBean.setFirstName(firstNameText.getText());
                textBean.setLastName(lastNameText.getText());
                textBean.setMiddleInitial(middleInitialText.getText());
                textBean.setAddressLine1(addressLine1Text.getText());
                textBean.setAddressLine2(addressLine2Text.getText());
                textBean.setCity(cityText.getText());
                textBean.setState(stateCombo.getSelectedItem().toString());
                textBean.setZip(zipCodeText.getText());
                textBean.setPhoneNo(phoneNoText.getText());
                
                if(!(ContactFileOperations.isContactExists(textBean)))
            	{
            	ContactFileOperations.updateContact(textBean, tableBean);
            	init();
            	}else
            	{
            		AlertUtil.msgbox("Contact already exists");
            	}
            	
               }
            	
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int row = table.getSelectedRow();
                
                if(row>=0)
                {
                
                //int column = table.getSelectedColumn();
                ContactBean deleteContact=new ContactBean();
                deleteContact.setFirstName((String)table.getValueAt(row, 0));
                deleteContact.setLastName((String)table.getValueAt(row, 1));
                deleteContact.setMiddleInitial((String)table.getValueAt(row, 2));


                try {
                    ContactFileOperations.deleteContact(deleteContact);
                    init();

                    //searchAction();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                }
            
            	
            }
        });
        
        
        /*searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	
            	
            	searchAction();
            	
            }
        });*/
        
        
        setLayout(null);

        add(infoLabel);
        add(firstName);
        add(firstNameText);
        add(lastName);
        add(lastNameText);
        add(middleInitial);
        add(middleInitialText);
        add(addressLine1);
        add(addressLine1Text);
        add(addressLine2);
        add(addressLine2Text);
        add(city);
        add(cityText);
        add(state);
        add(stateCombo);
        add(zipCode);
        add(zipCodeText);
        add(phoneNo);
        add(phoneNoText);
        add(searchButton);
        add(deleteButton);
        add(addButton);
        add(updateButton);

        setVisible(true);
        init();
    }

	public void init() {
		ContactFileOperations cfo = new ContactFileOperations(table, null);
        try {
			cfo.bindResultsetWithTable();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

   /* public void searchAction()
    {
    	ArrayList<ContactBean> fileObjectList=ContactFileOperations.readObjectFromFile();
    	    int rowCount= fileObjectList.size();
    	    int colCount=4;

    	
    	
    }*/
    
    
    public static void main(String args[]) {
       new ManageContacts();
    }
}

