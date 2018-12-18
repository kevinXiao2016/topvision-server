/***********************************************************************
 * $Id: OnuUpgradeWorker.java,v1.0 2014年9月23日 下午7:11:18 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.worker;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.network.service.TelnetLoginService;
import com.topvision.ems.upgrade.domain.UpgradeEntity;
import com.topvision.ems.upgrade.domain.UpgradeRecord;
import com.topvision.ems.upgrade.service.UpgradeJobService;
import com.topvision.ems.upgrade.service.UpgradeRecordService;
import com.topvision.ems.upgrade.telnet.OltTelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.ems.upgrade.utils.UpgradeStatusConstants;
import com.topvision.framework.common.IpUtils;

/**
 * @author loyal
 * @created @2014年9月23日-下午7:11:18
 * 
 */
public class OnuUpgradeWorker extends UpgradeWorker {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private TelnetUtilFactory telnetUtilFactory;
    private OltTelnetUtil oltTelnetUtil;

    @Override
    public UpgradeEntity call() {
        UpgradeEntity upgradeEntity = new UpgradeEntity();
        UpgradeRecord upgradeRecord = new UpgradeRecord();
        upgradeEntity = this.getUpgradeEntity();
        upgradeRecord = this.getUpgradeRecord();
        String ip = upgradeEntity.getIp();

        logger.debug("start upgrade-----" + ip);
        TelnetLoginService telnetLoginService = (TelnetLoginService) beanFactory.getBean("telnetLoginService");
        UpgradeRecordService upgradeRecordService = (UpgradeRecordService) beanFactory.getBean("upgradeRecordService");
        UpgradeJobService upgradeJobService = (UpgradeJobService) beanFactory.getBean("upgradeJobService");
        EntityService entityService = (EntityService) beanFactory.getBean("entityService");
        TelnetLogin telnetLogin = telnetLoginService.getTelnetLoginConfigByIp(new IpUtils(ip).longValue());
        if (telnetLogin == null) {
            telnetLogin = telnetLoginService.getGlobalTelnetLogin();
        }
        telnetUtilFactory = (TelnetUtilFactory) beanFactory.getBean("telnetUtilFactory");

        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.TELNET_NOW);
        upgradeJobService.updateUpgradeEntity(upgradeEntity);
        try {
            oltTelnetUtil = (OltTelnetUtil) telnetUtilFactory.getOltTelnetUtil(ip);
            oltTelnetUtil.connect(ip);
        } catch (Exception e) {
            upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.TELNET_ERROR);
            upgradeEntity.setRetry(false);
            upgradeJobService.updateUpgradeEntity(upgradeEntity);

