/***********************************************************************
 * $Id: EIConstant.java,v1.0 2015-7-9 下午4:25:19 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.exportAndImport.domain;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.topvision.platform.SystemConstants;

/**
 * @author fanzidong
 * @created @2015-7-9-下午4:25:19
 * 
 */
public class EIConstant {
    public static final String ROLE = "Role";
    public static final String ROLEFUNCTIONRELA = "RoleFunctionRela";
    public static final String TOPOFOLDER = "TopoFolder";
    public static final String MAPNODE = "MapNode";
    public static final String USER = "User";
    public static final String USERAUTHFOLDER = "UserAuthFolder";
    public static final String USERROLERELA = "UserRoleRela";
    public static final String USERPORTLETRELA = "UserPortletRela";
    public static final String USERPREFERENCES = "UserPreferences";
    public static final String ENTITY = "Entity";
    public static final String FOLDERVIEW = "FolderView";
    public static final String ENTITYFOLDERRELA = "EntityFolderRela";
    public static final String ENTITYALIAS = "EntityAlias";
    
    public static final String PREFIX = "Begin";
    public static final String SUFFIX = "End";

    public static final String EXPORT_ROOT = SystemConstants.ROOT_REAL_PATH + File.separator + "META-INF"
            + File.separator + "exportRoot" + File.separator;

    public static final String IMPORT_ROOT = SystemConstants.ROOT_REAL_PATH + File.separator + "META-INF"
            + File.separator + "importRoot" + File.separator;
    
    public static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");  
    
}
