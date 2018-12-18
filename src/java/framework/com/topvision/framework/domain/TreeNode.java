/***********************************************************************
 * $Id: TreeNode.java,v1.0 2013-10-12 上午10:07:40 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.framework.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fanzidong
 * @created @2013-10-12-上午10:07:40 系统中的树节点的基本信息
 */
public class TreeNode {
    private Long id;
    private Long parentId;
    private String text;
    private String iconCls;
    private String url;
    private List<TreeNode> children = new ArrayList<TreeNode>();
    private Boolean needATag;
    //private Boolean needCbx;
    //private Boolean checked;
    private Boolean display;
    private String name;
    private boolean entity;
    private String value;
    

    public TreeNode() {
        super();
    }
    
    public TreeNode(Long id, Long parentId, String text, String iconCls) {
        super();
        this.id = id;
        this.parentId = parentId;
        this.text = text;
        this.iconCls = iconCls;
    }

    public TreeNode(Long id, Long parentId, String text, String iconCls, String url) {
        super();
        this.id = id;
        this.parentId = parentId;
        this.text = text;
        this.iconCls = iconCls;
        this.url = url;
    }

    @Override
    public boolean equals(Object anotherNode) {
        if (!(anotherNode instanceof TreeNode)) {
            return false;
        }
        TreeNode aNode = (TreeNode) anotherNode;
        if (this.getId().equals(aNode.getId())) {
            return true;
        } else {
            return false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public Boolean getNeedATag() {
        return needATag;
    }

    public void setNeedATag(Boolean needATag) {
        this.needATag = needATag;
    }

    /*public Boolean getNeedCbx() {
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
    }*/

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public Boolean getEntity() {
		return entity;
	}

	public void setEntity(Boolean entity) {
		this.entity = entity;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
    
    
    
}
