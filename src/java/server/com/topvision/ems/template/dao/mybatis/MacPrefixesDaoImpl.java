/***********************************************************************
 * $Id: MacPrefixesDaoImpl.java,v 1.1 2009-11-8 下午06:06:08 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.template.dao.mybatis;

import org.springframework.stereotype.Repository;

import com.topvision.ems.template.dao.MacPrefixesDao;
import com.topvision.ems.template.domain.MacPrefixes;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @Create Date 2009-11-8 下午06:06:08
 * 
 * @author kelers
 * 
 */
@Repository("macPrefixesDao")
public class MacPrefixesDaoImpl extends MyBatisDaoSupport<MacPrefixes> implements MacPrefixesDao {
    @Override
    public String getDomainName() {
        return MacPrefixes.class.getName();
    }
}
