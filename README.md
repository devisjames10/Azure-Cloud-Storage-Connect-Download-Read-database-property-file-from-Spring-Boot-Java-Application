# Azure-Cloud-Storage-Connect-Upload-Download-Read-database-jsonfile-from-Spring-Boot-Java-Application
How to use the Spring Boot Starter for Azure Storage, using the client library for Java.

This application has been built using azure-storage-spring-boot-starter maven dependency to connect to the Azure storage blob data. It can read the blog storage json data in azure container, can download and use those data to create datasource, so that application can connect to database to perform some database operations. In order to do the same, we have set up run configuration property to auto feed application startup(Azure details like key, storage account name, protocol, container name, blob name etc.), please find the attached image for the same. Also find the json property file attached with this respiratory that will contain database connection properties.

