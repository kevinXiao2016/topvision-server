/***********************************************************************
 * $Id: OnuPerfDaoImpl.java,v1.0 2015-4-24 上午10:15:21 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.performance.engine.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.topvision.ems.engine.dao.EngineDaoSupport;
import com.topvision.ems.epon.performance.domain.OnuCatvOrInfoResult;
import com.topvision.ems.epon.performance.domain.OnuCpe;
import com.topvision.ems.epon.performance.domain.OnuFlowCollectInfo;
import com.topvision.ems.epon.performance.domain.OnuLinkQualityResult;
import com.topvision.ems.epon.performance.domain.OnuOnlineResult;
import com.topvision.ems.epon.performance.domain.OnuUniCpeCount;
import com.topvision.ems.epon.performance.engine.OnuPerfDao;

/**
 * @author flack
 * @created @2015-4-24-上午10:15:21
 *
 */
public class OnuPerfDaoImpl extends EngineDaoSupport<Object> implements OnuPerfDao {
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.performance.domain.OnuLinkCollectInfo";
    }

    @Override
    public void insertOnuOnlineStatus(OnuOnlineResult onlineResult) {
        this.getSqlSession().insert(getNameSpace("insertOnuOnlineStatus"), onlineResult);
    }

    @Override
    public void insertOnuLinkQuality(OnuLinkQualityResult linkQuality) {
        this.getSqlSession().insert(getNameSpace("insertOnuLinkQuality"), linkQuality);
    }

    @Override
    public List<Long> queryUniIndexByOnuId(Long onuId) {
        return this.getSqlSession().selectList(getNameSpace("queryUniIndexByOnuId"), onuId);
    }

    @Override
    public String queryOnuTypeByOnuId(Long onuId) {
        return this.getSqlSession().selectOne(getNameSpace("queryOnuTypeByOnuId"), onuId);
    }

    @Override
    public List<Long> queryPonIndexByOnuId(Long onuId) {
        return this.getSqlSession().selectList(getNameSpace("queryPonIndexByOnuId"), onuId);
    }

    @Override
    public void batchInsertOnuFlowQuality(List<OnuFlowCollectInfo> flowList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OnuFlowCollectInfo flowPerf : flowList) {
                sqlSession.insert(getNameSpace("insertOnuFlowQuality"), flowPerf);
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
    public void removeOnuCpeListByEntityId(Long entityId) {
        getSqlSession().delete(getNameSpace("removeOnuCpeListByEntityId"), entityId);
    }

    @Override
    public void removeOnuCpeCountByEntityId(Long entityId) {
        getSqlSession().delete(getNameSpace("removeOnuCpeCountByEntityId"), entityId);
    }

    @Override
    public void batchInsertOnuCpeList(List<OnuCpe> allOnuCpes) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OnuCpe onuCpe : allOnuCpes) {
                sqlSession.insert(getNameSpace("insertOnuCpeList"), onuCpe);
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
    public void batchInsertOnuCpeCount(List<OnuUniCpeCount> onuUniCpeCounts) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OnuUniCpeCount onuUniCpeCount : onuUniCpeCounts) {
                sqlSession.insert(getNameSpace("insertOnuCpeCount"), onuUniCpeCount);
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.performance.engine.OnuPerfDao#insertOnuCatvQuality(com.topvision.ems.epon.performance.domain.OnuCatvOrInfoEntry)
     */
    @Override
    public void insertOnuCatvQuality(OnuCatvOrInfoResult onuCatvOrInfoResult) {
        this.getSqlSession().insert(getNameSpace("insertOnuCatvQuality"), onuCatvOrInfoResult);
    }
}
