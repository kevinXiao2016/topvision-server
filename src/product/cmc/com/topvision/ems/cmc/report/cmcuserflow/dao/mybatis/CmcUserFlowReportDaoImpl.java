/***********************************************************************
 * $Id: CmcUserFlowReportDaoImpl.java,v1.0 2013-10-29 下午5:07:41 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.cmcuserflow.dao.mybatis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.report.cmcuserflow.dao.CmcUserFlowReportDao;
import com.topvision.ems.cmc.report.domain.CcmtsChannelSnrAvg;
import com.topvision.ems.cmc.report.domain.CmcUserFlowPortValue;
import com.topvision.ems.cmc.report.domain.CmcUserFlowReportDetail;
import com.topvision.ems.cmc.report.domain.CmcUserFlowReportStatistics;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2013-10-29-下午5:07:41
 * 
 */
@Repository("cmcUserFlowReportDao")
public class CmcUserFlowReportDaoImpl extends MyBatisDaoSupport<Entity> implements CmcUserFlowReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.report.domain.CmcUserFlowReport";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> statCmcUserFlowReport(List<TopoEntityStastic> relates, Map<String, Object> map) {
        Map<String, Object> cmcUserFlows = new LinkedHashMap<String, Object>();
        List<CmcUserFlowReportStatistics> queryResults = getSqlSession().selectList(
                getNameSpace("statCmcNumAndUserNumReport"), map);
        /* 存放界面上需要显示的列 */
        List<String> portIndexColumnList = new ArrayList<String>();

        for (TopoEntityStastic sta : relates) {
            Long entityId = sta.getEntityId();
            String folderName = sta.getFolderName();
            if (entityId == null) {
                cmcUserFlows.put(folderName, null);
                continue;
            }
            if (queryResults.size() > 0) {
                for (CmcUserFlowReportStatistics stats : queryResults) {
                    if (stats.getEntityId().equals(sta.getEntityId())) {
                        /** 存入portName */
                        if (stats.getPortIndex() != null && !portIndexColumnList.contains(stats.getPortName())) {
                            portIndexColumnList.add(stats.getPortName());
                        }
                        stats.setEntityId(entityId);
                        stats.setEntityIp(sta.getEntityIp());
                        stats.setEntityName(sta.getEntityName());
                        stats.setTypeId(sta.getTypeId());
                        /* 组装portValue * */
                        CmcUserFlowPortValue portValue = new CmcUserFlowPortValue();
                        portValue.setPortIndex(stats.getPortIndex());
                        portValue.setValue(stats.getUplinkPortRate());

                        if (cmcUserFlows.containsKey(sta.getFolderName())) {
                            /* 地域下包含的设备列表 * */
                            List<CmcUserFlowReportStatistics> list = (List<CmcUserFlowReportStatistics>) cmcUserFlows
                                    .get(folderName);
                            /*
                             * 在list中寻找该entity是否已经存在，如果存在，则将其entity中插入一个 portValue，否则插入一个entity *
                             */
                            boolean entityFound = false;
                            for (CmcUserFlowReportStatistics st : list) {
                                if (st.getEntityId().equals(entityId)) {
                                    portValue.setPortName(portValue.getPortName());
                                    st.getPortValueList().add(portValue);
                                    entityFound = true;
                                    break;
                                }
                            }
                            if (!entityFound) {
                                stats.getPortValueList().add(portValue);
                                list.add(stats);
                            }
                        } else {
                            List<CmcUserFlowReportStatistics> list = new ArrayList<CmcUserFlowReportStatistics>();
                            stats.getPortValueList().add(portValue);
                            list.add(stats);
                            cmcUserFlows.put(folderName, list);
                        }
                    }
                }
            } else {
                CmcUserFlowReportStatistics stats = new CmcUserFlowReportStatistics();
                stats.setEntityId(entityId);
                stats.setEntityName(sta.getEntityName());
                if (cmcUserFlows.containsKey(sta.getFolderName())) {
                    List<CmcUserFlowReportStatistics> list = (List<CmcUserFlowReportStatistics>) cmcUserFlows
                            .get(folderName);
                    list.add(stats);
                } else {
                    List<CmcUserFlowReportStatistics> list = new ArrayList<CmcUserFlowReportStatistics>();
                    list.add(stats);
                    cmcUserFlows.put(folderName, list);
                }
            }
        }
        cmcUserFlows.put("columns", portIndexColumnList);
        return cmcUserFlows;
    }

    @Override
    public Map<String, List<CmcUserFlowReportDetail>> selectUserFlowDetail(Map<String, Object> map) {
        Map<String, List<CmcUserFlowReportDetail>> detailMap = new ConcurrentSkipListMap<String, List<CmcUserFlowReportDetail>>();
        //此sql查出CCMTS的pon口信息及CM分类用户数和CPE分类用户数等信息，分类信息属于广州新需求
        List<CmcUserFlowReportDetail> details = getSqlSession()
                .selectList(getNameSpace("statCcmtsUserFlowDetail"), map);

        List<CcmtsChannelSnrAvg> avgs = getSqlSession().selectList(getNameSpace("selectCcmtsChannelSnrAvg"), map);
        for (CcmtsChannelSnrAvg avg : avgs) {
            Long cmcId = avg.getCmcId();
            for (CmcUserFlowReportDetail detail : details) {
                try {
                    if (cmcId.equals(detail.getCmcId())) {
                        detail.addChannelAvg(avg.getIfNo() - 1, avg);
                        break;
                    }
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("there may be dirty data in the cmcAttributes detail:[" + detail + "] ; cmcId:["
                                + cmcId + "]", e);
                    }
                }
            }
        }

        // /不能将这个循环放在上面的循环中的原因：当某个CC没有一个信道的时候，那么这个CC将不会被加入MAP
        for (CmcUserFlowReportDetail detail : details) {

            //将分到每个CCMTS的流量加入对象
            /*map.put("cmcId", detail.getCmcId());
            Double ccmtsMaxSend = getSqlSession().selectOne(getNameSpace("getCcmtsFlowforDetail"), map);
            detail.setCcmtsMaxSend(ccmtsMaxSend);
            if (ccmtsMaxSend != null) {
                detail.setCcmtsMaxSend(ccmtsMaxSend);
            }*/

            String ponName = detail.getPonDisplayName();
            List<CmcUserFlowReportDetail> list;
            if (detailMap.containsKey(ponName)) {
                list = detailMap.get(ponName);
            } else {
                list = new ArrayList<CmcUserFlowReportDetail>();
                detailMap.put(ponName, list);
            }
            if (!list.contains(detail)) {
                list.add(detail);
            }
        }
        return detailMap;
    }

    @Override
    public void migrateChannelCmHourly() {
        getSqlSession().update(getNameSpace("migrateChannelCmHourly"));
    }

    @Override
    public void summaryChannelCmDaily() {
        getSqlSession().update(getNameSpace("summaryChannelCmDaily"));
    }
}
