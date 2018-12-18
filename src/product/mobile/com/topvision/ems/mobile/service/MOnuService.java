/***********************************************************************
 * $Id: MOnuService.java,v1.0 2016年7月16日 下午4:16:55 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.service;

import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.mobile.domain.MobileOnu;

/**
 * @author flack
 * @created @2016年7月16日-下午4:16:55
 *
 */
public interface MOnuService {

    /**
     * 获取ONU基本信息
     * @param onuId
     * @return
     */
    public MobileOnu getOnuBaseInfo(Long onuId);

    /**
    * 获取ONU 能力信息
    * 
    * @param onuId
    * @return OltOnuCapability
    */
    public OltOnuCapability getOnuCapabilityInfo(Long onuId);

    /**
     * 获取ONU基本信息
     * 
     * @param entityId
     * @param onuId
     * @return
     */
    public Long getOnuPonIndex(Long entityId, Long onuId);

    /**
     * 通过ONU Id 获得上联PON口的光功率
     * 
     * @param entityId
     * @param onuId
     * @return
     */
    public OltPonOptical getOnuLinkPonOptical(Long onuId);

}
