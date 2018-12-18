/***********************************************************************
 * 
 * $Id: NbiServiceImpl.java,v1.0 2016-3-16 上午10:24:04 $
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.nbi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.nbi.dao.NbiDao;
import com.topvision.ems.nbi.domain.NbiBaseConfig;
import com.topvision.ems.nbi.domain.NbiTarget;
import com.topvision.ems.nbi.domain.NbiTargetGroup;
import com.topvision.ems.nbi.service.NbiCallBackService;
import com.topvision.ems.nbi.service.NbiConnectionService;
import com.topvision.ems.nbi.service.NbiService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.performance.nbi.api.NbiFtpConfig;
import com.topvision.performance.nbi.api.NbiMultiPeriod;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.service.SystemPreferencesService;

/**
 * @author lizongtian
 * @created @2016-3-16-上午10:24:04
 *
 */
@Service("nbiService")
public class NbiServiceImpl extends BaseService implements NbiService, EntityListener {
    @Autowired
    private SystemPreferencesService systemPreferencesService;
    @Autowired
    private NbiConnectionService nbiConnectionService;
    @Autowired
    private NbiDao nbiDao;
    @Autowired
    private NbiCallBackService nbiCallBackService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityTypeService entityTypeService;

    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(EntityListener.class, this);
    }

    @Override
    public void destroy() {
        if (logger.isInfoEnabled()) {
            logger.info("Invoke " + getClass() + " destroy method...");
        }
        messageService.removeListener(EntityListener.class, this);
    }

    @Override
    public NbiBaseConfig getNbiBaseConfig() {
        NbiBaseConfig nbiBaseConfig = nbiDao.getNbiBaseConfig();
        if (nbiBaseConfig == null) {
            nbiBaseConfig = new NbiBaseConfig();
        }
        return nbiBaseConfig;
    }

    @Override
    public NbiMultiPeriod getNbiMultiPeriod() {
        return nbiDao.getNbiMultiPeriod();
    }

    @Override
    public void updateNbiBaseConfig(NbiBaseConfig baseConfig) {
        nbiDao.updateNbiBaseConfig(baseConfig);
        nbiConnectionService.retryCheckNbi();
        NbiFtpConfig nbiFtpConfig = nbiCallBackService.loadNbiFtpConfigParam();
        nbiConnectionService.dynamicUpdateFtpConfig(nbiFtpConfig);
    }

    @Override
    public void updateNbiMultiPeriod(NbiMultiPeriod nbiMultiPeriod) {
        nbiDao.updateNbiMultiPeriod(nbiMultiPeriod);
        nbiConnectionService.dynamicUpdateGroupPeroid();
    }

    @Override
    public void updateNbiTarget(List<NbiTarget> targets) {
        nbiDao.updateNbiTarget(targets);
        nbiConnectionService.dynamicUpdateGroupIndexs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.nbi.service.NbiService#getNbiTargetGroup()
     */
    @Override
    public List<NbiTargetGroup> getNbiTargetGroup(Boolean oltModule, Boolean onuModule, Boolean cmcModule) {
        List<NbiTargetGroup> groups = new ArrayList<NbiTargetGroup>();
        if (oltModule) {
            groups.addAll(nbiDao.getNbiTargetGroup("olt"));
        }
        if (onuModule) {
            groups.addAll(nbiDao.getNbiTargetGroup("onu"));
        }
        if (cmcModule) {
            groups.addAll(nbiDao.getNbiTargetGroup("cmc"));
        }
        for (NbiTargetGroup group : groups) {
            List<NbiTarget> targets = nbiDao.getNbiTargetListByGroup(group.getGroupId());
            group.setNbiTargetList(targets);
        }
        return groups;
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.EntityListener#entityAdded(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent event) {
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.EntityListener#entityDiscovered(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.EntityListener#attributeChanged(long, java.lang.String[], java.lang.String[])
     */
    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.EntityListener#entityChanged(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void entityChanged(EntityEvent event) {
    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.EntityListener#entityRemoved(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void entityRemoved(EntityEvent event) {
        Entity entity = event.getEntity();
        long entityId = entity.getEntityId();
        if (entityTypeService.isOlt(entity.getTypeId())) {
            nbiConnectionService.dynamicRemoveEntity(entity.getIp());
        } else {
            nbiConnectionService.dynamicRemoveEntity(entityId);
        }

    }

    /* (non-Javadoc)
     * @see com.topvision.platform.message.event.EntityListener#managerChanged(com.topvision.platform.message.event.EntityEvent)
     */
    @Override
    public void managerChanged(EntityEvent event) {
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }

}
