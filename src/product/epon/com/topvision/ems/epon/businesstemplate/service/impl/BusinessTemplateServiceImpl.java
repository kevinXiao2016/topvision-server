/***********************************************************************
 * $Id: BusinessTemplateServiceImpl.java,v1.0 2015-12-8 下午2:02:16 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.businesstemplate.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.businesstemplate.dao.BusinessTemplateDao;
import com.topvision.ems.epon.businesstemplate.domain.OnuCapability;
import com.topvision.ems.epon.businesstemplate.domain.OnuIgmpProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuPortVlanProfile;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvIgmpVlanTrans;
import com.topvision.ems.epon.businesstemplate.domain.OnuSrvProfile;
import com.topvision.ems.epon.businesstemplate.facade.BusinessTemplateFacade;
import com.topvision.ems.epon.businesstemplate.service.BusinessTemplateService;
import com.topvision.ems.epon.igmp.domain.IgmpMcOnuVlanTransTable;
import com.topvision.ems.epon.igmp.domain.IgmpMcParamMgmtObjects;
import com.topvision.ems.epon.igmp.service.OltIgmpService;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.facade.FacadeFactory;
import com.topvision.platform.message.event.SynchronizedEvent;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author lizongtian
 * @created @2015-12-8-下午2:02:16
 *
 */
