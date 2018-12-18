/***********************************************************************
 * $Id: OltQosFacade.java,v1.0 2013年10月25日 下午5:53:46 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.facade;

import java.util.List;

import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.QosGlobalSetTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.ems.facade.Facade;
import com.topvision.framework.annotation.EngineFacade;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:53:46
 *
 */
@EngineFacade(serviceName = "OltQosFacade", beanName = "oltQosFacade")
public interface OltQosFacade extends Facade {
    /***************************************************** sni口和pon口 *******************************************/
    /**
     * 修改SNI或PON口的Qos队列优先级映射规则
     * 
     * @param snmpParam
     * @param qosPortBaseQosMapTable
     */
    void modifyPortQosMapRule(SnmpParam snmpParam, QosPortBaseQosMapTable qosPortBaseQosMapTable);

    /**
     * 修改SNI或PON口的Qos策略列表
     * 
     * @param snmpParam
     * @param qosPortBaseQosPolicyTable
     */
    void modifyPortQosPolicy(SnmpParam snmpParam, QosPortBaseQosPolicyTable qosPortBaseQosPolicyTable);

    /***************************************************** ONU *******************************************/
    /**
     * 修改ONU的Qos队列优先级映射规则
     * 
     * @param snmpParam
     * @param qosDeviceBaseQosMapTable
     */
    void modifyOnuQosMapRule(SnmpParam snmpParam, QosDeviceBaseQosMapTable qosDeviceBaseQosMapTable);

    /**
     * 修改ONU的Qos策略列表
     * 
     * @param snmpParam
     * @param qosDeviceBaseQosPolicyTable
     */
    void modifyOnuQosPolicy(SnmpParam snmpParam, QosDeviceBaseQosPolicyTable qosDeviceBaseQosPolicyTable);

    /**
     * 修改Onu的sla配置
     * 
     * @param snmpParam
     * @param slaTable
     *            Sla配置
     */
    void modifyOnuSlaList(SnmpParam snmpParam, SlaTable slaTable);

    /**
     * 刷新ONU qos队列优先级映射
     * 
     * @param snmpParam
     * @return
     */
    List<QosDeviceBaseQosMapTable> getQosDeviceBaseQosMapTable(SnmpParam snmpParam);

    /**
     * 刷新Onu qos策略
     * 
     * @param snmpParam
     * @return
     */
    List<QosDeviceBaseQosPolicyTable> getQosDeviceBaseQosPolicyTable(SnmpParam snmpParam);

    /**
     * 刷新QOS全局信息
     * 
     * @param snmpParam
     * @return
     */
    List<QosGlobalSetTable> getQosGlobalSetTable(SnmpParam snmpParam);

    /**
     * 刷新PON　SNI口 QOS 队列优先级映射
     * 
     * @param snmpParam
     * @return
     */
    List<QosPortBaseQosMapTable> getQosPortBaseQosMapTable(SnmpParam snmpParam);

    /**
     * 刷新PON SNI口 qos策略
     * 
     * @param snmpParam
     * @return
     */
    List<QosPortBaseQosPolicyTable> getQosPortBaseQosPolicyTable(SnmpParam snmpParam);

    /**
     * 刷新ONU 的SLA表
     * 
     * @param snmpParam
     * @return
     */
    List<SlaTable> getSlaTable(SnmpParam snmpParam);

    /**
     * 获取单个ONU的SLA信息
     * 
     * @param snmpParam
     *            SNMP网管参数
     * @param onuIndex
     *            ONU索引
     * @return List<SlaTable>
     */
    SlaTable getSlaTableByOnuIndex(SnmpParam snmpParam, Long onuIndex);
}
