/***********************************************************************
 * $Id: GponUniFilterMacDao.java,v1.0 2016年12月24日 下午5:42:23 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.unifiltermac.dao;


import com.topvision.ems.gpon.unifiltermac.facade.domain.GponUniFilterMac;
import com.topvision.framework.dao.Dao;

import java.util.List;

/**
 * @author jay
 * @created @2016年12月24日-下午5:42:23
 *
 */
public interface GponUniFilterMacDao extends Dao {

    /**
     * 载入一个uni口下的mac过滤列表
     * @param uniId
     * @return
     */
    List<GponUniFilterMac> loadGponUniFilterMacList(Long uniId);

    /**
     * 添加一条mac过滤列表
     * @param gponUniFilterMac
     */
    void addGponUniFilterMac(GponUniFilterMac gponUniFilterMac);

    /**
     * 删除一条mac过滤列表
     * @param gponUniFilterMac
     */
    void deleteGponUniFilterMac(GponUniFilterMac gponUniFilterMac);

    /**
     * 批量插入entity下的mac过滤列表
     * @param gponUniFilterMacs
     */
    void insertGponUniFilterMacForEntity(Long entityId ,List<GponUniFilterMac> gponUniFilterMacs);
    /**
     * 批量插入uni口下的mac过滤列表
     * @param gponUniFilterMacs
     */
    void insertGponUniFilterMacForUni(Long entityId ,List<GponUniFilterMac> gponUniFilterMacs);

    /**
     * 获取GponUniFilterMac
     * @param uniId
     * @param mac
     * @return
     */
    GponUniFilterMac loadGponUniFilterMac(Long uniId, String mac);
}
