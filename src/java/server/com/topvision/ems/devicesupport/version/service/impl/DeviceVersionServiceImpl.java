/***********************************************************************
 * $Id: DeviceVersionServiceImpl.java,v1.0 2017年10月11日 下午3:40:52 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.devicesupport.version.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.topvision.ems.devicesupport.version.DeviceVersionXmlParser;
import com.topvision.ems.devicesupport.version.service.DeviceVersionService;
import com.topvision.ems.devicesupport.version.util.DeviceFuctionSupport;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.event.SynchronizedListener;
import com.topvision.platform.message.service.MessageService;

/**
 * @author vanzand
 * @created @2017年10月11日-下午3:40:52
 *
 */
@Service("deviceVersionService")
public class DeviceVersionServiceImpl extends BaseService
        implements DeviceVersionService, BeanFactoryAware, EntityListener {

    @Autowired
    private EntityService entityService;
    @Autowired
    private EntityTypeService entityTypeService;
    @Autowired
    protected MessageService messageService;

    private BeanFactory beanFactory;

    private ConcurrentHashMap<Long, String> versionCache = new ConcurrentHashMap<Long, String>();

    private ConcurrentHashMap<Long, Map<String, JSONObject>> versionControlCache = new ConcurrentHashMap<Long, Map<String, JSONObject>>();

    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(EntityListener.class, this);
    }

    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(EntityListener.class, this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.devicesupport.version.service.DeviceVersionService#getEntityVersion(java.
     * lang.Long)
     */
    @Override
    public String getEntityVersion(Long entityId) {
        if (entityId == null) {
            return null;
        }
        if (!versionCache.containsKey(entityId)) {

            Entity entity = entityService.getEntity(entityId);

            DeviceVersionService service = null;
            if (entity != null && (entityTypeService.isOlt(entity.getTypeId()))) {
                // OLT 设备
                service = (DeviceVersionService) beanFactory.getBean("oltDeviceVersionService");
            } else if (entityTypeService.isCcmts(entity.getTypeId())) {
                // CCMTS
                service = (DeviceVersionService) beanFactory.getBean("ccmtsDeviceVersionService");
            } else {
                // TODO 扩展接口，预留给其它设备类型
            }
            if (service == null) {
                return null;
            }
            String entityVersion = service.getEntityVersion(entityId);
            if (entityVersion != null) {
                versionCache.put(entityId, entityVersion);
            }
        }

        return versionCache.get(entityId);

    }

    @Override
    public boolean isFunctionSupported(Long entityId, String functionName) {
        if (entityId == null) {
            return false;
        }
        // 获取对应设备的版本控制版本号信息
        Entity entity = entityService.getEntity(entityId);
        String versionName = getEntityVersion(entityId);
        return DeviceFuctionSupport.isSupportFunction(entity.getTypeId(), versionName, functionName);
    }

    @Override
    public Map<String, Boolean> isFunctionSupported(Long entityId, String[] functionNames) {
        Entity entity = entityService.getEntity(entityId);
        String versionName = getEntityVersion(entityId);

        Map<String, Boolean> result = new HashMap<String, Boolean>();
        boolean isSupport;
        for (int i = 0; i < functionNames.length; i++) {
            isSupport = DeviceFuctionSupport.isSupportFunction(entity.getTypeId(), versionName, functionNames[i]);
            result.put(functionNames[i], isSupport);
        }
        return result;
    }

    @Override
    public String getParamValue(Long entityId, String functionName, String paramName) {
        Entity entity = entityService.getEntity(entityId);
        String versionName = getEntityVersion(entityId);

        return DeviceFuctionSupport.getParamValue(entity.getTypeId(), versionName, functionName, paramName);
    }

    @Override
    public JSONObject getVersionControl(Long typeId, String version) {
        if (!versionControlCache.containsKey(typeId)) {
            versionControlCache.put(typeId, new HashMap<String, JSONObject>());
        }
        Map<String, JSONObject> versions = versionControlCache.get(typeId);
        if (!versions.containsKey(version)) {
            DeviceVersionXmlParser xmlParser = DeviceVersionXmlParser.getInstance();
            JSONObject versionControl = xmlParser.getVersionControl(typeId, version);
            versions.put(version, versionControl);
        }
        return versions.get(version);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void entityAdded(EntityEvent event) {
    }

    @Override
    public void entityDiscovered(EntityEvent event) {
    }

    @Override
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
        versionCache.remove(entityId);
        versionControlCache.remove(entityId);
    }

    @Override
    public void entityChanged(EntityEvent event) {
        Long entityId = event.getEntity().getEntityId();
        versionCache.remove(entityId);
        versionControlCache.remove(entityId);
    }

    @Override
    public void entityRemoved(EntityEvent event) {
        Long entityId = event.getEntity().getEntityId();
        versionCache.remove(entityId);
        versionControlCache.remove(entityId);
    }

    @Override
    public void managerChanged(EntityEvent event) {
        Long entityId = event.getEntity().getEntityId();
        versionCache.remove(entityId);
        versionControlCache.remove(entityId);
    }

    @Override
    public void entityReplaced(EntityEvent event) {
        Long entityId = event.getEntity().getEntityId();
        versionCache.remove(entityId);
        versionControlCache.remove(entityId);
    }

}