            upgradeRecord.setStatus(UpgradeStatusConstants.TELNET_ERROR);
            upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
            upgradeRecordService.saveUpgradeRecord(upgradeRecord);
            logger.error("Get oltTelnetUtil error " + ip, e);
            telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
            return upgradeEntity;
        }

        // 获取需要升级的槽位
        Pattern pattern = Pattern.compile("^(\\d+)(.*)");
        Set<Long> slot = new HashSet<Long>();
        try {
            if (oltTelnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(), telnetLogin.getEnablePassword(),telnetLogin.getIsAAA())) {
                String result = oltTelnetUtil.execCmd("show onu summary");
                logger.debug(ip + "--show onu summary ---------" + result);

                String[] showOnu = result.split("\n");
                for (String aShowOnu : showOnu) {
                    String[] temp = aShowOnu.replaceAll(" +", " ").split(" ");
                    if (temp.length > 2 && pattern.matcher(temp[1].toString().trim()).matches()) {// 以数字开头
                        slot.add(new Long(temp[1].split("/")[0].trim()));// 解析槽位号
                    }
                }
                
                String gponResult = oltTelnetUtil.execCmd("show onu gpon summary");
                logger.debug(ip + "--show onu gpon summary ---------" + result);
                String[] showGponOnu = gponResult.split(System.getProperty("line.separator"));
                for (String aShowGponOnu : showGponOnu) {
                    String[] temp = aShowGponOnu.replaceAll(" +", " ").split(" ");
                    if (temp.length > 2 && pattern.matcher(temp[0].toString().trim()).matches()) {// 以数字开头
                        slot.add(new Long(temp[0].trim()));// 解析槽位号
                    }
                }
            } else {
                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.TELNET_ERROR);
                upgradeEntity.setRetry(false);
                upgradeJobService.updateUpgradeEntity(upgradeEntity);

                upgradeRecord.setStatus(UpgradeStatusConstants.TELNET_ERROR);
                upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                logger.debug("login " + ip + " error");
                telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
                return upgradeEntity;
            }
        } catch (NumberFormatException e1) {
            upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.TELNET_ERROR);
            upgradeEntity.setRetry(false);
            upgradeJobService.updateUpgradeEntity(upgradeEntity);

            upgradeRecord.setStatus(UpgradeStatusConstants.TELNET_ERROR);
            upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
            upgradeRecordService.saveUpgradeRecord(upgradeRecord);
            logger.error("", e1);
            telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
            return upgradeEntity;
        }

        logger.info("Entity {} Slots is {}", ip, slot.toString());
        
        // 清空升级记录
        oltTelnetUtil.execCmd("end");
        oltTelnetUtil.execCmd("no onu-upgrade-record all");

        String upgradeFile = imageFile.substring(imageFile.lastIndexOf("/") + 1, imageFile.length());
        String lsResult = oltTelnetUtil.execCmd("ls onuapp");
        logger.debug(ip + "--ls onuapp----"+upgradeFile+"---" + lsResult);
        boolean isExistImageFile = lsResult.contains(upgradeFile);
        if (!isExistImageFile) {
            upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.DOWNLOADFILE_NOW);
            upgradeJobService.updateUpgradeEntity(upgradeEntity);

            String uploadResult = oltTelnetUtil.execCmd("download onu " + ftpIp + " " + ftpUserName + " " + ftpPassword
                    + " " + imageFile);
            logger.debug(ip + "--upload onu file-------" + uploadResult);
            if (!uploadResult.contains("Start")) {
                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.DOWNLOADFILE_ERROR);
                upgradeEntity.setRetry(true);
                upgradeJobService.updateUpgradeEntity(upgradeEntity);

                upgradeRecord.setStatus(UpgradeStatusConstants.DOWNLOADFILE_ERROR);
                upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                logger.debug("download upgrade file error---" + ip);
                telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
                return upgradeEntity;
            }

            int count = 15; // 设定超时时间为15分钟
            while (true) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
                if (count < 1) {
                    break;
                }
                String downLoadResult = oltTelnetUtil.execCmd("show file-transfer-status");
                String downLoadResultNew = oltTelnetUtil.execCmd("show file transfering-status");
                count--;
                if (downLoadResult.contains("transferring") || downLoadResultNew.contains("transferring")) {
                    logger.debug("transferring upgrade file");
                    continue;
                } else if (downLoadResult.contains("transfer successfully")
                        || downLoadResultNew.contains("transfer successfully")) {
                    logger.debug("transfer upgrade file successfully");
                    break;
                } else {
                    upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.DOWNLOADFILE_ERROR);
                    upgradeEntity.setRetry(true);
                    upgradeJobService.updateUpgradeEntity(upgradeEntity);

                    upgradeRecord.setStatus(UpgradeStatusConstants.DOWNLOADFILE_ERROR);
                    upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                    upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                    logger.debug("transfer upgrade file error");
                    telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
                    return upgradeEntity;
                }
            }
        } else {
            logger.debug("skip download image file.");
        }

        List<Long> transactionIds = new ArrayList<Long>();
        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.UPGRADE_NOW);
        upgradeJobService.updateUpgradeEntity(upgradeEntity);

        for (Long slotId : slot) {
            logger.info("Entity {} Slot {} upgrade command started", ip, slotId);
            
            String upgradeType = "ctc";
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("entityId", upgradeEntity.getEntityId());
            paramMap.put("slotNo", slotId);
            if (upgradeJobService.slotTypeIsGpua(paramMap)) {
                upgradeType = "omci-baseline";
            }
            
            // 测试过程发现下发升级命令概率出现%Onu list is empty，因此增加重试次数，最多5次
            int maxSendTimes = 6;
            int sendTimes = 1;
            String cmdString = "upgrade onu "+ upgradeType + " slot " + slotId + " ta-" + subType + " any "
                    + upgradeFile + " auto";
            logger.info(cmdString);
            String upgradeInfo = "";
            while (sendTimes < maxSendTimes) {
                upgradeInfo = oltTelnetUtil.execCmd(cmdString);
                if (upgradeInfo.contains("Onu list is empty")) {
                    sendTimes ++;
                } else {
                    String[] upgradeAInfoArray = upgradeInfo.split("\n");
                    for (String anUpgradeAInfoArray : upgradeAInfoArray) {
                        if (anUpgradeAInfoArray.toString().contains("Start to upgrade onu")) {
                            String[] temp = anUpgradeAInfoArray.toString().trim().split(" ");
                            transactionIds.add(new Long(temp[temp.length - 1].replace(".", "").trim()));
                        }
                    }
                    break;
                }
            }
            
            logger.info("Entity {} Slot {} upgrade command tried {} times", ip, slotId, sendTimes);
            logger.info("Entity {} Slot {} upgrade command finshed", ip, slotId);
        }

        Map<Long, String[]> upgradeResultMap = new HashMap<Long, String[]>();
        while (true) {
            try {
                Thread.sleep(60000);
                String upgradeResults = "";
                for (Long transactionId : transactionIds) {
                    String result = oltTelnetUtil.execCmd("show onu-upgrade-record transaction "
                            + transactionId + " verbose");
                    logger.debug(ip + "show onu-upgrade-record transaction " + transactionId + " verbose-------"
                            + result);
                    String[] upgradeResultArray = result.split("\n");
                    for (String anUpgradeResultArray : upgradeResultArray) {
                        if (anUpgradeResultArray.toString().trim().startsWith("Status")) {
                            String temp[] = anUpgradeResultArray.toString().split(":");
                            if (!"upgrading".equals(temp[1].toString().trim())) {
                                upgradeResultMap.put(transactionId, upgradeResultArray);
                            }
                        }
                    }
                    upgradeResults += result;
                }

                StringBuilder sb = new StringBuilder();
                List<String> onuUpgradeResultList = new ArrayList<String>();// 2/2/2(cc) success
                if (upgradeResultMap.size() == transactionIds.size()) {
                    logger.info("Entity {} all slot upgrade ok, now saving record", ip);
                    for (Long transactionId : transactionIds) {
                        String[] result = upgradeResultMap.get(transactionId);
                        for (String aResult : result) {
                            if (pattern.matcher(aResult.toString().trim()).matches()) {// 以数字开头
                                String temp = aResult.toString().replaceAll(" +", " ").trim();// 2/2/2(cc)
                                onuUpgradeResultList.add(temp);
                                sb.append(temp).append("\n");
                            }
                        }
                    }
                    upgradeEntity.setUpgradeNote(upgradeResults);
                    for (String anOnuUpgradeResultList : onuUpgradeResultList) {
                        // 3/2:1 success
                        try {
                            String[] index = anOnuUpgradeResultList.split("\\(")[0].toString().split("/");
                            String[] temp = index[1].split(":");
                            Long onuIndex = getOnuIndex(index[0], temp[0], temp[1].trim().split(" ")[0]);
                            Long onuEntityId = upgradeRecordService.getOnuEntityIdByIndexAndEntityId(onuIndex,
                                    upgradeEntity.getEntityId());
                            Entity entity = entityService.getEntity(onuEntityId);
                            String originVersion = upgradeRecordService.getOnuVersionByEntityId(onuEntityId);
                            UpgradeRecord onuUpgradeRecord = new UpgradeRecord();
                            onuUpgradeRecord.setEntityId(onuEntityId);
                            onuUpgradeRecord.setManageIp(ip);
                            onuUpgradeRecord.setEntityName(entity.getName());
                            onuUpgradeRecord.setMac(entity.getMac());
                            onuUpgradeRecord.setTypeName(entity.getTypeName());
                            onuUpgradeRecord.setOriginVersion(originVersion);
                            onuUpgradeRecord.setTypeId(entity.getTypeId());
                            onuUpgradeRecord.setJobName(upgradeRecord.getJobName());
                            onuUpgradeRecord.setStartTime(upgradeRecord.getStartTime());
                            onuUpgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                            onuUpgradeRecord.setUpgradeVersion(upgradeRecord.getUpgradeVersion());
                            onuUpgradeRecord.setUpLinkEntityName(upgradeRecord.getUpLinkEntityName());
                            onuUpgradeRecord.setRetryTimes(upgradeRecord.getRetryTimes());
                            if (anOnuUpgradeResultList.contains("success")) {
                                onuUpgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_SUCCESS);
                            } else {
                                onuUpgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                            }
                            upgradeRecordService.saveUpgradeRecord(onuUpgradeRecord);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }

                    upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.UPGRADE_SUCCESS);
                    upgradeEntity.setRetry(false);
                    upgradeJobService.updateUpgradeEntity(upgradeEntity);

                    upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_SUCCESS);
                    upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                    upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                    break;
                } else {
                    continue;
                }
            } catch (InterruptedException e) {
                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                upgradeJobService.updateUpgradeEntity(upgradeEntity);

                upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
                logger.debug("error");
                return upgradeEntity;
            }
        }
        logger.info("Entity {} OnuUpgradeWorker end", ip);
        telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
        return upgradeEntity;
    }

    private Long getOnuIndex(String... id) {
        if (id == null || id.length == 0) {
            return null;
        }
        Long index = 0L;
        for (int i = 0; i < id.length; i++) {
            index += (Long.parseLong(id[i]) << ((4 - i) * 8));
        }
        return index.longValue() & 0xFFFFFF0000L;
    }

    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }
}