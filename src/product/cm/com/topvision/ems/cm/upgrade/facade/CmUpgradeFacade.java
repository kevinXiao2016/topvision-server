/***********************************************************************
 * $Id: CmUpgradeFacade.java,v1.0 2016年12月5日 下午1:30:00 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.upgrade.facade;

import java.util.List;

import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmAutoUpgradeCfgTable;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsCmSwVersionTable;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSfFileInfoMgtTable;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSfFileObject;
import com.topvision.ems.cm.upgrade.facade.domain.TopCcmtsSingleCmUpgradeObject;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Rod John
 * @created @2016年12月5日-下午1:30:00
 *
 */
@EngineFacade(serviceName = "CmUpgradeFacade", beanName = "cmUpgradeFacade")
public interface CmUpgradeFacade {

    List<TopCcmtsSfFileInfoMgtTable> getTopCcmtsSfFileInfoMgtTable(SnmpParam snmpParam);

    void deleteCcmtsFile(SnmpParam snmpParam, TopCcmtsSfFileInfoMgtTable file);

    List<TopCcmtsCmSwVersionTable> getTopCcmtsCmSwVersionTable(SnmpParam snmpParam);

    void upgradeSingleCm(SnmpParam snmpParam, TopCcmtsSingleCmUpgradeObject object);

    TopCcmtsCmSwVersionTable getSingleCmSwVersion(SnmpParam snmpParam, TopCcmtsCmSwVersionTable cm);

    void clearAutoUpgradeConfig(SnmpParam snmpParam);

    void clearCmSoftversionFile(SnmpParam snmpParam);

    Integer getCcmtsSfFileTotalSize(SnmpParam snmpParam);

    void createAutoUpgradeConfig(SnmpParam snmpParam, TopCcmtsCmAutoUpgradeCfgTable config);
    
    void uploadCmFile(SnmpParam snmpParam, TopCcmtsSfFileObject action);
    
    String loadUploadProcess(SnmpParam snmpParam);
    
    Integer getCmUpgradeStatus(SnmpParam snmpParam, Long statusIndex);
    
    void setCcmtsCmAutoUpgradeEnable(SnmpParam snmpParam);
}
