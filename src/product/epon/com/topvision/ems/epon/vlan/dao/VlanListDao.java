/***********************************************************************
 * $Id: VlanListDao.java,v1.0 2016年6月8日 上午10:54:41 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.dao;

import java.util.List;

import com.topvision.ems.epon.vlan.domain.OltPortVlan;
import com.topvision.ems.epon.vlan.domain.OltPortVlanRelation;
import com.topvision.ems.epon.vlan.domain.PonVlanPortLocation;
import com.topvision.ems.epon.vlan.domain.UniPortVlan;
import com.topvision.framework.dao.Dao;

/**
 * @author Bravin
 * @created @2016年6月8日-上午10:54:41
 *
 */
public interface VlanListDao extends Dao {

    /**
     * @param entityId
     * @return
     */
    List<OltPortVlan> selectSniPortVlanList(Long entityId);

    /**
     * @param entityId
     * @return
     */
    List<OltPortVlanRelation> selectPortVlanRelation(Long entityId);

    /**
     * @param onuId
     * @return
     */
    List<UniPortVlan> selectUniPortVlan(Long onuId);

    /**
     * @param entityId
     * @return
     */
    List<OltPortVlan> selectPonPortVlanList(Long entityId);

    /**
     * @param entityId
     * @return
     */
    List<PonVlanPortLocation> selectSlotPonList(Long entityId);

}
