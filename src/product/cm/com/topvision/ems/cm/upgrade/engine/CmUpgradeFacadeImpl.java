/***********************************************************************
 * $Id: CmUpgradeFacadeImpl.java,v1.0 2016年12月8日 上午9:03:43 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.upgrade.engine;

import java.util.List;

import javax.annotation.Resource;

import com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmAutoUpgradeCfgTable;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmSwVersionTable;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSfFileInfoMgtTable;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSfFileObject;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSingleCmUpgradeObject;
import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.exception.engine.SnmpNoSuchObjectException;
import com.topvision.framework.snmp.RowStatus;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2016年12月8日-上午9:03:43
 *  
 */
@Facade("cmUpgradeFacade")
public class CmUpgradeFacadeImpl extends EmsFacade implements CmUpgradeFacade {
    private static final String AUTO_UPGRADE_ENABLE_OID = "1.3.6.1.4.1.32285.11.1.1.2.28.2.1.0";

    @Resource(name = "snmpExecutorService")
    private SnmpExecutorService snmpExecutorService;

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#getTopCcmtsSfFileInfoMgtTable(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public List<TopCcmtsSfFileInfoMgtTable> getTopCcmtsSfFileInfoMgtTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopCcmtsSfFileInfoMgtTable.class);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#deleteCcmtsFile(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSfFileInfoMgtTable)
     */
    @Override
    public void deleteCcmtsFile(SnmpParam snmpParam, TopCcmtsSfFileInfoMgtTable file) {
        snmpExecutorService.setData(snmpParam, file);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#getTopCcmtsCmSwVersionTable(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public List<TopCcmtsCmSwVersionTable> getTopCcmtsCmSwVersionTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, TopCcmtsCmSwVersionTable.class);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#upgradeSingleCm(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSingleCmUpgradeObject)
     */
    @Override
    public void upgradeSingleCm(SnmpParam snmpParam, TopCcmtsSingleCmUpgradeObject object) {
        snmpExecutorService.setData(snmpParam, object);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#getSingleCmSwVersion(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmSwVersionTable)
     */
    @Override
    public TopCcmtsCmSwVersionTable getSingleCmSwVersion(SnmpParam snmpParam, TopCcmtsCmSwVersionTable cm) {
        return snmpExecutorService.getTableLine(snmpParam, cm);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#clearAutoUpgradeConfig(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void clearAutoUpgradeConfig(SnmpParam snmpParam) {
        List<TopCcmtsCmAutoUpgradeCfgTable> results = snmpExecutorService.getTable(snmpParam, TopCcmtsCmAutoUpgradeCfgTable.class);
        for (TopCcmtsCmAutoUpgradeCfgTable config : results) {
            TopCcmtsCmAutoUpgradeCfgTable delete = new TopCcmtsCmAutoUpgradeCfgTable();
            delete.setTopCcmtsCmAutoUpgradeCmModelNum(config.getTopCcmtsCmAutoUpgradeCmModelNum());
            delete.setTopCcmtsCmAutoUpgradeRowStatus(RowStatus.DESTORY);
            snmpExecutorService.setData(snmpParam, delete);
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#clearCmSoftversionFile(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void clearCmSoftversionFile(SnmpParam snmpParam) {
        List<TopCcmtsSfFileInfoMgtTable> fileList = snmpExecutorService.getTable(snmpParam, TopCcmtsSfFileInfoMgtTable.class);
        for (TopCcmtsSfFileInfoMgtTable file : fileList) {
            if (TopCcmtsSfFileInfoMgtTable.CM_IMAGE.equals(file.getTopCcmtsSfFileType())) {
                // Delete Cm Image File
                TopCcmtsSfFileInfoMgtTable delete = new TopCcmtsSfFileInfoMgtTable();
                delete.setTopCcmtsSfFileType(file.getTopCcmtsSfFileType());
                delete.setTopCcmtsSfFileName(file.getTopCcmtsSfFileName());
                delete.setTopCcmtsSfFileMgtAction(TopCcmtsSfFileInfoMgtTable.ERASE);
                snmpExecutorService.setData(snmpParam, delete);
            }
        }

    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#getCcmtsSfFileTotalSize(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public Integer getCcmtsSfFileTotalSize(SnmpParam snmpParam) {
        // topCcmtsSfFileTotalSize oid = 1.3.6.1.4.1.32285.11.1.1.2.5.1.11.0
        String result = snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.5.1.11.0");
        if (result != null) {
            return Integer.parseInt(result);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#createAutoUpgradeConfig(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmAutoUpgradeCfgTable)
     */
    @Override
    public void createAutoUpgradeConfig(SnmpParam snmpParam, TopCcmtsCmAutoUpgradeCfgTable config) {
        snmpExecutorService.setData(snmpParam, config);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#uploadCmFile(com.topvision.framework.snmp.SnmpParam, com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSfFileObject)
     */
    @Override
    public void uploadCmFile(SnmpParam snmpParam, TopCcmtsSfFileObject action) {
        snmpExecutorService.setData(snmpParam, action);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#loadUploadProcess(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public String loadUploadProcess(SnmpParam snmpParam) {
        return snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.5.1.8.0");
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#getCmUpgradeStatus(com.topvision.framework.snmp.SnmpParam, java.lang.Long)
     */
    @Override
    public Integer getCmUpgradeStatus(SnmpParam snmpParam, Long statusIndex) {
        String result = snmpExecutorService.get(snmpParam, "1.3.6.1.4.1.32285.11.1.1.2.28.3.1.4." + statusIndex);
        if (result != null) {
            return Integer.parseInt(result);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.upgrade.facade.CmUpgradeFacade#setCcmtsCmAutoUpgradeEnable(com.topvision.framework.snmp.SnmpParam)
     */
    @Override
    public void setCcmtsCmAutoUpgradeEnable(SnmpParam snmpParam) {
        snmpExecutorService.set(snmpParam, AUTO_UPGRADE_ENABLE_OID, "1");
    }
}
