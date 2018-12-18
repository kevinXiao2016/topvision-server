/***********************************************************************
 * $Id: MOltDao.java,v1.0 2016年7月16日 下午4:53:30 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.mobile.domain.BaiduMapInfo;
import com.topvision.ems.mobile.domain.CmtsInfo;
import com.topvision.ems.mobile.domain.MobileOlt;
import com.topvision.ems.mobile.domain.MobileOnu;

/**
 * @author flack
 * @created @2016年7月16日-下午4:53:30
 *
 */
public interface MOltDao {

    /**
     * 获取OLT设备列表
     * @param paramMap
     * @return
     */
    public List<MobileOlt> queryOltList(Map<String, Object> paramMap);

    /**
     * 获取OLT设备列表数目
     * @param paramMap
     * @return
     */
    public Integer queryOltListCount(Map<String, Object> paramMap);

    /**
     * 获取OLT基本信息
     * @param entityId
     * @return
     */
    public MobileOlt queryOltBaseInfo(Long entityId);

    /**
     * 查询OLT下CMTS列表
     * @param paramMap
     * @return
     */
    public List<CmtsInfo> queryOltCmtsList(Map<String, Object> paramMap);

    /**
     * 查询OLT下CMTS数量
     * @param paramMap
     * @return
     */
    public Integer queryOltCmtsCount(Map<String, Object> paramMap);

    /**
     * 获取OLT下ONU列表
     * @param paramMap
     * @return
     */
    public List<MobileOnu> queryOltOnuList(Map<String, Object> paramMap);

    /**
     * 获取OLT下ONU数量
     * @param paramMap
     * @return
     */
    public Integer queryOltOnuCount(Map<String, Object> paramMap);


    public List<Long> getPonOnuIndex(Long entityId, Long ponIndex);

    public List<MobileOnu> queryOltOnuListWithRegion(Map<String, Object> map);

    public Integer queryOltOnuCountWithRegion(Map<String, Object> map);
    
    public Integer queryOltOnuOnlineCountWithRegion(Map<String, Object> map);

    public List<CmtsInfo> queryOltCmtsListWithRegion(Map<String, Object> map);

    public Integer queryOltCmtsCountWithRegion(Map<String, Object> map);

    public Integer queryOltCmtsOnlineCountWithRegion(Map<String, Object> map);

    public BaiduMapInfo getBaiduMapInfo(Long entityId);

    public void modifyEntityLocation(Map<String, Object> map);

    public void saveMapDataToDB(Map<String, Object> map);

}
