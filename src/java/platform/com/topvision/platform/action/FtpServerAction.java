/***********************************************************************
 * $Id: FtpServerAction.java,v1.0 2013-1-22 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/

package com.topvision.platform.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.FtpServerInfo;
import com.topvision.platform.service.FtpServerService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author fanzidong
 * @created @2013-1-22-上午10:50:03
 * 
 */
@Controller("ftpServerAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FtpServerAction extends BaseAction {

    private static final long serialVersionUID = -3743775149232961865L;
    @Autowired
    private FtpServerService ftpServerService;

    private JSONObject ftpServer;

    private String ip;
    private int port;
    private String userName;
    private String pwd;
    private String rootPath;
    private boolean writeable;

    /**
     * 展示FTP服务器管理页面
     * 
     * @return
     */
    public String showFtpServer() {
        FtpServerInfo ftpServerInfo = ftpServerService.getFtpServerAttr();
        ftpServer = JSONObject.fromObject(ftpServerInfo);
        return SUCCESS;
    }

    /**
     * 加载FTP服务器的启动状态及可达状态
     * 
     * @return
     * @throws IOException
     */
    public String loadFtpServerStatus() {
        FtpServerInfo ftpServerStatus = ftpServerService.getFtpServerStatus();
        // 向页面传值
        writeDataToAjax(JSONObject.fromObject(ftpServerStatus));
        return NONE;
    }

    /**
     * 获取服务器根的所有IP地址
     * 
     * @return
     */
    public String loadIpAddress() {
        List<String> ips = ftpServerService.getAllIpAddress();
        JSONArray json = new JSONArray();
        int index = 0;
        for (String ip : ips) {
            JSONObject o = new JSONObject();
            o.put("ipIndex", index++);
            o.put("ip", ip);
            json.add(o);
        }
        // 向页面传值
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取FTP服务器根目录下的所有子文件夹
     * 
     * @return
     */
    public String loadFtpServerDirectories() {
        List<String> directories = ftpServerService.getFtpServerDirectories();
        JSONArray json = new JSONArray();
        int index = 0;
        for (String directory : directories) {
            JSONObject o = new JSONObject();
            o.put("dirIndex", index++);
            o.put("dirName", directory);
            json.add(o);
        }
        // 向页面传值
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 修改FTP服务器基本参数
     * 
     * @return
     */
    public String modifyFtpServerAttr() {
        FtpServerInfo ftpServer = new FtpServerInfo(ip, port, userName, pwd, rootPath, writeable);
        ftpServerService.modifyFtpServerAttr(ftpServer);
        // 如果FTP服务器是启动的，则需要重启FTP服务器
        if (ftpServerService.getFtpServerStatus().isStarted()) {
            ftpServerService.reStartFtpServer();
        }
        return NONE;
    }

    /**
     * 启动FTP服务器
     * 
     * @return
     * @throws IOException
     */
    public String startFtpServer() {
        Map<String, Object> json = new HashMap<String, Object>();
        boolean result = ftpServerService.startFtpServer();
        json.put("success", result);
        // 获取FTP服务器的状态
        FtpServerInfo ftpServerStatus = ftpServerService.getFtpServerStatus();
        json.put("ftpServerStatus", JSONObject.fromObject(ftpServerStatus));
        // 将状态传递给页面
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 停止FTP服务器
     * 
     * @return
     * @throws IOException
     */
    public String stopFtpServer() {
        ftpServerService.stopFtpServer();
        // 获取FTP服务器的状态
        FtpServerInfo ftpServerStatus = ftpServerService.getFtpServerStatus();
        // 将状态传递给页面
        writeDataToAjax(JSONObject.fromObject(ftpServerStatus));
        return NONE;
    }

    /**
     * 重启FTP服务器
     * 
     * @return
     * @throws IOException
     */
    public String reStartFtpServer() {
        Map<String, Object> json = new HashMap<String, Object>();
        boolean result = ftpServerService.reStartFtpServer();
        json.put("success", result);
        // 获取FTP服务器的状态
        FtpServerInfo ftpServerStatus = ftpServerService.getFtpServerStatus();
        json.put("ftpServerStatus", ftpServerStatus);
        // 将状态传递给页面
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public FtpServerService getFtpServerService() {
        return ftpServerService;
    }

    public void setFtpServerService(FtpServerService ftpServerService) {
        this.ftpServerService = ftpServerService;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public JSONObject getFtpServer() {
        return ftpServer;
    }

    public void setFtpServer(JSONObject ftpServer) {
        this.ftpServer = ftpServer;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public boolean isWriteable() {
        return writeable;
    }

    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }
}
