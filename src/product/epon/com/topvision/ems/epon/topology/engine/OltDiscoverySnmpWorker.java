/***********************************************************************
 * $Id: OltDiscoverySnmpWorker.java,v1.0 2011-9-22 下午04:13:47 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.engine;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltTopOnuProductTable;
import com.topvision.ems.epon.topology.domain.OltDiscoveryData;
import com.topvision.ems.gpon.onu.domain.GponOnuAttribute;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.framework.common.LoggerUtil;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;

/**
 * @author Victor
 * @created @2011-9-22-下午04:13:47
 * 
 */
public class OltDiscoverySnmpWorker<T extends OltDiscoveryData> extends BaseOltDiscoverySnmpWorker<OltDiscoveryData> {
    private static final long serialVersionUID = -4069551288690695802L;

    /**
     * @param snmpParam
     * @param result
     */
    public OltDiscoverySnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.snmp.SnmpWorker#exec()
     */
    @Override
    protected void exec() throws Exception {
        Long timeTmp = 0L;
        super.exec();

        snmpParam
                .setMibs("RFC1213-MIB,NSCRTV-EPONEOC-EPON-MIB,SUMA-EPONEOC-EPON-MIB,TOPVISION-CCMTS-MIB,SNMP-VIEW-BASED-ACM-MIB,SNMP-USER-BASED-SM-MIB,SNMP-TARGET-MIB,SNMP-NOTIFICATION-MIB");
        snmpUtil.reset(snmpParam);

        List<String> excludeOids = result.getExcludeOids();
        try {
            // ONU
            timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "OltOnuAttribute");
            List<OltOnuAttribute> onus = snmpUtil.getTable(OltOnuAttribute.class, true, excludeOids);
            if (onus != null) {
                result.setOnus(new ArrayList<OltOnuAttribute>());
                for (OltOnuAttribute onu : onus) {
                    onu.setEntityId(result.getEntityId());
                    result.addOnu(onu);
                }
                try {
                    List<OltTopOnuProductTable> topOnuProductTables = snmpUtil.getTable(OltTopOnuProductTable.class,
                            true, excludeOids);
                    for (OltTopOnuProductTable productTable : topOnuProductTables) {
                        for (OltOnuAttribute onuAttribute : result.getOnus()) {
                            if (productTable.getOnuIndex().equals(onuAttribute.getOnuIndex())) {
                                onuAttribute.setOnuPreType(productTable.getTopOnuProductTypeNum());
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("topology OltTopOnuProductTable error:", e);
                }
            } else {
                logger.info("topology onus error: onus = null");
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "OltOnuAttribute", timeTmp);
        logger.info("topology onus finished!");

        try {
            // GPON ONU
            timeTmp = LoggerUtil.topoStartTimeLog(snmpParam.getIpAddress(), "GponOnuAttribute");
            List<GponOnuAttribute> onus = snmpUtil.getTable(GponOnuAttribute.class, true, excludeOids);
            if (onus != null) {
                for (GponOnuAttribute gponOnuAttribute : onus) {
                    gponOnuAttribute.setEntityId(result.getEntityId());
                    gponOnuAttribute.setOnuEorG(GponConstant.GPON_ONU);
                    Integer onuPreType = EponConstants.onuPreTypeFromEquipmentID(gponOnuAttribute.getOnuEquipmentID());
                    gponOnuAttribute.setOnuPreType(onuPreType);
                    result.addOnu(gponOnuAttribute);
                }
            } else {
                logger.info("topology onus error: onus = null");
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        LoggerUtil.topoEndTimeLog(snmpParam.getIpAddress(), "gponOnuAttribute", timeTmp);
        logger.info("topology gpon onus finished!");

        logger.info("finish discovery TolologyData ipAddress:" + snmpParam.getIpAddress() + "  EntityId:"
                + snmpParam.getEntityId());
    }
}
