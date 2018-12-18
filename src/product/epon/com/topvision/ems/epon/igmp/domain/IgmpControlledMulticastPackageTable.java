/***********************************************************************
 * $ IgmpControlledMulticastPackageTable.java,v1.0 2011-11-23 9:19:47 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmp.domain;

import java.util.List;

import com.topvision.ems.epon.utils.EponUtil;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.annotation.TableProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author jay
 * @created @2011-11-23-9:19:47
 */
@TableProperty(tables = { "default" })
public class IgmpControlledMulticastPackageTable implements AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    private Long entityId;
    /**
     * 该可控组播业务包的索引（流水号）
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.2.1.1", index = true)
    private Integer cmIndex;
    private Integer previewInterval;

    /**
     * 该可控组播业务包名称 OCTET STRING (SIZE (0..256))
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.2.1.2", writable = true, type = "OctetString")
    private String cmName;
    private String cmChName;
    /**
     * 该可控组播业务包支持的Proxy列表 采用类似INTERNET比特顺序，从左到右的OCTET分别为OCTET 0, OCTET 1, ...
     * 每个OCTET的bit从左到右分别为bit0，bit1，...，bit7。 按照bit和OCTET由小到大的顺序，每个bit对应于igmpProxyParaTable中的
     * 一个proxyIndex，如果bit置位，表示该proxyIndex支持，否则不支持。 举例来说，如果规划了80个频道，proxyIndex范围为1..80，用10个字节表示
     * proxy列表，假设该PON口只支持频道列表1,15,38，对应的cmProxyList可以 设置成0x80 02 00 00 04 00 00 00 00 00 OCTET
     * STRING (SIZE (0..250))
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.2.1.3", writable = true, type = "OctetString")
    private String cmProxyList;
    private List<Integer> cmProxyListNum;
    /**
     * 用户权限（permit、preview、deny） INTEGER { permit(1), preview(2), deny(3) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.2.1.4", writable = true, type = "Integer32")
    private Integer multicastUserAuthority;
    /**
     * 最大允许请求频道数
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.2.1.5", writable = true, type = "Integer32")
    private Integer maxRequestChannelNum;
    /**
     * 单次预览时长，单位：秒
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.2.1.6", writable = true, type = "Integer32")
    private Integer singlePreviewTime;
    /**
     * 预览总时长，单位：秒
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.2.1.7", writable = true, type = "Integer32")
    private Integer totalPreviewTime;
    /**
     * 预览复位时间，单位：秒
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.2.1.8", writable = true, type = "Integer32")
    private Integer previewResetTime;
    /**
     * 预览次数
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.2.1.9", writable = true, type = "Integer32")
    private Integer previewCount;
    /**
     * 行状态 INTEGER { active(1), notInService(2), notReady(3), createAndGo(4), createAndWait(5),
     * destroy(6) }
     */
    @SnmpProperty(table = "default", oid = "1.3.6.1.4.1.17409.2.3.6.4.2.1.10", writable = true, type = "Integer32")
    private Integer cmRowStatus;

    public String getCmChName() {
        return cmChName;
    }

    public void setCmChName(String cmChName) {
        this.cmChName = cmChName;
    }

    public Integer getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Integer cmIndex) {
        this.cmIndex = cmIndex;
    }

    public String getCmName() {
        return cmName;
    }

    public void setCmName(String cmName) {
        this.cmName = cmName;
    }

    public String getCmProxyList() {
        return cmProxyList;
    }

    public void setCmProxyList(String cmProxyList) {
        this.cmProxyList = cmProxyList;
        if (cmProxyList != null && !"".equals(cmProxyList)) {
            cmProxyListNum = EponUtil.getCmProxyListFromMib(cmProxyList);
        }
    }

    public Integer getCmRowStatus() {
        return cmRowStatus;
    }

    public void setCmRowStatus(Integer cmRowStatus) {
        this.cmRowStatus = cmRowStatus;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getMaxRequestChannelNum() {
        return maxRequestChannelNum;
    }

    public void setMaxRequestChannelNum(Integer maxRequestChannelNum) {
        this.maxRequestChannelNum = maxRequestChannelNum;
    }

    public Integer getMulticastUserAuthority() {
        return multicastUserAuthority;
    }

    public void setMulticastUserAuthority(Integer multicastUserAuthority) {
        this.multicastUserAuthority = multicastUserAuthority;
    }

    public Integer getPreviewCount() {
        return previewCount;
    }

    public void setPreviewCount(Integer previewCount) {
        this.previewCount = previewCount;
    }

    public Integer getPreviewResetTime() {
        return previewResetTime;
    }

    public void setPreviewResetTime(Integer previewResetTime) {
        this.previewResetTime = previewResetTime;
    }

    public Integer getSinglePreviewTime() {
        return singlePreviewTime;
    }

    public void setSinglePreviewTime(Integer singlePreviewTime) {
        this.singlePreviewTime = singlePreviewTime;
    }

    public Integer getTotalPreviewTime() {
        return totalPreviewTime;
    }

    public void setTotalPreviewTime(Integer totalPreviewTime) {
        this.totalPreviewTime = totalPreviewTime;
    }

    public Integer getPreviewInterval() {
        return previewInterval;
    }

    public void setPreviewInterval(Integer previewInterval) {
        this.previewInterval = previewInterval;
    }

    /**
     * @return the cmProxyListNum
     */
    public List<Integer> getCmProxyListNum() {
        return cmProxyListNum;
    }

    /**
     * @param cmProxyListNum
     *            the cmProxyListNum to set
     */
    public void setCmProxyListNum(List<Integer> cmProxyListNum) {
        this.cmProxyListNum = cmProxyListNum;
        cmProxyList = EponUtil.getBitMapFormProxyList(cmProxyListNum);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IgmpControlledMulticastPackageTable");
        sb.append("{cmChName='").append(cmChName).append('\'');
        sb.append(", entityId=").append(entityId);
        sb.append(", cmIndex=").append(cmIndex);
        sb.append(", cmName='").append(cmName).append('\'');
        sb.append(", cmProxyList='").append(cmProxyList).append('\'');
        sb.append(", cmProxyListNum=").append(cmProxyListNum);
        sb.append(", multicastUserAuthority=").append(multicastUserAuthority);
        sb.append(", maxRequestChannelNum=").append(maxRequestChannelNum);
        sb.append(", singlePreviewTime=").append(singlePreviewTime);
        sb.append(", totalPreviewTime=").append(totalPreviewTime);
        sb.append(", previewResetTime=").append(previewResetTime);
        sb.append(", previewCount=").append(previewCount);
        sb.append(", cmRowStatus=").append(cmRowStatus);
        sb.append('}');
        return sb.toString();
    }
}
