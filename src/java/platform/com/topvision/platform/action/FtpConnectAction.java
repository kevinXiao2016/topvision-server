/***********************************************************************
 * $Id: FtpClientException.java,v1.0 2013-1-22 上午10:50:03 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.domain.FtpFile;
import com.topvision.platform.exception.FtpConnectException;
import com.topvision.platform.service.FtpConnectService;
import com.topvision.platform.util.FtpClientUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author fanzidong
 * @created @2013-1-22-上午10:50:03
 * 
 */
@Controller("ftpConnectAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FtpConnectAction extends BaseAction {

    private static final long serialVersionUID = -3743775149232961865L;
    private final Logger logger = LoggerFactory.getLogger(FtpConnectAction.class);
    @Autowired
    private FtpConnectService ftpConnectService;

    private JSONObject ftpConnect;

    private String ip;
    private int port;
    private String userName;
    private String pwd;
    private String remotePath;
    private String fileName;
    private int fileType;
    // 上传的文件
    private File localFile;
    // 上传文件的名称(struts2解析)
    private String localFileFileName;
    // 上传文件的类型(struts2解析)
    private String localFileContentType;
    // 保存在FTP服务器端的文件名称
    private String remoteFileName;
    // 下载文件时保存到网管服务器端的文件名称
    private String serverFileName;

    /**
     * 展示FTP连接管理页面
     * 
     * @return
     */
    public String showFtpConnect() {
        FtpConnectInfo ftpConnectInfo = ftpConnectService.getFtpConnectAttr();
        ftpConnect = JSONObject.fromObject(ftpConnectInfo);
        return SUCCESS;
    }

    /**
     * 获取FTP状态，是否可以上传文件
     * 
     * @return
     * @throws IOException
     */
    public String checkFtpAvailable() throws IOException {
        boolean status = ftpConnectService.getFtpConnectStatus().isReachable();
        JSONObject json = new JSONObject();
        json.put("status", status);
        json.write(response.getWriter());
        return NONE;
    }

    /**
     * 加载FTP连接的可达状态及读写权限
     * 
     * @return
     */
    public String loadFtpConnectStatus() {
        Map<String, Object> json = new HashMap<String, Object>();
        FtpConnectInfo ftpConnectStatus = ftpConnectService.getFtpConnectStatus();
        json.put("ftpConnectStatus", ftpConnectStatus);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 修改FTP连接基本参数
     * 
     * @return
     */
    public String modiftyFtpConnectAttr() {
        FtpConnectInfo ftpConnect = new FtpConnectInfo(ip, port, userName, pwd, remotePath);
        ftpConnectService.modiftyFtpConnectAttr(ftpConnect);
        return NONE;
    }

    /**
     * 展示文件管理页面
     * 
     * @return
     */
    public String showFileManage() {
        FtpConnectInfo ftpConnectInfo = new FtpConnectInfo(ip, port, userName, pwd, remotePath);
        ftpConnect = JSONObject.fromObject(ftpConnectInfo);
        return SUCCESS;
    }

    /**
     * 获取当前工作目录
     * 
     * @return
     */
    public String loadWorkingDirectory() {
        Map<String, Object> json = new HashMap<String, Object>();
        // 获取当前工作目录
        try {
            String workDir = ftpConnectService.getWorkingDirectory(remotePath);
            json.put("workDir", workDir);
            json.put("success", true);
        } catch (FtpConnectException e) {
            json.put("success", false);
        }
        // 向页面传值
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取FTP服务器上当前目录下的文件列表
     * 
     * @return
     */
    public String loadFileList() {
        Map<String, Object> json = new HashMap<String, Object>();
        // 获取文件列表
        try {
            List<FtpFile> ftpFiles = ftpConnectService.getFileList(remotePath);
            json.put("fileNumber", ftpFiles.size());
            json.put("data", ftpFiles);
            json.put("success", true);
        } catch (FtpConnectException e) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 展示文件上传页面
     * 
     * @return
     */
    public String showFileUpload() {
        return SUCCESS;
    }

    /**
     * 上传本地文件到FTP服务器指定目录
     * 
     * @return message{1.true:上传成功;2.false:上传失败;3.file not found:文件不存在}
     */
    public String uploadFile() {
        String message = "";
        if (localFile.exists()) {
            String remoteFilePathName = remotePath + "/" + remoteFileName;
            boolean result = ftpConnectService.uploadFile(localFile, remoteFilePathName);
            message = String.valueOf(result);
        } else {
            // 告知前台文件不存在
            message = "file not found";
        }
        writeDataToAjax("{message:\"" + message + "\"}");
        return NONE;
    }

    /**
     * 从FTP服务器上将文件下载到网管服务器
     * 
     * @return
     */
    public String loadFtpFileToServer() {
        Map<String, Object> json = new HashMap<String, Object>();
        String fileFullName = "";
        // 判断remotePath是否以/结尾
        fileFullName = remotePath.endsWith("/") ? (remotePath + fileName) : (remotePath + "/" + fileName);
        String fileName = null;
        Pattern pattern = Pattern.compile("^.*\\/(.+)$");
        Matcher matcher = pattern.matcher(fileFullName);
        while (matcher.find()) {
            fileName = matcher.group(1);
        }
        File file = ftpConnectService.downloadFile(fileFullName, fileName);
        json.put("success", (file == null) ? false : true);
        json.put("serverFilePathName", file.getPath());
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 将网管服务器端文件下载到本地
     * 
     * @return
     * @throws IOException
     */
    public String downloadFile() {
        File file = new File(serverFileName);
        FileInputStream fis = null;
        OutputStream out = null;
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            ServletActionContext.getResponse().setContentType("application/x-download");
            // 转码文件名，解决中文名的问题
            fileName = new String(fileName.getBytes(FtpClientUtil.GBK), FtpClientUtil.ISO);
            ServletActionContext.getResponse().addHeader("Content-Disposition", "attachment;filename=" + fileName);
            out = ServletActionContext.getResponse().getOutputStream();
            int i;
            while ((i = fis.read(b)) > 0) {
                out.write(b, 0, i);
            }
        } catch (IOException e) {
            logger.debug("", e);
        } finally {
            // 关闭流,并删除服务器端临时文件
            try {
                if (out != null)
                    out.close();
                if (fis != null)
                    fis.close();
                if (file.exists())
                    file.delete();
            } catch (IOException e) {
                logger.debug("", e);
            }
        }
        return NONE;
    }

    /**
     * 删除FTP服务器上指定的文件
     * 
     * @return
     * @throws IOException
     */
    public String deleteFile() {
        boolean result = false;
        // 判断remotePath是否以/结尾
        String fileFullName = remotePath.endsWith("/") ? (remotePath + fileName) : (remotePath + "/" + fileName);
        try {
            result = ftpConnectService.deleteFile(fileFullName, fileType);
        } catch (FtpConnectException e) {
            result = false;
        }
        // 向页面传值
        writeDataToAjax(String.valueOf(result));
        return NONE;
    }

    /**
     * 获取服务器根的所有IP地址
     * 
     * @return
     */
    public String loadIpAddress() {
        List<String> ips = ftpConnectService.getAllIpAddress();
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

    public String newFolder() {
        boolean result = false;
        // 创建文件夹
        try {
            result = ftpConnectService.createFolder(fileName);
        } catch (FtpConnectException e) {
            result = false;
        }
        // 向页面传值
        writeDataToAjax(String.valueOf(result));
        return NONE;
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

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public JSONObject getFtpConnect() {
        return ftpConnect;
    }

    public void setFtpConnect(JSONObject ftpConnect) {
        this.ftpConnect = ftpConnect;
    }

    public File getLocalFile() {
        return localFile;
    }

    public void setLocalFile(File localFile) {
        this.localFile = localFile;
    }

    public String getLocalFileFileName() {
        return localFileFileName;
    }

    public void setLocalFileFileName(String localFileFileName) {
        this.localFileFileName = localFileFileName;
    }

    public String getLocalFileContentType() {
        return localFileContentType;
    }

    public void setLocalFileContentType(String localFileContentType) {
        this.localFileContentType = localFileContentType;
    }

    public String getRemoteFileName() {
        return remoteFileName;
    }

    public void setRemoteFileName(String remoteFileName) {
        this.remoteFileName = remoteFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public FtpConnectService getFtpConnectService() {
        return ftpConnectService;
    }

    public void setFtpConnectService(FtpConnectService ftpConnectService) {
        this.ftpConnectService = ftpConnectService;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }
}
