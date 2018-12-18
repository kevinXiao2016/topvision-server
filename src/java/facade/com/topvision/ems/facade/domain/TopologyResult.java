/***********************************************************************
 * $Id: TopologyResult.java,v 1.1 May 5, 2008 4:32:04 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.facade.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.topvision.framework.snmp.SnmpParam;

/**
 * @Create Date May 5, 2008 4:32:04 PM
 * 
 * @author kelers
 * 
 */
public class TopologyResult implements Serializable {
    private static final long serialVersionUID = -1389433015890804409L;
    private long entityId = 0;
    private String ip = null;
    private String mac = null;
    private String hostName = null;
    private SnmpParam snmpParam = null;
    // 连接计算原始数据，目前没有保存数据库，建议保存到数据库
    private String[] system = null;
    private Map<String, String> ipNetToMediaMap = null;
    private String dot1dBaseBridgeAddress = null;
    private Map<String, String> fdbTableMap = null;
    private List<String> macList = new ArrayList<String>();
    private String[][] ifTable = null;
    private String[][] ifXTable = null;
    private String[][] atTable = null;
    private String[][] ipAddrTable = null;
    private String[][] ipRouteTable = null;
    private String error = null;
    private boolean isSeed = false;
    private long discoveryTime = 0;
    private Object userObject = null;

    /**
     * @param mac
     *            the mac to add
     */
    public void addMac(String mac) {
        if (!macList.contains(mac)) {
            macList.add(mac);
        }
    }

    /**
     * @param macList
     *            the macList to set
     */
    public void addMacList(List<String> macList) {
        getMacList().addAll(macList);
    }

    /**
     * @return the atTable
     */
    public String[][] getAtTable() {
        return atTable;
    }

    /**
     * @return the discoveryTime
     */
    public long getDiscoveryTime() {
        return discoveryTime;
    }

    /**
     * @return the dot1dBaseBridgeAddress
     */
    public String getDot1dBaseBridgeAddress() {
        return dot1dBaseBridgeAddress;
    }

