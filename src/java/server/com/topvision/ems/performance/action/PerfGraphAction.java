/***********************************************************************
 * $Id: PerfGraphAction.java,v1.0 2013-9-9 下午04:51:59 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.performance.action;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.framework.web.struts2.BaseAction;

/**
 * @author lizongtian
 * @created @2013-9-9-下午04:51:59
 * 
 */
@Controller("perfGraphAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PerfGraphAction extends BaseAction {
    private static final long serialVersionUID = 624809894227514867L;
    private final Logger logger = LoggerFactory.getLogger(PerfGraphAction.class);

    protected Long entityId;
    protected String entityName;
    protected String indexs;
    protected Long deviceType;
    protected String startTime;
    protected String endTime;

    protected Integer cpuUsed;
    protected Integer cpuUsedEnable;
    protected Integer memUsed;
    protected Integer memUsedEnable;
    protected Integer flashUsed;
    protected Integer flashUsedEnable;
    protected Integer optLink;
    protected Integer optLinkEnable;

    protected Integer sniFlow;
    protected Integer sniFlowEnable;
    protected Integer ponFlow;
    protected Integer ponFlowEnable;
    protected Integer onuPonFlow;
    protected Integer onuPonFlowEnable;
    protected Integer uniFlow;
    protected Integer uniFlowEnable;
    protected Integer boardTemp;
    protected Integer boardTempEnable;
    protected Integer fanSpeed;
    protected Integer fanSpeedEnable;

    protected Integer upLinkFlow;
    protected Integer upLinkFlowEnable;
    protected Integer macFlow;
    protected Integer macFlowEnable;
    protected Integer channelSpeed;
    protected Integer channelSpeedEnable;
    protected Integer moduleTemp;
    protected Integer moduleTempEnable;
    protected Integer snr;
    protected Integer snrEnable;
    protected Integer ber;
    protected Integer berEnable;

    protected Integer direction;
    protected String groupName;

    /**
     * 获取PerfTargetAction对象中指定属性的值
     * 
     * @param perfGraphAction
     * @param propertyName
     * @return
     */
    protected Object getPropertyValueOfPerfGraphAction(PerfGraphAction perfGraphAction, String propertyName) {
        Object value = null;
        try {
            Class<?> objectClass = perfGraphAction.getClass();
            PropertyDescriptor pd = new PropertyDescriptor(propertyName, objectClass);
            Method getMethod = pd.getReadMethod();// 获得get方法
            value = getMethod.invoke(perfGraphAction);// 执行get方法获取对应性能指标的值
        } catch (Exception e) {
            logger.info("this property don't exist, or there is no proper get method!");
        }
        return value;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getIndexs() {
        return indexs;
    }

    public void setIndexs(String indexs) {
        this.indexs = indexs;
    }

    public Long getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Long deviceType) {
        this.deviceType = deviceType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(Integer cpuUsed) {
        this.cpuUsed = cpuUsed;
    }

    public Integer getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(Integer memUsed) {
        this.memUsed = memUsed;
    }

    public Integer getFlashUsed() {
        return flashUsed;
    }

    public void setFlashUsed(Integer flashUsed) {
        this.flashUsed = flashUsed;
    }

    public Integer getOptLink() {
        return optLink;
    }

    public void setOptLink(Integer optLink) {
        this.optLink = optLink;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getSniFlow() {
        return sniFlow;
    }

    public void setSniFlow(Integer sniFlow) {
        this.sniFlow = sniFlow;
    }

    public Integer getPonFlow() {
        return ponFlow;
    }

    public void setPonFlow(Integer ponFlow) {
        this.ponFlow = ponFlow;
    }

    public Integer getOnuPonFlow() {
        return onuPonFlow;
    }

    public void setOnuPonFlow(Integer onuPonFlow) {
        this.onuPonFlow = onuPonFlow;
    }

    public Integer getUniFlow() {
        return uniFlow;
    }

    public void setUniFlow(Integer uniFlow) {
        this.uniFlow = uniFlow;
    }

    public Integer getBoardTemp() {
        return boardTemp;
    }

    public void setBoardTemp(Integer boardTemp) {
        this.boardTemp = boardTemp;
    }

    public Integer getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(Integer fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public Integer getUpLinkFlow() {
        return upLinkFlow;
    }

    public void setUpLinkFlow(Integer upLinkFlow) {
        this.upLinkFlow = upLinkFlow;
    }

    public Integer getMacFlow() {
        return macFlow;
    }

    public void setMacFlow(Integer macFlow) {
        this.macFlow = macFlow;
    }

    public Integer getChannelSpeed() {
        return channelSpeed;
    }

    public void setChannelSpeed(Integer channelSpeed) {
        this.channelSpeed = channelSpeed;
    }

    public Integer getModuleTemp() {
        return moduleTemp;
    }

    public void setModuleTemp(Integer moduleTemp) {
        this.moduleTemp = moduleTemp;
    }

    public Integer getSnr() {
        return snr;
    }

    public void setSnr(Integer snr) {
        this.snr = snr;
    }

    public Integer getBer() {
        return ber;
    }

    public void setBer(Integer ber) {
        this.ber = ber;
    }

    public Integer getCpuUsedEnable() {
        return cpuUsedEnable;
    }

    public void setCpuUsedEnable(Integer cpuUsedEnable) {
        this.cpuUsedEnable = cpuUsedEnable;
    }

    public Integer getMemUsedEnable() {
        return memUsedEnable;
    }

    public void setMemUsedEnable(Integer memUsedEnable) {
        this.memUsedEnable = memUsedEnable;
    }

    public Integer getFlashUsedEnable() {
        return flashUsedEnable;
    }

    public void setFlashUsedEnable(Integer flashUsedEnable) {
        this.flashUsedEnable = flashUsedEnable;
    }

    public Integer getOptLinkEnable() {
        return optLinkEnable;
    }

    public void setOptLinkEnable(Integer optLinkEnable) {
        this.optLinkEnable = optLinkEnable;
    }

    public Integer getSniFlowEnable() {
        return sniFlowEnable;
    }

    public void setSniFlowEnable(Integer sniFlowEnable) {
        this.sniFlowEnable = sniFlowEnable;
    }

    public Integer getPonFlowEnable() {
        return ponFlowEnable;
    }

    public void setPonFlowEnable(Integer ponFlowEnable) {
        this.ponFlowEnable = ponFlowEnable;
    }

    public Integer getOnuPonFlowEnable() {
        return onuPonFlowEnable;
    }

    public void setOnuPonFlowEnable(Integer onuPonFlowEnable) {
        this.onuPonFlowEnable = onuPonFlowEnable;
    }

    public Integer getUniFlowEnable() {
        return uniFlowEnable;
    }

    public void setUniFlowEnable(Integer uniFlowEnable) {
        this.uniFlowEnable = uniFlowEnable;
    }

    public Integer getBoardTempEnable() {
        return boardTempEnable;
    }

    public void setBoardTempEnable(Integer boardTempEnable) {
        this.boardTempEnable = boardTempEnable;
    }

    public Integer getFanSpeedEnable() {
        return fanSpeedEnable;
    }

    public void setFanSpeedEnable(Integer fanSpeedEnable) {
        this.fanSpeedEnable = fanSpeedEnable;
    }

    public Integer getUpLinkFlowEnable() {
        return upLinkFlowEnable;
    }

    public void setUpLinkFlowEnable(Integer upLinkFlowEnable) {
        this.upLinkFlowEnable = upLinkFlowEnable;
    }

    public Integer getMacFlowEnable() {
        return macFlowEnable;
    }

    public void setMacFlowEnable(Integer macFlowEnable) {
        this.macFlowEnable = macFlowEnable;
    }

    public Integer getChannelSpeedEnable() {
        return channelSpeedEnable;
    }

    public void setChannelSpeedEnable(Integer channelSpeedEnable) {
        this.channelSpeedEnable = channelSpeedEnable;
    }

    public Integer getModuleTempEnable() {
        return moduleTempEnable;
    }

    public void setModuleTempEnable(Integer moduleTempEnable) {
        this.moduleTempEnable = moduleTempEnable;
    }

    public Integer getSnrEnable() {
        return snrEnable;
    }

    public void setSnrEnable(Integer snrEnable) {
        this.snrEnable = snrEnable;
    }

    public Integer getBerEnable() {
        return berEnable;
    }

    public void setBerEnable(Integer berEnable) {
        this.berEnable = berEnable;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

}
