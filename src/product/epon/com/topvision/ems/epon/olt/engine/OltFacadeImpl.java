/***********************************************************************
 * $Id: OltControlFacadeImpl.java,v1.0 2013-10-25 上午10:48:55 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.engine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.config.domain.OltControlFileCommand;
import com.topvision.ems.epon.domain.CcmtsFftMonitorScalar;
import com.topvision.ems.epon.fault.domain.OltTrapConfig;
import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltClearCmOnTime;
import com.topvision.ems.epon.olt.domain.OltFileAttribute;
import com.topvision.ems.epon.olt.domain.OltMacAddressLearnTable;
import com.topvision.ems.epon.olt.domain.TopOnuGlobalCfgMgmt;
import com.topvision.ems.epon.olt.domain.TopSysFileDirEntry;
import com.topvision.ems.epon.olt.facade.OltFacade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.exception.engine.SnmpSetException;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author flack
 * @created @2013-10-25-上午10:48:55
 * 
 */
public class OltFacadeImpl extends EmsFacade implements OltFacade {
    private SnmpExecutorService snmpExecutorService;

    @Override
    @Deprecated
    public OltAttribute modifyOltAttribute(SnmpParam snmpParam, OltAttribute oltAttribute) {
        return snmpExecutorService.setData(snmpParam, oltAttribute);
    }

    @Override
    public void setOltTrapConfig(SnmpParam snmpParam, String manageAddress, String community) {
        OltTrapConfig oltTrapConfig = new OltTrapConfig();
        oltTrapConfig.setEponManagementAddrName(manageAddress);
        oltTrapConfig.setAddrTAddress(manageAddress);
        oltTrapConfig.setEponManagementAddrCommunity(community);
        oltTrapConfig.setEponManagementAddrRowStatus(RowStatus.CREATE_AND_GO);
        snmpExecutorService.setData(snmpParam, oltTrapConfig);
    }

    @Override
    public OltAttribute modifyOltBaseInfo(SnmpParam snmpParam, OltAttribute oltAttribute) {
        return snmpExecutorService.setData(snmpParam, oltAttribute);
    }

    @Override
    public OltAttribute modifyInBandConfig(SnmpParam snmpParam, OltAttribute oltAttribute) {
        return snmpExecutorService.setData(snmpParam, oltAttribute);
    }

    @Override
    public OltAttribute modifyOutBandConfig(SnmpParam snmpParam, OltAttribute oltAttribute) {
        return snmpExecutorService.setData(snmpParam, oltAttribute);
    }

    @Override
    public OltAttribute modifyOltSnmpConfig(SnmpParam snmpParam, OltAttribute oltAttribute) {
        // //snmp4j的Version与设备的version关系对应转换
        // //snmp4j中version的版本为0:V1 1:V2C 3:V3 OLT设备version的对应关系 1:V1 2:V2C 3:V3
        // if(oltAttribute.getTopSysSnmpVersion()!= null &&
        // oltAttribute.getTopSysSnmpVersion()!= 3){
        // if(oltAttribute.getTopSysSnmpVersion().equals(0)){
        // oltAttribute.setTopSysSnmpVersion(1);
        // }else if(oltAttribute.getTopSysSnmpVersion().equals(1)){
        // oltAttribute.setTopSysSnmpVersion(2);
        // }
        // }
        return snmpExecutorService.setData(snmpParam, oltAttribute);
    }

    @Override
    public void resetOlt(SnmpParam snmpParam) {
        String oid = "1.3.6.1.4.1.32285.11.2.3.1.2.5.0";
        snmpExecutorService.set(snmpParam, oid, "1");
    }

    @Override
    public Long getOltSysTime(SnmpParam snmpParam) {
        final String oid = "1.3.6.1.4.1.17409.2.3.1.1.1.0";
        return Long.parseLong(snmpExecutorService.get(snmpParam, oid));
    }

    @Override
    public void sysTiming(SnmpParam snmpParam, final Long sysTime) {
        try {
            List<Exception> exception = new ArrayList<Exception>(1);
            exception = snmpExecutorService.execute(new SnmpWorker<List<Exception>>(snmpParam) {
                private static final long serialVersionUID = -727320896339548070L;

                @Override
                protected void exec() {
                    try {
                        snmpUtil.reset(snmpParam);
                        Calendar time = Calendar.getInstance();
                        time.setTimeInMillis(sysTime);
                        int year = time.get(Calendar.YEAR);
                        VariableBinding vb = new VariableBinding(new OID("1.3.6.1.4.1.17409.2.3.1.1.1.0"),
                                new OctetString(new byte[] { (byte) ((year & 0xFF00) >> 8), (byte) (year & 0xFF),
                                        (byte) (time.get(Calendar.MONTH) + 1), (byte) time.get(Calendar.DAY_OF_MONTH),
                                        (byte) time.get(Calendar.HOUR_OF_DAY), (byte) time.get(Calendar.MINUTE),
                                        (byte) time.get(Calendar.SECOND), (byte) time.get(Calendar.MILLISECOND) }));
                        snmpUtil.set(vb);
                    } catch (Exception e) {
                        logger.error("", e);
                        result.add(e);
                    }
                }
            }, exception);
            if (!exception.isEmpty()) {
                throw exception.get(0);
            }
        } catch (Exception e) {
            logger.debug("", e);
            if (e instanceof SnmpException) {
                throw (SnmpException) e;
            } else {
                throw new SnmpSetException(e);
            }
        }
    }

