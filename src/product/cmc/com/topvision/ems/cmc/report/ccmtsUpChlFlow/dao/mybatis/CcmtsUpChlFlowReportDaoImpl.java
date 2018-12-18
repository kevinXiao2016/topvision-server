/***********************************************************************
 * $Id: CcmtsUpChlFlowReportDaoImpl.java,v1.0 2014-3-24 下午3:26:41 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.ccmtsUpChlFlow.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.report.ccmtsUpChlFlow.dao.CcmtsUpChlFlowReportDao;
import com.topvision.ems.cmc.report.domain.CcmtsChlFlowDetail;
import com.topvision.ems.cmc.report.domain.CcmtsChlFlowInfo;
import com.topvision.ems.cmc.report.domain.CcmtsChlFlowStatic;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2014-3-24-下午3:26:41
 * 
 */
@Repository("ccmtsUpChlFlowReportDao")
public class CcmtsUpChlFlowReportDaoImpl extends MyBatisDaoSupport<Entity> implements CcmtsUpChlFlowReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.report.domain.CcmtsUpChlFlowReport";
    }

    @Override
    public Map<String, List<CcmtsChlFlowStatic>> statUpChlFlowStatic(List<TopoEntityStastic> relates,
            Map<String, Object> map) {
        Map<String, List<CcmtsChlFlowStatic>> folderCcmtsUpChlFlow = new LinkedHashMap<String, List<CcmtsChlFlowStatic>>();
        List<CcmtsChlFlowStatic> ccmtsChlFlowReportList = getSqlSession().selectList(
                getNameSpace("getUpChlFlowReportStat"), map);
        for (TopoEntityStastic sta : relates) {
            Long entityId = sta.getEntityId();
            String folderName = sta.getFolderName();
            if (entityId == null) {
                folderCcmtsUpChlFlow.put(folderName, null);
                continue;
            }
            // 获取一个OLT下的端口信息
            CcmtsChlFlowStatic result = new CcmtsChlFlowStatic();
            for (int i = 0; i < ccmtsChlFlowReportList.size(); i++) {
                if (ccmtsChlFlowReportList.get(i).getEntityId().equals(entityId)) {
                    result = ccmtsChlFlowReportList.get(i);
                }
            }
            if (result.getChlNum() == null) {
                continue;
            }
            result.setEntityId(entityId);
            result.setEntityIp(sta.getEntityIp());
            result.setEntityName(sta.getEntityName());
            result.setTypeId(sta.getTypeId());
            result.setFolderId(sta.getFolderId());

            if (folderCcmtsUpChlFlow.containsKey(sta.getFolderName())) {
                List<CcmtsChlFlowStatic> list = folderCcmtsUpChlFlow.get(folderName);
                list.add(result);
            } else {
                List<CcmtsChlFlowStatic> list = new ArrayList<CcmtsChlFlowStatic>();
                list.add(result);
                folderCcmtsUpChlFlow.put(folderName, list);
            }
        }
        return folderCcmtsUpChlFlow;
    }

    @Override
    public Map<String, List<CcmtsChlFlowDetail>> statUpChlFlowDetail(List<TopoEntityStastic> relates,
            Map<String, Object> map) {
        Map<String, List<CcmtsChlFlowDetail>> folderCcmtsUpChlFlowDtail = new LinkedHashMap<String, List<CcmtsChlFlowDetail>>();
        // 先获取有哪些CCMTS,顺便获取其上联OLT的基本信息
        List<CcmtsChlFlowDetail> ccmtsChlFlowDetailList = getSqlSession().selectList(
                getNameSpace("getUpChlFlowDetailStat"), map);
        for (TopoEntityStastic sta : relates) {
            // 每个sta是一个CCMTS
            Long entityId = sta.getEntityId();
            String folderName = sta.getFolderName();
            if (entityId == null) {
                folderCcmtsUpChlFlowDtail.put(folderName, null);
                continue;
            }
            CcmtsChlFlowDetail result = new CcmtsChlFlowDetail();
            for (int i = 0; i < ccmtsChlFlowDetailList.size(); i++) {
                if (ccmtsChlFlowDetailList.get(i).getEntityId().equals(entityId)) {
                    result = ccmtsChlFlowDetailList.get(i);

                    // 获取CCMTS流量统计
                    Map<String, Object> chlFlowMap = new HashMap<String, Object>();
                    chlFlowMap.put("cmcId", entityId);
                    chlFlowMap.put("timeSql", map.get("flowTimeSql").toString());
                    chlFlowMap.put("rangeSql", map.get("rangeSql").toString());
                    List<CcmtsChlFlowInfo> ccmtsChlFlowInfos = getSqlSession().selectList(
                            getNameSpace("getCcmtsChlFlowInfos"), chlFlowMap);

                    // 如果无性能采集，继续
                    if (ccmtsChlFlowInfos == null || ccmtsChlFlowInfos.size() == 0) {
                        continue;
                    }
                    result.setCcmtsChlFlowInfos(ccmtsChlFlowInfos);

                    // 此list用于获取用户数统计
                    List<Long> channelIndexList = new ArrayList<Long>();

                    // 获取利用率平均值
                    Double usageAvg = 0d;
                    int j = 0;
                    for (; j < ccmtsChlFlowInfos.size(); j++) {
                        usageAvg += ccmtsChlFlowInfos.get(j).getMaxFlowUsage();
                        channelIndexList.add(ccmtsChlFlowInfos.get(j).getChannelIndex());
                    }
                    if (j != 0) {
                        usageAvg = usageAvg / j;
                    }
                    result.setUsageAvg(usageAvg);

                    // 获取用户数详情
                    Map<String, Object> userNumMap = new HashMap<String, Object>();
                    userNumMap.put("entityId", ccmtsChlFlowDetailList.get(i).getCmcEntityId());
                    userNumMap.put("channelIndexList", channelIndexList);
                    userNumMap.put("timeSql", map.get("userNumTimeSql").toString());
                    CcmtsChlFlowDetail userNumDetail = getSqlSession().selectOne(getNameSpace("getUserNumDetail"),
                            userNumMap);
                    // 如果无采集数据，继续
                    if (userNumDetail == null) {
                        result.setMaxRegUserNum(0);
                        result.setMaxUserNum(0);
                        continue;
                    }
                    result.setMaxRegUserNum(userNumDetail.getMaxRegUserNum());
                    result.setMaxUserNum(userNumDetail.getMaxUserNum());
                }
            }
            result.setEntityId(entityId);
            result.setEntityName(sta.getEntityName());
            result.setTypeId(sta.getTypeId());
            result.setFolderId(sta.getFolderId());

            if (folderCcmtsUpChlFlowDtail.containsKey(sta.getFolderName())) {
                List<CcmtsChlFlowDetail> list = folderCcmtsUpChlFlowDtail.get(folderName);
                list.add(result);
            } else {
                List<CcmtsChlFlowDetail> list = new ArrayList<CcmtsChlFlowDetail>();
                list.add(result);
                folderCcmtsUpChlFlowDtail.put(folderName, list);
            }
        }
        return folderCcmtsUpChlFlowDtail;
    }
}
