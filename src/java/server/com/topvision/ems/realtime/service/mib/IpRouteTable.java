/***********************************************************************
 * $Id: IpRouteTable.java,v 1.1 2009-10-5 上午10:21:43 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.realtime.service.mib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @Create Date 2009-10-5 上午10:21:43
 * 
 * @author kelers
 * 
 */
public class IpRouteTable extends AbstractMibValue {
    protected static final Logger logger = LoggerFactory.getLogger(IpRouteTable.class);

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
            String[][] values = getTable(entityId, "ipRouteTable");
            if (values == null || values.length == 0) {
                json.put("rowCount", 0);
            } else {
                json.put("rowCount", values.length);
                for (int i = 0; i < values.length; i++) {
                    temp = new JSONObject();
                    for (int j = 0; j < values[i].length; j++) {
                        if (j == 7) {
                            if (values[i][j].equals("1")) {
                                temp.put("header" + j, "other");
                            } else if (values[i][j].equals("2")) {
                                temp.put("header" + j, "invalid");
                            } else if (values[i][j].equals("3")) {
                                temp.put("header" + j, "direct");
                            } else if (values[i][j].equals("4")) {
                                temp.put("header" + j, "indirect");
                            } else {
                                temp.put("header" + j, values[i][j]);
                            }
                        } else if (j == 8) {
                            if (values[i][j].equals("1")) {
                                temp.put("header" + j, "other");
                            } else if (values[i][j].equals("2")) {
                                temp.put("header" + j, "local");
                            } else if (values[i][j].equals("3")) {
                                temp.put("header" + j, "netmgmt");
                            } else if (values[i][j].equals("4")) {
                                temp.put("header" + j, "icmp");
                            } else if (values[i][j].equals("5")) {
                                temp.put("header" + j, "egp");
                            } else if (values[i][j].equals("6")) {
                                temp.put("header" + j, "ggp");
                            } else if (values[i][j].equals("7")) {
                                temp.put("header" + j, "hello");
                            } else if (values[i][j].equals("8")) {
                                temp.put("header" + j, "rip");
                            } else if (values[i][j].equals("9")) {
                                temp.put("header" + j, "is-is");
                            } else if (values[i][j].equals("10")) {
                                temp.put("header" + j, "es-is");
                            } else if (values[i][j].equals("11")) {
                                temp.put("header" + j, "ciscoIgrp");
                            } else if (values[i][j].equals("12")) {
                                temp.put("header" + j, "bbnSpfIgp");
                            } else if (values[i][j].equals("13")) {
                                temp.put("header" + j, "ospf");
                            } else if (values[i][j].equals("14")) {
                                temp.put("header" + j, "bgp");
                            } else {
                                temp.put("header" + j, values[i][j]);
                            }
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
        return getString("IpRouteTable");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.realtime.service.mib.AbstractMibValue#getHeaders()
     */
    @Override
    public String[] getHeaders() {
        return new String[] { getString("ipRouteDest"), getString("ipRouteIfIndex"), getString("ipRouteMetric1"),
                getString("ipRouteMetric2"), getString("ipRouteMetric3"), getString("ipRouteMetric4"),
                getString("ipRouteNextHop"), getString("ipRouteType"), getString("ipRouteProto"),
                getString("ipRouteAge"), getString("ipRouteMask"), getString("ipRouteMetric5"),
                getString("ipRouteInfo") };
    }
}
