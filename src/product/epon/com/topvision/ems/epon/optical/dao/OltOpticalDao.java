/***********************************************************************
 * $Id: OltOpticalDao.java,v1.0 2013-10-26 上午09:19:12 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.optical.dao;

import java.util.List;

import com.topvision.ems.epon.optical.domain.OltOnuOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OltPonOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltSniOptical;
import com.topvision.ems.epon.optical.domain.OltSysOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OnuPonOptical;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author lizongtian
 * @created @2013-10-26-上午09:19:12
 *
 */
public interface OltOpticalDao extends BaseEntityDao<Object> {

    /**
     * 得到SNI光口光信息
     * 
     * @param entityId
     * @param portId
     * @return
     */
    public OltSniOptical getOltSniOptical(Long portId);

    /**
    * 得到PON口光信息
    * 
    * @param entityId
    * @param portId
    * @return
    */
    public OltPonOptical getOltPonOptical(Long portId);

    /**
    * 添加PON口光信息
    * 
    * @param oltPonOptical
    */
    public void addOltPonOptical(OltPonOptical oltPonOptical);

    /**
    * 修改PON口光信息
    * 
    * @param oltPonOptical
    */
    public void updateOltPonOptical(OltPonOptical oltPonOptical);

    /**
    * 添加SNI口光信息
    * 
    * @param oltSniOptical
    */
    public void addOltSniOptical(OltSniOptical oltSniOptical);

    /**
    * 修改SNI口光信息
    * 
    * @param oltSniOptical
    */
    public void updateOltSniOptical(OltSniOptical oltSniOptical);

    /**
    * 添加ONU PON口光信息
    * 
    * @param OnuPonOptical
    */
    public void addOnuPonOptical(OnuPonOptical onuPonOptical);

    /**
    * 修改ONU PON口光信息
    * 
    * @param OnuPonOptical
    */
    public void updateOnuPonOptical(OnuPonOptical onuPonOptical);

    /**
     * 批量添加OLT全局光阈值
     * 
     * @param oltPonOpticals
     * @param entityId
     */
    public void batchInsertOltOptical(List<OltSysOpticalAlarm> oltPonOpticals, Long entityId);

    /**
    * 批量添加PON口光信息
    * 
    * @param oltPonOpticals
    * @param entityId
    */
    public void batchInsertPonOptical(List<OltPonOptical> oltPonOpticals, Long entityId);

    /**
    * 批量添加SNI口光信息
    * 
    * @param oltSniOpticals
    * @param entityId
    */
    public void batchInsertSniOptical(List<OltSniOptical> oltSniOpticals, Long entityId);

    /**
    * 批量添加ONU PON口光信息
    * 
    * @param OnuPonOptical
    * @param entityId
    */
    public void batchInsertOnuPonOptical(List<OnuPonOptical> onuPonOpticals, Long entityId);

    /**
    * 得到ONU PON口光信息
    * 
    * @param entityId
    * @param onuPonIndex
    * @return
    */
    public OnuPonOptical getOnuPonOptical(Long entityId, Long onuPonIndex);

    /**
    * 得到所有PON口的光信息
    * 
    * @param entityId
    * @return
    */
    public List<OltPonOptical> getAllPonOptical(Long entityId);

    /**
    * 得到所有SNI光口的光信息
    * 
    * @param entityId
    * @return
    */
    public List<OltSniOptical> getAllSniOptical(Long entityId);

    /**
    * 获取ONU PON ID
    * 
    * @param entityId
    * @param onuPonIndex
    * @return
    */
    public Long getOnuPonId(Long entityId, Long onuPonIndex);

    /**
    * 获取所有ONU的PON口光信息
    * 
    * @param entityId
    * @return
    */
    public List<OnuPonOptical> getAllOnuOptical(Long entityId);

    /**
     * 获取全局光功率阀值告警
     * @param sysOpticalAlarm
     * @return
     */
    public OltSysOpticalAlarm querySysOpticalAlarm(OltSysOpticalAlarm sysOpticalAlarm);

    /**
     * 获取全局光功率阀值告警列表
     * @param entityId
     * @return
     */
    public List<OltSysOpticalAlarm> querySysOpticalAlarmList(Long entityId);

    /**
     * 插入全局光功率告警
     * @param sysOpticalAlarm
     */
    public void insertSysOpticalAlarm(OltSysOpticalAlarm sysOpticalAlarm);

    /**
     * 修改全局光功率告警
     * @param sysOpticalAlarm
     */
    public void updateSysOpticalAlarm(OltSysOpticalAlarm sysOpticalAlarm);

    /**
     * 删除全局光功率告警
     * @param sysOpticalAlarm
     */
    public void deleteSysOpticalAlarm(OltSysOpticalAlarm sysOpticalAlarm);

    /**
     * 获取ONU光功率告警
     * @param onuOptical
     * @return
     */
    public List<OltOnuOpticalAlarm> queryOnuOpticalAlarmList(OltOnuOpticalAlarm onuOptical);

    /**
     * 插入ONU光功率告警
     * @param onuOpticalAlarm
     */
    public void insertOnuOpticalAlarm(OltOnuOpticalAlarm onuOpticalAlarm);

    /**
     * 批量插入ONU光功率告警
     * @param onuOpticalAlarmList
     */
    public void batchInsertOnuOpticalAlarm(List<OltOnuOpticalAlarm> onuOpticalAlarmList, Long entityId);

    /**
     * 获取Pon光功率告警
     * @param ponOptical
     * @return
     */
    public List<OltPonOpticalAlarm> queryPonOpticalAlarmList(OltPonOpticalAlarm ponOptical);

    /**
     * 插入Pon光功率告警
     * @param ponOpticalAlarm
     */
    public void insertPonOpticalAlarm(OltPonOpticalAlarm ponOpticalAlarm);

    /**
     * 批量插入Pon光功率告警
     * @param ponOpticalAlarmList
     */
    public void batchInsertPonOpticalAlarm(List<OltPonOpticalAlarm> ponOpticalAlarmList, Long entityId);

    /**
     * 修改PON端口光阈值信息
     * @param list
     * @param entityId
     */
    public void updateOltPonOptAlarm(List<OltPonOpticalAlarm> list, Long entityId);

    /**
     * 删除PON端口光阈值信息
     * @param oltPonOpticalAlarm
     */
    public void deleteOltPonOptAlarm(OltPonOpticalAlarm oltPonOpticalAlarm);

    /**
     * 修改ONU光阈值信息
     * @param list
     * @param entityId
     */
    public void updateOltOnuOptAlarm(List<OltOnuOpticalAlarm> list, Long entityId);

    /**
     * 删除ONU光阈值信息
     * @param onuOpticalAlarm
     */
    public void deleteOnuOptAlarm(OltOnuOpticalAlarm onuOpticalAlarm);
}
