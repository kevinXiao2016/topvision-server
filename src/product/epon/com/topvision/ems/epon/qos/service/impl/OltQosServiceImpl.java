/***********************************************************************
 * $Id: OltQosServiceImpl.java,v1.0 2013年10月25日 下午5:50:00 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.qos.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.exception.ModifyOnuQosMapRuleException;
import com.topvision.ems.epon.exception.ModifyOnuQosPolicyException;
import com.topvision.ems.epon.exception.ModifyOnuSlaListException;
import com.topvision.ems.epon.exception.ModifyPortQosMapRuleException;
import com.topvision.ems.epon.exception.ModifyPortQosPolicyException;
import com.topvision.ems.epon.exception.RefreshQosDeviceBaseQosMapTableException;
import com.topvision.ems.epon.exception.RefreshQosDeviceBaseQosPolicyTableException;
import com.topvision.ems.epon.exception.RefreshQosPortBaseQosMapTableException;
import com.topvision.ems.epon.exception.RefreshQosPortBaseQosPolicyTableException;
import com.topvision.ems.epon.exception.RefreshSlaTableException;
import com.topvision.ems.epon.qos.dao.OltQosDao;
import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosDeviceBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosMapTable;
import com.topvision.ems.epon.qos.domain.QosPortBaseQosPolicyTable;
import com.topvision.ems.epon.qos.domain.SlaTable;
import com.topvision.ems.epon.qos.facade.OltQosFacade;
import com.topvision.ems.epon.qos.service.OltQosService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:50:00
 *
 */
