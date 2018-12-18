/***********************************************************************
 * $Id: FtpClientException.java,v1.0 2013-1-22 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.util.List;

import org.apache.ftpserver.usermanager.impl.BaseUser;

import com.topvision.framework.service.Service;
import com.topvision.platform.domain.FtpServerInfo;

/**
 * 内置FTP服务器管理业务接口
 * 
 * @author fanzidong
 * @created @2013-1-22-上午10:50:03
 */
public interface FtpServerService extends Service {

    /**
     * 获取内置FTP服务器参数
     * 
     * @return FTP服务器参数(调用时要进行非空判断)
     * @throws Exception
     */
    public FtpServerInfo getFtpServerAttr();

    /**
     * 获取内置FTP服务器的状态(启动状态、可达状态)
     * 
     * @param ftpServer
     * @return FTP服务器的状态
     */
    public FtpServerInfo getFtpServerStatus();

    /**
     * 更新内置FTP服务器参数
     * 
     * @param ftpServer
     */
    public void modifyFtpServerAttr(FtpServerInfo ftpServer);

    /**
     * 启动内置FTP服务器
     * 
     * @param ftpServer
     * @return 启动是否成功
     */
    public boolean startFtpServer();

    /**
     * 停止内置FTP服务器
     * 
     * @param ftpServer
     */
    public void stopFtpServer();

    /**
     * 重启内置FTP服务器
     * 
     * @param ftpServer
     * @return 重启是否成功
     */
    public boolean reStartFtpServer();

    /**
     * 获取FTP服务器的所有文件夹路径
     * 
     * @return
     */
    public List<String> getFtpServerDirectories();

    /**
     * 获取服务器上所有的IP地址
     * 
     * @return
     */
    public List<String> getAllIpAddress();

    public BaseUser getAdminUser();
}
