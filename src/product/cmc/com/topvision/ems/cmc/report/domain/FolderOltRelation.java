/***********************************************************************
 * $Id: FolderOltRelation.java,v1.0 2013-9-25 下午12:05:26 $
 * 
 * @author: fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.report.domain;

import java.util.Map;

/**
 * @author fanzidong
 * @created @2013-9-25-下午12:05:26
 * 
 */
public class FolderOltRelation {
    private Long folderId;
    private String folderName;
    private Map<String, OltPonRelation> oltPonRelations;
    private Integer rowspan;

    public FolderOltRelation() {
        super();
    }

    public FolderOltRelation(Long folderId, String folderName, Integer rowspan) {
        super();
        this.folderId = folderId;
        this.folderName = folderName;
        this.rowspan = rowspan;
    }

    public FolderOltRelation(Long folderId, String folderName, Map<String, OltPonRelation> oltPonRelations,
            Integer rowspan) {
        super();
        this.folderId = folderId;
        this.folderName = folderName;
        this.oltPonRelations = oltPonRelations;
        this.rowspan = rowspan;
    }

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

    public Map<String, OltPonRelation> getOltPonRelations() {
        return oltPonRelations;
    }

    public void setOltPonRelations(Map<String, OltPonRelation> oltPonRelations) {
        this.oltPonRelations = oltPonRelations;
    }

    public Integer getRowspan() {
        return rowspan;
    }

    public void setRowspan(Integer rowspan) {
        this.rowspan = rowspan;
    }

}
