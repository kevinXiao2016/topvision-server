/***********************************************************************
 * $Id: TftpServerAction.java,v1.0 2013-1-26 下午4:19:30 $
 * 
 * @author: Administrator
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.TftpFile;
import com.topvision.platform.domain.TftpInfo;
import com.topvision.platform.service.TftpServerService;
import com.topvision.platform.util.FtpClientUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Administrator
 * @created @2013-1-26-下午4:19:30
 * 
 */
@Controller("tftpServerAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TftpServerAction extends BaseAction {
    private static final long serialVersionUID = 7217408112550934396L;
    private final Logger logger = LoggerFactory.getLogger(TftpServerAction.class);
    @Autowired
    private TftpServerService tftpServerService;

    private JSONObject tftpServer;

    private String ip;
    private int port;
    private String rootPath;
    private String fileName;
    private String remoteFileName;
    // 下载文件时保存到网管服务器端的文件名称
    private String serverFileName;

    /**
     * 展示TFTP服务器管理页面
     * 
     * @return
     */
    public String showTftpServer() {
        TftpInfo tftpServerInfo = tftpServerService.getTftpServerAttr();
        setTftpServer(JSONObject.fromObject(tftpServerInfo));
        return SUCCESS;
    }

    /**
     * 获取主机的所有IP地址
     * 
     * @return
     */
    public String loadIpAddress() {
        List<String> ips = tftpServerService.getAllIpAddress();
        JSONArray json = new JSONArray();
        for (String ip : ips) {
            JSONObject o = new JSONObject();
            o.put("ip", ip);
            json.add(o);
        }
        // 向页面传值
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 获取TFTP服务器根目录下的所有子文件夹
     * 
     * @return
     */
    public String loadTftpServerDirectories() {
        List<String> directories = tftpServerService.getTftpServerDirectories();
        JSONArray json = new JSONArray();
        for (String directory : directories) {
            JSONObject o = new JSONObject();
            o.put("dirName", directory);
            json.add(o);
        }
        // 向页面传值
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 检查端口是否被占用
     * 
     * @return
     */
    public String loadPortOccupancy() {
        JSONObject json = new JSONObject();
        json.put("result", tftpServerService.isUdpPortUsed(port));
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 修改TFTP服务器基本参数
     * 
     * @return
     */
    public String modifyTftpServerAttr() {
        TftpInfo tftpServer = new TftpInfo(ip, port, rootPath);
        tftpServerService.modifyTftpServerAttr(tftpServer);
        // 如果TFTP服务器是启动的，则需要重启TFTP服务器
        if (tftpServerService.isTftpServerStarted()) {
            tftpServerService.reStartTftpServer();
        }
        return NONE;
    }

    /**
     * 启动TFTP服务器
     * 
     * @return
     */
    public String startTftpServer() {
        JSONObject json = new JSONObject();
        json.put("result", tftpServerService.startTftpServer());
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 停止TFTP服务器
     * 
     * @return
     */
    public String stopTftpServer() {
        tftpServerService.stopTftpServer();
        return NONE;
    }

    /**
     * 重启TFTP服务器
     * 
     * @return
     */
    public String reStartTftpServer() {
        JSONObject json = new JSONObject();
        json.put("result", tftpServerService.reStartTftpServer());
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 加载TFTP服务器的启动状态
     * 
     * @return
     * @throws IOException
     */
    public String loadTftpServerStatus() {
        JSONObject json = new JSONObject();
        json.put("result", tftpServerService.isTftpServerStarted());
        writeDataToAjax(json);
        return NONE;
    }

    /**
     * 展示文件管理页面
     * 
     * @return
     */
    public String showFileManage() {
        return SUCCESS;
    }

    /**
     * 加载TFTP服务器上的所有文件
     * 
     * @return
     * @throws IOException
     */
    public String loadFileList() {
        Map<String, Object> json = new HashMap<String, Object>();
        List<TftpFile> tftpFiles = tftpServerService.getFileList();
        json.put("fileNumber", tftpFiles.size());
        json.put("data", tftpFiles);
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
     * 上传本地文件到TFTP服务器指定目录
     * 
     * @return message{1.true:上传成功;2.false:上传失败;3.file not found:文件不存在}
     */
    public String uploadFile() {
        boolean result = false;
        // 获取前台传过来的文件内容及参数
        MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
        Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
        // 取出每一个参数,其实只有一个文件
        while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
            String fileParameterName = fileParameterNames.nextElement();
            String[] contentType = multiWrapper.getContentTypes(fileParameterName);
            if (isNonEmpty(contentType)) {
                String[] fileName = multiWrapper.getFileNames(fileParameterName);
                if (isNonEmpty(fileName)) {
                    File[] files = multiWrapper.getFiles(fileParameterName);
                    if (files != null) {
                        for (File file : files) {
                            // 终于拿到文件了
                            result = tftpServerService.uploadFile(file, remoteFileName);
                            // 对于这里的上传操作，这里只处理单个文件
                            break;
                        }
                    }
                }
            }
        }
        // 将成功与否传递给页面
        writeDataToAjax(String.valueOf(result));
        return NONE;
    }

    /**
     * 读取文件信息的时候判断数组是否有可用对象
     * 
     * @param objArray
     *            数组列表
     * @return String
     */
    private boolean isNonEmpty(Object[] objArray) {
        boolean result = false;
        for (int index = 0; index < objArray.length && !result; index++) {
            if (objArray[index] != null) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 将FTP服务器上将文件下载到本地
     * 
     * @return
     * @throws IOException
     */
    public String downloadFile() {
        // 拼接出TFTP服务器端文件路径
        String serverFilePathName = TftpInfo.getTftprootpath() + tftpServerService.getTftpServerAttr().getRootPath()
                + "/" + remoteFileName;
        File file = new File(serverFilePathName);
        FileInputStream fis = null;
        OutputStream out = null;
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            ServletActionContext.getResponse().setContentType("application/x-download");
            // 转码文件名，解决中文名的问题
            // 从serverFileName中获取文件名
            String fileName = null;
            Pattern pattern = Pattern.compile("^.*\\/(.+)$");
            Matcher matcher = pattern.matcher(serverFilePathName);
            while (matcher.find()) {
                fileName = matcher.group(1);
            }
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
            } catch (IOException e) {
                logger.debug("", e);
            }
        }
        return NONE;
    }

    /**
     * 删除TFTP服务器上指定的文件
     * 
     * @return
     */
    public String deleteTftpFile() {
        Map<String, Object> json = new HashMap<String, Object>();
        boolean deleted = tftpServerService.deleteFile(fileName);
        json.put("result", deleted);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public TftpServerService getTftpServerService() {
        return tftpServerService;
    }

    public void setTftpServerService(TftpServerService tftpServerService) {
        this.tftpServerService = tftpServerService;
    }

    public JSONObject getTftpServer() {
        return tftpServer;
    }

    public void setTftpServer(JSONObject tftpServer) {
        this.tftpServer = tftpServer;
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

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRemoteFileName() {
        return remoteFileName;
    }

    public void setRemoteFileName(String remoteFileName) {
        this.remoteFileName = remoteFileName;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    public Logger getLogger() {
        return logger;
    }

}
