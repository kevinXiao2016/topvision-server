/***********************************************************************
 * $Id: CcmtsCmListFacade.java,v1.0 2013-10-30 下午4:10:43 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.facade;

import java.util.List;

import com.topvision.ems.cmc.ccmts.domain.CmcClearCmOnTime;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmStatusForContactCmc;
import com.topvision.ems.cmc.facade.domain.DocsIfDownstreamChannelForContactCmc;
import com.topvision.ems.cmc.facade.domain.DocsIfSignalQualityForContactCmc;
import com.topvision.ems.cmc.facade.domain.DocsIfUpstreamChannelForContactCmc;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author YangYi
 * @created @2013-10-30-下午4:10:43
 * 
 */
@EngineFacade(serviceName = "CcmtsCmListFacade", beanName = "ccmtsCmListFacade")
public interface CcmtsCmListFacade extends Facade {
    /**
     * 通过CC重启CM
     * 
     * @param snmpParam
     * @param cmIndex
     */
    void restartCmByCcmts(SnmpParam snmpParam, Long cmIndex);

    /**
     * 通过CCMTS重启CCMTS下的所有CM
     * 
     * @param snmpParam
     * @param cmcIndex
     */
    void restartAllCm(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 获取下行信号质量
     * 
     * @param snmpParam
     * @param cmIp
     * @return
     */
    List<DocsIfSignalQualityForContactCmc> getDocsIfSignalQualityForContactCmc(SnmpParam snmpParam, String cmIp);

    /**
     * 获取下行射频相关参数
     * 
     * @param snmpParam
     * @param cmIp
     * @return
     */
    List<DocsIfDownstreamChannelForContactCmc> getDocsIfDownstreamForContactCmc(SnmpParam snmpParam, String cmIp);

    /**
     * 获取上行射频相关参数
     * 
     * @param snmpParam
     * @param cmIp
     * @return
     */
    List<DocsIfUpstreamChannelForContactCmc> getDocsIfUpstreamForContactCmc(SnmpParam snmpParam, String cmIp);

    /**
     * 获取cmtatus相关参数
     * 
     * @param snmpParam
     * @param cmIp
     * @return
     */
    CmStatusForContactCmc showCmStatusForContactCmc(SnmpParam snmpParam, String cmIp);

    /**
     * 获取flap信息
     * 
     * @param cms
     * @param snmpParam
     * @return
     */
    List<CmFlap> getCmFlap(SnmpParam snmpParam);

    /**
     * 清除CC下所有下线的CM
     * 
     * @param snmpParam
     * @param cmcIndex
     */
    void clearAllOfflineCmsOnCC(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 刷新关联的CM列表信息
     * 
     * @param snmpParam
     *            SnmpParam
     * @return List<CmAttribute>
     */
    List<CmAttribute> refreshContactedCmList(SnmpParam snmpParam, Long cmcIndex);

    /**
     * 清除离线一定时间的CM
     * 
     * @param
     * @return void
     */
    void clearOnTimeCmOfCC(SnmpParam snmpParam, Integer time);
    
    public CmcClearCmOnTime getCmcClearTime(SnmpParam snmpParam);
    
}
