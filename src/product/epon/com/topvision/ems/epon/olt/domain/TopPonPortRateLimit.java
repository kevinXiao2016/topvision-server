/***********************************************************************
 * $Id: TopPonPortRateLimit.java,v1.0 2012-12-18 下午16:47:59 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.domain;

import com.topvision.framework.utils.EponIndex;
import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lzt
 * @created @2012-12-18-下午16:47:59
 * 
 */
public class TopPonPortRateLimit implements AliasesSuperType {
    private static final long serialVersionUID = 7202908273891020667L;
    private Long entityId;
    private Long ponIndex;
    private Long ponId;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.7.1.1", index = true)
    private Integer ponPortRateLmtCardIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.7.1.2", index = true)
    private Integer ponPortRateLmtPortIndex;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.7.1.3", writable = true, type = "Integer32")
    private Integer ponPortUpRateLmt;
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.3.7.1.4", writable = true, type = "Integer32")
    private Integer ponPortDownRateLmt;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getPonId() {
        return ponId;
    }

    public void setPonId(Long ponId) {
        this.ponId = ponId;
    }

    public Integer getPonPortRateLmtCardIndex() {
        return ponPortRateLmtCardIndex;
    }

    public void setPonPortRateLmtCardIndex(Integer ponPortRateLmtCardIndex) {
        this.ponPortRateLmtCardIndex = ponPortRateLmtCardIndex;
    }

    public Integer getPonPortRateLmtPortIndex() {
        return ponPortRateLmtPortIndex;
    }

    public void setPonPortRateLmtPortIndex(Integer ponPortRateLmtPortIndex) {
        this.ponPortRateLmtPortIndex = ponPortRateLmtPortIndex;
    }

    public Integer getPonPortUpRateLmt() {
        return ponPortUpRateLmt;
    }

    public void setPonPortUpRateLmt(Integer ponPortUpRateLmt) {
        this.ponPortUpRateLmt = ponPortUpRateLmt;
    }

    public Integer getPonPortDownRateLmt() {
        return ponPortDownRateLmt;
    }

    public void setPonPortDownRateLmt(Integer ponPortDownRateLmt) {
        this.ponPortDownRateLmt = ponPortDownRateLmt;
    }

    public Long getPonIndex() {
        if (ponIndex == null) {
            ponIndex = EponIndex.getPonIndex(ponPortRateLmtCardIndex, ponPortRateLmtPortIndex);
        }
        return ponIndex;
    }

    public void setPonIndex(Long ponIndex) {
        ponPortRateLmtCardIndex = EponIndex.getSlotNo(ponIndex).intValue();
        ponPortRateLmtPortIndex = EponIndex.getPonNo(ponIndex).intValue();
        this.ponIndex = ponIndex;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TopPonPortRateLimit [entityId=");
        builder.append(entityId);
        builder.append(", ponNo=");
        builder.append(ponId);
        builder.append(", ponPortRateLmtCardIndex=");
        builder.append(ponPortRateLmtCardIndex);
        builder.append(", ponPortRateLmtPortIndex=");
        builder.append(ponPortRateLmtPortIndex);
        builder.append(", ponPortUpRateLmt=");
        builder.append(ponPortUpRateLmt);
        builder.append(", ponPortDownRateLmt=");
        builder.append(ponPortDownRateLmt);
        builder.append("]");
        return builder.toString();
    }

}
