/***********************************************************************
 * $Id: AutoClearDaoImpl.java,v1.0 2016年11月14日 上午10:50:22 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.autoclear.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.autoclear.dao.AutoClearDao;
import com.topvision.ems.autoclear.domain.AutoClearCmciRecord;
import com.topvision.ems.autoclear.domain.AutoClearRecord;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Rod John
 * @created @2016年11月14日-上午10:50:22
 *
 */
@Repository("autoClearDao")
public class AutoClearDaoImpl extends MyBatisDaoSupport<Entity> implements AutoClearDao {

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.autoclear.dao.AutoClearDao";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.autoclear.dao.AutoClearDao#insertAutoClearRecord(com.topvision.ems.autoclear.domain.AutoClearRecord)
     */
    @Override
    public void insertAutoClearRecord(AutoClearRecord autoClearRecord) {
        getSqlSession().insert(getNameSpace() + "insertAutoClearRecord", autoClearRecord);
    }
    
    @Override
    public void insertAutoClearCmciRecord(AutoClearCmciRecord autoClearCmciRecord){
        getSqlSession().insert(getNameSpace() + "insertAutoClearCmciRecord", autoClearCmciRecord);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.autoclear.dao.AutoClearDao#loadAutoClearRecord(java.lang.Integer, java.lang.Integer)
     */
    @Override
    public List<AutoClearRecord> loadAutoClearRecord(Integer start, Integer limit) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("limit", limit);
        return getSqlSession().selectList(getNameSpace() + "loadAutoClearRecord", map);
    }
    
    @Override
    public List<AutoClearCmciRecord> loadAutoClearCmciRecord(Integer start, Integer limit) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("start", start);
        map.put("limit", limit);
        return getSqlSession().selectList(getNameSpace() + "loadAutoClearCmciRecord", map);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.autoclear.dao.AutoClearDao#loadAutoClearRecordCount()
     */
    @Override
    public Integer loadAutoClearRecordCount() {
        return getSqlSession().selectOne(getNameSpace() + "loadAutoClearRecordCount");
    }
    
    @Override
    public Integer loadAutoClearCmciRecordCount() {
        return getSqlSession().selectOne(getNameSpace() + "loadAutoClearCmciRecordCount");
    }

}
