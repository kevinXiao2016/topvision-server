/***********************************************************************
 * $Id: CmTopoInfo.java,v1.0 2013-11-2 上午10:38:08 $
 * 
 * @author: haojie
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.domain;

/**
 * @author haojie
 * @created @2013-11-2-上午10:38:08
 * 
 */
public class CmTopo {
    private Long folderId;
    private String folderName;

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CmTopo [folderId=");
        builder.append(folderId);
        builder.append(", folderName=");
        builder.append(folderName);
        builder.append("]");
        return builder.toString();
    }

}
