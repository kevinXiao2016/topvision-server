/***********************************************************************
 * $ IgmpControlledMulticastUserAuthorityTable.java,v1.0 2011-11-23 9:11:50 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import java.util.List;

import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-23-9:11:50
 */
@TableProperty(tables = { "default" })
public class IgmpControlledMulticastUserAuthorityTable implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * 设备索引号 For OLT, set to corresponding device/slot/port For ONU, set to 0
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.1.1.1", index = true)
    private Long deviceIndex;
    /**
     * 板卡索引号 For OLT, set to 0 For ONU, set to corresponding slot
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.1.1.2", index = true)
    private Long slotNo;
    /**
     * 端口索引号 For OLT, set to 0 For ONU, set to corresponding port
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.1.1.3", index = true)
    private Long portNo;
    private Long portIndex;
    /**
     * 可控组播组列表. 用法类似于cmProxyList，每个bit对应于cmIndex OCTET STRING (SIZE (0..250))
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.1.1.4", writable = true, type = "OctetString")
    private String multicastPackageList;
    private List<Integer> multicastPackageListNum;
    /**
     * IGMP全局带宽池大小 - 该端口支持的最大组播带宽 单位：Kbps INTEGER (0..4294967295)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.1.1.5", writable = true, type = "Integer32")
    private Long igmpGlobalBW;
    /**
     * IGMP全局带宽池当前使用大小 - 该端口当前实际使用的组播带宽 单位：Kbps INTEGER (0..4294967295)
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.1.1.6")
    private Long igmpGlobalBWUsed;
    /**
     * 行创建 INTEGER { active(1), notInService(2), notReady(3), createAndGo(4), createAndWait(5),
     * destroy(6) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.1.1.7", writable = true, type = "Integer32")
    private Integer cmUserAuthorityRowStatus;

    public Long getPortIndex() {
        if (portIndex == null) {
            if (EponIndex.getOnuNoByMibDeviceIndex(deviceIndex) == 0) {
                // 此时代表为OLT
                portIndex = EponIndex.getPonIndexByMibDeviceIndex(deviceIndex);
            } else {
                portIndex = EponIndex.getUniIndexByMibIndex(deviceIndex, slotNo, portNo);
            }
        }
        return portIndex;
    }

    public void setPortIndex(Long portIndex) {
        this.portIndex = portIndex;
        Long onuNo = EponIndex.getOnuNo(portIndex);
        if (onuNo == 0) {
            deviceIndex = EponIndex.getOnuMibIndexByIndex(portIndex);
            slotNo = EponIndex.getSlotNo(portIndex);
            portNo = EponIndex.getPonNo(portIndex);
        } else {
            deviceIndex = EponIndex.getOnuMibIndexByIndex(portIndex);
            slotNo = EponIndex.getOnuCardNo(portIndex);
            portNo = EponIndex.getUniNo(portIndex);
        }
    }

    public Integer getCmUserAuthorityRowStatus() {
        return cmUserAuthorityRowStatus;
    }

    public void setCmUserAuthorityRowStatus(Integer cmUserAuthorityRowStatus) {
        this.cmUserAuthorityRowStatus = cmUserAuthorityRowStatus;
    }

    public Long getDeviceIndex() {
        return deviceIndex;
    }

    public void setDeviceIndex(Long deviceIndex) {
        this.deviceIndex = deviceIndex;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getIgmpGlobalBW() {
        return igmpGlobalBW;
    }

    public void setIgmpGlobalBW(Long igmpGlobalBW) {
        this.igmpGlobalBW = igmpGlobalBW;
    }

    public Long getIgmpGlobalBWUsed() {
        return igmpGlobalBWUsed;
    }

    public void setIgmpGlobalBWUsed(Long igmpGlobalBWUsed) {
        this.igmpGlobalBWUsed = igmpGlobalBWUsed;
    }

    public String getMulticastPackageList() {
        return multicastPackageList;
    }

    public void setMulticastPackageList(String multicastPackageList) {
        this.multicastPackageList = multicastPackageList;
        // 此处位图处理方法同cmProxyList
        multicastPackageListNum = EponUtil.getCmProxyListFromMib(multicastPackageList);
    }

    /**
     * @return the multicastPackageListNum
     */
    public List<Integer> getMulticastPackageListNum() {
        return multicastPackageListNum;
    }

    /**
     * @param multicastPackageListNum
     *            the multicastPackageListNum to set
     */
    public void setMulticastPackageListNum(List<Integer> multicastPackageListNum) {
        this.multicastPackageListNum = multicastPackageListNum;
        // 此处位图处理方法同cmProxyList
        multicastPackageList = EponUtil.getBitMapFormProxyList(multicastPackageListNum);
    }

    public Long getPortNo() {
        return portNo;
    }

    public void setPortNo(Long portNo) {
        this.portNo = portNo;
    }

    public Long getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(Long slotNo) {
        this.slotNo = slotNo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IgmpControlledMulticastUserAuthorityTable");
        sb.append("{cmUserAuthorityRowStatus=").append(cmUserAuthorityRowStatus);
        sb.append(", entityId=").append(entityId);
        sb.append(", deviceIndex=").append(deviceIndex);
        sb.append(", slotNo=").append(slotNo);
        sb.append(", portNo=").append(portNo);
        sb.append(", portIndex=").append(portIndex);
        sb.append(", multicastPackageList='").append(multicastPackageList).append('\'');
        sb.append(", igmpGlobalBW=").append(igmpGlobalBW);
        sb.append(", igmpGlobalBWUsed=").append(igmpGlobalBWUsed);
        sb.append('}');
        return sb.toString();
    }
}
