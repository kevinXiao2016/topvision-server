/***********************************************************************
 * $Id: OltPonPortFlowReportDaoImpl.java,v1.0 2013-10-28 上午10:49:05 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.report.oltponportflow.dao.mybatis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.report.domain.OltPonPortFlowDetail;
import com.topvision.ems.epon.report.domain.OltPonPortFlowStastic;
import com.topvision.ems.epon.report.oltponportflow.dao.OltPonPortFlowReportDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.performance.dao.DevicePerfTargetDao;
import com.topvision.ems.report.domain.TopoEntityStastic;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2013-10-28-上午10:49:05
 * 
 */
@Repository("oltPonPortFlowReportDao")
public class OltPonPortFlowReportDaoImpl extends MyBatisDaoSupport<Entity> implements OltPonPortFlowReportDao {
    @Autowired
    private DevicePerfTargetDao devicePerfTargetDao;

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.report.domain.OltPonPortFlowReport";
    }

    @Override
    public Map<String, List<OltPonPortFlowStastic>> statPonFlowReport(List<TopoEntityStastic> relates,
            Map<String, Object> map) {
        Map<String, List<OltPonPortFlowStastic>> folderSniFlows = new LinkedHashMap<String, List<OltPonPortFlowStastic>>();
        // 在同一事务中batch模式和simple模式之间无法转换，系统默认选择了simple模式，所以碰到需要批量更新时，只能在单独的事务中进行
        // 新获取一个模式为BATCH，自动提交为false的session
        SqlSession session = getBatchSession();
        // 针对拓扑结构中的每个设备进行查询
        try {
            for (TopoEntityStastic sta : relates) {
                Long entityId = sta.getEntityId();
                String folderName = sta.getFolderName();
                if (entityId == null) {
                    folderSniFlows.put(folderName, null);
                    continue;
                }
                // 由于entityId这个key是同样的，所以每次查询的时候，entityId都能是当前这个查询所需要的entityId
                map.put("entityId", entityId.toString());
                map.put("collectInterval", devicePerfTargetDao.queryColIntervalByIdAndName(entityId, "olt_ponFlow"));
                OltPonPortFlowStastic result = session.selectOne(getNameSpace("statEntityPonFlow"), map);
                result.setEntityName(sta.getEntityName());
                result.setTypeId(sta.getTypeId());
                result.setEntityIp(sta.getEntityIp());
                result.setEntityId(sta.getEntityId());
                Integer sniPortCount = session.selectOne(getNameSpace("selectEntityPonCount"), entityId);
                result.setCurrentLinkedPortCount(sniPortCount);
                if (folderSniFlows.containsKey(sta.getFolderName())) {
                    List<OltPonPortFlowStastic> list = folderSniFlows.get(folderName);
                    list.add(result);
                } else {
                    List<OltPonPortFlowStastic> list = new ArrayList<OltPonPortFlowStastic>();
                    list.add(result);
                    folderSniFlows.put(folderName, list);
                }
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
        return folderSniFlows;
    }

    @Override
    public List<OltPonPortFlowDetail> selectPonFlowDetail(Map<String, Object> map) {
        map.put("collectInterval", devicePerfTargetDao.queryColIntervalByIdAndName(
                Long.parseLong(map.get("entityId").toString()), "olt_ponFlow"));
        return getSqlSession().selectList(getNameSpace("selectPonFlowDetail"), map);
    }

}
