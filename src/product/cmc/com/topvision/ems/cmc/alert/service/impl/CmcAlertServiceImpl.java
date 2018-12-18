/***********************************************************************
 * $Id: CmcAlertServiceImpl.java,v1.0 2011-12-8 上午10:58:18 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.alert.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.alert.dao.CmcAlertDao;
import com.topvision.ems.cmc.alert.service.CmcAlertService;
import com.topvision.ems.cmc.facade.domain.DocsDevEvControl;
import com.topvision.ems.fault.dao.AlertTypeDao;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.framework.service.BaseService;

/**
 * 告警功能实现
 * 
 * @author loyal
 * @created @2011-12-8-上午10:58:18
 * 
 */
@Service("cmcAlertService")
public class CmcAlertServiceImpl extends BaseService implements CmcAlertService {
    @Resource(name = "cmcAlertDao")
    private CmcAlertDao cmcAlertDao;
    @Autowired
    private AlertTypeDao alertTypeDao;

    public CmcAlertDao getCmcAlertDao() {
        return cmcAlertDao;
    }

    public void setCmcAlertDao(CmcAlertDao cmcAlertDao) {
        this.cmcAlertDao = cmcAlertDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcAlertService#getCmcAlertList(java.lang.Long)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List<Alert> getCmcAlertList(Map map, Integer start, Integer limit) {
        return cmcAlertDao.getCmcAlertList(map, start, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcAlertService#getCmcHistoryAlertList(java.lang.Long)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List<HistoryAlert> getCmcHistoryAlertList(Map map, Integer start, Integer limit) {
        return cmcAlertDao.getCmcHistoryAlertList(map, start, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.getCmcAlertListNum#getCmcAlertListNum(java.lang.Long)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Integer getCmcAlertListNum(Map map) {
        return cmcAlertDao.getCmcAlertListNum(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcAlertService#getCmcHistoryAlertListNum(java.lang.Long)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Integer getCmcHistoryAlertListNum(Map map) {
        return cmcAlertDao.getCmcHistoryAlertListNum(map);
    }

    @Override
    public List<DocsDevEvControl> getdocsDevEvControlList(Long entityId) {
        return cmcAlertDao.getdocsDevEvControlList(entityId);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.service.CmcAlertService#getCmPollAlertType()
     */
    public List<AlertType> getCmPollAlertType() {
        return cmcAlertDao.selectCmPollAlertType();
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.service.CmcAlertService#getCurrentCmPollAlertList(java.util.Map, java.lang.Integer, java.lang.Integer)
     */
    public List<Alert> getCurrentCmPollAlertList(Map<String, String> map, Integer start, Integer limit) {
        return cmcAlertDao.selectCurrentCmPollAlertList(map, start, limit);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.cmc.service.CmcAlertService#getHisCmPollAlertList(java.util.Map, java.lang.Integer, java.lang.Integer)
     */
    public List<HistoryAlert> getHisCmPollAlertList(Map<String, String> map, Integer start, Integer limit) {
        return cmcAlertDao.selectHisCmPollAlertList(map, start, limit);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.service.CmcAlertService#getCurrentCmPollAlertNum(java.util.Map)
     */
    public Integer getCurrentCmPollAlertNum(Map<String, String> map) {
        return cmcAlertDao.selectCurrentCmPollAlertNum(map);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.cmc.service.CmcAlertService#getHisCmPollAlertListNum(java.util.Map)
     */
    public Integer getHisCmPollAlertListNum(Map<String, String> map) {
        return cmcAlertDao.selectHisCmPollAlertListNum(map);
    }

    /**
     * 获取CCMTS的告警类型,主要是排除其中的epon类型
     * 
     * @update by flackyang 2013-11-12
     */
    @Override
    public List<AlertType> getCmcAlertTypes() {
        List<AlertType> alertTypes = alertTypeDao.getAllAlertType();
        List<AlertType> cmcAlertTypes = new ArrayList<AlertType>();
        List<Integer> cmtsCategoryList = Arrays.asList(AlertType.ALERT_CMTS);
        List<Integer> specialEponType = Arrays.asList(AlertType.SPECIAL_CATEGORY_EPON);
        for (AlertType type : alertTypes) {
            if (cmtsCategoryList.contains(type.getCategory()) && !specialEponType.contains(type.getTypeId())) {
                if (type.getTypeId().equals(200002)) {// 移出spectrumAlertClearType
                    continue;
                }
                cmcAlertTypes.add(type);
            }
        }
        return cmcAlertTypes;
    }

}
