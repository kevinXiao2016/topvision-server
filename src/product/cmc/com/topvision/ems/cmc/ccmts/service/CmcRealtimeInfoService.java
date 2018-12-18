/***********************************************************************
 * $Id: CmcRealtimeService.java,v1.0 2014年5月11日 上午10:08:43 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ccmts.service;

import java.util.Map;

import com.topvision.framework.service.Service;

/**
 * @author loyal
 * @created @2014年5月11日-上午10:08:43
 *
 */
public interface CmcRealtimeInfoService extends Service{
    /**
     * 获取cmc实时信息
     * @param cmcId
     * @return
     */
    public Map<String,Object> getCmcRealTimeData(Long cmcId);

    public boolean openRemoteQuery(Long cmcId);

    public String sendCommonConfig(Long cmcId);
    
    String[] getCmcSnr(Long cmcId, String[] channelIndexs);

}
