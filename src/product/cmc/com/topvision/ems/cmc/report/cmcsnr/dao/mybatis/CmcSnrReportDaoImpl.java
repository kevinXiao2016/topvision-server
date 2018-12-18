/***********************************************************************
 * $Id: CmcSnrReportDaoImpl.java,v1.0 2013-10-29 下午4:38:44 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmcsnr.dao.mybatis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.report.cmcsnr.dao.CmcSnrReportDao;
import com.topvision.ems.cmc.report.domain.CmcSnrReportDetail;
import com.topvision.ems.cmc.report.domain.CmcSnrReportStatistics;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2013-10-29-下午4:38:44
 * 
 */
@Repository("cmcSnrReportDao")
public class CmcSnrReportDaoImpl extends MyBatisDaoSupport<Entity> implements CmcSnrReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.report.domain.CmcSnrReport";
    }

    @Override
    public Map<String, List<CmcSnrReportStatistics>> statCmcSnrReport(List<TopoEntityStastic> relates,
            Map<String, Object> map) {
        Map<String, List<CmcSnrReportStatistics>> folderCmcSnr = new LinkedHashMap<String, List<CmcSnrReportStatistics>>();
        List<CmcSnrReportStatistics> snrReportList = getSqlSession().selectList(getNameSpace("getSnrReportStat"), map);
        for (TopoEntityStastic sta : relates) {
            Long entityId = sta.getEntityId();
            String folderName = sta.getFolderName();
            if (entityId == null) {
                folderCmcSnr.put(folderName, null);
                continue;
            }
            // 获取一个OLT下的端口信息
            CmcSnrReportStatistics result = new CmcSnrReportStatistics();
            for (int i = 0; i < snrReportList.size(); i++) {
                if (snrReportList.get(i).getEntityId().equals(entityId)) {
                    result = snrReportList.get(i);
                }
            }
            result.setEntityId(entityId);
            result.setEntityIp(sta.getEntityIp());
            result.setEntityName(sta.getEntityName());
            result.setTypeId(sta.getTypeId());
            result.setFolderId(sta.getFolderId());

            if (folderCmcSnr.containsKey(sta.getFolderName())) {
                List<CmcSnrReportStatistics> list = folderCmcSnr.get(folderName);
                list.add(result);
            } else {
                List<CmcSnrReportStatistics> list = new ArrayList<CmcSnrReportStatistics>();
                list.add(result);
                folderCmcSnr.put(folderName, list);
            }
        }
        return folderCmcSnr;
    }

    @Override
    public List<CmcSnrReportDetail> getSnrReportDetail(Map<String, Object> map) {
        List<CmcSnrReportDetail> list = new ArrayList<CmcSnrReportDetail>();
        if (map.get("entityId") != null) {
            list = getSqlSession().selectList(getNameSpace("getSnrReportDetailForEntity"), map);
        } else if (map.get("folderId") != null) {
            map.put("folderEntity", "t_entity_" + map.get("folderId").toString());
            list = getSqlSession().selectList(getNameSpace("getSnrReportDetailForFolder"), map);
        } else {
            list = getSqlSession().selectList(getNameSpace("getSnrReportDetailForAll"), map);
        }
        return list;
    }

    @Override
    public void migrateSnrHourly() {
        getSqlSession().update(getNameSpace("migrateSnrHourly"));
    }

    @Override
    public void summarySnrDaily() {
        getSqlSession().update(getNameSpace("summarySnrDaily"));
    }
}
