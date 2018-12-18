/***********************************************************************
 * $Id: BusinessTemplateService.java,v1.0 2015-12-8 下午2:00:49 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.businesstemplate.service;

import java.util.List;

import com.topvision.ems.epon.businesstemplate.domain.OnuCapability;
import com.topvision.ems.epon.businesstemplate.domain.OnuIgmpProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuPortVlanProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvIgmpVlanTrans;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvProfile;
import com.topvision.framework.service.Service;

/**
 * @author lizongtian
 * @created @2015-12-8-下午2:00:49
 *
 */
public interface BusinessTemplateService extends Service {

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

    void deleteOnuSrvIgmpVlanTrans(OnuSrvIgmpVlanTrans onuIgmpVlanTrans);

    void deleteOnuCapability(OnuCapability capability);

    void updateOnuSrvProfile(OnuSrvProfile srvProfile);

    void updateOnuIgmpProfile(OnuIgmpProfile igmpProfile);

    void updateOnuPortVlanProfile(OnuPortVlanProfile portVlanProfile);

    void addOnuSrvProfile(OnuSrvProfile srvProfile);

    void addOnuIgmpProfile(OnuIgmpProfile igmpProfile);

    void addOnuPortVlanProfile(OnuPortVlanProfile portVlanProfile);

    void addOnuSrvIgmpVlanTrans(OnuSrvIgmpVlanTrans onuIgmpVlanTrans);

    void addOnuCapability(OnuCapability capability);

    void refreshOnuSrvProfile(Long entityId);

    void refreshOnuPortVlanProfile(Long entityId);

    void refreshOnuIgmpProfile(Long entityId);

    void refreshOnuSrvIgmpVlanTrans(Long entityId);

    void refreshOnuCapability(Long entityId);

    List<Integer> getIgmpVlanList(Long entityId);

    List<Integer> getIgmpVlanTransIds(Long entityId);

    /**
     * 解绑定业务模板能力集
     * @param entityId
     * @param srvProfileId
     */
    void unBindCapability(OnuSrvProfile srvProfile);

    List<OnuSrvIgmpVlanTrans> loadOnuIgmpVlanTrans(Long entityId, Integer profileId, Integer portId);
    
}
