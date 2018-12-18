/***********************************************************************
 * $Id: IpqamTopoService.java,v1.0 2016年5月6日 下午5:07:22 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.service;

/**
 * @author loyal
 * @created @2016年5月6日-下午5:07:22
 * 
 */
public interface IpqamRefreshService {
    /**
     * 刷新节目流信息
     * 
     * @param cmcId
     */
    void refreshProgramInfo(Long cmcId);

    /**
     * 刷新EQAM信息
     * 
     * @param cmcId
     */
    void refreshEqamInfo(Long cmcId);

    /**
     * 为了兼容之前的通过http方式来获取信息的功能，在独立E型尚不支持snmp的方式的时候，临时应用
     * 
     * @param cmcId
     */
    void refreshCmcIpqamByHttp(Long cmcId);

    /**
     * 刷新OLT下节目流信息
     * 
     * @param entityId
     */
    void refreshOltProgramInfo(Long entityId);
}
