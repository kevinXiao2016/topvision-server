package com.topvision.ems.network.dao;

import java.util.List;

import com.topvision.ems.network.domain.EntityAddress;
import com.topvision.framework.dao.BaseEntityDao;

public interface EntityAddressDao extends BaseEntityDao<EntityAddress> {
    /**
     * 删除给定设备ID的设备地址信息.
     * 
     * @param entity
     */
    void deleteByEntityId(Long entityId);

    /**
     * 插入设备地址信息.
     * 
     * @param ea
     */
    void insertOrUpdate(EntityAddress ea);

    /**
     * 批量插入设备地址信息.
     * 
     * @param eas
     */
    void insertOrUpdate(List<EntityAddress> eas);

    /**
     * 获取给定IP的设备地址信息.
     * 
     * @param ip
     * @return
     */
    EntityAddress selectByAddress(String ip);

    /**
     * 获取给定IP的设备地址信息.
     * 
     * @param ip
     * @return
     */
    List<EntityAddress> selectByAddressList(String ip);

    /**
     * 将设备地址信息中的IP修改为给定IP.
     * 
     * @param entityId
     * @param ip
     * @param oldIp
     */
    void updateAddress(Long entityId, String ip, String oldIp);
    
    
    /**
     * 删除设备IP以外的设备地址信息
     * 
     * @param entityId
     */
    void deleteEntityAddressWithoutEntityIp(Long entityId);
    
    /**
     * 清空缓存
     */
    void clearCache();
    
}
