/***********************************************************************
 * $Id: DispersionEngineDaoImpl.java,v1.0 2015-3-26 下午1:56:54 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.dispersion.engine.dao.mybatis;

import java.util.List;

import com.topvision.ems.cm.dispersion.engine.dao.DispersionEngineDao;
import com.topvision.ems.cm.dispersion.engine.domain.OpticalNodeRelation;
import com.topvision.ems.engine.dao.EngineDaoSupport;

/**
 * @author fanzidong
 * @created @2015-3-26-下午1:56:54
 *
 */
public class DispersionEngineDaoImpl extends EngineDaoSupport<OpticalNodeRelation> implements DispersionEngineDao {

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.dispersion.engine.dao.DispersionEngineDao#selectOpticalNodeRelation()
     */
    @Override
    public List<OpticalNodeRelation> selectOpticalNodeRelation() {
        return getSqlSession().selectList(getNameSpace("selectOpticalNodeRelation"));
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.dispersion.engine.domain.OpticalNodeRelation";
    }

}
