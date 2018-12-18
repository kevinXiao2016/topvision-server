/***********************************************************************
 * $ CmStatusDB.java,v1.0 2013-6-21 15:45:13 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.topvision.framework.utils.CmcIndexUtils;
import com.topvision.framework.dao.mybatis.AliasesSuperType;


/**
 * @author jay
 * @created @2013-6-21-15:45:13
 */
@Alias("initialDataCmAction")
public class InitialDataCmAction implements Serializable,  AliasesSuperType {
    private static final long serialVersionUID = 7342180206202447869L;
    /**
    1: other(1) // OFFLINE
 	2: ranging(2)
 	3: rangingAborted(3)
 	4: rangingComplete(4)
 	5: ipComplete(5)
 	6: registrationComplete(6)
 	7: accessDenied(7)
 	8: operational(8)
 	9: registeredBPIInitializing(9)

     */
    private Long entityId;
    private Long cmIndex;
    private Long downChannelIfIndex;
    private Long upChannelIfIndex;
    private Long ccmtsIndex;
    private Long cmmac;
    private Long cmip;
    private Integer state;
    private Long realtimeLong;

    public Long getCmIndex() {
        return cmIndex;
    }

    public void setCmIndex(Long cmIndex) {
        this.cmIndex = cmIndex;
    }

    public Long getCmip() {
        return cmip;
    }

    public void setCmip(Long cmip) {
        this.cmip = cmip;
    }

    public Long getCmmac() {
        return cmmac;
    }

    public void setCmmac(Long cmmac) {
        this.cmmac = cmmac;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Timestamp getRealtime() {
        return new Timestamp(realtimeLong);
    }

    public void setRealtime(Timestamp realtime) {
        this.realtimeLong = realtime.getTime();
    }

    public Long getRealtimeLong() {
        return realtimeLong;
    }

    public void setRealtimeLong(Long realtimeLong) {
        this.realtimeLong = realtimeLong;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getCcmtsIndex() {
        if (upChannelIfIndex == null) {
        	//20160414 jay 对大C特殊处理成0
            return 0L;
        }
        return CmcIndexUtils.getCmcIndexFromChannelIndex(upChannelIfIndex);
    }

    public Long getDownChannelIfIndex() {
        if (downChannelIfIndex == null) {
            return 1L;
        }
        return downChannelIfIndex;
    }

    public void setDownChannelIfIndex(Long downChannelIfIndex) {
        this.downChannelIfIndex = downChannelIfIndex;
    }

    public Long getUpChannelIfIndex() {
        return upChannelIfIndex;
    }

    public void setUpChannelIfIndex(Long upChannelIfIndex) {
        this.upChannelIfIndex = upChannelIfIndex;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("InitialDataCmAction");
        sb.append("{ccmtsIndex=").append(ccmtsIndex);
        sb.append(", entityId=").append(entityId);
        sb.append(", cmIndex=").append(cmIndex);
        sb.append(", downChannelIfIndex=").append(downChannelIfIndex);
        sb.append(", upChannelIfIndex=").append(upChannelIfIndex);
        sb.append(", cmmac=").append(cmmac);
        sb.append(", cmip=").append(cmip);
        sb.append(", state=").append(state);
        sb.append(", realtimeLong=").append(realtimeLong);
        sb.append('}');
        return sb.toString();
    }
}
