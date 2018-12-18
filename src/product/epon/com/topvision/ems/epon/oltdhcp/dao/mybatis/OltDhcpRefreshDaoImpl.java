/***********************************************************************
 * $Id: OltDhcpRefreshDaoImpl.java,v1.0 2017年11月17日 上午11:34:34 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.oltdhcp.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.oltdhcp.dao.OltDhcpRefreshDao;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpCpeInfo;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpGlobalObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpPortAttribute;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpServerGroup;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStaticIp;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpStatisticsObjects;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVLANCfg;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltDhcpVifCfg;
import com.topvision.ems.epon.oltdhcp.facade.domain.TopOltPppoeStatisticsObjects;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author haojie
 * @created @2017年11月17日-上午11:34:34
 *
 */
@Repository("oltDhcpRefreshDao")
public class OltDhcpRefreshDaoImpl extends MyBatisDaoSupport<Object> implements OltDhcpRefreshDao {

    @Override
    protected String getDomainName() {
        return "OltDhcpRefresh";
    }

    @Override
    public void batchInsertTopOltDhcpCpeInfos(Long entityId, List<TopOltDhcpCpeInfo> topOltDhcpCpeInfos) {
        getSqlSession().delete(getNameSpace("deleteTopOltDhcpCpeInfos"), entityId);
        SqlSession session = getBatchSession();
        try {
            for (TopOltDhcpCpeInfo cpe : topOltDhcpCpeInfos) {
                cpe.setEntityId(entityId);
                session.insert(getNameSpace("insertTopOltDhcpCpeInfo"), cpe);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("DHCP CPE insert failed:", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTopOltDhcpGlobalObjects(Long entityId, TopOltDhcpGlobalObjects topOltDhcpGlobalObjects) {
        topOltDhcpGlobalObjects.setEntityId(entityId);
        getSqlSession().insert(getNameSpace("insertTopOltDhcpGlobalObjects"), topOltDhcpGlobalObjects);
    }

    @Override
    public void batchInsertTopOltDhcpPortAttributes(Long entityId,
            List<TopOltDhcpPortAttribute> topOltDhcpPortAttributes) {
        getSqlSession().delete(getNameSpace("deleteTopOltDhcpPortAttributes"), entityId);
        SqlSession session = getBatchSession();
        try {
            for (TopOltDhcpPortAttribute port : topOltDhcpPortAttributes) {
                port.setEntityId(entityId);
                session.insert(getNameSpace("insertTopOltDhcpPortAttribute"), port);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("DHCP portAttribute  insert failed:", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertTopOltDhcpServerGroups(Long entityId, List<TopOltDhcpServerGroup> topOltDhcpServerGroups) {
        getSqlSession().delete(getNameSpace("deleteTopOltDhcpServerGroups"), entityId);
        SqlSession session = getBatchSession();
        try {
            for (TopOltDhcpServerGroup group : topOltDhcpServerGroups) {
                group.setEntityId(entityId);
                session.insert(getNameSpace("insertTopOltDhcpServerGroup"), group);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("DHCP groups insert failed:", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertTopOltDhcpStaticIps(Long entityId, List<TopOltDhcpStaticIp> topOltDhcpStaticIps) {
        getSqlSession().delete(getNameSpace("deleteTopOltDhcpStaticIps"), entityId);
        SqlSession session = getBatchSession();
        try {
            for (TopOltDhcpStaticIp staticIp : topOltDhcpStaticIps) {
                staticIp.setEntityId(entityId);
                session.insert(getNameSpace("insertTopOltDhcpStaticIp"), staticIp);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("DHCP staticIps insert failed:", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTopOltDhcpStatisticsObjects(Long entityId,
            TopOltDhcpStatisticsObjects topOltDhcpStatisticsObjects) {
        topOltDhcpStatisticsObjects.setEntityId(entityId);
        getSqlSession().insert(getNameSpace("insertTopOltDhcpStatisticsObjects"), topOltDhcpStatisticsObjects);
    }

    @Override
    public void batchInsertTopOltDhcpVifCfgs(Long entityId, List<TopOltDhcpVifCfg> topOltDhcpVifCfgs) {
        getSqlSession().delete(getNameSpace("deleteTopOltDhcpVifCfgs"), entityId);
        SqlSession session = getBatchSession();
        try {
            for (TopOltDhcpVifCfg vif : topOltDhcpVifCfgs) {
                vif.setEntityId(entityId);
                session.insert(getNameSpace("insertTopOltDhcpVifCfg"), vif);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("DHCP VifCfgs insert failed:", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void batchInsertTopOltDhcpVLANCfgs(Long entityId, List<TopOltDhcpVLANCfg> topOltDhcpVLANCfgs) {
        getSqlSession().delete(getNameSpace("deleteTopOltDhcpVLANCfgs"), entityId);
        SqlSession session = getBatchSession();
        try {
            for (TopOltDhcpVLANCfg vlan : topOltDhcpVLANCfgs) {
                vlan.setEntityId(entityId);
                session.insert(getNameSpace("insertTopOltDhcpVLANCfg"), vlan);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("DHCP VlanCfgs insert failed:", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public void insertTopOltPppoeStatisticsObjects(Long entityId,
            TopOltPppoeStatisticsObjects topOltPppoeStatisticsObjects) {
        topOltPppoeStatisticsObjects.setEntityId(entityId);
        getSqlSession().insert(getNameSpace("insertTopOltPppoeStatisticsObjects"), topOltPppoeStatisticsObjects);
    }

}
