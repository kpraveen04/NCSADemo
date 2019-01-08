package com.kpraveen.FileTransferServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.kpraveen.Exception.ExceptionMessages;
import com.kpraveen.consts.Constants;
import com.kpraveen.utility.PropertiesLoader;
import com.kpraveen.utility.SFTPFile;
import com.kpraveen.zip.UnzipUtility;
import com.kpraveen.zip.ZipUtility;

/**
 * Hello world!
 *
 */
public class App implements Constants, ExceptionMessages {
	public static void main(String[] args) throws Exception {

		// load the properties
		try {
			Properties prop = PropertiesLoader.loadProperties();
			SFTPFile sftpFile = new SFTPFile();
			String txtFileName = sftpFile.getFileName(FILENAME_PREFIX, TEXT_FILENAME_EXTENTION);
			String zipFileName = sftpFile.getFileName(FILENAME_PREFIX, ZIP_FILENAME_EXTENTION);
			
			
			String action =prop.getProperty(ACTION).trim();
			
			
			String sftpHost =prop.getProperty(SFTPHOST).trim();
			String sftpPort =prop.getProperty(SFTPPORT).trim();
			String sftpUser =prop.getProperty(SFTPUSER).trim();
			String sftpPass =prop.getProperty(SFTPPASS).trim();
			String sftpWorkingDir =prop.getProperty(SFTPWORKINGDIR).trim();
			if(!sftpWorkingDir.endsWith("/")) {
				sftpWorkingDir+="/";
			} 
			

			if (SEND.equalsIgnoreCase(action)) {
				
				
				String localFilePathSender =prop.getProperty(LOCAL_FILE_PATH_SENDER).trim();
				if(!localFilePathSender.endsWith("/")) {
					localFilePathSender+="/";
				} 

				System.out.println("You have set ACTION as SEND. Starting the upload process!");
				System.out.println("");
				System.out.println("You have set local path from where file needs to be picked as : "+localFilePathSender);
				System.out.println("");
				System.out.println("You have set Remote server for the file to be sent as :"+sftpHost);
				System.out.println("");
				System.out.println("You have set Remote server path for the file to be sent as :"+sftpWorkingDir);
				
				// Check if the file exists

				boolean exists = new File(localFilePathSender + txtFileName).exists();
				boolean zipSuccess = false;
				boolean sendSuccess = false;
				if (exists) {

					try {
						// create a temp directory
						new File(localFilePathSender + TEMP).mkdir();
					} catch (Exception e) {
						System.err.println("**************************************************");
						System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
						System.err.println("**************************************************");
						System.err.println("Error while creating temp Directory.Contact Admin");
						e.printStackTrace();
						System.exit(-1); // Exit the program because of error
					}

					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					Date date = new Date();

					try {
						// Zip the file in a temp location

						System.out.println("Start zipping :: " + dateFormat.format(date));
						zipSuccess = ZipUtility.zipFile(
								new File(localFilePathSender + txtFileName),
								localFilePathSender + TEMP + zipFileName);

						date = new Date();

					} catch (Exception e) {
						System.err.println("**************************************************");
						System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
						System.err.println("**************************************************");
						System.err.println("There was an error zipping the file. Please check the input file and retry again!");
						System.exit(-1);// Terminate process because of error

					}
					System.out.println("zipping complete :: " + dateFormat.format(date));
					// send the file
					System.out.println("Text File: " + txtFileName);
					System.out.println("compressed File: " + zipFileName);

					date = new Date();

					System.out.println("sending the zip over network start:: " + dateFormat.format(date));

					if (zipSuccess) {
						try {
							sendSuccess = sftpFile.send(zipFileName, sftpHost, sftpPort,
									sftpUser, sftpPass,
									sftpWorkingDir, localFilePathSender + TEMP);

							date = new Date();

							System.out.println("sending the zip over network complete :: " + dateFormat.format(date));

						} catch (Exception e) {
							System.err.println("**************************************************");
							System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
							System.err.println("**************************************************");
							System.err.println("Error while sending the file to FTP Server, please contact Admin");
							e.printStackTrace();
						}

						/*if (sendSuccess) {
							try {
								// delete the temp folder
								File file = new File(localFilePathSender + TEMP+zipFileName);
								//delete(file);
								if (file.delete()) {
									System.out.println(" temp zip File deleted successfully");
								}

								else {
									System.out.println("The temp zip File couldn't be deleted!");
								}
							} catch (Exception e) {
								System.err.println("**************************************************");
								System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
								System.err.println("**************************************************");
								System.err.println("Error while deleting temp folder. Please contact Admin");
								e.printStackTrace();
							}
						}*/
					}
				} else {
					System.err.println("**************************************************");
					System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
					System.err.println("**************************************************");
					System.err.format(LOCAL_FILE_PATH_SENDER_NOT_PRESENT_ERROR, txtFileName,
							localFilePathSender);
					System.exit(-1); // Exit the program because of error
				}
			}

			// read and recieve the file
			else if (RECEIVE.equalsIgnoreCase(action)) {
				
				String localFilePathReciever =prop.getProperty(LOCAL_FILE_PATH_RECEIVER).trim();
				if(!localFilePathReciever.endsWith("/")) {
					localFilePathReciever+="/";
				} 
				
				System.out.println("You have set ACTION as RECEIVE. Starting the downloading process!");
				System.out.println("");
				System.out.println("You have set local path where you want file to be downloaded as : "+localFilePathReciever);
				System.out.println("");
				System.out.println("You have set Remote server for the file to be received from as :"+sftpHost);
				System.out.println("");
				System.out.println("You have set Remote server path for the file from where file needs to be copied as :"+sftpWorkingDir);

				boolean isReceivingSuccess= false;
				boolean isUnzipSuccess=false;
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				System.out.println("receiving the zipped file over network start:: " + dateFormat.format(date));
				isReceivingSuccess = sftpFile.recieve(zipFileName, sftpHost, sftpPort,
						sftpUser, sftpPass, sftpWorkingDir,
						localFilePathReciever);
				date = new Date();

				System.out.println("receiving the zipped file over network ends:: " + dateFormat.format(date));
				
				if(isReceivingSuccess) {
					UnzipUtility unzipUtility = new UnzipUtility();
					isUnzipSuccess =unzipUtility.unzipLocalFileAndDeleteZip(localFilePathReciever+zipFileName, localFilePathReciever);
				}
				
				if(isUnzipSuccess) {
					delete(new File(localFilePathReciever+zipFileName));
				}
			} else {
				System.err.println("Not a valid ACTION provided!! Please check applicaton.properties file and set ACTION as SEND or RECEIVE");
			}

		} catch (FileNotFoundException ex) {
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println("Properties file doesn't exist");
		} catch (IOException e) {
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println("  Error while receiving file over network" );
			System.exit(-1);// Exit the program because of error
			
		} catch (Exception e) {
			//System.err.println("**************************************************");
			//System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			//System.err.println("**************************************************");
			//System.err.println("Error while receiving file over network" );
			System.err.println("This is embarassing!!!An unhandled exception is thrown, please contact Admin!"+e.getMessage());
			System.exit(-1);// Exit the program because of error
			
		}

	}
	
	//To delete the temp directory
	public static void delete(File file)
	    	throws IOException{
	 
	    	if(file.isDirectory()){
	 
	    		//directory is empty, then delete it
	    		if(file.list().length==0){
	    			
	    		   file.delete();
	    		   System.out.println("Directory is deleted : " 
	                                                 + file.getAbsolutePath());
	    			
	    		}else{
	    			
	    		   //list all the directory contents
	        	   String files[] = file.list();
	     
	        	   for (String temp : files) {
	        	      //construct the file structure
	        	      File fileDelete = new File(file, temp);
	        		 
	        	      //recursive delete
	        	     delete(fileDelete);
	        	   }
	        		
	        	   //check the directory again, if empty then delete it
	        	   if(file.list().length==0){
	           	     file.delete();
	        	     System.out.println("Directory is deleted : " 
	                                                  + file.getAbsolutePath());
	        	   }
	    		}
	    		
	    	}else{
	    		//if file, then delete it
	    		file.delete();
	    		System.out.println("File is deleted : " + file.getAbsolutePath());
	    	}
	    }
}
