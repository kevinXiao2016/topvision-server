/***********************************************************************
 * $Id: CheckTreeNode.java,v1.0 2016年9月26日 上午10:56:19 $
 * 
 * @author: vanzand
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;

/**
 * @author vanzand
 * @created @2016年9月26日-上午10:56:19
 *  
 */
public class CheckTreeNode extends TreeNode {
    private Boolean needCbx;
    private Boolean checked = false;

    public Boolean getNeedCbx() {
        return needCbx;
    }

    public void setNeedCbx(Boolean needCbx) {
        this.needCbx = needCbx;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

}