    @Override
    public void switchoverOlt(SnmpParam snmpParam, Long slotIndex) {
        // 约定主备倒换的操作索引使用9或10都行，其他索引一定不行
        // Long index = EponIndex.getSlotNo(slotIndex);
        String oid = "1.3.6.1.4.1.17409.2.3.1.3.1.1.13.1." + slotIndex;
        snmpExecutorService.set(snmpParam, oid, "2");
    }

    @Override
    public void syncSlaveBoard(SnmpParam snmpParam, final Integer syncAction) {
        snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.2.3.1.6.2.0", syncAction.toString());
    }

    @Override
    public void restoreOlt(SnmpParam snmpParam) {
        String oid = "1.3.6.1.4.1.32285.11.2.3.1.2.6.0";
        snmpExecutorService.set(snmpParam, oid, "1");
    }

    @Override
    @Deprecated
    public OltAttribute modifyOltFacility(SnmpParam snmpParam, OltAttribute oltAttribute) {
        return snmpExecutorService.setData(snmpParam, oltAttribute);
    }

    @Override
    public List<OltFileAttribute> getOltFilePath(SnmpParam snmpParam) {
        List<OltFileAttribute> files = new ArrayList<OltFileAttribute>();
        files = snmpExecutorService.execute(new SnmpWorker<List<OltFileAttribute>>(snmpParam) {
            private static final long serialVersionUID = -727320896339548070L;

            @Override
            protected void exec() {
                snmpUtil.reset(snmpParam);
                result.addAll(snmpUtil.getTable(OltFileAttribute.class, true));
            }
        }, files);
        return files;
    }

    @Override
    public Integer getOltFileTransferStatus(SnmpParam snmpParam) {
        return Integer.parseInt(snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.17409.2.3.1.6.1.1.9.1"));
        // return 1;
    }

    @Override
    public void deleteOltFile(SnmpParam snmpParam, OltFileAttribute oltFileAttribute) {
        oltFileAttribute = snmpExecutorService.setData(snmpParam, oltFileAttribute);
    }

    @Override
    public void contorlOltFile(SnmpParam snmpParam, OltControlFileCommand oltControlFileCommand) {
        oltControlFileCommand = snmpExecutorService.setData(snmpParam, oltControlFileCommand);
        // 返回值为文件动作后的状态，用于判断是否成功
        // return oltControlFileCommand.getTransferStatus();
        // return getOltFileTransferStatus(snmpParam);
    }

    @Override
    public Integer getOltSyncSlaveBoardStatus(SnmpParam snmpParam) {
        return Integer.parseInt(snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.2.3.1.6.3.0"));
    }

    @Override
    public void execOltMacLearnSyncAction(SnmpParam snmpParam) {
        snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.2.3.1.8.1.1.0", new Integer(1).toString());
    }

    @Override
    public List<OltMacAddressLearnTable> refreshOltMacLearnTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, OltMacAddressLearnTable.class);
    }

    @Override
    public String getValueByOid(SnmpParam snmpParam, String oid) {
        return snmpExecutorService.get(snmpParam, oid);
    }

    @Override
    public <T> T getDomainInfoLine(SnmpParam snmpParam, T t) {
        return snmpExecutorService.getTableLine(snmpParam, t);
    }

    @Override
    public <T> List<T> getDomainInfoList(SnmpParam snmpParam, Class<T> T) {
        return snmpExecutorService.getTable(snmpParam, T);
    }

    @Override
    public String getCmcEntityType(Long cmcIndex, SnmpParam snmpParam) {
        String oid = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.2." + cmcIndex;
        return snmpExecutorService.get(snmpParam, oid);
    }

    @Override
    public List<TopSysFileDirEntry> refreshFileDir(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopSysFileDirEntry.class);
    }

    @Override
    public void modifyCcmtsFftGbStatus(SnmpParam snmpParam, CcmtsFftMonitorScalar ccmtsFftMonitorScalar) {
        snmpExecutorService.setData(snmpParam, ccmtsFftMonitorScalar);
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }

    @Override
    public List<TopOnuGlobalCfgMgmt> getOnuGlobalCfgMgmt(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopOnuGlobalCfgMgmt.class);
    }

    @Override
    public void modifyOnuGlobalCfgMgmt(SnmpParam snmpParam, TopOnuGlobalCfgMgmt topOnuGlobalCfgMgmt) {
        snmpExecutorService.set(snmpParam,
                "1.3.6.1.4.1.32285.11.2.3.4.15.2.1.2." + topOnuGlobalCfgMgmt.getTopOnuGlobalCfgMgmtItemIndex(), "0x"
                        + topOnuGlobalCfgMgmt.getTopOnuGlobalCfgMgmtValue());
    }

    @Override
    public OltClearCmOnTime getCmcClearTime(SnmpParam snmpParam) {
        return snmpExecutorService.getData(snmpParam, OltClearCmOnTime.class);
    }

    public boolean getOltSwitchStatus(SnmpParam snmpParam) {
        String status = snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.16.1.1.0");
        return status != null && status.equalsIgnoreCase("1");
    }

    @Override
    public void startSpectrumSwitchOlt(SnmpParam snmpParam) {
        snmpExecutorService.set(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.16.1.1.0", "1");
    }
}
