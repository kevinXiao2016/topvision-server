/***********************************************************************
 * $Id: OltQosService.java,v1.0 2013年10月25日 下午5:49:10 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.service;

import java.util.List;

import com.topvision.ems.epon.exception.ModifyOnuQosMapRuleException;
import com.topvision.ems.epon.exception.ModifyOnuQosPolicyException;
import com.topvision.ems.epon.exception.ModifyOnuSlaListException;
import com.topvision.ems.epon.exception.ModifyPortQosMapRuleException;
import com.topvision.ems.epon.exception.ModifyPortQosPolicyException;
import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.framework.service.Service;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:49:10
 *
 */
public interface OltQosService extends Service {
    /***************************************************** sni口和pon口 *******************************************/
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
     * 修改SNI或PON口的Qos队列优先级映射规则
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @param portQosMapRuleIndex
     *            规则INDEX
     * @param portBaseQosMapOctetList
     *            映射关系列表
     * @throws ModifyPortQosMapRuleException
     *             修改失败时抛出
     */
    void modifyPortQosMapRule(Long entityId, Long portIndex, Integer portQosMapRuleIndex,
            List<Integer> portBaseQosMapOctetList);

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
     * 修改SNI或PON口的Qos策略列表
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @param portQosPolicyMode
     *            策略模式
     * @param portBaseQosPolicyWeightOctetList
     *            权重关系列表
     * @param portQosPolicySpBandwidthRange
     *            队列带宽
     * @throws ModifyPortQosPolicyException
     *             修改失败时抛出
     */
    void modifyPortQosPolicy(Long entityId, Long portIndex, Integer portQosPolicyMode,
            List<Integer> portBaseQosPolicyWeightOctetList, List<Integer> portQosPolicySpBandwidthRange);

    /***************************************************** ONU *******************************************/
    /**
     * 获取ONU的Qos队列优先级映射规则
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONUINDEX
     * @return QosDeviceBaseQosMapTable
     */
    QosDeviceBaseQosMapTable getOnuQosMapRule(Long entityId, Long onuIndex);

    /**
     * 修改ONU的Qos队列优先级映射规则
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONUINDEX
     * @param onuQosMapRuleIndex
     *            规则INDEX
     * @param deviceBaseQosMapOctetList
     *            映射关系列表
     * @throws ModifyOnuQosMapRuleException
     *             修改失败时抛出
     */

    void modifyOnuQosMapRule(Long entityId, Long onuIndex, Integer onuQosMapRuleIndex,
            List<Integer> deviceBaseQosMapOctetList);

    /**
     * 获取ONU的Qos策略列表
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONUINDEX
     * @return QosDeviceBaseQosPolicyTable
     */
    QosDeviceBaseQosPolicyTable getOnuQosPolicy(Long entityId, Long onuIndex);

    /**
     * 修改ONU的Qos策略列表
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONUINDEX
     * @param deviceBaseQosPolicyMode
     *            策略模式
     * @param deviceBaseQosPolicyWeightOctetList
     *            权重关系列表
     * @param deviceBaseQosPolicySpBandwidthRangeList
     *            队列带宽
     * @throws ModifyOnuQosPolicyException
     *             修改失败时抛出
     */
    void modifyOnuQosPolicy(Long entityId, Long onuIndex, Integer deviceBaseQosPolicyMode,
            List<Integer> deviceBaseQosPolicyWeightOctetList, List<Integer> deviceBaseQosPolicySpBandwidthRangeList);

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
     * 修改Onu的sla配置
     * 
     * @param slaTable
     *            Sla配置
     * @throws ModifyOnuSlaListException
     *             修改失败时抛出
     */
    void modifyOnuSlaList(SlaTable slaTable);

    /**
     * 获取单个ONU所在PON口下的所有ONU SLA配置信息
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONU索引
     * @return List<SlaTable>
     */
    List<SlaTable> getOnuSlaListInAPon(Long entityId, Long onuIndex);

    void refreshQosPortBaseQosMapTable(Long entityId);

    void refreshQosPortBaseQosPolicyTable(Long entityId);

    void refreshSlaTable(Long entityId);

    void refreshQosDeviceBaseQosMapTable(Long entityId);

    void refreshQosDeviceBaseQosPolicyTable(Long entityId);
}
