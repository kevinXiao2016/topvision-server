/***********************************************************************
 * $Id: VlanListService.java,v1.0 2016年6月8日 上午10:17:48 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.service;

import java.util.List;

import com.topvision.ems.epon.vlan.domain.OltPortVlan;
import com.topvision.ems.epon.vlan.domain.PonVlanPortLocation;
import com.topvision.ems.epon.vlan.domain.UniPortVlan;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2016年6月8日-上午10:17:48
 *
 */
public interface VlanListService extends Service {

    /**
     * 刷新VLAN的所有数据
     * @param entityId
     * @param jconnectionId 
     */
    void refreshVlan(Long entityId, String jconnectionId);

    /**
     * @param entityId
     * @return
     */
    List<OltPortVlan> loadSniPortVlanList(Long entityId);

    /**
     * 批量应用VLAN
     * @param entityId
     * @param sniIndexs
     * @param taggedList
     * @param untaggedList
     */
    void batchApplyPortVlanConfig(Long entityId, List<Long> sniIndexs, List<Integer> taggedList,
            List<Integer> untaggedList);

    /**
     * @param onuId
     * @return
     */
    List<UniPortVlan> loadUniPortVlan(Long onuId);

    /**
     * @param entityId
     * @return
     */
    List<OltPortVlan> loadPonPortVlanList(Long entityId);

    /**
     * @param ponId
     * @param targetPonIdList
     * @param services
     * @param jconnectionId
     */
    void batchCopyPonServiceVlanConfig(Long ponId, List<Long> targetPonIdList, String services, String jconnectionId);

    /**
     * @param entityId
     * @return
     */
    List<PonVlanPortLocation> loadSlotPonList(Long entityId);

}
