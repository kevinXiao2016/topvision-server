/***********************************************************************
 * $Id: ToolbarButtonDaoImpl.java,v 1.1 Oct 25, 2009 4:38:52 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.dao.ToolbarButtonDao;
import com.topvision.platform.domain.ToolbarButton;

/**
 * @author kelers
 * @Create Date Oct 25, 2009 4:38:52 PM
 */
@Repository("toolbarButtonDao")
public class ToolbarButtonDaoImpl extends MyBatisDaoSupport<ToolbarButton> implements ToolbarButtonDao {
    @Override
    public String getDomainName() {
        return ToolbarButton.class.getName();
    }
}
