/***********************************************************************
 * $Id: OltMirrorServiceImpl.java,v1.0 2013-10-25 下午2:09:04 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.mirror.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.exception.SetValueConflictException;
import com.topvision.ems.epon.mirror.dao.OltMirrorDao;
import com.topvision.ems.epon.mirror.domain.OltSniMirrorConfig;
import com.topvision.ems.epon.mirror.facade.OltMirrorFacade;
import com.topvision.ems.epon.mirror.service.OltMirrorService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2013-10-25-下午2:09:04
 *
 */
@Service("oltMirrorService")
public class OltMirrorServiceImpl extends BaseService implements OltMirrorService, SynchronizedListener {
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltMirrorDao oltMirrorDao;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;

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

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        try {
            refreshOltMirror(event.getEntityId());
            logger.info("refreshOltMirror finish");
        } catch (Exception e) {
            logger.error("refreshOltMirror wrong", e);
        }
    }

    @Override
    public void refreshOltMirror(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        List<OltSniMirrorConfig> oltSniMirrorConfigs = getOltMirrorFacade(snmpParam.getIpAddress())
                .getOltMirrorConfigs(snmpParam);
        if (oltSniMirrorConfigs != null) {
            oltMirrorDao.batchInsertOltSniMirrorConfig(oltSniMirrorConfigs, entityId);
        }
    }

    @Override
    public List<OltSniMirrorConfig> getMirrorConfigList(Long entityId) {
        return oltMirrorDao.getMirrorConfigList(entityId);
    }

    @Override
    public void modifyMirrorName(Long entityId, Integer sniMirrorGroupIndex, String sniMirrorGroupName) {
        OltSniMirrorConfig oltSniMirrorConfig = new OltSniMirrorConfig();
        oltSniMirrorConfig.setEntityId(entityId);
        oltSniMirrorConfig.setSniMirrorGroupIndex(sniMirrorGroupIndex);
        oltSniMirrorConfig.setSniMirrorGroupName(sniMirrorGroupName);
        // 设备上修改镜像名称
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltSniMirrorConfig mirrorConfig = getOltMirrorFacade(snmpParam.getIpAddress()).modifyMirrorName(snmpParam,
                oltSniMirrorConfig);
        // 验证修改结果
        if (mirrorConfig != null && mirrorConfig.getSniMirrorGroupName().equals(sniMirrorGroupName)) {
            oltMirrorDao.modifyMirrorName(entityId, sniMirrorGroupIndex, sniMirrorGroupName);
        } else {
            throw new SetValueConflictException("Business.setMirrorName");
        }
    }

    @Override
    public void updateMirrorPortList(Long entityId, Integer sniMirrorGroupIndex,
            List<Long> sniMirrorGroupSrcOutPortIndexList, List<Long> sniMirrorGroupSrcInPortIndexList,
            List<Long> sniMirrorGroupDstPortIndexList) {
        OltSniMirrorConfig oltSniMirrorConfig = new OltSniMirrorConfig();
        oltSniMirrorConfig.setEntityId(entityId);
        oltSniMirrorConfig.setSniMirrorGroupIndex(sniMirrorGroupIndex);
        oltSniMirrorConfig.setSniMirrorGroupDstPortIndexList(sniMirrorGroupDstPortIndexList);
        oltSniMirrorConfig.setSniMirrorGroupSrcInPortIndexList(sniMirrorGroupSrcInPortIndexList);
        oltSniMirrorConfig.setSniMirrorGroupSrcOutPortIndexList(sniMirrorGroupSrcOutPortIndexList);
        // 设备上修改镜像端口列表
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        OltSniMirrorConfig mirrorConfig = getOltMirrorFacade(snmpParam.getIpAddress()).updateMirrorPortList(snmpParam,
                oltSniMirrorConfig);
        // 验证修改结果
        if (mirrorConfig != null
                && mirrorConfig.getSniMirrorGroupDstPortList()
                        .equals(oltSniMirrorConfig.getSniMirrorGroupDstPortList())
                && mirrorConfig.getSniMirrorGroupSrcInPortList().equals(
                        oltSniMirrorConfig.getSniMirrorGroupSrcInPortList())
                && mirrorConfig.getSniMirrorGroupSrcOutPortList().equals(
                        oltSniMirrorConfig.getSniMirrorGroupSrcOutPortList())) {
            oltMirrorDao.updateMirrorPortList(oltSniMirrorConfig);
        } else {
            throw new SetValueConflictException("Business.setMirrorPortList");
        }
    }

    /**
     * 获取OltControlFacade对象
     * 
     * @param ip
     *            被采集设备IP
     * @return OltControlFacade
     */
    private OltMirrorFacade getOltMirrorFacade(String ip) {
        return facadeFactory.getFacade(ip, OltMirrorFacade.class);
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {
    }
}
