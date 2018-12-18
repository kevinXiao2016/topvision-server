/***********************************************************************
 * $Id: FtpClientException.java,v1.0 2013-1-22 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.io.File;
import java.util.List;

import com.topvision.framework.service.Service;
import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.domain.FtpFile;
import com.topvision.platform.exception.FtpConnectException;

/**
 * FTP连接管理业务接口
 * @author fanzidong
 * @created @2013-1-22-上午10:50:03
 */
public interface FtpConnectService extends Service {

    /**
     * 获取FTP连接的基本参数
     * @return FTP连接的基本参数(包括IP、port、userName、pwd、remotePath,调用时要进行非空判断)
     */
    public FtpConnectInfo getFtpConnectAttr();

    /**
     * 获取FTP连接的状态(可达状态、读取权限、写入权限)
     * @param ftpConnect
     * @return FTP连接的状态(可通过三个isXXX方法来分别获取)
     */
    public FtpConnectInfo getFtpConnectStatus();

    /**
     * 更新FTP连接的基本参数
     * @param ftpConnect
     */
    public void modiftyFtpConnectAttr(FtpConnectInfo ftpConnect);

    /**
     * 连接FTP服务器并返回当前工作目录
     * @param ftpConnect
     * @return 当前工作目录
     */
    public String getWorkingDirectory(String folderName) throws FtpConnectException;

    /**
     * 获取FTP服务器上指定文件夹下的所有文件
     * @param ftpConnect
     * @return 文件集合(调用时要进行非空判断)
     */
    public List<FtpFile> getFileList(String folderName);

    /**
     * 向FTP服务器的指定文件夹下上传文件
     * @param file 待上传的文件(服务器端的文件)
     * @param remoteFilePathName 保存在FTP服务器端的文件路径名称
     * @return 上传是否成功
     */
    public boolean uploadFile(File file, String remoteFilePathName);

    /**
     * 下载FTP服务器上的指定文件
     * @param fileFullName ftp服务器上文件的全名(包括路径)
     * @param serverFileFullName 下载文件存放在NM3000服务器上的文件名称
     * @return 下载成功与否
     */
    public File downloadFile(String ftpFileName, String serverFileName);

    /**
     * 删除指定的文件
     * @param fileFullName ftp服务器上文件的全名(包括路径)
     * @param fileType 删除文件的类型：0为文件，1为文件夹
     * @return 删除是否成功
     */
    public boolean deleteFile(String fileFullName, int fileType);

    /**
     * 在FTP服务器的指定文件夹下创建子文件夹
     * @param folerFullName 文件夹全名(包括路径)
     * @return 是否创建成功
     */
    public boolean createFolder(String folerFullName);
    
    /**
     * 获取服务器上所有的IP地址
     * @return
     */
    public List<String> getAllIpAddress();
    
    /**
     * 获取FTP服务器上是否存在该文件
     * @return
     */
    public boolean isFileExist(String filePathName);
}
