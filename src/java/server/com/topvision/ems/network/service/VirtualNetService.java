/***********************************************************************
 * $Id: VirtualNetService.java,v1.0 2012-2-23 上午11:36:49 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.service;

import java.util.List;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.ProductInVirtual;
import com.topvision.ems.network.domain.ProductSnmpParam;
import com.topvision.ems.network.domain.VirtualNetAttribute;
import com.topvision.ems.network.domain.VirtualProduct;
import com.topvision.framework.service.Service;

/**
 * @author zhanglongyang
 * @created @2012-2-23-上午11:36:49
 * 
 */
public interface VirtualNetService extends Service {

    /**
     * 在虚拟网络中新建设备
     * 
     * @param productInVirtual
     * @param productSnmpParam
     */
    void createProduct(ProductInVirtual productInVirtual, ProductSnmpParam productSnmpParam);

    /**
     * 建立设备与虚拟网络关联关系
     * 
     * @param productId
     * @param virtualNetId
     */
    void relateProductToVirtualNet(Long productId, Long virtualNetId);

    /**
     * 在虚拟网络中删除设备
     * 
     * @param productId
     * @param productType
     */
    void deleteProduct(Long productId, Long productType);

    /**
     * 解除设备与虚拟网络关联关系
     * 
     * @param productId
     * @param productType
     * @param virtualNetId
     */
    void removeProductVirtualNetRelation(Long productId, Long productType, Long virtualNetId);

    /**
     * 将设备添加到拓扑图上
     * 
     * @param productId
     * @param entity
     */
    Long relateProductToTopoGraph(Long productId, Entity entity);

    /**
     * 将设备从拓扑图上删除
     * 
     * @param entityId
     * 
     */
    void removeProductFromTopoGraph(Long entityId);

    /**
     * 将设备从拓扑图上某个地域删除
     * 
     * @param entityId
     * @param folderId
     */
    void removeProductFromFolder(Long entityId, Long folderId);

    /**
     * 新建虚拟网络
     * 
     * @return
     */
    Long createVirtualNet(VirtualNetAttribute virtualNetAttribute);

    /**
     * 删除虚拟网络
     * 
     * @return
     */
    void deleteVirtualNet(Long virtualNetId);

    /**
     * 重命名虚拟网络
     * 
     * @param virtualNetId
     * @param virtualNetName
     */
    void renameVirtualNet(Long virtualNetId, String virtualNetName);

    /**
     * 修改虚拟网络属性
     * 
     * @param virtualNetAttribute
     */
    void modifyVirtualNetAttribute(VirtualNetAttribute virtualNetAttribute);

    /**
     * 获取虚拟网络中所有设备
     * 
     * @return
     */
    List<VirtualProduct> getAllProductsInVirtualNet(Long virtualNetId, Long start, Long limit);

    /**
     * 获取虚拟网络中设备snmp参数
     * 
     * @return
     */
    ProductSnmpParam getProductSnmpParamByProductId(Long productId, Long productType);

    /**
     * 获取虚拟网络中所有设备个数
     * 
     * @return
     */
    Long getAllProductsInVirtualNetCount(Long virtualNetId);

    /**
     * 保存虚拟网络在拓扑图位置信息
     * 
     * @param vnList
     */
    void saveVirtualNetCoordinate(List<VirtualNetAttribute> vnList);

    /**
     * 判断设备是否在拓扑图上存在
     * 
     * @param productId
     * @param productType
     * @return boolean
     */
    boolean isProductExistsInTopo(Long productId, Long productType, Long folderId);

    /**
     * 在虚拟网络中重命名设备
     * 
     * @param virtualNetId
     * @param productId
     * @param productType
     * @param productName
     */
    void renameProductInVirtualNet(Long virtualNetId, Long productId, Long productType, String productName);

    /**
     * 根据CMCID获取虚拟网络的网络信息
     */
    VirtualNetAttribute getVirtualNetAttributeByVirtualId(Long cmcId);
}
