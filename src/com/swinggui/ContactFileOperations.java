
package com.swinggui;

/**
 * 
 */


import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;



/**
 * @author Dany
 *
 */
public class ContactFileOperations{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	JTable table = null;
    List<ContactBean> resultSet = null;
    public static Connection con = null;

    public ContactFileOperations(JTable table, ContactBean contactBean) {
        this.table = table;
        fetchSearchResults(contactBean);
    }


    private void fetchSearchResults(ContactBean contactBean) {
    	resultSet = readObjectFromFile(contactBean);
	}


	protected JTable bindResultsetWithTable() throws Exception {
        List<Record> records = new ArrayList<Record>();
        Object[] values;
        for (ContactBean cb : resultSet) {
            values = new Object[4];
            values[0] = cb.getFirstName();
            values[1] = cb.getLastName();
            values[2] = cb.getMiddleInitial();
            values[3] = cb.getPhoneNo();
            Record record = new Record(values);
            records.add(record);
        }
        process(records);
        return this.table;
    }

    protected void process(List<Record> chunks) {
        ResultSetTableModel tableModel = (this.table.getModel() instanceof ResultSetTableModel ? (ResultSetTableModel) this.table.getModel() : null);
        if (tableModel == null) {
            tableModel = new ResultSetTableModel(chunks);
            this.table.setModel(tableModel);
        } else {
            tableModel.getRecords().clear();
            tableModel.getRecords().addAll(chunks);
        }
        tableModel.fireTableDataChanged();
    }
	
	
	
	
	
	
	
	
	public static void addObjectToFile(ContactBean contact)
	{
		System.out.println("Test  1");
		GoogleCloudStorageOperations.addContactToSimpleDB(contact);
		
	}


	public static ArrayList<ContactBean> readObjectFromFile(ContactBean contactBean)
	{
		
		boolean searchDataflag=false;
		
		if(contactBean==null)
		{
			searchDataflag=true;
		}else
		{
		if(contactBean.getFirstName().equals("")&&contactBean.getLastName().equals("")&&contactBean.getMiddleInitial().equals("")&&contactBean.getCity().equals("")&&contactBean.getAddressLine1().equals("")&&contactBean.getAddressLine2().equals("")&&contactBean.getZip().equals("")&&contactBean.getPhoneNo().equals(""))
		{
			searchDataflag=true;
		}
		}
		
		ArrayList<ContactBean> fileObjectList=new ArrayList<ContactBean>();
		
		ArrayList<ContactBean> googleCloudContactList=GoogleCloudStorageOperations.retrieveItem();
		
		for(ContactBean obj1 : googleCloudContactList)
        {
       	 System.out.println("First Name From DB : "+obj1.getFirstName());
        }
		System.out.println("Test Dinesh");
		 
	             for(ContactBean loadedObj : googleCloudContactList) {
	            	  if(searchDataflag)
	            	  {
	              fileObjectList.add(loadedObj);
	            	  }else
	            	  {
	            		 
	            		  if( (!contactBean.getFirstName().equals(""))&&(contactBean.getFirstName().equals(loadedObj.getFirstName())) || 
	          					(!contactBean.getLastName().equals(""))&&(contactBean.getLastName().equals(loadedObj.getLastName())) ||
	          					(!contactBean.getMiddleInitial().equals(""))&&(contactBean.getMiddleInitial().equals(loadedObj.getMiddleInitial())) ||
	          					(!contactBean.getAddressLine1().equals(""))&&(contactBean.getAddressLine1().equals(loadedObj.getAddressLine1())) ||
	          					(!contactBean.getAddressLine2().equals(""))&&(contactBean.getAddressLine2().equals(loadedObj.getAddressLine2())) ||
	          					(!contactBean.getCity().equals(""))&&(contactBean.getCity().equals(loadedObj.getCity())) ||
	          					(!contactBean.getState().equals(""))&&(contactBean.getState().equals(loadedObj.getState())) ||
	          					(!contactBean.getZip().equals(""))&&(contactBean.getZip().equals(loadedObj.getZip())) ||
	          					(!contactBean.getPhoneNo().equals(""))&&(contactBean.getPhoneNo().equals(loadedObj.getPhoneNo()))
	          					)
	          			{
	        	              fileObjectList.add(loadedObj);

	          			}
	            		  
	            		  
	            		  
	            	  }
	            }
	             
	             for(ContactBean obj : fileObjectList)
	             {
	            	 System.out.println("FirstName : "+obj.getFirstName());
	             }
	              return fileObjectList;
	           
	}
	
	public static void deleteContact(ContactBean deleteContact)
	{
		GoogleCloudStorageOperations.deleteContact(deleteContact);
		
	}
	
	
	public static ContactBean getSelectedContact(ContactBean selectContactBean)
	{
		return  GoogleCloudStorageOperations.getSelectedBean(selectContactBean);
		
		
	}
	
	
	public static void updateContact(ContactBean textBean, ContactBean tableBean)
	{

		File f= new File(GUIConstants._FILE_NAME);

		ArrayList<ContactBean> fileObjectList=new ArrayList<ContactBean>();
		 try {
			 FileInputStream fi = new FileInputStream(f);
			 
	          ObjectInputStream ois = new ObjectInputStream(fi);
	           try{
	              ContactBean loadedObj = null;
	              while ((fi.available()>0) && (loadedObj = (ContactBean) ois.readObject()) != null) {
	              fileObjectList.add(loadedObj);
	            }
	           } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
	               ois.close();
	           }

	       } catch (StreamCorruptedException e) {
	           e.printStackTrace();
	       } catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			
			f.delete();
			f = new File(GUIConstants._FILE_NAME);
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(new FileOutputStream(f, true));
		

		 for(ContactBean o : fileObjectList)
		 {
			 if(o.getFirstName().equals(tableBean.getFirstName())&&o.getLastName().equals(tableBean.getLastName())&&o.getMiddleInitial().equals(tableBean.getMiddleInitial()))
			 {
				 oos.writeObject(textBean);
			 }else
			 {

				 oos.writeObject(o);

			 }
		 }
			oos.close();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	
	
	public static boolean isContactExists(ContactBean contact)
	{
		File f= new File(GUIConstants._FILE_NAME);
		ArrayList<ContactBean> fileObjectList=new ArrayList<ContactBean>();
		 try {
			 FileInputStream fi = new FileInputStream(f);
			 
	          ObjectInputStream ois = new ObjectInputStream(fi);
	           try{
	              ContactBean loadedObj = null;
	              while ((fi.available()>0) && (loadedObj = (ContactBean) ois.readObject()) != null) {
	            	  
	            	  if(loadedObj.getFirstName().equals(contact.getFirstName())&&loadedObj.getLastName().equals(contact.getLastName())&&loadedObj.getMiddleInitial().equals(contact.getMiddleInitial()))
	     			 {
	            		  return true;
	            	}
	              //fileObjectList.add(loadedObj);
	            }
	           } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
	               ois.close();
	           }
	       } catch (StreamCorruptedException e) {
	           e.printStackTrace();
	       } catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		 return false;
	}
	

}
