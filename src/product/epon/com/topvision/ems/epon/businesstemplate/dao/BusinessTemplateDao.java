/***********************************************************************
 * $Id: BusinessTemplateDao.java,v1.0 2015-12-8 下午2:06:59 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.businesstemplate.dao;

import java.util.List;

import com.topvision.ems.epon.businesstemplate.domain.OnuCapability;
import com.topvision.ems.epon.businesstemplate.domain.OnuIgmpProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuPortVlanProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvIgmpVlanTrans;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvProfile;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author lizongtian
 * @created @2015-12-8-下午2:06:59
 *
 */
public interface BusinessTemplateDao extends BaseEntityDao<OnuSrvProfile> {

    List<OnuSrvProfile> getOnuSrvProfiles(Long entityId);

    List<OnuIgmpProfile> getOnuIgmpProfiles(Long entityId, Integer profileId);

    List<OnuPortVlanProfile> getOnuPortVlanProfiles(Long entityId, Integer profileId);

    List<OnuCapability> getOnuCapabilitys(Long entityId);

    OnuSrvProfile getOnuSrvProfile(Long entityId, Integer profileId);

    OnuIgmpProfile getOnuIgmpProfile(Long entityId, Integer profileId, Integer igmpPortId);

    OnuPortVlanProfile getOnuPortVlanProfile(Long entityId, Integer profileId, Integer srvPortId);

    void deleteOnuSrvProfile(OnuSrvProfile srvProfile);

    void deleteOnuIgmpProfile(OnuIgmpProfile igmpProfile);

    void deleteOnuPortVlanProfile(OnuPortVlanProfile portVlanProfile);

    void deleteOnuSrvIgmpVlanTrans(OnuSrvIgmpVlanTrans igmpVlanTrans);

    void deleteOnuCapability(OnuCapability capability);

    void updateOnuSrvProfile(OnuSrvProfile srvProfile);

    void updateOnuIgmpProfile(OnuIgmpProfile igmpProfile);

    void updateOnuPortVlanProfile(OnuPortVlanProfile portVlanProfile);

    void addOnuSrvProfile(OnuSrvProfile srvProfile);

    void addOnuIgmpProfile(OnuIgmpProfile igmpProfile);

    void addOnuPortVlanProfile(OnuPortVlanProfile portVlanProfile);

    void addOnuSrvIgmpVlanTrans(OnuSrvIgmpVlanTrans igmpVlanTrans);

    void addOnuCapability(OnuCapability capability);

    void batchInsertOnuSrvProfile(Long entityId, List<OnuSrvProfile> srvProfiles);

    void batchInsertOnuIgmpProfile(Long entityId, List<OnuIgmpProfile> igmpProfiles);

    void batchInsertOnuPortVlanProfile(Long entityId, List<OnuPortVlanProfile> portVlanProfiles);

    void batchInsertOnuIgmpVlanTrans(Long entityId, List<OnuSrvIgmpVlanTrans> igmpVlanTrans);

    void batchInsertOnuCapability(Long entityId, List<OnuCapability> capabilitys);

    /**
     * 修改业务模板与能力集的绑定关系
     * @param srvProfile
     */
    void updateProfileBindCap(OnuSrvProfile srvProfile);

    List<OnuSrvIgmpVlanTrans> loadOnuIgmpVlanTrans(Long entityId, Integer profileId, Integer portId);
}
