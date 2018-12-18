/***********************************************************************
 * $Id: MOltServiceImpl.java,v1.0 2016年7月16日 下午4:19:00 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.onuauth.domain.OnuAuthFail;
import com.topvision.ems.epon.onuauth.service.OnuAuthFailListService;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.mobile.dao.MOltDao;
import com.topvision.ems.mobile.domain.BaiduMapInfo;
import com.topvision.ems.mobile.domain.CmtsInfo;
import com.topvision.ems.mobile.domain.MEntityType;
import com.topvision.ems.mobile.domain.MobileOlt;
import com.topvision.ems.mobile.domain.MobileOnu;
import com.topvision.ems.mobile.service.MOltService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author flack
 * @created @2016年7月16日-下午4:19:00
 *
 */
@Service("mOltService")
public class MOltServiceImpl extends BaseService implements MOltService {

    @Autowired
    private MOltDao mOltDao;
    @Autowired
    private OnuAuthFailListService onuAuthFailListService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public List<MobileOlt> getOltList(Map<String, Object> paramMap) {
        return mOltDao.queryOltList(paramMap);
    }

    @Override
    public Integer getOltListCount(Map<String, Object> paramMap) {
        return mOltDao.queryOltListCount(paramMap);
    }

    @Override
    public MobileOlt getOltBaseInfo(Long entityId) {
        MobileOlt mobileOlt = mOltDao.queryOltBaseInfo(entityId);
        // 处理在线时长
        if (mobileOlt != null) {
            if (mobileOlt.getSysUpTime() != null) {
                Long sysUpTime = mobileOlt.getSysUpTime() / 100
                        + (System.currentTimeMillis() - mobileOlt.getSnapTime().getTime()) / 1000;
                mobileOlt.setSysUpTime(sysUpTime);
            } else {
                // 防止未采集到在线时间
                mobileOlt.setSysUpTime(-1L);
            }
        }
        return mobileOlt;
    }

    @Override
    public List<CmtsInfo> getOltCmtsList(Map<String, Object> paramMap) {
        return mOltDao.queryOltCmtsList(paramMap);
    }

    @Override
    public Integer getOltCmtsCount(Map<String, Object> paramMap) {
        //return mOltDao.queryOltListCount(paramMap);
        return mOltDao.queryOltCmtsCount(paramMap);
    }

    @Override
    public List<MobileOnu> getOltOnuList(Map<String, Object> paramMap) {
        return mOltDao.queryOltOnuList(paramMap);
    }

    @Override
    public Integer getOltOnuCount(Map<String, Object> paramMap) {
        return mOltDao.queryOltOnuCount(paramMap);
    }

    @Override
    public List<OnuAuthFail> getOnuAuthFailList(Map<String, Object> queryMap) {
        return onuAuthFailListService.getOnuAuthFailList(queryMap);
    }

    @Override
    public OnuAuthFail getOnuAuthFailObject(Long entityId, Long onuIndex) {
        return onuAuthFailListService.getOnuAuthFailObject(entityId, onuIndex);
    }

    @Override
    public Long getOnuAuthFailCount(Map<String, Object> queryMap) {
        return onuAuthFailListService.getOnuAuthFailCount(queryMap);
    }

    @Override
    public List<MEntityType> getEntityTypeList(Long subType) {
        List<MEntityType> mEntityTypes = new ArrayList<MEntityType>();
        List<EntityType> entityTypes = entityTypeService.loadSubType(subType);
        for (EntityType entityType : entityTypes) {
            MEntityType mType = new MEntityType();
            if (EponConstants.UNKNOWN_ONU_TYPE == entityType.getTypeId()) {
                mType.setDisplayName("NONE");
            } else {
                mType.setDisplayName(entityType.getDisplayName());
            }
            mType.setTypeId(entityType.getTypeId());
            if (!(EponConstants.OTHER_ONU_TYPE == entityType.getTypeId())) {
                mEntityTypes.add(mType);
            }
        }
        Collections.sort(mEntityTypes);
        return mEntityTypes;
    }

    @Override
    public List<Long> getPonOnuIndex(Long entityId, Long ponIndex) {
        List<Long> onuIndexs = mOltDao.getPonOnuIndex(entityId, ponIndex);
        List<Long> onuIds = new ArrayList<Long>();
        if (onuIndexs != null) {
            for (Long onuIndex : onuIndexs) {
                onuIds.add(EponIndex.getOnuNo(onuIndex));
            }
        }
        return onuIds;
    }

    @Override
    public List<MobileOnu> getOltOnuListWithRegion(Map<String, Object> map) {
        return mOltDao.queryOltOnuListWithRegion(map);
    }

    @Override
    public Integer getOltOnuCountWithRegion(Map<String, Object> map) {
        return mOltDao.queryOltOnuCountWithRegion(map);
    }

    @Override
    public Integer getOltOnuOnlineCount(Map<String, Object> map) {
        return mOltDao.queryOltOnuOnlineCountWithRegion(map);
    }
    
    @Override
    public List<CmtsInfo> getOltCmtsListWithRegion(Map<String, Object> map) {
        return mOltDao.queryOltCmtsListWithRegion(map);
    }

    @Override
    public Integer getOltCmtsCountWithRegion(Map<String, Object> map) {
        return mOltDao.queryOltCmtsCountWithRegion(map);
    }

    @Override
    public BaiduMapInfo getBaiduMapInfo(Long entityId) {
        return mOltDao.getBaiduMapInfo(entityId);
    }

    @Override
    public void modifyEntityLocation(Map<String, Object> map) {
        mOltDao.modifyEntityLocation(map);
    }

    @Override
    public void saveMapDataToDB(Map<String, Object> map) {
        mOltDao.saveMapDataToDB(map);
    }

    @Override
    public Integer getOltCmtsOnlineCountWithRegion(Map<String, Object> map) {
        return mOltDao.queryOltCmtsOnlineCountWithRegion(map);
    }

}
