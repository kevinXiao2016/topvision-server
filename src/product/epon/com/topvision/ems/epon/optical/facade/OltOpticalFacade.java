/***********************************************************************
 * $Id: OltOpticalFacade.java,v1.0 2013-10-26 上午09:33:11 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.optical.facade;

import java.util.List;

import com.topvision.ems.epon.optical.domain.OltOnuOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OltPonOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltSniOptical;
import com.topvision.ems.epon.optical.domain.OltSysOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OnuPonOptical;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author lizongtian
 * @created @2013-10-26-上午09:33:11
 *
 */
@EngineFacade(serviceName = "OltOpticalFacade", beanName = "oltOpticalFacade")
public interface OltOpticalFacade extends Facade {

    /**
     * 获取一条SNI口光信息
     * 
     * @param snmpParam
     * @param slotNo
     * @param portNo
     * @return
     */
    public OltSniOptical loadOltSniOptical(SnmpParam snmpParam, Integer slotNo, Integer portNo);

    /**
     * 获取SNI口光功率信息
     * 同时获取多条记录
     * @param snmpParam
     * @param sniList
     * @return
     */
    List<OltSniOptical> getSniOpticalList(SnmpParam snmpParam, List<OltSniOptical> sniList);

    /**
    * 获取一条PON口光信息
    * 
    * @param snmpParam
    * @param slotNo
    * @param portNo
    * @return
    */
    public OltPonOptical loadOltPonOptical(SnmpParam snmpParam, Integer slotNo, Integer portNo);

    /**
     * 获取PON口光功率信息
     * 同时获取多条
     * @param snmpParam
     * @param ponList
     * @return
     */
    List<OltPonOptical> getPonOpticalList(SnmpParam snmpParam, List<OltPonOptical> ponList);

    /**
    * 获取一条ONU PON口光信息
    * 
    * @param snmpParam
    * @param deviceIndex
    * @return
    */
    public OnuPonOptical loadOnuPonOptical(SnmpParam snmpParam, Long deviceIndex);

    // 暂时未调用
    public List<OltSniOptical> getOltSniOptical(SnmpParam snmpParam);

    // 暂时未调用
    public List<OltPonOptical> getOtlPonOptical(SnmpParam snmpParam);

    /**
     *  配置OLT全局光阈值
     * @param snmpParam
     * @param alarm
     * @return 
     */
    public OltSysOpticalAlarm modifySysOpticalAlarm(SnmpParam snmpParam, OltSysOpticalAlarm alarm);

    /**
     * 配置OLT PON端口光阈值
     * @param snmpParam
     * @param thresh
     * @return 
     */
    public OltPonOpticalAlarm modifyOltPonOptAlarm(SnmpParam snmpParam, OltPonOpticalAlarm thresh);

    /**
     * 删除OLT PON端口光阈值
     * @param snmpParam
     * @param thresh
     * @return 
     */
    public OltPonOpticalAlarm deleteOltPonOptAlarm(SnmpParam snmpParam, OltPonOpticalAlarm thresh);

    /**
     * 配置OLT ONU光阈值
     * @param snmpParam
     * @param thresh
     * @return 
     */
    public OltOnuOpticalAlarm modifyOltOnuOptAlarm(SnmpParam snmpParam, OltOnuOpticalAlarm thresh);

    /**
     * 删除OLT ONU光阈值
     * @param snmpParam
     * @param thresh
     * @return 
     */
    public OltOnuOpticalAlarm deleteOnuOptAlarm(SnmpParam snmpParam, OltOnuOpticalAlarm thresh);

    /**
     * 获取OLT ONU光阈值
     * @param snmpParam
     * @param thresh
     * @return 
     */
    public OltOnuOpticalAlarm getOnuOptAlarm(SnmpParam snmpParam, OltOnuOpticalAlarm thresh);

    /**
     * 获取全局光阈值信息列表
     * @param snmpParam
     * @return
     */
    public List<OltSysOpticalAlarm> getOltSysOpticalAlarm(SnmpParam snmpParam);

    /**
     *  刷新OLT ONU端口光阈值配置
     * @param snmpParam
     * @return
     */
    public List<OltOnuOpticalAlarm> getOltOnuOpticalAlarm(SnmpParam snmpParam);

    /**
     *  刷新OLT PON端口光阈值配置
     * @param snmpParam
     * @return
     */
    public List<OltPonOpticalAlarm> getOltPonOpticalAlarm(SnmpParam snmpParam);
}
