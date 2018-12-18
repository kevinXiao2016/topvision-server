/***********************************************************************
 * $Id: OltRunningStatusReportDaoImpl.java,v1.0 2013-10-29 上午11:48:58 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.oltrunningstatus.dao.mybatis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.report.domain.OltPonRunningStatus;
import com.topvision.ems.cmc.report.domain.OltRunningStatus;
import com.topvision.ems.cmc.report.oltrunningstatus.dao.OltRunningStatusReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2013-10-29-上午11:48:58
 * 
 */
@Repository("oltRunningStatusReportDao")
public class OltRunningStatusReportDaoImpl extends MyBatisDaoSupport<Entity> implements OltRunningStatusReportDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.report.domain.OltRunningStatusReport";
    }

    @Override
    public Map<String, List<OltRunningStatus>> statOltRunningStatusReport(List<TopoEntityStastic> relates,
            Map<String, Object> map) {
        Map<String, List<OltRunningStatus>> oltRunningStatusMap = new LinkedHashMap<String, List<OltRunningStatus>>();
        // 在同一事务中batch模式和simple模式之间无法转换，系统默认选择了simple模式，所以碰到需要批量更新时，只能在单独的事务中进行
        // 新获取一个模式为BATCH，自动提交为false的session
        SqlSession session = getBatchSession();
        // 针对拓扑结构中的每个设备进行查询
        try {
            for (TopoEntityStastic sta : relates) {
                Long entityId = sta.getEntityId();
                String folderName = sta.getFolderName();
                if (entityId == null) {
                    oltRunningStatusMap.put(folderName, null);
                    continue;
                }

                // map里面包含时间段'sql' olt的entityId 以及userId
                map.put("entityId", entityId.toString());
                // 获取sni的流量峰值
                OltRunningStatus result = getSqlSession().selectOne(getNameSpace("getOltRunningStatus"), map);
                if (result == null) {
                    continue;
                }
                result.setOltName(sta.getEntityName());

                // 获取pon流量峰值
                List<OltPonRunningStatus> oltPonRunningStatusList = getSqlSession().selectList(
                        getNameSpace("getOltPonRunningStatus"), map);

                for (OltPonRunningStatus ps : oltPonRunningStatusList) {
                    ps.setOltName(result.getOltName());
                    ps.setSniUsageString(result.getSniUsageString());

                    // 获取pon流量峰值之间点的在线CM数量统计
                    Map<String, Object> map1 = new LinkedHashMap<String, Object>();
                    map1.put("realtime", ps.getCollectTime());
                    map1.put("ponId", ps.getPonId());
                    map1.put("entityId", entityId);
                    map1.put("sql", map.get("sql").toString().replace("d.collectTime", "realtime"));
                    Long cmNum = getSqlSession().selectOne(getNameSpace("queryForCmNum"), map1);
                    if (cmNum != null) {
                        ps.setCmNum(cmNum);
                    } else {
                        ps.setCmNum(0l);
                    }
                    // TODO CC数量目前没有采集，用实时CC数量代替
                    Long ccNum = getSqlSession().selectOne(getNameSpace("queryforCcNum"), ps.getPonId());
                    if (ccNum != null) {
                        ps.setCcNum(ccNum);
                    } else {
                        ps.setCcNum(0l);
                    }
                }
                result.setOltPonRunningStatusList(oltPonRunningStatusList);

                if (oltRunningStatusMap.containsKey(sta.getFolderName())) {
                    List<OltRunningStatus> list = oltRunningStatusMap.get(folderName);
                    list.add(result);
                } else {
                    List<OltRunningStatus> list = new ArrayList<OltRunningStatus>();
                    list.add(result);
                    oltRunningStatusMap.put(folderName, list);
                }
            }
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return oltRunningStatusMap;
    }

}
