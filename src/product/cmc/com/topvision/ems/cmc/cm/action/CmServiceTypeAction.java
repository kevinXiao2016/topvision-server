/***********************************************************************
 * $Id: CmServiceTypeAction.java,v1.0 2016年11月3日 上午9:05:08 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.cm.domain.CmServiceType;
import com.topvision.ems.cmc.cm.service.CmServiceTypeService;
import com.topvision.ems.cmc.util.CmcUtil;
import com.topvision.framework.common.ExcelUtil;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.SystemConstants;

/**
 * @author haojie
 * @created @2016年11月3日-上午9:05:08
 *
 */
@Controller("cmServiceTypeAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmServiceTypeAction extends BaseAction {
    private static final long serialVersionUID = 6970602424649595610L;
    @Autowired
    private CmServiceTypeService cmServiceTypeService;
    private List<CmServiceType> cmServiceTypes;
    private CmServiceType cmServiceType;
    private String fileName;
    private String serviceType;
    private JSONObject serviceTypeJson;
    private Boolean deleteStatus;

    public String showCmServiceType() {
        return SUCCESS;
    }

    public String loadCmServiceTypeList() {
        Map<String, Object> json = new HashMap<String, Object>();
        cmServiceTypes = cmServiceTypeService.getCmServiceTypeList();
        json.put("data", cmServiceTypes);
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public String addCmServiceType() {
        CmServiceType cmServiceType = new CmServiceType();
        cmServiceType.setFileName(fileName);
        cmServiceType.setServiceType(serviceType);
        cmServiceTypeService.addCmServiceType(cmServiceType);
        return NONE;
    }

    public String showModifyCmServiceType() {
        cmServiceType = cmServiceTypeService.getCmServiceTypeById(fileName);
        serviceTypeJson = JSONObject.fromObject(cmServiceType);
        return SUCCESS;
    }

    public String modifyCmServiceType() {
        CmServiceType cmServiceType = new CmServiceType();
        cmServiceType.setFileName(fileName);
        cmServiceType.setServiceType(serviceType);
        cmServiceTypeService.modifyCmServiceType(cmServiceType);
        return NONE;
    }

    public String deleteCmServiceType() {
        cmServiceTypeService.deleteCmServiceType(fileName);
        return NONE;
    }

    public String showImportCmServiceType() {
        return SUCCESS;
    }

    /**
     * 下载cm业务类型 导入模板
     * 
     * @return
     * @throws UnsupportedEncodingException
     */
    public String downLoadCmServiceTypeTemplate() throws UnsupportedEncodingException {
        String tmpFile = "cmServiceTypeTemplate.xlsx";
        StringBuilder fileName = new StringBuilder(SystemConstants.ROOT_REAL_PATH);
        fileName.append("META-INF/");
        fileName.append(tmpFile);
        int i;
        byte[] b = new byte[1024];
        OutputStream out = null;
        FileInputStream fis = null;
        ServletActionContext.getResponse().setContentType("application/x-download");
        ServletActionContext.getResponse().addHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(tmpFile, "UTF-8"));
        try {
            File rFile = new File(fileName.toString());
            if (rFile != null) {
                fis = new FileInputStream(rFile);
                out = ServletActionContext.getResponse().getOutputStream();
                while ((i = fis.read(b)) > 0) {
                    out.write(b, 0, i);
                }
            }
        } catch (Exception e) {
            logger.debug("downLoad cmServiceTypeTemplate error:{}", e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (out != null) {
                    out.flush();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                logger.debug("downLoad cmServiceTypeTemplate error:{}", e);
            }
        }
        return NONE;
    }

    /**
     * 扫描CM业务类型导入信息
     * 
     * @return
     * @throws Exception
     */
    public String scanCmServiceTypeImport() throws Exception {
        List<CmServiceType> cmServiceTypes = new ArrayList<CmServiceType>();
        String[][] excelData = null;
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
            Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
            while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
                String inputName = fileParameterNames.nextElement();
                String[] contentType = multiWrapper.getContentTypes(inputName);
                if (isNonEmpty(contentType)) {
                    String[] fileName = multiWrapper.getFileNames(inputName);
                    if (CmcUtil.isNonEmpty(fileName)) {
                        File[] files = multiWrapper.getFiles(inputName);
                        if (files != null) {
                            for (File file1 : files) {
                                try {
                                    excelData = ExcelUtil.getExcelDataFor2007(file1).get(0);
                                    break;
                                } catch (Exception e) {
                                    try {
                                        // 增加2003Excel文档支持
                                        String fileNameStr = file1.getPath();
                                        excelData = ExcelUtil.getExcelDataFor2003(fileNameStr).get(0);
                                        break;
                                    } catch (Exception e1) {
                                        logger.error("", e);
                                        json.put("message", "FileWrong");
                                        writeDataToAjax(JSONObject.fromObject(json));
                                        return NONE;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (excelData == null) {
                json.put("message", "FileWrong");
                writeDataToAjax(JSONObject.fromObject(json));
                return NONE;
            } else {
                for (int i = 1; i < excelData.length; i++) {
                    if (excelData[i] == null) {
                        continue;
                    }
                    if (excelData[i][0] == null || "".endsWith(excelData[i][0].toUpperCase())) {
                        continue;
                    }
                    String fileName = excelData[i][0].trim();
                    String serviceType = excelData[i].length >= 2 ? excelData[i][1].trim() : null;
                    CmServiceType cmServiceType = new CmServiceType();
                    if (fileName != null && fileName.length() > 127) {
                        json.put("message", "fileNameWrong");
                        json.put("errorLine", i + 1);
                        writeDataToAjax(JSONObject.fromObject(json));
                        return NONE;
                    }
                    if (serviceType != null && serviceType.length() > 64) {
                        json.put("message", "serviceTypeWrong");
                        json.put("errorLine", i + 1);
                        writeDataToAjax(JSONObject.fromObject(json));
                        return NONE;
                    }
                    cmServiceType.setFileName(fileName);
                    cmServiceType.setServiceType(serviceType);
                    cmServiceTypes.add(cmServiceType);
                }
                cmServiceTypeService.addCmServiceTypes(cmServiceTypes, deleteStatus);
                json.put("message", "success");
                json.put("number", cmServiceTypes.size());
            }
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
            json.put("message", "fail");
        }
        writeDataToAjax(JSONObject.fromObject(json));
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
        for (int i = 0; i < objArray.length && !result; i++) {
            if (objArray[i] != null) {
                result = true;
            }
        }
        return result;
    }

    /**
     * @return the cmServiceTypes
     */
    public List<CmServiceType> getCmServiceTypes() {
        return cmServiceTypes;
    }

    /**
     * @param cmServiceTypes
     *            the cmServiceTypes to set
     */
    public void setCmServiceTypes(List<CmServiceType> cmServiceTypes) {
        this.cmServiceTypes = cmServiceTypes;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the serviceType
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType
     *            the serviceType to set
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return the cmServiceType
     */
    public CmServiceType getCmServiceType() {
        return cmServiceType;
    }

    /**
     * @param cmServiceType
     *            the cmServiceType to set
     */
    public void setCmServiceType(CmServiceType cmServiceType) {
        this.cmServiceType = cmServiceType;
    }

    /**
     * @return the serviceTypeJson
     */
    public JSONObject getServiceTypeJson() {
        return serviceTypeJson;
    }

    /**
     * @param serviceTypeJson
     *            the serviceTypeJson to set
     */
    public void setServiceTypeJson(JSONObject serviceTypeJson) {
        this.serviceTypeJson = serviceTypeJson;
    }

    /**
     * @return the deleteStatus
     */
    public Boolean getDeleteStatus() {
        return deleteStatus;
    }

    /**
     * @param deleteStatus
     *            the deleteStatus to set
     */
    public void setDeleteStatus(Boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

}
