/***********************************************************************
 * $Id: ImportAction.java,v1.0 2015-7-9 上午9:39:52 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.action;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.exportAndImport.service.ImportService;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author fanzidong
 * @created @2015-7-9-上午9:39:52
 * 
 */
@Controller("importAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportAction extends BaseAction {

    private static final long serialVersionUID = 550804000052778650L;

    @Autowired
    private ImportService importService;

    private String jconnectionId;

    // 上传的文件
    private File localFile;
    // 上传文件的名称(struts2解析)
    private String localFileFileName;
    // 上传文件的类型(struts2解析)
    private String localFileContentType;

    /**
     * 导入Excel文件
     * 
     * @return
     * @throws IOException
     */
    public String importEntireExcel() throws IOException {
        JSONObject ret = new JSONObject();
        // 接收导入文件
        if (localFile != null && localFile.exists()) {
            // 执行导入操作，并返回操作结果
            Boolean importRst = importService.importEntireExcel(localFile, jconnectionId);
            if (importRst) {
                ret.put("success", importRst);
            } else {
                ret.put("error", "convert error");
            }
        } else {
            ret.put("error", "uploadError");
        }
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(ret);
        return NONE;
    }

    public ImportService getImportService() {
        return importService;
    }

    public void setImportService(ImportService importService) {
        this.importService = importService;
    }

    public String getJconnectionId() {
        return jconnectionId;
    }

    public void setJconnectionId(String jconnectionId) {
        this.jconnectionId = jconnectionId;
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
