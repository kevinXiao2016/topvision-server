/***********************************************************************
 * $Id: OnuFacadeImpl.java,v1.0 2013-10-25 上午11:28:54 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.engine;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.cpelocation.domain.OnuCpeLocation;
import com.topvision.ems.epon.cpelocation.domain.OnuCpeLocationImprove;
import com.topvision.ems.epon.onu.domain.CC8800ABaseInfo;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgBand;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgProfile;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgRecord;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuCatv;
import com.topvision.ems.epon.onu.domain.OltOnuRstp;
import com.topvision.ems.epon.onu.domain.OltOnuUpgrade;
import com.topvision.ems.epon.onu.domain.OltOnuUpgradeEx;
import com.topvision.ems.epon.onu.domain.OltOnuVoip;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltTopOnuProductTable;
import com.topvision.ems.epon.onu.domain.OnuBaseInfo;
import com.topvision.ems.epon.onu.domain.OnuCatvInfo;
import com.topvision.ems.epon.onu.domain.OnuDeregisterTable;
import com.topvision.ems.epon.onu.domain.OnuQualityInfo;
import com.topvision.ems.epon.onu.domain.OnuReplaceEntry;
import com.topvision.ems.epon.onu.domain.TopGponOnuSpeed;
import com.topvision.ems.epon.onu.domain.TopOnuSpeed;
import com.topvision.ems.epon.onu.facade.OnuFacade;
import com.topvision.ems.epon.performance.domain.OnuLinkCollectInfo;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.exception.engine.SnmpNoResponseException;
import com.topvision.framework.exception.engine.SnmpNoSuchInstanceException;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;

/**
 * @author flack
 * @created @2013-10-25-上午11:28:54
 *
 */
public class OnuFacadeImpl extends EmsFacade implements OnuFacade {
    private SnmpExecutorService snmpExecutorService;

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public Integer setOnuAdminStatus(SnmpParam snmpParam, Long onuIndex, Integer status) {
        OltOnuAttribute oltOnuAttribute = new OltOnuAttribute();
        oltOnuAttribute.setOnuIndex(onuIndex);
        oltOnuAttribute.setOnuAdminStatus(status);
        oltOnuAttribute = snmpExecutorService.setData(snmpParam, oltOnuAttribute);
        return oltOnuAttribute.getOnuAdminStatus();
    }

    @Override
    public String modifyOnuName(SnmpParam snmpParam, Long onuIndex, String onuName) {
        OltOnuAttribute oltOnuAttribute = new OltOnuAttribute();
        oltOnuAttribute.setOnuIndex(onuIndex);
        oltOnuAttribute.setOnuName(onuName);
        oltOnuAttribute = snmpExecutorService.setData(snmpParam, oltOnuAttribute);
        return oltOnuAttribute.getOnuName();
    }

    @Override
    public void resetOnu(SnmpParam snmpParam, Long onuIndex) {
        OltOnuAttribute oltOnuAttribute = new OltOnuAttribute();
        oltOnuAttribute.setOnuIndex(onuIndex);
        oltOnuAttribute.setResetONU(1);
        oltOnuAttribute = snmpExecutorService.setData(snmpParam, oltOnuAttribute);
    }

    @Override
    public void deregisterOnu(SnmpParam snmpParam, Long onuIndex) {
        OltTopOnuCapability oltTopOnuCapability = new OltTopOnuCapability();
        oltTopOnuCapability.setOnuIndex(onuIndex);
        oltTopOnuCapability.setCapDeregister(1);
        oltTopOnuCapability = snmpExecutorService.setData(snmpParam, oltTopOnuCapability);
    }

    @Override
    public Integer setOnuVoipEnable(SnmpParam snmpParam, Long onuIndex, Integer onuVoipEnable) {
        OltOnuVoip onuVoip = new OltOnuVoip();
        onuVoip.setOnuIndex(onuIndex);
        onuVoip.setOnuVoipEnable(onuVoipEnable);
        onuVoip = snmpExecutorService.setData(snmpParam, onuVoip);
        return onuVoip.getOnuVoipEnable();
    }

