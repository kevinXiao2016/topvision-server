/***********************************************************************
 * $Id: NavigationButtonDao.java,v 1.1 Oct 25, 2009 4:36:00 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao;

import com.topvision.framework.dao.BaseEntityDao;
import com.topvision.platform.domain.NavigationButton;

/**
 * @author kelers
 * @Create Date Oct 25, 2009 4:36:00 PM
 */
public interface NavigationButtonDao extends BaseEntityDao<NavigationButton> {
    NavigationButton selectNaviByName(String name);
}
