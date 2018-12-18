/***********************************************************************
 * $Id: VirtualNetDaoImpl.java,v1.0 2012-2-23 上午11:33:57 $
 * 
 * @author: zhanglongyang
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.VirtualNetDao;
import com.topvision.ems.network.domain.ProductSnmpParam;
import com.topvision.ems.network.domain.VirtualNetAttribute;
import com.topvision.ems.network.domain.VirtualProduct;
import com.topvision.ems.network.service.NetworkConstants;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author zhanglongyang
 * @created @2012-2-23-上午11:33:57
 * 
 */
@Repository("virtualNetDao")
public class VirtualNetDaoImpl extends MyBatisDaoSupport<Object> implements VirtualNetDao {
    @Autowired
    private EntityDao entityDao;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.dao.VirtualNetDao#createVirtualNet(com.topvision.ems.network.domain
     * . VirtualNetAttribute)
     */
    @Override
    public Long createVirtualNet(VirtualNetAttribute virtualNetAttribute) {
        getSqlSession().insert(getNameSpace() + "createVirtualNet", virtualNetAttribute);
        return virtualNetAttribute.getVirtualNetId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.VirtualNetDao#deleteVirtualNet(java.lang.Long)
     */
    @Override
    public void deleteVirtualNet(Long virtualNetId) {
        getSqlSession().delete(getNameSpace() + "deleteVirtualNet", virtualNetId);
    }

    @Override
    public void renameVirtualNet(Long virtualNetId, String virtualNetName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("virtualNetId", virtualNetId);
        params.put("virtualName", virtualNetName);
        getSqlSession().update(getNameSpace() + "renameVirtualNet", params);
    }

    @Override
    public void modifyVirtualNetAttribute(VirtualNetAttribute virtualNetAttribute) {
        getSqlSession().update(getNameSpace() + "modifyVirtualNetAttribute", virtualNetAttribute);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.VirtualNetDao#getAllProductsInVirtualNet(java.lang.Long)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<VirtualProduct> getAllProductsInVirtualNet(Long virtualNetId, Long start, Long limit) {
        Map map = new HashMap();
        map.put("virtualNetId", virtualNetId);
        map.put("start", start);
        map.put("limit", limit);
        return getSqlSession().selectList(getNameSpace("getAllProductsInVirtualNet"), map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.dao.VirtualNetDao#getAllProductsInVirtualNetCount(java.lang.Long)
     */
    @Override
    public Long getAllProductsInVirtualNetCount(Long virtualNetId) {
        return getSqlSession().selectOne(getNameSpace("getAllProductsInVirtualNetCount"), virtualNetId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.dao.VirtualNetDao#insertVirtualProductRelation(com.topvision.ems
     * .network.domain.VirtualNetEntityRela)
     */
    @Override
    public void insertVirtualProductRelation(VirtualProduct virtualProduct) {
        getSqlSession().insert(getNameSpace() + "insertVirtualProductRelation", virtualProduct);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.VirtualNetDao#getVirtualNetByFolderId(java.lang.Long)
     */
    @Override
    public List<VirtualNetAttribute> getVirtualNetByFolderId(Long folderId) {
        return getSqlSession().selectList(getNameSpace("getVirtualNetByFolderId"), folderId);
    }

    /*
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.VirtualNetDao#deleteProduct(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void deleteProduct(Long productId, Long productType) {
        // 在虚拟子网中删除产品首先删除产品与虚拟子网的关系，然后删除设备的关系表
        // TODO 需要首先获取folderId
        Long folderId = 0l;
        if (isProductExistsInTopo(productId, productType, folderId)) {
            // TODO 删除entity表
            Long entityId = getEntityIdByProductIdAndType(productId, productType);
            entityDao.removeProductFromTopoGraph(entityId);
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("productId", productId);
        params.put("productType", productType);
        getSqlSession().delete(getNameSpace() + "deleteProductFromVirtualNet", params);
        // TODO 后续还需要删除每种设备的单独关系表,达到级联删除设备信息的目的
        params.clear();
        params.put("tableName", NetworkConstants.VIRTUALPRODUCTMAP_MAP.get(productType) + "entityrelation");
        params.put("productName", NetworkConstants.VIRTUALPRODUCTMAP_MAP.get(productType) + "Id");
        params.put("productId", productId);
        getSqlSession().delete(getNameSpace() + "deleteProduct", params);
    }

    /*
     * 
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.dao.VirtualNetDao#removeProductVirtualNetRelation(java.lang.Long,
     * java.lang.Long, java.lang.Long)
     */
    @Override
    public void removeProductVirtualNetRelation(Long productId, Long productType, Long virtualNetId) {
        // 在虚拟子网中删除关系只需要删除产品与虚拟子网的关系即可
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("productId", productId);
        params.put("productType", productType);
        params.put("virtualNetId", virtualNetId);
        getSqlSession().delete(getNameSpace() + "removeProductVirtualNetRelation", params);
    }

    /*
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.VirtualNetDao#relateProductToVirtualNet(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void relateProductToVirtualNet(Long productId, Long virtualNetId) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("productId", productId);
        params.put("virtualNetId", virtualNetId);
        getSqlSession().insert(getNameSpace() + "relateProductToVirtualNet", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.VirtualNetDao#insertEntityProductRelation(java.lang.Long,
     * java.lang.Long, java.lang.String)
     */
    @Override
    public void insertEntityProductRelation(Long productId, Long entityId, Long productType) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("productId", productId);
        params.put("entityId", entityId);
        params.put("productType", productType);
        getSqlSession().insert(getNameSpace() + "insertEntityProductRelation", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.dao.VirtualNetDao#insertVirtualProductToEntity(com.topvision.ems
     * .facade.domain.Entity)
     */
    @Override
    public Long insertVirtualProductToEntity(Entity entity) {
        getSqlSession().insert("Entity.insertEntity", entity);
        Long entityId = entity.getEntityId();
        entity.getParam().setEntityId(entityId);
        getSqlSession().insert("Entity.insertSnmpParam", entity.getParam());
        getSqlSession().insert("Entity.entity2Folder", entity);
        return entityId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.dao.VirtualNetDao#getProductSnmpParamByProductId(java.lang.Long)
     */
    @Override
    public ProductSnmpParam getProductSnmpParamByProductId(Long productId, Long productType) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("productId", productId);
        params.put("productType", productType);
        return getSqlSession().selectOne(getNameSpace("getProductSnmpParamByProductId"), params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.dao.VirtualNetDao#insertProSnmpParam(com.topvision.ems.network.
     * domain.ProductSnmpParam)
     */
    @Override
    public void insertProSnmpParam(ProductSnmpParam productSnmpParam) {
        getSqlSession().insert(getNameSpace() + "insertProSnmpParam", productSnmpParam);
    }

    /*
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.VirtualNetDao#saveVirtualNetCoordinate(java.util.List)
     */
    @Override
    public void saveVirtualNetCoordinate(final List<VirtualNetAttribute> vnList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (VirtualNetAttribute vn : vnList) {
                sqlSession.update(getNameSpace() + "saveVirtualNetCoordinate", vn);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.VirtualNetDao#isProductExistsInTopo(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public boolean isProductExistsInTopo(Long productId, Long productType, Long folderId) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("productId", productId);
        params.put("productType", productType);
        params.put("folderId", folderId);
        Long count = (Long) getSqlSession().selectOne(getNameSpace("isProductExistsInTopo"), params);
        if (count > 0) {
            return true;
        } else {
            return false;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.dao.VirtualNetDao#getEntityIdByProductIdAndType(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public Long getEntityIdByProductIdAndType(Long productId, Long productType) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("productId", productId);
        params.put("productType", productType);
        return getSqlSession().selectOne(getNameSpace("getEntityIdByProductIdAndType"), params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.VirtualNetDao#selectByAddress(java.lang.String)
     */
    @Override
    public String selectByAddress(String ipAddress) {
        return getSqlSession().selectOne(getNameSpace("selectByAddress"), ipAddress);
    }

    /*
     * 
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.VirtualNetDao#renameProductInVirtualNet(java.lang.Long,
     * java.lang.Long, java.lang.Long, java.lang.String)
     */
    @Override
    public void renameProductInVirtualNet(Long virtualNetId, Long productId, Long productType, String productName) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("virtualNetId", virtualNetId);
        params.put("productId", productId);
        params.put("productType", productType);
        params.put("productName", productName);
        getSqlSession().update(getNameSpace() + "renameProductInVirtualNet", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.VirtualNetDao#removeProductFromFolder(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void removeProductFromFolder(Long entityId, Long folderId) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("entityId", entityId);
        params.put("folderId", folderId);
        getSqlSession().delete(getNameSpace() + "removeProductFromFolder", params);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.VirtualNetDao#isProductExistsInFolder(java.lang.Long)
     */
    @Override
    public boolean isProductExistsInFolder(Long entityId) {
        Integer count = (Integer) getSqlSession().selectOne(getNameSpace("isProductExistsInFolder"), entityId);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Long getVirtualNetIdByCmcId(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace("getVirtualNetIdByCmcId"), cmcId);
    }

    @Override
    public VirtualNetAttribute getVirtualNetByVirtualNetId(Long virtualNetId) {
        return getSqlSession().selectOne(getNameSpace("getVirtualNetByVirtualNetId"), virtualNetId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.VirtualNet";
    }

}
