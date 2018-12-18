/***********************************************************************
 * $Id: OnuCpeDaoImpl.java,v1.0 2016年7月6日 上午10:27:57 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onucpe.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onucpe.dao.OnuCpeDao;
import com.topvision.ems.epon.onucpe.domain.OnuUniCpe;
import com.topvision.ems.epon.performance.domain.OnuCpe;
import com.topvision.ems.epon.performance.domain.OnuUniCpeCount;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2016年7月6日-上午10:27:57
 *
 */
@Repository
public class OnuCpeDaoImpl extends MyBatisDaoSupport<Object> implements OnuCpeDao {

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onucpe.dao.OnuCpeDao#selectOnuUniCpeList(java.lang.Long)
     */
    @Override
    public List<OnuUniCpe> selectOnuUniCpeList(Long oltId) {
        return getSqlSession().selectList(getNameSpace("selectOnuUniCpeList"), oltId);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "OnuCpe";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onucpe.dao.OnuCpeDao#deleteCpeListByOnuId(java.lang.Long)
     */
    @Override
    public void deleteCpeListByOnuId(Long onuId) {
        getSqlSession().delete(getNameSpace("deleteCpeListByOnuId"), onuId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onucpe.dao.OnuCpeDao#batchinsertOnuCpe(java.util.List)
     */
    @Override
    public void batchinsertOnuCpe(List<OnuCpe> cpeList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OnuCpe onuCpe : cpeList) {
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

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onucpe.dao.OnuCpeDao#selectOltUniCpeList(java.lang.Long, int, int)
     */
    @Override
    public List<OnuUniCpe> selectOltUniCpeList(Long entityId, int start, int limit) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        map.put("start", start);
        map.put("limit", limit);
        return getSqlSession().selectList(getNameSpace("selectOltUniCpeList"), map);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onucpe.dao.OnuCpeDao#loadOltUniCpeListCount(java.lang.Long)
     */
    @Override
    public int selectOltUniCpeListCount(Long oltId) {
        return getSqlSession().selectOne(getNameSpace("selectOltUniCpeListCount"), oltId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onucpe.dao.OnuCpeDao#deleteCpeCountListByOnuId(java.lang.Long)
     */
    @Override
    public void deleteCpeCountListByOnuId(Long onuId) {
        getSqlSession().delete(getNameSpace("deleteCpeCountListByOnuId"), onuId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.onucpe.dao.OnuCpeDao#batchInsertOnuCpeCount(java.util.List)
     */
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

}
