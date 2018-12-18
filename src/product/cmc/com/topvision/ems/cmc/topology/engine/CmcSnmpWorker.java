/***********************************************************************
 * $Id: CmcSnmpWorker.java,v1.0 2011-6-28 下午08:28:45 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.topology.engine;

import java.util.HashMap;
import java.util.Map;

import com.topvision.ems.cmc.domain.CmcData;
import com.topvision.ems.engine.executor.network.DiscoverySnmpWorker;
import com.topvision.framework.exception.engine.SnmpException;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-6-28-下午08:28:45
 * 
 */
public class CmcSnmpWorker extends DiscoverySnmpWorker<CmcData> {
    private static final long serialVersionUID = -7643728981956535270L;

    /**
     * @param snmpParam
     * @param data
     */
    public CmcSnmpWorker(SnmpParam snmpParam) {
        super(snmpParam);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.engine.executor.network.DiscoverySnmpWorker#exec()
     */
    @Override
    public void exec() {
        super.run();
        try {
            snmpUtil.loadMib("DOCS-IF-MIB");
        } catch (Exception e1) {
            logger.error("", e1);
        }
        try {
            result.setOsVersion(snmpUtil.get("1.3.6.1.2.1.69.1.3.5.0"));
        } catch (Exception e) {
            logger.error("", e);
        }
        try {
            result.setSn(snmpUtil.get("1.3.6.1.2.1.69.1.1.4.0"));
        } catch (Exception e) {
            logger.error("", e);
        }
        result.setHardVersion("Unknown");
        result.setChannelAbility("4DS*16US");
        Map<String, String[][]> userObject = new HashMap<String, String[][]>();
        try {
            userObject.put(
                    "docsIfDownstreamChannelTable",
                    snmpUtil.getTableWithIndex(new String[] { "1.3.6.1.2.1.2.2.1.1", "1.3.6.1.2.1.10.127.1.1.1.1.1",
                            "1.3.6.1.2.1.10.127.1.1.1.1.2", "1.3.6.1.2.1.10.127.1.1.1.1.3",
                            "1.3.6.1.2.1.10.127.1.1.1.1.4", "1.3.6.1.2.1.10.127.1.1.1.1.5",
                            "1.3.6.1.2.1.10.127.1.1.1.1.6", "1.3.6.1.2.1.10.127.1.1.1.1.7" }));
            if (logger.isDebugEnabled() && userObject.get("docsIfDownstreamChannelTable") != null) {
                logger.debug(snmpUtil.table2String((String[][]) userObject.get("docsIfDownstreamChannelTable")));
            }
        } catch (SnmpException e) {
            logger.error("", e);
        }

        try {
            userObject.put(
                    "docsIfUpstreamChannelTable",
                    snmpUtil.getTableWithIndex(new String[] { "1.3.6.1.2.1.2.2.1.1", "1.3.6.1.2.1.10.127.1.1.2.1.1",
                            "1.3.6.1.2.1.10.127.1.1.2.1.2", "1.3.6.1.2.1.10.127.1.1.2.1.3",
                            "1.3.6.1.2.1.10.127.1.1.2.1.4", "1.3.6.1.2.1.10.127.1.1.2.1.5",
                            "1.3.6.1.2.1.10.127.1.1.2.1.6", "1.3.6.1.2.1.10.127.1.1.2.1.7",
                            "1.3.6.1.2.1.10.127.1.1.2.1.8", "1.3.6.1.2.1.10.127.1.1.2.1.9",
                            "1.3.6.1.2.1.10.127.1.1.2.1.10", "1.3.6.1.2.1.10.127.1.1.2.1.11",
                            "1.3.6.1.2.1.10.127.1.1.2.1.12", "1.3.6.1.2.1.10.127.1.1.2.1.13",
                            "1.3.6.1.2.1.10.127.1.1.2.1.14", "1.3.6.1.2.1.10.127.1.1.2.1.15",
                            "1.3.6.1.2.1.10.127.1.1.2.1.16", "1.3.6.1.2.1.10.127.1.1.2.1.17",
                            "1.3.6.1.2.1.10.127.1.1.2.1.18", "1.3.6.1.2.1.10.127.1.1.2.1.19" }));
            if (logger.isDebugEnabled() && userObject.get("docsIfUpstreamChannelTable") != null) {
                logger.debug(snmpUtil.table2String((String[][]) userObject.get("docsIfUpstreamChannelTable")));
            }
        } catch (SnmpException e) {
            logger.error("", e);
        }

        try {
            userObject.put(
                    "docsIfCmtsCmStatusTable",
                    snmpUtil.getTableWithIndex(new String[] { "1.3.6.1.2.1.10.127.1.3.3.1.1",
                            "1.3.6.1.2.1.10.127.1.3.3.1.2", "1.3.6.1.2.1.10.127.1.3.3.1.3",
                            "1.3.6.1.2.1.10.127.1.3.3.1.4", "1.3.6.1.2.1.10.127.1.3.3.1.5",
                            "1.3.6.1.2.1.10.127.1.3.3.1.6", "1.3.6.1.2.1.10.127.1.3.3.1.7",
                            "1.3.6.1.2.1.10.127.1.3.3.1.8", "1.3.6.1.2.1.10.127.1.3.3.1.9",
                            "1.3.6.1.2.1.10.127.1.3.3.1.10", "1.3.6.1.2.1.10.127.1.3.3.1.11",
                            "1.3.6.1.2.1.10.127.1.3.3.1.12", "1.3.6.1.2.1.10.127.1.3.3.1.13",
                            "1.3.6.1.2.1.10.127.1.3.3.1.14", "1.3.6.1.2.1.10.127.1.3.3.1.15",
                            "1.3.6.1.2.1.10.127.1.3.3.1.16", "1.3.6.1.2.1.10.127.1.3.3.1.17",
                            "1.3.6.1.2.1.10.127.1.3.3.1.18", "1.3.6.1.2.1.10.127.1.3.3.1.19",
                            "1.3.6.1.2.1.10.127.1.3.3.1.20", "1.3.6.1.2.1.10.127.1.3.3.1.21",
                            "1.3.6.1.2.1.10.127.1.3.3.1.22" }));
            if (logger.isDebugEnabled() && userObject.get("docsIfCmtsCmStatusTable") != null) {
                logger.debug(snmpUtil.table2String((String[][]) userObject.get("docsIfCmtsCmStatusTable")));
            }
        } catch (SnmpException e) {
            logger.error("", e);
        }

        result.setUserObject(userObject);
    }
}