@Service("oltQosService")
public class OltQosServiceImpl extends BaseService implements OltQosService {
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private OltQosDao oltQosDao;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
    }

    /**
     * 获取ONU的Qos队列优先级映射规则
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return QosDeviceBaseQosMapTable
     */
    @Override
    public QosDeviceBaseQosMapTable getOnuQosMapRule(Long entityId, Long onuIndex) {
        return oltQosDao.getOnuQosMapRule(entityId, onuIndex);
    }

    /**
     * 获取ONU的Qos策略列表
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return QosDeviceBaseQosPolicyTable
     */
    @Override
    public QosDeviceBaseQosPolicyTable getOnuQosPolicy(Long entityId, Long onuIndex) {
        return oltQosDao.getOnuQosPolicy(entityId, onuIndex);
    }

    /**
     * 获取SNI或PON口的Qos队列优先级映射规则
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return QosPortBaseQosMapTable
     */
    @Override
    public QosPortBaseQosMapTable getPortQosMapRule(Long entityId, Long portIndex) {
        return oltQosDao.getPortQosMapRule(entityId, portIndex);
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
    @Override
    public QosPortBaseQosPolicyTable getPortQosPolicy(Long entityId, Long portIndex) {
        return oltQosDao.getPortQosPolicy(entityId, portIndex);
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
    @Override
    public SlaTable getOnuSlaList(Long entityId, Long onuIndex) {
        return oltQosDao.getOnuSlaList(entityId, onuIndex);
    }

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

    @Override
    public void modifyOnuQosMapRule(Long entityId, Long onuIndex, Integer onuQosMapRuleIndex,
            List<Integer> deviceBaseQosMapOctetList) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltQosFacade oltQosFacade = getOltQosFacade(snmpParam.getIpAddress());
        try {
            QosDeviceBaseQosMapTable qosDeviceBaseQosMapTable = new QosDeviceBaseQosMapTable();
            qosDeviceBaseQosMapTable.setEntityId(entityId);
            qosDeviceBaseQosMapTable.setOnuIndex(onuIndex);
            qosDeviceBaseQosMapTable.setDeviceBaseQosMapRuleIndex(onuQosMapRuleIndex);
            qosDeviceBaseQosMapTable.setDeviceBaseQosMapOctetList(deviceBaseQosMapOctetList);
            // 修改一个AclList到设备
            oltQosFacade.modifyOnuQosMapRule(snmpParam, qosDeviceBaseQosMapTable);
            // 修改一个AclList到DB
            oltQosDao.modifyOnuQosMapRule(qosDeviceBaseQosMapTable);
        } catch (Exception e) {
            throw new ModifyOnuQosMapRuleException(e);
        }
    }

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
    @Override
    public void modifyOnuQosPolicy(Long entityId, Long onuIndex, Integer deviceBaseQosPolicyMode,
            List<Integer> deviceBaseQosPolicyWeightOctetList, List<Integer> deviceBaseQosPolicySpBandwidthRangeList) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltQosFacade oltQosFacade = getOltQosFacade(snmpParam.getIpAddress());
        try {
            QosDeviceBaseQosPolicyTable qosDeviceBaseQosPolicyTable = new QosDeviceBaseQosPolicyTable();
            qosDeviceBaseQosPolicyTable.setEntityId(entityId);
            qosDeviceBaseQosPolicyTable.setOnuIndex(onuIndex);
            qosDeviceBaseQosPolicyTable.setPolicyMode(deviceBaseQosPolicyMode);
            qosDeviceBaseQosPolicyTable.setDeviceBaseQosPolicyWeightOctetList(deviceBaseQosPolicyWeightOctetList);
            // TODO 注释掉带宽，暂不支持
            // qosDeviceBaseQosPolicyTable.setDeviceBaseQosPolicySpBandwidthRangeList(deviceBaseQosPolicySpBandwidthRangeList);
            // 修改一个AclList到设备
            oltQosFacade.modifyOnuQosPolicy(snmpParam, qosDeviceBaseQosPolicyTable);
            // 修改一个AclList到DB
            oltQosDao.modifyOnuQosPolicy(qosDeviceBaseQosPolicyTable);
        } catch (Exception e) {
            throw new ModifyOnuQosPolicyException(e);
        }
    }

    /**
     * 修改Onu的sla配置
     * 
     * @param slaTable
     *            Sla配置
     * @throws ModifyOnuSlaListException
     *             修改失败时抛出
     */
    @Override
    public void modifyOnuSlaList(SlaTable slaTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(slaTable.getEntityId());
        // 获取facade
        OltQosFacade oltQosFacade = getOltQosFacade(snmpParam.getIpAddress());
        try {
            // 修改一个SlaList到设备
            oltQosFacade.modifyOnuSlaList(snmpParam, slaTable);
            // 修改一个SlaList到DB
            oltQosDao.modifyOnuSlaList(slaTable);
        } catch (Exception e) {
            throw new ModifyOnuSlaListException(e);
        }
    }

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
    @Override
    public void modifyPortQosMapRule(Long entityId, Long portIndex, Integer portQosMapRuleIndex,
            List<Integer> portBaseQosMapOctetList) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltQosFacade oltQosFacade = getOltQosFacade(snmpParam.getIpAddress());
        try {
            QosPortBaseQosMapTable portBaseQosMapTable = new QosPortBaseQosMapTable();
            portBaseQosMapTable.setEntityId(entityId);
            portBaseQosMapTable.setPortIndex(portIndex);
            portBaseQosMapTable.setSlotNo(1L);
            portBaseQosMapTable.setPortNo(0L);
            portBaseQosMapTable.setPortBaseQosMapRuleIndex(portQosMapRuleIndex);
            portBaseQosMapTable.setPortBaseQosMapOctetList(portBaseQosMapOctetList);
            // 修改一个AclList到设备
            oltQosFacade.modifyPortQosMapRule(snmpParam, portBaseQosMapTable);
            // 修改一个AclList到DB
            portBaseQosMapTable.setPortIndex(portIndex);
            oltQosDao.modifyPortQosMapRule(portBaseQosMapTable);
        } catch (Exception e) {
            throw new ModifyPortQosMapRuleException(e);
        }
    }

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
    @Override
    public void modifyPortQosPolicy(Long entityId, Long portIndex, Integer portQosPolicyMode,
            List<Integer> portBaseQosPolicyWeightOctetList, List<Integer> portQosPolicySpBandwidthRange) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltQosFacade oltQosFacade = getOltQosFacade(snmpParam.getIpAddress());
        try {
            QosPortBaseQosPolicyTable qosPortBaseQosPolicyTable = new QosPortBaseQosPolicyTable();
            qosPortBaseQosPolicyTable.setEntityId(entityId);
            qosPortBaseQosPolicyTable.setPortIndex(portIndex);
            qosPortBaseQosPolicyTable.setSlotNo(1L);
            qosPortBaseQosPolicyTable.setPortNo(0L);
            qosPortBaseQosPolicyTable.setPolicyMode(portQosPolicyMode);
            qosPortBaseQosPolicyTable.setPortBaseQosPolicyWeightOctetList(portBaseQosPolicyWeightOctetList);
            qosPortBaseQosPolicyTable.setPortBaseQosPolicySpBandwidthRangeList(portQosPolicySpBandwidthRange);
            // 修改一个AclList到设备
            oltQosFacade.modifyPortQosPolicy(snmpParam, qosPortBaseQosPolicyTable);
            // 修改一个AclList到DB
            qosPortBaseQosPolicyTable.setPortIndex(portIndex);
            oltQosDao.modifyPortQosPolicy(qosPortBaseQosPolicyTable);
        } catch (Exception e) {
            throw new ModifyPortQosPolicyException(e);
        }
    }

    /**
     * 获取单个ONU所在PON口下的所有ONU SLA配置信息
     * 
     * @param entityId
     *            设备ID
     * @param onuIndex
     *            ONU索引
     * @return List<SlaTable>
     */
    @Override
    public List<SlaTable> getOnuSlaListInAPon(Long entityId, Long onuIndex) {
        // 从数据库获得该ONU所在PON口的所有ONU的SLA信息
        List<Long> tmpOnuList = new ArrayList<Long>();
        tmpOnuList = oltQosDao.getOnuIndexList(entityId, onuIndex);
        List<SlaTable> tmpSlaList = new ArrayList<SlaTable>();
        if (tmpOnuList != null && tmpOnuList.size() != 0) {
            for (Long aIndex : tmpOnuList) {
                SlaTable temSla = new SlaTable();
                temSla = oltQosDao.getOnuSlaList(entityId, aIndex);
                if (temSla != null) {
                    tmpSlaList.add(temSla);
                }
            }
        }
        return tmpSlaList;
    }

    /**
     * 获取OltQosFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltPerfFacade
     */
    private OltQosFacade getOltQosFacade(String ip) {
        return facadeFactory.getFacade(ip, OltQosFacade.class);
    }

    @Override
    public void refreshQosDeviceBaseQosMapTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltQosFacade oltPerfFacade = getOltQosFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<QosDeviceBaseQosMapTable> qosDeviceBaseQosMapTables = oltPerfFacade
                    .getQosDeviceBaseQosMapTable(snmpParam);
            for (QosDeviceBaseQosMapTable qosDeviceBaseQosMapTable : qosDeviceBaseQosMapTables) {
                qosDeviceBaseQosMapTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltQosDao.saveQosDeviceBaseQosMapTable(entityId, qosDeviceBaseQosMapTables);
        } catch (Exception e) {
            throw new RefreshQosDeviceBaseQosMapTableException(e);
        }
    }

    @Override
    public void refreshQosDeviceBaseQosPolicyTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltQosFacade oltPerfFacade = getOltQosFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<QosDeviceBaseQosPolicyTable> qosDeviceBaseQosPolicyTables = oltPerfFacade
                    .getQosDeviceBaseQosPolicyTable(snmpParam);
            for (QosDeviceBaseQosPolicyTable qosDeviceBaseQosPolicyTable : qosDeviceBaseQosPolicyTables) {
                qosDeviceBaseQosPolicyTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltQosDao.saveQosDeviceBaseQosPolicyTable(entityId, qosDeviceBaseQosPolicyTables);
        } catch (Exception e) {
            throw new RefreshQosDeviceBaseQosPolicyTableException(e);
        }
    }

    @Override
    public void refreshQosPortBaseQosMapTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltQosFacade oltPerfFacade = getOltQosFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<QosPortBaseQosMapTable> qosPortBaseQosMapTables = oltPerfFacade.getQosPortBaseQosMapTable(snmpParam);
            for (QosPortBaseQosMapTable qosPortBaseQosMapTable : qosPortBaseQosMapTables) {
                qosPortBaseQosMapTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltQosDao.saveQosPortBaseQosMapTable(entityId, qosPortBaseQosMapTables);
        } catch (Exception e) {
            throw new RefreshQosPortBaseQosMapTableException(e);
        }
    }

    @Override
    public void refreshQosPortBaseQosPolicyTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltQosFacade oltPerfFacade = getOltQosFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<QosPortBaseQosPolicyTable> portBaseQosPolicyTables = oltPerfFacade
                    .getQosPortBaseQosPolicyTable(snmpParam);
            for (QosPortBaseQosPolicyTable portBaseQosPolicyTable : portBaseQosPolicyTables) {
                portBaseQosPolicyTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltQosDao.saveQosPortBaseQosPolicyTable(entityId, portBaseQosPolicyTables);
        } catch (Exception e) {
            throw new RefreshQosPortBaseQosPolicyTableException(e);
        }
    }

    @Override
    public void refreshSlaTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltQosFacade oltPerfFacade = getOltQosFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        try {
            List<SlaTable> slaTables = oltPerfFacade.getSlaTable(snmpParam);
            for (SlaTable slaTable : slaTables) {
                slaTable.setEntityId(entityId);
            }
            // 将设置保存到数据库
            oltQosDao.saveSlaTable(entityId, slaTables);
        } catch (Exception e) {
            throw new RefreshSlaTableException(e);
        }
    }

}
