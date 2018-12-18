/***********************************************************************
 * $Id: OpticalReceiverAction.java,v1.0 2016年9月13日 下午3:12:29 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.action;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcAttribute;
import com.topvision.ems.cmc.ccmts.service.CmcService;
import com.topvision.ems.cmc.opticalreceiver.domain.OpticalReceiverData;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwUpg;
import com.topvision.ems.cmc.opticalreceiver.service.OpticalReceiverService;
import com.topvision.ems.cmc.opticalreceiver.util.OpticalReceiverUtil;
import com.topvision.ems.cmc.util.IPAddressUtils;
import com.topvision.ems.cmc.util.StringUtils;
import com.topvision.framework.EnvironmentConstants;
import com.topvision.framework.utils.HttpUtils;
import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.TftpInfo;
import com.topvision.platform.service.TftpClientService;
import com.topvision.platform.service.TftpServerService;

import net.sf.json.JSONObject;

/**
 * @author haojie
 * @created @2016年9月13日-下午3:12:29
 *
 */
@Controller("opticalReceiverAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OpticalReceiverAction extends BaseAction {
    private static final long serialVersionUID = -1121757442875611057L;
    @Autowired
    private OpticalReceiverService opticalReceiverService;
    @Resource(name = "cmcService")
    private CmcService cmcService;
    @Autowired
    private TftpClientService tftpClientService;
    @Autowired
    private TftpServerService tftpServerService;
    private CmcAttribute cmcAttribute;
    private Long cmcId;
    private OpticalReceiverData opticalReceiverData;

    private String fileName;
    // 上传的文件
    private File localFile;
    // 上传文件的名称(struts2解析)
    private String localFileFileName;
    // 上传文件的类型(struts2解析)
    private String localFileContentType;

    /**
     * 展现光机页面
     * 
     * @return
     */
    public String showOptReciver() {
        setCmcAttribute(cmcService.getCmcAttributeByCmcId(cmcId));
        return SUCCESS;
    }

    /**
     * 加载光机信息（数据库）
     */
    public String loadOpticalReceiverInfo() {
        OpticalReceiverData data = opticalReceiverService.getOpticalReceiverInfo(cmcId);
        writeDataToAjax(data);
        return NONE;
    }

    /**
     * 展示光机固件升级页面
     * 
     * @return
     */
    public String showUpgrade() {
        return SUCCESS;
    }

    /**
     * 光机升级
     * 
     * @throws IOException
     */
    public String upgrade() throws IOException {
        JSONObject ret = new JSONObject();
        if (localFile != null && localFile.exists()) {
            // 将文件拷贝至内置TFTP目录，以备传输至设备(升级只能使用内置TFTP服务器)
            tftpClientService.sendFileToInnerServer(localFile, localFileFileName);

            // 封装升级对象
            TopCcmtsDorFwUpg topCcmtsDorFwUpg = new TopCcmtsDorFwUpg();
            Long entityId = cmcService.getEntityIdByCmcId(cmcId);
            Long cmcIndex = cmcService.getCmcIndexByCmcId(cmcId);
            TftpInfo innerTftpServer = tftpServerService.getTftpServerAttr();
            String tftpServerIp = innerTftpServer.getIp();
            if (StringUtils.isEmpty(tftpServerIp) || IPAddressUtils.isLocalhost(tftpServerIp)) {
                tftpServerIp = EnvironmentConstants.getHostAddress();
            }
            topCcmtsDorFwUpg.setCmcId(cmcId);
            topCcmtsDorFwUpg.setEntityId(entityId);
            topCcmtsDorFwUpg.setCmcIndex(cmcIndex);
            topCcmtsDorFwUpg.setFwUpgDevIndex(OpticalReceiverUtil.generateNextIndex(cmcIndex));
            topCcmtsDorFwUpg.setFwUpgFileName(localFileFileName);
            topCcmtsDorFwUpg.setFwUpgServerAddr(tftpServerIp);
            topCcmtsDorFwUpg.setFwUpgAction(1);
            topCcmtsDorFwUpg.setFwUpgProto(2);// 2 tftp
            opticalReceiverService.upgradeOpticalReceiver(topCcmtsDorFwUpg);
        }
        write(ret, HttpUtils.CONTENT_TYPE_HTML);
        return NONE;
    }

    /**
     * 获取升级进度
     */
    public String getUpgradeProgress() {
        JSONObject json = new JSONObject();
        Integer progress = opticalReceiverService.getUpdateProgress(cmcId);
        json.put("progress", progress);
        write(json, HttpUtils.CONTENT_TYPE_JSON);
        return NONE;
    }

    /**
     * 从设备获取数据
     */
    public String refreshDataFromDevice() {
        opticalReceiverService.refreshOpticalReceiver(cmcId);
        OpticalReceiverData data = opticalReceiverService.getOpticalReceiverInfo(cmcId);
        writeDataToAjax(data);
        return NONE;
    }

    /**
     * 保存光机配置
     */
    public String saveConfig() {
        opticalReceiverService.modifyOpticalReceiver(opticalReceiverData);
        return NONE;
    }

    /**
     * 恢复出厂配置
     */
    public String reset() {
        opticalReceiverService.restorFactory(cmcId);
        return NONE;
    }

    public CmcAttribute getCmcAttribute() {
        return cmcAttribute;
    }

    public void setCmcAttribute(CmcAttribute cmcAttribute) {
        this.cmcAttribute = cmcAttribute;
    }

    public Long getCmcId() {
        return cmcId;
    }

    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
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

    public OpticalReceiverData getOpticalReceiverData() {
        return opticalReceiverData;
    }

    public void setOpticalReceiverData(OpticalReceiverData opticalReceiverData) {
        this.opticalReceiverData = opticalReceiverData;
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
