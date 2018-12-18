/***********************************************************************
 * $Id: CmUpgradeAction.java,v1.0 2016年12月5日 下午2:10:33 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.upgrade.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cm.upgrade.domain.CmUpgradeConfig;
import com.topvision.ems.cm.upgrade.domain.CmcAutoUpgradeProcess;
import com.topvision.ems.cm.upgrade.domain.CmcUpgradeInfo;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmSwVersionTable;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSfFileInfoMgtTable;
import com.topvision.ems.cm.upgrade.service.CmUpgradeService;
import com.topvision.ems.cmc.cm.service.CmService;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.template.service.impl.EntityTypeServiceImpl;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.service.impl.FtpServerServiceImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Rod John
 * @created @2016年12月5日-下午2:10:33
 *
 */
@Controller("cmUpgradeAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CmUpgradeAction extends BaseAction {

    private static final long serialVersionUID = -8661888934387192627L;
    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    private CmService cmService;
    @Autowired
    private CmUpgradeService cmUpgradeService;
    private Long entityId;
    private String fileName;
    private String cmMac;
    // Search Field
    private String search;
    private String status;
    private Long typeId;
    private String folderId;
    // CM Upgrade Config
    private Integer configId;
    private String modulNum;
    private String softVersion;
    private String versionFileName;
    private Integer fileUpdateFlag = 1;
    private List<String> modulList;
    // Apply Auto Config
    private String chooseEntityIds;
    // 1 - database  2 - snmp
    private Integer cmVersionInfoType;

    private JSONArray ccmtsSfFileInfoList = new JSONArray();

    /**
     * Show CCMTS File
     * 
     * @return
     */
    public String showCcmtsFile() {
        return SUCCESS;
    }

    /**
     * Show Cm Upgrade Config
     * 
     * @return
     */
    public String showCmUpgradeConfig() {
        return SUCCESS;
    }

    /**
     * Show Cm Upgrade Statisitcs
     * 
     * @return
     */
    public String showCmUpgradeStatisitcs() {
        return SUCCESS;
    }

    /**
     * Load File
     * 
     * @return
     */
    public String loadCcmtsFile() {
        List<TopCcmtsSfFileInfoMgtTable> result = cmUpgradeService.loadCcmtsFile(entityId);
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("data", result);
        json.put("rowCount", result.size());
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * Delete File
     * 
     * @return
     */
    public String deleteCcmtsFile() {
        cmUpgradeService.deleteCcmtsFile(entityId, fileName);
        return NONE;
    }

    /**
     * UpLoad File
     * 
     * @return
     */
    public String uploadCcmtsFile() {
        String result = null;
        JSONObject ret = new JSONObject();
        try {
            // 上传到CM文件夹
            MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
            Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
            File destFile = null;
            while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
                String inputName = fileParameterNames.nextElement();
                String[] contentType = multiWrapper.getContentTypes(inputName);
                if (isNonEmpty(contentType)) {
                    String[] fileName = multiWrapper.getFileNames(inputName);
                    if (isNonEmpty(fileName)) {
                        File[] files = multiWrapper.getFiles(inputName);
                        if (files != null && files.length > 0) {
                            File tmpFile = files[0];
                            destFile = new File(FtpServerServiceImpl.FTPROOTPATH + File.separator + versionFileName);
                            if (destFile.exists()) {
                                destFile.delete();
                            }
                            tmpFile.renameTo(destFile);
                            break;
                        }
                    }
                }
            }
            Integer fileSize = cmUpgradeService.loadCcmtsFileSize(entityId);
            Integer destSize = (int) destFile.length();
            if (fileSize + destSize >= 20 * 1024 * 1024) {
                result = "fileSizeExceed";
            } else {
                cmUpgradeService.uploadCcmtsFile(entityId, destFile);
                result = "success";
            }
        } catch (Exception e) {
            logger.error("", e);
            result = "failure";
        }
        ret.put("result", result);
        writeDataToAjax(ret);
        return NONE;
    }

    /**
     * Load Upload Process
     * 
     * @return
     */
    public String loadUploadProcess() {
        String result = cmUpgradeService.loadUploadProcess(entityId);
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * Show Upgrade CM
     * 
     * @return
     */
    public String showUpgradeSingleCm() {
        // entityId = cmId
        return SUCCESS;
    }

    /**
     * Load Upgrade CM
     * 
     * @return
     */
    public String loadUpgradeCmData() {
        // entityId = cmId
        Long cmcId = cmService.getCmcIdByCmId(entityId);
        List<TopCcmtsSfFileInfoMgtTable> result = cmUpgradeService.loadCcmtsFile(cmcId);
        cmMac = cmService.getCmAttributeByCmId(entityId).getStatusMacAddress();
        TopCcmtsCmSwVersionTable version = cmUpgradeService.getSingleCmSwVersion(entityId);
        if (version != null) {
            modulNum = version.getTopCcmtsCmModelNum();
            softVersion = version.getTopCcmtsCmSwVersion();
        }
        JSONObject data = new JSONObject();
        data.put("cmMac", cmMac);
        data.put("modulNum", modulNum);
        data.put("softVersion", softVersion);
        data.put("ccmtsSfFileInfoList", result);
        writeDataToAjax(data);
        return NONE;
    }

    /**
     * Upgrade Single CM
     * 
     * @return
     */
    public String upgradeSingleCm() {
        CmAttribute cmAttribute = cmService.getCmAttributeByCmId(entityId);
        String result = cmUpgradeService.upgradeSingleCm(cmAttribute.getCmcId(), cmMac, cmAttribute.getStatusIndex(), fileName);
        writeDataToAjax(result);
        return NONE;
    }

    /**
     * Show Auto Upgrade
     * 
     * @return
     */
    public String showAutoUpgrade() {
        return SUCCESS;
    }

    /**
     * Show Upgrade Upload
     * 
     * @return
     */
    public String showCmUpgradeUpload() {
        return SUCCESS;
    }

    /**
     * Load Auto Upgrade Config
     * 
     * @return
     */
    public String loadAutoUpgradeConfig() {
        List<CmUpgradeConfig> configs = cmUpgradeService.loadCmUpgradeConfig();
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("data", configs);
        json.put("rowCount", configs.size());
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 
     * 
     * @return
     */
    public String showAddAutoUpgradeConfig() {
        this.modulList = cmUpgradeService.loadCmModuleNum();
        JSONArray profileArr = JSONArray.fromObject(modulList);
        writeDataToAjax(profileArr);
        return NONE;
    }

    /**
     * Add Auto Upgrade Config
     * 
     * @return
     */
    public String addAutoUpgradeConfig() {
    	JSONObject ret = new JSONObject();
        String result = null;
        try {
            CmUpgradeConfig config = uploadFileToCmSoftware(configId, modulNum, softVersion, versionFileName);
            cmUpgradeService.addAutoUpgradeConfig(config);
            result = "success";
        } catch (Exception e) {
            logger.error("", e);
            result = "failure";
        }
        ret.put("result", result);
        writeDataToAjax(ret);
        return NONE;
    }

    /**
     * Modify Auto Upgrade Config
     * 
     * @return
     */
    public String modifyAutoUpgradeConfig() {
    	JSONObject ret = new JSONObject();
        String result = null;
        try {
            CmUpgradeConfig config = null;
            if (fileUpdateFlag == 1) {
                config = uploadFileToCmSoftware(configId, modulNum, softVersion, versionFileName);
            } else {
                config = new CmUpgradeConfig(configId, modulNum, softVersion);
            }
            cmUpgradeService.modifyAutoUpgradeConfig(config);
            result = "success";
        } catch (Exception e) {
            logger.error("", e);
            result = "failure";
        }
        ret.put("result", result);
        writeDataToAjax(ret);
        return NONE;
    }

    /**
     * Delete Auto Upgrade Config
     * 
     * @return
     */
    public String deleteAutoUpgradeConfig() {
        cmUpgradeService.deleteAutoUpgradeConfig(configId);
        return NONE;
    }

    /**
     * Load Auto Upgrade Entity
     * 
     * @return
     */
    public String loadCcmtsE() {
        Map<String, Object> params = new HashMap<>();
        if (search != null && !"".equals(search)) {
            params.put("search", search);
        }
        if (status != null && !"".equals(status)) {
            params.put("status", status);
        }
        if (typeId != null && !"".equals(typeId)) {
            params.put("typeId", typeId);
        }
        if (folderId != null && !"".equals(folderId)) {
            params.put("folderId", folderId);
        }
        List<CmcUpgradeInfo> cmcUpgradeInfos = cmUpgradeService.loadCmcUpgradeInfo(params);
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("data", cmcUpgradeInfos);
        json.put("rowCount", cmcUpgradeInfos.size());
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * Load Entity Type
     * 
     * @return
     */
    public String loadEntityType() {
        Map<String, Long> typeIds = new HashMap<>();
        EntityType cc8800E = entityTypeService.getEntityType(EntityTypeServiceImpl.CCMTS8800E);
        typeIds.put(cc8800E.getDisplayName(), cc8800E.getTypeId());
        EntityType cc8800CE = entityTypeService.getEntityType(EntityTypeServiceImpl.CCMTS8800C_E);
        typeIds.put(cc8800CE.getDisplayName(), cc8800CE.getTypeId());
        EntityType cc8800DE = entityTypeService.getEntityType(EntityTypeServiceImpl.CCMTS8800D_E);
        typeIds.put(cc8800DE.getDisplayName(), cc8800DE.getTypeId());
        EntityType cc8800F = entityTypeService.getEntityType(EntityTypeServiceImpl.CCMTS8800F);
        typeIds.put(cc8800F.getDisplayName(), cc8800F.getTypeId());
        JSONObject object = JSONObject.fromObject(typeIds);
        writeDataToAjax(object);
        return NONE;
    }

    /**
     * Load TopoFolder
     * 
     * @return
     */
    public String loadFolderIdList() {
        return NONE;
    }

    /**
     * Load Modul Num
     * 
     * @return
     */
    public String loadModulNum() {
        List<String> modulList = cmUpgradeService.loadCmModuleNum();
        JSONArray profileArr = JSONArray.fromObject(modulList);
        writeDataToAjax(profileArr);
        return NONE;
    }

    /**
     *  
     * 
     * @return
     */
    public String showCmVersionStatics() {
        return SUCCESS;
    }

    /**
     * 
     * 
     * @return
     */
    public String loadCmVersionStatics() {
        Map<String, Map<String, Integer>> result = cmUpgradeService.getCmVersionInfo(cmVersionInfoType, entityId);
        JSONObject resultObject = JSONObject.fromObject(result);
        writeDataToAjax(resultObject);
        return NONE;
    }

    /**
     * 传文件
     * 
     */
    private CmUpgradeConfig uploadFileToCmSoftware(Integer configId, String modulNum, String softVersion, String versionFileName) {
        // 上传到CM文件夹
        MultiPartRequestWrapper multiWrapper = (MultiPartRequestWrapper) ServletActionContext.getRequest();
        Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
        Integer fileSize = null;
        while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
            String inputName = fileParameterNames.nextElement();
            String[] contentType = multiWrapper.getContentTypes(inputName);
            if (isNonEmpty(contentType)) {
                String[] fileName = multiWrapper.getFileNames(inputName);
                if (isNonEmpty(fileName)) {
                    File[] files = multiWrapper.getFiles(inputName);
                    if (files != null && files.length > 0) {
                        File tmpFile = files[0];
                        File destFile = new File(FtpServerServiceImpl.FTPROOTPATH + File.separator + versionFileName);
                        if (destFile.exists()) {
                            destFile.delete();
                        }
                        tmpFile.renameTo(destFile);
                        fileSize = (int) destFile.length();
                        break;
                    }
                }
            }
        }
        CmUpgradeConfig config = new CmUpgradeConfig(configId, modulNum, softVersion, versionFileName, fileSize);
        return config;
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
     * Apply Auto Upgrade Process
     * 
     * @return
     */
    public String applyAutoUpgradeConfig() {
        List<Long> entityIds = new ArrayList<>();
        for (String tmp : chooseEntityIds.split(",")) {
            entityIds.add(Long.parseLong(tmp));
        }
        cmUpgradeService.applyAutoUpgradeConfig(entityIds);
        return NONE;
    }

    /**
     * Show Auto Upgrade Result
     * 
     * @return
     */
    public String showAutoUpgradeResult() {
        return SUCCESS;
    }

    /**
     * Load Auto Upgrade Process
     * 
     * @return
     */
    public String loadAutoUpgradeProcess() {
        List<CmcAutoUpgradeProcess> processes = cmUpgradeService.loadAutoUpgradeProcess();
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("data", processes);
        json.put("rowCount", processes.size());
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * @return the entityService
     */
    public EntityService getEntityService() {
        return entityService;
    }

    /**
     * @param entityService the entityService to set
     */
    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    /**
     * @return the entityTypeService
     */
    public EntityTypeService getEntityTypeService() {
        return entityTypeService;
    }

    /**
     * @param entityTypeService the entityTypeService to set
     */
    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    /**
     * @return the cmUpgradeService
     */
    public CmUpgradeService getCmUpgradeService() {
        return cmUpgradeService;
    }

    /**
     * @param cmUpgradeService the cmUpgradeService to set
     */
    public void setCmUpgradeService(CmUpgradeService cmUpgradeService) {
        this.cmUpgradeService = cmUpgradeService;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the cmMac
     */
    public String getCmMac() {
        return cmMac;
    }

    /**
     * @param cmMac the cmMac to set
     */
    public void setCmMac(String cmMac) {
        this.cmMac = cmMac;
    }

    /**
     * @return the search
     */
    public String getSearch() {
        return search;
    }

    /**
     * @param search the search to set
     */
    public void setSearch(String search) {
        this.search = search;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the typeId
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * @param typeId the typeId to set
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the folderId
     */
    public String getFolderId() {
        return folderId;
    }

    /**
     * @param folderId the folderId to set
     */
    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    /**
     * @return the configId
     */
    public Integer getConfigId() {
        return configId;
    }

    /**
     * @param configId the configId to set
     */
    public void setConfigId(Integer configId) {
        this.configId = configId;
    }

    /**
     * @return the modulNum
     */
    public String getModulNum() {
        return modulNum;
    }

    /**
     * @param modulNum the modulNum to set
     */
    public void setModulNum(String modulNum) {
        this.modulNum = modulNum;
    }

    /**
     * @return the softVersion
     */
    public String getSoftVersion() {
        return softVersion;
    }

    /**
     * @param softVersion the softVersion to set
     */
    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    /**
     * @return the versionFileName
     */
    public String getVersionFileName() {
        return versionFileName;
    }

    /**
     * @param versionFileName the versionFileName to set
     */
    public void setVersionFileName(String versionFileName) {
        this.versionFileName = versionFileName;
    }

    /**
     * @return the modulList
     */
    public List<String> getModulList() {
        return modulList;
    }

    /**
     * @param modulList the modulList to set
     */
    public void setModulList(List<String> modulList) {
        this.modulList = modulList;
    }

    /**
     * @return the chooseEntityIds
     */
    public String getChooseEntityIds() {
        return chooseEntityIds;
    }

    /**
     * @param chooseEntityIds the chooseEntityIds to set
     */
    public void setChooseEntityIds(String chooseEntityIds) {
        this.chooseEntityIds = chooseEntityIds;
    }

    /**
     * @return the fileUpdateFlag
     */
    public Integer getFileUpdateFlag() {
        return fileUpdateFlag;
    }

    /**
     * @param fileUpdateFlag the fileUpdateFlag to set
     */
    public void setFileUpdateFlag(Integer fileUpdateFlag) {
        this.fileUpdateFlag = fileUpdateFlag;
    }

    /**
     * @return the cmVersionInfoType
     */
    public Integer getCmVersionInfoType() {
        return cmVersionInfoType;
    }

    /**
     * @param cmVersionInfoType the cmVersionInfoType to set
     */
    public void setCmVersionInfoType(Integer cmVersionInfoType) {
        this.cmVersionInfoType = cmVersionInfoType;
    }

    public JSONArray getCcmtsSfFileInfoList() {
        return ccmtsSfFileInfoList;
    }

    public void setCcmtsSfFileInfoList(JSONArray ccmtsSfFileInfoList) {
        this.ccmtsSfFileInfoList = ccmtsSfFileInfoList;
    }

}
