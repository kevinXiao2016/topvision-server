/***********************************************************************
 * $Id: CmPollTaskBuildDaoImpl.java,v1.0 2015年3月7日 下午3:27:24 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cm.cmpoll.taskbuild.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cm.cmpoll.facade.domain.CmPollTask;
import com.topvision.ems.cm.cmpoll.taskbuild.dao.CmPollTaskBuildDao;
import com.topvision.ems.cm.cmpoll.taskbuild.domain.CmPollAttribute;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2015年3月7日-下午3:27:24
 * 
 */
@Repository("cmPollTaskBuildDao")
public class CmPollTaskBuildDaoImpl extends MyBatisDaoSupport<CmPollTask> implements CmPollTaskBuildDao {

    @Override
    public List<CmAttribute> selectCmListCountN(Long cmId, Long count) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmId", cmId);
        map.put("count", count);
        return getSqlSession().selectList(getNameSpace("selectCmListCountN"), map);
    }

    @Override
    public List<Long> selectEntityIdWithIp(Long type) {
        return getSqlSession().selectList(getNameSpace("selectEntityIdWithIp"), type);
    }

    @Override
    public List<CmPollAttribute> selectCmListByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectCmListByEntityId"), entityId);
    }

    @Override
    public Long getCmOnLineNum() {
        return getSqlSession().selectOne(getNameSpace("getCmNum"));
    }

    @Override
    protected String getDomainName() {
        return CmPollTask.class.getName();
    }

}
