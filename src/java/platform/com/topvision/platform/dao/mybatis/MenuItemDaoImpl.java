/***********************************************************************
 * $Id: MenuItemDaoImpl.java,v 1.1 Oct 25, 2009 4:39:42 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.MenuItemDao;
import com.topvision.platform.domain.MenuItem;

/**
 * @author kelers
 * @Create Date Oct 25, 2009 4:39:42 PM
 */
@Repository("menuItemDao")
public class MenuItemDaoImpl extends MyBatisDaoSupport<MenuItem> implements MenuItemDao {
    @Override
    public String getDomainName() {
        return MenuItem.class.getName();
    }
}
