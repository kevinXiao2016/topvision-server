/***********************************************************************
 * $Id: OltConfigFileServiceImpl.java,v1.0 2016年1月9日 下午4:03:39 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.config.service.impl;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.topvision.ems.congfigbackup.exception.SaveConfigException;
import com.topvision.ems.congfigbackup.exception.UploadConfigFileException;
import com.topvision.ems.congfigbackup.service.ProductionConfigBackupService;
import com.topvision.ems.congfigbackup.util.ConfigBackupUtil;
import com.topvision.ems.epon.config.domain.OltControlFileCommand;
import com.topvision.ems.epon.config.facade.OltConfigFileFacade;
import com.topvision.ems.epon.olt.service.OltUploadAndUpdateService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.FileUtils;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.service.FtpConnectService;
import com.topvision.platform.util.FtpClientUtil;

/**
 * @author Bravin
 * @created @2016年1月9日-下午4:03:39
 *
 */
@Service("oltConfigBackupService")
public class OltConfigBackupServiceImpl implements ProductionConfigBackupService {
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private OltUploadAndUpdateService oltUploadAndUpdateService;
    @Autowired
    private FtpConnectService ftpConnectService;
    @Value("${fileautosave.startcon:startcon.cfg}")
    private String START_CON_CFG;

    @Override
    public void execDownloadAndBackup(Long entityType, Long entityId, String ip) throws IOException {
        //备份配置文件之前先判断配置文件的状态
        Integer configFileStatus = configFileStatus(entityId, ip);
        if (EponConstants.OLT_SAVE_CONFIG_SAVING.equals(configFileStatus)) {
            return;
        } else {
            FtpConnectInfo connectInfo = ftpConnectService.getFtpConnectAttr();
            // 先删除FTP服务器上的config文件
            ftpConnectService.deleteFile(START_CON_CFG, FtpClientUtil.FILE_TYPE);
            OltControlFileCommand controlFileCommand = new OltControlFileCommand();
            controlFileCommand.setFileTransferProtocolType(EponConstants.FILE_TRANS_PRO_FTP);
            controlFileCommand.setServerIpAddress(connectInfo.getIp());
            controlFileCommand.setFtpUserName(connectInfo.getUserName());
            controlFileCommand.setFtpUserPassword(connectInfo.getPwd());
            String ftpServerFileName = "startcon.cfg";
            String configFilePath = "/tffs0/" + "startcon.cfg";
            controlFileCommand.setTransferFileDstNamePath(ftpServerFileName);
            controlFileCommand.setTransferFileSrcNamePath(configFilePath);
            // 网管页面所谓的下载实际是对OLT设备的上传操作，OLT设备向FTP服务器上传文件
            controlFileCommand.setTransferAction(EponConstants.FILE_OLT_UPLOAD);
            oltUploadAndUpdateService.contorlOltFile(entityId, controlFileCommand);
            if (!oltUploadAndUpdateService.getOltTranslationStatus(entityId, 0L).equals(
                    EponConstants.FILE_TRANS_SUCCESS)) {
                throw new RuntimeException("save failed!");
            }
            compareAndBackUp(entityId);
        }
    }

    private void compareAndBackUp(Long entityId) throws IOException {
        if (ftpConnectService.isFileExist(START_CON_CFG)) {
            // 以下三步为了将文件写入相应的downLoad文件夹以作记录
            File srcFile = ftpConnectService.downloadFile(START_CON_CFG, START_CON_CFG);
            // 文件差异性检查,如果不一样，就将当前文件设为最新的配置文件,并复制一份到设备配置文件目录下
            if (ConfigBackupUtil.compareFile(srcFile, entityId, START_CON_CFG)) {
                String destDirectory = ConfigBackupUtil.getDirectory(entityId);
                File dstFile = new File(destDirectory + START_CON_CFG);
                FileUtils.copy(srcFile, dstFile);
                // 设该文件为最新的配置文件
                ConfigBackupUtil.setFileNewest(srcFile, entityId, START_CON_CFG);
            }
        }
    }

    @Override
    public void saveConfigFile(String ip, Long entityType, Long entityId) {
        // facade 获取topSysOltSaveStatus
        OltConfigFileFacade facade = facadeFactory.getFacade(ip, OltConfigFileFacade.class);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer topSysOltSaveStatus = facade.getOltSaveStatus(snmpParam);
        if (topSysOltSaveStatus == null) {
            throw new SaveConfigException("Business.connection");
        } else {
            // 如果不为 savedOrReady(2)和savedFail(3)则抛异常不能马上保存
            if (!topSysOltSaveStatus.equals(EponConstants.OLT_SAVE_CONFIG_SAVEDORREADY)
                    && !topSysOltSaveStatus.equals(EponConstants.OLT_SAVE_CONFIG_SAVEDFAIL)) {
                throw new SaveConfigException("Business.saveConfig");
            } else {
                // 发送facade保存操作
                facade.saveOltConfig(snmpParam);
            }
        }
    }

    @Override
    public void applyConfigToDevice(Long entityId, Long entityType, String ip, String directory) throws Exception {
        FtpConnectInfo connectInfo = ftpConnectService.getFtpConnectAttr();
        File startconfig = new File(directory + File.separator + START_CON_CFG);
        ftpConnectService.deleteFile(START_CON_CFG, FtpClientUtil.FILE_TYPE);
        boolean uploadFileResult = ftpConnectService.uploadFile(startconfig, START_CON_CFG);
        if (!uploadFileResult) {
            throw new UploadConfigFileException();
        }
        OltControlFileCommand oltControlFileCommand = new OltControlFileCommand();
        oltControlFileCommand.setFileTransferProtocolType(EponConstants.FILE_TRANS_PRO_FTP);
        oltControlFileCommand.setServerIpAddress(connectInfo.getIp());
        oltControlFileCommand.setFtpUserName(connectInfo.getUserName());
        oltControlFileCommand.setFtpUserPassword(connectInfo.getPwd());
        oltControlFileCommand.setTransferFileDstNamePath("/tffs0/startcon.cfg");
        oltControlFileCommand.setTransferFileSrcNamePath(START_CON_CFG);
        // 网管页面所谓的上传实际是对OLT设备的下载操作，OLT设备从FTP服务器下载文件
        oltControlFileCommand.setTransferAction(EponConstants.FILE_OLT_DOWNLOAD);
        oltUploadAndUpdateService.contorlOltFile(entityId, oltControlFileCommand);
        int fileStatusResult = oltUploadAndUpdateService.getOltTranslationStatus(entityId, startconfig.length());
        if (fileStatusResult != EponConstants.FILE_TRANS_SUCCESS) {
            throw new RuntimeException("apply failed!");
        }
    }

    private Integer configFileStatus(Long entityId, String ip) {
        OltConfigFileFacade facade = facadeFactory.getFacade(ip, OltConfigFileFacade.class);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        Integer configStatus = EponConstants.OLT_SAVE_CONFIG_SAVEDORREADY;
        int count = 0;
        while (count < 3) {
            try {
                configStatus = facade.getOltSaveStatus(snmpParam);
                if (configStatus == null || EponConstants.OLT_SAVE_CONFIG_SAVING.equals(configStatus)) {
                    Thread.sleep(10000);
                    continue;
                } else {
                    break;
                }
            } catch (Exception e) {
                continue;
            } finally {
                count++;
            }
        }
        return configStatus;
    }
}
