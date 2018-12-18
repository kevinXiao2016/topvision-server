/***********************************************************************
 * $Id: CmtsAlertImpl.java,v1.0 2013-10-22 下午5:07:40 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmts.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmts.dao.CmtsAlertDao;
import com.topvision.ems.cmts.service.CmtsAlertService;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.framework.service.BaseService;

/**
 * @author loyal
 * @created @2013-10-22-下午5:07:40
 * 
 */
@Service("cmtsAlertService")
public class CmtsAlertServiceImpl extends BaseService implements CmtsAlertService {
    @Resource(name = "cmtsAlertDao")
    private CmtsAlertDao cmtsAlertDao;

    /**
     * 获取CMTS的类型,主要是排除olt和ccmts的类型
     * @update by flackyang 2013-11-12
     */
    @Override
    public List<AlertType> getCmtsAlertType() {
        List<AlertType> alertTypes = cmtsAlertDao.selectCmtsAlertType();
        List<AlertType> cmtsAlertTypes = new ArrayList<AlertType>();
        List<Integer> cmtsCategoryList = Arrays.asList(AlertType.ALERT_CMTS);
        List<Integer> specialEponType = Arrays.asList(AlertType.SPECIAL_CATEGORY_EPON);
        for (AlertType type : alertTypes) {
            if (cmtsCategoryList.contains(type.getCategory()) && !specialEponType.contains(type.getTypeId())) {
                if (type.getTypeId().equals(200002)) {// 移出spectrumAlertClearType
                    continue;
                }
                cmtsAlertTypes.add(type);
            }
        }
        return cmtsAlertTypes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmts.service.CmtsAlertService#getCmtsAlertList(java.util.Map,
     * java.lang.Integer, java.lang.Integer)
     */
    public List<Alert> getCmtsAlertList(Map<String, Object> map, Integer start, Integer limit) {
        return cmtsAlertDao.selectCmtsAlertList(map, start, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmts.service.CmtsAlertService#getCmtsAlertListNum(java.util.Map)
     */
    public Integer getCmtsAlertListNum(Map<String, Object> map) {
        return cmtsAlertDao.selectCmtsAlertListNum(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmts.service.CmtsAlertService#getCmtsHistoryAlertList(java.util.Map,
     * java.lang.Integer, java.lang.Integer)
     */
    public List<HistoryAlert> getCmtsHistoryAlertList(Map<String, Object> map, Integer start, Integer limit) {
        return cmtsAlertDao.selectCmtsHistoryAlertList(map, start, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmts.service.CmtsAlertService#getCmtsHistoryAlertListNum(java.util.Map)
     */
    public Integer getCmtsHistoryAlertListNum(Map<String, Object> map) {
        return cmtsAlertDao.selectCmtsHistoryAlertListNum(map);
    }

    public CmtsAlertDao getCmtsAlertDao() {
        return cmtsAlertDao;
    }

    public void setCmtsAlertDao(CmtsAlertDao cmtsAlertDao) {
        this.cmtsAlertDao = cmtsAlertDao;
    }

}
