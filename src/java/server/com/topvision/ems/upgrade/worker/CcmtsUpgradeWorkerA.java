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
import com.topvision.platform.util.StringUtil;

public class CcmtsUpgradeWorkerA extends UpgradeWorker {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private TelnetUtilFactory telnetUtilFactory;
    private OltTelnetUtil oltTelnetUtil;
    private static final Long CMC8800E = 30013l;//CC8800E(强集中)
    private static final Long CMC8800CE = 30014l;//CC8800C-E(强集中)
    private static final Long CMC8800DE = 30015l;//CC8800D-E(强集中)
    private static final Long CMC8800F = 30023l;//CC8800F(强集中)

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
            telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
            logger.error("Get oltTelnetUtil error " + ip, e);
            return upgradeEntity;
        }

        // 获取需要升级的槽位
        Set<Long> slot = new HashSet<Long>();
        try {
            if (oltTelnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(), telnetLogin.getEnablePassword(),telnetLogin.getIsAAA())) {
                String result = oltTelnetUtil.execCmd("show ccmts");
                logger.debug(ip + "--show ccmts ---------" + result);

                String[] showCCmts = result.split("\n");
                for (String showCCmt : showCCmts) {
                    String[] temp = showCCmt.replaceAll(" +", " ").split(" ");
                    if (temp.length > 0 && temp[0].startsWith("C")) {
                        slot.add(new Long(temp[0].split("/")[0].replace("C", "").trim()));// 解析槽位号
                    }
                }
            } else {
                oltTelnetUtil.disconnect();
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
            telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
            logger.error("", e1);
            return upgradeEntity;
        }

        logger.info("Entity {} Slots is {}", upgradeEntity.getEntityId(), slot.toString());
        
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
                upgradeEntity.setRetry(false);
                upgradeEntity.setNote(uploadResult);
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
                if (downLoadResult.contains("% Unknown command.")) {
                    downLoadResult = oltTelnetUtil.execCmd("show file transfering-status");
                }
                count--;
                if (downLoadResult.contains("transferring")) {
                    logger.debug("transferring upgrade file");
                    continue;
                } else if (downLoadResult.contains("transfer successfully")) {
                    logger.debug("transfer upgrade file successfully");
                    break;
                } else {
                    upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.DOWNLOADFILE_ERROR);
                    upgradeEntity.setRetry(false);
                    upgradeEntity.setNote(downLoadResult);
                    upgradeJobService.updateUpgradeEntity(upgradeEntity);

                    upgradeRecord.setStatus(UpgradeStatusConstants.DOWNLOADFILE_ERROR);
                    upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                    upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                    logger.debug("transfer upgrade file error:" + downLoadResult);
                    telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
                    return upgradeEntity;
                }
            }
        } else {
            logger.debug("skip download image file.");
        }

        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.UPGRADE_NOW);
        upgradeJobService.updateUpgradeEntity(upgradeEntity);

        List<Long> transactionId = new ArrayList<Long>();
        for (Long slotId : slot) {
            logger.info("Entity {} Slot {} upgrade command started", upgradeEntity.getEntityId(), slotId);
            
            // gpua板卡下发omci-extend命令
            String upgradeType = "ctc";
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("entityId", upgradeEntity.getEntityId());
            paramMap.put("slotNo", slotId);
            if (upgradeJobService.slotTypeIsGpua(paramMap)) {
                upgradeType = "omci-extend";
            }
            
            // 测试过程发现下发升级命令概率出现%Onu list is empty，因此增加重试次数，最多5次
            int maxSendTimes = 6;
            int sendTimes = 1;
            String cmdString = "";
            if (upgradeFile.contains("CC8800C-10G") || upgradeFile.contains("CC8800A") || upgradeFile.contains("CC8800C")) {
                sendTimes = 1;
                if (upgradeFile.contains("CC8800C-10G")) {
                    cmdString = getCmdString(upgradeType, slotId, "cc8800c10g", upgradeFile);
                } else if (upgradeFile.contains("CC8800A")) {
                    cmdString = getCmdString(upgradeType, slotId, "cc8800a", upgradeFile);
                } else if (upgradeFile.contains("CC8800C")) {
                    cmdString = getCmdString(upgradeType, slotId, "cc8800c", upgradeFile);
                }
                logger.info(cmdString);
                String upgradeInfo = "";
                while (sendTimes < maxSendTimes) {
                    upgradeInfo = oltTelnetUtil.execCmd(cmdString);
                    if (upgradeInfo.contains("Onu list is empty")) {
                        sendTimes ++;
                    } else {
                        transactionId.addAll(parseTransactionIds(upgradeInfo));
                        break;
                    }
                }
            }  else if (upgradeFile.contains("CC8800E")) {
                //标志是否进入其中一个条件
                boolean flag = false;
                //cc8800e
                if (upgradeJobService.hasCmtsType(upgradeEntity.getEntityId(), CMC8800E)) {
                    flag = true;
                    sendTimes = 1;
                    cmdString = getCmdString(upgradeType, slotId, "cc8800e", upgradeFile);
                    logger.info(cmdString);
                    String upgradeEInfo = "";
                    while (sendTimes < maxSendTimes) {
                        upgradeEInfo = oltTelnetUtil.execCmd(cmdString);
                        if (upgradeEInfo.contains("Onu list is empty")) {
                            sendTimes ++;
                        } else {
                            transactionId.addAll(parseTransactionIds(upgradeEInfo));
                            break;
                        }
                    }
                }
                
                //cc8800ce
                if (upgradeJobService.hasCmtsType(upgradeEntity.getEntityId(), CMC8800CE)) {
                    flag = true;
                    sendTimes = 1;
                    cmdString = getCmdString(upgradeType, slotId, "cc8800ce", upgradeFile);
                    logger.info(cmdString);
                    String upgradeCEInfo = "";
                    while (sendTimes < maxSendTimes) {
                        upgradeCEInfo = oltTelnetUtil.execCmd(cmdString);
                        if (upgradeCEInfo.contains("Onu list is empty")) {
                            sendTimes ++;
                        } else {
                            transactionId.addAll(parseTransactionIds(upgradeCEInfo));
                            break;
                        }
                    }
                }
                
                //cc8800de
                if (upgradeJobService.hasCmtsType(upgradeEntity.getEntityId(), CMC8800DE)) {
                    flag = true;
                    sendTimes = 1;
                    cmdString = getCmdString(upgradeType, slotId, "cc8800de", upgradeFile);
                    logger.info(cmdString);
                    String upgradeDEInfo = "";
                    while (sendTimes < maxSendTimes) {
                        upgradeDEInfo = oltTelnetUtil.execCmd(cmdString);
                        if (upgradeDEInfo.contains("Onu list is empty")) {
                            sendTimes ++;
                        } else {
                            transactionId.addAll(parseTransactionIds(upgradeDEInfo));
                            break;
                        }
                    }
                }
                
                //cc8800f
                if (upgradeJobService.hasCmtsType(upgradeEntity.getEntityId(), CMC8800F)) {
                    flag = true;
                    sendTimes = 1;
                    cmdString = getCmdString(upgradeType, slotId, "cc8800f", upgradeFile);
                    logger.info(cmdString);
                    String upgradeFInfo = "";
                    while (sendTimes < maxSendTimes) {
                        upgradeFInfo = oltTelnetUtil.execCmd(cmdString);
                        if (upgradeFInfo.contains("Onu list is empty")) {
                            sendTimes ++;
                        } else {
                            transactionId.addAll(parseTransactionIds(upgradeFInfo));
                            break;
                        }
                    }
                }
                
                //如果cc8800e/ce/de/f一个条件都没进入，则记录错误日志，可能是网管没有采集到该设备下的cc
                if (!flag) {
                    logger.error("Entity {} Slot {} not found cc8800e/ce/de/f", upgradeEntity.getEntityId(), slotId);
                }
            }
            
            logger.info("Entity {} Slot {} upgrade command tried {} times", upgradeEntity.getEntityId(), slotId, sendTimes);
            logger.info("Entity {} Slot {} upgrade command finished", upgradeEntity.getEntityId(), slotId);
        }
        
        Map<Long, String[]> upgradeResultMap = new HashMap<Long, String[]>();
        Pattern pattern = Pattern.compile("^(\\d+)(.*)");
        while (true) {
            try {
                Thread.sleep(60000);
                for (Long aTransactionId1 : transactionId) {
                    if (upgradeResultMap.get(aTransactionId1) == null) {
                        logger.info("Entity {} transactionId {} cycle to show result", upgradeEntity.getEntityId(), aTransactionId1);
                        String upgradeResult = oltTelnetUtil.execCmd("show onu-upgrade-record transaction "
                                + aTransactionId1  + " verbose");
                        logger.debug(ip + "show onu-upgrade-record transaction " + aTransactionId1 + "-------"
                                + upgradeResult);
                        String[] upgradeResultArray = upgradeResult.split("\n");
                        for (String anUpgradeResultArray : upgradeResultArray) {
                            if (anUpgradeResultArray.toString().trim().startsWith("Status")) {
                                String temp[] = anUpgradeResultArray.toString().split(":");
                                if (!"upgrading".equals(temp[1].toString().trim())) {
                                    logger.info("Entity {} transactionId {} show ok", upgradeEntity.getEntityId(), aTransactionId1);
                                    upgradeResultMap.put(aTransactionId1, upgradeResultArray);
                                }
                            }
                        }
                    }
                }
                StringBuilder sb = new StringBuilder();
                List<String> ccUpgradeResultList = new ArrayList<String>();// 2/2/2(cc) success// 保存a型设备升级结果
                if (upgradeResultMap.size() == transactionId.size()) {
                    logger.info("Entity {} the size match ok, upgradeResultMap.size = transactionId.size", upgradeEntity.getEntityId());
                    String upgradeResults = "";
                    for (Long aTransactionId : transactionId) {
                        String upgradeResult = oltTelnetUtil.execCmd("show onu-upgrade-record transaction "
                                + aTransactionId  + " verbose");
                        upgradeResults += upgradeResult;
                        String[] result = upgradeResultMap.get(aTransactionId);
                        for (String aResult : result) {
                            if (pattern.matcher(aResult.toString().trim()).matches()) {// 以数字开头
                                String temp = aResult.toString().replaceAll(" +", " ").trim();// 2/2/2(cc)
                                ccUpgradeResultList.add(temp);
                                sb.append(temp).append("\n");
                            }
                        }
                    }
                    upgradeEntity.setUpgradeNote(upgradeResults);
                    
                    logger.info("Entity {} all slot upgrade ok, now waiting save record", upgradeEntity.getEntityId());
                    for (String ccUpgradeResult : ccUpgradeResultList) {
                        try {
                            String[] index = ccUpgradeResult.split("\\(")[0].toString().split("/");
                            Long ccIndex = (new Long(index[0]) << 27) | (new Long(index[1]) << 23)
                                    | (new Long(index[2]) << 16);
                            Long ccEntityId = upgradeRecordService.getCmcEntityIdByIndexAndEntityId(ccIndex,
                                    upgradeEntity.getEntityId());
                            Entity entity = entityService.getEntity(ccEntityId);
                            String originVersion = upgradeRecordService.getCmcVersionByEntityId(ccEntityId);
                            UpgradeRecord ccUpgradeRecord = new UpgradeRecord();
                            ccUpgradeRecord.setEntityId(ccEntityId);
                            ccUpgradeRecord.setManageIp(ip);
                            ccUpgradeRecord.setEntityName(entity.getName());
                            ccUpgradeRecord.setMac(entity.getMac());
                            ccUpgradeRecord.setTypeName(entity.getTypeName());
                            ccUpgradeRecord.setOriginVersion(originVersion);
                            ccUpgradeRecord.setTypeId(entity.getTypeId());
                            ccUpgradeRecord.setJobName(upgradeRecord.getJobName());
                            ccUpgradeRecord.setStartTime(upgradeRecord.getStartTime());
                            ccUpgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                            ccUpgradeRecord.setUpgradeVersion(upgradeRecord.getUpgradeVersion());
                            ccUpgradeRecord.setUpLinkEntityName(upgradeRecord.getUpLinkEntityName());
                            ccUpgradeRecord.setRetryTimes(upgradeRecord.getRetryTimes());
                            if (ccUpgradeResult.contains("success")) {
                                ccUpgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_SUCCESS);
                            } else {
                                ccUpgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                            }
                            upgradeRecordService.saveUpgradeRecord(ccUpgradeRecord);
                        } catch (Exception e) {
                            // 不存在的ccEntityId不做处理
                        }
                    }

                    // 如果不是所有的板卡都有则记录失败
                    if (slot.size() > transactionId.size()) {
                        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                        upgradeEntity.setRetry(true);
                        
                        upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                        upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                    } else {
                        upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.UPGRADE_SUCCESS);
                        upgradeEntity.setRetry(false);
                        
                        upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_SUCCESS);
                        upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                    }
                    upgradeJobService.updateUpgradeEntity(upgradeEntity);
                    upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                    logger.info("Entity {} save record ok", upgradeEntity.getEntityId());
                    break;
                } else {
                    continue;
                }
            } catch (Exception e) {
                upgradeEntity.setUpgradeStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                upgradeJobService.updateUpgradeEntity(upgradeEntity);

                upgradeRecord.setStatus(UpgradeStatusConstants.UPGRADE_FAIL);
                upgradeRecord.setEndTime(new Timestamp(System.currentTimeMillis()));
                upgradeRecordService.saveUpgradeRecord(upgradeRecord);
                telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
                logger.error("error", e);
                return upgradeEntity;
            } 
        }
        logger.info("Entity {} CcmtsUpgradeWorkerA end", upgradeEntity.getEntityId());
        telnetUtilFactory.releaseTelnetUtil(oltTelnetUtil);
        return upgradeEntity;

    }

    /*private String getImageFileName() {
        int index = imageFile.lastIndexOf("/");
        String re;
        if (index != -1 ) {
            re = imageFile.substring(index + 1);
        } else {
            re = imageFile;
        }
        return re;
    }*/
    
    /**
     * 解析transactionId
     * @param upgradeInfo
     * @return
     */
    private List<Long> parseTransactionIds(String upgradeInfo){
        List<Long> transactionIds = new ArrayList<Long>();
        if (!StringUtil.isEmpty(upgradeInfo)) {
            String[] upgradeInfoArray = upgradeInfo.split("\n");
            for (String anUpgradeInfo : upgradeInfoArray) {
                if(anUpgradeInfo.toString().contains("Start to upgrade onu")){
                    String[] temp = anUpgradeInfo.toString().trim().split(" ");
                    transactionIds.add(new Long(temp[temp.length - 1].replace(".", "").trim()));
                }
            }
        }
        return transactionIds;
    }
    
    private String getCmdString(String upgradeType, Long slotId, String type, String upgradeFile){
        StringBuffer sb = new StringBuffer();
        sb.append("upgrade ccmts")
        .append(" ")
        .append(upgradeType)
        .append(" ")
        .append("slot")
        .append(" ")
        .append(slotId)
        .append(" ")
        .append(type)
        .append(" ")
        .append(upgradeFile)
        .append(" ")
        .append("auto");
        return sb.toString();
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
