/***********************************************************************
 * $Id: OltOpticalService.java,v1.0 2013-10-26 上午09:16:48 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.optical.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.optical.domain.OltOnuOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OltPonOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltSniOptical;
import com.topvision.ems.epon.optical.domain.OltSysOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OnuPonOptical;
import com.topvision.framework.service.Service;

/**
 * @author lizongtian
 * @created @2013-10-26-上午09:16:48
 *
 */
public interface OltOpticalService extends Service {

    /**
    * 获取SNI光口的光信息
    * @param entityId
    * @param portIndex
    * @return
    */
    public OltSniOptical loadOltSniOptical(Long entityId, Long portIndex);

    /**
    * 获取PON口光信息
    * @param entityId
    * @param portIndex
    * @return
    */
    public OltPonOptical loadOltPonOptical(Long entityId, Long portIndex);

    /**
    * 获取ONU PON口光信息
    * @param entityId
    * @param onuPonIndex
    * @return
    */
    public OnuPonOptical loadOnuPonOptical(Long entityId, Long onuPonIndex);

    /**
     * 获取ONU PON口光信息
     * @param entityId
     * @param onuIndex
     * @return
     */
    public OnuPonOptical getOnuOpticalInfo(Long entityId, Long onuIndex);

    /**
    * 得到SNI光口光信息
    * @param entityId
    * @param portId
    * @return
    */
    public OltSniOptical getOltSniOptical(Long portId);

    /**
    * 得到PON口光信息
    * @param entityId
    * @param portId
    * @return
    */
    public OltPonOptical getOltPonOptical(Long portId);

    /**
    * 得到ONU PON口光信息
    * @param entityId
    * @param onuPonIndex
    * @return
    */
    public OnuPonOptical getOnuPonOptical(Long entityId, Long onuPonIndex);

    /**
    * 获取OLT上所有光口的光信息
    * @param entityId
    * @return
    */
    public List<Map<String, String>> getOltAllPortOptical(Long entityId);

    /**
    * 获取所有ONU的PON口光信息
    * @param entityId
    * @return
    */
    public List<OnuPonOptical> getAllOnuOptical(Long entityId);

    /**
    * 获取全局光功率阀值告警
    * @param sysOpticalAlarm
    * @return
    */
    public OltSysOpticalAlarm getSysOpticalAlarm(OltSysOpticalAlarm sysOpticalAlarm);

    /**
     * 获取全局光功率阀值告警列表
     * @param entityId
     * @return
     */
    public List<OltSysOpticalAlarm> getSysOpticalAlarmList(Long entityId);

    /**
    * 修改全局光功率告警
    * @param list
    */
    public void modifySysOpticalAlarm(List<OltSysOpticalAlarm> list);

    /**
    * 获取ONU光功率告警
    * @param onuOptical
    * @return
    */
    public List<OltOnuOpticalAlarm> getOnuOpticalAlarmList(OltOnuOpticalAlarm onuOptical);

    /**
    * 获取Pon光功率告警
    * @param ponOptical
    * @return
    */
    public List<OltPonOpticalAlarm> getPonOpticalAlarmList(OltPonOpticalAlarm ponOptical);

    /**
    * 配置OLT全局光阈值
    * @param entityId
    * @param list
    */
    public void modifySysOpticalAlarm(Long entityId, List<OltSysOpticalAlarm> list);

    /**
    * 配置OLT PON端口光阈值
    * @param entityId
    * @param list
    */
    public void modifyOltPonOptAlarm(Long entityId, List<OltPonOpticalAlarm> list);

    /**
     * 删除OLT PON端口光阈值
     * @param onOpticalAlarm
     */
    public void deletePonOpticalAlarm(OltPonOpticalAlarm onOpticalAlarm);

    /**
    * 配置OLT ONU光阈值
    * @param entityId
    * @param list
    */
    public void modifyOltOnuOptAlarm(Long entityId, List<OltOnuOpticalAlarm> list);

    /**
     * 清除OLT ONU光阈值
     * @param onuOpticalAlarm
     */
    public void deleteOnuOptAlarm(OltOnuOpticalAlarm onuOpticalAlarm);

    /**
     * 刷新全局光阈值配置
     * @param entityId
     */
    void refreshOltSysOpticalAlarm(long entityId);

    /**
     * 刷新OLT PON端口光阈值配置
     * @param entityId
     */
    void refreshOltPonOpticalAlarm(long entityId);

    /**
     * 刷新OLT ONU端口光阈值配置
     * @param entityId
     */
    void refreshOltOnuOpticalAlarm(long entityId);
    
    /**
     * 刷新OLT PON口光信息
     * @param entityId
     */
    void refreshOltPonOptical(Long entityId);
    
    /**
     * 刷新OLT SNI口光信息
     * @param entityId
     */
    void refreshOltSniOptical(Long entityId);
}
