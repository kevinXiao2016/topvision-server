/***********************************************************************
 * $Id: MibServiceImpl.java,v 1.1 2009-10-5 上午12:31:40 kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.realtime.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.realtime.service.MibService;
import com.topvision.ems.realtime.service.mib.AbstractMibValue;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.domain.Attribute;

import net.sf.json.JSONObject;

/**
 * @Create Date 2009-10-5 上午12:31:40
 * 
 * @author kelers
 * 
 */
public class MibServiceImpl extends BaseService implements MibService {
    private List<AbstractMibValue> mibs;
    private List<Attribute> types;

    /**
     * (non-Javadoc)
     * 
     * @see com.zetaframework.service.BaseService#destroy()
     */
    @Override
    @PreDestroy
    public void destroy() {
        super.destroy();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.realtime.service.MibService#getData(long, java.lang.String)
     */
    @Override
    public JSONObject getData(long entityId, String clazz) {
        AbstractMibValue value = null;
        for (AbstractMibValue mib : mibs) {
            if (mib.getClass().getName().equals(clazz)) {
                value = mib;
                break;
            }
        }
        if (value == null) {
            return null;
        } else {
            return value.getData(entityId);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.realtime.service.MibService#getHeaders(java.lang.String)
     */
    @Override
    public String[] getHeaders(String clazz) {
        AbstractMibValue value = null;
        for (AbstractMibValue mib : mibs) {
            if (mib.getClass().getName().equals(clazz)) {
                value = mib;
                break;
            }
        }
        if (value == null) {
            return null;
        } else {
            return value.getHeaders();
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.realtime.service.MibService#getTypes()
     */
    @Override
    public List<Attribute> getTypes(Entity entity) {
        return types;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.zetaframework.service.BaseService#initialize()
     */
    @Override
    public void initialize() {
        super.initialize();
        types = new ArrayList<Attribute>();
        if (mibs == null || mibs.isEmpty()) {
            return;
        }
        for (AbstractMibValue mib : mibs) {
            Attribute attr = new Attribute();
            attr.setName(mib.getDisplayName());
            attr.setValue(mib.getClass().getName());
            types.add(attr);
        }
    }

    /**
     * @param mibs
     *            the mibs to set
     */
    public void setMibs(List<AbstractMibValue> mibs) {
        this.mibs = mibs;
    }
}
