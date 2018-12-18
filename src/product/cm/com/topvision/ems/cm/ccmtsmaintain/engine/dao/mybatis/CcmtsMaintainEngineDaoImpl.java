/***********************************************************************
 * $Id: CcmtsMaintainEngineDaoImpl.java,v1.0 2015-5-26 下午8:40:02 $
 * 
 * @author: Fanzidong
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.ccmtsmaintain.engine.dao.mybatis;

import java.util.List;

import com.topvision.ems.cm.ccmtsmaintain.engine.dao.CcmtsMaintainEngineDao;
import com.topvision.ems.cm.ccmtsmaintain.engine.domain.CcmtsChannel;
import com.topvision.ems.engine.dao.EngineDaoSupport;

/**
 * @author fanzidong
 * @created @2015-5-26-下午8:40:02
 *
 */
public class CcmtsMaintainEngineDaoImpl extends EngineDaoSupport<CcmtsChannel> implements CcmtsMaintainEngineDao {

    /* (non-Javadoc)
     * @see com.topvision.ems.cm.ccmtsmaintain.engine.dao.CcmtsMaintainEngineDao#selectCcmtsChannels()
     */
    @Override
    public List<CcmtsChannel> selectCcmtsChannels() {
        return getSqlSession().selectList(getNameSpace("selectCcmtsChannels"));
    }
    
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.ccmtsmaintain.engine.domain.CcmtsChannel";
    }

}
