package com.topvision.ems.cmc.topology.service;

import com.topvision.framework.service.Service;

public interface CmcRefreshService extends Service {

    /**
     * Refresh Single CC8800A
     * 
     * @param entityId
     * @param cmcIndex
     * @param cmcId
     */
    void refreshCC8800A(Long entityId, Long cmcIndex, Long cmcId);

    /**
     * Auto Refresh Single CC8800A
     * 
     * @param entityId
     * @param cmcIndex
     * @param cmcId
     */
    void autoRefreshCC8800A(Long entityId, Long cmcIndex, Long cmcId);

}
