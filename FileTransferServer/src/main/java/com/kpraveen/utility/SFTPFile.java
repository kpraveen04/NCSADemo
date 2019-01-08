/**Author : Kpraveen
 * Date : 10/06/2018
 * Description :
	 * This class contains both the send and receive methods.
	 * The send method would be used to transfer the file to remote server location using sftp
	 * The receive method would be used to fetch the file from the remote server to local machine using sftp
	 * It also contains the method call to read the properties file to get the details of local and remote server and the credentials for sftp
	 * It also contains the call to utility for compressing and decompressing the file
 */

package com.kpraveen.utility;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.kpraveen.consts.Constants;

import java.io.*;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

public class SFTPFile implements Constants {
	//for testing the uploading and downloading utility
	public static void main(String args[]) {

		// load the properties
		try {
			Properties prop = PropertiesLoader.loadProperties();
			SFTPFile sftpFile = new SFTPFile();
			String fileName = sftpFile.getFileName(prop.getProperty(FILENAME_PREFIX), TEXT_FILENAME_EXTENTION);

			// create and send the file

			System.out.println("fileName: " + fileName);

			// if(SEND.equalsIgnoreCase(args[0]))
			try {
				sftpFile.send(fileName, prop.getProperty(SFTPHOST), prop.getProperty(SFTPPORT),
						prop.getProperty(SFTPUSER), prop.getProperty(SFTPPASS), prop.getProperty(SFTPWORKINGDIR),
						prop.getProperty(LOCAL_FILE_PATH_SENDER));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// read and recieve the file
			/*
			 * if(RECEIVE.equalsIgnoreCase(args[0])) { sftpFile.recieve
			 * (fileName,prop.getProperty(SFTPHOST), prop.getProperty(SFTPPORT),
			 * prop.getProperty(SFTPUSER),prop.getProperty(SFTPPASS),prop.getProperty(
			 * SFTPWORKINGDIR) ,prop.getProperty(LOCAL_FILE_PATH_RECEIVER)); }
			 */

		} catch (FileNotFoundException ex) {
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println("Properties file doesn't exist");
		}

	}

	public boolean send(String fileName, String SFTPHOST, String SFTPPORT, String SFTPUSER, String SFTPPASS,
			String SFTPWORKINGDIR, String Local_file_path_sender) throws Exception {

		boolean isSuccessful = false;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		System.out.println("preparing the host information for sftp.");
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER, SFTPHOST, Integer.parseInt(SFTPPORT));
			session.setPassword(SFTPPASS);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			System.out.println("Host connected.");
			channel = session.openChannel("sftp");
			channel.connect();
			System.out.println("sftp channel opened and connected.");
			channelSftp = (ChannelSftp) channel;
			channelSftp.cd(SFTPWORKINGDIR);
			File f = new File(Local_file_path_sender + fileName);
			channelSftp.put(new FileInputStream(f), f.getName());
			// log.info("File transfered successfully to host.");
			
			System.out.println("File transfered successfully to host.");
			isSuccessful = true;
		}

