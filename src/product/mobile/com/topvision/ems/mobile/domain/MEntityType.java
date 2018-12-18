/***********************************************************************
 * $Id: EntityType.java,v1.0 2016年7月21日 上午9:50:13 $
 * 
 * @author: lzt
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.mobile.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author lzt
 * @created @2016年7月21日-上午9:50:13
 *
 */
public class MEntityType implements AliasesSuperType, Comparable<MEntityType> {
    private static final long serialVersionUID = 1224131657372615933L;
    private Long typeId;
    private String displayName;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public int compareTo(MEntityType o) {
        if (this.typeId > o.typeId) {
            return -1;
        } else if (this.typeId == o.typeId) {
            return 0;
        } else {
            return 1;
        }
    }

}
