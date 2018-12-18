/***********************************************************************
 * $Id: CmtsCommandSendWorkerB.java,v1.0 2016年3月23日 下午3:04:06 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.network.dao.CommandSendDao;
import com.topvision.ems.network.domain.SendConfigEntity;
import com.topvision.ems.network.domain.SendConfigResult;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.ems.upgrade.telnet.CcmtsTelnetUtil;
import com.topvision.ems.upgrade.telnet.TelnetUtilFactory;
import com.topvision.framework.common.IpUtils;
import com.topvision.framework.ping.PingExecutorService;

/**
 * @author loyal
 * @created @2016年3月23日-下午3:04:06
 * 
 */
public class CmtsCommandSendWorkerB extends CommandSendWorker {
    private final Logger logger = LoggerFactory.getLogger(CmtsCommandSendWorkerB.class);

    public void process() {
        logger.info("CmtsCommandSendWorkerB process sendConfigEntityObject[" + sendConfigEntityObject + "]");
        entityTypeService = (EntityTypeService) beanFactory.getBean("entityTypeService");
        pingExecutorService = (PingExecutorService) beanFactory.getBean("pingExecutorService");
        telnetUtilFactory = (TelnetUtilFactory) beanFactory.getBean("telnetUtilFactory");
        commandSendDao = (CommandSendDao) beanFactory.getBean("commandSendDao");
        SendConfigEntity sendConfigEntity = sendConfigEntityObject.getSendConfigEntitys().get(0);
        if (sendConfigEntity == null) {
            return;
        }
        String ip = new IpUtils(sendConfigEntity.getIp()).toString();
        boolean pingCheck = checkPingReachable(ip);
        if (!pingCheck) {
            savaCommandSendResult(sendConfigEntity.getEntityId(), SendConfigResult.TELNETERROR, "IP is not reachable");
            logger.info("IP is not reachable 2 sendConfigEntity[" + ip + "]");
            telnetUtilFactory.releaseTelnetUtil(telnetUtil);
            return;
        }
        try {
            telnetUtil = (CcmtsTelnetUtil) telnetUtilFactory.getCcmtsTelnetUtil(ip);
            telnetUtil.connect(ip);
            if (telnetUtil.login(telnetLogin.getUserName(), telnetLogin.getPassword(), telnetLogin.getEnablePassword(),
                    telnetLogin.getIsAAA())) {
                telnetUtil.execCmd1("config terminal");
            } else {
                savaCommandSendResult(sendConfigEntity.getEntityId(), SendConfigResult.TELNETERROR, telnetUtil.getRe()
                        .toString());
                logger.info("Telnet error sendConfigEntity[" + sendConfigEntity + "]");
                telnetUtilFactory.releaseTelnetUtil(telnetUtil);
                return;
            }
        } catch (Exception e) {
            savaCommandSendResult(sendConfigEntity.getEntityId(), SendConfigResult.TELNETERROR, "IP is not reachable");
            logger.info("IP is not reachable  sendConfigEntity[" + sendConfigEntity + "]");
            telnetUtilFactory.releaseTelnetUtil(telnetUtil);
            return;
        }
        boolean isCmdError = false;
        boolean isWriteFileError = true;
        List<String> configs = configsMap.get(sendConfigEntity.getEntityId());
        try {
            for (String config : configs) {
                if (!telnetUtil.execCmd1(config)) {
                    isCmdError = true;
                    continue;
                }
                try {
                    Thread.sleep(sendCommandInterval);
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
            }
            if (!isCmdError) {
                isWriteFileError = !telnetUtil.writeConfig();
            }
        } finally {
            telnetUtilFactory.releaseTelnetUtil(telnetUtil);
        }
        Integer resultCode;
        if (isCmdError) {
            resultCode = SendConfigResult.CONFIGERROR;
        } else if (!isCmdError && isWriteFileError) {
            resultCode = SendConfigResult.WRITEFILEERROR;
        } else {
            resultCode = SendConfigResult.SUCCESS;
        }
        savaCommandSendResult(sendConfigEntity.getEntityId(), resultCode, telnetUtil.getRe().toString());
        logger.info("finish sendConfigEntity[" + sendConfigEntity + "]");
    }

}
