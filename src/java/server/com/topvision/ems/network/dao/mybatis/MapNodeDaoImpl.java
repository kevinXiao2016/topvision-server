package com.topvision.ems.network.dao.mybatis;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.network.dao.MapNodeDao;
import com.topvision.ems.network.domain.MapNode;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("mapNodeDao")
public class MapNodeDaoImpl extends MyBatisDaoSupport<MapNode> implements MapNodeDao {
    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#deleteMapNodeByFolder(java.lang.String)
     */
    @Override
    public void deleteMapNodeByFolder(Long folderId) {
        getSqlSession().delete(getNameSpace("deleteMapNodeByFolder"), folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#getMapNodeByObjId(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public MapNode getMapNodeByObjId(MapNode node) {
        return getSqlSession().selectOne(getNameSpace("getMapNodeByObjId"), node);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#getMapNodes(Long)
     */
    @Override
    public List<MapNode> getMapNodes(Long folderId) {
        return getSqlSession().selectList(getNameSpace("getMapNodes"), folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#saveMapNodeCoordinate(java.util.List)
     */
    @Override
    public void saveMapNodeCoordinate(final List<MapNode> nodes) {
        if (nodes == null || nodes.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = nodes.size();
            for (int i = 0; i < size; i++) {
                sqlSession.update(getNameSpace() + "saveMapNodeCoordinate", nodes.get(i));
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
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeDashedBorder(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeDashedBorder(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeDashedBorder", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeExpanded(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeExpanded(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeExpanded", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeFillColor(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeFillColor(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeFillColor", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeFixed(java.util.List)
     */
    @Override
    public void updateMapNodeFixed(final List<MapNode> nodes) {
        if (nodes == null || nodes.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = nodes.size();
            for (int i = 0; i < size; i++) {
                sqlSession.update(getNameSpace() + "updateMapNodeFixed", nodes.get(i));
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
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeFixed(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeFixed(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeFixed", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeFontColor(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeFontColor(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeFontColor", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeFontSize(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeFontSize(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeFontSize", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeGroup(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeGroup(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeGroup", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeIcon(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeIcon(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeIcon", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeShadow(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeShadow(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeShadow", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeSize(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeSize(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeSize", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeStrokeColor(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeStrokeColor(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeStrokeColor", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeStrokeWeight(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeStrokeWeight(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeStrokeWeight", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeText(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeText(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeText", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateMapNodeUrl(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeUrl(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateMapNodeUrl", mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.MapNodeDao#updateUserObject(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateUserObject(MapNode mapNode) {
        getSqlSession().update(getNameSpace() + "updateTextByUserObj", mapNode);
    }

    @Override
    public List<MapNode> getAllMapNodes() {
        return getSqlSession().selectList(getNameSpace() + "getAllMapNodes");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.MapNode";
    }
}
