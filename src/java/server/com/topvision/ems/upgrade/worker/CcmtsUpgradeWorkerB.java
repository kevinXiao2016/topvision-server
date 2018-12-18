/***********************************************************************
 * $Id: CmtsUpgradeWorkerB.java,v1.0 2014年9月23日 下午7:08:54 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.worker;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.ems.upgrade.domain.UpgradeEntity;
import com.topvision.ems.upgrade.domain.UpgradeRecord;
import com.topvision.ems.upgrade.listener.UpgradeTftpEventListener;
import com.topvision.ems.upgrade.service.UpgradeCheckService;
import com.topvision.ems.upgrade.service.UpgradeJobService;
import com.topvision.ems.upgrade.service.UpgradeParamService;
import com.topvision.ems.upgrade.service.UpgradeRecordService;
import com.topvision.ems.upgrade.telnet.CcmtsTelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.ems.upgrade.thread.UpgradeCC8800BCmdThread;
import com.topvision.ems.upgrade.utils.UpgradeStatusConstants;
import com.topvision.framework.common.IpUtils;
import com.topvision.platform.service.TftpServerService;

/**
 * @author loyal
 * @created @2014年9月23日-下午7:08:54
 * 
 */
public class CcmtsUpgradeWorkerB extends UpgradeWorker {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private TelnetUtilFactory telnetUtilFactory;
    private CcmtsTelnetUtil ccmtsTelnetUtil;
    // 检查CC是否在线的最长等待时间 默认5分钟
    private Long waitTime = 300000L;
    private NumberFormat nf = new DecimalFormat("##");

    private boolean checkUpgradeCompleteState = false;
    private double tftpComplete = 0;

    @Override
    public UpgradeEntity call() {
        UpgradeEntity upgradeEntity = new UpgradeEntity();
        UpgradeRecord upgradeRecord = new UpgradeRecord();
        upgradeEntity = this.getUpgradeEntity();
        upgradeRecord = this.getUpgradeRecord();
        String ip = upgradeEntity.getIp();
        logger.debug("start upgrade" + ip);

        TelnetLoginService telnetLoginService = (TelnetLoginService) beanFactory.getBean("telnetLoginService");
        UpgradeCheckService upgradeCheckService = (UpgradeCheckService) beanFactory.getBean("upgradeCheckC_BService");
        TftpServerService tftpServerService = (TftpServerService) beanFactory.getBean("tftpServerService");
        UpgradeJobService upgradeJobService = (UpgradeJobService) beanFactory.getBean("upgradeJobService");
        UpgradeRecordService upgradeRecordService = (UpgradeRecordService) beanFactory.getBean("upgradeRecordService");
        UpgradeParamService upgradeParamService = (UpgradeParamService) beanFactory.getBean("upgradeParamService");
        Long writeConfig = upgradeParamService.getUpgradeGlobalParam().getWriteConfig();
        TelnetLogin telnetLogin = telnetLoginService.getTelnetLoginConfigByIp(new IpUtils(ip).longValue());
        if (telnetLogin == null) {
            telnetLogin = telnetLoginService.getGlobalTelnetLogin();
        }

        telnetUtilFactory = (TelnetUtilFactory) beanFactory.getBean("telnetUtilFactory");

        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.TELNET_NOW);
        upgradeJobService.updateUpgradeEntity(upgradeEntity);

        try {
            ccmtsTelnetUtil = (CcmtsTelnetUtil) telnetUtilFactory.getCcmtsTelnetUtil(ip);
            ccmtsTelnetUtil.connect(ip);
            if (!ccmtsTelnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(),
                    telnetLogin.getEnablePassword(),telnetLogin.getIsAAA())) {
                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.TELNET_ERROR);
                upgradeEntity.setRetry(false);
                upgradeJobService.updateUpgradeEntity(upgradeEntity);

