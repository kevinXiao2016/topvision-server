/***********************************************************************
 * $Id: PnmpPollFacade.java,v1.0 2011-7-1 下午02:43:48 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.facade;

import com.topvision.ems.cm.pnmp.facade.domain.CmtsCm;
import com.topvision.ems.cm.pnmp.domain.PnmpPollCollectParam;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpPollTask;
import com.topvision.ems.facade.Facade;
import com.topvision.ems.network.domain.TelnetLogin;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

import java.util.List;

/**
 * @author jay
 * @created @2011-7-1-下午02:43:48
 * 
 */
@EngineFacade(serviceName = "pnmpPollFacade", beanName = "pnmpPollFacade", category = "PNMP")
public interface PnmpPollFacade extends Facade {
    /**
     * 接收采集任务
     * 
     * @param cmPollTask
     * @return
     */
    boolean appendTesk(Long time, List<PnmpPollTask> cmPollTask);

    /**
     * 开始新的一轮采集
     */
    void roundStart(Long time, PnmpPollTask pnmpPollTask);

    /**
     * 结束一轮采集
     */
    void roundFinished(Long time, PnmpPollTask pnmpPollTask);

    /**
     * 心跳检测
     */
    void heartBeat();

    /**
     * 初始化采集器
     */
    void initRunAug(PnmpPollCollectParam cmPollCollectParam);

    /**
     * 处理一轮结束事件
     * 
     * @param time
     * @param cmPollTask
     */
    void appendEndTask(Long time, PnmpPollTask cmPollTask);

    List<CmtsCm> getCmtsCmList(SnmpParam snmpParam);

    void setCollectSnmpParam(SnmpParam snmpParam);

    PnmpCmData realPnmp(Long cmtsId,SnmpParam snmpParam, SnmpParam cmSnmpParam, String cmMac);
}
