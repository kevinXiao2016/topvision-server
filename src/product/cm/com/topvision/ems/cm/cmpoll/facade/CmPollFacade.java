/***********************************************************************
 * $Id: CmPollFacade.java,v1.0 2011-7-1 下午02:43:48 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.facade;

import java.util.List;

import com.topvision.ems.cm.cmpoll.config.domain.CmPollCollectParam;
import com.topvision.ems.cm.cmpoll.facade.domain.*;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Victor
 * @created @2011-7-1-下午02:43:48
 * 
 */
@EngineFacade(serviceName = "cmPollFacade", beanName = "cmPollFacade", category = "CmPoll")
public interface CmPollFacade extends Facade {
    /**
     * 接收采集任务
     * 
     * @param cmPollTask
     * @return
     */
    boolean appendTesk(Long time, List<CmPollTask> cmPollTask);

    /**
     * 开始新的一轮采集
     */
    void roundStart(Long time);

    /**
     * 结束一轮采集
     */
    void roundFinished(Long time);

    /**
     * 心跳检测
     */
    void heartBeat();

    /**
     * 初始化采集器
     */
    void initRunAug(CmPollCollectParam cmPollCollectParam);

    /**
     * 用Remote Query方式查询设备下所有CM 3.0 下行信道实时信号
     * 
     * @param snmpParam
     * @return
     */
    List<CmPoll3DsRemoteQuery> getCm3DsSignal(SnmpParam snmpParam);

    /**
     * 用Remote Query方式查询设备下所有CM 3.0 上行信道实时信号
     * 
     * @param snmpParam
     * @return
     */
    List<CmPoll3UsRemoteQuery> getCm3UsSignal(SnmpParam snmpParam);

    /**
     * 用Remote Query方式查询设备下所有CM 3.0 topCcmtsCmRemoteQueryCmTable信息
     * 
     * @param snmpParam
     * @return
     */
    List<DocsIf3CmPollUsStatus> getDocsIf3CmPollUsStatus(SnmpParam snmpParam);

    /**
     * 获取上行信道信息
     * 
     * @param snmpParam
     * @return
     */
    List<UpstreamChannelInfo> getUpstreamChannelInfo(SnmpParam snmpParam);

    /**
     * 处理一轮结束事件
     * 
     * @param time
     * @param cmPollTask
     */
    void appendEndTask(Long time, CmPollTask cmPollTask);

    List<DownstreamChannelInfo> getDownstreamChannelInfo(SnmpParam snmpParam);

    List<CmPollCmRemoteQuery> getCmPollCmRemoteQueryList(SnmpParam snmpParam);

    List<DocsIf3CmPollUsStatus> getDocsIf3CmPollUsStatusByCmIndex(SnmpParam snmpParam, Long statusIndex);

    CmPollCmRemoteQuery getCmPollCmRemoteQueryByCmIndex(SnmpParam snmpParam, Long statusIndex);

    List<CmPoll3UsRemoteQuery> getCm3UsSignalByCmIndex(SnmpParam snmpParam, Long statusIndex);

    List<CmPoll3DsRemoteQuery> getCm3DsSignalByCmIndex(SnmpParam snmpParam, Long statusIndex);
}
