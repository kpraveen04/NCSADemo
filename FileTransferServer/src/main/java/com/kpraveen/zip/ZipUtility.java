/**Author : Kpraveen
 * Date : 10/06/2018
 * Description :
 * This is the utility to zip the file before SFTPing to the server
 */
package com.kpraveen.zip;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtility {

   /* private static final String INPUT_FILE = "C:/Spring/compression/TestData_20181006.txt";
    private static final String OUTPUT_FILE = "C:/Spring/compression/TestData_20181006.zip";

    public static void main(String[] args) {

        zipFile(new File(INPUT_FILE), OUTPUT_FILE);

    }*/

    public static boolean zipFile(File inputFile, String zipFilePath) throws Exception{
    	
    	boolean isSuccessful = false;
        try {

            // Wrap a FileOutputStream around a ZipOutputStream
            // to store the zip stream to a file. Note that this is
            // not absolutely necessary
            FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

            // a ZipEntry represents a file entry in the zip archive
            // We name the ZipEntry after the original file's name
            ZipEntry zipEntry = new ZipEntry(inputFile.getName());
            zipOutputStream.putNextEntry(zipEntry);

            FileInputStream fileInputStream = new FileInputStream(inputFile);
            byte[] buf = new byte[8192]; //Taking a bigger buffer to improve performance while zipping
            int bytesRead;

            // Read the input file by chunks of 4096 bytes
            // and write the read bytes to the zip stream
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		Date date = new Date();
    		
    		System.out.println("Start writing the buffer:: "+dateFormat.format(date));
            while ((bytesRead = fileInputStream.read(buf)) > 0) {
                zipOutputStream.write(buf, 0, bytesRead);
            }

           
    		date = new Date();
    		
    		System.out.println("stop writing the buffer :: "+dateFormat.format(date));
            // close ZipEntry to store the stream to the file
            zipOutputStream.closeEntry();

            zipOutputStream.close();
            fileOutputStream.close();

            fileInputStream.close();
            System.out.println("Regular file :" + inputFile.getCanonicalPath()+" is zipped to archive :"+zipFilePath);

            isSuccessful=true;
        } catch (IOException e) {
           throw e;
        } catch(Exception ee) {
        	throw ee;
        }
        
        return isSuccessful;

    }
    
   
}