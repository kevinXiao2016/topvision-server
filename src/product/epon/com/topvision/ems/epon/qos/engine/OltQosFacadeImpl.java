/***********************************************************************
 * $Id: OltQosFacadeImpl.java,v1.0 2013年10月25日 下午5:54:30 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.engine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.ems.engine.executor.EmsFacade;
import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.QosGlobalSetTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.ems.epon.qos.facade.OltQosFacade;
import com.topvision.framework.annotation.Facade;
import com.topvision.framework.snmp.SnmpExecutorService;
import com.topvision.framework.snmp.SnmpParam;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:54:30
 *
 */
@Facade("oltQosFacade")
public class OltQosFacadeImpl extends EmsFacade implements OltQosFacade {
    @Autowired
    private SnmpExecutorService snmpExecutorService;

    /**
     * 刷新ONU qos队列优先级映射
     * 
     * @param snmpParam
     * @return
     */
    public List<QosDeviceBaseQosMapTable> getQosDeviceBaseQosMapTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, QosDeviceBaseQosMapTable.class);
    }

    /**
     * 刷新Onu qos策略
     * 
     * @param snmpParam
     * @return
     */
    public List<QosDeviceBaseQosPolicyTable> getQosDeviceBaseQosPolicyTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, QosDeviceBaseQosPolicyTable.class);
    }

    /**
     * 刷新QOS全局信息
     * 
     * @param snmpParam
     * @return
     */
    public List<QosGlobalSetTable> getQosGlobalSetTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, QosGlobalSetTable.class);
    }

    /**
     * 刷新PON　SNI口 QOS 队列优先级映射
     * 
     * @param snmpParam
     * @return
     */
    public List<QosPortBaseQosMapTable> getQosPortBaseQosMapTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, QosPortBaseQosMapTable.class);
    }

    /**
     * 刷新PON SNI口 qos策略
     * 
     * @param snmpParam
     * @return
     */
    public List<QosPortBaseQosPolicyTable> getQosPortBaseQosPolicyTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, QosPortBaseQosPolicyTable.class);
    }

    /**
     * 刷新ONU 的SLA表
     * 
     * @param snmpParam
     * @return
     */
    public List<SlaTable> getSlaTable(SnmpParam snmpParam) {
        return snmpExecutorService.getTable(snmpParam, SlaTable.class);
    }

    /**
     * 修改ONU的Qos队列优先级映射规则
     * 
     * @param snmpParam
     * @param qosDeviceBaseQosMapTable
     */
    public void modifyOnuQosMapRule(SnmpParam snmpParam, QosDeviceBaseQosMapTable qosDeviceBaseQosMapTable) {
        snmpExecutorService.setData(snmpParam, qosDeviceBaseQosMapTable);
    }

    /**
     * 修改ONU的Qos策略列表
     * 
     * @param snmpParam
     * @param qosDeviceBaseQosPolicyTable
     */
    public void modifyOnuQosPolicy(SnmpParam snmpParam, QosDeviceBaseQosPolicyTable qosDeviceBaseQosPolicyTable) {
        snmpExecutorService.setData(snmpParam, qosDeviceBaseQosPolicyTable);
    }

    /**
     * 修改Onu的sla配置
     * 
     * @param snmpParam
     * @param slaTable
     *            Sla配置
     */
    public void modifyOnuSlaList(SnmpParam snmpParam, SlaTable slaTable) {
        snmpExecutorService.setData(snmpParam, slaTable);
    }

    /**
     * 修改SNI或PON口的Qos队列优先级映射规则
     * 
     * @param snmpParam
     * @param qosPortBaseQosMapTable
     */
    public void modifyPortQosMapRule(SnmpParam snmpParam, QosPortBaseQosMapTable qosPortBaseQosMapTable) {
        snmpExecutorService.setData(snmpParam, qosPortBaseQosMapTable);
    }

    /**
     * 修改SNI或PON口的Qos策略列表
     * 
     * @param snmpParam
     * @param qosPortBaseQosPolicyTable
     */
    public void modifyPortQosPolicy(SnmpParam snmpParam, QosPortBaseQosPolicyTable qosPortBaseQosPolicyTable) {
        snmpExecutorService.setData(snmpParam, qosPortBaseQosPolicyTable);
    }

    /**
     * 获取单个ONU的SLA信息
     * 
     * @param snmpParam
     *            SNMP网管参数
     * @param onuIndex
     *            ONU索引
     * @return List<SlaTable>
     */
    @Override
    public SlaTable getSlaTableByOnuIndex(SnmpParam snmpParam, Long onuIndex) {
        SlaTable slaTable = new SlaTable();
        slaTable.setOnuIndex(onuIndex);
        snmpExecutorService.getTableLine(snmpParam, slaTable);
        return slaTable;
    }

    public SnmpExecutorService getSnmpExecutorService() {
        return snmpExecutorService;
    }

    public void setSnmpExecutorService(SnmpExecutorService snmpExecutorService) {
        this.snmpExecutorService = snmpExecutorService;
    }
}
