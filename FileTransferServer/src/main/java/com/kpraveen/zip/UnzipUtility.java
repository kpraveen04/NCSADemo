/**Author : Kpraveen
 * Date : 10/06/2018
 * Description :
 * This is the utility to unzip the file after transferring to local machine
 */
package com.kpraveen.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipUtility {
    public static void main(String[] args) throws IOException {
        
    }
    
    
    public boolean unzipLocalFileAndDeleteZip(String zipFilePath,String localPathForReceiving) throws Exception{
    	boolean isSuccessful= false;
    	try {
    	String fileZip = zipFilePath;//"C:/Spring/TestFileReciever/TestData_20181006.zip";
        byte[] buffer = new byte[4096];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while(zipEntry != null){
            String fileName = zipEntry.getName();
            //File newFile = new File("C:/Spring/TestFileReciever/" + fileName);
            File newFile = new File(localPathForReceiving + fileName);
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            zipEntry = zis.getNextEntry();
        }
        isSuccessful=true;
        zis.closeEntry();
        zis.close();
        
		//return isSuccessful;
    	} catch(Exception e) {
    		System.err.println("Error while unzipping the file");
    		System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			throw e;
    	}
		return isSuccessful;
    }
}