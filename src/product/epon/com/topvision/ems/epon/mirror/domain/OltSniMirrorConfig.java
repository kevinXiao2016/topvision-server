/***********************************************************************
 * $Id: OltSniMirrorConfig.java,v1.0 2011-11-5 下午02:18:49 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.mirror.domain;

import java.io.Serializable;
import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author huqiao
 * @created @2011-11-5-下午02:18:49
 * 
 */
public class OltSniMirrorConfig implements Serializable, AliasesSuperType {

    private static final long serialVersionUID = -995331476418993733L;
    private Long entityId;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.3.1.1", index = true)
    private Integer sniMirrorGroupIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.3.1.2", writable = true, type = "OctetString")
    private String sniMirrorGroupName;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.3.1.3", writable = true, type = "OctetString")
    private String sniMirrorGroupDstPortList;
    private List<Long> sniMirrorGroupDstPortIndexList;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.3.1.4", writable = true, type = "OctetString")
    private String sniMirrorGroupSrcInPortList;
    private List<Long> sniMirrorGroupSrcInPortIndexList;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.3.1.5", writable = true, type = "OctetString")
    private String sniMirrorGroupSrcOutPortList;
    private List<Long> sniMirrorGroupSrcOutPortIndexList;
    @SnmpProperty(oid = "1.3.6.1.4.1.17409.2.3.2.3.1.6", writable = true, type = "Integer32")
    private Integer sniMirrorGroupRowstatus;
    //目的端口名称
    private String destPortName;

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
     * @return the sniMirrorGroupIndex
     */
    public Integer getSniMirrorGroupIndex() {
        return sniMirrorGroupIndex;
    }

    /**
     * @param sniMirrorGroupIndex
     *            the sniMirrorGroupIndex to set
     */
    public void setSniMirrorGroupIndex(Integer sniMirrorGroupIndex) {
        this.sniMirrorGroupIndex = sniMirrorGroupIndex;
    }

    /**
     * @return the sniMirrorGroupName
     */
    public String getSniMirrorGroupName() {
        return sniMirrorGroupName;
    }

    /**
     * @param sniMirrorGroupName
     *            the sniMirrorGroupName to set
     */
    public void setSniMirrorGroupName(String sniMirrorGroupName) {
        this.sniMirrorGroupName = sniMirrorGroupName;
    }

    /**
     * @return the sniMirrorGroupDstPortList
     */
    public String getSniMirrorGroupDstPortList() {
        return sniMirrorGroupDstPortList;
    }

    /**
     * @param sniMirrorGroupDstPortList
     *            the sniMirrorGroupDstPortList to set
     */
    public void setSniMirrorGroupDstPortList(String sniMirrorGroupDstPortList) {
        this.sniMirrorGroupDstPortList = sniMirrorGroupDstPortList;
        sniMirrorGroupDstPortIndexList = EponUtil.getSniPortIndexFromMib(sniMirrorGroupDstPortList);
    }

    /**
     * @return the sniMirrorGroupSrcInPortList
     */
    public String getSniMirrorGroupSrcInPortList() {
        return sniMirrorGroupSrcInPortList;
    }

    /**
     * @param sniMirrorGroupSrcInPortList
     *            the sniMirrorGroupSrcInPortList to set
     */
    public void setSniMirrorGroupSrcInPortList(String sniMirrorGroupSrcInPortList) {
        this.sniMirrorGroupSrcInPortList = sniMirrorGroupSrcInPortList;
        sniMirrorGroupSrcInPortIndexList = EponUtil.getSniPortIndexFromMib(sniMirrorGroupSrcInPortList);
    }

    /**
     * @return the sniMirrorGroupSrcOutPortList
     */
    public String getSniMirrorGroupSrcOutPortList() {
        return sniMirrorGroupSrcOutPortList;
    }

