/***********************************************************************
 * $Id: UpgradeStatus.java,v1.0 2014年11月22日 下午2:56:16 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.upgrade.domain;

/**
 * @author loyal
 * @created @2014年11月22日-下午2:56:16
 *
 */
public class UpgradeStatusCode {
    private Integer id;
    private String displayName;
    
    public UpgradeStatusCode(Integer id, String displayName) {
        super();
        this.id = id;
        this.displayName = displayName;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
}
