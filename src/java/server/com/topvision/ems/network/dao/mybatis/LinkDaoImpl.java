package com.topvision.ems.network.dao.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.network.dao.LinkDao;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.LinkEx;
import com.topvision.ems.network.domain.PortEx;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("linkDao")
public class LinkDaoImpl extends MyBatisDaoSupport<Link> implements LinkDao {
    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#deleteLinkByEntityId(java.util.List)
     */
    @Override
    public void deleteLinkByEntityId(final List<Long> entityIds) {
        if (entityIds == null || entityIds.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = entityIds.size();
            for (int i = 0; i < size; i++) {
                sqlSession.delete(getNameSpace() + "deleteLinkByEntityId", entityIds.get(i));
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#deleteLinkByEntityId(Long)
     */
    @Override
    public void deleteLinkByEntityId(Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteLinkByEntityId", entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#emptyLinkByRecyle()
     */
    @Override
    public void emptyLinkByRecyle() {
        getSqlSession().delete(getNameSpace() + "emptyLinkByRecyle");
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#getAllLink(java.util.Map)
     */
    @Override
    public List<LinkEx> getAllLink(Map<String, Long> map) {
        return getSqlSession().selectList(getNameSpace("getAllLink"), map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#getLinkByFolderId(Long)
     */
    @Override
    public List<Link> getLinkByFolderId(Long folderId) {
        return getSqlSession().selectList(getNameSpace("getLinkByFolderId"), folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#getLinkByPort(Long, Integer)
     */
    @Override
    public List<Link> getLinkByPort(Long entityId, Long ifIndex) {
        Link link = new Link();
        link.setSrcEntityId(entityId);
        link.setSrcIfIndex(ifIndex);
        return getSqlSession().selectList(getNameSpace("getLinkByPort"), link);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#getLinkFlowTop(java.util.Map)
     */
    @Override
    // FIXME Link.xml中【getLinkFlowTop】的sql中，参数实际上未被使用
    public List<LinkEx> getLinkFlowTop(Map<String, String> map) {
        return getSqlSession().selectList(getNameSpace("getLinkFlowTop"), map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#getLinkIdByFolderId(Long)
     */
    @Override
    public List<Long> getLinkIdByFolderId(Long folderId) {
        return getSqlSession().selectList(getNameSpace("getLinkIdByFolderId"), folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#getLinkRateTop(java.util.Map)
     */
    @Override
    // FIXME Link.xml中【getLinkRateTop】的sql中，参数实际上未被使用
    public List<LinkEx> getLinkRateTop(Map<String, String> map) {
        return getSqlSession().selectList(getNameSpace("getLinkRateTop"), map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#getLinkTableByEntity(Long)
     */
    @Override
    public List<PortEx> getLinkTableByEntity(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getLinkTableByEntity"), entityId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#insertOrUpdateEntity(com.topvision.ems.network.domain.Link)
     */
    @Override
    public void insertOrUpdateEntity(Link link) {
        Link obj = (Link) getSqlSession().selectOne(getNameSpace("hasLink"), link);
        if (obj == null) {
            super.insertEntity(link);
        } else {
            link.setLinkId(obj.getLinkId());
            super.updateEntity(link);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#insertOrUpdateEntity(java.util.List)
     */
    @Override
    public void insertOrUpdateEntity(List<Link> links) {
        for (Link link : links) {
            this.insertOrUpdateEntity(link);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#queryLink(java.lang.String)
     */
    @Override
    public List<LinkEx> queryLink(String query) {
        return getSqlSession().selectList(getNameSpace("queryLink"), query);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.LinkDao#updateOutline(com.topvision.ems.network.domain.Link)
     */
    @Override
    public void updateOutline(Link link) {
        getSqlSession().update(getNameSpace() + "updateOutline", link);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.dao.LinkDao#isLinkExists(com.topvision.ems.network.domain.Link)
     */
    @Override
    public Long isLinkExists(Link link) {
        Long linkId = (Long) getSqlSession().selectOne(getNameSpace("isLinkExists"), link);
        if (linkId != null) {
            return linkId;
        }
        return null;
    }

    @Override
    public List<Long> getLinkIdByEntityIds(List<Long> entityIds) {
    	if(entityIds==null || entityIds.size()==0){
    		return new ArrayList<Long>();
    	}
        return getSqlSession().selectList(getNameSpace("getLinkIdByEntityIds"), entityIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.Link";
    }
}
