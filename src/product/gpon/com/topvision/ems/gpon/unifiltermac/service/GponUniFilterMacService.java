/***********************************************************************
 * $Id: GponUniFilterMacService.java,v1.0 2016年12月24日 上午8:58:38 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.unifiltermac.service;

import java.util.List;

import com.topvision.ems.gpon.unifiltermac.facade.domain.GponUniFilterMac;

/**
 * @author jay
 * @created @2016年12月24日-上午8:58:38
 *
 */
public interface GponUniFilterMacService {

    /**
     * 加载一个uni口下的所有过滤列表
     * @param uniId
     * @return
     */
    List<GponUniFilterMac> loadGponUniFilterMacList(Long uniId);

    /**
     * 添加一个mac过滤
     * @param uniId
     * @param mac
     */
    Boolean addGponUniFilterMac(Long uniId, String mac);

    /**
     * 删除一个mac过滤
     * @param uniId
     * @param mac
     */
    void deleteGponUniFilterMac(Long uniId, String mac);

    /**
     * 刷新一个uni口下的所有mac过滤列表  由于设备缺陷，现在只能实现为全设备刷新
     * @param uniId
     */
    void refreshGponUniFilterMac(Long uniId);

    void refreshDeviceUniFilterMac(Long entityId);
}
