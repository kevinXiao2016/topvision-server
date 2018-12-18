/***********************************************************************
 * $Id: OltPortInfoService.java,v1.0 2016-4-12 上午11:54:32 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portinfo.service;

import java.util.List;

import com.topvision.ems.epon.portinfo.domain.OltPortOpticalInfo;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2016-4-12-上午11:54:32
 *
 */
public interface OltPortInfoService extends Service {

    /**
     * 查询SNI口光功率信息列表
     * @param entityId
     * @return
     */
    List<OltPortOpticalInfo> getSniOpticalInfoList(Long entityId);

    /**
     * 查询PON口光功率信息列表
     * @param entityId
     * @return
     */
    List<OltPortOpticalInfo> getPonOpticalInfoList(Long entityId);

    /**
     * 更新SNI口光功率信息
     * @param sniOpticalInfo
     */
    void updateSniOpticalInfo(OltPortOpticalInfo sniOpticalInfo);

    /**
     * 更新PON口光功率信息
     * @param ponOpticalInfo
     */
    void updatePonOpticalInfo(OltPortOpticalInfo ponOpticalInfo);

    /**
     * 刷新SNI口光功率信息
     * @param entityId
     * @param portIndex
     * @param sniId
     * @return
     */
    OltPortOpticalInfo refreshSniOpticalInfo(Long entityId, Long portIndex, Long sniId, Integer perfStats);

    /**
     * 刷新所有的SNI口光功率信息
     * @param entityId
     */
    void refreshAllSniOptical(Long entityId, String jConnectedId, String seesionId);

    /**
     * 刷新PON光功率信息
     * @param entityId
     * @param portIndex
     * @param ponId
     * @return
     */
    OltPortOpticalInfo refreshPonOpticalInfo(Long entityId, Long portIndex, Long ponId, Integer perfStats);

    /**
     * 刷新所有的PON口光功率信息
     * @param entityId
     */
    void refreshAllPonOptical(Long entityId, String jConnectedId, String seesionId);

}
