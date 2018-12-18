/***********************************************************************
 * $Id: OltIgmpConfigFacadeImpl.java,v1.0 2016-6-6 下午4:38:26 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.engine;

import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCascadePort;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcParam;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcProfileGroupRela;
import com.topvision.ems.epon.igmpconfig.domain.IgmpCtcRecord;
import com.topvision.ems.epon.igmpconfig.domain.IgmpGlobalGroup;
import com.topvision.ems.epon.igmpconfig.domain.IgmpGlobalParam;
import com.topvision.ems.epon.igmpconfig.domain.IgmpOnuConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpSnpStaticFwd;
import com.topvision.ems.epon.igmpconfig.domain.IgmpSnpUplinkPort;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniBindCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniVlanTrans;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanGroup;
import com.topvision.ems.epon.igmpconfig.domain.IgmpVlanInfo;
import com.topvision.ems.epon.igmpconfig.facade.OltIgmpConfigFacade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author flack
 * @created @2016-6-6-下午4:38:26
 *
 */
public class OltIgmpConfigFacadeImpl extends EmsFacade implements OltIgmpConfigFacade {
    private SnmpExecutorService snmpExecutorService;

    public static final String MODE_OID = "1.3.6.1.4.1.32285.11.2.3.6.1.1.2.0";
    public static final String CDRREPORT_OID = "1.3.6.1.4.1.32285.11.2.3.6.1.5.1.4.0";
    public static final int CDRREPORT_FLAG = 1;

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public IgmpGlobalParam getGloablParam(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, IgmpGlobalParam.class);
    }

    @Override
    public void modifyIgmpMode(SnmpParam snmpParam, Integer igmpMode) {
        snmpExecutorService.set(snmpParam, MODE_OID, String.valueOf(igmpMode));
    }

    @Override
    public void modifyGlobalParam(SnmpParam snmpParam, IgmpGlobalParam globalParam) {
        snmpExecutorService.setData(snmpParam, globalParam);
    }

    @Override
    public void createCascadePort(SnmpParam snmpParam, IgmpCascadePort cascadePort) {
        cascadePort.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, cascadePort);
    }

    @Override
    public List<IgmpCascadePort> getCascadePortList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpCascadePort.class);
    }

    @Override
    public void destoryCascadePort(SnmpParam snmpParam, IgmpCascadePort cascadePort) {
        cascadePort.setRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, cascadePort);
    }

    @Override
    public IgmpSnpUplinkPort getSnpUplinkPort(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, IgmpSnpUplinkPort.class);
    }

    @Override
    public void modifySnpUplinkPort(SnmpParam snmpParam, IgmpSnpUplinkPort uplinkPort) {
        try {
            snmpExecutorService.setData(snmpParam, uplinkPort);
        } catch (SnmpException e) {
            logger.info("modifySnpUplinkPort {}", e);
            /*
             * 配置失败，网管侧进行重试一次
             */
            snmpExecutorService.setData(snmpParam, uplinkPort);
        }
    }

    @Override
    public void createSnpStaticFwd(SnmpParam snmpParam, IgmpSnpStaticFwd staticFwd) {
        staticFwd.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, staticFwd);
    }

    @Override
    public List<IgmpSnpStaticFwd> getSnpStaticFwdList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpSnpStaticFwd.class);
    }

    @Override
    public void destorySnpStaticFwd(SnmpParam snmpParam, IgmpSnpStaticFwd staticFwd) {
        staticFwd.setRowStatus(RowStatus.DESTORY);
        try {
            snmpExecutorService.setData(snmpParam, staticFwd);
        } catch (SnmpException e) {
            logger.info("destorySnpStaticFwd {}", e);
            /*
             * 配置失败，网管侧进行重试一次
             */
            snmpExecutorService.setData(snmpParam, staticFwd);
        }
    }

    @Override
    public void createVlanInfo(SnmpParam snmpParam, IgmpVlanInfo vlanInfo) {
        vlanInfo.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, vlanInfo);
    }

    @Override
    public List<IgmpVlanInfo> getVlanInfoList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpVlanInfo.class);
    }

    @Override
    public void modifyVlanInfo(SnmpParam snmpParam, IgmpVlanInfo vlanInfo) {
        try {
            snmpExecutorService.setData(snmpParam, vlanInfo);
        } catch (SnmpException e) {
            logger.info("modifyVlanInfo {}", e);
            /*
             * 配置失败，网管侧进行重试一次
             */
            snmpExecutorService.setData(snmpParam, vlanInfo);
        }
    }

    @Override
    public void destoryVlanInfo(SnmpParam snmpParam, Integer vlanId) {
        IgmpVlanInfo vlanInfo = new IgmpVlanInfo();
        vlanInfo.setVlanId(vlanId);
        vlanInfo.setRowStatus(RowStatus.DESTORY);
        try {
            snmpExecutorService.setData(snmpParam, vlanInfo);
        } catch (SnmpException e) {
            logger.info("destoryVlanInfo {}", e);
            /*
             * 删除失败，网管侧进行重试一次
             */
            snmpExecutorService.setData(snmpParam, vlanInfo);
        }
    }

    @Override
    public void createVlanGroup(SnmpParam snmpParam, IgmpVlanGroup groupInfo) {
        groupInfo.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, groupInfo);
    }

    @Override
    public List<IgmpVlanGroup> getVlanGroupList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpVlanGroup.class);
    }

    @Override
    public void modifyVlanGroup(SnmpParam snmpParam, IgmpVlanGroup groupInfo) {
        groupInfo.setGroupDesc(null);
        snmpExecutorService.setData(snmpParam, groupInfo);
    }

    @Override
    public void destoryVlanGroup(SnmpParam snmpParam, IgmpVlanGroup groupInfo) {
        groupInfo.setRowStatus(RowStatus.DESTORY);
        try {
            snmpExecutorService.setData(snmpParam, groupInfo);
        } catch (SnmpException e) {
            logger.info("destoryVlanGroup {}", e);
            /*
             * 删除失败，网管侧进行重试一次
             */
            snmpExecutorService.setData(snmpParam, groupInfo);
        }
    }

    @Override
    public List<IgmpGlobalGroup> getGlobalGroupList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpGlobalGroup.class);
    }

    @Override
    public IgmpCtcParam getCtcParam(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, IgmpCtcParam.class);
    }

    @Override
    public void modifyCtcParam(SnmpParam snmpParam, IgmpCtcParam ctcParam) {
        try {
            snmpExecutorService.setData(snmpParam, ctcParam);
        } catch (SnmpException e) {
            logger.info("modifyCtcParam {}", e);
            /*
             * 配置失败，网管侧进行重试一次
             */
            snmpExecutorService.setData(snmpParam, ctcParam);
        }

    }

    @Override
    public void reportCtcCdr(SnmpParam snmpParam) {
        snmpExecutorService.set(snmpParam, CDRREPORT_OID, String.valueOf(CDRREPORT_FLAG));
    }

    @Override
    public void createCtcProfile(SnmpParam snmpParam, IgmpCtcProfile ctcProfile) {
        ctcProfile.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, ctcProfile);
    }

    @Override
    public List<IgmpCtcProfile> getCtcProfileList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpCtcProfile.class);
    }

    @Override
    public void modifyCtcProfile(SnmpParam snmpParam, IgmpCtcProfile ctcProfile) {
        snmpExecutorService.setData(snmpParam, ctcProfile);
    }

    @Override
    public void destoryCtcProfile(SnmpParam snmpParam, Integer profileId) {
        IgmpCtcProfile ctcProfile = new IgmpCtcProfile();
        ctcProfile.setProfileId(profileId);
        ctcProfile.setRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, ctcProfile);
    }

    @Override
    public void createProfileGroupRela(SnmpParam snmpParam, IgmpCtcProfileGroupRela profileGroup) {
        profileGroup.setRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, profileGroup);
    }

    @Override
    public List<IgmpCtcProfileGroupRela> getProfileGroupRelaList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpCtcProfileGroupRela.class);
    }

    @Override
    public void destoryProfileGroupRela(SnmpParam snmpParam, IgmpCtcProfileGroupRela profileGroup) {
        profileGroup.setRowStatus(RowStatus.DESTORY);
        try {
            snmpExecutorService.setData(snmpParam, profileGroup);
        } catch (SnmpException e) {
            logger.info("destoryProfileGroupRela {}", e);
            /*
             * 删除失败，网管侧进行重试一次
             */
            snmpExecutorService.setData(snmpParam, profileGroup);
        }
    }

    @Override
    public List<IgmpCtcRecord> getCtcRecordList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpCtcRecord.class);
    }

    @Override
    public List<IgmpUniBindCtcProfile> getBindCtcProfileList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpUniBindCtcProfile.class);
    }

    @Override
    public List<IgmpOnuConfig> getIgmpOnuConfigList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpOnuConfig.class);
    }

    @Override
    public List<IgmpUniConfig> getAllUniConfigList(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpUniConfig.class);
    }

    @Override
    public List<IgmpUniVlanTrans> getAllUniVlanTrans(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, IgmpUniVlanTrans.class);
    }

}
