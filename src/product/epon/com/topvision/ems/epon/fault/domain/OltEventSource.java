/***********************************************************************
 * $Id: OltEventSource.java,v1.0 2017年1月12日 下午3:58:08 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.domain;

import com.topvision.ems.fault.domain.EventSource;

/**
 * @author vanzand
 * @created @2017年1月12日-下午3:58:08
 *
 */
public class OltEventSource extends EventSource {

    public static final String SOURCE_SPECIAL = "special";
    public static final String SOURCE_OLT = "olt";
    public static final String SOURCE_SLOT = "slot";
    public static final String SOURCE_PORT = "port";
    public static final String SOURCE_ONU = "onu";
    public static final String SOURCE_CMTS = "cmts";
    public static final String SOURCE_ONU_PORT = "onuPort";
    public static final String SOURCE_VLAN = "vlan";
    public static final String SOURCE_ADDITIONALTEXT = "additionalText";

    private String ip;
    private Long index;
    private Long slotNo;
    private Long portNo;
    private Long onuNo;
    private Long onuPortNo;
    private Long vlan;

    private String additionalText;
    private String speical;
    private String onuAlias;

    public OltEventSource(Long entityId) {
        super(entityId);
    }

    private static final Long OFA_SLOT = 30L;// OFA设备告警source
    private static final Long OFA_PUMP1_SLOT = 31L;// OFA pump1告警source
    private static final Long OFA_PUMP2_SLOT = 32L;// OFA pump2告警source
    private static final String OFA = "OFA";
    private static final String OFA_PUMP1 = "OFA:PUMP1";
    private static final String OFA_PUMP2 = "OFA:PUMP2";

    // static {
    // OFA_ALARAM_CODE = new ArrayList<Integer>();
    // OFA_ALARAM_CODE.add(4133);
    // OFA_ALARAM_CODE.add(4134);
    // OFA_ALARAM_CODE.add(4138);
    // OFA_ALARAM_CODE.add(4139);
    // OFA_ALARAM_CODE.add(54400);
    // OFA_ALARAM_CODE.add(54401);
    // OFA_ALARAM_CODE.add(54405);
    // OFA_ALARAM_CODE.add(54406);
    //
    // OFA_PUMP_CODE = new ArrayList<Integer>();
    // OFA_ALARAM_CODE.add(4135);
    // OFA_PUMP_CODE.add(4136);
    // OFA_ALARAM_CODE.add(54402);
    // OFA_PUMP_CODE.add(54403);
    // }

    @Override
    public String formatSource() {
        String sourceStr = "";
        if (source == null) {
            return sourceStr;
        }
        switch (source) {
        case SOURCE_OLT:
            sourceStr = formatOltSource(ip);
            break;
        case SOURCE_SLOT:
            sourceStr = formatSlotSource(slotNo);
            break;
        case SOURCE_PORT:
            sourceStr = formatPortSource(slotNo, portNo);
            break;
        case SOURCE_ONU:
            sourceStr = formatOnuSource(slotNo, portNo, onuNo);
            break;
        case SOURCE_ONU_PORT:
            sourceStr = formatOnuPortSource(slotNo, portNo, onuNo, onuPortNo);
            break;
        case SOURCE_VLAN:
            sourceStr = formatVlanSource(vlan);
            break;
        case SOURCE_ADDITIONALTEXT:
            sourceStr = additionalText;
            break;
        case SOURCE_SPECIAL:
            sourceStr = speical;
            break;
        case SOURCE_CMTS:
            sourceStr = formatCmtsSource(slotNo, portNo, onuNo);
            break;
        }
        return sourceStr;
    }

    private String formatOltSource(String ip) {
        return ip;
    }

    public static String formatSlotSource(Long slotNo) {
        if (OFA_SLOT.equals(slotNo)) {
            return OFA;
        } else if (OFA_PUMP1_SLOT.equals(slotNo)) {
            return OFA_PUMP1;
        } else if (OFA_PUMP2_SLOT.equals(slotNo)) {
            return OFA_PUMP2;
        } else {
            return String.format("SLOT:%d", slotNo);
        }
    }

    public static String formatPortSource(Long slotNo, Long portNo) {
        return String.format("PORT:%d/%d", slotNo, portNo);
    }

    public static String formatOnuSource(Long slotNo, Long portNo, Long onuNo) {
        return String.format("ONU:%d/%d:%d", slotNo, portNo, onuNo);
    }

    public static String formatOnuPortSource(Long slotNo, Long portNo, Long onuNo, Long onuPortNo) {
        return String.format("UNI:%d/%d:%d/%d", slotNo, portNo, onuNo, onuPortNo);
    }

    private String formatVlanSource(Long vlan) {
        return String.format("VLAN IF: %d", vlan);
    }

    private String formatCmtsSource(Long slotNo2, Long portNo2, Long onuNo2) {
        return String.format("CMTS:%d/%d:%d", slotNo, portNo, onuNo);
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    public Long getPortNo() {
        return portNo;
    }

    public void setPortNo(Long portNo) {
        this.portNo = portNo;
    }

    public Long getOnuNo() {
        return onuNo;
    }

    public void setOnuNo(Long onuNo) {
        this.onuNo = onuNo;
    }

    public Long getOnuPortNo() {
        return onuPortNo;
    }

    public void setOnuPortNo(Long onuPortNo) {
        this.onuPortNo = onuPortNo;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSpeical() {
        return speical;
    }

    public void setSpeical(String speical) {
        this.speical = speical;
    }

    public Long getVlan() {
        return vlan;
    }

    public void setVlan(Long vlan) {
        this.vlan = vlan;
    }

    public String getOnuAlias() {
        return onuAlias;
    }

    public void setOnuAlias(String onuAlias) {
        this.onuAlias = onuAlias;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

}
