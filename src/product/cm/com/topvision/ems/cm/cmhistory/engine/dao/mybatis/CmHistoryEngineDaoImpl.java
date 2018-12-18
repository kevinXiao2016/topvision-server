/***********************************************************************
 * $Id: CmHistoryEngineDaoImpl.java,v1.0 2015年4月14日 下午3:50:34 $ * 
 * @author: YangYib
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmhistory.engine.dao.mybatis;

import com.topvision.ems.cm.cmhistory.domain.CmHistoryShow;
import com.topvision.ems.cm.cmhistory.engine.dao.CmHistoryEngineDao;
import com.topvision.ems.cm.cmhistory.engine.domain.CmHistory;
import com.topvision.ems.engine.dao.EngineDaoSupport;

/**
 * @author YangYi
 * @created @2015年4月14日-下午3:50:34
 * 
 */

public class CmHistoryEngineDaoImpl extends EngineDaoSupport<CmHistoryShow> implements CmHistoryEngineDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.cmhistory.engine.domain.CmHistory";
    }

    @Override
    public void insertCmHistory(CmHistory c) {
        getSqlSession().insert(getNameSpace("insertCmHistory"), c);
    }

    @Override
    public void insertSpecifiedCmListLast(CmHistory c) {
        getSqlSession().delete(getNameSpace("deleteSpecifiedCmListLast"), c);
        getSqlSession().insert(getNameSpace("insertSpecifiedCmListLast"), c);

    }

}
