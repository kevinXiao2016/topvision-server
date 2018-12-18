/**
 * 
 */
package com.topvision.platform.message.event;

/**
 * @author niejun
 * 
 */
public interface EntityListener extends EmsListener {
    /**
     * 添加entity
     * 
     * @param event
     */
    void entityAdded(EntityEvent event);

    /**
     * 发现完entity
     * 
     * @param event
     */
    void entityDiscovered(EntityEvent event);

    /**
     * 设备属性变化
     * 
     * @param entityId
     * @param attrNames
     * @param attrValues
     */
    void attributeChanged(long entityId, String[] attrNames, String[] attrValues);

    /**
     * 设备变化
     * 
     * @param event
     */
    void entityChanged(EntityEvent event);

    /**
     * 删除Entity
     * 
     * @param event
     */
    void entityRemoved(EntityEvent event);

    /**
     * 设备管理状态变化
     * 
     * @param event
     */
    void managerChanged(EntityEvent event);
    
    /**
     * 设备变化
     * 
     * @param event
     */
    void entityReplaced(EntityEvent event);
}
