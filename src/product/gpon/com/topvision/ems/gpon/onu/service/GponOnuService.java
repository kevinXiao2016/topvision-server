/***********************************************************************
 * $Id: GponOnuService.java,v1.0 2016年10月17日 下午3:40:55 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.onu.service;

import java.util.HashMap;
import java.util.List;

import com.topvision.ems.gpon.onu.domain.GponOnuCapability;
import com.topvision.ems.gpon.onu.domain.GponOnuInfoSoftware;
import com.topvision.ems.gpon.onu.domain.GponOnuIpHost;
import com.topvision.ems.gpon.onu.domain.GponUniAttribute;

/**
 * @author Rod John
 * @created @2016年10月17日-下午3:40:55
 *
 */
public interface GponOnuService {

    /**
     * 查询GPON ONU能力信息
     * @param onuId
     * @return
     */
    GponOnuCapability queryForGponOnuCapability(long onuId);

    GponOnuInfoSoftware queryForGponOnuSoftware(Long onuId);

    List<GponOnuIpHost> loadGponOnuIpHost(HashMap<String, Object> map);

    void addGponOnuIpHost(GponOnuIpHost gponOnuIpHost);

    GponOnuIpHost getGponOnuIpHost(Long onuId, Integer onuIpHostIndex);

    void modifyGponOnuIpHost(GponOnuIpHost gponOnuIpHost);

    void delGponOnuIpHost(GponOnuIpHost gponOnuIpHost);

    void refreshOnuIpHost(Long entityId);

    List<Integer> usedHostIpIndex(Long onuId);

    List<GponUniAttribute> loadGponOnuUniList(Long onuId);

    GponUniAttribute loadUniVlanConfig(Long uniId);

    void setUniVlanConfig(Long entityId, Long uniId, Integer gponOnuUniPri, Integer gponOnuUniPvid);

    void refreshGponOnuSoftware(Long entityId, Long onuIndex);
}
