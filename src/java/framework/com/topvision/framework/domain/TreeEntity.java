/**
 * 
 */
package com.topvision.framework.domain;

/**
 * @author niejun
 * 
 */
public interface TreeEntity {
    /**
     * 获取结点ID.
     * 
     * @return
     */
    String getId();

    /**
     * 获取父结点ID.
     * 
     * @return
     */
    String getParentId();

    /**
     * 设置显示的文本.
     * 
     * @return
     */
    String getText();
}
