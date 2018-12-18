/***********************************************************************
 * $Id: VirtualNetAction.java,v1.0 2012-2-23 上午11:33:01 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.ems.network.domain.ProductInVirtual;
import com.topvision.ems.network.domain.ProductSnmpParam;
import com.topvision.ems.network.domain.VirtualNetAttribute;
import com.topvision.ems.network.domain.VirtualProduct;
import com.topvision.ems.network.service.VirtualNetService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.snmp.SnmpParam;
import com.topvision.framework.web.struts2.BaseAction;

import net.sf.json.JSONObject;

/**
 * @author zhanglongyang
 * @created @2012-2-23-上午11:33:01
 * 
 */
@Controller("virtualNetAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VirtualNetAction extends BaseAction {
    private static final long serialVersionUID = -4120694541110786756L;
    @Autowired
    private VirtualNetService virtualNetService;
    @Autowired
    private EntityTypeService entityTypeService;
    private Long virtualNetEntityType;
    private Long virtualNetId;
    private String virtualNetName;
    private Long productId;
    private Long folderId;
    private Long entityId;
    private Long start;
    private Long limit;
    private Long typeId;
    private String virtualNetEntityName;// 别名
    private String virtualNetEntityIp;
    private String readCommunity;
    private VirtualNetAttribute virtualNetAttribute;
    private Long productCount;
    private List<VirtualProduct> virtualProductList = new ArrayList<VirtualProduct>();
    private Long productType;
    private String productName;
    private ProductInVirtual product;
    private ProductSnmpParam productSnmp;
    private List<Long> virtualNetIds;
    private List<Integer> x;
    private List<Integer> y;

    /**
     * 将设备添加到拓扑图上
     * 
     * @return
     * @throws Exception
     */
    public String relateProductToTopoGraph() throws Exception {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            Entity entity = new Entity();
            // 获取设备类型
            EntityType et = entityTypeService.getEntityType(typeId);
            if (et != null) {
                entity.setCorpId(entityTypeService.getCorpBySysObjId(et.getSysObjectID()));
                entity.setTypeName(et.getName());
                entity.setTypeId(et.getTypeId());
                entity.setIcon48(et.getIcon48());
            }
            entity.setFolderId(folderId);
            entity.setIp(virtualNetEntityIp);
            entity.setName(virtualNetEntityName);
            // 添加设备的子网信息,子网的ID
            entity.setVirtualNetworkStatus(virtualNetId.intValue());
            // 获取SnmpParam参数
            ProductSnmpParam productSnmpParam = virtualNetService.getProductSnmpParamByProductId(productId,
                    et.getTypeId());
            SnmpParam snmpParam = new SnmpParam();
            snmpParam.setIpAddress(productSnmpParam.getIpAddress());
            snmpParam.setCommunity(productSnmpParam.getReadCommunity());
            snmpParam.setWriteCommunity(productSnmpParam.getWriteCommunity());
            entity.setParam(snmpParam);
            Long newEntityId = virtualNetService.relateProductToTopoGraph(productId, entity);
            json.put("entityId", newEntityId);
            json.put("success", true);
        } catch (Exception ex) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 从拓扑图上删除设备
     * 
     * @return
     * @throws IOException
     */
    public String removeProductFromTopoGraph() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            virtualNetService.removeProductFromFolder(entityId, folderId);
            json.put("success", true);
        } catch (Exception ex) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 新建虚拟网络
     * 
     * @return
     * @throws IOException
     */
    public String createVirtualNet() throws IOException {
        virtualNetId = virtualNetService.createVirtualNet(virtualNetAttribute);
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            json.put("id", virtualNetId);
            json.put("nodeId", virtualNetId);
            // 获取新建子网属性
            json.put("text", virtualNetAttribute.getVirtualName());
            json.put("x", virtualNetAttribute.getX());
            json.put("y", virtualNetAttribute.getY());
            // json.put("icon", virtualNetAttribute.getIcon());
            json.put("width", virtualNetAttribute.getWidth());
            json.put("height", virtualNetAttribute.getHeight());
            json.put("fixed", virtualNetAttribute.getFixed());
            json.put("visiable", virtualNetAttribute.getVisiable());
            json.put("name", virtualNetAttribute.getVirtualName());
            json.put("objType", 3);
            json.put("type", virtualNetAttribute.getVirtualType());
            json.put("success", true);
        } catch (Exception ex) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 删除虚拟网络
     * 
     * @return
     * @throws IOException
     */
    public String deleteVirtualNet() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        productCount = virtualNetService.getAllProductsInVirtualNetCount(virtualNetId);
        if (productCount > 0) {
            json.put("success", true);
            json.put("hasProduct", true);
        } else {
            json.put("hasProduct", false);
            try {
                virtualNetService.deleteVirtualNet(virtualNetId);
                json.put("success", true);
            } catch (Exception ex) {
                json.put("success", false);
            }
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 重命名虚拟网络
     * 
     * @return
     * @throws IOException
     */
    public String renameVirtualNet() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            virtualNetService.renameVirtualNet(virtualNetId, virtualNetName);
            json.put("success", true);
        } catch (Exception ex) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 修改虚拟网络属性
     * 
     * @return
     * @throws IOException
     */
    public String modifyVirtualNetAttribute() throws IOException {
        VirtualNetAttribute virtualNetAttribute = new VirtualNetAttribute();
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            virtualNetService.modifyVirtualNetAttribute(virtualNetAttribute);
            json.put("success", true);
        } catch (Exception ex) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 获取虚拟网络中所有设备
     * 
     * @return
     * @throws IOException
     */
    public String getAllProductsInVirtualNet() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            productCount = virtualNetService.getAllProductsInVirtualNetCount(virtualNetId);
            virtualProductList = virtualNetService.getAllProductsInVirtualNet(virtualNetId, start, limit);
            json.put("data", virtualProductList);
            json.put("rowCount", productCount);
            json.put("success", true);
        } catch (Exception ex) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 在虚拟网络中创建设备
     * 
     * @return
     * @throws IOException
     */
    public String createProduct() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            virtualNetService.createProduct(product, productSnmp);
            json.put("exist", false);
            // TODO 获取新建子网属性
            json.put("success", true);
        } catch (Exception ex) {
            json.put("exist", true);
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 在虚拟网络中重命名设备
     * 
     * @return
     */
    public String renameProductInVirtualNet() {
        virtualNetService.renameProductInVirtualNet(virtualNetId, productId, productType, productName);
        return NONE;
    }

    /**
     * 建立设备与虚拟网络关联关系
     * 
     * @return
     * @throws IOException
     */
    public String relateProductToVirtualNet() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            virtualNetService.relateProductToVirtualNet(productId, virtualNetId);
            json.put("success", true);
        } catch (Exception ex) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 在虚拟网络中删除设备
     * 
     * @return
     * @throws IOException
     */
    public String deleteProduct() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            virtualNetService.deleteProduct(productId, productType);
            json.put("success", true);
        } catch (Exception ex) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 解除设备与虚拟网络关联关系
     * 
     * @return
     * @throws IOException
     */
    public String removeProductVirtualNetRelation() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            virtualNetService.removeProductVirtualNetRelation(productId, productType, virtualNetId);
            json.put("success", true);
        } catch (Exception ex) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 保存虚拟网络在拓扑图位置信息
     * 
     * @return
     * @throws IOException
     */
    public String saveVirtualNetCoordinate() throws IOException {
        List<VirtualNetAttribute> vnList = new ArrayList<VirtualNetAttribute>();
        Map<String, Object> json = new HashMap<String, Object>();
        VirtualNetAttribute vn;
        for (int i = 0; i < virtualNetIds.size(); i++) {
            vn = new VirtualNetAttribute();
            vn.setVirtualNetId(virtualNetIds.get(i));
            vn.setFolderId(folderId);
            vn.setX(x.get(i));
            vn.setY(y.get(i));
            vnList.add(vn);
        }
        try {
            virtualNetService.saveVirtualNetCoordinate(vnList);
            json.put("success", true);
        } catch (Exception ex) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    /**
     * 判断设备是否在拓扑图上
     * 
     * @return
     * @throws IOException
     */
    public String isProductExistsInTopo() throws IOException {
        Map<String, Object> json = new HashMap<String, Object>();
        try {
            boolean isProductExistsInTopo = virtualNetService.isProductExistsInTopo(productId, productType, folderId);
            json.put("isProductExistsInTopo", isProductExistsInTopo);
            json.put("success", true);
        } catch (Exception ex) {
            json.put("success", false);
        }
        writeDataToAjax(JSONObject.fromObject(json));
        return NONE;
    }

    public EntityTypeService getEntityTypeService() {
        return entityTypeService;
    }

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    public Long getVirtualNetEntityType() {
        return virtualNetEntityType;
    }

    public void setVirtualNetEntityType(Long virtualNetEntityType) {
        this.virtualNetEntityType = virtualNetEntityType;
    }

    public Long getVirtualNetId() {
        return virtualNetId;
    }

    public void setVirtualNetId(Long virtualNetId) {
        this.virtualNetId = virtualNetId;
    }

    public String getVirtualNetEntityName() {
        return virtualNetEntityName;
    }

    public void setVirtualNetEntityName(String virtualNetEntityName) {
        this.virtualNetEntityName = virtualNetEntityName;
    }

    public String getVirtualNetEntityIp() {
        return virtualNetEntityIp;
    }

    public void setVirtualNetEntityIp(String virtualNetEntityIp) {
        this.virtualNetEntityIp = virtualNetEntityIp;
    }

    public String getReadCommunity() {
        return readCommunity;
    }

    public void setReadCommunity(String readCommunity) {
        this.readCommunity = readCommunity;
    }

    public VirtualNetService getVirtualNetService() {
        return virtualNetService;
    }

    public void setVirtualNetService(VirtualNetService virtualNetService) {
        this.virtualNetService = virtualNetService;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public VirtualNetAttribute getVirtualNetAttribute() {
        return virtualNetAttribute;
    }

    public void setVirtualNetAttribute(VirtualNetAttribute virtualNetAttribute) {
        this.virtualNetAttribute = virtualNetAttribute;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public Long getProductCount() {
        return productCount;
    }

    public void setProductCount(Long productCount) {
        this.productCount = productCount;
    }

    public String getVirtualNetName() {
        return virtualNetName;
    }

    public void setVirtualNetName(String virtualNetName) {
        this.virtualNetName = virtualNetName;
    }

    public Long getProductType() {
        return productType;
    }

    public void setProductType(Long productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductInVirtual getProduct() {
        return product;
    }

    public void setProduct(ProductInVirtual product) {
        this.product = product;
    }

    public ProductSnmpParam getProductSnmp() {
        return productSnmp;
    }

    public void setProductSnmp(ProductSnmpParam productSnmp) {
        this.productSnmp = productSnmp;
    }

    public List<Long> getVirtualNetIds() {
        return virtualNetIds;
    }

    public void setVirtualNetIds(List<Long> virtualNetIds) {
        this.virtualNetIds = virtualNetIds;
    }

    public List<Integer> getX() {
        return x;
    }

    public void setX(List<Integer> x) {
        this.x = x;
    }

    public List<Integer> getY() {
        return y;
    }

    public void setY(List<Integer> y) {
        this.y = y;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

}
