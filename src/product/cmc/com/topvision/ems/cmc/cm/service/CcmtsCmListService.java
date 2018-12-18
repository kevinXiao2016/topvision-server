/***********************************************************************
 * $Id: CcmtsCmListService.java,v1.0 2013-10-30 上午9:44:19 $
 * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.service;

import java.util.List;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.framework.service.Service;
import com.topvision.platform.domain.UserContext;

/**
 * @author YangYi
 * @created @2013-10-30-上午9:44:19
 * 
 */
public interface CcmtsCmListService extends Service {
    /**
     * 刷新CC页面中关联的CM列表tab
     * 
     * @param cmcId
     *            Long
     * @param cmcIndex
     *            Long
     * @return List<CmAttribute>
     */
    List<CmAttribute> refreshContactedCmList(Long cmcId, Long cmcIndex);

    /**
     * 清除CC下所有下线的CM_在设备上操作
     * 
     * @param cmcId
     */
    void clearAllOfflineCmsOnCC(Long cmcId);

    /**
     * 删除数据库中cmc下已下线的cm
     * 
     * @param cmcId
     *            Long
     */
    void deleteCmcOfflineCm(long cmcId);

    /**
     * 根据Mac重启Cm
     * 
     * @param cmcId
     * @param mac
     */
    void restartCmByMac(Long cmcId, String mac);

    /**
     * 重启CCMTS下所有CM
     * 
     * @param cmcId
     */
    void restartAllCm(Long cmcId);

    /**
     * 根据CPE设备ip定位到所在CM
     * 
     * @param cpeIp
     *            String
     * @return List<CmAttribute>
     */
    List<CmAttribute> showCmByCpeIp(String cpeIp);

    List<CmAttribute> getCmListByCmcId(Long cmcId);

}
