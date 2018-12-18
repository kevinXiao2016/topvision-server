/***********************************************************************
 * $Id: GponProfileDao.java,v1.0 2016年10月30日 下午5:42:23 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.gpon.profile.dao;

import java.util.List;

import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfileEthPortConfig;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortNumProfile;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanAggregation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanCfg;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTranslation;
import com.topvision.ems.gpon.profile.facade.domain.GponSrvProfilePortVlanTrunk;
import com.topvision.framework.dao.Dao;

/**
 * @author Bravin
 * @created @2016年10月30日-下午5:42:23
 *
 */
public interface GponProfileDao extends Dao {

    List<GponSrvProfilePortVlanAggregation> selectGponSrvProfilePortVlanAggregationList(Long entityId);

    List<GponSrvProfilePortNumProfile> selectGponSrvProfilePortNumProfileList(Long entityId);

    List<GponSrvProfileCfg> selectGponSrvProfileCfgList(Long entityId);

    List<GponSrvProfilePortVlanTrunk> selectGponSrvProfilePortVlanTrunkList(Long entityId);

    List<GponSrvProfilePortVlanTranslation> selectGponSrvProfilePortVlanTranslationList(Long entityId);
    
    void deleteGponSrvProfilePortNumProfile(GponSrvProfilePortNumProfile object);

    void deleteGponSrvProfileEthPortConfig(GponSrvProfileEthPortConfig object);

    void deleteGponSrvProfilePortVlanCfg(GponSrvProfilePortVlanCfg object);

    void deleteGponSrvProfileCfg(GponSrvProfileCfg object);

}
