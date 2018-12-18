/***********************************************************************
 * $Id: OnuVoipDao.java,v1.0 2017年5月4日 上午11:25:06 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onuvoip.dao;

import java.util.HashMap;
import java.util.List;

import com.topvision.ems.gpon.onu.domain.GponOnuAttribute;
import com.topvision.ems.gpon.onuvoip.domain.TopOnuIfPotsInfo;
import com.topvision.ems.gpon.onuvoip.domain.TopGponOnuPots;
import com.topvision.ems.gpon.onuvoip.domain.TopSIPPstnUser;
import com.topvision.ems.gpon.onuvoip.domain.TopVoIPLineStatus;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author haojie
 * @created @2017年5月4日-上午11:25:06
 *
 */
public interface OnuVoipDao extends BaseEntityDao<GponOnuAttribute> {

    /**
     * 批量插入或更新pots口线路状态
     * 
     * @param entityId
     * @param topVoIPLineStatusList
     */
    void batchInsertOrUpdateTopVoIPLineStatus(Long entityId, List<TopVoIPLineStatus> topVoIPLineStatusList);

    /**
     * 批量插入或更新voip用户配置信息
     * 
     * @param entityId
     * @param topSIPPstnUserList
     */
    void batchInsertOrUpdateTopSIPPstnUser(Long entityId, List<TopSIPPstnUser> topSIPPstnUserList);

    /**
     * 获取pots口线路状态信息
     * 
     * @param onuId
     * @return
     */
    TopVoIPLineStatus getTopVoIPLineStatus(Long onuId);

    /**
     * 修改pots口用户配置信息
     * 
     * @param topSIPPstnUser
     */
    void updateTopSIPPstnUser(TopSIPPstnUser topSIPPstnUser);

    /**
     * 批量插入或更新pots口状态
     * 
     * @param entityId
     * @param potsInfoList
     */
    void batchInsertOrUpdateTopOnuIfPotsInfo(Long entityId, List<TopOnuIfPotsInfo> potsInfoList);

    /**
     * 插入或更新pots口状态
     * 
     * @param entityId
     * @param potsInfo
     */
    void insertOrUpdateTopOnuIfPotsInfo(Long entityId, TopOnuIfPotsInfo potsInfo);

    /**
     * 从数据库查询GPON ONU POTS口信息
     * 
     * @param onuId
     * @return
     */
    List<TopGponOnuPots> getGponOnuPotsList(Long onuId);

    /**
     * 根据onuId和potsIdx获取pots信息
     * 
     * @param map
     * @return
     */
    TopSIPPstnUser getTopSIPPstnUser(HashMap<String, Object> map);

    /**
     * 更新设置pots口使能
     * @param onuId
     * @param topSIPPstnUserPotsIdx
     * @param potsAdminStatus
     */
    void updatePotsAdminStatus(Long onuId, Integer topSIPPstnUserPotsIdx, Integer potsAdminStatus);

}
