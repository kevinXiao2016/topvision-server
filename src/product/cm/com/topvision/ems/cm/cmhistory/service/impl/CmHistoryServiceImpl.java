/***********************************************************************
 * $Id: CmHistoryServiceImpl.java,v1.0 2015年4月9日 下午8:38:46 $ * 
 * @author: YangYi
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmhistory.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.cm.cmhistory.dao.CmHistoryDao;
import com.topvision.ems.cm.cmhistory.domain.CmHistoryShow;
import com.topvision.ems.cm.cmhistory.engine.domain.CmHistory;
import com.topvision.ems.cm.cmhistory.service.CmHistoryService;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollResult;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.message.service.MessageService;

/**
 * @author YangYi
 * @created @2015年4月9日-下午8:38:46
 * 
 */
@Service("cmHistoryService")
public class CmHistoryServiceImpl extends BaseService implements CmHistoryService {
    @Resource(name = "cmHistoryDao")
    private CmHistoryDao cmHistoryDao;

    private String[] checkStatuss = new String[] { "", "pingError", "snmpError", "remoteQueryError" };

    @Override
    public List<CmHistoryShow> getCmHistory(Map<String, Object> queryMap) {
        List<CmHistoryShow> cmHistoryList = cmHistoryDao.getCmHistory(queryMap);
        for (CmHistoryShow cm : cmHistoryList) {
            if (cm.getStatusValue() != null && CmAttribute.isCmOnline(cm.getStatusValue())) {// CM在线
                if (cm.getCheckStatus() != null && cm.getCheckStatus() >= 1 && cm.getCheckStatus() <= 3) {
                    cm.setStatusString(CmAttribute.CM_STATUS_VALUE.get(cm.getStatusValue()) + "("
                            + getStr(checkStatuss[cm.getCheckStatus()]) + ")");
                } else {
                    cm.setStatusString(CmAttribute.CM_STATUS_VALUE.get(cm.getStatusValue()) + checkStatuss[0]);
                }
            } else if (cm.getStatusValue() != null && cm.getStatusValue() >= 0 && cm.getStatusValue() <= 9) {
                cm.setStatusString(CmAttribute.CM_STATUS_VALUE.get(cm.getStatusValue()));
            } else {
                cm.setStatusString("collectError");
            }
        }
        return cmHistoryList;
    }

    private String getStr(String key, String... strings) {
        try {
            return ResourceManager.getResourceManager("com.topvision.ems.cm.cmhistory.resources").getString(key,
                    strings);
        } catch (Exception e) {
            logger.debug("", e);
            return key;
        }
    }

    // TODO Test 准备删除
    @Override
    public void insert1WData() {
        List<CmHistory> cmHistoryList = new ArrayList<CmHistory>();
        Long cmId = 1L;
        for (long i = 0; i < 10000; i++) {
            cmId++;
            CmHistory c = new CmHistory();
            c.setCmId(cmId);
            c.setCollectTime(new Timestamp(System.currentTimeMillis()));
            c.setStatusValue(6);
            c.setCheckStatus(CmPollResult.CHECKSUCCESS);
            c.setUpChannelId("1/2/3/4");
            c.setDownChannelId("1/2/3/4");
            c.setUpChannelFreq("5.0/15.0/25.0/45.0");
            c.setDownChannelFreq("5.0/15.0/25.0/45.0");
            c.setUpRecvPower("20.0/23.0/25.0/26.0");
            c.setUpSnr("20.0/23.0/25.0/26.0");
            c.setDownSnr("20.0/23.0/25.0/26.0");
            c.setUpSendPower("20.0/23.0/25.0/26.0");
            c.setDownRecvPower("20.0/23.0/25.0/26.0");
            cmHistoryList.add(c);
        }
        cmHistoryDao.batchInsertCmHistory(cmHistoryList);
    }

    // TODO Test 准备删除
    @Override
    public void insert3600WData() {
        List<CmHistory> cmHistoryList = new ArrayList<CmHistory>();
        for (int t = 0; t < 180; t++) {
            cmHistoryList.clear();
            for (long i = 1; i < 200000; i++) {
                CmHistory c = new CmHistory();
                c.setCmId(i);
                c.setCollectTime(new Timestamp(System.currentTimeMillis()));
                c.setStatusValue(6);
                c.setCheckStatus(CmPollResult.CHECKSUCCESS);
                c.setUpChannelId("1/2/3/4");
                c.setDownChannelId("1/2/3/4");
                c.setUpChannelFreq("5.0/15.0/25.0/45.0");
                c.setDownChannelFreq("5.0/15.0/25.0/45.0");
                c.setUpRecvPower("20.0/23.0/25.0/26.0");
                c.setUpSnr("20.0/23.0/25.0/26.0");
                c.setDownSnr("20.0/23.0/25.0/26.0");
                c.setUpSendPower("20.0/23.0/25.0/26.0");
                c.setDownRecvPower("20.0/23.0/25.0/26.0");
                cmHistoryList.add(c);
            }
            cmHistoryDao.batchInsertCmHistory(cmHistoryList);
        }
    }
}
