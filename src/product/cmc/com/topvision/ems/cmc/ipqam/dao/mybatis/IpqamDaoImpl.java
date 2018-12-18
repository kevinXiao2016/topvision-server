/***********************************************************************
 * $Id: IpqamDaoImpl.java,v1.0 2016年5月7日 上午11:38:01 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.ipqam.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ipqam.dao.IpqamDao;
import com.topvision.ems.cmc.ipqam.domain.Ipqam;
import com.topvision.ems.cmc.ipqam.domain.Program;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author loyal
 * @created @2016年5月7日-上午11:38:01
 * 
 */
@Repository("ipqamDao")
public class IpqamDaoImpl extends MyBatisDaoSupport<Ipqam> implements IpqamDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.ipqam.domain.Ipqam";
    }

    @Override
    public List<Ipqam> selectEqamList(Long cmcId) {
        return getSqlSession().selectList(getNameSpace() + "selectEqamList", cmcId);
    }

    @Override
    public List<Program> selectProgramList(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace() + "selectProgramList", queryMap);
    }

    @Override
    public Long selectProgramListCount(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace() + "selectProgramListCount", cmcId);
    }

    @Override
    public List<Program> selectOltEqamList(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace() + "selectOltProgramList", queryMap);
    }

    @Override
    public Long selectOltEqamListCount(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace() + "selectOltEqamListCount", queryMap);
    }

    @Override
    public Integer selectEqamSupportCountUnderOlt(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "selectEqamSupportCountUnderOlt", entityId);
    }

    @Override
    public Integer selectEqamSupport(Long entityId) {
        return getSqlSession().selectOne(getNameSpace() + "selectEqamSupport", entityId);
    }

}
