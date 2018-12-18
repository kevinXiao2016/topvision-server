/***********************************************************************
 * $Id: IpRouteDaoImpl.java,v1.0 2013-11-16 下午03:03:53 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.iproute.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.iproute.dao.IpRouteDao;
import com.topvision.ems.epon.iproute.domain.IpRoute;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Rod John
 * @created @2013-11-16-下午03:03:53
 * 
 */
@Repository("ipRouteDao")
public class IpRouteDaoImpl extends MyBatisDaoSupport<IpRoute> implements IpRouteDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.iproute.domain.IpRoute";
    }

    @Override
    public void refreshIpRoute(Long entityId, List<IpRoute> routes) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteEntity", entityId);
            for (IpRoute route : routes) {
                sqlSession.insert(getNameSpace() + "insertEntity", route);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void insertIpRoute(IpRoute ipRoute) {
        this.getSqlSession().insert(getNameSpace("insertEntity"), ipRoute);
    }

    @Override
    public void deleteIpRoute(IpRoute ipRoute) {
        this.getSqlSession().delete(getNameSpace("deleteIpRoute"), ipRoute);
    }

    @Override
    public List<IpRoute> queryIpRouteList(Map<String, Object> map) {
        return this.getSqlSession().selectList(getNameSpace("queryIpRouteList"), map);
    }

    @Override
    public int queryIpRouteCount(Map<String, Object> map) {
        return this.getSqlSession().selectOne(getNameSpace("queryIpRouteCount"), map);
    }

    @Override
    public void updateIpRoute(IpRoute ipRoute) {
        this.getSqlSession().update(getNameSpace("updateIpRoute"), ipRoute);
    }

}
