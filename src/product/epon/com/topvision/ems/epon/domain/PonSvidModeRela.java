/***********************************************************************
 * $Id: PonSvidModeRela.java,v1.0 2011-10-31 上午10:16:01 $
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
 * PON口SVID与VLNA模式关系
 * 
 * @author zhanglongyang
 * @created @2011-10-31-上午10:16:01
 * 
 */
public class PonSvidModeRela implements Serializable, AliasesSuperType {
    private static final long serialVersionUID = -4128735181934553092L;
    private Long entityId;
    private Long ponId;
    private Long ponIndex;
    private Long onuMac;
    private Integer svid;
    private List<Integer> svidList;
    private String svidString;
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
     * @return the svidList
     */
    public List<Integer> getSvidList() {
        return svidList;
    }

    /**
     * @param svidList
     *            the svidList to set
     */
    public void setSvidList(List<Integer> svidList) {
        this.svidList = svidList;
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
     * @return the svidString
     */
    public String getSvidString() {
        return svidString;
    }

    /**
     * @param svidString
     *            the svidString to set
     */
    public void setSvidString(String svidString) {
        this.svidString = svidString;
        if (svidString != null) {
            svidList = new ArrayList<Integer>();
            if (svidString.indexOf(":") > 0) {
                // 聚合/Trunk模式
                svidList = EponUtil.getVlanListFromMib(svidString);
            } else if (svidString.indexOf("-") > 0) {
                // QinQ模式
                String[] range = svidString.split("-");
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);
                for (int i = 0; i < end - start + 1; i++) {
                    svidList.add(start + i);
                }
            } else {
                // 转换模式
                svidList.add(Integer.parseInt(svidString));
            }
        }
    }

    /**
     * @return the svid
     */
    public Integer getSvid() {
        return svid;
    }

    /**
     * @param svid
     *            the svid to set
     */
    public void setSvid(Integer svid) {
        this.svid = svid;
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PonSvidModeRela{");
        sb.append("entityId=").append(entityId);
        sb.append(", ponId=").append(ponId);
        sb.append(", ponIndex=").append(ponIndex);
        sb.append(", onuMac=").append(onuMac);
        sb.append(", svid=").append(svid);
        sb.append(", svidList=").append(svidList);
        sb.append(", svidString=").append(svidString);
        sb.append(", vlanMode=").append(vlanMode);
        sb.append('}');
        return sb.toString();
    }
}
