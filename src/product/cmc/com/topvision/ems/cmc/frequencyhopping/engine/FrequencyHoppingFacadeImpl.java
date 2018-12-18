/***********************************************************************
 * $Id: CcmtsSpectrumGpFacadeImpl.java,v1.0 2013-8-2 上午10:41:13 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.engine;

import java.util.List;

import javax.annotation.Resource;

import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGp;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpChnl;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpFreq;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpGlobal;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpHopHis;
import com.topvision.ems.cmc.frequencyhopping.facade.FrequencyHoppingFacade;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author haojie
 * @created @2013-8-2-上午10:41:13
 * 
 */
@Facade("frequencyHoppingFacade")
public class FrequencyHoppingFacadeImpl extends EmsFacade implements FrequencyHoppingFacade {
    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public void setGpGlobalToDevice(SnmpParam snmpParam, CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal) {
        snmpExecutorService.setData(snmpParam, ccmtsSpectrumGpGlobal);
    }

    @Override
    public CcmtsSpectrumGpGlobal getGpGlobalFromDevice(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, CcmtsSpectrumGpGlobal.class);
    }

    @Override
    public void destoryGroupFromDevice(SnmpParam snmpParam, CcmtsSpectrumGp ccmtsSpectrumGp) {
        ccmtsSpectrumGp.setGroupRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, ccmtsSpectrumGp);
    }

    @Override
    public List<CcmtsSpectrumGpChnl> getChnlGroupFromDevice(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CcmtsSpectrumGpChnl.class);
    }

    @Override
    public void setChnlGroupToDevice(SnmpParam snmpParam, CcmtsSpectrumGpChnl chnlGroup) {
        snmpExecutorService.setData(snmpParam, chnlGroup);
    }

    @Override
    public List<CcmtsSpectrumGp> getGroupFromDevice(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CcmtsSpectrumGp.class);
    }

    @Override
    public List<CcmtsSpectrumGpFreq> getGroupFreqFromDevice(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CcmtsSpectrumGpFreq.class);
    }

    @Override
    public void clearDeviceGroupHopHis(SnmpParam snmpParam, CcmtsSpectrumGpChnl chnlGroup) {
        snmpExecutorService.setData(snmpParam, chnlGroup);
    }

    @Override
    public List<CcmtsSpectrumGpHopHis> getGroupHopHisFromDevice(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, CcmtsSpectrumGpHopHis.class);
    }

    @Override
    public void createGroupToDevice(SnmpParam snmpParam, CcmtsSpectrumGp ccmtsSpectrumGp) {
        ccmtsSpectrumGp.setGroupRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, ccmtsSpectrumGp);
    }

    @Override
    public void createGroupFreqToDevice(SnmpParam snmpParam, CcmtsSpectrumGpFreq ccmtsSpectrumGpFreq) {
        ccmtsSpectrumGpFreq.setFreqRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, ccmtsSpectrumGpFreq);
    }

}
