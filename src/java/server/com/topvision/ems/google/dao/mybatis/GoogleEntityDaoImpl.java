package com.topvision.ems.google.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.google.dao.GoogleEntityDao;
import com.topvision.ems.google.domain.GoogleEntity;
import com.topvision.ems.network.domain.Link;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.exception.dao.DaoException;
import com.topvision.platform.util.CurrentRequest;

@Repository("googleEntityDao")
public class GoogleEntityDaoImpl extends MyBatisDaoSupport<GoogleEntity> implements GoogleEntityDao {
    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.google.dao.GoogleEntityDao#getLinkInGoogleMap()
     */
    @Override
    public List<Link> getLinkInGoogleMap() throws DaoException {
        return getSqlSession().selectList(getNameSpace("getLinkInGoogleMap"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.google.dao.GoogleEntityDao#insertOrUpdateGoogleEntity()
     */
    @Override
    public void insertOrUpdateGoogleEntity(GoogleEntity entity) {
        if (entity != null && entity.getEntityId() > 0) {
            GoogleEntity temp = getSqlSession().selectOne(getNameSpace("getGoogleEntityInfo"), entity.getEntityId());
            if (temp == null) {
                super.insertEntity(entity);
            } else {
                super.updateEntity(entity);
            }
        }
    }
    
    @Override
    public void batchInsertOrUpdateGoogleEntity(List<GoogleEntity> googleEntities) {
        SqlSession session = getBatchSession();
        try {
            for (GoogleEntity googleEntity : googleEntities) {
                session.insert(getNameSpace("batchInsertOrUpdateGoogleEntity"), googleEntity);
            }
            session.commit();
        } catch (Exception e) {
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public String getDomainName() {
        return GoogleEntity.class.getName();
    }

    @Override
    public List<Entity> queryAvaibleDevice() {
        HashMap<String, String> authorityHashMap = new HashMap<String, String>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("queryAvaibleDevice"), authorityHashMap);
    }

    @Override
    public void fixlocation(Long entityId, boolean fixed) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("fixed", fixed ? 1L : 0L);// true:draggable-1
        getSqlSession().update(getNameSpace("fixlocation"), map);
    }

}
