/***********************************************************************
 * $Id: PnmpMonitorCmService.java,v1.0 2017年8月8日 下午4:35:28 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.pnmp.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.pnmp.dao.PnmpMonitorCmDao;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cm.pnmp.service.PnmpMonitorCmService;
import com.topvision.ems.cm.pnmp.util.PnmpConstants;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.constants.Symbol;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.utils.CmcIndexUtils;

/**
 * @author lizongtian
 * @created @2017年8月8日-下午4:35:28
 *
 */
@Service("pnmpMonitorCmService")
public class PnmpMonitorCmServiceImpl extends BaseService implements PnmpMonitorCmService {

    private final int THRESHOLD_NUM = 1000;

    @Autowired
    private PnmpMonitorCmDao pnmpMonitorDao;

    @Override
    public List<String> selectAllMiddleMonitorCmMac() {
        return pnmpMonitorDao.selectAllMiddleMonitorCmMac();
    }

    @Override
    public List<PnmpCmData> selectAllMiddleMonitorCmList() {
        return pnmpMonitorDao.selectAllMiddleMonitorCmList();
    }

    @Override
    public List<PnmpCmData> getAllMiddleMonitorCmList() {
        return pnmpMonitorDao.selectAllMiddleMonitorCmList();
    }

    @Override
    public List<PnmpCmData> getAllHighMonitorCmList() {
        return pnmpMonitorDao.selectAllHighMonitorCmList();
    }

    @Override
    public List<PnmpCmData> getHighMonitorCmList(Map<String, Object> queryMap) {
        return pnmpMonitorDao.selectHighMonitorCmList(queryMap);
    }

    @Override
    public Integer getAllHighMonitorCmListNum() {
        return pnmpMonitorDao.selectAllHighMonitorCmListNum();
    }

    @Override
    public Integer getAllMiddleMonitorCmListNum() {
        return pnmpMonitorDao.selectAllMiddleMonitorCmListNum();
    }

    @Override
    public Integer getHighMonitorCmListNum(Map<String, Object> queryMap) {
        return pnmpMonitorDao.selectHighMonitorCmListNum(queryMap);
    }

    @Override
    public void addMiddleMonitorCm(String cmMac) {

    }

    @Override
    public void removeMiddleMonitorCm(String cmMac) {

    }

    @Override
    public Integer addHighMonitorCm(Map<String, Object> queryMap) {
        // 为空，说明没有找到该cm对应的cmts，即不在网管中，则不添加入高频采集队列
        if (pnmpMonitorDao.selectCmcAttributeByCmMac(queryMap) == null) {
            return PnmpConstants.MAC_NOT_IN_EMS;
        }
        // 不为空，说明高频采集已经存在
        if (pnmpMonitorDao.selectHighMonitorCmByPK(queryMap) != null) {
            return PnmpConstants.MAC_DUPLICATE;
        }
        // 高频采集CM数量不超过1000
        if (pnmpMonitorDao.selectAllHighMonitorCmListNum() > THRESHOLD_NUM) {
            return PnmpConstants.MAC_HIGH_FULL;
        }
        try {
            pnmpMonitorDao.insertHighMonitorCm(MacUtils.formatMac(queryMap.get("cmMac").toString()));
            return PnmpConstants.MAC_ADD_OK;
        } catch (Exception e) {
            // 其它原因
            logger.error("", e);
            return PnmpConstants.MAC_ADD_FAIL;
        }
    }

    @Override
    public void removeHighMonitorCm(String cmMac) {
        pnmpMonitorDao.deleteHighMonitorCm(cmMac);
    }

    @Override
    public void batchRemoveHighMonitorCm(List<String> cmMacs) {
        pnmpMonitorDao.batchDeleteHighMonitorCm(cmMacs);
    }

    @Override
    public Map<String, Object> getCmcAttributeByCmMac(Map<String, Object> queryMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        map = pnmpMonitorDao.selectCmcAttributeByCmMac(queryMap);
        Long cmcIndex = null;
        if (map != null && (cmcIndex = (Long) map.get(PnmpConstants.CMC_INDEX)) != null) {
            String location = CmcIndexUtils.getSlotNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getPonNo(cmcIndex).toString() + Symbol.SLASH
                    + CmcIndexUtils.getCmcId(cmcIndex).toString();
            map.put(PnmpConstants.LOCATION, location);
        }
        return map;
    }

}
