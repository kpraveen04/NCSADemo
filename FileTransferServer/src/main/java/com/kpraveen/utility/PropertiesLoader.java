/**Author : Kpraveen
 * Date : 10/06/2018
 * Description :
 * This is the utility to load the properties file from an external file. It additionally validates all the mandatory fields in that file.
 */
package com.kpraveen.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.kpraveen.Exception.ExceptionMessages;
import com.kpraveen.consts.Constants;

public class PropertiesLoader implements ExceptionMessages,Constants{
	
	public static Properties loadProperties() throws FileNotFoundException{
	
	Properties prop = new Properties();
	InputStream input = null;
	
	try {

			String filename = "application.properties";
			//String path = "./application.properties";
			input = PropertiesLoader.class.getClassLoader().getResourceAsStream(filename);
			
			//load the file handle for main.properties
			//FileInputStream  file = new FileInputStream(path);
		
			//FileReader reader = new FileReader(new File(new File("."),  filename)); // For external properties file
			/*if(file ==null){
	            System.out.println("Sorry, unable to find " + filename);
		    throw new FileNotFoundException();
		    
		} 
		if(file==null){
	            System.err.println(FILEISEMPTY);
		    throw new FileNotFoundException();
		   
		}*/

		// load a properties file
		prop.load(input);

		// get the property value and print it out
		/*System.out.println(prop.getProperty("SFTPHOST"));
		System.out.println(prop.getProperty("SFTPPORT"));
		System.out.println(prop.getProperty("SFTPUSER"));
		System.out.println(prop.getProperty("SFTPPASS"));
		System.out.println(prop.getProperty("SFTPWORKINGDIR"));
		System.out.println(prop.getProperty("Local.file.path.sender"));
		System.out.println(prop.getProperty("Local.file.path.reciever"));
		*/
		
		

	}
	catch(FileNotFoundException fnf) {
		System.err.println("**************************************************");
		System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
		System.err.println("**************************************************");
		System.err.println(APPL_PROP_NOT_FOUND);
		System.exit(-1);
	}
	catch (IOException io) {
		System.err.println("**************************************************");
		System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
		System.err.println("**************************************************");
		System.err.println("There is an error reading the properties file");
		io.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		PropertiesLoader propertiesLoader = new PropertiesLoader();
		propertiesLoader.mandatoryCheck(prop);

	}
	return prop;
	}
	
	public void mandatoryCheck(Properties prop)
	{
		if(prop.getProperty(SFTPHOST)==null)
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(SFTPHOST_ERROR);
			System.exit(-1);// exit the program
		}
		
		if(prop.getProperty(SFTPPORT)==null)
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(SFTPPORT_ERROR);
			System.exit(-1);// exit the program
		}
		
		if(prop.getProperty(SFTPUSER)==null)
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(SFTPUSER_ERROR);
			System.exit(-1);// exit the program
		}
		
		if(prop.getProperty(SFTPPASS)==null)
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(SFTPPASS_ERROR);
			System.exit(-1);// exit the program
		}
		
		if(prop.getProperty(SFTPWORKINGDIR)==null)
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(SFTPWORKINGDIR_ERROR);
			System.exit(-1);// exit the program
		}
		
		if(prop.getProperty(ACTION)==null)
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(ACTION_MISSING_ERROR);
			System.exit(-1);// exit the program
		}
		
		if(prop.getProperty(FILE_NAME_EXTN)==null )
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(FILE_EXTN_ERROR);
			System.exit(-1);// exit the program
		}
		
		if(prop.getProperty(FILENAME_PREFIX_KEY)==null)
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(FILE_PREFIX_ERROR);
			System.exit(-1);// exit the program
		}
		
		if(prop.getProperty(LOCAL_FILE_PATH_SENDER)==null && prop.getProperty(ACTION).trim().equalsIgnoreCase(SEND))
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(LOCAL_FILE_PATH_SENDER_ERROR);
			System.exit(-1);// exit the program
		}
		
		if(prop.getProperty(LOCAL_FILE_PATH_RECEIVER)==null && prop.getProperty(ACTION).trim().equalsIgnoreCase(RECEIVE))
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(LOCAL_FILE_PATH_RECEIVER_ERROR);
			System.exit(-1);// exit the program
		}
		
		if(prop.getProperty(FILE_NAME_EXTN)!=null && !(prop.getProperty(FILE_NAME_EXTN).trim().equals(TEXT_FILENAME_EXTENTION)))
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(FILE_EXTN_ERROR);
			System.exit(-1);// exit the program
		}
		
		if(prop.getProperty(FILENAME_PREFIX_KEY)!=null  && !(prop.getProperty(FILENAME_PREFIX_KEY).trim().equals(FILENAME_PREFIX)))
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(FILE_PREFIX_ERROR);
			System.exit(-1);// exit the program
		}
		
		if((!(prop.getProperty(ACTION).trim().equals(SEND)) && !(prop.getProperty(ACTION).trim().equals(RECEIVE))))
		{
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(ACTION_MISSING_ERROR);
			System.exit(-1);// exit the program
		}
		
	}

}
