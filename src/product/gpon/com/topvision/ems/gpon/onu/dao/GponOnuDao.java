/***********************************************************************
 * $Id: GponOnuDao.java,v1.0 2016年10月17日 下午4:03:57 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.dao;

import java.util.HashMap;
import java.util.List;

import com.topvision.ems.gpon.onu.domain.GponOnuAttribute;
import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.domain.GponOnuInfoSoftware;
import com.topvision.ems.gpon.onu.domain.GponOnuIpHost;
import com.topvision.ems.gpon.onu.domain.GponOnuUniPvid;
import com.topvision.ems.gpon.onu.domain.GponUniAttribute;
import com.topvision.ems.gpon.onuvoip.domain.TopGponOnuCapability;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Rod John
 * @created @2016年10月17日-下午4:03:57
 *
 */
public interface GponOnuDao extends BaseEntityDao<GponOnuAttribute> {

    /**
     * syncGponOnuCapability
     * 
     * @param gponOnuCapabilities
     * @param topGponOnuCapabilities 
     * @param onuMap
     */
    void syncGponOnuCapability(List<GponOnuCapability> gponOnuCapabilities, HashMap<Long, Long> onuMap);

    /**
     * syncGponOnuIpHosts
     * 
     * @param entityId
     * @param gponOnuIpHosts
     */
    void syncGponOnuIpHosts(Long entityId, List<GponOnuIpHost> gponOnuIpHosts);

    /**
     * syncGponSoftware
     * 
     * @param entityId
     * @param gponOnuInfoSoftwares
     */
    void syncGponSoftware(Long entityId, List<GponOnuInfoSoftware> gponOnuInfoSoftwares);

    void syncGponOnuUniPvid(Long entityId, List<GponOnuUniPvid> gponOnuUniPvidList);

    /**
     * @param onuId
     * @return
     */
    GponOnuCapability queryForGponOnuCapability(long onuId);

    GponOnuInfoSoftware queryForGponOnuSoftware(Long onuId);

    List<GponOnuIpHost> loadGponOnuIpHost(HashMap<String, Object> map);

    void addGponOnuIpHost(GponOnuIpHost gponOnuIpHost);

    GponOnuIpHost getGponOnuIpHost(Long onuId, Integer onuIpHostIndex);

    void modifyGponOnuIpHost(GponOnuIpHost gponOnuIpHost);

    void delGponOnuIpHost(Long onuId, Integer onuIpHostIndex);

    void delGponOnuIpHostByOnuId(Long onuId);

    List<Integer> usedHostIpIndex(Long onuId);

    List<GponUniAttribute> loadGponOnuUniList(Long onuId);

    GponUniAttribute loadUniVlanConfig(Long uniId);

    void setUniVlanConfig(Long uniId, Integer gponOnuUniPri, Integer gponOnuUniPvid);

}
