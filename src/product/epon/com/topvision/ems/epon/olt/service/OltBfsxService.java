/***********************************************************************
 * $Id: OltBfsxService.java,v1.0 2014年9月24日 上午11:32:12 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.olt.service;

import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2014年9月24日-上午11:32:12
 *
 */
public interface OltBfsxService extends Service {

    /**
     * 刷新OLT的可见信息
     * @param entityId
     */
    void bfsxOltInfo(long entityId);

    /**
     * 刷新OLT VLAN的数据
     * @param entityId
     */
    void bfsxOltVlan(Long entityId);

    void bfsxOltMirror(Long entityId);

    void bfsxOltTrunk(Long entityId);

    void bfsxOltPonProtectGroup(Long entityId);
}
