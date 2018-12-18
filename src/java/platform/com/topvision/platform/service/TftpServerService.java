/***********************************************************************
 * $Id: TftpServerService.java,v1.0 2013-1-26 下午3:41:21 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.io.File;
import java.util.List;

import com.topvision.framework.service.Service;
import com.topvision.framework.tftp.TftpEventListener;
import com.topvision.platform.domain.TftpFile;
import com.topvision.platform.domain.TftpInfo;

/**
 * @author Administrator
 * @created @2013-1-26-下午3:41:21
 *
 */
public interface TftpServerService  extends Service  {
    
    /**
     * 获取内置TFTP服务器的基本参数
     * @return
     */
    public TftpInfo getTftpServerAttr();
    
    /**
     * 检测内置TFTP服务器的启动状态
     * @return
     */
    public boolean isTftpServerStarted();
    
    /**
     * 修改内置TFTP服务器的基本参数
     * @param tftpServer
     */
    public void modifyTftpServerAttr(TftpInfo tftpServer);
    
    /**
     * 启动内置TFTP服务器
     * @return
     */
    public boolean startTftpServer();
    
    /**
     * 停止内置TFTP服务器
     * @return
     */
    public void stopTftpServer();
    
    /**
     * 重启内置TFTP服务器
     * @return
     */
    public boolean reStartTftpServer();
    
    /**
     * 判断UDP端口是否被占用
     * @param port
     * @return
     */
    public boolean isUdpPortUsed(int port);
    
    /**
     * 获取TFTP服务器的所指定的根路径下的所有文件夹相对路径
     * @return
     */
    public List<String> getTftpServerDirectories();
    
    /**
     * 获取内置TFTP服务器目录下的所有文件
     * @return
     */
    public List<TftpFile> getFileList();
    
    /**
     * 上传文件
     * @param file 服务器上的文件
     * @param remoteFileName 保存在TFTP服务器上的文件名
     * @return
     */
    public boolean uploadFile(File file, String remoteFileName);
    
    /**
     * 将TFTP服务器上文件下载到网管服务器上
     * @param remoteFileName
     * @param serverFilePathName
     * @return
     */
    public boolean downloadFile(String remoteFileName, String serverFilePathName);
    
    /**
     * 删除内置TFTP服务器目录下的指定文件
     * @param fileName
     * @return
     */
    public boolean deleteFile(String fileName);

    /**
     * 获取本机所有网卡上的所有IP地址
     * @return
     */
    public List<String> getAllIpAddress();

    /**
     * 注册一个tftp状态监听器
     * @param tftpEventListener
     */
    public void addTftpEventListener (TftpEventListener tftpEventListener);

    /**
     * 移除一个tftp状态监听器
     * @param tftpEventListener
     */
    public void removeTftpEventListener (TftpEventListener tftpEventListener);
}
