/***********************************************************************
 * $Id: OltSysOpticalAlarm.java,v1.0 2013-8-28 下午3:19:32 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.optical.domain;

import java.io.Serializable;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2013-8-28-下午3:19:32
 *
 */
public class OltSysOpticalAlarm implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 9042780489194077137L;

    public static final Integer E_ALM_PORT_SFP_RXPWR_HIGH = 0x3011;/*!<SFP接收光功率超过上限 CTC*/
    public static final Integer E_ALM_PORT_SFP_RXPWR_LOW = 0x3012;/*!<SFP接收光功率低于下限 CTC*/
    public static final Integer E_ALM_PORT_SFP_TXPWR_HIGH = 0x3013;/*!<SFP发送光功率超过上限 CTC*/
    public static final Integer E_ALM_PORT_SFP_TXPWR_LOW = 0x3014;/*!<SFP发送光功率低于下限 CTC*/

    public static final Integer E_ALM_ONU_US_RXPWR_LOW = 0x4005;/*!<ONU上行发送光功率过低 NSCRTV*/
    public static final Integer E_ALM_ONU_US_RXPWR_HIGH = 0x4006;/*!<ONU上行发送光功率过高 NSCRTV*/
    public static final Integer E_ALM_ONU_DS_RXPWR_LOW = 0x4007;/*!<ONU下行接收光功率过低 NSCRTV*/
    public static final Integer E_ALM_ONU_DS_RXPWR_HIGH = 0x4008;/*!<ONU下行接收光功率过高 NSCRTV*/

    public static final Integer E_ALM_ONU_RXPWR_LOW = 0x3014;
    public static final Integer E_ALM_ONU_RXPWR_HIGH = 0x3013;
    public static final Integer E_ALM_ONU_TXPWR_LOW = 0x3012;
    public static final Integer E_ALM_ONU_TXPWR_HIGH = 0x3011;
    public static final Integer E_ALM_PORT_RXPWR_HIGH = 0x4006;
    public static final Integer E_ALM_PORT_RXPWR_LOW = 0x4005;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.9.1.1.1", index = true)
    private Integer topSysOptAlarmIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.9.1.1.2", writable = true, type = "Integer32")
    private Integer topSysOptAlarmSwitch;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.1.9.1.1.3", writable = true, type = "Integer32")
    private Integer topSysOptAlarmSoapTime;
    private Long entityId;

    public Integer getTopSysOptAlarmIndex() {
        return topSysOptAlarmIndex;
    }

    public void setTopSysOptAlarmIndex(Integer topSysOptAlarmIndex) {
        this.topSysOptAlarmIndex = topSysOptAlarmIndex;
    }

    public Integer getTopSysOptAlarmSwitch() {
        return topSysOptAlarmSwitch;
    }

    public void setTopSysOptAlarmSwitch(Integer topSysOptAlarmSwitch) {
        this.topSysOptAlarmSwitch = topSysOptAlarmSwitch;
    }

    public Integer getTopSysOptAlarmSoapTime() {
        return topSysOptAlarmSoapTime;
    }

    public void setTopSysOptAlarmSoapTime(Integer topSysOptAlarmSoapTime) {
        this.topSysOptAlarmSoapTime = topSysOptAlarmSoapTime;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSysOpticalAlarm [topSysOptAlarmIndex=");
        builder.append(topSysOptAlarmIndex);
        builder.append(", topSysOptAlarmSwitch=");
        builder.append(topSysOptAlarmSwitch);
        builder.append(", topSysOptAlarmSoapTime=");
        builder.append(topSysOptAlarmSoapTime);
        builder.append("]");
        return builder.toString();
    }

}
