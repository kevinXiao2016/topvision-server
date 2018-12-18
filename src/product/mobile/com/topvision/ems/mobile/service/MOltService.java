/***********************************************************************
 * $Id: MOltService.java,v1.0 2016年7月16日 下午4:13:17 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.ems.mobile.domain.BaiduMapInfo;
import com.topvision.ems.mobile.domain.CmtsInfo;
import com.topvision.ems.mobile.domain.MEntityType;
import com.topvision.ems.mobile.domain.MobileOlt;
import com.topvision.ems.mobile.domain.MobileOnu;

/**
 * @author flack
 * @created @2016年7月16日-下午4:13:17
 *
 */
public interface MOltService {

    /**
     * 获取OLT设备列表
     * @param paramMap
     * @return
     */
    public List<MobileOlt> getOltList(Map<String, Object> paramMap);

    /**
     * 获取OLT设备列表数目
     * @param paramMap
     * @return
     */
    public Integer getOltListCount(Map<String, Object> paramMap);

    /**
     * 获取OLT基本信息
     * @param entityId
     * @return
     */
    public MobileOlt getOltBaseInfo(Long entityId);

    /**
     * 查询OLT下CMTS列表
     * @param paramMap
     * @return
     */
    public List<CmtsInfo> getOltCmtsList(Map<String, Object> paramMap);

    /**
     * 查询OLT下CMTS数量
     * @param paramMap
     * @return
     */
    public Integer getOltCmtsCount(Map<String, Object> paramMap);

    /**
     * 获取OLT下ONU列表
     * @param paramMap
     * @return
     */
    public List<MobileOnu> getOltOnuList(Map<String, Object> paramMap);

    /**
     * 获取OLT下ONU数量
     * @param paramMap
     * @return
     */
    public Integer getOltOnuCount(Map<String, Object> paramMap);

    /**
     * 获取认证失败列表
     * 
     * @param queryMap
     * @return
     */
    public List<OnuAuthFail> getOnuAuthFailList(Map<String, Object> queryMap);

    /**
     * 获取认证失败列表
     * 
     * @param entityId
     *            onuIndex
     * @return
     */
    public OnuAuthFail getOnuAuthFailObject(Long entityId, Long onuIndex);

    /**
     * 获取认证失败数量
     * 
     * @param queryMap
     * @return
     */
    public Long getOnuAuthFailCount(Map<String, Object> queryMap);
    
    /**
     * 获取设备类型
     * 
     * @param entityType
     * @return
     */
    public List<MEntityType> getEntityTypeList(Long subType);

    /**
     * 获取指定PON口已经使用的LLID
     * 
     * @param entityId
     * @param ponIndex
     * @return
     */
    public List<Long> getPonOnuIndex(Long entityId, Long ponIndex);
    //以下为查询列表时带上地域信息
    public List<MobileOnu> getOltOnuListWithRegion(Map<String, Object> map);

    public Integer getOltOnuCountWithRegion(Map<String, Object> map);

    public List<CmtsInfo> getOltCmtsListWithRegion(Map<String, Object> map);

    public Integer getOltCmtsCountWithRegion(Map<String, Object> map);

    public BaiduMapInfo getBaiduMapInfo(Long entityId);
    public Integer getOltCmtsOnlineCountWithRegion(Map<String, Object> map);

    public void modifyEntityLocation(Map<String, Object> map);

    public void saveMapDataToDB(Map<String, Object> map);


    public Integer getOltOnuOnlineCount(Map<String, Object> map);


}
