/***********************************************************************
 * $Id: IgmpForwardingSnooping.java,v1.0 2012-12-18 上午9:53:49 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;

/**
 * @author Bravin
 * @created @2012-12-18-上午9:53:49
 * 
 */
public class TopIgmpForwardingSnooping {
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.5.1.1.1", index = true)
    private Integer mvlanId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.5.1.1.2", index = true)
    private String ip;
    private Integer vid;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.5.1.1.3")
    private String portListString;
    private List<Long> portList;
    private Timestamp lastChangeTime;

    /**
     * 将IGMP SNOOPING的位图转换成List
     * 
     * @param igmpString
     * @return
     */
    public static List<Long> parseSnoopingString2List(String igmpString) {
        // 一共128字节 每个槽位用四个字节表示
        List<Long> portIndexList = new ArrayList<Long>();
        String[] igmpBytes = igmpString.split(":");
        if (igmpBytes.length != 128) {
            return new ArrayList<Long>();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String igmp : igmpBytes) {
            int igmpInt = Integer.parseInt(igmp, 16);
            stringBuilder.append(String.format("%08d", Integer.parseInt(Integer.toBinaryString(igmpInt))));
        }
        String result = stringBuilder.toString();
        int portFlag;
        int slotNum;
        int portNum;
        Long portIndex;
        if (result.indexOf("1") != -1) {
            portFlag = result.indexOf("1");
            slotNum = portFlag / 32 + 1;
            portNum = portFlag % 32 + 1;
            // getPonIndex同样适用于sniIndex
            portIndex = EponIndex.getPonIndex(slotNum, portNum);
            portIndexList.add(portIndex);
            while (result.substring(result.indexOf("1") + 1).indexOf("1") >= 0) {
                result = result.substring(result.indexOf("1") + 1);
                portFlag += result.indexOf("1") + 1;
                slotNum = portFlag / 32 + 1;
                portNum = portFlag % 32 + 1;
                // getPonIndex同样适用于sniIndex
                portIndex = EponIndex.getPonIndex(slotNum, portNum);
                portIndexList.add(portIndex);
            }
        }
        return portIndexList;
    }

    /**
     * @return the entityId
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * @param entityId
     *            the entityId to set
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the mvlanId
     */
    public Integer getMvlanId() {
        return mvlanId;
    }

    /**
     * @param mvlanId
     *            the mvlanId to set
     */
    public void setMvlanId(Integer mvlanId) {
        this.mvlanId = mvlanId;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the vid
     */
    public Integer getVid() {
        return vid;
    }

    /**
     * @param vid
     *            the vid to set
     */
    public void setVid(Integer vid) {
        this.vid = vid;
    }

    /**
     * @return the portListString
     */
    public String getPortListString() {
        return portListString;
    }

    /**
     * @param portListString
     *            the portListString to set
     */
    public void setPortListString(String portListString) {
        this.portList = parseSnoopingString2List(portListString);
        this.portListString = portListString;
    }

    /**
     * @return the portList
     */
    public List<Long> getPortList() {
        return portList;
    }

    /**
     * @param portList
     *            the portList to set
     */
    public void setPortList(List<Long> portList) {
        this.portList = portList;
    }

    /**
     * @return the lastChangeTime
     */
    public Timestamp getLastChangeTime() {
        return lastChangeTime;
    }

    /**
     * @param lastChangeTime
     *            the lastChangeTime to set
     */
    public void setLastChangeTime(Timestamp lastChangeTime) {
        this.lastChangeTime = lastChangeTime;
    }

}
