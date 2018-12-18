/***********************************************************************
 * $Id: FtpClientUtil.java,v1.0 2013-1-22 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FTP客户端工具类
 * 
 * @author fanzidong
 * @created @2013-1-22-上午10:50:03
 * 
 */
public class FtpClientUtil {
	public FTPClient fTPClient = new FTPClient();
	private final Logger logger = LoggerFactory.getLogger(FtpClientUtil.class);

	public static final int TIMEOUT = 7000;
	public static final String UTF8 = "utf-8";
	public static final String ISO = "iso-8859-1";
	public static final String GBK = "GBK";
    public static final int FILE_TYPE = 0;
    public static final int DIRECTORY_TYPE = 0;

	    /**
     * 连接FTP服务器
     * 
     * @param ip
     * @param port
     * @param userName
     * @param password
     * @return 是否可以连通
     */
	public boolean connect(String ip, int port, String userName, String password) {
		boolean connected = false;
		fTPClient.setConnectTimeout(TIMEOUT);
		try {
			fTPClient.connect(ip, port);
            // 如果能够连接，便尝试登录FTP服务器
			if (FTPReply.isPositiveCompletion(fTPClient.getReplyCode())) {
				connected = fTPClient.login(userName, password);
			}
		} catch (SocketException e) {
			logger.info("cannot bind this port:" + port);
		} catch (IOException e) {
			logger.info("I/O exception happened when try to connect the ftpserver");
		} finally {
			fTPClient.setConnectTimeout(0);
		}
		return connected;
	}

	    /**
     * 断开与FTP服务器的连接
     * 
     */
	public void disconnect() {
		if (fTPClient.isConnected()) {
			try {
				fTPClient.disconnect();
			} catch (IOException e) {
				logger.info("I/O exception happened when try to disconnect the ftpserver");
			}
		}
	}

	    /**
     * 在指定的文件目录创建子文件夹
     * 
     * @param remotePathName
     *            FTP服务器端路径名
     * @param newFolderName
     *            新建文件夹的名称
     * @return 是否创建成功
     */
	public boolean createSubDirectory(String remotePathName,
			String newFolderName) {
		boolean result = false;
        // 跳转到指定目录，在本应用情景中不存在目录不存在的情况，故不予考虑
		try {
			fTPClient.changeWorkingDirectory(new String(remotePathName
					.getBytes(UTF8), ISO));
			result = fTPClient.makeDirectory(new String(newFolderName
					.getBytes(UTF8), ISO));
		} catch (UnsupportedEncodingException e) {
			logger.info("the encodingType is not supported");
		} catch (IOException e) {
			logger.info("I/O exception happened when changing directory");
		}
		return result;
	}

	    /**
     * 将网管服务器上的文件上传到FTP服务器
     * 
     * @param file
     *            待上传的文件
     * @param remoteFilePathName
     *            保存在FTP服务器上的文件路径名
     * @return 是否上传成功
     */
	public boolean uploadFile(File file, String remoteFilePathName) {
		boolean result = false;
		FileInputStream fis = null;

        // 设置PassiveMode传输
		fTPClient.enterLocalPassiveMode();

		try {
            // 设置以二进制流的方式传输
			fTPClient.setFileType(FTP.BINARY_FILE_TYPE);
			fTPClient.setControlEncoding("GBK");
		} catch (IOException e) {
			return false;
		}

		if (!file.exists()) {
			logger.debug("the file is not exists");
			return false;
		}
		try {
			fis = new FileInputStream(file);
			result = fTPClient.storeFile(
					new String(remoteFilePathName.getBytes(UTF8), ISO), fis);
		} catch (UnsupportedEncodingException e) {
			logger.info("the encodingType is not supported");
		} catch (IOException e) {
			logger.info("I/O exception happened when uploading file");
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.info("an I/O exception has occurred when closing file stream");
				}
			}
		}
		return result;
	}

	    /**
     * 将FTP服务器上的文件下载到网管服务器
     * 
     * @param remoteFilePathName
     *            FTP服务器端文件路径名
     * @param serverFilePathName
     *            网管服务器端文件路径名(需要绝对路径名，下载的文件内容将放在此文件中)
     * @throws IOException
     */
	public boolean downloadFile(String remoteFilePathName,
			String serverFilePathName){
		boolean result = false;
		FileOutputStream out = null;
		File file = null;

        // 设置被动模式
		fTPClient.enterLocalPassiveMode();

		try {
            // 设置以二进制方式传输
			fTPClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (IOException e) {
			logger.info("binary file type is denied");
			return false;
		}

        // 开始下载文件
		try {
			file = new File(serverFilePathName);
			out = new FileOutputStream(file);
		    result = fTPClient.retrieveFile(new String(
					remoteFilePathName.getBytes(UTF8), ISO), out);
		    return result;
		} catch (IOException e) {
			logger.info("an I/O exception has occurred when download a file");
			return false;
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.info("an I/O exception has occurred when closing file stream");
			}
		}
	}

	    /**
     * 删除FTP服务器端的文件
     * 
     * @param remoteFilePathName
     *            FTP服务器端文件路径名
     * @param fileType
     *            待删除文件类型：0为文件、1为文件夹
     * @return 是否删除成功
     */
	public boolean deleteFile(String remoteFilePathName, int fileType) {
		boolean result = false;
		try {
			if (fileType == 0) {
				result = fTPClient.deleteFile(new String(remoteFilePathName
						.getBytes(UTF8), ISO));
			} else if (fileType == 1) {
				result = fTPClient.removeDirectory(new String(
						remoteFilePathName.getBytes(UTF8), ISO));
			}
		} catch (IOException e) {
			logger.info("failed to delete a file");
		}
		return result;
	}

}
