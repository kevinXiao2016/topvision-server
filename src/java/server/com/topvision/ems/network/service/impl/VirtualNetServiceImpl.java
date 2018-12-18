/***********************************************************************
 * $Id: VirtualNetServiceImpl.java,v1.0 2012-2-23 上午11:37:30 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityAddressDao;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.VirtualNetDao;
import com.topvision.ems.network.domain.ProductInVirtual;
import com.topvision.ems.network.domain.ProductSnmpParam;
import com.topvision.ems.network.domain.VirtualNetAttribute;
import com.topvision.ems.network.domain.VirtualProduct;
import com.topvision.ems.network.message.VirtualNetEvent;
import com.topvision.ems.network.message.VirtualNetListener;
import com.topvision.ems.network.service.VirtualNetService;
import com.topvision.exception.service.ExistEntityException;
import com.topvision.exception.service.ExistEntityInVirtualNetException;
import com.topvision.framework.service.BaseService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.message.service.MessageService;

/**
 * @author zhanglongyang
 * @created @2012-2-23-上午11:37:30
 * 
 */
@Service("virtualNetService")
public class VirtualNetServiceImpl extends BaseService implements VirtualNetService {
    @Autowired
    private VirtualNetDao virtualNetDao;
    @Autowired
    private EntityAddressDao entityAddressDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EntityDao entityDao;
    private byte snmpRetry = 0;
    private int snmpPort = 161;
    private long snmpTimeout = 1000;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.VirtualNetService#createEntity(com.topvision.ems.network
     * .domain.VirtualNetEntityRela)
     */
    @Override
    public void createProduct(ProductInVirtual productInVirtual, ProductSnmpParam productSnmpParam) {
        if (entityAddressDao.selectByAddress(productInVirtual.getProductIp()) != null) {
            throw new ExistEntityException("Exist Entity:" + productInVirtual.getProductIp());
        }
        String virtualNetName = virtualNetDao.selectByAddress(productInVirtual.getProductIp());
        if (virtualNetName != null) {
            throw new ExistEntityInVirtualNetException(virtualNetName);
        }
        VirtualNetEvent virtualNetEvent = new VirtualNetEvent(this);
        virtualNetEvent.setActionName("productAddedInVirtualNet");
        virtualNetEvent.setListener(VirtualNetListener.class);
        virtualNetEvent.setVirtualNetEntityIp(productSnmpParam.getIpAddress());
        virtualNetEvent.setProductInVirtual(productInVirtual);
        virtualNetEvent.setProductSnmpParam(productSnmpParam);
        messageService.addMessage(virtualNetEvent);
    }

    @Override
    public void relateProductToVirtualNet(Long productId, Long virtualNetId) {
        virtualNetDao.relateProductToVirtualNet(productId, virtualNetId);
    }

    @Override
    public void deleteProduct(Long productId, Long productType) {
        virtualNetDao.deleteProduct(productId, productType);
    }

    @Override
    public void removeProductVirtualNetRelation(Long productId, Long productType, Long virtualNetId) {
        virtualNetDao.removeProductVirtualNetRelation(productId, productType, virtualNetId);
    }

    @Override
    public Long relateProductToTopoGraph(Long productId, Entity entity) {
        // 新增entity记录
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        entity.setCreateTime(ts);
        entity.setModifyTime(ts);

        // 设置SNMP参数
        SnmpParam sp = entity.getParam();
        sp.setRetry(snmpRetry);
        sp.setPort(snmpPort);
        sp.setTimeout(snmpTimeout);

        Long entityId = virtualNetDao.insertVirtualProductToEntity(entity);
        // TODO 建立设备与拓扑图关系
        virtualNetDao.insertEntityProductRelation(productId, entityId, entity.getTypeId());

        return entityId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.VirtualNetService#createVirtualNet(com.topvision.ems.network
     * .domain.VirtualNetAttribute)
     */
    @Override
    public Long createVirtualNet(VirtualNetAttribute virtualNetAttribute) {
        return virtualNetDao.createVirtualNet(virtualNetAttribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.VirtualNetService#deleteVirtualNet(java.lang.Long)
     */
    @Override
    public void deleteVirtualNet(Long virtualNetId) {
        virtualNetDao.deleteVirtualNet(virtualNetId);
    }

    @Override
    public void renameVirtualNet(Long virtualNetId, String virtualNetName) {
        virtualNetDao.renameVirtualNet(virtualNetId, virtualNetName);
    }

    @Override
    public void modifyVirtualNetAttribute(VirtualNetAttribute virtualNetAttribute) {
        virtualNetDao.modifyVirtualNetAttribute(virtualNetAttribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.VirtualNetService#getAllProductsInVirtualNet(java.lang.
     * Long)
     */
    @Override
    public List<VirtualProduct> getAllProductsInVirtualNet(Long virtualNetId, Long start, Long limit) {
        return virtualNetDao.getAllProductsInVirtualNet(virtualNetId, start, limit);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.VirtualNetService#getAllProductsInVirtualNetCount(java.
     * lang.Long)
     */
    @Override
    public Long getAllProductsInVirtualNetCount(Long virtualNetId) {
        return virtualNetDao.getAllProductsInVirtualNetCount(virtualNetId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.VirtualNetService#getProductSnmpParamByProductId(java.lang
     * .Long)
     */
    @Override
    public ProductSnmpParam getProductSnmpParamByProductId(Long productId, Long productType) {
        return virtualNetDao.getProductSnmpParamByProductId(productId, productType);
    }

    /*
     * 
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.VirtualNetService#saveVirtualNetCoordinate(java.util.List)
     */
    @Override
    public void saveVirtualNetCoordinate(List<VirtualNetAttribute> vnList) {
        // 由于在拓扑图上虚拟网络数量有限，所以不采用缓存机制实现，仅通过批量更新的方式。
        virtualNetDao.saveVirtualNetCoordinate(vnList);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.VirtualNetService#isProductExistsInTopo(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public boolean isProductExistsInTopo(Long productId, Long productType, Long folderId) {
        return virtualNetDao.isProductExistsInTopo(productId, productType, folderId);
    }

    /*
     * 
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.VirtualNetService#renameProductInVirtualNet(java.lang.Long,
     * java.lang.Long, java.lang.Long, java.lang.String)
     */
    @Override
    public void renameProductInVirtualNet(Long virtualNetId, Long productId, Long productType, String productName) {
        Long entityId = virtualNetDao.getEntityIdByProductIdAndType(productId, productType);
        if (entityId != null) {
            // 已添加到拓扑图上时，需要更新Entity中的别名。
            entityDao.renameEntity(entityId, productName + getString("virtualNet.topGraph.shortcut"));
        }
        // 更新虚拟网络中该设备的别名
        virtualNetDao.renameProductInVirtualNet(virtualNetId, productId, productType, productName);
    }

    /**
     * 国际化
     * 
     * @param key
     *            key
     * @return String
     */
    protected String getString(String key) {
        try {
            return ResourceManager.getResourceManager("com.topvision.ems.network.resources").getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    public void setVirtualNetDao(VirtualNetDao virtualNetDao) {
        this.virtualNetDao = virtualNetDao;
    }

    /**
     * @return the virtualNetDao
     */
    public VirtualNetDao getVirtualNetDao() {
        return virtualNetDao;
    }

    /**
     * @return the messageService
     */
    public MessageService getMessageService() {
        return messageService;
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setSnmpRetry(byte snmpRetry) {
        this.snmpRetry = snmpRetry;
    }

    public void setSnmpPort(int snmpPort) {
        this.snmpPort = snmpPort;
    }

    public void setSnmpTimeout(long snmpTimeout) {
        this.snmpTimeout = snmpTimeout;
    }

    /**
     * @return the entityAddressDao
     */
    public EntityAddressDao getEntityAddressDao() {
        return entityAddressDao;
    }

    /**
     * @param entityAddressDao
     *            the entityAddressDao to set
     */
    public void setEntityAddressDao(EntityAddressDao entityAddressDao) {
        this.entityAddressDao = entityAddressDao;
    }

    /**
     * @return the entityDao
     */
    public EntityDao getEntityDao() {
        return entityDao;
    }

    /**
     * @param entityDao
     *            the entityDao to set
     */
    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.VirtualNetService#removeProductFromTopoGraph(java.lang.
     * Long)
     */
    @Override
    public void removeProductFromTopoGraph(Long entityId) {
        entityDao.removeProductFromTopoGraph(entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.service.VirtualNetService#removeProductFromFolder(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void removeProductFromFolder(Long entityId, Long folderId) {
        virtualNetDao.removeProductFromFolder(entityId, folderId);
        if (!virtualNetDao.isProductExistsInFolder(entityId)) {
            entityDao.removeProductFromTopoGraph(entityId);
        }
    }

    @Override
    public VirtualNetAttribute getVirtualNetAttributeByVirtualId(Long cmcId) {
        Long virtualNetId = virtualNetDao.getVirtualNetIdByCmcId(cmcId);

        return virtualNetDao.getVirtualNetByVirtualNetId(virtualNetId);
    }

}
