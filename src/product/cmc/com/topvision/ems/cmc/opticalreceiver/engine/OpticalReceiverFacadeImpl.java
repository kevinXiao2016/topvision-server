/***********************************************************************
 * $Id: OpticalReceiverFacadeImpl.java,v1.0 2016年9月13日 下午3:23:17 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.opticalreceiver.engine;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorABSwitch;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorChannelNum;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDCPower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorDevParams;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwUpg;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorFwdEq;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorLinePower;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRFPort;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRRXOptPow;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsDorRevAtt;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsOpRxInput;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsOpRxOutput;
import com.topvision.ems.cmc.opticalreceiver.domain.TopCcmtsSysDorType;
import com.topvision.ems.cmc.opticalreceiver.facade.OpticalReceiverFacade;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author haojie
 * @created @2016年9月13日-下午3:23:17
 *
 */
@Facade("opticalReceiverFacade")
public class OpticalReceiverFacadeImpl extends EmsFacade implements OpticalReceiverFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    @Override
    public TopCcmtsSysDorType getSysDorType(SnmpParam snmpParam, TopCcmtsSysDorType topCcmtsSysDorType) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsSysDorType);
    }

    @Override
    public TopCcmtsDorABSwitch getABSwitch(SnmpParam snmpParam, TopCcmtsDorABSwitch topCcmtsDorABSwitch) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsDorABSwitch);
    }

    @Override
    public TopCcmtsDorChannelNum getChannelNum(SnmpParam snmpParam, TopCcmtsDorChannelNum topCcmtsDorChannelNum) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsDorChannelNum);
    }

    @Override
    public TopCcmtsDorDCPower getDCPower(SnmpParam snmpParam, TopCcmtsDorDCPower topCcmtsDorDCPower) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsDorDCPower);
    }

    @Override
    public TopCcmtsDorDevParams getDevParams(SnmpParam snmpParam, TopCcmtsDorDevParams topCcmtsDorDevParams) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsDorDevParams);
    }

    @Override
    public TopCcmtsDorFwdAtt getFwdAtt(SnmpParam snmpParam, TopCcmtsDorFwdAtt topCcmtsDorFwdAtt) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsDorFwdAtt);
    }

    @Override
    public TopCcmtsDorFwdEq getFwdEq(SnmpParam snmpParam, TopCcmtsDorFwdEq topCcmtsDorFwdEq) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsDorFwdEq);
    }

    @Override
    public TopCcmtsDorLinePower getLinePower(SnmpParam snmpParam, TopCcmtsDorLinePower topCcmtsDorLinePower) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsDorLinePower);
    }

    @Override
    public TopCcmtsDorRevAtt getRevAtt(SnmpParam snmpParam, TopCcmtsDorRevAtt topCcmtsDorRevAtt) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsDorRevAtt);
    }

    @Override
    public TopCcmtsDorRFPort getRFPort(SnmpParam snmpParam, TopCcmtsDorRFPort topCcmtsDorRFPort) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsDorRFPort);
    }

    @Override
    public TopCcmtsDorRRXOptPow getRRXOptPow(SnmpParam snmpParam, TopCcmtsDorRRXOptPow topCcmtsDorRRXOptPow) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsDorRRXOptPow);
    }

    @Override
    public TopCcmtsOpRxInput getOpRxInput(SnmpParam snmpParam, TopCcmtsOpRxInput topCcmtsOpRxInput) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsOpRxInput);
    }

    @Override
    public TopCcmtsOpRxOutput getOpRxOutput(SnmpParam snmpParam, TopCcmtsOpRxOutput topCcmtsOpRxOutput) {
        return snmpExecutorService.getTableLine(snmpParam, topCcmtsOpRxOutput);
    }

    @Override
    public void modifyOpRxOutput(SnmpParam snmpParam, TopCcmtsOpRxOutput topCcmtsOpRxOutput) {
        snmpExecutorService.setData(snmpParam, topCcmtsOpRxOutput);
    }

    @Override
    public void modifyDevParams(SnmpParam snmpParam, TopCcmtsDorDevParams topCcmtsDorDevParams) {
        snmpExecutorService.setData(snmpParam, topCcmtsDorDevParams);
    }

    @Override
    public void modifyFwdAtt(SnmpParam snmpParam, TopCcmtsDorFwdAtt topCcmtsDorFwdAtt) {
        snmpExecutorService.setData(snmpParam, topCcmtsDorFwdAtt);
    }

    @Override
    public void modifyFwdEq(SnmpParam snmpParam, TopCcmtsDorFwdEq topCcmtsDorFwdEq) {
        snmpExecutorService.setData(snmpParam, topCcmtsDorFwdEq);
    }

    @Override
    public void modifyRevAtt(SnmpParam snmpParam, TopCcmtsDorRevAtt topCcmtsDorRevAtt) {
        snmpExecutorService.setData(snmpParam, topCcmtsDorRevAtt);
    }

    @Override
    public void modifyChannelNum(SnmpParam snmpParam, TopCcmtsDorChannelNum topCcmtsDorChannelNum) {
        snmpExecutorService.setData(snmpParam, topCcmtsDorChannelNum);
    }

    @Override
    public void restorFactory(SnmpParam snmpParam, Long index) {
        snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.21.24.1.5." + index, "1");
    }

    @Override
    public void upgradeOpticalReceiver(SnmpParam snmpParam, TopCcmtsDorFwUpg topCcmtsDorFirmWare) {
        snmpExecutorService.setData(snmpParam, topCcmtsDorFirmWare);
    }

    @Override
    public Integer getUpdateProgress(SnmpParam snmpParam, Long topCcmtsDorFirmWareIndex) {
        return Integer.parseInt(snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.21.26.1.8."
                + topCcmtsDorFirmWareIndex));
    }

    @Override
    public void setRealTimeSnmpDataStatus(SnmpParam snmpParam, String state) {
        try {
            snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.18.2.1.0", state);
        } catch (Exception e) {
            logger.debug("set opticalReceiverRealTimeSnmpDataStatus error", e);
        }
    }
}
