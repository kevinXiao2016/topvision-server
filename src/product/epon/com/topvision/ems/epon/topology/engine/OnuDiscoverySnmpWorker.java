/***********************************************************************
 * $Id: OnuDiscoverySnmpWorker.java,v1.0 2011-12-23 下午01:52:59 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.engine;

import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.topology.domain.OnuDiscoveryData;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.domain.GponUniAttribute;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.gpon.onuvoip.domain.TopGponOnuCapability;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;

/**
 * @author huqiao
 * @created @2011-12-23-下午01:52:59
 * 
 */
public class OnuDiscoverySnmpWorker<T extends OnuDiscoveryData> extends SnmpWorker<T> {
    private static final long serialVersionUID = 3754316005596684419L;

    /**
     * @param snmpParam
     */
    public OnuDiscoverySnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.snmp.SnmpWorker#exec()
     */
    @Override
    protected void exec() throws Exception {
        snmpUtil.reset(snmpParam);
        Long entityId = result.getEntityId();
        collectEponOnu(entityId);
        collectGponOnu(entityId);
    }

    private void collectEponOnu(Long entityId) {
        try {
            // topOnuCapabilities
            List<OltTopOnuCapability> topOnuCapabilities = snmpUtil.getTable(OltTopOnuCapability.class, true);
            for (OltTopOnuCapability entry : topOnuCapabilities) {
                entry.setEntityId(entityId);
            }
            result.setOltTopOnuCapabilities(topOnuCapabilities);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology onu topcapability finished!");

        try {
            // oltOnuCapabilities
            List<OltOnuCapability> oltOnuCapabilities = snmpUtil.getTable(OltOnuCapability.class, true);
            for (OltOnuCapability entry : oltOnuCapabilities) {
                entry.setEntityId(entityId);
            }
            result.setOltOnuCapabilities(oltOnuCapabilities);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology onu capability finished!");

        try {
            // ONUPON
            List<OltOnuPonAttribute> onuPons = snmpUtil.getTable(OltOnuPonAttribute.class, true);
            for (OltOnuPonAttribute onuPon : onuPons) {
                onuPon.setEntityId(entityId);
            }
            result.setOltOnuPonAttributes(onuPons);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology onu pon finished!");

        try {
            // UNI
            List<OltUniAttribute> oltUniAttributes = snmpUtil.getTable(OltUniAttribute.class, true);
            for (OltUniAttribute uni : oltUniAttributes) {
                uni.setEntityId(result.getEntityId());
                uni.setUniEorG("E");
            }
            result.setOltUniAttributes(oltUniAttributes);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology uni attribute finished!");

        try {
            // uniExt
            List<OltUniExtAttribute> uniExtAttributes = snmpUtil.getTable(OltUniExtAttribute.class, true);
            for (OltUniExtAttribute entry : uniExtAttributes) {
                entry.setEntityId(result.getEntityId());
            }
            result.setOltUniExtAttributes(uniExtAttributes);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology uni ext attribute finished!");
    }

    private void collectGponOnu(Long entityId) {
        try {
            List<GponOnuCapability> gponOnuCapabilities = snmpUtil.getTable(GponOnuCapability.class, true);
            for (GponOnuCapability entry : gponOnuCapabilities) {
                entry.setEntityId(entityId);
            }
            result.setGponOnuCapabilities(gponOnuCapabilities);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology GponOnuCapability finished!");

        try {
            List<GponUniAttribute> gponUniAttributes = snmpUtil.getTable(GponUniAttribute.class, true);
            for (GponUniAttribute uni : gponUniAttributes) {
                uni.setEntityId(entityId);
                uni.setUniEorG(GponConstant.GPON_ONU);
            }
            result.addGponUniAttributes(gponUniAttributes);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology GponOnuCapability finished!");
        
        try {
            List<TopGponOnuCapability> topGponOnuCapabilities = snmpUtil.getTable(TopGponOnuCapability.class, true);
            for (TopGponOnuCapability top : topGponOnuCapabilities) {
                top.setEntityId(entityId);
            }
            result.setTopGponOnuCapabilities(topGponOnuCapabilities);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology GponOnuCapability finished!");
        
    }

}
