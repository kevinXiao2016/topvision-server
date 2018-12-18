/***********************************************************************
 * $Id: PonCvidModeRela.java,v1.0 2011-10-31 上午10:11:44 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * PON口CVID与VLNA模式关系
 * 
 * @author zhanglongyang
 * @created @2011-10-31-上午10:11:44
 * 
 */
public class PonCvidModeRela implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = 6204175389680717451L;
    private Long entityId;
    private Long ponId;
    private Long ponIndex;
    private Long onuMac;
    private Integer cvid;
    private List<Integer> cvidList;
    private String cvidString;
    private Integer vlanMode;
    private String onuMacAddress;

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
     * @return the ponId
     */
    public Long getPonId() {
        return ponId;
    }

    /**
     * @param ponId
     *            the ponId to set
     */
    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    /**
     * @return the onuMac
     */
    public Long getOnuMac() {
        return onuMac;
    }

    /**
     * @param onuMac
     *            the onuMac to set
     */
    public void setOnuMac(Long onuMac) {
        this.onuMac = onuMac;
    }

    /**
     * @return the onuMacAddress
     */
    public String getOnuMacAddress() {
        return onuMacAddress;
    }

    /**
     * @param onuMacAddress
     *            the onuMacAddress to set
     */
    public void setOnuMacAddress(String onuMacAddress) {
        this.onuMacAddress = onuMacAddress;
    }

    /**
     * @return the cvidList
     */
    public List<Integer> getCvidList() {
        return cvidList;
    }

    /**
     * @param cvidList
     *            the cvidList to set
     */
    public void setCvidList(List<Integer> cvidList) {
        this.cvidList = cvidList;
    }

    /**
     * @return the vlanMode
     */
    public Integer getVlanMode() {
        return vlanMode;
    }

    /**
     * @param vlanMode
     *            the vlanMode to set
     */
    public void setVlanMode(Integer vlanMode) {
        this.vlanMode = vlanMode;
    }

    /**
     * @return the cvidString
     */
    public String getCvidString() {
        return cvidString;
    }

    /**
     * @param cvidString
     *            the cvidString to set
     */
    public void setCvidString(String cvidString) {
        this.cvidString = cvidString;
        if (cvidString != null) {
            cvidList = new ArrayList<Integer>();
            if (cvidString.indexOf(":") > 0) {
                // 聚合/Trunk模式
                cvidList = EponUtil.getVlanListFromMib(cvidString);
            } else if (cvidString.indexOf("-") > 0) {
                // QinQ模式
                String[] range = cvidString.split("-");
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                for (int i = 0; i < end - start + 1; i++) {
                    cvidList.add(start + i);
                }
            } else {
                // 转换模式
                cvidList.add(Integer.parseInt(cvidString));
            }
        }
    }

    /**
     * @return the ponIndex
     */
    public Long getPonIndex() {
        return ponIndex;
    }

    /**
     * @param ponIndex
     *            the ponIndex to set
     */
    public void setPonIndex(Long ponIndex) {
        this.ponIndex = ponIndex;
    }

    /**
     * @return the cvid
     */
    public Integer getCvid() {
        return cvid;
    }

    /**
     * @param cvid
     *            the cvid to set
     */
    public void setCvid(Integer cvid) {
        this.cvid = cvid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PonCvidModeRela{");
        sb.append("cvidList=").append(cvidList);
        sb.append(", entityId=").append(entityId);
        sb.append(", ponId=").append(ponId);
        sb.append(", ponIndex=").append(ponIndex);
        sb.append(", onuMac=").append(onuMac);
        sb.append(", ponIndex=").append(ponIndex);
        sb.append(", cvid=").append(cvid);
        sb.append(", cvidString='").append(cvidString).append('\'');
        sb.append(", vlanMode=").append(vlanMode);
        sb.append('}');
        return sb.toString();
    }
}
