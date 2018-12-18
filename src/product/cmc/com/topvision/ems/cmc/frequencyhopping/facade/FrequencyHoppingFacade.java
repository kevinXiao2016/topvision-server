/***********************************************************************
 * $Id: CcmtsSpectrumGpFacade.java,v1.0 2013-8-2 上午10:39:33 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.frequencyhopping.facade;

import java.util.List;

import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGp;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpChnl;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpFreq;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpGlobal;
import com.topvision.ems.cmc.frequencyhopping.domain.CcmtsSpectrumGpHopHis;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author haojie
 * @created @2013-8-2-上午10:39:33
 * 
 */
@EngineFacade(serviceName = "FrequencyHoppingFacade", beanName = "frequencyHoppingFacade")
public interface FrequencyHoppingFacade extends Facade {

    /**
     * 修改CCMTS自动跳频全局配置
     * 
     * @param snmpParam
     * @param ccmtsSpectrumGpGlobal
     */
    void setGpGlobalToDevice(SnmpParam snmpParam, CcmtsSpectrumGpGlobal ccmtsSpectrumGpGlobal);

    /**
     * 获取CCMTS自动跳频全局配置
     * 
     * @param snmpParam
     * @return
     */
    CcmtsSpectrumGpGlobal getGpGlobalFromDevice(SnmpParam snmpParam);

    /**
     * 删除设备侧跳频组
     * 
     * @param snmpParam
     * @param ccmtsSpectrumGp
     */
    void destoryGroupFromDevice(SnmpParam snmpParam, CcmtsSpectrumGp ccmtsSpectrumGp);

    /**
     * 获取信道跳频组相关信息(应用到信道的跳频组和备份调制方式)
     * 
     * @param snmpParam
     * @return
     */
    List<CcmtsSpectrumGpChnl> getChnlGroupFromDevice(SnmpParam snmpParam);

    /**
     * 修改修道跳频组相关信息(应用到信道的跳频组和备份调制方式)
     * 
     * @param snmpParam
     * @param chnlGroup
     */
    void setChnlGroupToDevice(SnmpParam snmpParam, CcmtsSpectrumGpChnl chnlGroup);

    /**
     * 获取设备侧跳频组信息
     * 
     * @param snmpParam
     * @return
     */
    List<CcmtsSpectrumGp> getGroupFromDevice(SnmpParam snmpParam);

    /**
     * 获取设备侧跳频组成员频点信息
     * 
     * @param snmpParam
     * @return
     */
    List<CcmtsSpectrumGpFreq> getGroupFreqFromDevice(SnmpParam snmpParam);

    /**
     * * 清空信道跳频历史
     * 
     * @param snmpParam
     * @param hopHis
     */
    void clearDeviceGroupHopHis(SnmpParam snmpParam, CcmtsSpectrumGpChnl chnlGroup);

    /**
     * 获取信道跳频历史
     * 
     * @param snmpParam
     * @return
     */
    List<CcmtsSpectrumGpHopHis> getGroupHopHisFromDevice(SnmpParam snmpParam);

    /**
     * 在设备上新增跳频组
     * 
     * @param snmpParam
     * @param ccmtsSpectrumGp
     */
    void createGroupToDevice(SnmpParam snmpParam, CcmtsSpectrumGp ccmtsSpectrumGp);

    /**
     * 在设备上新增跳频组成员频点
     * 
     * @param snmpParam
     * @param ccmtsSpectrumGpFreq
     */
    void createGroupFreqToDevice(SnmpParam snmpParam, CcmtsSpectrumGpFreq ccmtsSpectrumGpFreq);

}
