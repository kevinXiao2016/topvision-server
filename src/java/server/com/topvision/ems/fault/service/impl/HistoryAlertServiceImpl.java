package com.topvision.ems.fault.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.fault.dao.HistoryAlertDao;
import com.topvision.ems.fault.domain.AlertTypeEx;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.ems.fault.service.HistoryAlertService;
import com.topvision.framework.domain.Page;
import com.topvision.framework.domain.PageData;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.util.CurrentRequest;

@Service("historyAlertService")
public class HistoryAlertServiceImpl extends BaseService implements HistoryAlertService {
    @Autowired
    private HistoryAlertDao historyAlertDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.HistoryAlertService#deleteHistoryAlert (java.util.List)
     */
    @Override
    public void deleteHistoryAlert(List<Long> alertIds) {
        historyAlertDao.deleteByPrimaryKey(alertIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.HistoryAlertService#emptyHistoryAlert()
     */
    @Override
    public void emptyHistoryAlert() {
        historyAlertDao.empty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.HistoryAlertService#getEntityHistoryAlert
     * (java.lang.Long, com.topvision.framework.domain.Page, java.util.Map)
     */
    @Override
    public PageData<HistoryAlert> getEntityHistoryAlert(Long entityId, Page p, Map<String, String> map) {
        Map<String, String> m = map;
        if (m == null) {
            m = new HashMap<String, String>();
        }
        m.put("entityId", entityId.toString());
        return this.queryHistoryAlert(p, map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.HistoryAlertService#getHistoryAlert(java .lang.Long)
     */
    @Override
    public HistoryAlert getHistoryAlert(Long alertId) {
        return historyAlertDao.getHistoryAlertByAlertId(alertId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertService#handleHistoryAlert(com.
     * zetaframework.dao.event.MyResultHandler, java.util.Map)
     */
    @Override
    public void handleHistoryAlert(MyResultHandler handler, Map<String, String> map) {
        // map.put("type", type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.HistoryAlertService#queryHistoryAlert
     * (com.topvision.framework .domain.Page, java.util.Map)
     */
    @Override
    public PageData<HistoryAlert> queryHistoryAlert(Page p, Map<String, String> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return historyAlertDao.selectByMap(map, p);
    }

    public void setHistoryAlertDao(HistoryAlertDao historyAlertDao) {
        this.historyAlertDao = historyAlertDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.fault.service.AlertService#statHistoryAlertByCategory()
     */
    @Override
    public List<AlertTypeEx> statHistoryAlertByCategory() {
        return historyAlertDao.statHistoryAlertByCategory();
    }

}
