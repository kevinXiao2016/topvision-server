/***********************************************************************
 * $Id: OnuRefreshSnmpWorker.java,v1.0 2015-8-5 下午1:42:52 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.topology.engine;

import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuPonAttribute;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltTopOnuProductTable;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.topology.domain.OnuDiscoveryData;
import com.topvision.ems.gpon.onu.domain.GponOnuAttribute;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.domain.GponUniAttribute;
import com.topvision.ems.gpon.onuauth.GponConstant;
import com.topvision.ems.gpon.onuvoip.domain.TopGponOnuCapability;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.snmp.SnmpWorker;
import com.topvision.framework.utils.EponConstants;

/**
 * @author Rod John
 * @created @2015-8-5-下午1:42:52
 *
 */
public class OnuRefreshSnmpWorker<T extends OnuDiscoveryData> extends SnmpWorker<T> {
    private static final long serialVersionUID = 7416291017940760712L;

    /**
     * @param snmpParam
     */
    public OnuRefreshSnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.snmp.SnmpWorker#exec()
     */
    @Override
    protected void exec() throws Exception {
        snmpUtil.reset(snmpParam);
        Long onuIndex = result.getOnuIndexs().get(0);
        Long entityId = result.getEntityId();
        String onuEorG = result.getOnuEorG();
        if (EponConstants.EPON_ONU.equals(onuEorG)) {
            refreshEponOnu(entityId, onuIndex);
        } else if (GponConstant.GPON_ONU.equals(onuEorG)) {
            refreshGponOnuInfo(entityId, onuIndex);
        }
    }

    /**
     * Topo Epon Onu Info
     * 
     * @param entityId
     * @param onuIndex
     */
    private void refreshEponOnu(Long entityId, Long onuIndex) {
        try {
            OltOnuAttribute oltOnuAttribute = new OltOnuAttribute();
            oltOnuAttribute.setOnuIndex(onuIndex);
            oltOnuAttribute = snmpUtil.getTableLine(oltOnuAttribute);

            OltTopOnuProductTable oltTopOnuProduct = new OltTopOnuProductTable();
            oltTopOnuProduct.setOnuIndex(onuIndex);
            oltTopOnuProduct = snmpUtil.getTableLine(oltTopOnuProduct);

            oltOnuAttribute.setOnuPreType(oltTopOnuProduct.getOnuType());
            oltOnuAttribute.setEntityId(entityId);
            result.addOltOnuAttribute(oltOnuAttribute);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology onu attribute finished!");

        try {
            OltTopOnuCapability topOnuCapability = new OltTopOnuCapability();
            topOnuCapability.setOnuIndex(onuIndex);
            topOnuCapability = snmpUtil.getTableLine(topOnuCapability);
            topOnuCapability.setEntityId(entityId);
            result.addOltTopOnuCapabilities(topOnuCapability);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology onu topcapability finished!");

        try {
            OltOnuCapability onuCapability = new OltOnuCapability();
            onuCapability.setOnuIndex(onuIndex);
            onuCapability = snmpUtil.getTableLine(onuCapability);
            onuCapability.setEntityId(entityId);
            result.addOltOnuCapabilities(onuCapability);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology onu capability finished!");

        try {
            List<OltOnuPonAttribute> onuPonAttributes = result.getOltOnuPonAttributes();
            onuPonAttributes = snmpUtil.getTableLine(onuPonAttributes);
            result.setOltOnuPonAttributes(onuPonAttributes);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology onu pon finished!");

        try {
            List<OltUniAttribute> oltUniAttributes = result.getOltUniAttributes();
            oltUniAttributes = snmpUtil.getTableLine(oltUniAttributes);
            result.setOltUniAttributes(oltUniAttributes);

            List<OltUniExtAttribute> oltUniExtAttributes = new ArrayList<>();
            for (OltUniAttribute uniAttribute : oltUniAttributes) {
                OltUniExtAttribute uniExtAttribute = new OltUniExtAttribute();
                uniExtAttribute.setUniIndex(uniAttribute.getUniIndex());
                oltUniExtAttributes.add(uniExtAttribute);
            }
            oltUniExtAttributes = snmpUtil.getTableLine(oltUniExtAttributes);
            result.setOltUniExtAttributes(oltUniExtAttributes);

        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology uni attribute finished!");
    }

    /**
     * Topo Gpon Onu Info
     * 
     * @param entityId
     * @param onuIndex
     */
    private void refreshGponOnuInfo(Long entityId, Long onuIndex) {
        try {
            GponOnuAttribute gponOnuAttribute = new GponOnuAttribute();
            gponOnuAttribute.setOnuIndex(onuIndex);
            gponOnuAttribute = snmpUtil.getTableLine(gponOnuAttribute);
            String onuEquipmentID = gponOnuAttribute.getOnuEquipmentID();
            if (onuEquipmentID != null && onuEquipmentID.length() > 0 && onuEquipmentID.contains("PN")) {
                Long typeId = Long.parseLong(
                        onuEquipmentID.substring(onuEquipmentID.length() - 2, onuEquipmentID.length()), 16);
                gponOnuAttribute.setOnuPreType(typeId.intValue());
            } else {
                gponOnuAttribute.setOnuPreType(EponConstants.UNKNOWN_ONU_TYPE);
            }
            result.addOltOnuAttribute(gponOnuAttribute);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology gpononu attribute finished!");

        try {
            GponOnuCapability gponOnuCapability = new GponOnuCapability();
            gponOnuCapability.setOnuIndex(onuIndex);
            gponOnuCapability = snmpUtil.getTableLine(gponOnuCapability);
            gponOnuCapability.setEntityId(entityId);
            result.addGponOnuCapability(gponOnuCapability);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology gpononu capability finished!");

        try {
            TopGponOnuCapability topGponOnuCapability = new TopGponOnuCapability();
            topGponOnuCapability.setOnuIndex(onuIndex);
            topGponOnuCapability = snmpUtil.getTableLine(topGponOnuCapability);
            topGponOnuCapability.setEntityId(entityId);
            result.addTopGponOnuCapability(topGponOnuCapability);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology topGponOnuCapability finished!");

        try {
            List<OltUniAttribute> uniAttributes = result.getOltUniAttributes();
            List<GponUniAttribute> gponUniAttributes = new ArrayList<>();
            for (OltUniAttribute uni : uniAttributes) {
                GponUniAttribute gponUniAttribute = new GponUniAttribute();
                gponUniAttribute.setEntityId(entityId);
                gponUniAttribute.setUniIndex(uni.getUniIndex());
                gponUniAttributes.add(gponUniAttribute);
                gponUniAttribute.setUniEorG("G");
            }
            gponUniAttributes = snmpUtil.getTableLine(gponUniAttributes);
            result.addGponUniAttributes(gponUniAttributes);
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("topology gpononu uni finished!");

    }

}
