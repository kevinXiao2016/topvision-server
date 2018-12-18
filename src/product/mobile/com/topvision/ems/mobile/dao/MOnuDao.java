/***********************************************************************
 * $Id: MOnuDao.java,v1.0 2016年7月18日 下午5:19:44 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.dao;

import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.mobile.domain.MobileOnu;

/**
 * @author flack
 * @created @2016年7月18日-下午5:19:44
 *
 */
public interface MOnuDao {

    /**
     * 获取ONU基本信息
     * @param onuId
     * @return
     */
    public MobileOnu queryOnuBaseInfo(Long onuId);

    public Long getOnuPonIndex(Long entityId, Long onuId);

    public OltPonOptical getOnuLinkPonOptical(Long onuId);

}
