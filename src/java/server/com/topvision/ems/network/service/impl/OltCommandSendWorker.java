/***********************************************************************
 * $Id: CmtsCommandSendWorkerA.java,v1.0 2016年3月23日 下午3:03:56 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.network.dao.CommandSendDao;
import com.topvision.ems.network.domain.SendConfigEntity;
import com.topvision.ems.network.domain.SendConfigResult;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.telnet.OltTelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.ping.PingExecutorService;
import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.utils.EponIndex;

/**
 * @author loyal
 * @created @2016年3月23日-下午3:03:56
 * 
 */
public class OltCommandSendWorker extends CommandSendWorker {
    private String loginCommadResult;

    public void process() {
        logger.info("OltCommandSendWorker process sendConfigEntityObject[" + sendConfigEntityObject + "]");
        entityTypeService = (EntityTypeService) beanFactory.getBean("entityTypeService");
        pingExecutorService = (PingExecutorService) beanFactory.getBean("pingExecutorService");
        telnetUtilFactory = (TelnetUtilFactory) beanFactory.getBean("telnetUtilFactory");
        commandSendDao = (CommandSendDao) beanFactory.getBean("commandSendDao");
        List<SendConfigEntity> sendConfigEntitys = sendConfigEntityObject.getSendConfigEntitys();
        String ip = new IpUtils(sendConfigEntityObject.getIp()).toString();
        boolean pingCheck = checkPingReachable(ip);
        if (!pingCheck) {
            logger.info("IP is not reachable 2 sendConfigEntity[" + ip + "]");
            for (SendConfigEntity sendConfigEntity : sendConfigEntitys) {
                savaCommandSendResult(sendConfigEntity.getEntityId(), SendConfigResult.TELNETERROR,
                        "IP is not reachable");
            }
            return;
        }
        try {
            telnetUtil = (OltTelnetUtil) telnetUtilFactory.getOltTelnetUtil(ip);
            telnetUtil.connect(ip);
            if (!telnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(),
                    telnetLogin.getEnablePassword(), telnetLogin.getIsAAA())) {
                for (SendConfigEntity sendConfigEntity : sendConfigEntitys) {
                    savaCommandSendResult(sendConfigEntity.getEntityId(), SendConfigResult.TELNETERROR, telnetUtil
                            .getRe().toString());
                }
                logger.info("Telnet error sendConfigEntity[" + ip + "]");
                telnetUtilFactory.releaseTelnetUtil(telnetUtil);
                return;
            }
        } catch (Exception e) {
            logger.info("IP is not reachable  sendConfigEntity[" + ip + "]");
            for (SendConfigEntity sendConfigEntity : sendConfigEntitys) {
                savaCommandSendResult(sendConfigEntity.getEntityId(), SendConfigResult.TELNETERROR,
                        "IP is not reachable");
            }
            telnetUtilFactory.releaseTelnetUtil(telnetUtil);
            return;
        }
        logger.info("Start OltCommandSendWorker sendConfigEntity[" + ip + "]");
        loginCommadResult = telnetUtil.getRe();
        for (SendConfigEntity sendConfigEntity : sendConfigEntitys) {
            telnetUtil.execCmd1("end");
            telnetUtil.setRe(loginCommadResult);
            List<String> configs = new ArrayList<String>();
            configs = configsMap.get(sendConfigEntity.getEntityId());
            if (entityTypeService.isCcmtsWithoutAgent(sendConfigEntity.getTypeId())) {
                String channelName = getChannelNameMarkFromIndex(sendConfigEntity.getCmcIndex());
                telnetUtil.execCmd1("config terminal");
                telnetUtil.execCmd1("interface ccmts " + channelName);
                // configs = cmtsAConfigs;
            } else if (entityTypeService.isOnu(sendConfigEntity.getTypeId())) {
                String onuName = getOnuNameMarkFromIndex(sendConfigEntity.getOnuIndex());
                telnetUtil.execCmd1("config terminal");
                telnetUtil.execCmd1("interface onu " + onuName);
                // configs = onuConfigs;

            } else {
                telnetUtil.execCmd1("config terminal");
                // configs = oltConfigs;
            }
            boolean isCmdError = false;
            for (String config : configs) {
                if (!telnetUtil.execCmd1(config)) {
                    isCmdError = true;
                    continue;
                }
                try {
                    Thread.sleep(sendCommandInterval);
                } catch (InterruptedException e) {
                }
            }

            Integer resultCode;
            if (isCmdError) {
                resultCode = SendConfigResult.CONFIGERROR;
            } else {
                resultCode = SendConfigResult.WAITWRITECONFIG;
            }
            sendConfigEntity.setState(resultCode);
            savaCommandSendResult(sendConfigEntity.getEntityId(), resultCode, telnetUtil.getRe().toString());
            logger.info("finish sendConfigEntity[" + sendConfigEntity + "]");
        }

        boolean isWriteFileError = true;
        try {
            isWriteFileError = !telnetUtil.writeConfig();
            // 更新所有设备下发状态
            if (isWriteFileError) {
                commandSendDao.updateCommandSendStatue(sendConfigEntitys, SendConfigResult.WRITEFILEERROR);
            } else {
                commandSendDao.updateCommandSendStatue(sendConfigEntitys, SendConfigResult.SUCCESS);
            }
        } finally {
            telnetUtilFactory.releaseTelnetUtil(telnetUtil);
        }
    }

    /**
     * 组装channelName
     * 
     * @param channelIndex
     *            Long
     * @return String
     */
    private String getChannelNameMarkFromIndex(Long channelIndex) {
        Long slotNo = CmcIndexUtils.getSlotNo(channelIndex);
        Long ponNo = CmcIndexUtils.getPonNo(channelIndex);
        Long cmcNo = CmcIndexUtils.getCmcId(channelIndex);
        StringBuilder sb = new StringBuilder();
        sb.append(slotNo).append("/").append(ponNo).append("/").append(cmcNo);
        return sb.toString();
    }

    /**
     * 组装channelName
     * 
     * @param onuIndex
     *            Long
     * @return String
     */
    private String getOnuNameMarkFromIndex(Long onuIndex) {
        Long slotNo = EponIndex.getSlotNo(onuIndex);
        Long ponNo = EponIndex.getPonNo(onuIndex);
        Long onuNo = EponIndex.getOnuNo(onuIndex);
        StringBuilder sb = new StringBuilder();
        sb.append(slotNo).append("/").append(ponNo).append(":").append(onuNo);
        return sb.toString();
    }

}
