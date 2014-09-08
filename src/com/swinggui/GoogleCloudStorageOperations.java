/**
 * 
 */
package com.swinggui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.java.StorageSample;
import main.java.StorageSampleNew.SampleSettings;

import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.DeleteAttributesRequest;
import com.amazonaws.services.simpledb.model.DeleteDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Lists;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.ObjectAccessControl;
import com.google.api.services.storage.model.StorageObject;
import com.google.appengine.labs.repackaged.com.google.common.collect.ImmutableList;
import com.google.appengine.labs.repackaged.com.google.common.collect.ImmutableMap;

/**
 * @author Dany
 *
 */
public class GoogleCloudStorageOperations {

	/**
	 * @param args
	 */
	static String newDomain="contactsSimpleDB";
	//static AmazonSimpleDB sdb = getDomain(newDomain);
	  /**
	   * Be sure to specify the name of your application. If the application name is {@code null} or
	   * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
	   */
	  private static final String APPLICATION_NAME = "ContactManager";
	  private static final String BUCKET_NAME = "student_contact";

	  /** Directory to store user credentials. */
	  private static final java.io.File DATA_STORE_DIR =
	      new java.io.File(System.getProperty("user.home"), ".store/storage_sample");

	  /**
	   * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
	   * globally shared instance across your application.
	   */
	  private static FileDataStoreFactory dataStoreFactory;

	  /** Global instance of the JSON factory. */
	  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	  /** Global instance of the HTTP transport. */
	  private static HttpTransport httpTransport;

	  private static Storage client;

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	
	public static ArrayList<ContactBean> retrieveItem()
	{
		ArrayList<ContactBean> contactlist=new ArrayList<ContactBean>();

		Storage.Buckets.Get getBucket = client.buckets().get(BUCKET_NAME);
        getBucket.setProjection("full");
        Bucket bucket = getBucket.execute();
        
        // List the contents of the bucket.
        Storage.Objects.List listObjects = client.objects().list(BUCKET_NAME);
        com.google.api.services.storage.model.Objects objects;
        
        do {
          objects = listObjects.execute();
          List<StorageObject> items = objects.getItems();
          if (null == items) {
            System.out.println("There were no objects in the given bucket; try adding some and re-running.");
            break;
          }
          for (Object object : items) {
        	  ContactBean contactObject=(ContactBean) object;
        	  contactlist.add((ContactBean)object);
            System.out.println(((StorageObject) object).getName() + " (" + ((StorageObject) object).getSize() + " bytes)");
          }
          listObjects.setPageToken(objects.getNextPageToken());
        } while (null != objects.getNextPageToken());
        

		return contactlist;
	}
	
	public static ContactBean getSelectedBean(ContactBean contact)
	{
		String selectString = "select * from "+newDomain+ " where FirstName="+contact.getFirstName()+" and SecondName="+contact.getLastName()+" and MiddleInitial="+contact.getMiddleInitial();
		
		AmazonSimpleDB sdb = getDomain(newDomain);
				
		SelectRequest select = new SelectRequest(selectString);
		ArrayList<ContactBean> contactlist=new ArrayList<ContactBean>();

		for(Item item : sdb.select(select).getItems())
		{
			
			System.out.println("Item Name : "+item.getName());
			ContactBean contactbean = new ContactBean();
			for(Attribute attribute : item.getAttributes())
			{
			
			System.out.println("Name :"+attribute.getName()+" Value : "+attribute.getValue());
			if(attribute.getName().equals("FirstName"))
				contactbean.setFirstName(attribute.getValue());
			else if(attribute.getName().equals("SecondName"))
				contactbean.setLastName(attribute.getValue());
			else if(attribute.getName().equals("MiddleInitial"))
				contactbean.setMiddleInitial(attribute.getValue());
			else if(attribute.getName().equals("AddressLine1"))
				contactbean.setAddressLine1(attribute.getValue());
			else if(attribute.getName().equals("AddressLine2"))
				contactbean.setAddressLine2(attribute.getValue());
			else if(attribute.getName().equals("City"))
				contactbean.setCity(attribute.getValue());
			else if(attribute.getName().equals("State"))
				contactbean.setState(attribute.getValue());
			else if(attribute.getName().equals("Zip"))
				contactbean.setZip(attribute.getValue());
			else if(attribute.getName().equals("PhoneNumber"))
				contactbean.setPhoneNo(attribute.getValue());
			}
			contactlist.add(contactbean);

		}
		
		return contactlist.get(0);
	}
	
	
	public static void deleteContact(ContactBean deleteContactBean)
	{
		String objectToBeDeleted="";
		
		ArrayList<ContactBean> contactList=new ArrayList<ContactBean>();
		Storage.Buckets.Get getBucket = client.buckets().get(BUCKET_NAME);
        getBucket.setProjection("full");
        Bucket bucket = getBucket.execute();
        
        // List the contents of the bucket.
        Storage.Objects.List listObjects = client.objects().list(BUCKET_NAME);
        com.google.api.services.storage.model.Objects objects;
        
        do {
          objects = listObjects.execute();
          List<StorageObject> items = objects.getItems();
          if (null == items) {
            System.out.println("There were no objects in the given bucket; try adding some and re-running.");
            break;
          }
          for (Object object : items) {
        	  ContactBean contactObject=(ContactBean) object;

        	  if(contactObject==deleteContactBean)
        	  {
        		  objectToBeDeleted=((StorageObject)object).getName();
        	  }
          }
          listObjects.setPageToken(objects.getNextPageToken());
        } while (null != objects.getNextPageToken());
		
		client.objects().delete(BUCKET_NAME, objectToBeDeleted).execute();

	}
	
