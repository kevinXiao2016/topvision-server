/***********************************************************************
 * $ VlanTransparentRule.java,v1.0 2012-9-6 11:24:55 $
 *
 * @author: yq
 *
 ***********************************************************************/
package com.topvision.ems.epon.vlan.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author yq
 * @created @2012-9-6-11:24:55
 */
public class VlanTransparentRule implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 2926656152597905884L;

    public static final Integer TAG = 0;
    public static final Integer UNTAG = 1;

    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.5.1.1", index = true)
    private Long deviceIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.5.1.2", index = true)
    private Integer cardNum = 0;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.5.1.3", index = true)
    private Integer portNum = 0;
    private Long entityId;
    private Long slotNo;
    private Long portNo;
    private Long portIndex;
    private Long portId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.5.1.4", writable = true, type = "OctetString")
    private String transparentId;
    private List<Integer> transparentIdList;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.5.1.5", writable = true, type = "OctetString")
    private String transparentMode;
    private List<Integer> transparentModeList;

    /**
     * 行状态
     */
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.7.5.1.6", writable = true, type = "Integer32")
    private Integer pqRowStatus;

    /**
     * @return the deviceIndex
     */
    public Long getDeviceIndex() {
        if (deviceIndex == null) {
            deviceIndex = EponIndex.getOnuMibIndexByIndex(portIndex);
        }
        return deviceIndex;
    }

    /**
     * @param deviceIndex
     *            the deviceIndex to set
     */
    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Long getPortIndex() {
        if (portIndex == null) {
            if (slotNo != null && portNo != null) {
                portIndex = EponIndex.getPonIndex(slotNo.intValue(), portNo.intValue());
            }
        }
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
        deviceIndex = EponIndex.getOnuMibIndexByIndex(portIndex);
        slotNo = EponIndex.getSlotNo(portIndex);
        portNo = EponIndex.getPonNo(portIndex);
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

    public Long getPortId() {
        return portId;
    }

    public void setPortId(Long portId) {
        this.portId = portId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getTransparentId() {
        return transparentId;
    }

    public void setTransparentId(String transparentId) {
        this.transparentId = transparentId;
        if (transparentId != null) {
            transparentIdList = EponUtil.getVlanListFromMib(transparentId);
        }
    }

    public List<Integer> getTransparentIdList() {
        return transparentIdList;
    }

    public void setTransparentIdList(List<Integer> transparentIdList) {
        this.transparentIdList = transparentIdList;
        if (transparentIdList != null) {
            transparentId = EponUtil.getVlanBitMapFormList(transparentIdList);
        }
    }

    public String getTransparentMode() {
        return transparentMode;
    }

    public void setTransparentMode(String transparentMode) {
        this.transparentMode = transparentMode;
        if (transparentMode != null) {
            transparentModeList = EponUtil.getVlanListFromMib(transparentMode);
        }
    }

    public List<Integer> getTransparentModeList() {
        return transparentModeList;
    }

    public void setTransparentModeList(List<Integer> transparentModeList) {
        this.transparentModeList = transparentModeList;
        if (transparentModeList != null) {
            transparentMode = EponUtil.getVlanBitMapFormList(transparentModeList);
        }
    }

    public Integer getPqRowStatus() {
        return pqRowStatus;
    }

    public void setPqRowStatus(Integer pqRowStatus) {
        this.pqRowStatus = pqRowStatus;
    }

    public Integer getCardNum() {
        return cardNum;
    }

    public void setCardNum(Integer cardNum) {
        this.cardNum = 0;
    }

    public Integer getPortNum() {
        return portNum;
    }

    public void setPortNum(Integer portNum) {
        this.portNum = 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("VlanTransparentRule={").append("slotNo=").append(slotNo).append(",portNo=").append(portNo)
                .append(",transparentIdList=").append(transparentIdList).append(",transparentModeList=")
                .append(transparentModeList).append("}");
        return sb.toString();
    }
}
