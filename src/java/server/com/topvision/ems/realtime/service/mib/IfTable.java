/***********************************************************************
 * $Id: IfTable.java,v 1.1 2009-10-5 上午11:17:44 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.realtime.service.mib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.network.util.PortUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Create Date 2009-10-5 上午11:17:44
 * 
 * @author kelers
 * 
 */
public class IfTable extends AbstractMibValue {
    protected static final Logger logger = LoggerFactory.getLogger(IfTable.class);

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.realtime.service.mib.AbstractMibValue#getData(long)
     */
    @Override
    public JSONObject getData(long entityId) {
        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject temp = null;
        try {
            String[][] values = getTable(entityId, "ifTable");
            if (values == null || values.length == 0) {
                json.put("rowCount", 0);
            } else {
                json.put("rowCount", values.length);
                for (int i = 0; i < values.length; i++) {
                    temp = new JSONObject();
                    for (int j = 0; j < values[i].length; j++) {
                        if (j == 2) {
                            temp.put("header" + j, PortUtil.getIfTypeString(Integer.parseInt(values[i][j])));
                        } else if (j == 4) {
                            temp.put("header" + j, PortUtil.getIfSpeedString(Long.parseLong(values[i][j])));
                        } else if (j == 6) {
                            temp.put("header" + j, PortUtil.getIfAdminStatusString(Byte.parseByte(values[i][j])));
                        } else if (j == 7) {
                            temp.put("header" + j, PortUtil.getIfOperStatusString(Byte.parseByte(values[i][j])));
                        } else if (j == 9 || j == 15) {
                            temp.put("header" + j, PortUtil.getIfOctetsString(Long.parseLong(values[i][j])));
                        } else {
                            temp.put("header" + j, values[i][j]);
                        }
                    }
                    array.add(temp);
                }
                json.put("data", array);
            }
        } catch (Exception ex) {
            logger.debug("get Data.", ex);
        }
        return json;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.realtime.service.mib.AbstractMibValue#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return getString("IfTable");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.realtime.service.mib.AbstractMibValue#getHeaders()
     */
    @Override
    public String[] getHeaders() {
        return new String[] { getString("ifIndex"), getString("ifDescr"), getString("ifType"), getString("ifMtu"),
                getString("ifSpeed"), getString("ifPhysAddress"), getString("ifAdminStatus"),
                getString("ifOperStatus"), getString("ifLastChange"), getString("ifInOctets"),
                getString("ifInUcastPkts"), getString("ifInNUcastPkts"), getString("ifInDiscards"),
                getString("ifInErrors"), getString("ifInUnknownProtos"), getString("ifOutOctets"),
                getString("ifOutUcastPkts"), getString("ifOutNUcastPkts"), getString("ifOutDiscards"),
                getString("ifOutErrors"), getString("ifOutQLen"), getString("ifSpecific") };
    }
}
