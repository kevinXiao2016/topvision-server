/***********************************************************************
 * $Id: OltPortInfoDaoImpl.java,v1.0 2016-4-12 上午11:31:39 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.portinfo.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OltSniOptical;
import com.topvision.ems.epon.portinfo.dao.OltPortInfoDao;
import com.topvision.ems.epon.portinfo.domain.OltPortOpticalInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2016-4-12-上午11:31:39
 *
 */
@Service("oltPortInfoDao")
public class OltPortInfoDaoImpl extends MyBatisDaoSupport<Object> implements OltPortInfoDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.portinfo.domain.OltPortOpticalInfo";
    }

    @Override
    public List<OltPortOpticalInfo> querySniOpticalInfoList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("querySniOpticalInfo"), entityId);
    }

    @Override
    public List<OltPortOpticalInfo> queryPonOpticalInfoList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryPonOpticalInfo"), entityId);
    }

    @Override
    public void updateSniOpticalInfo(OltPortOpticalInfo sniOpticalInfo) {
        getSqlSession().update(getNameSpace("updateSniOpticalInfo"), sniOpticalInfo);
    }

    @Override
    public void batchUpdateSniOptical(List<OltPortOpticalInfo> sniOpticalList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltPortOpticalInfo sniOptical : sniOpticalList) {
                sqlSession.update(getNameSpace("updateSniOpticalInfo"), sniOptical);
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
    public void updatePonOpticalInfo(OltPortOpticalInfo ponOpticalInfo) {
        getSqlSession().update(getNameSpace("updatePonOpticalInfo"), ponOpticalInfo);
    }

    @Override
    public void batchUpdatePonOptical(List<OltPortOpticalInfo> ponOpticalList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltPortOpticalInfo ponOptical : ponOpticalList) {
                sqlSession.update(getNameSpace("updatePonOpticalInfo"), ponOptical);
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
    public List<OltSniOptical> queryOltSniList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryOltSniList"), entityId);
    }

    @Override
    public List<OltPonOptical> queryOltPonList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("queryOltPonList"), entityId);
    }

}
