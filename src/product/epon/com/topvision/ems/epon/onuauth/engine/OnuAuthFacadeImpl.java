/***********************************************************************
 * $Id: OnuAuthFacadeImpl.java,v1.0 2013年10月25日 下午6:06:53 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuAuthModify;
import com.topvision.ems.epon.onuauth.domain.OltAuthenMacInfo;
import com.topvision.ems.epon.onuauth.domain.OltAuthenSnInfo;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.domain.OltOnuBlockAuthen;
import com.topvision.ems.epon.onuauth.domain.OltPonOnuAuthModeTable;
import com.topvision.ems.epon.onuauth.facade.OnuAuthFacade;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2013年10月25日-下午6:06:53
 *
 */
public class OnuAuthFacadeImpl extends EmsFacade implements OnuAuthFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    public Integer setOnuAuthenPolicy(SnmpParam snmpParam, Integer policy) {
        OltAttribute oltAttribute = new OltAttribute();
        oltAttribute.setOnuAuthenticationPolicy(policy);
        oltAttribute = snmpExecutorService.setData(snmpParam, oltAttribute);
        return oltAttribute.getOnuAuthenticationPolicy();
    }

    @Override
    public OltAuthenMacInfo addOnuMacAuthen(SnmpParam snmpParam, OltAuthenMacInfo oltAuthenMacInfo) {
        oltAuthenMacInfo.setOnuAuthenRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, oltAuthenMacInfo);
    }

    @Override
    public OltAuthenMacInfo modifyOnuMacAuthen(SnmpParam snmpParam, OltAuthenMacInfo oltAuthenMacInfo) {
        oltAuthenMacInfo.setOnuAuthenRowStatus(RowStatus.CREATE_AND_WAIT);
        return snmpExecutorService.setData(snmpParam, oltAuthenMacInfo);
    }

    @Override
    public OltAuthenMacInfo deleteOnuMacAuthen(SnmpParam snmpParam, Long onuIndex) {
        OltAuthenMacInfo oltAuthenMacInfo = new OltAuthenMacInfo();
        oltAuthenMacInfo.setOnuIndex(onuIndex);
        oltAuthenMacInfo.setOnuAuthenRowStatus(RowStatus.DESTORY);
        return snmpExecutorService.setData(snmpParam, oltAuthenMacInfo);
    }

    @Override
    public OltAuthenSnInfo deleteOnuSnAuthen(SnmpParam snmpParam, Long onuIndex) {
        OltAuthenSnInfo oltAuthenSnInfo = new OltAuthenSnInfo();
        oltAuthenSnInfo.setTopOnuAuthLogicSnCardIndex(EponIndex.getSlotNo(onuIndex).intValue());
        oltAuthenSnInfo.setTopOnuAuthLogicSnPonIndex(EponIndex.getPonNo(onuIndex).intValue());
        oltAuthenSnInfo.setTopOnuAuthLogicSnOnuIndex(EponIndex.getOnuNo(onuIndex).intValue());
        oltAuthenSnInfo.setTopOnuAuthLogicSnRowStatus(RowStatus.DESTORY);
        return snmpExecutorService.setData(snmpParam, oltAuthenSnInfo);
    }

    @Override
    @Deprecated
    public List<OltAuthentication> getOnuAuthenPreConfigList(Long entityId, Long ponId) {
        // 被废弃的方法
        return null;
    }

    @Override
    public List<OltAuthenMacInfo> getAuthenMacInfos(SnmpParam snmpParam) {
        List<OltAuthenMacInfo> oltAuthenMacInfos = snmpExecutorService.getTable(snmpParam, OltAuthenMacInfo.class);
        return oltAuthenMacInfos;
    }

    @Override
    public List<OltAuthenSnInfo> getAuthenSnInfos(SnmpParam snmpParam) {
        List<OltAuthenSnInfo> oltAuthenSnInfos = snmpExecutorService.getTable(snmpParam, OltAuthenSnInfo.class);
        return oltAuthenSnInfos;
    }

    @Override
    public List<OltOnuBlockAuthen> getBlockAuthens(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltOnuBlockAuthen.class);
    }

    @Override
    public void setOnuAuthMode(SnmpParam snmpParam, OltPonOnuAuthModeTable oltPonOnuAuthModeTable) {
        snmpExecutorService.setData(snmpParam, oltPonOnuAuthModeTable);
    }

    @Override
    public void onuAuthInstead(SnmpParam snmpParam, OltOnuAuthModify oltOnuAuthModify) {
        snmpExecutorService.setData(snmpParam, oltOnuAuthModify);
    }

    @Override
    public OltAuthenSnInfo addOnuSnAuthen(SnmpParam snmpParam, OltAuthenSnInfo oltAuthenSnInfo) {
        oltAuthenSnInfo.setTopOnuAuthLogicSnRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, oltAuthenSnInfo);
    }

    @Override
    public OltAuthenSnInfo modifyOnuSnAuthen(SnmpParam snmpParam, OltAuthenSnInfo oltAuthenSnInfo) {
        oltAuthenSnInfo.setTopOnuAuthLogicSnRowStatus(RowStatus.CREATE_AND_WAIT);
        return snmpExecutorService.setData(snmpParam, oltAuthenSnInfo);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

}