@Service("businessTemplateService")
public class BusinessTemplateServiceImpl extends BaseService implements BusinessTemplateService, SynchronizedListener {
    @Autowired
    private BusinessTemplateDao businessTemplateDao;
    @Autowired
    private EntityService entityService;
    @Autowired
    private OltIgmpService oltIgmpService;
    @Autowired
    private FacadeFactory facadeFactory;
    @Autowired
    private MessageService messageService;

    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
        messageService.removeListener(SynchronizedListener.class, this);
    }

    @Override
    @PostConstruct
    public void initialize() {
        super.initialize();
        messageService.addListener(SynchronizedListener.class, this);
    }

    @Override
    public List<OnuSrvProfile> getOnuSrvProfiles(Long entityId) {
        return businessTemplateDao.getOnuSrvProfiles(entityId);
    }

    @Override
    public List<OnuIgmpProfile> getOnuIgmpProfiles(Long entityId, Integer profileId) {
        return businessTemplateDao.getOnuIgmpProfiles(entityId, profileId);
    }

    @Override
    public List<OnuPortVlanProfile> getOnuPortVlanProfiles(Long entityId, Integer profileId) {
        return businessTemplateDao.getOnuPortVlanProfiles(entityId, profileId);
    }

    @Override
    public void deleteOnuSrvProfile(OnuSrvProfile srvProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(srvProfile.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).deleteOnuSrvProfile(snmpParam, srvProfile);
        businessTemplateDao.deleteOnuSrvProfile(srvProfile);
    }

    @Override
    public void deleteOnuIgmpProfile(OnuIgmpProfile igmpProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(igmpProfile.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).deleteOnuIgmpProfile(snmpParam, igmpProfile);
        businessTemplateDao.deleteOnuIgmpProfile(igmpProfile);
    }

    @Override
    public void deleteOnuPortVlanProfile(OnuPortVlanProfile portVlanProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(portVlanProfile.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).deleteOnuPortVlanProfile(snmpParam, portVlanProfile);
        businessTemplateDao.deleteOnuPortVlanProfile(portVlanProfile);
    }

    @Override
    public void updateOnuSrvProfile(OnuSrvProfile srvProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(srvProfile.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).modifyOnuSrvProfile(snmpParam, srvProfile);
        businessTemplateDao.updateOnuSrvProfile(srvProfile);
    }

    @Override
    public void updateOnuIgmpProfile(OnuIgmpProfile igmpProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(igmpProfile.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).modifyOnuIgmpProfile(snmpParam, igmpProfile);
        refreshOnuSrvIgmpVlanTrans(igmpProfile.getEntityId());
        businessTemplateDao.updateOnuIgmpProfile(igmpProfile);
    }

    @Override
    public void updateOnuPortVlanProfile(OnuPortVlanProfile portVlanProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(portVlanProfile.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).modifyOnuPortVlanProfile(snmpParam, portVlanProfile);
        businessTemplateDao.updateOnuPortVlanProfile(portVlanProfile);
    }

    @Override
    public void addOnuSrvProfile(OnuSrvProfile srvProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(srvProfile.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).addOnuSrvProfile(snmpParam, srvProfile);
        businessTemplateDao.addOnuSrvProfile(srvProfile);
    }

    @Override
    public void addOnuIgmpProfile(OnuIgmpProfile igmpProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(igmpProfile.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).addOnuIgmpProfile(snmpParam, igmpProfile);
        businessTemplateDao.addOnuIgmpProfile(igmpProfile);
    }

    @Override
    public void addOnuPortVlanProfile(OnuPortVlanProfile portVlanProfile) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(portVlanProfile.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).addOnuPortVlanProfile(snmpParam, portVlanProfile);
        businessTemplateDao.addOnuPortVlanProfile(portVlanProfile);
    }

    @Override
    public void refreshOnuSrvProfile(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        try {
            List<OnuSrvProfile> srvProfiles = getBusinessTemplateFacade(snmpParam.getIpAddress()).refreshOnuSrvProfile(
                    snmpParam);
            businessTemplateDao.batchInsertOnuSrvProfile(entityId, srvProfiles);
        } catch (Exception e) {
            logger.info("refreshOnuSrvProfile error {}", e.getMessage());
            //需要把异常抛出,否则前台会认为是成功的
            throw new RuntimeException(e.getMessage(), e.getCause());

        }
    }

    @Override
    public void refreshOnuPortVlanProfile(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        try {
            List<OnuPortVlanProfile> portVlanProfiles = getBusinessTemplateFacade(snmpParam.getIpAddress())
                    .refreshOnuPortVlanProfile(snmpParam);
            businessTemplateDao.batchInsertOnuPortVlanProfile(entityId, portVlanProfiles);
        } catch (Exception e) {
            logger.info("refreshOnuPortVlanProfile error {}", e.getMessage());
            //需要把异常抛出,否则前台会认为是成功的
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void refreshOnuIgmpProfile(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        try {
            List<OnuIgmpProfile> igmpProfiles = getBusinessTemplateFacade(snmpParam.getIpAddress())
                    .refreshOnuIgmpProfile(snmpParam);
            businessTemplateDao.batchInsertOnuIgmpProfile(entityId, igmpProfiles);
        } catch (Exception e) {
            logger.info("refreshOnuIgmpProfile error {}", e.getMessage());
            //需要把异常抛出,否则前台会认为是成功的
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void refreshOnuSrvIgmpVlanTrans(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        try {
            List<OnuSrvIgmpVlanTrans> igmpVlanTrans = getBusinessTemplateFacade(snmpParam.getIpAddress())
                    .refreshOnuSrvIgmpVlanTrans(snmpParam);
            businessTemplateDao.batchInsertOnuIgmpVlanTrans(entityId, igmpVlanTrans);
        } catch (Exception e) {
            logger.info("OnuSrvIgmpVlanTrans error {}", e.getMessage());
            // 需要把异常抛出,否则前台会认为是成功的
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    private BusinessTemplateFacade getBusinessTemplateFacade(String ip) {
        return facadeFactory.getFacade(ip, BusinessTemplateFacade.class);
    }

    @Override
    public void insertEntityStates(SynchronizedEvent event) {
        Long entityId = event.getEntityId();
        try {
            refreshOnuSrvProfile(entityId);
        } catch (Exception e) {
            logger.error("refreshOnuSrvProfile error {}", e.getMessage());
        }
        try {
            refreshOnuPortVlanProfile(entityId);
        } catch (Exception e) {
            logger.error("refreshOnuPortVlanProfile error {}", e.getMessage());
        }
        try {
            refreshOnuIgmpProfile(entityId);
        } catch (Exception e) {
            logger.error("refreshOnuIgmpProfile error {}", e.getMessage());
        }
        try {
            refreshOnuCapability(entityId);
        } catch (Exception e) {
            logger.error("refreshOnuCapability error {}", e.getMessage());
        }
        try {
            refreshOnuSrvIgmpVlanTrans(entityId);
        } catch (Exception e) {
            logger.error("refreshOnuSrvIgmpVlanTrans error {}", e.getMessage());
        }
    }

    @Override
    public void updateEntityStates(SynchronizedEvent event) {

    }

    @Override
    public OnuSrvProfile getOnuSrvProfile(Long entityId, Integer profileId) {
        return businessTemplateDao.getOnuSrvProfile(entityId, profileId);
    }

    @Override
    public OnuIgmpProfile getOnuIgmpProfile(Long entityId, Integer profileId, Integer igmpPortId) {
        return businessTemplateDao.getOnuIgmpProfile(entityId, profileId, igmpPortId);
    }

    @Override
    public OnuPortVlanProfile getOnuPortVlanProfile(Long entityId, Integer profileId, Integer srvPortId) {
        return businessTemplateDao.getOnuPortVlanProfile(entityId, profileId, srvPortId);
    }

    @Override
    public List<OnuCapability> getOnuCapabilitys(Long entityId) {
        return businessTemplateDao.getOnuCapabilitys(entityId);
    }

    @Override
    public void deleteOnuCapability(OnuCapability capability) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(capability.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).deleteOnuCapability(snmpParam, capability);
        businessTemplateDao.deleteOnuCapability(capability);
    }

    @Override
    public void addOnuCapability(OnuCapability capability) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(capability.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).addOnuCapability(snmpParam, capability);
        //防止Id重复，数据库不做记录,从设备获取数据
        //businessTemplateDao.addOnuCapability(capability);
        //由于ID处理的原因,在添加完成后刷新数据进行同步
        refreshOnuCapability(capability.getEntityId());
    }

    @Override
    public void refreshOnuCapability(Long entityId) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(entityId);
        try {
            List<OnuCapability> capabilitys = getBusinessTemplateFacade(snmpParam.getIpAddress()).refreshOnuCapability(
                    snmpParam);
            businessTemplateDao.batchInsertOnuCapability(entityId, capabilitys);
        } catch (Exception e) {
            logger.info("refreshOnuCapability error {}", e.getMessage());
            //需要把异常抛出,否则前台会认为是成功的
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Integer> getIgmpVlanList(Long entityId) {
        IgmpMcParamMgmtObjects igmpMcParamMgmt = oltIgmpService.getIgmpMaxGroupNum(entityId);
        List<Integer> mVlan = new ArrayList<Integer>();
        if (igmpMcParamMgmt.getTopMcMVlanList() != null && igmpMcParamMgmt.getTopMcMVlanList().size() > 0) {
            for (Integer mvid : igmpMcParamMgmt.getTopMcMVlanList()) {
                mVlan.add(mvid);
            }
        }
        return mVlan;
    }

    @Override
    public List<Integer> getIgmpVlanTransIds(Long entityId) {
        List<IgmpMcOnuVlanTransTable> transList = oltIgmpService.getIgmpVlanTrans(entityId);
        List<Integer> transIds = new ArrayList<Integer>();
        for (IgmpMcOnuVlanTransTable trans : transList) {
            transIds.add(trans.getTopMcOnuVlanTransIndex());
        }
        return transIds;
    }

    @Override
    public void unBindCapability(OnuSrvProfile srvProfile) {
        //进行业务模板能力集的解绑定
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(srvProfile.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).unBindCapability(snmpParam, srvProfile);
        //更新数据库
        businessTemplateDao.updateProfileBindCap(srvProfile);
    }

    @Override
    public void deleteOnuSrvIgmpVlanTrans(OnuSrvIgmpVlanTrans onuIgmpVlanTrans) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuIgmpVlanTrans.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).deleteOnuIgmpVlanTrans(snmpParam, onuIgmpVlanTrans);
        businessTemplateDao.deleteOnuSrvIgmpVlanTrans(onuIgmpVlanTrans);
    }

    @Override
    public void addOnuSrvIgmpVlanTrans(OnuSrvIgmpVlanTrans onuIgmpVlanTrans) {
        SnmpParam snmpParam = entityService.getSnmpParamByEntity(onuIgmpVlanTrans.getEntityId());
        this.getBusinessTemplateFacade(snmpParam.getIpAddress()).addOnuSrvIgmpVlanTrans(snmpParam, onuIgmpVlanTrans);
        businessTemplateDao.addOnuSrvIgmpVlanTrans(onuIgmpVlanTrans);
    }

    @Override
    public List<OnuSrvIgmpVlanTrans> loadOnuIgmpVlanTrans(Long entityId, Integer profileId, Integer portId) {
        return businessTemplateDao.loadOnuIgmpVlanTrans(entityId, profileId, portId);
    }

}