    @Override
    public Integer setOnuTemperatureDetectEnable(SnmpParam snmpParam, Long onuIndex, Integer onuTemperatureDetectEnable) {
        OltTopOnuCapability oltTopOnuCapability = new OltTopOnuCapability();
        oltTopOnuCapability.setOnuIndex(onuIndex);
        oltTopOnuCapability.setTemperatureDetectEnable(onuTemperatureDetectEnable);
        oltTopOnuCapability = snmpExecutorService.setData(snmpParam, oltTopOnuCapability);
        return oltTopOnuCapability.getTemperatureDetectEnable();
    }

    @Override
    public Integer setOnuFecEnable(SnmpParam snmpParam, Long onuIndex, Integer onuFecEnable) {
        OltOnuCapability oltOnuCapability = new OltOnuCapability();
        oltOnuCapability.setOnuIndex(onuIndex);
        oltOnuCapability.setOnuFecEnable(onuFecEnable);
        oltOnuCapability = snmpExecutorService.setData(snmpParam, oltOnuCapability);
        return oltOnuCapability.getOnuFecEnable();
    }

    @Override
    public Integer setOnuIsolationEnable(SnmpParam snmpParam, Long onuIndex, Integer onuIsolationEnable) {
        OltTopOnuCapability oltTopOnuCapability = new OltTopOnuCapability();
        oltTopOnuCapability.setOnuIndex(onuIndex);
        oltTopOnuCapability.setOnuIsolationEnable(onuIsolationEnable);
        oltTopOnuCapability = snmpExecutorService.setData(snmpParam, oltTopOnuCapability);
        return oltTopOnuCapability.getOnuIsolationEnable();
    }

    @Override
    public Integer setOnu15minEnable(SnmpParam snmpParam, Long onuIndex, Integer onu15minEnable) {
        OltTopOnuCapability oltTopOnuCapability = new OltTopOnuCapability();
        oltTopOnuCapability.setOnuIndex(onuIndex);
        oltTopOnuCapability.setPonPerfStats15minuteEnable(onu15minEnable);
        oltTopOnuCapability = snmpExecutorService.setData(snmpParam, oltTopOnuCapability);
        return oltTopOnuCapability.getPonPerfStats15minuteEnable();
    }

    @Override
    public Integer setOnu24hEnable(SnmpParam snmpParam, Long onuIndex, Integer onu24hEnable) {
        OltTopOnuCapability oltTopOnuCapability = new OltTopOnuCapability();
        oltTopOnuCapability.setOnuIndex(onuIndex);
        oltTopOnuCapability.setPonPerfStats24hourEnable(onu24hEnable);
        oltTopOnuCapability = snmpExecutorService.setData(snmpParam, oltTopOnuCapability);
        return oltTopOnuCapability.getPonPerfStats24hourEnable();
    }

    @Override
    public Integer setOnuCatvEnable(SnmpParam snmpParam, Long onuIndex, Integer onuCatvEnable) {
        OltOnuCatv oltOnuCatv = new OltOnuCatv();
        oltOnuCatv.setOnuIndex(onuIndex);
        oltOnuCatv.setOnuCatvEnable(onuCatvEnable);
        oltOnuCatv = snmpExecutorService.setData(snmpParam, oltOnuCatv);
        return oltOnuCatv.getOnuCatvEnable();
    }

    @Override
    public Integer setOnuMacMaxNum(SnmpParam snmpParam, Long onuIndex, Integer onuMacMaxNum) {
        OltTopOnuCapability oltTopOnuCapability = new OltTopOnuCapability();
        oltTopOnuCapability.setOnuIndex(onuIndex);
        oltTopOnuCapability.setCapAddrMaxQuantity(onuMacMaxNum);
        oltTopOnuCapability = snmpExecutorService.setData(snmpParam, oltTopOnuCapability);
        return oltTopOnuCapability.getCapAddrMaxQuantity();
    }

