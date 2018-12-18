/***********************************************************************
 * $Id: PerfGlobal.java,v1.0 2013-6-14 下午04:46:38 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;
import com.topvision.framework.domain.BaseEntity;

/**
 * @author Rod John
 * @created @2013-6-14-下午04:46:38
 * 
 */
public class PerfGlobal extends BaseEntity implements AliasesSuperType {

    private static final long serialVersionUID = -5615391334023596696L;
    public static final int PERF_ON = 1;
    public static final int PERF_DOWN = 0;

    // 全局配置类型
    private Long flag;
    // 是否开启性能采集
    private Integer isPerfOn;
    // 是否关联默认阈值模板
    private Integer isRelationWithDefaultTemplate;
    // 默认阈值模板ID
    private Integer defaultTemplateId;
    // 是否启动性能阈值告警
    private Integer isPerfThreshold;
    // 默认采集时间
    private Long defaultCollectTime;

    public PerfGlobal(Long typeId, Integer isPerfOn, Integer isRelationWithDefaultTemplate, Integer isPerfThreshold) {
        super();
        this.flag = typeId;
        this.isPerfOn = isPerfOn;
        this.isRelationWithDefaultTemplate = isRelationWithDefaultTemplate;
        this.isPerfThreshold = isPerfThreshold;
    }

    public PerfGlobal() {
        super();
    }

    /**
     * @return the flag
     */
    public Long getFlag() {
        return flag;
    }

    /**
     * @param flag
     *            the flag to set
     */
    public void setFlag(Long flag) {
        this.flag = flag;
    }

    /**
     * @return the isRelationWithDefaultTemplate
     */
    public Integer getIsRelationWithDefaultTemplate() {
        return isRelationWithDefaultTemplate;
    }

    /**
     * @param isRelationWithDefaultTemplate
     *            the isRelationWithDefaultTemplate to set
     */
    public void setIsRelationWithDefaultTemplate(Integer isRelationWithDefaultTemplate) {
        this.isRelationWithDefaultTemplate = isRelationWithDefaultTemplate;
    }

    /**
     * @return the isPerfThreshold
     */
    public Integer getIsPerfThreshold() {
        return isPerfThreshold;
    }

    /**
     * @param isPerfThreshold
     *            the isPerfThreshold to set
     */
    public void setIsPerfThreshold(Integer isPerfThreshold) {
        this.isPerfThreshold = isPerfThreshold;
    }

    /**
     * @return the defaultCollectTime
     */
    public Long getDefaultCollectTime() {
        return defaultCollectTime;
    }

    /**
     * @param defaultCollectTime
     *            the defaultCollectTime to set
     */
    public void setDefaultCollectTime(Long defaultCollectTime) {
        this.defaultCollectTime = defaultCollectTime;
    }

    public Integer getIsPerfOn() {
        return isPerfOn;
    }

    public void setIsPerfOn(Integer isPerfOn) {
        this.isPerfOn = isPerfOn;
    }

    /**
     * @return the defaultTemplateId
     */
    public Integer getDefaultTemplateId() {
        return defaultTemplateId;
    }

    /**
     * @param defaultTemplateId
     *            the defaultTemplateId to set
     */
    public void setDefaultTemplateId(Integer defaultTemplateId) {
        this.defaultTemplateId = defaultTemplateId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PerfGlobal [flag=");
        builder.append(flag);
        builder.append(", isPerfOn=");
        builder.append(isPerfOn);
        builder.append(", isRelationWithDefaultTemplate=");
        builder.append(isRelationWithDefaultTemplate);
        builder.append(", isPerfThreshold=");
        builder.append(isPerfThreshold);
        builder.append(", defaultCollectTime=");
        builder.append(defaultCollectTime);
        builder.append("]");
        return builder.toString();
    }
}
