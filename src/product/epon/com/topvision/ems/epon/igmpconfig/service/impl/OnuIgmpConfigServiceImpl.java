/***********************************************************************
 * $Id: OnuIgmpConfigServiceImpl.java,v1.0 2016-6-6 下午4:16:49 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.igmpconfig.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.igmpconfig.IgmpConstants;
import com.topvision.ems.epon.igmpconfig.dao.OnuIgmpConfigDao;
import com.topvision.ems.epon.igmpconfig.domain.IgmpOnuConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpPortInfo;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniBindCtcProfile;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniConfig;
import com.topvision.ems.epon.igmpconfig.domain.IgmpUniVlanTrans;
import com.topvision.ems.epon.igmpconfig.facade.OnuIgmpConfigFacade;
import com.topvision.ems.epon.igmpconfig.service.OltIgmpConfigService;
import com.topvision.ems.epon.igmpconfig.service.OnuIgmpConfigService;
import com.topvision.ems.epon.onu.dao.OnuDao;
import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.topology.event.OnuSynchronizedEvent;
import com.topvision.ems.epon.topology.event.OnuSynchronizedListener;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.utils.EponConstants;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.service.MessageService;

/**
 * @author flack
 * @created @2016-6-6-下午4:16:49
 *
 */
@Service("onuIgmpConfigService")
public class OnuIgmpConfigServiceImpl extends BaseService implements OnuIgmpConfigService, OnuSynchronizedListener {

    @Autowired
    private MessageService messageService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OnuIgmpConfigDao onuIgmpConfigDao;
    @Autowired
    private OltIgmpConfigService oltIgmpConfigService;
    @Autowired
    private OnuDao onuDao;

