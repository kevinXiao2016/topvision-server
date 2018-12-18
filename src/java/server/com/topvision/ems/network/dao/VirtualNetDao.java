/***********************************************************************
 * $Id: VirtualNetDao.java,v1.0 2012-2-23 上午11:30:50 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao;

import java.util.List;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.ProductSnmpParam;
import com.topvision.ems.network.domain.VirtualNetAttribute;
import com.topvision.ems.network.domain.VirtualProduct;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author zhanglongyang
 * @created @2012-2-23-上午11:30:50
 * 
 */
public interface VirtualNetDao extends BaseEntityDao<Object> {
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
     * 获取虚拟网络中所有设备个数
     * 
     * @return
     */
    Long getAllProductsInVirtualNetCount(Long virtualNetId);

    /**
     * 获取拓扑文件夹中所有虚拟网络
     * 
     * @return
     */
    List<VirtualNetAttribute> getVirtualNetByFolderId(Long folderId);

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
     * 将设备从拓扑图上的某个地域删除
     * 
     * @param entityId
     * @param folderId
     */
    void removeProductFromFolder(Long entityId, Long folderId);

    /**
     * 建立product与entity关系
     * 
     * @param productId
     */
    void insertEntityProductRelation(Long productId, Long entityId, Long productType);

    /**
     * 插入设备与虚拟网络关联管理
     * 
     * @param rela
     */
    void insertVirtualProductRelation(VirtualProduct rela);

    /**
     * 插入虚拟网络设备到拓扑图
     * 
     * @param entity
     */
    Long insertVirtualProductToEntity(Entity entity);

    /**
     * 获取虚拟网络中设备snmp参数
     * 
     * @return
     */
    ProductSnmpParam getProductSnmpParamByProductId(Long productId, Long productType);

    /**
     * 插入设备SNMP信息表
     * 
     * @param productSnmpParam
     */
    void insertProSnmpParam(ProductSnmpParam productSnmpParam);

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
     * 获取entityId
     * 
     * @param productId
     * @param productType
     * @return Long
     */
    Long getEntityIdByProductIdAndType(Long productId, Long productType);

    /**
     * 查询IP地址是否存在
     * 
     * @param ipAddress
     * @return
     */
    String selectByAddress(String ipAddress);

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
     * 判断拓扑图上是否还存在该设备(包括所有地域)
     * 
     * @param entityId
     * @return
     */
    boolean isProductExistsInFolder(Long entityId);

    /**
     * 根据CMCid找到所在虚拟网络的id
     */
    Long getVirtualNetIdByCmcId(Long cmcId);

    /**
     * 根据虚拟网络的id找到该虚拟网络的信息
     */
    VirtualNetAttribute getVirtualNetByVirtualNetId(Long virtualNetId);

}
