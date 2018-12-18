/***********************************************************************
 * $Id: OltAclServiceImpl.java,v1.0 2013年10月25日 下午5:40:14 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.acl.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.acl.dao.OltAclDao;
import com.topvision.ems.epon.acl.domain.AclListTable;
import com.topvision.ems.epon.acl.domain.AclPortACLListTable;
import com.topvision.ems.epon.acl.domain.AclRuleTable;
import com.topvision.ems.epon.acl.facade.OltAclFacade;
import com.topvision.ems.epon.acl.service.OltAclService;
import com.topvision.ems.epon.exception.AddAclListException;
import com.topvision.ems.epon.exception.AddAclPortACLListException;
import com.topvision.ems.epon.exception.AddAclRuleListException;
import com.topvision.ems.epon.exception.DeleteAclListException;
import com.topvision.ems.epon.exception.DeleteAclPortACLListException;
import com.topvision.ems.epon.exception.DeleteAclRuleListException;
import com.topvision.ems.epon.exception.ModifyAclListException;
import com.topvision.ems.epon.exception.ModifyAclRuleListException;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.common.LoggerUtil;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author Bravin
 * @created @2013年10月25日-下午5:40:14
 *
 */
@Service("oltAclService")
public class OltAclServiceImpl extends BaseService implements OltAclService, SynchronizedListener {
    @Autowired
    private EntityService entityService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OltAclDao oltAclDao;

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    /**
     * 新增设备业务属性
     * 
     * @param event
     */
    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        Long timeTmp = 0L;
        logger.info("begin to discovery AclData EntityId:" + event.getEntityId());
        try {
            timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "AclListTable");
            refreshAclListTable(event.getEntityId());
            LoggerUtil.topoEndTimeLog(event.getIpAddress(), "AclListTable", timeTmp);
            logger.info("refresh AclList finished!");
            try {
                timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "AclRuleTable");
                refreshAclRuleList(event.getEntityId());
                LoggerUtil.topoEndTimeLog(event.getIpAddress(), "AclRuleTable", timeTmp);
                logger.info("refresh AclRule finished!");
                try {
                    timeTmp = LoggerUtil.topoStartTimeLog(event.getIpAddress(), "AclPortACLListTable");
                    refreshAclPortACLList(event.getEntityId());
                    LoggerUtil.topoEndTimeLog(event.getIpAddress(), "AclPortACLListTable", timeTmp);
                    logger.info("refresh AclPort finished!");
                } catch (Exception e) {
                    logger.error("refresh AclPort error", e);
                }
            } catch (Exception e) {
                logger.error("refresh AclRule error", e);
            }
        } catch (Exception e) {
            logger.error("refresh AclList error", e);
        }
        logger.info("finish discovery AclData EntityId:" + event.getEntityId());
    }

    /**
     * 同步设备业务属性
     * 
     * @param event
     */
    @Override
    public void updateEntityStates(SynchronizedEvent event) {

    }

    /**
     * 添加一个AclList
     * 
     * @param aclListTable
     *            AclList的参数
     * @throws AddAclListException
     *             添加AclList失败时抛出
     */
    @Override
    public void addAclList(AclListTable aclListTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(aclListTable.getEntityId());
        // 获取facade
        OltAclFacade oltAclFacade = getOltAclFacade(snmpParam.getIpAddress());
        // 添加一个AclList到设备
        oltAclFacade.addAclList(snmpParam, aclListTable);
        // 添加一个AclList到DB
        aclListTable.setTopAclRuleNum(0);
        oltAclDao.addAclList(aclListTable);
    }

    /**
     * 关联一个ACLLIST到一个端口
     * 
     * @param aclPortACLListTable
     *            关联参数
     * @throws AddAclPortACLListException
     *             当添加关联失败的时候抛出
     */
    @Override
    public void addAclPortACLList(AclPortACLListTable aclPortACLListTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(aclPortACLListTable.getEntityId());
        // 获取facade
        OltAclFacade oltAclFacade = getOltAclFacade(snmpParam.getIpAddress());
        // 关联一个ACLLIST到一个端口到设备
        oltAclFacade.addAclPortACLList(snmpParam, aclPortACLListTable);
        // 关联一个ACLLIST到一个端口到DB
        oltAclDao.addAclPortACLList(aclPortACLListTable);
    }

    /**
     * 添加一个AclRule
     * 
     * @param aclRuleTable
     *            AclRule的参数
     * @throws AddAclRuleListException
     *             当添加AclRule失败的时候抛出
     */
    @Override
    public void addAclRuleList(AclRuleTable aclRuleTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(aclRuleTable.getEntityId());
        // 获取facade
        OltAclFacade oltAclFacade = getOltAclFacade(snmpParam.getIpAddress());
        // 添加一个AclRule到设备
        oltAclFacade.addAclRuleList(snmpParam, aclRuleTable);
        // 添加一个AclRule到DB
        // 须同时修改规则Num号
        oltAclDao.addAclRuleList(aclRuleTable);
        oltAclDao.modifyAclListAclRuleNum(aclRuleTable.getEntityId(), aclRuleTable.getTopAclRuleListIndex(),
                EponConstants.ACL_LIST_RULENUM_ADD);
    }

    /**
     * 删除一个AclList
     * 
     * @param entityId
     *            设备Id
     * @param topAclListIndex
     *            AclList的index
     * @throws DeleteAclListException
     *             删除AclList失败时抛出
     */
    @Override
    public void deleteAclList(Long entityId, Integer topAclListIndex) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltAclFacade oltAclFacade = getOltAclFacade(snmpParam.getIpAddress());
        // 删除一个AclList到DB
        oltAclDao.deleteAclList(entityId, topAclListIndex);
        // 删除一个AclList到设备
        oltAclFacade.deleteAclList(snmpParam, topAclListIndex);
    }

    /**
     * 删除一个acllist的关联
     * 
     * @param aclPortACLListTable
     *            删除管理的参数
     * @throws DeleteAclPortACLListException
     *             当删除关联失败的时候抛出
     */
    @Override
    public void deleteAclPortACLList(AclPortACLListTable aclPortACLListTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(aclPortACLListTable.getEntityId());
        // 获取facade
        OltAclFacade oltAclFacade = getOltAclFacade(snmpParam.getIpAddress());
        // 删除一个acllist的关联到设备
        oltAclFacade.deleteAclPortACLList(snmpParam, aclPortACLListTable);
        // 删除一个acllist的关联到DB
        oltAclDao.deleteAclPortACLList(aclPortACLListTable);
    }

    /**
     * 删除一个AclRule
     * 
     * @param entityId
     *            设备Id
     * @param topAclRuleListIndex
     *            AclList的index
     * @param topAclRuleIndex
     *            aclrule的Index
     * @throws DeleteAclRuleListException
     *             当删除AclRule失败的时候抛出
     */
    @Override
    public void deleteAclRuleList(Long entityId, Integer topAclRuleListIndex, Integer topAclRuleIndex) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltAclFacade oltAclFacade = getOltAclFacade(snmpParam.getIpAddress());
        // 删除一个AclList到设备
        oltAclFacade.deleteAclRuleList(snmpParam, topAclRuleListIndex, topAclRuleIndex);
        // 删除一个AclList到DB
        oltAclDao.deleteAclRuleList(entityId, topAclRuleListIndex, topAclRuleIndex);
        // 删除后修改对应的ACLlist的ruleNum
        oltAclDao.modifyAclListAclRuleNum(entityId, topAclRuleListIndex, EponConstants.ACL_LIST_RULENUM_DEL);
    }

    /**
     * 获取设备所有AclList的方法
     * 
     * @param entityId
     *            设备ID
     * @return List<AclListTable>
     */
    @Override
    public List<AclListTable> getAclList(Long entityId) {
        return oltAclDao.getAclList(entityId);
    }

    /**
     * 获取端口下关联AclList的方法
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @param topAclPortDirection
     *            规则方向
     * @return List<AclPortACLListTable>
     */
    @Override
    public List<AclPortACLListTable> getAclPortACLList(Long entityId, Long portIndex, Integer topAclPortDirection) {
        return oltAclDao.getAclPortACLList(entityId, portIndex, topAclPortDirection);
    }

    /**
     * 获取端口所在的板卡的所有端口绑定的ACL的ID列表
     * 
     * @param entityId
     *            设备ID
     * @param portIndex
     *            端口INDEX
     * @return List<Integer>
     */
    @Override
    public List<Integer> getAclListBySlot(Long entityId, Long portIndex) {
        List<Integer> aclIdList = new ArrayList<Integer>();
        Long slotNum = EponIndex.getSlotNo(portIndex);
        List<AclPortACLListTable> tmpList = oltAclDao.getAllAclPortACLList(entityId);
        if (tmpList != null && tmpList.size() != 0) {
            for (AclPortACLListTable tmp : tmpList) {
                if (EponIndex.getSlotNo(tmp.getDeviceIndex()).equals(slotNum)) {
                    aclIdList.add(tmp.getTopPortAclListIndex());
                }
            }
        }
        return aclIdList;
    }

    /**
     * 获取一个Acllist下的所有Aclrule
     * 
     * @param entityId
     *            设备Id
     * @param topAclRuleListIndex
     *            AclList的Index
     * @return List<AclRuleTable>
     */
    @Override
    public List<AclRuleTable> getAclRuleList(Long entityId, Integer topAclRuleListIndex) {
        return oltAclDao.getAclRuleList(entityId, topAclRuleListIndex);
    }

    /**
     * 获取所有的Aclrule
     * 
     * @param entityId
     *            设备Id
     * @return List<AclRuleTable>
     */
    @Override
    public List<AclRuleTable> getAllAclRuleList(Long entityId) {
        return oltAclDao.getAllAclRuleList(entityId);
    }

    /**
     * 获取一个AclList下的所有port的方法
     * 
     * @param entityId
     *            设备Id
     * @param topAclRuleListIndex
     *            AclList的Index
     * @return List<AclPortACLListTable>
     */
    @Override
    public List<AclPortACLListTable> getAclPortByAclList(Long entityId, Integer topAclRuleListIndex) {
        return oltAclDao.getAclPortByAclList(entityId, topAclRuleListIndex);
    }

    /**
     * 修改一个AclList
     * 
     * @param aclListTable
     *            修改的参数
     * @throws ModifyAclListException
     *             修改AclList失败时抛出
     */
    @Override
    public void modifyAclList(AclListTable aclListTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(aclListTable.getEntityId());
        // 获取facade
        OltAclFacade oltAclFacade = getOltAclFacade(snmpParam.getIpAddress());
        // 修改一个AclList到设备
        oltAclFacade.modifyAclList(snmpParam, aclListTable);
        // 修改一个AclList到DB
        oltAclDao.modifyAclList(aclListTable);
    }

    /**
     * 修改一个AclRule
     * 
     * @param aclRuleTable
     * @throws ModifyAclRuleListException
     *             当修改AclRule失败的时候抛出
     */
    @Override
    public void modifyAclRuleList(AclRuleTable aclRuleTable) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(aclRuleTable.getEntityId());
        // 获取facade
        OltAclFacade oltAclFacade = getOltAclFacade(snmpParam.getIpAddress());
        // 修改一个AclRule到设备
        oltAclFacade.modifyAclRuleList(snmpParam, aclRuleTable);
        // 修改一个AclRule到DB
        oltAclDao.modifyAclRuleList(aclRuleTable);
    }

    /**
     * 获取OltAclFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltPerfFacade
     */
    private OltAclFacade getOltAclFacade(String ip) {
        return facadeFactory.getFacade(ip, OltAclFacade.class);
    }

    @Override
    public void refreshAclRuleList(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltAclFacade oltPerfFacade = getOltAclFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        List<AclRuleTable> aclRuleTables = oltPerfFacade.getAclRuleList(snmpParam);
        for (AclRuleTable aclRuleTable : aclRuleTables) {
            aclRuleTable.setEntityId(entityId);
        }
        // 将设置保存到数据库
        oltAclDao.saveAclRuleList(entityId, aclRuleTables);
    }

    @Override
    public void refreshAclPortACLList(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltAclFacade oltPerfFacade = getOltAclFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        List<AclPortACLListTable> aclPortACLListTables = oltPerfFacade.getAclPortACLList(snmpParam);
        for (AclPortACLListTable aclPortACLListTable : aclPortACLListTables) {
            aclPortACLListTable.setEntityId(entityId);
        }
        // 将设置保存到数据库
        oltAclDao.saveAclPortACLList(entityId, aclPortACLListTables);
    }

    @Override
    public void refreshAclListTable(Long entityId) {
        // 获得设备实体
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        // 获取facade
        OltAclFacade oltPerfFacade = getOltAclFacade(snmpParam.getIpAddress());
        // 从设备获取轮询间隔设置
        List<AclListTable> aclListTables = oltPerfFacade.getAclList(snmpParam);
        for (AclListTable aclListTable : aclListTables) {
            aclListTable.setEntityId(entityId);
        }
        // 将设置保存到数据库
        oltAclDao.saveAclList(entityId, aclListTables);
    }

    @Override
    public AclRuleTable getAclRule(Long entityId, Integer aclIndex, Integer aclRuleIndex) {
        return oltAclDao.getAclRule(entityId, aclIndex, aclRuleIndex);
    }

}
