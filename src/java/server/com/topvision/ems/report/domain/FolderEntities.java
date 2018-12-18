/***********************************************************************
 * $Id: FolderEntities.java,v1.0 2013-10-9 上午9:27:15 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.report.domain;

import java.util.Map;

/**
 * @author fanzidong
 * @created @2013-10-9-上午9:27:15
 * 
 */
public class FolderEntities {
    private Long folderId;
    private String folderName;
    private Map<Long, Object> entities;
    private Integer rowspan;

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

    public Map<Long, Object> getEntities() {
        return entities;
    }

    public void setEntities(Map<Long, Object> entities) {
        this.entities = entities;
    }

    public Integer getRowspan() {
        return rowspan;
    }

    public void setRowspan(Integer rowspan) {
        this.rowspan = rowspan;
    }

}
