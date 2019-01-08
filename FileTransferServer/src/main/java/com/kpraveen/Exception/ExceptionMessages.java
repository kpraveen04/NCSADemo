package com.kpraveen.Exception;

public interface ExceptionMessages {

	public String FILENOTFOUND ="The application.properties file is not found at the specified location";
	public String FILEISEMPTY ="Looks the application.properties file doesn't contain any data or there was an error reading the file.";
	public String APPL_PROP_NOT_FOUND="The application.properties file is not present, please provide it";
	public String LOCAL_FILE_PATH_SENDER_NOT_PRESENT_ERROR="Either the file to be sent %s is not present or the path %s for the test data file doesn't exist. Please fix and retry!";
	
	public String SFTPHOST_ERROR="Please provide the SFTP host name under property SFTPHOST";
	public String SFTPPORT_ERROR="Please provide the SFTP port number under property SFTPPORT. Default is 22.";
	public String SFTPUSER_ERROR="Please provide the SFTP user name under property SFTPUSER";
	public String SFTPPASS_ERROR="Please provide the SFTP password under property SFTPPASS";
	public String SFTPWORKINGDIR_ERROR="Please provide the SFTP host directory under property SFTPWORKINGDIR";
	public String LOCAL_FILE_PATH_SENDER_ERROR="Please provide the path of folder from where file needs to be copied under property Local.file.path.sender for sending file";
	public String LOCAL_FILE_PATH_RECEIVER_ERROR="Please provide the  path of folder where file needs to be copied from FTP Server under property Local.file.path.receiver";
	public String FILE_EXTN_ERROR="Please provide the file name extension under property filename.extension. The default is .txt. . If this needs to be changed, please contact Admin";
	public String FILE_PREFIX_ERROR= "Please provide the filename prefix under property filename.prefix. The default is TestData_. If this needs to be changed, please contact Admin";
	
	public String ACTION_MISSING_ERROR= "Please provide the value of ACTION in properties file. This tells whether you want to send or receive file. For more details read runbook!";
}
