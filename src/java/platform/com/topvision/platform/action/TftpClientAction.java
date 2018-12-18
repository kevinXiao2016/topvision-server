/***********************************************************************
 * $Id: TftpClientAction.java,v1.0 2014-1-21 上午10:50:03 $
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

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.TftpClientInfo;
import com.topvision.platform.service.TftpClientService;
import com.topvision.platform.service.impl.TftpClientServiceImpl;

import net.sf.json.JSONObject;

@Controller("tftpClientAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TftpClientAction extends BaseAction {

    private static final long serialVersionUID = -1416153762882857010L;
    private JSONObject tftpClientJson;
    private String ip;
    private Integer port;
    private String remoteFileName;

    // 上传的文件
    private File localFile;
    // 上传文件的名称(struts2解析)
    private String localFileFileName;
    // 上传文件的类型(struts2解析)
    private String localFileContentType;

    @Autowired
    private TftpClientService tftpClientService;

    public String showTftpClient() {
        // 获取TFTP客户端的IP和端口
        TftpClientInfo tftpClientInfo = tftpClientService.getTftpClientInfo();
        tftpClientJson = JSONObject.fromObject(tftpClientInfo);
        return SUCCESS;
    }

    public String modifyTftpClient() {
        TftpClientInfo tftpClientInfo = new TftpClientInfo();
        tftpClientInfo.setIp(ip);
        tftpClientInfo.setPort(port);
        tftpClientService.modifyTftpClient(tftpClientInfo);
        return NONE;
    }

    public String loadTftpFileToServer() throws IOException {
        Boolean result = tftpClientService.downloadFile(remoteFileName);
        JSONObject json = new JSONObject();
        json.put("success", result);
        json.write(response.getWriter());
        return NONE;
    }

    public String downloadTftpFile() throws Exception {
        FileInputStream fis = null;
        OutputStream out = null;
        File file = new File(TftpClientServiceImpl.HUBPATH + File.separator + remoteFileName);
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            ServletActionContext.getResponse().setContentType("application/x-download");
            ServletActionContext.getResponse().addHeader("Content-Disposition",
                    "attachment;filename=" + remoteFileName);
            out = ServletActionContext.getResponse().getOutputStream();
            int i;
            while ((i = fis.read(b)) > 0) {
                out.write(b, 0, i);
            }
        } catch (IOException e) {
            // logger.debug("", e);
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
                // logger.debug("", e);
            }
        }
        return NONE;
    }

    public String upLoadFileToTftpServer() throws IOException {
        // 获取上传的文件主体
        String code = "";
        if (localFile != null && localFile.exists()) {
            Boolean result = tftpClientService.uploadFile(localFile, remoteFileName);
            code = result ? "1" : "-1";
        } else {
            code = "-1";
        }
        ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
        ServletActionContext.getResponse().setCharacterEncoding("utf-8");
        ServletActionContext.getResponse().getWriter().write("{code:\"" + code + "\"}");
        ServletActionContext.getResponse().getWriter().flush();
        ServletActionContext.getResponse().getWriter().close();
        return NONE;
    }

    public JSONObject getTftpClientJson() {
        return tftpClientJson;
    }

    public void setTftpClientJson(JSONObject tftpClientJson) {
        this.tftpClientJson = tftpClientJson;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getRemoteFileName() {
        return remoteFileName;
    }

    public void setRemoteFileName(String remoteFileName) {
        this.remoteFileName = remoteFileName;
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

}