		catch (Exception ex) {
			
			System.err.println("Exception found while transferring the file over network.");
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			System.err.println(ex.getMessage());
			if (ex.getMessage().contains("java.net.UnknownHostException")) {
				System.err.format("Host %s doesn't exist or cannot connect! Please check with Admin!", SFTPHOST);
			}
			else if (ex.getMessage().contains("Connection refused: connect")) {
				System.err.format("Check SFTP details, SFTP host: %s, SFTP Port: %s , unable to connect !", SFTPHOST,
						SFTPPORT);
			}
			else if (ex.getMessage().contains("Auth fail")) {
				System.err.format(
						"Check SFTP server credentials, SFTP User: %s, SFTP Password: %s , Credentials seems incorrect!",
						SFTPUSER, SFTPPASS);
			} else {
				System.err.format("Please check with the Admin for the error : %s!"+ex.getMessage());
			}

			throw ex;

		} finally {
			try {
				channelSftp.exit();
				System.out.println("sftp Channel exited.");
				channel.disconnect();
				System.out.println("Channel disconnected.");
				session.disconnect();
				System.out.println("Host Session disconnected.");
			} catch (Exception e) {
				// do nothing
				System.exit(-1);
				// throw e;
			}

		}
		return isSuccessful;
	}

	public boolean recieve(String fileName, String SFTPHOST, String SFTPPORT, String SFTPUSER, String SFTPPASS,
			String SFTPWORKINGDIR, String Local_file_path_receiver) throws Exception {

		boolean isSuccessful = false;
		Session session = null;
		boolean noFileToBePulled= true;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(SFTPUSER, SFTPHOST, Integer.parseInt(SFTPPORT));
			session.setPassword(SFTPPASS);
			session.setConfig("StrictHostKeyChecking", "no");
			System.out.println("Establishing Connection...");
			session.connect();
			System.out.println("Connection established.");
			System.out.println("Creating SFTP Channel.");
			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
			System.out.println("SFTP Channel created.");

			/*
			 * if (getFilesInWorkingDirectory(sftpChannel,SFTPWORKINGDIR,fileName)) {
			 * OutputStream output = new FileOutputStream(
			 * Local_file_path_receiver+fileName); System.out.println("Starting download");
			 * InputStream is = sftpChannel.get(SFTPWORKINGDIR+fileName,
			 * Local_file_path_receiver+fileName); System.out.println("download complete");
			 * }
			 */

			List<ChannelSftp.LsEntry> list = getFiles(sftpChannel, SFTPWORKINGDIR, Local_file_path_receiver);
			try {
				System.out.println("SFTPUtil : ls command output is :" + list);
				for (ChannelSftp.LsEntry file : list) {
					if (!file.getAttrs().isDir() && file.getFilename().equals(fileName)) {
						System.out.println("SFTPUtil : downloading file to local working dir, filename is : ["
								+ file.getFilename() + "]");
						sftpChannel.get(file.getFilename(), file.getFilename());
						noFileToBePulled=false;
					} 
				}
				if(noFileToBePulled) {
					
						System.err.format("The file %s is not present at the FTP server location %s. PLease check with Admin!",fileName,SFTPWORKINGDIR);
						throw new Exception("The file is not present at the FTP server location. PLease check with Admin!");
					
				}

				sftpChannel.rm(SFTPWORKINGDIR + fileName);
				isSuccessful = true;

			} finally {
				if (sftpChannel != null)
					sftpChannel.disconnect();
			}

			sftpChannel.exit();

		} catch (Exception ex) {

			System.err.println("Exception found while trying to receive the file over network."+ex.getCause());

			//System.err.println(ex.getMessage());
			System.err.println("**************************************************");
			System.err.println("PROCESS FAILED!!! READ BELOW FOR MORE DETAILS");
			System.err.println("**************************************************");
			
			if (ex.getMessage().contains("java.net.UnknownHostException")) {
				System.err.format("Host %s doesn't exist or cannot connect! Please check with Admin!", SFTPHOST);
			}
			if (ex.getMessage().contains("The file is not present at the FTP server location. PLease check with Admin!")) {
				System.err.format("The file is not present at the FTP server location. PLease check with Admin!");
			}
			else if (ex.getMessage().contains("Connection refused: connect")) {
				System.err.format("Check SFTP details, SFTP host: %s, SFTP Port: %s , unable to connect !", SFTPHOST,
						SFTPPORT);
			}
			else if (ex.getMessage().contains("Auth fail")) {
				System.err.format(
						"Check SFTP server credentials, SFTP User: %s, SFTP Password: %s , Credentials seems incorrect!",
						SFTPUSER, SFTPPASS);
			} 
			else {
				System.err.format("Please check with the Admin for the error : %s!"+ex.getMessage());
			}
			
			throw ex;
		} finally {
			session.disconnect();
		}
		
		return isSuccessful;

	}

	public String getFileName(String fileNamePrefix, String fileNameExtension) {

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		// System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
		return fileNamePrefix + dateFormat.format(date) + fileNameExtension;
	}

	public boolean getFilesInWorkingDirectory(ChannelSftp sftpChannel, String SFTPWORKINGDIR, String fileName) {

		Vector<ChannelSftp.LsEntry> list = null;
		try {
			list = sftpChannel.ls(SFTPWORKINGDIR + fileName);
		} catch (SftpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (ChannelSftp.LsEntry entry : list) {
			return true;
		}
		return false;
	}

	public static List<ChannelSftp.LsEntry> getFiles(ChannelSftp sftpChannel, String srcDir, String destDir)
			throws Exception {

		try {
		sftpChannel.lcd(destDir);
		System.out.println("SFTPUtil : local working dir: " + sftpChannel.lpwd());

		sftpChannel.cd(srcDir);
		System.out.println("SFTPUtil : remote working dir: " + sftpChannel.pwd());

		// Get a listing of the remote directory
		@SuppressWarnings("unchecked")
		List<ChannelSftp.LsEntry> list = sftpChannel.ls(".");
		System.out.println("SFTPUtil : running command 'ls .' on remote server : ");
		return list;
		} catch (Exception e) {
			if(e.getMessage().contains("No such file")) {
				System.err.format("The FTP server doesn't have directory : %s , please check with Admin!",srcDir);
				System.err.println(" ");
			}
			throw e;
			
		}
		
	}

}