	/** Authorizes the installed application to access user's protected data. */
	private static Credential authorize() throws Exception {
	    // Load client secrets.
	        GoogleClientSecrets clientSecrets = null;
	        try {
	            System.out.println("Test1");

	        clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
	            new InputStreamReader(GoogleCloudStorageOperations.class.getResourceAsStream("/main/resources/client_secrets.json")));
	        System.out.println("Test");
	        if (clientSecrets.getDetails().getClientId() == null ||
	                clientSecrets.getDetails().getClientSecret() == null) {
	                throw new Exception("client_secrets not well formed.");
	        }
	        } catch (Exception e) {
	                System.out.println("Problem loading client_secrets.json file. Make sure it exists, you are " + 
	                                   "loading it with the right path, and a client ID and client secret are " +
	                                   "defined in it.\n" + e.getMessage());
	                System.exit(1);
	        }

	    // Set up authorization code flow.
	    // Ask for only the permissions you need. Asking for more permissions will
	    // reduce the number of users who finish the process for giving you access
	    // to their accounts. It will also increase the amount of effort you will
	    // have to spend explaining to users what you are doing with their data.
	    // Here we are listing all of the available scopes. You should remove scopes
	    // that you are not actually using.
	    Set<String> scopes = new HashSet<String>();
	    scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);
	    scopes.add(StorageScopes.DEVSTORAGE_READ_ONLY);
	    scopes.add(StorageScopes.DEVSTORAGE_READ_WRITE);

	    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	        httpTransport, JSON_FACTORY, clientSecrets, scopes)
	        .setDataStoreFactory(dataStoreFactory)
	        .build();
	    // Authorize.
	    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("dinesha.cit");
	  }
	
	public static void getDomain(String domain)
	{
	    try {
	        // Initialize the transport.
	        httpTransport = GoogleNetHttpTransport.newTrustedTransport();

	        // Initialize the data store factory.
	        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

	        // Authorization.
	        Credential credential = authorize();

	        // Set up global Storage instance.
	        client = new Storage.Builder(httpTransport, JSON_FACTORY, credential)
	            .setApplicationName(APPLICATION_NAME).build();

	        // Get metadata about the specified bucket.
	        Storage.Buckets.Get getBucket = client.buckets().get(BUCKET_NAME);
	        getBucket.setProjection("full");
	        Bucket bucket = getBucket.execute();
	        System.out.println("name: " + BUCKET_NAME);
	        System.out.println("location: " + bucket.getLocation());
	        System.out.println("timeCreated: " + bucket.getTimeCreated());
	        System.out.println("owner: " + bucket.getOwner());
	        
	        // List the contents of the bucket.
	        Storage.Objects.List listObjects = client.objects().list(BUCKET_NAME);
	        com.google.api.services.storage.model.Objects objects;
	        do {
	          objects = listObjects.execute();
	          List<StorageObject> items = objects.getItems();
	          if (null == items) {
	            System.out.println("There were no objects in the given bucket; try adding some and re-running.");
	            break;
	          }
	          for (StorageObject object : items) {
	            System.out.println(object.getName() + " (" + object.getSize() + " bytes)");
	          }
	          listObjects.setPageToken(objects.getNextPageToken());
	        } while (null != objects.getNextPageToken());

	      } catch (IOException e) {
	        System.err.println(e.getMessage());
	      } catch (Throwable t) {
	        t.printStackTrace();
	      }
	      System.exit(1);
	    }
	
	private static void uploadObject(boolean useCustomMetadata) throws IOException {
	    final long objectSize = 100 * 1000 * 1000 /* 100 MB */;
	    InputStreamContent mediaContent = new InputStreamContent(
	        "application/octet-stream", new RandomDataBlockInputStream(objectSize, 1024));
	    // Not strictly necessary, but allows optimization in the cloud.
	    // mediaContent.setLength(OBJECT_SIZE);

	    StorageObject objectMetadata = null;

	    StorageObject settings;
		if (useCustomMetadata) {
	      // If you have custom settings for metadata on the object you want to set
	      // then you can allocate a StorageObject and set the values here. You can
	      // leave out setBucket(), since the bucket is in the insert command's
	      // parameters.
	      List<ObjectAccessControl> acl = Lists.newArrayList();
	      if (settings.getEmail() != null && !settings.getEmail().isEmpty()) {
	        acl.add(
	            new ObjectAccessControl().setEntity("user-" + settings.getEmail()).setRole("OWNER"));
	      }
	      if (settings.getDomain() != null && !settings.getDomain().isEmpty()) {
	        acl.add(new ObjectAccessControl().setEntity("domain-" + settings.getDomain())
	            .setRole("READER"));
	      }
	      objectMetadata = new StorageObject().setName(settings.getPrefix() + "myobject")
	          .setMetadata(ImmutableMap.of("key1", "value1", "key2", "value2")).setAcl(acl)
	          .setContentDisposition("attachment");
	    }

	    Storage.Objects.Insert insertObject =
	    		client.objects().insert(settings.getBucket(), objectMetadata, mediaContent);

	    if (!useCustomMetadata) {
	      // If you don't provide metadata, you will have specify the object
	      // name by parameter. You will probably also want to ensure that your
	      // default object ACLs (a bucket property) are set appropriately:
	      // https://developers.google.com/storage/docs/json_api/v1/buckets#defaultObjectAcl
	      insertObject.setName(settings.getPrefix() + "myobject");
	    }

	    insertObject.getMediaHttpUploader()
	        .setProgressListener(new CustomUploadProgressListener()).setDisableGZipContent(true);
	    // For small files, you may wish to call setDirectUploadEnabled(true), to
	    // reduce the number of HTTP requests made to the server.
	    if (mediaContent.getLength() > 0 && mediaContent.getLength() <= 2 * 1000 * 1000 /* 2MB */) {
	      insertObject.getMediaHttpUploader().setDirectUploadEnabled(true);
	    }
	    insertObject.execute();
	  }

	public static void addContactToSimpleDB(ContactBean contact, boolean useCustomMetadata)
	{
		InputStream inputStream;  // object data, e.g., FileInputStream
		long byteCount;  // size of input stream

		InputStreamContent mediaContent = new InputStreamContent("application/octet-stream", (InputStream)contact);
		// Knowing the stream length allows server-side optimization, and client-side progress
		// reporting with a MediaHttpUploaderProgressListener.
		mediaContent.setLength(byteCount);

		StorageObject objectMetadata = null;

		if (useCustomMetadata) {
		  // If you have custom settings for metadata on the object you want to set
		  // then you can allocate a StorageObject and set the values here. You can
		  // leave out setBucket(), since the bucket is in the insert command's
		  // parameters.
		  objectMetadata = new StorageObject()
		      .setName("myobject")
		      .setMetadata(ImmutableMap.of("key1", "value1", "key2", "value2"))
		      .setAcl(ImmutableList.of(
		          new ObjectAccessControl().setEntity("domain-example.com").setRole("READER"),
		          new ObjectAccessControl().setEntity("user-administrator@example.com").setRole("OWNER")
		          ))
		      .setContentDisposition("attachment");
		}

		Storage.Objects.Insert insertObject = client.objects().insert("mybucket", objectMetadata,
		    contact);

		if (!useCustomMetadata) {
		  // If you don't provide metadata, you will have specify the object
		  // name by parameter. You will probably also want to ensure that your
		  // default object ACLs (a bucket property) are set appropriately:
		  // https://developers.google.com/storage/docs/json_api/v1/buckets#defaultObjectAcl
		  insertObject.setName("myobject");
		}

		// For small files, you may wish to call setDirectUploadEnabled(true), to
		// reduce the number of HTTP requests made to the server.
		if (mediaContent.getLength() > 0 && mediaContent.getLength() <= 2 * 1000 * 1000 /* 2MB */) {
		  insertObject.getMediaHttpUploader().setDirectUploadEnabled(true);
		}

		insertObject.execute();
		
		
		AmazonSimpleDB sdb=getDomain(newDomain);
		sdb.batchPutAttributes(new BatchPutAttributesRequest(newDomain, addItem(contact)));

	}
	
	public static ArrayList<ReplaceableItem> addItem(ContactBean contact)
	{
		ArrayList<ReplaceableItem> contactList=new ArrayList<ReplaceableItem>();
		
		String itemName=contact.getFirstName()+contact.getLastName()+contact.middleInitial;
		contactList.add(new ReplaceableItem(itemName).withAttributes(
				new ReplaceableAttribute("FirstName", contact.getFirstName(), true),
				new ReplaceableAttribute("SecondName", contact.getLastName(), true),
				new ReplaceableAttribute("MiddleInitial", contact.getMiddleInitial(), true),
				new ReplaceableAttribute("AddressLine1", contact.getAddressLine1(), true),
				new ReplaceableAttribute("AddressLine2", contact.getAddressLine2(), true),
				new ReplaceableAttribute("City", contact.getCity(), true),
				new ReplaceableAttribute("State", contact.getState(), true),
				new ReplaceableAttribute("Zip", contact.getZip(), true),
				new ReplaceableAttribute("PhoneNumber", contact.getPhoneNo(), true)));
		
		return contactList;
	}

}
