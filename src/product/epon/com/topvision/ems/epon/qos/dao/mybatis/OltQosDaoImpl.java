/***********************************************************************
 * $Id: OltQosDaoImpl.java,v1.0 2013-10-31 下午8:00:39 $
 * 
 * @author: jay
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.qos.dao.OltQosDao;
import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author jay
 * @created @2011-11-23-18:10:03
 * 
 * @Mybatis Modify by Rod @2013-10-25
 */
@Repository("oltQosDao")
public class OltQosDaoImpl extends MyBatisDaoSupport<Entity> implements OltQosDao {

    /**
     * 获取ONU的Qos队列优先级映射规则
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return List<QosDeviceBaseQosMapTable>
     */
    public QosDeviceBaseQosMapTable getOnuQosMapRule(Long entityId, Long onuIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("onuIndex", "" + onuIndex);
        return getSqlSession().selectOne(getNameSpace() + "getOnuQosMapRule", map);
    }

    /**
     * 获取ONU的Qos策略列表
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return List<QosDeviceBaseQosPolicyTable
     */
    public QosDeviceBaseQosPolicyTable getOnuQosPolicy(Long entityId, Long onuIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("onuIndex", "" + onuIndex);
        return getSqlSession().selectOne(getNameSpace() + "getOnuQosPolicy", map);
    }

    /**
     * 获取Onu的sla配置
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONUINDEX
     * @return
     */
    public SlaTable getOnuSlaList(Long entityId, Long onuIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("onuIndex", "" + onuIndex);
        return getSqlSession().selectOne(getNameSpace() + "getOnuSlaList", map);
    }

    /**
     * 获取SNI或PON口的Qos队列优先级映射规则
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return List<QosPortBaseQosMapTable>
     */
    public QosPortBaseQosMapTable getPortQosMapRule(Long entityId, Long portIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("portIndex", "" + portIndex);
        return getSqlSession().selectOne(getNameSpace() + "getPortQosMapRule", map);
    }

    /**
     * 获取SNI或PON口的Qos策略列表
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return QosPortBaseQosPolicyTable
     */
    public QosPortBaseQosPolicyTable getPortQosPolicy(Long entityId, Long portIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("portIndex", "" + portIndex);
        return getSqlSession().selectOne(getNameSpace() + "getPortQosPolicy", map);
    }

    /**
     * 修改ONU的Qos队列优先级映射规则
     * 
     * @param qosDeviceBaseQosMapTable
     */

    public void modifyOnuQosMapRule(QosDeviceBaseQosMapTable qosDeviceBaseQosMapTable) {
        getSqlSession().update(getNameSpace() + "updateOnuQosMapRule", qosDeviceBaseQosMapTable);
    }

    /**
     * 修改ONU的Qos策略列表
     * 
     * @param qosDeviceBaseQosPolicyTable
     */
    public void modifyOnuQosPolicy(QosDeviceBaseQosPolicyTable qosDeviceBaseQosPolicyTable) {
        getSqlSession().update(getNameSpace() + "updateOnuQosPolicy", qosDeviceBaseQosPolicyTable);
    }

    /**
     * 修改Onu的sla配置
     * 
     * @param slaTable
     *            Sla配置
     */
    public void modifyOnuSlaList(SlaTable slaTable) {
        getSqlSession().update(getNameSpace() + "updateOnuSlaList", slaTable);
    }

    /**
     * 修改SNI或PON口的Qos队列优先级映射规则
     * 
     * @param qosPortBaseQosMapTable
     */
    public void modifyPortQosMapRule(QosPortBaseQosMapTable qosPortBaseQosMapTable) {
        getSqlSession().update(getNameSpace() + "updatePortQosMapRule", qosPortBaseQosMapTable);
    }

    /**
     * 修改SNI或PON口的Qos策略列表
     * 
     * @param qosPortBaseQosPolicyTable
     */
    public void modifyPortQosPolicy(QosPortBaseQosPolicyTable qosPortBaseQosPolicyTable) {
        getSqlSession().update(getNameSpace() + "updatePortQosPolicy", qosPortBaseQosPolicyTable);
    }

    /**
     * 保存ONU qos队列优先级映射
     * 
     * @param qosDeviceBaseQosMapTables
     */
    public void saveQosDeviceBaseQosMapTable(Long entityId,
            final List<QosDeviceBaseQosMapTable> qosDeviceBaseQosMapTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteQosDeviceBaseQosMapTable", entityId);
            for (QosDeviceBaseQosMapTable qosDeviceBaseQosMapTable : qosDeviceBaseQosMapTables) {
                sqlSession.insert(getNameSpace() + "insertQosDeviceBaseQosMapTable", qosDeviceBaseQosMapTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存Onu qos策略
     * 
     * @param qosDeviceBaseQosPolicyTables
     */
    public void saveQosDeviceBaseQosPolicyTable(Long entityId,
            final List<QosDeviceBaseQosPolicyTable> qosDeviceBaseQosPolicyTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteQosDeviceBaseQosPolicyTable", entityId);
            for (QosDeviceBaseQosPolicyTable qosDeviceBaseQosPolicyTable : qosDeviceBaseQosPolicyTables) {
                sqlSession.insert(getNameSpace() + "insertQosDeviceBaseQosPolicyTable", qosDeviceBaseQosPolicyTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存PON　SNI口 QOS 队列优先级映射
     * 
     * @param qosPortBaseQosMapTables
     */
    public void saveQosPortBaseQosMapTable(Long entityId, final List<QosPortBaseQosMapTable> qosPortBaseQosMapTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteQosPortBaseQosMapTable", entityId);
            for (QosPortBaseQosMapTable qosPortBaseQosMapTable : qosPortBaseQosMapTables) {
                sqlSession.insert(getNameSpace() + "insertQosPortBaseQosMapTable", qosPortBaseQosMapTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存PON SNI口 qos策略
     * 
     * @param qosPortBaseQosPolicyTables
     */
    public void saveQosPortBaseQosPolicyTable(Long entityId,
            final List<QosPortBaseQosPolicyTable> qosPortBaseQosPolicyTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteQosPortBaseQosPolicyTable", entityId);
            for (QosPortBaseQosPolicyTable qosPortBaseQosPolicyTable : qosPortBaseQosPolicyTables) {
                sqlSession.insert(getNameSpace() + "insertQosPortBaseQosPolicyTable", qosPortBaseQosPolicyTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 保存ONU 的SLA表
     * 
     * @param slaTables
     */
    public void saveSlaTable(Long entityId, final List<SlaTable> slaTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteSlaTable", entityId);
            for (SlaTable slaTable : slaTables) {
                sqlSession.insert(getNameSpace() + "insertSlaTable", slaTable);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltQosDao#getOnuIndexList(java.lang.Long)
     */
    @Override
    public List<Long> getOnuIndexList(Long entityId, Long onuIndex) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("entityId", "" + entityId);
        map.put("onuIndex", "" + onuIndex);
        return getSqlSession().selectList(getNameSpace() + "getOnuIndexList", map);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.qos.domain.OltQos";
    }

}
