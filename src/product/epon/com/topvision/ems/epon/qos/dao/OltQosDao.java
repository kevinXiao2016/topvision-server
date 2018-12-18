/***********************************************************************
 * $Id: OltQosDao.java,v1.0 2013年10月25日 下午5:52:43 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.dao;

import java.util.List;

import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:52:43
 *
 */
public interface OltQosDao extends BaseEntityDao<Entity> {

    /**
     * 获取ONU的Qos队列优先级映射规则
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return QosDeviceBaseQosMapTable
     */
    QosDeviceBaseQosMapTable getOnuQosMapRule(Long entityId, Long onuIndex);

    /**
     * 获取ONU的Qos策略列表
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return QosDeviceBaseQosPolicyTable
     */
    QosDeviceBaseQosPolicyTable getOnuQosPolicy(Long entityId, Long onuIndex);

    /**
     * 获取SNI或PON口的Qos队列优先级映射规则
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return QosPortBaseQosMapTable
     */
    QosPortBaseQosMapTable getPortQosMapRule(Long entityId, Long portIndex);

    /**
     * 获取SNI或PON口的Qos策略列表
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return QosPortBaseQosPolicyTable
     */
    QosPortBaseQosPolicyTable getPortQosPolicy(Long entityId, Long portIndex);

    /**
     * 获取Onu的sla配置
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONUINDEX
     * @return
     */
    SlaTable getOnuSlaList(Long entityId, Long onuIndex);

    /**
     * 修改ONU的Qos队列优先级映射规则
     * 
     * @param qosDeviceBaseQosMapTable
     */

    void modifyOnuQosMapRule(QosDeviceBaseQosMapTable qosDeviceBaseQosMapTable);

    /**
     * 修改ONU的Qos策略列表
     * 
     * @param qosDeviceBaseQosPolicyTable
     */
    void modifyOnuQosPolicy(QosDeviceBaseQosPolicyTable qosDeviceBaseQosPolicyTable);

    /**
     * 修改Onu的sla配置
     * 
     * @param slaTable
     *            Sla配置
     */
    void modifyOnuSlaList(SlaTable slaTable);

    /**
     * 修改SNI或PON口的Qos队列优先级映射规则
     * 
     * @param qosPortBaseQosMapTable
     */
    void modifyPortQosMapRule(QosPortBaseQosMapTable qosPortBaseQosMapTable);

    /**
     * 修改SNI或PON口的Qos策略列表
     * 
     * @param qosPortBaseQosPolicyTable
     */
    void modifyPortQosPolicy(QosPortBaseQosPolicyTable qosPortBaseQosPolicyTable);

    /**
     * 保存ONU qos队列优先级映射
     * 
     * @param qosDeviceBaseQosMapTables
     */
    void saveQosDeviceBaseQosMapTable(Long entityId, List<QosDeviceBaseQosMapTable> qosDeviceBaseQosMapTables);

    /**
     * 保存Onu qos策略
     * 
     * @param qosDeviceBaseQosPolicyTables
     */
    void saveQosDeviceBaseQosPolicyTable(Long entityId, List<QosDeviceBaseQosPolicyTable> qosDeviceBaseQosPolicyTables);

    /**
     * 保存PON　SNI口 QOS 队列优先级映射
     * 
     * @param qosPolicyTables
     */
    void saveQosPortBaseQosMapTable(Long entityId, List<QosPortBaseQosMapTable> qosPolicyTables);

    /**
     * 保存PON SNI口 qos策略
     * 
     * @param portBaseQosPolicyTables
     */
    void saveQosPortBaseQosPolicyTable(Long entityId, List<QosPortBaseQosPolicyTable> portBaseQosPolicyTables);

    /**
     * 保存ONU 的SLA表
     * 
     * @param slaTables
     */
    void saveSlaTable(Long entityId, List<SlaTable> slaTables);

    /**
     * 获取单个ONU所在PON口下得所有ONU索引
     * 
     * @param onuIndex
     *            ONU索引
     * @param entityId
     *            设备索引
     * @return List<Long>
     */
    List<Long> getOnuIndexList(Long entityId, Long onuIndex);
}
