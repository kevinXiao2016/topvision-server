/***********************************************************************
 * $Id: CmStatusPerfResult.java,v1.0 2012-7-17 上午10:33:43 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

import java.util.ArrayList;
import java.util.List;

import com.topvision.framework.constants.Symbol;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmPartialSvcState;
import com.topvision.ems.cmc.facade.domain.DocsIf3CmtsCmUsStatus;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.facade.domain.OperClass;
import com.topvision.ems.facade.domain.PerformanceResult;

/**
 * @author loyal
 * @created @2012-7-17-上午10:33:43
 * 
 */
public class CmStatusPerfResult extends PerformanceResult<OperClass> {
    /**
     * @param domain
     */
    public CmStatusPerfResult(OperClass domain) {
        super(domain);
    }

    private static final long serialVersionUID = -6434107944338930410L;
    private Long cmcId;
    private Long cmId;
    private List<CmAttribute> cmAttributes;
    //是否为实时刷新
    private Boolean realtimeRefresh = false;
    //是否为全设备刷新   false表示单台ccmts刷新
    private Boolean allDevice = true;
    private boolean cmArrayEmpty = false;
    /**
     * CM的上行信道信息，支持3.0CM，Added by huangdongsheng
     */
    private List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatus;
    /**
     * CM 的Flap信息
     * 
     * @author dosion
     */
    private List<CmFlap> cmFlaps;
    
    /**
     * partial service受损上下行信道
     */
    private List<CmPartialSvcState> partialStates;
    
    private long dt;

    /**
     * @return the cmcId
     */
    public Long getCmcId() {
        return cmcId;
    }

    /**
     * @param cmcId
     *            the cmcId to set
     */
    public void setCmcId(Long cmcId) {
        this.cmcId = cmcId;
    }

    /**
     * @return the cmId
     */
    public Long getCmId() {
        return cmId;
    }

    /**
     * @param cmId
     *            the cmId to set
     */
    public void setCmId(Long cmId) {
        this.cmId = cmId;
    }

    /**
     * @return the cmAttributes
     */
    public List<CmAttribute> getCmAttributes() {
        return cmAttributes;
    }

    /**
     * @param cmAttributes
     *            the cmAttributes to set
     */
    public void setCmAttributes(List<CmAttribute> cmAttributes) {
        this.cmAttributes = cmAttributes;
    }

    /**
     * @return the dt
     */
    public long getDt() {
        return dt;
    }

    /**
     * @param dt
     *            the dt to set
     */
    public void setDt(long dt) {
        this.dt = dt;
    }

    /**
     * 添加 信道cm统计信息
     * 
     * @param channelCmNumStatic
     *            信道统计信息
     */
    public void addCmAttribute(CmAttribute cmAttribute) {
        if (cmAttributes == null) {
            cmAttributes = new ArrayList<CmAttribute>();
        }
        cmAttributes.add(cmAttribute);
    }

    /**
     * @return the docsIf3CmtsCmUsStatus
     */
    public List<DocsIf3CmtsCmUsStatus> getDocsIf3CmtsCmUsStatus() {
        return docsIf3CmtsCmUsStatus;
    }

    /**
     * @param docsIf3CmtsCmUsStatus
     *            the docsIf3CmtsCmUsStatus to set
     */
    public void setDocsIf3CmtsCmUsStatus(List<DocsIf3CmtsCmUsStatus> docsIf3CmtsCmUsStatus) {
        this.docsIf3CmtsCmUsStatus = docsIf3CmtsCmUsStatus;
    }

    /**
     * @return the cmFlaps
     */
    public List<CmFlap> getCmFlaps() {
        return cmFlaps;
    }

    /**
     * @param cmFlaps
     *            the cmFlaps to set
     */
    public void setCmFlaps(List<CmFlap> cmFlaps) {
        this.cmFlaps = cmFlaps;
    }

    public boolean isCmArrayEmpty() { return cmArrayEmpty; }

    public void setCmArrayEmpty(boolean cmArrayEmpty) { this.cmArrayEmpty = cmArrayEmpty; }

    public Boolean isRealtimeRefresh() {
        return realtimeRefresh;
    }

    public void setRealtimeRefresh(Boolean realtimeRefresh) {
        this.realtimeRefresh = realtimeRefresh;
    }

    public Boolean isAllDevice() {
        return allDevice;
    }

    public void setAllDevice(Boolean allDevice) {
        this.allDevice = allDevice;
    }

    /*
             * (non-Javadoc)
             *
             * @see java.lang.Object#toString()
             */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmStatusPerfResult [entityId=");
        builder.append(getEntityId());
        builder.append(", cmcId=");
        builder.append(cmcId);
        builder.append(", cmId=");
        builder.append(cmId);
        builder.append(", cmAttributes=");
        builder.append(cmAttributes);
        builder.append(", dt=");
        builder.append(dt);
        builder.append(Symbol.BRACKET_RIGHT);
        return builder.toString();
    }

    public List<CmPartialSvcState> getPartialStates() {
        return partialStates;
    }

    public void setPartialStates(List<CmPartialSvcState> partialStates) {
        this.partialStates = partialStates;
    }
}
