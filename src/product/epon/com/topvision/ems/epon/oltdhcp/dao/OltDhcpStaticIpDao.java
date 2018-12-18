/***********************************************************************
 * $Id: OltDhcpStaticIpDao.java,v1.0 2017年11月22日 上午8:51:58 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStaticIp;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author haojie
 * @created @2017年11月22日-上午8:51:58
 *
 */
public interface OltDhcpStaticIpDao extends BaseEntityDao<Object> {

    /**
     * 获取静态IP列表信息
     * 
     * @param queryMap
     * @return
     */
    List<TopOltDhcpStaticIp> getOltDhcpStaticIp(Map<String, Object> queryMap);

    /**
     * 获取静态IP数量
     * 
     * @param queryMap
     * @return
     */
    Long getOltDhcpStaticIpCount(Map<String, Object> queryMap);

    /**
     * 插入静态IP
     * 
     * @param staticIp
     */
    void insertOltDhcpStaticIp(TopOltDhcpStaticIp staticIp);

    /**
     * 删除静态IP
     * 
     * @param entityId
     * @param ipIndex
     * @param maskIndex
     */
    void deleteOltDhcpStaticIp(Long entityId, String ipIndex, String maskIndex);

    /**
     * 更新防静态IP开关
     * 
     * @param entityId
     * @param sourceVerifyEnable
     */
    void updateOltDhcpSourceVerifyEnable(Long entityId, Integer sourceVerifyEnable);

    /**
     * 获取OLT下的槽位号列表
     * 
     * @param entityId
     * @return
     */
    List<Long> selectOltSlotIdList(Long entityId);

    /**
     * 获取OLT下的某槽位的端口号列表
     * 
     * @param map
     * @return
     */
    List<Long> selectOltSlotPonIndexList(Map<String, Object> map);

    /**
     * 获取OLT下某槽位某端口下的onuIndex
     * 
     * @param map
     * @return
     */
    List<Long> selectOltSlotPonOnuIndexList(Map<String, Object> map);

    /**
     * 获取staticIp数量
     * 
     * @param staticIp
     * @return
     */
    Long getOnuStaticIpCount(TopOltDhcpStaticIp staticIp);

    /**
     * 查询唯一静态IP
     * 
     * @param entityId
     * @param ipIndex
     * @param maskIndex
     * @return
     */
    TopOltDhcpStaticIp selectUniqueStaticIp(Long entityId, String ipIndex, String maskIndex);

}
