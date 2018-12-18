/***********************************************************************
 * $Id: CmcConfigBackupServiceImpl.java,v1.0 2016年5月20日 下午5:13:43 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.config.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.opensymphony.xwork2.ActionContext;
import com.topvision.ems.congfigbackup.exception.FTPDownloadConfigFileException;
import com.topvision.ems.congfigbackup.exception.UploadConfigFileException;
import com.topvision.ems.congfigbackup.service.ProductionConfigBackupService;
import com.topvision.ems.congfigbackup.util.ConfigBackupUtil;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.ems.upgrade.telnet.TelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.framework.common.FileUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.FtpConnectInfo;
import com.topvision.platform.domain.TftpClientInfo;
import com.topvision.platform.service.FtpConnectService;
import com.topvision.platform.service.TftpClientService;
import com.topvision.platform.util.StringUtil;

/**
 * @author Bravin
 * @created @2016年5月20日-下午5:13:43
 *
 */
@Service("cmcConfigBackupService")
public class CmcConfigBackupServiceImpl extends BaseService implements ProductionConfigBackupService {
    private static final String STRING_TEMPLATE_TFTPTEMP = "{0}META-INF{1}tftpTemp{1}";
    private static final String STRING_TEMPLATE_START_CONFIG = "{0}META-INF{1}startConfig{1}{2}{1}";
    @Autowired
    private TelnetLoginService telnetLoginService;
    @Autowired
    private TelnetUtilFactory telnetUtilFactory;
    @Autowired
    private FtpConnectService ftpConnectService;
    @Autowired
    private TftpClientService tftpClientService;
    @Value("${fileautosave.cc.config:config}")
    private String CC_CONFIG;
    @Value("${fileautosave.cm.config:cm-config}")
    private String CM_CONFIG;
    @Value("${fileautosave.eqam.config:eqam-config}")
    private String EQAM_CONFIG;

    /* (non-Javadoc)
     * @see com.topvision.ems.congfigBackup.service.ProductionConfigBackupService#execDownloadConfigFileCommand(com.topvision.platform.domain.FtpConnectInfo, java.lang.Long)
     */
    @Override
    public void execDownloadAndBackup(Long entityType, Long entityId, String ip) throws Exception {
        FtpConnectInfo connectInfo = ftpConnectService.getFtpConnectAttr();
        TelnetLogin telnetLogin = telnetLoginService.getEntityTelnetLogin(ip);
        TelnetUtil telnetUtil = telnetUtilFactory.getTelnetUtil(entityType, ip, telnetLogin);
        try {
            String path = StringUtil.format(STRING_TEMPLATE_TFTPTEMP, SystemConstants.ROOT_REAL_PATH, File.separator);
            /**文件上传前先清空tftp目录,防止文件使用了历史的备份文件*/
            ConfigBackupUtil.clearCmtsBackUpFile(path, CC_CONFIG, CM_CONFIG, EQAM_CONFIG);
            if (!telnetUtil.uploadConfig(connectInfo, CC_CONFIG)) {
                throw new FTPDownloadConfigFileException();
            }
            telnetUtil.uploadConfig(connectInfo, CM_CONFIG);
            telnetUtil.uploadConfig(connectInfo, EQAM_CONFIG);
            compareAndBackUp(entityId);
        } finally {
            if (telnetUtil != null) {
                telnetUtil.disconnect();
                telnetUtilFactory.releaseTelnetUtil(telnetUtil);
            }
        }

    }

