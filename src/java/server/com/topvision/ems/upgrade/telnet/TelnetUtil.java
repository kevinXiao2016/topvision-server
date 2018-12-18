/***********************************************************************
 * $Id: TelnetUtil.java,v1.0 2014年9月23日 下午4:07:30 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.telnet;

import java.io.IOException;

import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.domain.TftpClientInfo;

/**
 * @author loyal
 * @created @2014年9月23日-下午4:07:30
 * 
 */
public interface TelnetUtil {
    /**
     * 连接
     * 
     * @param ip
     * @throws IOException
     */
    void connect(String ip) throws IOException;

    /**
     * 执行命令,返回命令行结果
     * 
     * @param cmd
     * @return
     */
    String execCmd(String cmd);

    /**
     * 执行命令，返回命令执行成功/失败
     * 
     * @param cmd
     * @return
     */
    Boolean execCmd1(String cmd);

    /**
     * 获取超时时间
     * 
     * @return
     */
    Long getTimeout();

    /**
     * 设置超时时间
     * 
     * @param timeout
     */
    void setTimeout(Long timeout);

    /**
     * 断开连接
     */
    void disconnect();

    /**
     * 登录
     * 
     * @param userName
     * @param password
     * @return
     */
    boolean login(String userName, String password, String enablePassword,Boolean isAAA);

    /**
     * 进入enable视图
     * 
     * @param password
     * @return
     */
    boolean enterEnable(String password);

    /**
     * 保存配置
     * 
     * @return
     */
    boolean writeConfig();

    /**
     * 重启
     * 
     * @return
     */
    void reset();

    /**
     * 进入config视图
     * 
     * @return
     */
    boolean enterConfig();

    /**
     * quit命令
     */
    void quit();

    /**
     * end命令
     */
    void end();

    /**
     * 获取IP
     * 
     * @return
     */
    String getIp();

    /**
     * 获取命令行执行结果
     * 
     * @return
     */
    String getRe();

    /**
     * 设备通过ftp上传文件
     * 
     * @param ip
     * @return 
     * @throws Exception 
     */
    boolean uploadConfig(FtpConnectInfo connectInfo, String fileName) throws Exception;

    /**
     * 设备通过FTP下载文件
     * 
     * @param connectInfo
     * @param fileName
     * @param newFileName 
     * @throws Exception
     */
    void downLoadConfig(FtpConnectInfo connectInfo, String fileName) throws Exception;

    void setRe(String re);
    
    void downLoadConfig(FtpConnectInfo connectInfo, String fileName, String newFileName) throws Exception;

    /**
     * 设备通过TFTP下载文件
     * 
     * @param tftpClientInfo
     * @param fileName
     * @throws Exception
     */
    void downLoadConfig(TftpClientInfo tftpClientInfo, String fileName, String newFileName) throws Exception;

    /**
     * 设备通过TFTP下载文件
     * 
     * @param tftpClientInfo
     * @param fileName
     * @throws Exception
     */
    void downLoadConfig(TftpClientInfo tftpClientInfo, String fileName) throws Exception;
}
