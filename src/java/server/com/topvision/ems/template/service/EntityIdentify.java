/***********************************************************************
 * $Id: EntityIdentify.java,v 1.1 May 16, 2008 6:29:30 PM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.service;

import com.topvision.ems.facade.domain.EntityType;

/**
 * @Create Date May 16, 2008 6:29:30 PM
 * 
 * @author kelers
 * 
 */
public interface EntityIdentify {
    long getCorp(String sysObjectID);

    String getOS(String sysObjectID);

    EntityType identify(String sysObjectID, String sysServices);
}
