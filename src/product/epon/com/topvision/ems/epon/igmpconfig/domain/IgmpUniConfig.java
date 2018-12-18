/***********************************************************************
 * $Id: IgmpUniConfig.java,v1.0 2016-6-7 下午5:24:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.domain;

import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2016-6-7-下午5:24:47
 * UNI口IGMP配置
 */
public class IgmpUniConfig implements AliasesSuperType {
    private static final long serialVersionUID = 3906235392019163861L;

    private Long entityId;
    private Long onuId;
    private Long uniIndex;
    //端口类型
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.1", index = true)
    private Integer portType;
    //板卡索引号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.2", index = true)
    private Integer slotNo;
    //pon口索引号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.3", index = true)
    private Integer ponNo;
    //onu认证索引号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.4", index = true)
    private Integer onuNo;
    //uni口索引号
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.5", index = true)
    private Integer uniNo;
    //最大组播数
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.6", writable = true, type = "Integer32")
    private Integer maxGroupNum;
    //组播vlan序列
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.7", writable = true, type = "OctetString")
    private String vlanList;
    private List<Integer> uniVlanArray;
    //组播vlan模式
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.2.2.1.8", writable = true, type = "Integer32")
    private Integer vlanMode;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getOnuId() {
        return onuId;
    }

    public void setOnuId(Long onuId) {
        this.onuId = onuId;
    }

    public Long getUniIndex() {
        if (uniIndex == null) {
            uniIndex = EponIndex.getUniIndex(slotNo, ponNo, onuNo, uniNo);
        }
        return uniIndex;
    }

    public void setUniIndex(Long uniIndex) {
        this.uniIndex = uniIndex;
    }

    public Integer getPortType() {
        return portType;
    }

    public void setPortType(Integer portType) {
        this.portType = portType;
    }

    public Integer getSlotNo() {
        if (slotNo == null && uniIndex != null) {
            slotNo = EponIndex.getSlotNo(uniIndex).intValue();
        }
        return slotNo;
    }

    public void setSlotNo(Integer slotNo) {
        this.slotNo = slotNo;
    }

    public Integer getPonNo() {
        if (ponNo == null && uniIndex != null) {
            ponNo = EponIndex.getPonNo(uniIndex).intValue();
        }
        return ponNo;
    }

    public void setPonNo(Integer ponNo) {
        this.ponNo = ponNo;
    }

    public Integer getOnuNo() {
        if (onuNo == null && uniIndex != null) {
            onuNo = EponIndex.getOnuNo(uniIndex).intValue();
        }
        return onuNo;
    }

    public void setOnuNo(Integer onuNo) {
        this.onuNo = onuNo;
    }

    public Integer getUniNo() {
        if (uniNo == null && uniIndex != null) {
            uniNo = EponIndex.getUniNo(uniIndex).intValue();
        }
        return uniNo;
    }

    public void setUniNo(Integer uniNo) {
        this.uniNo = uniNo;
    }

    public Integer getMaxGroupNum() {
        return maxGroupNum;
    }

    public void setMaxGroupNum(Integer maxGroupNum) {
        this.maxGroupNum = maxGroupNum;
    }

    public String getVlanList() {
        return vlanList;
    }

    public void setVlanList(String vlanList) {
        this.vlanList = vlanList;
    }

    public Integer getVlanMode() {
        return vlanMode;
    }

    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
    }

    public List<Integer> getUniVlanArray() {
        if (vlanList != null) {
            this.uniVlanArray = EponUtil.getTwiceBitValueFromOcterString(vlanList);
        }
        return uniVlanArray;
    }

    public void setUniVlanArray(List<Integer> uniVlanArray) {
        this.uniVlanArray = uniVlanArray;
        if (uniVlanArray != null) {
            this.vlanList = EponUtil.getOcterStringFromTwoByteValueList(uniVlanArray, 32);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpUniConfig [entityId=");
        builder.append(entityId);
        builder.append(", onuId=");
        builder.append(onuId);
        builder.append(", uniIndex=");
        builder.append(uniIndex);
        builder.append(", portType=");
        builder.append(portType);
        builder.append(", slotNo=");
        builder.append(slotNo);
        builder.append(", ponNo=");
        builder.append(ponNo);
        builder.append(", onuNo=");
        builder.append(onuNo);
        builder.append(", uniNo=");
        builder.append(uniNo);
        builder.append(", maxGroupNum=");
        builder.append(maxGroupNum);
        builder.append(", vlanList=");
        builder.append(vlanList);
        builder.append(", vlanMode=");
        builder.append(vlanMode);
        builder.append("]");
        return builder.toString();
    }

}