    /**
     * @param sniMirrorGroupSrcOutPortList
     *            the sniMirrorGroupSrcOutPortList to set
     */
    public void setSniMirrorGroupSrcOutPortList(String sniMirrorGroupSrcOutPortList) {
        this.sniMirrorGroupSrcOutPortList = sniMirrorGroupSrcOutPortList;
        sniMirrorGroupSrcOutPortIndexList = EponUtil.getSniPortIndexFromMib(sniMirrorGroupSrcOutPortList);
    }

    /**
     * @return the sniMirrorGroupRowstatus
     */
    public Integer getSniMirrorGroupRowstatus() {
        return sniMirrorGroupRowstatus;
    }

    /**
     * @param sniMirrorGroupRowstatus
     *            the sniMirrorGroupRowstatus to set
     */
    public void setSniMirrorGroupRowstatus(Integer sniMirrorGroupRowstatus) {
        this.sniMirrorGroupRowstatus = sniMirrorGroupRowstatus;
    }

    /**
     * @return the sniMirrorGroupDstPortIndexList
     */
    public List<Long> getSniMirrorGroupDstPortIndexList() {
        return sniMirrorGroupDstPortIndexList;
    }

    /**
     * @param sniMirrorGroupDstPortIndexList
     *            the sniMirrorGroupDstPortIndexList to set
     */
    public void setSniMirrorGroupDstPortIndexList(List<Long> sniMirrorGroupDstPortIndexList) {
        this.sniMirrorGroupDstPortIndexList = sniMirrorGroupDstPortIndexList;
        sniMirrorGroupDstPortList = EponUtil.getMibStringFromSniPortList(sniMirrorGroupDstPortIndexList);
    }

    /**
     * @return the sniMirrorGroupSrcInPortIndexList
     */
    public List<Long> getSniMirrorGroupSrcInPortIndexList() {
        return sniMirrorGroupSrcInPortIndexList;
    }

    /**
     * @param sniMirrorGroupSrcInPortIndexList
     *            the sniMirrorGroupSrcInPortIndexList to set
     */
    public void setSniMirrorGroupSrcInPortIndexList(List<Long> sniMirrorGroupSrcInPortIndexList) {
        this.sniMirrorGroupSrcInPortIndexList = sniMirrorGroupSrcInPortIndexList;
        sniMirrorGroupSrcInPortList = EponUtil.getMibStringFromSniPortList(sniMirrorGroupSrcInPortIndexList);
    }

    /**
     * @return the sniMirrorGroupSrcOutPortIndexList
     */
    public List<Long> getSniMirrorGroupSrcOutPortIndexList() {
        return sniMirrorGroupSrcOutPortIndexList;
    }

    /**
     * @param sniMirrorGroupSrcOutPortIndexList
     *            the sniMirrorGroupSrcOutPortIndexList to set
     */
    public void setSniMirrorGroupSrcOutPortIndexList(List<Long> sniMirrorGroupSrcOutPortIndexList) {
        this.sniMirrorGroupSrcOutPortIndexList = sniMirrorGroupSrcOutPortIndexList;
        sniMirrorGroupSrcOutPortList = EponUtil.getMibStringFromSniPortList(sniMirrorGroupSrcOutPortIndexList);
    }

    public String getDestPortName() {
        return destPortName;
    }

    public void setDestPortName(String destPortName) {
        this.destPortName = destPortName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OltSniMirrorConfig [sniMirrorGroupIndex=");
        builder.append(sniMirrorGroupIndex);
        builder.append(", sniMirrorGroupName=");
        builder.append(sniMirrorGroupName);
        builder.append(", sniMirrorGroupDstPortList=");
        builder.append(sniMirrorGroupDstPortList);
        builder.append(", sniMirrorGroupSrcInPortList=");
        builder.append(sniMirrorGroupSrcInPortList);
        builder.append(", sniMirrorGroupSrcOutPortList=");
        builder.append(sniMirrorGroupSrcOutPortList);
        builder.append(", sniMirrorGroupRowstatus=");
        builder.append(sniMirrorGroupRowstatus);
        builder.append("]");
        return builder.toString();
    }

}
