package com.topvision.ems.network.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.TopologyDao;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.domain.TopoLabel;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("topologyDao")
public class TopologyDaoImpl extends MyBatisDaoSupport<Object> implements TopologyDao {
    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopologyDao#getEdgeByFolderId(Long)
     */
    @Override
    public List<Link> getEdgeByFolderId(Long folderId) {
        return getSqlSession().selectList(getNameSpace("getEdgeByFolderId"), folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopologyDao#getEdgeIdByFolderId(Long)
     */
    @Override
    public List<Long> getEdgeIdByFolderId(Long folderId) {
        return getSqlSession().selectList(getNameSpace("getEdgeIdByFolderId"), folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopologyDao#getNodeCoordByFolderId(Long)
     */
    @Override
    public List<Entity> getNodeCoordByFolderId(Long folderId) {
        return getSqlSession().selectList(getNameSpace("getNodeCoordByFolderId"), folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopologyDao#getTopoLabels()
     */
    @Override
    public List<TopoLabel> getTopoLabels() {
        return getSqlSession().selectList(getNameSpace("getTopoLabels"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopologyDao#getVertexByFolderId(Long)
     */
    @Override
    public List<Entity> getVertexByFolderId(Long folderId) {
        return getSqlSession().selectList(getNameSpace("getVertexByFolderId"), folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopologyDao#saveEntityCoordinate(java.util.List)
     */
    @Override
    public void saveEntityCoordinate(final List<Entity> entities) {
        if (entities == null || entities.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = entities.size();
            for (int i = 0; i < size; i++) {
                sqlSession.update(getNameSpace() + "saveEntityCoordinate", entities.get(i));
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
     * @see com.topvision.ems.network.dao.TopologyDao#saveFolderCoordinate(java.util.List)
     */
    @Override
    public void saveFolderCoordinate(final List<TopoFolder> folders) {
        if (folders == null || folders.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = folders.size();
            for (int i = 0; i < size; i++) {
                sqlSession.update(getNameSpace() + "saveFolderCoordinate", folders.get(i));
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
     * @see com.topvision.ems.network.dao.TopologyDao#updateTopoLabel(java.util.List)
     */
    @Override
    public void updateTopoLabel(final List<TopoLabel> labels) {
        if (labels == null || labels.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = labels.size();
            for (int i = 0; i < size; i++) {
                sqlSession.update(getNameSpace() + "updateTopoLabel", labels.get(i));
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
    public List<Long> getFloderList() {
        return getSqlSession().selectList(getNameSpace("getFloderList"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.Topology";
    }
}