    /**
     * @return the entityId
     */
    public long getEntityId() {
        return entityId;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @return the fdbTableMap
     */
    public Map<String, String> getFdbTableMap() {
        return fdbTableMap;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @return the ifTable
     */
    public String[][] getIfTable() {
        return ifTable;
    }

    /**
     * @return the ifXTable
     */
    public String[][] getIfXTable() {
        return ifXTable;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @return the ipAddrTable
     */
    public String[][] getIpAddrTable() {
        return ipAddrTable;
    }

    /**
     * @return the ipNetToMediaMap
     */
    public Map<String, String> getIpNetToMediaMap() {
        return ipNetToMediaMap;
    }

    /**
     * @return the ipRouteTable
     */
    public String[][] getIpRouteTable() {
        return ipRouteTable;
    }

    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @return the macList
     */
    public List<String> getMacList() {
        return macList;
    }

    /**
     * @return the snmpParam
     */
    public SnmpParam getSnmpParam() {
        return snmpParam;
    }

    /**
     * @return the system
     */
    public String[] getSystem() {
        return system;
    }

    /**
     * @return the userObject
     */
    public Object getUserObject() {
        return userObject;
    }

    /**
     * @return the isSeed
     */
    public boolean isSeed() {
        return isSeed;
    }

    /**
     * @param atTable
     *            the atTable to set
     */
    public void setAtTable(String[][] atTable) {
        this.atTable = atTable;
    }

    /**
     * @param discoveryTime
     *            the discoveryTime to set
     */
    public void setDiscoveryTime(long discoveryTime) {
        this.discoveryTime = discoveryTime;
    }

    /**
     * @param dot1dBaseBridgeAddress
     *            the dot1dBaseBridgeAddress to set
     */
    public void setDot1dBaseBridgeAddress(String dot1dBaseBridgeAddress) {
        this.dot1dBaseBridgeAddress = dot1dBaseBridgeAddress;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @param fdbTableMap
     *            the fdbTableMap to set
     */
    public void setFdbTableMap(Map<String, String> fdbTableMap) {
        this.fdbTableMap = fdbTableMap;
    }

    /**
     * @param hostName
     *            the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @param ifTable
     *            the ifTable to set
     */
    public void setIfTable(String[][] ifTable) {
        this.ifTable = ifTable;
    }

    /**
     * @param ifXTable
     *            the ifXTable to set
     */
    public void setIfXTable(String[][] ifXTable) {
        this.ifXTable = ifXTable;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @param ipAddrTable
     *            the ipAddrTable to set
     */
    public void setIpAddrTable(String[][] ipAddrTable) {
        this.ipAddrTable = ipAddrTable;
    }

    /**
     * @param ipNetToMediaMap
     *            the ipNetToMediaMap to set
     */
    public void setIpNetToMediaMap(Map<String, String> ipNetToMediaMap) {
        this.ipNetToMediaMap = ipNetToMediaMap;
    }

    /**
     * @param ipRouteTable
     *            the ipRouteTable to set
     */
    public void setIpRouteTable(String[][] ipRouteTable) {
        this.ipRouteTable = ipRouteTable;
    }

    /**
     * @param mac
     *            the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @param isSeed
     *            the isSeed to set
     */
    public void setSeed(boolean isSeed) {
        this.isSeed = isSeed;
    }

    /**
     * @param snmpParam
     *            the snmpParam to set
     */
    public void setSnmpParam(SnmpParam snmpParam) {
        this.snmpParam = snmpParam;
    }

    /**
     * @param system
     *            the system to set
     */
    public void setSystem(String[] system) {
        this.system = system;
    }

    /**
     * @param userObject
     *            the userObject to set
     */
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    @Override
    public String toString() {
        StringBuilder data = new StringBuilder();
        data.append(ip).append("-").append(hostName).append("(").append(entityId).append(")\n");
        data.append("Discover Time:").append(DateFormat.getDateTimeInstance().format(new Date(discoveryTime)))
                .append("\n");
        // private String[] system = null;
        if (system == null || system.length == 0) {
            data.append("system:null\n");
        } else {
            data.append("system:\n");
            for (String s : system) {
                data.append("\t").append(s).append("\n");
            }
        }
        // private Map<String, String> ipNetToMediaMap = null;
        if (ipNetToMediaMap == null || ipNetToMediaMap.isEmpty()) {
            data.append("ipNetToMediaMap:null\n");
        } else {
            data.append("ipNetToMediaMap:\n");
            for (String s : ipNetToMediaMap.keySet()) {
                data.append("\t").append(s).append("<=>").append(ipNetToMediaMap.get(s)).append("\n");
            }
        }
        // private String dot1dBaseBridgeAddress = null;
        data.append("dot1dBaseBridgeAddress:").append(dot1dBaseBridgeAddress).append("\n");
        // private Map<String, String> fdbTableMap = null;
        if (fdbTableMap == null || fdbTableMap.isEmpty()) {
            data.append("fdbTableMap:null\n");
        } else {
            data.append("fdbTableMap:\n");
            for (String s : fdbTableMap.keySet()) {
                data.append("\t").append(s).append("<=>").append(fdbTableMap.get(s)).append("\n");
            }
        }
        // private List<String> macList = new ArrayList<String>();
        if (macList == null || macList.isEmpty()) {
            data.append("macList:null\n");
        } else {
            data.append("macList:");
            for (String s : macList) {
                data.append(s).append(";");
            }
            data.append("\n");
        }
        // private String[][] ifTable = null;
        if (ifTable == null || ifTable.length == 0) {
            data.append("ifTable:null\n");
        } else {
            data.append("ifTable:\n");
            for (String s[] : ifTable) {
                if (s == null || s.length == 0) {
                    continue;
                }
                data.append("\t");
                for (String ss : s) {
                    data.append(ss).append(";");
                }
                data.append("\n");
            }
        }
        // private String[][] ifXTable = null;
        if (ifXTable == null || ifXTable.length == 0) {
            data.append("ifXTable:null\n");
        } else {
            data.append("ifXTable:\n");
            for (String s[] : ifXTable) {
                if (s == null || s.length == 0) {
                    continue;
                }
                data.append("\t");
                for (String ss : s) {
                    data.append(ss).append(";");
                }
                data.append("\n");
            }
        }
        // private String[][] atTable = null;
        if (atTable == null || atTable.length == 0) {
            data.append("atTable:null\n");
        } else {
            data.append("atTable:\n");
            for (String s[] : atTable) {
                if (s == null || s.length == 0) {
                    continue;
                }
                data.append("\t");
                for (String ss : s) {
                    data.append(ss).append(";");
                }
                data.append("\n");
            }
        }
        // private String[][] ipAddrTable = null;
        if (ipAddrTable == null || ipAddrTable.length == 0) {
            data.append("ipAddrTable:null\n");
        } else {
            data.append("ipAddrTable:\n");
            for (String s[] : ipAddrTable) {
                if (s == null || s.length == 0) {
                    continue;
                }
                data.append("\t");
                for (String ss : s) {
                    data.append(ss).append(";");
                }
                data.append("\n");
            }
        }
        // private String[][] ipRouteTable = null;
        if (ipRouteTable == null || ipRouteTable.length == 0) {
            data.append("ipRouteTable:null\n");
        } else {
            data.append("ipRouteTable:\n");
            for (String s[] : ipRouteTable) {
                if (s == null || s.length == 0) {
                    continue;
                }
                data.append("\t");
                for (String ss : s) {
                    data.append(ss).append(";");
                }
                data.append("\n");
            }
        }
        // private String error = null;
        data.append("error:").append(error).append("\n");
        return data.toString();
    }
}
