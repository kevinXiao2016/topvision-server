/***********************************************************************
 * $Id: OltPortInfoDao.java,v1.0 2016-4-12 上午11:30:29 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portinfo.dao;

import java.util.List;

import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OltSniOptical;
import com.topvision.ems.epon.portinfo.domain.OltPortOpticalInfo;

/**
 * @author flack
 * @created @2016-4-12-上午11:30:29
 *
 */
public interface OltPortInfoDao {

    /**
     * 查询SNI口光功率信息列表
     * @param entityId
     * @return
     */
    List<OltPortOpticalInfo> querySniOpticalInfoList(Long entityId);

    /**
     * 查询PON口光功率信息列表
     * @param entityId
     * @return
     */
    List<OltPortOpticalInfo> queryPonOpticalInfoList(Long entityId);

    /**
     * 更新SNI口光功率信息
     * @param sniOpticalInfo
     */
    void updateSniOpticalInfo(OltPortOpticalInfo sniOpticalInfo);

    /**
     * 批量更新SNI口光功率信息
     * @param sniOpticalList
     */
    void batchUpdateSniOptical(List<OltPortOpticalInfo> sniOpticalList);

    /**
     * 更新PON口光功率信息
     * @param ponOpticalInfo
     */
    void updatePonOpticalInfo(OltPortOpticalInfo ponOpticalInfo);

    /**
     * 批量更新PON端口光功率信息
     * @param ponOpticalList
     */
    void batchUpdatePonOptical(List<OltPortOpticalInfo> ponOpticalList);

    /**
     * 获取OLT设备SNI口基本数据
     * @param entityId
     * @return
     */
    List<OltSniOptical> queryOltSniList(Long entityId);

    /**
     * 获取OLT设备PON口基本数据
     * @param entityId
     * @return
     */
    List<OltPonOptical> queryOltPonList(Long entityId);
}
