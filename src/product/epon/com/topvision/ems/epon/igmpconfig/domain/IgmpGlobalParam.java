/***********************************************************************
 * $Id: IgmpGlobalParam.java,v1.0 2016-6-7 上午11:35:07 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.domain;

import com.topvision.framework.annotation.SnmpProperty;
import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author flack
 * @created @2016-6-7-上午11:35:07
 * IGMP全局参数配置对象
 */
public class IgmpGlobalParam implements AliasesSuperType {
    private static final long serialVersionUID = -881126591153460500L;

    private Long entityId;
    //IGMP版本
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.1.0", writable = true, type = "Integer32")
    private Integer igmpVersion;
    //IGMP模式
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.2.0", writable = true, type = "Integer32")
    private Integer igmpMode;
    //组播IGMPv3兼容模式退回到v2模式的允许时长
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.3.0", writable = true, type = "Integer32")
    private Integer v2Timeout;
    //V3查询报文响应时间
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.4.0", writable = true, type = "Integer32")
    private Integer v3RespTime;
    //V2查询报文响应时间
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.5.0", writable = true, type = "Integer32")
    private Integer v2RespTime;
    //下行通用组查询间隔
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.6.0", writable = true, type = "Integer32")
    private Integer commonInterval;
    //下行特定组查询间隔
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.7.0", writable = true, type = "Integer32")
    private Integer specialInterval;
    //下行特定组查询次数
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.8.0", writable = true, type = "Integer32")
    private Integer squeryNum;
    //V3特定组查询报文响应时间
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.9.0", writable = true, type = "Integer32")
    private Integer squeryRespV3;
    //V2特定组查询报文响应时间
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.10.0", writable = true, type = "Integer32")
    private Integer squeryRespV2;
    //健壮系数
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.11.0", writable = true, type = "Integer32")
    private Integer robustVariable;
    //查询报文源ip
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.12.0", writable = true, type = "IpAddress")
    private String querySrcIp;
    //IGMP最大带宽（允许的带宽之和）
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.13.0", writable = true, type = "Integer32")
    private Integer globalBW;
    //snooping 模式转发表项的老化时间
    @SnmpProperty(oid = "1.3.6.1.4.1.32285.11.2.3.6.1.1.14.0", writable = true, type = "Integer32")
    private Integer snpAgingTime;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Integer getIgmpVersion() {
        return igmpVersion;
    }

    public void setIgmpVersion(Integer igmpVersion) {
        this.igmpVersion = igmpVersion;
    }

    public Integer getIgmpMode() {
        return igmpMode;
    }

    public void setIgmpMode(Integer igmpMode) {
        this.igmpMode = igmpMode;
    }

    public Integer getV2Timeout() {
        return v2Timeout;
    }

    public void setV2Timeout(Integer v2Timeout) {
        this.v2Timeout = v2Timeout;
    }

    public Integer getV3RespTime() {
        return v3RespTime;
    }

    public void setV3RespTime(Integer v3RespTime) {
        this.v3RespTime = v3RespTime;
    }

    public Integer getV2RespTime() {
        return v2RespTime;
    }

    public void setV2RespTime(Integer v2RespTime) {
        this.v2RespTime = v2RespTime;
    }

    public Integer getCommonInterval() {
        return commonInterval;
    }

    public void setCommonInterval(Integer commonInterval) {
        this.commonInterval = commonInterval;
    }

    public Integer getSpecialInterval() {
        return specialInterval;
    }

    public void setSpecialInterval(Integer specialInterval) {
        this.specialInterval = specialInterval;
    }

    public Integer getSqueryNum() {
        return squeryNum;
    }

    public void setSqueryNum(Integer squeryNum) {
        this.squeryNum = squeryNum;
    }

    public Integer getSqueryRespV3() {
        return squeryRespV3;
    }

    public void setSqueryRespV3(Integer squeryRespV3) {
        this.squeryRespV3 = squeryRespV3;
    }

    public Integer getSqueryRespV2() {
        return squeryRespV2;
    }

    public void setSqueryRespV2(Integer squeryRespV2) {
        this.squeryRespV2 = squeryRespV2;
    }

    public Integer getRobustVariable() {
        return robustVariable;
    }

    public void setRobustVariable(Integer robustVariable) {
        this.robustVariable = robustVariable;
    }

    public String getQuerySrcIp() {
        return querySrcIp;
    }

    public void setQuerySrcIp(String querySrcIp) {
        this.querySrcIp = querySrcIp;
    }

    public Integer getGlobalBW() {
        return globalBW;
    }

    public void setGlobalBW(Integer globalBW) {
        this.globalBW = globalBW;
    }

    public Integer getSnpAgingTime() {
        return snpAgingTime;
    }

    public void setSnpAgingTime(Integer snpAgingTime) {
        this.snpAgingTime = snpAgingTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IgmpGlobalParam [entityId=");
        builder.append(entityId);
        builder.append(", igmpVersion=");
        builder.append(igmpVersion);
        builder.append(", igmpMode=");
        builder.append(igmpMode);
        builder.append(", v2Timeout=");
        builder.append(v2Timeout);
        builder.append(", v3RespTime=");
        builder.append(v3RespTime);
        builder.append(", v2RespTime=");
        builder.append(v2RespTime);
        builder.append(", commonInterval=");
        builder.append(commonInterval);
        builder.append(", specialInterval=");
        builder.append(specialInterval);
        builder.append(", squeryNum=");
        builder.append(squeryNum);
        builder.append(", squeryRespV3=");
        builder.append(squeryRespV3);
        builder.append(", squeryRespV2=");
        builder.append(squeryRespV2);
        builder.append(", robustVariable=");
        builder.append(robustVariable);
        builder.append(", querySrcIp=");
        builder.append(querySrcIp);
        builder.append(", globalBW=");
        builder.append(globalBW);
        builder.append(", snpAgingTime=");
        builder.append(snpAgingTime);
        builder.append("]");
        return builder.toString();
    }

}