    public static final Integer ONU_SINGLE_TOPO = 1;

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(OnuSynchronizedListener.class, this);
    }

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(OnuSynchronizedListener.class, this);
    }

    @Override
    public void addBindCtcProfile(IgmpUniBindCtcProfile bindCtcProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(bindCtcProfile.getEntityId());
        getOnuIgmpFacade(snmpParam.getIpAddress()).createBindCtcProfile(snmpParam, bindCtcProfile);
        onuIgmpConfigDao.insertBindCtcProfile(bindCtcProfile);
    }

    @Override
    public List<IgmpUniBindCtcProfile> getBindCtcProfileList(Long entityId, Long uniIndex) {
        return onuIgmpConfigDao.queryBindCtcProfileList(entityId, uniIndex);
    }

    @Override
    public List<IgmpUniBindCtcProfile> getHasGroupBindProfile(Long entityId) {
        return onuIgmpConfigDao.queryHasGroupBindProfile(entityId);
    }

    @Override
    public void deleteBindCtcProfile(IgmpUniBindCtcProfile bindProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(bindProfile.getEntityId());
        getOnuIgmpFacade(snmpParam.getIpAddress()).destoryBindCtcProfile(snmpParam, bindProfile);
        onuIgmpConfigDao.deleteBindCtcProfile(bindProfile);
    }

    @Override
    public void refreshUniBindProfile(Long entityId) {
        oltIgmpConfigService.refreshUniBindCtcProfile(entityId);
    }

    @Override
    public void addIgmpOnuConfig(IgmpOnuConfig onuConfig) {
        onuIgmpConfigDao.insertIgmpOnuConfig(onuConfig);
    }

    @Override
    public IgmpOnuConfig getIgmpOnuConfig(Long entityId, Long onuIndex) {
        return onuIgmpConfigDao.queryIgmpOnuConfig(entityId, onuIndex);
    }

    @Override
    public void modifyIgmpOnuConfig(IgmpOnuConfig onuConfig) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuConfig.getEntityId());
        getOnuIgmpFacade(snmpParam.getIpAddress()).modifyIgmpOnuConfig(snmpParam, onuConfig);
        onuIgmpConfigDao.updateIgmpOnuConfig(onuConfig);
    }

    @Override
    public void refreshIgmpOnuConfig(Long entityId, Long onuIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        IgmpOnuConfig onuConfig = getOnuIgmpFacade(snmpParam.getIpAddress()).getIgmpOnuConfig(snmpParam, onuIndex);
        onuConfig.setEntityId(entityId);
        onuIgmpConfigDao.insertOrUpdateOnuConfig(onuConfig);
    }

    @Override
    public void addIgmpUniConfig(IgmpUniConfig uniConfig) {
        onuIgmpConfigDao.insertIgmpUniConfig(uniConfig);
    }

    @Override
    public IgmpUniConfig getIgmpUniConfig(Long entityId, Long uniIndex) {
        return onuIgmpConfigDao.queryIgmpUniConfig(entityId, uniIndex);
    }

    @Override
    public void modifyIgmpUniConfig(IgmpUniConfig uniConfig) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(uniConfig.getEntityId());
        getOnuIgmpFacade(snmpParam.getIpAddress()).modifyIgmpUniConfig(snmpParam, uniConfig);
        onuIgmpConfigDao.updateIgmpUniConfig(uniConfig);
    }

    @Override
    public void refreshIgmpUniConfig(Long entityId, Long uniIndex) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        IgmpUniConfig uniConfig = getOnuIgmpFacade(snmpParam.getIpAddress()).getIgmpUniConfig(snmpParam, uniIndex);
        uniConfig.setEntityId(entityId);
        onuIgmpConfigDao.insertOrUpdateUniConfig(uniConfig);
    }

    @Override
    public void addUniVlanTrans(IgmpUniVlanTrans vlanTrans) {
        //由于MIB索引原因,需要带上端口类型
        vlanTrans.setPortType(IgmpConstants.IGMP_PORTTYPE_UNI);
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanTrans.getEntityId());
        getOnuIgmpFacade(snmpParam.getIpAddress()).createUniVlanTrans(snmpParam, vlanTrans);
        onuIgmpConfigDao.insertUniVlanTrans(vlanTrans);
    }

    @Override
    public List<IgmpUniVlanTrans> getUniVlanTransList(Long entityId, Long uniIndex) {
        return onuIgmpConfigDao.queryUniVlanTransList(entityId, uniIndex);
    }

    @Override
    public void deleteUniVlanTrans(IgmpUniVlanTrans vlanTrans) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(vlanTrans.getEntityId());
        getOnuIgmpFacade(snmpParam.getIpAddress()).destoryUniVlanTrans(snmpParam, vlanTrans);
        onuIgmpConfigDao.deleteUniVlanTrans(vlanTrans);
    }

    @Override
    public void refreshUniVlanTrans(Long entityId) {
        oltIgmpConfigService.refreshAllUniVlanTransList(entityId);
    }

    @Override
    public List<IgmpPortInfo> getUniPortList(Long onuId) {
        return onuIgmpConfigDao.queryUniPortList(onuId);
    }

    @Override
    public void insertEntityStates(OnuSynchronizedEvent event) {
        //处理ONU IGMP业务 考虑刷新单个ONU的情况
        Long entityId = event.getEntityId();
        List<Long> onuIndexlList = event.getOnuIndexList();
        if (onuIndexlList.size() == ONU_SINGLE_TOPO) {
            Long onuIndex = onuIndexlList.get(0);
            OltOnuAttribute onuAttr = onuDao.getOnuAttributeByIndex(entityId, onuIndexlList.get(0));
            if (EponConstants.EPON_ONU.equals(onuAttr.getOnuEorG())) {
                //刷新单个ONU的IGMP配置
                try {
                    refreshIgmpOnuConfig(entityId, onuIndex);
                    logger.info("refreshIgmpOnuConfig finish");
                } catch (Exception e) {
                    logger.error("refreshIgmpOnuConfig wrong", e);
                }
                //刷新ONU所有UNI端口的IGMP配置,目前不支持根据ONUINDEX获取所有UNI IGMP配置,所有刷新整个设备的UNI配置
                try {
                    oltIgmpConfigService.refreshAllUniConfigList(entityId);
                    logger.info("refreshAllUniConfig finish");
                } catch (Exception e) {
                    logger.error("refreshAllUniConfig wrong", e);
                }
                //刷新所有UNI口VLAN转化配置
                try {
                    refreshUniVlanTrans(entityId);
                    logger.info("refreshUniVlanTrans finish");
                } catch (Exception e) {
                    logger.error("refreshUniVlanTrans wrong", e);
                }
                //刷新所有UNI口绑定模板配置
                try {
                    refreshUniBindProfile(entityId);
                    logger.info("refreshUniBindProfile finish");
                } catch (Exception e) {
                    logger.error("refreshUniBindProfile wrong", e);
                }
            }
        }
    }

    /**
     * 获取ONU IGMP配置Facade
     * @param ip
     * @return
     */
    private OnuIgmpConfigFacade getOnuIgmpFacade(String ip) {
        return facadeFactory.getFacade(ip, OnuIgmpConfigFacade.class);
    }
}
