/***********************************************************************
 * $Id: UserAuthFolder.java,v1.0 2015-6-30 下午4:53:05 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.domain;

import com.topvision.framework.dao.mybatis.AliasesSuperType;

/**
 * @author fanzidong
 * @created @2015-6-30-下午4:53:05
 * 
 */
public class UserAuthFolder implements AliasesSuperType{
    private static final long serialVersionUID = 2928774136267465328L;
    
    private Long userId;
    private String userName;
    private Long folderId;
    private String folderName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

}