    @Override
    public Integer setOnuRstpBridgeMode(SnmpParam snmpParam, Long onuIndex, Integer onuRstpBridgeMode) {
        OltOnuRstp oltOnuRstp = new OltOnuRstp();
        oltOnuRstp.setOnuIndex(onuIndex);
        oltOnuRstp.setRstpBridgeMode(onuRstpBridgeMode);
        oltOnuRstp = snmpExecutorService.setData(snmpParam, oltOnuRstp);
        return oltOnuRstp.getRstpBridgeMode();
    }

    @Override
    public List<OltOnuCatv> getOltOnuCatv(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltOnuCatv.class);
    }

    @Override
    public List<OltOnuRstp> getOltOnuRstp(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltOnuRstp.class);
    }

    @Override
    public List<OltOnuVoip> getOnuVoip(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltOnuVoip.class);
    }

    @Override
    public List<OltOnuUpgradeEx> getOnuUpgradeRecords(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltOnuUpgradeEx.class);
    }

    @Override
    public String getOnuUpgradeStatus(SnmpParam snmpParam, Integer transactionIndex) {
        OltOnuUpgrade oltOnuUpgrade = new OltOnuUpgrade();
        oltOnuUpgrade.setTopOnuUpgradeTransactionIndex(transactionIndex);
        snmpExecutorService.getTableLine(snmpParam, oltOnuUpgrade);
        return oltOnuUpgrade.getTopOnuUpgradeStatus();
    }

    @Override
    public OltOnuUpgrade addOnuUpgrade(SnmpParam snmpParam, OltOnuUpgrade oltOnuUpgrade) {
        oltOnuUpgrade.setTopOnuUpgradeRowStatus(RowStatus.CREATE_AND_GO);
        return snmpExecutorService.setData(snmpParam, oltOnuUpgrade);
    }

    @Override
    public void deleteOnuUpgrade(SnmpParam snmpParam, OltOnuUpgradeEx oltOnuUpgradeEx) {
        OltOnuUpgradeEx onuUpgradeEx = new OltOnuUpgradeEx();
        onuUpgradeEx.setTopOnuUpgradeTransactionIndex(oltOnuUpgradeEx.getTopOnuUpgradeTransactionIndex());
        onuUpgradeEx.setTopOnuUpgradeRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, onuUpgradeEx);
    }

    @Override
    public Integer getOnuTemperature(SnmpParam snmpParam, Long onuIndex) {
        OltTopOnuCapability onu = new OltTopOnuCapability();
        onu.setOnuIndex(onuIndex);
        OltTopOnuCapability tmpOnu = snmpExecutorService.getTableLine(snmpParam, onu);
        return tmpOnu.getCurrentTemperature();
    }

    @Override
    public List<OltOnuAttribute> getOnuAttributes(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltOnuAttribute.class);
    }

    @Override
    public List<OltOnuCapability> getOnuCapabilities(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltOnuCapability.class);
    }

    /**
     * 设置ONU预配置类型
     * 
     * @param entityId
     * @param onuIndex
     * @param type
     */
    @Override
    public void setOnuPreType(SnmpParam snmpParam, OltTopOnuProductTable oltOnuPreType) {
        snmpExecutorService.setData(snmpParam, oltOnuPreType);
    }

    @Override
    public List<OltTopOnuProductTable> getOnuPreType(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltTopOnuProductTable.class);
    }

    @Override
    public void addOnuAutoUpgProfile(SnmpParam snmpParam, OltOnuAutoUpgProfile p) {
        OltOnuAutoUpgProfile o = new OltOnuAutoUpgProfile();
        o.setProfileId(p.getProfileId());
        if (p.getProName() != null && !p.getProName().equalsIgnoreCase("")) {
            o.setProName(p.getProName());
        }
        if (p.getProOnuType() != null) {
            o.setProOnuType(p.getProOnuType());
        }
        if (p.getProOnuTypeStr() != null && !p.getProOnuTypeStr().equalsIgnoreCase("")
                && p.getProOnuTypeStr().length() == 4) {
            o.setProOnuTypeStr(p.getProOnuTypeStr());
        } else {
            o.setProOnuTypeStr("none");
        }
        if (p.getProHwVersion() != null) {
            o.setProHwVersion(p.getProHwVersion());
        } else {
            o.setProHwVersion("");
        }
        o.setProMode(p.getProMode());
        o.setProNewVersion(p.getProNewVersion());
        if (p.getApp() != null && !p.getApp().equalsIgnoreCase("")) {
            o.setApp(p.getApp());
        }
        if (p.getBoot() != null && !p.getBoot().equalsIgnoreCase("")) {
            o.setBoot(p.getBoot());
        }
        if (p.getWebs() != null && !p.getWebs().equalsIgnoreCase("")) {
            o.setWebs(p.getWebs());
        }
        if (p.getOther() != null && !p.getOther().equalsIgnoreCase("")) {
            o.setOther(p.getOther());
        }
        o.setPers(p.getPers());
        o.setProUpgTime(p.getProUpgTime());
        o.setTopOnuAutoUpgProfileRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, o);
    }

    @Override
    public void modifyOnuAutoUpgProfile(SnmpParam snmpParam, OltOnuAutoUpgProfile p) {
        OltOnuAutoUpgProfile o = new OltOnuAutoUpgProfile();
        o.setProfileId(p.getProfileId());
        if (p.getProName() != null && !p.getProName().equalsIgnoreCase("")) {
            o.setProName(p.getProName());
        }
        if (p.getProOnuType() != null) {
            o.setProOnuType(p.getProOnuType());
        }
        if (p.getProOnuTypeStr() != null) {
            o.setProOnuTypeStr(p.getProOnuTypeStr());
        } else {
            o.setProOnuTypeStr("none");
        }
        if (p.getProHwVersion() != null) {
            o.setProHwVersion(p.getProHwVersion());
        } else {
            o.setProHwVersion("");
        }
        o.setProMode(p.getProMode());
        o.setProNewVersion(p.getProNewVersion());
        if (p.getApp() != null && !p.getApp().equalsIgnoreCase("")) {
            o.setApp(p.getApp());
        }
        if (p.getBoot() != null && !p.getBoot().equalsIgnoreCase("")) {
            o.setBoot(p.getBoot());
        }
        if (p.getWebs() != null && !p.getWebs().equalsIgnoreCase("")) {
            o.setWebs(p.getWebs());
        }
        if (p.getOther() != null && !p.getOther().equalsIgnoreCase("")) {
            o.setOther(p.getOther());
        }
        o.setPers(p.getPers());
        o.setProUpgTime(p.getProUpgTime());
        snmpExecutorService.setData(snmpParam, o);
    }

    @Override
    public void delOnuAutoUpgProfile(SnmpParam snmpParam, Integer profileId) {
        OltOnuAutoUpgProfile o = new OltOnuAutoUpgProfile();
        o.setProfileId(profileId);
        o.setTopOnuAutoUpgProfileRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, o);
    }

    @Override
    public void bandOnuAutoUpgProfile(SnmpParam snmpParam, OltOnuAutoUpgBand band) {
        OltOnuAutoUpgBand o = new OltOnuAutoUpgBand();
        o.setProfileId(band.getProfileId());
        o.setSlotNo(band.getSlotNo());
        o.setPonNo(band.getPonNo());
        o.setOnuNo(EponConstants.EPON_ONU_AUTOUPG_ALLNO_RESTART);
        o.setTopOnuAutoUpgInstallRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, o);
    }

    @Override
    public void unbandOnuAutoUpgProfile(SnmpParam snmpParam, OltOnuAutoUpgBand band) {
        OltOnuAutoUpgBand o = new OltOnuAutoUpgBand();
        o.setProfileId(band.getProfileId());
        o.setSlotNo(band.getSlotNo());
        o.setPonNo(band.getPonNo());
        o.setOnuNo(EponConstants.EPON_ONU_AUTOUPG_ALLNO_RESTART);
        o.setTopOnuAutoUpgInstallRowStatus(RowStatus.DESTORY);
        snmpExecutorService.setData(snmpParam, o);
    }

    @Override
    public void restartOnuAutoUpg(SnmpParam snmpParam, OltOnuAutoUpgBand band) {
        OltOnuAutoUpgBand o = new OltOnuAutoUpgBand();
        o.setProfileId(band.getProfileId());
        o.setSlotNo(band.getSlotNo());
        o.setPonNo(band.getPonNo());
        o.setOnuNo(EponConstants.EPON_ONU_AUTOUPG_ALLNO_RESTART);
        o.setTopOnuAutoUpgInstallAction(EponConstants.EPON_ONU_AUTOUPG_RESTART);
        snmpExecutorService.setData(snmpParam, o);
    }

    @Override
    public void cancelOnuAutoUpg(SnmpParam snmpParam, OltOnuAutoUpgRecord record) {
        OltOnuAutoUpgRecord o = new OltOnuAutoUpgRecord();
        o.setSlotNo(record.getSlotNo());
        o.setRecordId(record.getRecordId());
        o.setUpgRecordAction(EponConstants.EPON_ONU_AUTOUPG_CANCEL);
        snmpExecutorService.setData(snmpParam, o);
    }

    @Override
    public List<OltOnuAutoUpgProfile> getOnuAutoUpgProfile(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltOnuAutoUpgProfile.class);
    }

    @Override
    public List<OltOnuAutoUpgBand> getOnuAutoUpgBand(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltOnuAutoUpgBand.class);
    }

    @Override
    public List<OltOnuAutoUpgRecord> getOnuAutoUpgRecord(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltOnuAutoUpgRecord.class);
    }

    @Override
    public List<OltTopOnuCapability> getOnuListAttribute(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltTopOnuCapability.class);
    }

    @Override
    public OnuBaseInfo getOnuBaseInfo(SnmpParam snmpParam, OnuBaseInfo baseInfo) {
        return snmpExecutorService.getTableLine(snmpParam, baseInfo);
    }

    @Override
    public CC8800ABaseInfo getCC8800ABaseInfo(SnmpParam snmpParam, CC8800ABaseInfo caBaseInfo) {
        return snmpExecutorService.getTableLine(snmpParam, caBaseInfo);
    }

    @Override
    public OnuQualityInfo refreshOnuQuality(SnmpParam snmpParam, OnuQualityInfo onuQualityInfo) {
        return snmpExecutorService.getTableLine(snmpParam, onuQualityInfo);
    }

    @Override
    public OnuCatvInfo refreshOnuCatv(SnmpParam snmpParam, OnuCatvInfo onuCatvInfo) {
        return snmpExecutorService.getTableLine(snmpParam, onuCatvInfo);
    }

    @Override
    public OltOnuCatv getOltOnuCatv(SnmpParam snmpParam, Long onuIndex) {
        OltOnuCatv oltOnuCatv = new OltOnuCatv();
        oltOnuCatv.setOnuIndex(onuIndex);
        return snmpExecutorService.getTableLine(snmpParam, oltOnuCatv);
    }

    @Override
    public OltOnuVoip getOnuVoip(SnmpParam snmpParam, Long onuIndex) {
        OltOnuVoip oltOnuVoip = new OltOnuVoip();
        oltOnuVoip.setOnuIndex(onuIndex);
        return snmpExecutorService.getTableLine(snmpParam, oltOnuVoip);
    }

    @Override
    public OltOnuRstp getOltOnuRstp(SnmpParam snmpParam, Long onuIndex) {
        OltOnuRstp oltOnuRstp = new OltOnuRstp();
        oltOnuRstp.setOnuIndex(onuIndex);
        return snmpExecutorService.getTableLine(snmpParam, oltOnuRstp);
    }

    @Override
    public OnuCpeLocation getOnuCpeLocation(SnmpParam snmpParam, String cpeMac) {
        OnuCpeLocationImprove cpeLocImprove = new OnuCpeLocationImprove();
        OnuCpeLocation cpeLoc = new OnuCpeLocation();
        cpeLocImprove.setMacLocation(cpeMac);
        cpeLoc.setMacLocation(cpeMac);
        try {
            cpeLocImprove = snmpExecutorService.getTableLine(snmpParam, cpeLocImprove);
            cpeLoc.setSlotLocation(cpeLocImprove.getSlotLocation());
            cpeLoc.setPortLocation(cpeLocImprove.getPortLocation());
            cpeLoc.setOnuLocation(cpeLocImprove.getOnuLocation());
            cpeLoc.setUniLocation(cpeLocImprove.getUniLocation());
            cpeLoc.setVlanId(cpeLocImprove.getSvlan());
        } catch (Exception e) {
            cpeLoc = snmpExecutorService.getTableLine(snmpParam, cpeLoc);
        }
        return cpeLoc;
    }

    @Override
    public List<OnuLinkCollectInfo> getOnuLinkInfoList(SnmpParam snmpParam, List<OnuLinkCollectInfo> onuLinkList) {
        return snmpExecutorService.getTableLine(snmpParam, onuLinkList);
    }

    @Override
    public List<OnuCatvInfo> getOnuCatvInfoList(SnmpParam snmpParam, List<OnuCatvInfo> onuCatvInfo) {
        return snmpExecutorService.getTableLine(snmpParam, onuCatvInfo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.onu.facade.OnuFacade#replaceOnuEntry(com.topvision.framework.snmp.
     * SnmpParam, com.topvision.ems.epon.onu.domain.OnuReplaceEntry)
     */
    @Override
    public void replaceOnuEntry(SnmpParam snmpParam, OnuReplaceEntry onuReplaceEntry) {
        snmpExecutorService.setData(snmpParam, onuReplaceEntry);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.onu.facade.OnuFacade#getOnuDeregisterInfo(java.util.List)
     */
    @Override
    public List<OnuDeregisterTable> getOnuDeregisterInfo(List<OnuDeregisterTable> tables) {
        List<Long> noSupportOltList = new ArrayList<>();
        List<Long> noResponseOltList = new ArrayList<>();
        for (OnuDeregisterTable tmp : tables) {
            try {
                if (noSupportOltList.contains(tmp.getEntityId()) || noResponseOltList.contains(tmp.getEntityId())) {
                    continue;
                }
                tmp = snmpExecutorService.getTableLine(tmp.getSnmpParam(), tmp);
            } catch (SnmpNoSuchInstanceException e) {
                noSupportOltList.add(tmp.getEntityId());
            } catch (SnmpNoResponseException e) {
                noResponseOltList.add(tmp.getEntityId());
            } catch (SnmpException e) {
                logger.error("getOnuDeregisterInfo error:", e);
            }
        }
        return tables;
    }

    @Override
    public TopOnuSpeed getEponOnuDownRate(SnmpParam snmpParam, Long onuIndex) {
        TopOnuSpeed topOnuSpeed = new TopOnuSpeed();
        topOnuSpeed.setOnuIndex(onuIndex);
        return snmpExecutorService.getTableLine(snmpParam, topOnuSpeed);
    }

    @Override
    public TopGponOnuSpeed getGponOnuDownRate(SnmpParam snmpParam, Long onuIndex) {
        TopGponOnuSpeed topGponOnuSpeed = new TopGponOnuSpeed();
        topGponOnuSpeed.setOnuIndex(onuIndex);
        return snmpExecutorService.getTableLine(snmpParam, topGponOnuSpeed);
    }

    @Override
    public void startEponOnuDownRateTest(SnmpParam snmpParam, Long onuIndex) {
        TopOnuSpeed topOnuSpeed = new TopOnuSpeed();
        topOnuSpeed.setOnuIndex(onuIndex);
        topOnuSpeed.setTopOnuSpeedTestAction(1);
        // topOnuSpeed.setTopOnuSpeedTestUrl(TopOnuSpeed.SERVER_URL_3);
        snmpExecutorService.setData(snmpParam, topOnuSpeed);
    }

    @Override
    public void startGponOnuDownRateTest(SnmpParam snmpParam, Long onuIndex) {
        TopGponOnuSpeed topGponOnuSpeed = new TopGponOnuSpeed();
        topGponOnuSpeed.setOnuIndex(onuIndex);
        topGponOnuSpeed.setTopGponOnuSpeedTestAction(1);
        // topGponOnuSpeed.setTopGponOnuSpeedTestUrl(TopOnuSpeed.SERVER_URL_3);
        snmpExecutorService.setData(snmpParam, topGponOnuSpeed);
    }
}