                upgradeRecord.setStatus(UpgradeStatusConstants.TELNET_ERROR);
                upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
                return upgradeEntity;
            }
        } catch (Exception e) {
            upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.TELNET_ERROR);
            upgradeEntity.setRetry(false);
            upgradeJobService.updateUpgradeEntity(upgradeEntity);

            upgradeRecord.setStatus(UpgradeStatusConstants.TELNET_ERROR);
            upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
            upgradeRecordService.saveUpgradeRecord(upgradeRecord);
            logger.debug("connnet " + ip + "error", e);
            telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
            return upgradeEntity;
        }
        Long now = System.currentTimeMillis();
        upgradeEntity.setUpgradeNote("");
        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKONLINE_NOW);
        upgradeJobService.updateUpgradeEntity(upgradeEntity);
        while (true) {
            boolean online = upgradeCheckService.checkOnlineStatus(ccmtsTelnetUtil);
            if (online) {
                logger.info("CC8800B [" + ip + "] is online");
                break;
            } else {
                if (waitTime < System.currentTimeMillis() - now) {
                    logger.info("check CC8800B [" + ip + "] online state time out  ");
                    upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKONLINE_ERROR);
                    upgradeEntity.setRetry(false);
                    upgradeJobService.updateUpgradeEntity(upgradeEntity);

                    upgradeRecord.setStatus(UpgradeStatusConstants.CHECKONLINE_ERROR);
                    upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                    upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                    telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
                    return upgradeEntity;
                } else {
                    logger.trace("check CC8800B [" + ip + "] online state wait next time ");
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKONLINE_ERROR);
                        upgradeEntity.setRetry(false);
                        upgradeJobService.updateUpgradeEntity(upgradeEntity);

                        upgradeRecord.setStatus(UpgradeStatusConstants.CHECKONLINE_ERROR);
                        upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                        upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                        telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
                        return upgradeEntity;
                    }
                }
            }

        }

        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKUBOOT_NOW);
        upgradeJobService.updateUpgradeEntity(upgradeEntity);

        boolean uboot = upgradeCheckService.checkUbootVersion(ccmtsTelnetUtil, ubootVersion);
        if (uboot) {
            logger.info("CC8800B [" + ip + "]  uboot check success");
        } else {
            logger.info("CC8800B [" + ip + "]  uboot check fail");
            upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.CHECKUBOOT_ERROR);
            upgradeEntity.setRetry(false);
            upgradeJobService.updateUpgradeEntity(upgradeEntity);

            upgradeRecord.setStatus(UpgradeStatusConstants.CHECKUBOOT_ERROR);
            upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
            upgradeRecordService.saveUpgradeRecord(upgradeRecord);
            telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
            return upgradeEntity;
        }
        String upgradeCmd = "load image tftp " + tftpIp + " " + imageFile;

        ccmtsTelnetUtil.setTimeout(1800000l);// 由于升级时间较长，超时时间设置为30分钟

        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.LOADIMAGE_NOW);
        upgradeJobService.updateUpgradeEntity(upgradeEntity);

        UpgradeCC8800BCmdThread upgradeCC8800BCmdThread = new UpgradeCC8800BCmdThread(ccmtsTelnetUtil, upgradeCmd);
        upgradeCC8800BCmdThread.start();

        // 监听tftp传输进度
        UpgradeTftpEventListener upgradeTftpEventListener = new UpgradeTftpEventListener(ip);
        tftpServerService.addTftpEventListener(upgradeTftpEventListener);
        boolean startTransfer = false;
        while (true) {
            // 完成状态
            checkUpgradeCompleteState = upgradeCC8800BCmdThread.isUpgradeCompleteFlag();
            tftpComplete = upgradeTftpEventListener.getComplete() / upgradeTftpEventListener.getFilesize();
            if (logger.isTraceEnabled()) {
                logger.trace("check upgrade state  " + "checkUpgradeCompleteState[" + checkUpgradeCompleteState
                        + "] upgradeCmd[" + upgradeCmd + "] complete[" + nf.format(tftpComplete) + "]");
            }
            if (tftpComplete != 1) {
                logger.info("upgradeCmd [" + upgradeCmd + "] tftp complete " + nf.format(tftpComplete * 100) + "%");
                if (Double.isNaN(tftpComplete)) {
                    tftpComplete = 0;
                }
                upgradeEntity.setUpgradeNote(nf.format(tftpComplete * 100) + "%");
                upgradeJobService.updateUpgradeEntity(upgradeEntity);
            } else {
                logger.info("upgradeCmd [" + upgradeCmd + "] Device mirroring update...");
                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.UPGRADE_NOW);
                upgradeEntity.setUpgradeNote("");
                upgradeJobService.updateUpgradeEntity(upgradeEntity);
            }

            if (checkUpgradeCompleteState) {
                break;
            }

            if (tftpComplete > 0) {
                startTransfer = true;
            }

            if (startTransfer && tftpComplete == 0) {
                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.LOADIMAGE_ERROR);
                upgradeEntity.setUpgradeNote("");
                upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                upgradeJobService.updateUpgradeEntity(upgradeEntity);

                upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
                return upgradeEntity;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.LOADIMAGE_ERROR);
                upgradeEntity.setUpgradeNote("");
                upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                upgradeJobService.updateUpgradeEntity(upgradeEntity);

                upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
                return upgradeEntity;
            }
        }

        boolean checkUpgradeState = upgradeCC8800BCmdThread.isUpgradeState();
        String result = upgradeCC8800BCmdThread.getCmdResult();
        if (checkUpgradeState) {
            // check result
            logger.debug("upgradeCmd [" + upgradeCmd + "] cmd end");
            // 升级命令执行完成
            if (!upgradeCheckService.checkUpgradeVersion(result)) {
                logger.debug("upgradeCmd [" + upgradeCmd + "] device fail");
                logger.trace("upgrade result[" + result + "]");

                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.LOADIMAGE_ERROR);
                upgradeEntity.setUpgradeNote(result);
                upgradeJobService.updateUpgradeEntity(upgradeEntity);
                upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
                return upgradeEntity;
            } else {
                logger.debug("upgradeCmd [" + upgradeCmd + "] success");
            }
        } else {
            logger.debug("upgradeCmd [" + upgradeCmd + "] cmd error");
            upgradeEntity.setUpgradeNote(result);
            upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.LOADIMAGE_ERROR);
            upgradeJobService.updateUpgradeEntity(upgradeEntity);
            upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_FAIL);
            upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
            upgradeRecordService.saveUpgradeRecord(upgradeRecord);
            telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
            return upgradeEntity;
        }

        upgradeEntity.setUpgradeNote(result);
        if (writeConfig < 1) {
            upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.WIRTECONFIG_NOW);
            upgradeJobService.updateUpgradeEntity(upgradeEntity);
            if (!ccmtsTelnetUtil.writeConfig()) {
                upgradeEntity.setUpgradeNote(result);
                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.WIRTECONFIG_ERROR);
                upgradeEntity.setRetry(false);
                upgradeJobService.updateUpgradeEntity(upgradeEntity);
                upgradeRecord.setStatus(UpgradeStatusConstants.WIRTECONFIG_ERROR);
                upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
                return upgradeEntity;
            }
        }

        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.RESET_NOW);
        upgradeJobService.updateUpgradeEntity(upgradeEntity);

        ccmtsTelnetUtil.reset();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("", e);
        }

        boolean resetTest = upgradeCheckService.checkPing(ip);

        if (resetTest) {
            upgradeEntity.setUpgradeNote(result);
            upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.RESET_ERROR);
            upgradeEntity.setRetry(false);
            upgradeJobService.updateUpgradeEntity(upgradeEntity);
            upgradeRecord.setStatus(UpgradeStatusConstants.RESET_ERROR);
            upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
            upgradeRecordService.saveUpgradeRecord(upgradeRecord);
            telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
            return upgradeEntity;
        }

        int count = 30; // 设定超时时间为5分钟
        boolean snmpStatus = false; // snmp不通
        boolean versionStatus = true;// 版本不一致
        while (true) {
            logger.debug("check snmp and version start " + count);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                logger.error("", e);
            }

            if (upgradeCheckService.checkPing(ip)) {
                snmpStatus = upgradeCheckService.checkSnmp(snmpParam);
                if (snmpStatus) {
                    versionStatus = upgradeCheckService.checkVersion(snmpParam, upgradeEntity.getUpgradeVersion());
                }
            }
            if (count < 1) {
                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.RESET_ERROR);
                upgradeEntity.setRetry(false);
                upgradeJobService.updateUpgradeEntity(upgradeEntity);

                upgradeRecord.setStatus(UpgradeStatusConstants.RESET_ERROR);
                upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                logger.debug("check snmp and version count < 1");
                telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
                return upgradeEntity;
            }
            count--;
            if (snmpStatus && !versionStatus) {
                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.UPGRADE_SUCCESS);
                upgradeEntity.setRetry(false);
                upgradeJobService.updateUpgradeEntity(upgradeEntity);

                upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_SUCCESS);
                upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                logger.debug("check snmp and version " + count + "snmpStatus=" + snmpStatus + "versionStatus+"
                        + versionStatus);
                telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
                return upgradeEntity;
            } else if (snmpStatus && versionStatus) {
                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                upgradeJobService.updateUpgradeEntity(upgradeEntity);

                upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                logger.debug("check snmp and version " + count + "snmpStatus=" + snmpStatus + "versionStatus+"
                        + versionStatus);
                telnetUtilFactory.releaseTelnetUtil(ccmtsTelnetUtil);
                return upgradeEntity;
            } else {
                logger.debug("check snmp and version " + count + "snmpStatus=" + snmpStatus + "versionStatus+"
                        + versionStatus);
                continue;
            }
        }
    }

    public Long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }

}
