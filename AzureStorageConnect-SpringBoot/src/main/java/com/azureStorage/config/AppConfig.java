package com.azureStorage.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Configuration
@ComponentScan

public class AppConfig {
	
	
	@Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }    

    
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource)
    {
        return new DataSourceTransactionManager(dataSource);
    }
    
    
	
    @Bean
    public DataSource dataSource() 
    {
        
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		File sourceFile = null, downloadedFile = null;
		System.out.println("Azure Blob storage quick start sample");

		CloudStorageAccount storageAccount;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		FileReader fileReader =null;

		try {    
			storageAccount = CloudStorageAccount.parse(getStorageConnectionString());
			blobClient = storageAccount.createCloudBlobClient();
			//container = blobClient.getContainerReference("database-details-container");
			container = blobClient.getContainerReference(getAzureContainerName());
			System.out.println("Connecting to container: " + container.getName());
			
			sourceFile = File.createTempFile("sampleFile", ".json");
			
			//CloudBlockBlob blob = container.getBlockBlobReference("whiskey-dev-db-details.json");
			CloudBlockBlob blob = container.getBlockBlobReference(getAzureBlobName());
			System.out.println("Blob URI :: ++++  "+blob.getUri());
			downloadedFile = new File(sourceFile.getParentFile(), "whiskey-dev-db-details_temp.json");
			blob.downloadToFile(downloadedFile.getAbsolutePath());
			System.out.println("downloadedFile.getAbsolutePath() :: ++++  "+downloadedFile.getAbsolutePath());
			} 
			catch (StorageException ex)
			{
				System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
			}
			catch (Exception ex) 
			{
				System.out.println(ex.getMessage());
			}
		
		
		try {
			JSONParser parser = new JSONParser();
			 fileReader = new FileReader(downloadedFile.getAbsolutePath().toString());
			Object obj = parser.parse(fileReader);
			JSONObject jsonObject = (JSONObject) obj;
            System.out.println("jsonObject =>"+jsonObject);

            String jdbcURL = (String) jsonObject.get("jdbcURL");
            System.out.println(" jdbc URL : "+jdbcURL);
            
            String jdbcUserName = (String) jsonObject.get("jdbcUserName");
            System.out.println(" jdbc User Names : "+jdbcUserName);
            
            String jdbcPwd = (String) jsonObject.get("jdbcPwd");
            System.out.println(" jdbc password : "+jdbcPwd);
            
            String driverName = (String) jsonObject.get("driverName");
            System.out.println(" driver name : "+driverName);
                     
   //==============================================================================================
             dataSource.setDriverClassName(driverName);
           // dataSource.setUrl("jdbc:postgresql://" + jdbcURL + "?autoReconnect=true"); 
            dataSource.setUrl(jdbcURL + "?autoReconnect=true");
            dataSource.setUsername(jdbcUserName);
            dataSource.setPassword(jdbcPwd); 
          } 
		
	   	catch(Exception ex) {
		ex.printStackTrace();
			} 
		
		
		
		finally{
			System.out.println("Deleting the source, and downloaded files");
		
			 if (fileReader != null) {
			        try {
			        	fileReader.close();
			        } catch (IOException e) {
			            // This is unrecoverable. Just report it and move on
			            e.printStackTrace();
			        }
			    }
			 

		    if(sourceFile != null)
					sourceFile.delete();
			if(downloadedFile != null)
				downloadedFile.deleteOnExit();	
		}
    
		   System.out.println(" dataSource password  "+dataSource.getPassword());
		   System.out.println(" dataSource username "+dataSource.getUsername());
		   System.out.println(" dataSource url "+dataSource.getUrl());
		  
		   
		  return dataSource;
    }


	private String getAzureContainerName() {
		String CONTAINER_NAME = System.getenv("CONTAINER_NAME");
		return CONTAINER_NAME;
	}


	private String getAzureBlobName() {
		String BLOB_NAME = System.getenv("BLOB_NAME");
		return BLOB_NAME;
	}


	private String getStorageConnectionString() {
		
		String ACCOUNT_NAME = System.getenv("ACCOUNT_NAME");
    	String ACCOUNT_KEY = System.getenv("ACCOUNT_KEY");
    	String ENDPOINTS_PROTOCOL = System.getenv("ENDPOINTS_PROTOCOL");
    	String ENDPOINT_SUFFIX = System.getenv("ENDPOINT_SUFFIX");
    
    	final String storageConnectionString =  "DefaultEndpointsProtocol="+ENDPOINTS_PROTOCOL+";" +
												"AccountName="+ACCOUNT_NAME+";" +
												"AccountKey="+ACCOUNT_KEY+";" +
												"EndpointSuffix="+ENDPOINT_SUFFIX ;
		    	
    	System.out.println(" storageConnectionString ===>> "+storageConnectionString);

		return storageConnectionString;
	}
}