    /**
     * 比较并备份有差别的配置文件
     * @throws IOException 
     * 
     */
    private void compareAndBackUp(Long entityId) throws IOException {
        String root = SystemConstants.ROOT_REAL_PATH;
        String $tftpTemp = StringUtil.format(STRING_TEMPLATE_TFTPTEMP, root, File.separator);
        String $startcon = StringUtil.format(STRING_TEMPLATE_START_CONFIG, root, File.separator, entityId);
        File config = new File($startcon + CC_CONFIG);
        File cmConfig = new File($startcon + CM_CONFIG);
        File eqamConfig = new File($startcon + EQAM_CONFIG);
        /**备份CC的cm-config,config.eqam-config*/
        File tmpConfig = new File($tftpTemp + CC_CONFIG);
        File tmpCmConfig = new File($tftpTemp + CM_CONFIG);
        File tmpEqamConfig = new File($tftpTemp + EQAM_CONFIG);
        boolean backup = false;
        ActionContext context = ServletActionContext.getContext();
        if (context != null) {
            backup = true;
        }
        if (!backup && ConfigBackupUtil.compareFile(config, tmpConfig)) {
            backup = true;
        }
        if (!backup && ConfigBackupUtil.compareFile(cmConfig, tmpCmConfig)) {
            backup = true;
        }
        if (!backup && ConfigBackupUtil.compareFile(eqamConfig, tmpEqamConfig)) {
            backup = true;
        }
        if (backup) {
            // 记录为历史记录
            String destDirectory = ConfigBackupUtil.getDirectory(entityId);
            if (tmpConfig.exists()) {
                FileUtils.copy(tmpConfig, new File(destDirectory + CC_CONFIG));
            }
            if (tmpCmConfig.exists()) {
                FileUtils.copy(tmpCmConfig, new File(destDirectory + CM_CONFIG));
            }
            if (tmpEqamConfig.exists()) {
                FileUtils.copy(tmpEqamConfig, new File(destDirectory + EQAM_CONFIG));
            }
            ConfigBackupUtil.setFileNewest(tmpConfig, entityId, CC_CONFIG);
            ConfigBackupUtil.setFileNewest(tmpCmConfig, entityId, CM_CONFIG);
            ConfigBackupUtil.setFileNewest(tmpEqamConfig, entityId, EQAM_CONFIG);
        }
        //ConfigBackupUtil.clearDirectory($tftpTemp);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.congfigBackup.service.ProductionConfigBackupService#saveConfigFile(java.lang.String, java.lang.Long)
     */
    @Override
    public void saveConfigFile(String ip, Long entityType, Long entityId) {
        // 通过IP地址获得telnetLogin对象
        TelnetLogin telnetLogin = telnetLoginService.getEntityTelnetLogin(ip);
        TelnetUtil telnetUtil = telnetUtilFactory.getTelnetUtil(entityType, ip, telnetLogin);
        try {
            telnetUtil.writeConfig();
        } finally {
            if (telnetUtil != null) {
                telnetUtil.disconnect();
                telnetUtilFactory.releaseTelnetUtil(telnetUtil);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.congfigBackup.service.ProductionConfigBackupService#applyConfigToDevice(java.lang.Long, com.topvision.platform.domain.FtpConnectInfo,  java.io.File)
     */
    @Override
    public void applyConfigToDevice(Long entityId, Long entityType, String ip, String directory) throws Exception {
        TftpClientInfo connectInfo = tftpClientService.getTftpClientInfo();
        TelnetLogin telnetLogin = telnetLoginService.getEntityTelnetLogin(ip);
        TelnetUtil telnetUtil = telnetUtilFactory.getTelnetUtil(entityType, ip, telnetLogin);
        // 下发文件下载命令
        File[] configs = new File(directory).listFiles();
        try {
            for (File file : configs) {
                String name = file.getName();
                String newFileName = name + ConfigBackupUtil.DIRECTORY_FORMAT.format(new Date());
                boolean uploadFileResult = tftpClientService.uploadFile(file, newFileName);
                //boolean uploadFileResult = ftpConnectService.uploadFile(file, name);
                if (!uploadFileResult) {
                    throw new UploadConfigFileException();
                }
                telnetUtil.downLoadConfig(connectInfo, name, newFileName);
            }
        } finally {
            if (telnetUtil != null) {
                telnetUtil.disconnect();
                telnetUtilFactory.releaseTelnetUtil(telnetUtil);
            }
        }
    }
}
