/***********************************************************************
 * $Id: EntityTreeNode.java,v1.0 2017年4月11日 下午3:32:09 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;

/**
 * @author fanzidong
 * @created @2017年4月11日-下午3:32:09
 *
 */
public class EntityTreeNode extends TreeNode {
    private Long typeId;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

}
