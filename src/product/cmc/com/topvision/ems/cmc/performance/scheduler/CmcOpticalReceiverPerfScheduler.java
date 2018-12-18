/***********************************************************************
 * $Id: CmcOpticalReceiverPerfScheduler.java,v1.0 2013-12-16 上午11:49:44 $
 * 
 * @author: dosion
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.scheduler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverAcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverDcPower;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverInputInfo;
import com.topvision.ems.cmc.optical.facade.domain.CmcOpReceiverRfPort;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsSysDorType;
import com.topvision.ems.cmc.performance.domain.CmcOpticalReceiverPerf;
import com.topvision.ems.cmc.performance.domain.CmcOpticalReceiverPerfResult;
import com.topvision.ems.engine.performance.AbstractExecScheduler;
import com.topvision.framework.annotation.Engine;

/**
 * @author dosion
 * @created @2013-12-16-上午11:49:44
 * 
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Engine("cmcOpticalReceiverPerfScheduler")
public class CmcOpticalReceiverPerfScheduler extends AbstractExecScheduler<CmcOpticalReceiverPerf> {
    @Override
    public void exec() {
        if (logger.isDebugEnabled()) {
            logger.debug("CmcOpticalReceiverPerfScheduler  entityId[" + operClass.getCmcId() + "] exec start.");
        }
        try {
            if (operClass.getMonitorId() != null) {
                getCallback()
                        .recordTaskCollectTime(operClass.getMonitorId(), new Timestamp(System.currentTimeMillis()));
            }
        } catch (Exception e) {
            logger.debug("", e);
        }
        try {
            snmpParam = getCallback().getSnmpParamByEntity(snmpParam.getEntityId());
            long cmcId = operClass.getCmcId();
            int cmcIndex = operClass.getCmcIndex().intValue();
            int index = generateNextIndex(cmcIndex);
            long dt = System.currentTimeMillis();
            Timestamp ts = new Timestamp(dt);
            // 取光机类型，如果没有光机，则不做指标数据采集
            TopCcmtsSysDorType topCcmtsSysDorType = new TopCcmtsSysDorType();
            try {
                topCcmtsSysDorType.setCmcIndex(operClass.getCmcIndex());
                topCcmtsSysDorType = snmpExecutorService.getTableLine(snmpParam, topCcmtsSysDorType);
            } catch (Exception e) {
                logger.debug("TopCcmtsSysDorType get error {0}", e);
            }
            if (topCcmtsSysDorType.getTopCcmtsSysDorType() == null
                    || "".equals(topCcmtsSysDorType.getTopCcmtsSysDorType())) {
                //没有光机
                return;
            }

            // INPUT Info
            List<CmcOpReceiverInputInfo> inputInfoList = new ArrayList<CmcOpReceiverInputInfo>();
            int infoIndex = index;
            for (int i = 0; i < 2; i++) {
                CmcOpReceiverInputInfo inputInfo = new CmcOpReceiverInputInfo();
                inputInfo.setInputIndex(infoIndex);
                try {
                    inputInfo = snmpExecutorService.getTableLine(snmpParam, inputInfo);
                } catch (Exception e) {
                    logger.debug("CmcOpReceiverInputInfo get error {0}", e);
                }
                inputInfo.setDt(dt);
                infoIndex = generateNextIndex(infoIndex);
                inputInfoList.add(inputInfo);
            }
            // AC Power
            CmcOpReceiverAcPower acPower = new CmcOpReceiverAcPower();
            acPower = snmpExecutorService.getTableLine(snmpParam, acPower);
            acPower.setDt(dt);
            // DC Power
            List<CmcOpReceiverDcPower> dcPowerList = new ArrayList<CmcOpReceiverDcPower>();
            int dcIndex = index;
            for (int i = 0; i < 3; i++) {
                CmcOpReceiverDcPower power = new CmcOpReceiverDcPower();
                power.setPowerIndex(dcIndex);
                try {
                    power = snmpExecutorService.getTableLine(snmpParam, power);
                } catch (Exception e) {
                    logger.debug("CmcOpReceiverDcPower get error {0}", e);
                }
                power.setDt(dt);
                dcPowerList.add(power);
                dcIndex = generateNextIndex(dcIndex);
            }
            logger.trace("CmcServiceQualityPertResult write result to file.");
            // 获取射频输出电平
            int rfIndex = index;
            List<CmcOpReceiverRfPort> rfPortList = new ArrayList<CmcOpReceiverRfPort>();
            CmcOpReceiverRfPort rfPort = null;
            for (int i = 0; i < 4; i++) {
                rfPort = new CmcOpReceiverRfPort();
                rfPort.setRfPortIndex(rfIndex);
                try {
                    rfPort = snmpExecutorService.getTableLine(snmpParam, rfPort);
                } catch (Exception e) {
                    logger.debug("CmcOpReceiverRfPort get error {0}", e);
                }
                rfPort.setCollectTime(ts);
                rfPortList.add(rfPort);
                rfIndex = generateNextIndex(rfIndex);
            }
            CmcOpticalReceiverPerfResult result = new CmcOpticalReceiverPerfResult(operClass);
            result.setEntityId(cmcId);
            result.setInputInfo(inputInfoList);
            result.setAcPower(acPower);
            result.setDcPowerList(dcPowerList);
            result.setRfPortList(rfPortList);
            // 将结果记入磁盘文件 等待server处理
            addLocalFileData(result);
        } catch (Exception e) {
            logger.debug("", e);
        }
        logger.debug("CmcOpticalReceiverPerfScheduler exec end.");

    }

    /**
     * 按照光机Table的Index的规则，通过给定的index生成下一个index，光机的index使用低位开始的第二位为1开始编号，
     * 例如，第一个index为cmcIndex+2，第二个索引为cmcIndex + 2 +2。
     * 
     * @param index
     *            一个存在的index
     * @return 该index的下一个index
     */
    private Integer generateNextIndex(Integer index) {
        return index + 2;
    }
}
