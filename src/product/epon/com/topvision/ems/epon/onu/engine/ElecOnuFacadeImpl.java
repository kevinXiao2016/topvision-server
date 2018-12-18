/***********************************************************************
 * $Id: OnuFacadeImpl.java,v1.0 2013-10-25 上午11:28:54 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.olt.domain.TopPonPortChgObjects;
import com.topvision.ems.epon.onu.domain.OltOnuComAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuComStatics;
import com.topvision.ems.epon.onu.domain.OltOnuComVlanConfig;
import com.topvision.ems.epon.onu.domain.OltOnuMacMgmt;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.facade.ElecOnuFacade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2013-10-25-上午11:28:54
 *
 */
public class ElecOnuFacadeImpl extends EmsFacade implements ElecOnuFacade {
    private SnmpExecutorService snmpExecutorService;

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public void setOnuComVlan(SnmpParam param, Integer onuComVlan) {
        OltOnuComVlanConfig oltOnuComVlanConfig = new OltOnuComVlanConfig();
        oltOnuComVlanConfig.setOnuComVlan(onuComVlan);
        snmpExecutorService.setData(param, oltOnuComVlanConfig);
    }

    @Override
    public void setOnuIpMaskInfo(SnmpParam param, Long onuIndex, String onuIp, String onuMask, String onuGateway) {
        OltTopOnuCapability capability = new OltTopOnuCapability();
        capability.setOnuIndex(onuIndex);
        capability.setTopOnuMgmtIp(onuIp);
        capability.setTopOnuNetMask(onuMask);
        capability.setTopOnuGateway(onuGateway);
        snmpExecutorService.setData(param, capability);
    }

    @Override
    public void setOnuComAttribute(SnmpParam param, OltOnuComAttribute attribute) {
        snmpExecutorService.setData(param, attribute);
    }

    @Override
    public OltOnuComStatics getOnuComStatisc(SnmpParam param, Long onuComIndex) {
        OltOnuComStatics statics = new OltOnuComStatics();
        statics.setOnuComIndex(onuComIndex);
        return snmpExecutorService.getTableLine(param, statics);
    }

    @Override
    public void cleanOnuComStatisc(SnmpParam param, Long onuComIndex) {
        OltOnuComStatics statics = new OltOnuComStatics();
        statics.setOnuComIndex(onuComIndex);
        // TODO 定义静态变量
        statics.setOnuComStatisClear(1);
        snmpExecutorService.setData(param, statics);
    }

    @Override
    public void switchPonInfo(SnmpParam param, Long srcPonIndex, Long dstPonIndex, Integer action) {
        TopPonPortChgObjects ponPortChgObjects = new TopPonPortChgObjects();
        ponPortChgObjects.setPonSrcIndex(srcPonIndex);
        ponPortChgObjects.setPonDstIndex(dstPonIndex);
        // TODO 定义静态变量
        ponPortChgObjects.setPonPortChgAction(1);
        snmpExecutorService.setData(param, ponPortChgObjects);
    }

    @Override
    public OltOnuComVlanConfig getOnuComVlan(SnmpParam param) {
        // 只存在一条数据,不能使用getTable方式
        return snmpExecutorService.getData(param, OltOnuComVlanConfig.class);
    }

    @Override
    public OltOnuComAttribute getOnuComAttribute(SnmpParam param, Long onuComIndex) {
        OltOnuComAttribute attribute = new OltOnuComAttribute();
        attribute.setOnuComIndex(onuComIndex);
        return snmpExecutorService.getTableLine(param, attribute);
    }

    @Override
    public List<OltOnuComAttribute> getOltOnuComAttributes(SnmpParam param) {
        return snmpExecutorService.getTable(param, OltOnuComAttribute.class);
    }

    @Override
    public OltOnuMacMgmt getOnuMacMgmt(SnmpParam param, Long onuIndex) {
        OltOnuMacMgmt o = new OltOnuMacMgmt();
        o.setOnuIndex(onuIndex);
        return snmpExecutorService.getTableLine(param, o);
    }

    @Override
    public void setOnuMacMgmt(SnmpParam param, OltOnuMacMgmt oltOnuMacMgmt) {
        snmpExecutorService.setData(param, oltOnuMacMgmt);
    }

    @Override
    public void addOnuMacMgmt(SnmpParam param, OltOnuMacMgmt oltOnuMacMgmt) {
        oltOnuMacMgmt.setTopOnuMacRowStatus(RowStatus.CREATE_AND_GO);
        oltOnuMacMgmt.setTopOnuMacList("");
        snmpExecutorService.setData(param, oltOnuMacMgmt);
    }

}
