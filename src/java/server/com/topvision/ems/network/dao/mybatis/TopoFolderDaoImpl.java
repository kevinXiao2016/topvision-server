package com.topvision.ems.network.dao.mybatis;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.TopoFolderDao;
import com.topvision.ems.network.domain.FolderRelation;
import com.topvision.ems.network.domain.FolderUserGroupRela;
import com.topvision.ems.network.domain.MapNode;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.domain.TopoFolderEx;
import com.topvision.ems.network.domain.TopoFolderStat;
import com.topvision.ems.report.dao.StatReportDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.platform.util.CurrentRequest;
import com.topvision.platform.util.DataBaseConstants;

@Repository("topoFolderDao")
public class TopoFolderDaoImpl extends MyBatisDaoSupport<TopoFolder> implements TopoFolderDao {
    @Autowired
    private StatReportDao statReportDao;

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#deleteFolderUserGroup(Long)
     */
    @Override
    public void deleteFolderUserGroup(Long userGroupId) {
        getSqlSession().delete(getNameSpace() + "deleteFolderUserGroup", userGroupId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#deleteTopoFolder(java.lang.String)
     */
    @Override
    public void deleteTopoFolder(Long folderId) {
        getSqlSession().delete(getNameSpace() + "deleteByPath", folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#getChildCount(Long)
     */
    @Override
    public Integer getChildCount(Long folderId) {
        return getSqlSession().selectOne(getNameSpace("getChildCount"), folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#getChildTopoFolder(Long)
     */
    @Override
    public List<TopoFolder> getChildTopoFolder(Long superiorId) {
        return getSqlSession().selectList(getNameSpace("getChildTopoFolder"), superiorId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#getChildTopoFolderByPath(java.lang.String)
     */
    @Override
    public List<TopoFolderEx> getChildTopoFolderByPath(String superiorPath) {
        return getSqlSession().selectList(getNameSpace("getChildTopoFolderByPath"), superiorPath);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#getEntityCountInFolder(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public Integer getEntityCountInFolder(TopoFolder folder) {
        return getSqlSession().selectOne(getNameSpace("getEntityCountInFolder"), folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#getFolderPath(Long)
     */
    @Override
    public String getFolderPath(Long folderId) {
        return getSqlSession().selectOne(getNameSpace("getFolderPath"), folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#getMyTopoFolder(java.util.Map)
     */
    @Override
    public List<TopoFolder> getMyTopoFolder(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityFolderName());
        List<TopoFolder> tmp = getSqlSession().selectList(getNameSpace("getMyTopoFolder"), map);
        // Map<String, Object> idmap = getSqlMapClientTemplate().queryForMap(getNameSpace() +
        // "getMyTopoFolderMapNode",
        // null, "userObjId", "nodeId");
        Map<String, Object> idmap = selectMapByKeyAndValue(getNameSpace() + "getMyTopoFolderMapNode", null,
                "userObjId", "nodeId");
        for (Iterator<TopoFolder> topoFolderIterator = tmp.iterator(); topoFolderIterator.hasNext();) {
            TopoFolder topoFolder = topoFolderIterator.next();
            if (idmap.containsKey(topoFolder.getFolderId())) {
                topoFolder.setMapNodeId((Long) idmap.get(topoFolder.getFolderId()));
            }
        }
        return tmp;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#getStatTopoFolder()
     */
    @Override
    public List<TopoFolderStat> getStatTopoFolder() {
        HashMap<String, String> authorityHashMap = new HashMap<String, String>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityFolderName());
        return getSqlSession().selectList(getNameSpace("getStatTopoFolder"), authorityHashMap);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#getTopoFolderByGroup(Long)
     */
    @Override
    public List<TopoFolder> getTopoFolderByGroup(Long userGroupId) {
        return getSqlSession().selectList(getNameSpace("getTopoFolderByGroup"), userGroupId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#getTopoFolderByIp(java.lang.String)
     */
    @Override
    public List<TopoFolderEx> getTopoFolderByIp(String ip) {
        return getSqlSession().selectList(getNameSpace("getTopoFolderByIp"), ip);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#getTopoFolderByName(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public TopoFolder getTopoFolderByName(TopoFolder folder) {
        return getSqlSession().selectOne(getNameSpace("getTopoFolderByName"), folder);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#getFolderIdByName(java.lang.String)
     */
    @Override
    public Long getFolderIdByName(String topoName) {
        return getSqlSession().selectOne(getNameSpace("getFolderIdByName"), topoName);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.ibatis.IbatisDaoSupport#insertEntity(java.lang.Object)
     */
    @Override
    public void insertEntity(TopoFolder folder) {
        super.insertEntity(folder);
        String path = getFolderPath(folder.getSuperiorId());
        if (path == null) {
            path = "";
        }
        folder.setPath(path + folder.getFolderId() + "/");
        updateFolderPath(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#insertFolderUserGroup(com.topvision.ems.network.domain.FolderUserGroupRela)
     */
    @Override
    public void insertFolderUserGroup(FolderUserGroupRela rela) {
        getSqlSession().insert(getNameSpace() + "insertFolderUserGroup", rela);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#insertFolderUserGroup(java.util.List)
     */
    @Override
    public void insertFolderUserGroup(final List<FolderUserGroupRela> relas) {
        if (relas == null || relas.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = relas.size();
            for (int i = 0; i < size; i++) {
                sqlSession.insert(getNameSpace() + "insertFolderUserGroup", relas.get(i));
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
     * @see com.topvision.ems.network.dao.TopoFolderDao#loadMapNodeByFolderId(Long)
     */
    @Override
    public List<MapNode> loadMapNodeByFolderId(Long folderId) {
        // FIXME xml文件中没有loadMapNodeByFolderId
        return getSqlSession().selectList(getNameSpace("loadMapNodeByFolderId"), folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#loadSubnetFolder()
     */
    @Override
    public List<TopoFolder> loadSubnetFolder() {
        return getSqlSession().selectList(getNameSpace("loadSubnetFolder"));
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#loadTopoFolder()
     */
    @Override
    public List<TopoFolder> loadTopoFolder() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("AuthorityFolder", CurrentRequest.getUserAuthorityFolderName());
        return getSqlSession().selectList(getNameSpace("loadTopoFolder"), map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#loadTopoMap()
     */
    @Override
    public List<TopoFolder> loadTopoMap() {
        // Modify by Rod
        HashMap<String, String> authorityHashMap = new HashMap<String, String>();
        authorityHashMap.put("Authority", CurrentRequest.getUserAuthorityFolderName());
        return getSqlSession().selectList(getNameSpace("loadTopoMap"), authorityHashMap);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#renameTopoFolder(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void renameTopoFolder(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "renameTopoFolder", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#statTopoFolder(com.topvision.framework.event.MyResultHandler)
     */
    @Override
    public void statTopoFolder(MyResultHandler handler) {
        super.selectWithRowHandler(getNameSpace() + "statTopoFolder", handler);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateDisplayAlertIcon(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateDisplayAlertIcon(TopoFolder topoFolder) {
        getSqlSession().update(getNameSpace() + "updateDisplayAlertIcon", topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateDisplayAllEntity(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateDisplayAllEntity(TopoFolder topoFolder) {
        getSqlSession().update(getNameSpace() + "updateDisplayAllEntity", topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateDisplayLink(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateDisplayLink(TopoFolder topoFolder) {
        getSqlSession().update(getNameSpace() + "updateDisplayLink", topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateFolderBgPosition(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateFolderBgPosition(TopoFolder topoFolder) {
        getSqlSession().update(getNameSpace() + "updateFolderBgPosition", topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateFolderEntityLabel(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateFolderEntityLabel(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateFolderEntityLabel", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateFolderFixed(java.util.List)
     */
    @Override
    public void updateFolderFixed(final List<TopoFolder> folders) {
        if (folders == null || folders.size() == 0) {
            return;
        }
        SqlSession sqlSession = getBatchSession();
        try {
            int size = folders.size();
            for (int i = 0; i < size; i++) {
                sqlSession.insert(getNameSpace() + "updateFolderFixed", folders.get(i));
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
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateFolderFixed(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateFolderFixed(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateFolderFixed", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateFolderLinkLabel(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateFolderLinkLabel(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateFolderLinkLabel", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateFolderPath(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateFolderPath(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateFolderPath", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateMarkerAlertMode(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateMarkerAlertMode(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateMarkerAlertMode", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderBgColor(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderBgColor(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderBgColor", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderBgFlag(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderBgFlag(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderBgFlag", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderBgImg(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderBgImg(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderBgImg", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderDisplayCluetip(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayCluetip(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateDisplayCluetip", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderDisplayDesktop(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayDesktop(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderDisplayDesktop", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderDisplayEntityLabel(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayEntityLabel(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderDisplayEntityLabel", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderDisplayGrid(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayGrid(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderDisplayGrid", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderDisplayL3switch(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayL3switch(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderDisplayL3switch", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderDisplayLinkLabel(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayLinkLabel(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderDisplayLinkLabel", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderDisplayName(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayName(TopoFolder topoFolder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderDisplayName", topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderDisplayNoSnmp(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayNoSnmp(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderDisplayNoSnmp", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderDisplayOthers(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayOthers(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderDisplayOthers", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderDisplayRouter(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayRouter(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderDisplayRouter", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderDisplayServer(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayServer(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderDisplayServer", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderDisplaySwitch(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplaySwitch(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderDisplaySwitch", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderIcon(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderIcon(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderIcon", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderLinkColor(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderLinkColor(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderLinkColor", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderLinkShadow(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderLinkShadow(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderLinkShadow", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderLinkWidth(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderLinkWidth(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderLinkWidth", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderOrginEntity(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderOrginEntity(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderOrginEntity", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderOutline(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderOutline(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderOutline", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderRefreshInterval(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderRefreshInterval(TopoFolder folder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderRefreshInterval", folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#updateTopoFolderZoom(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderZoom(TopoFolder topoFolder) {
        getSqlSession().update(getNameSpace() + "updateTopoFolderZoom", topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.dao.TopoFolderDao#getDisplayNameType(java.lang.Long)
     */
    @Override
    public TopoFolder getDisplayNameType(Long folderId) {
        return getSqlSession().selectOne(getNameSpace("getDisplayNameType"), folderId);
    }

    public List<TopoFolder> getTopoFolderList(TopoFolder topoFolder) {
        return getSqlSession().selectList(getNameSpace("getTopoFolderList"), topoFolder);
    }

    public Integer getFodlerOtherNum(Long folderId) {
        Integer tmp1 = getSqlSession().selectOne(getNameSpace("getEntityNumByFolder"), folderId);
        Integer tmp2 = getSqlSession().selectOne(getNameSpace("getFodlerOtherNum"), folderId);
        return tmp1 - tmp2;
    }

    /**
     * 创建设备视图
     * 
     * @Add by Rod
     * 
     * @param databaseType
     * @param folderId
     */
    private void createTopoFolderView(String databaseType, Long folderId) {
        String topoViewName = "V_Topo_" + folderId;
        StringBuilder topoViewSqlBuilder = new StringBuilder("create view ");
        if (databaseType.equalsIgnoreCase(DataBaseConstants.MYSQL)) {
            // Mysql通过Function实现地域的递归查询
            topoViewSqlBuilder.append(topoViewName);
            topoViewSqlBuilder.append(" as select * from topofolder where find_in_set(folderId, topoFolderFun(");
            topoViewSqlBuilder.append(folderId);
            topoViewSqlBuilder.append("))");
        } else if (databaseType.equalsIgnoreCase(DataBaseConstants.ORACLE)) {
            // TODO
        }
        // 创建设备视图
        // getSqlMapClientTemplate().update(getNameSpace() + "createAuthorityView",
        // entityViewSqlBuilder.toString());
        // 创建地域视图
        getSqlSession().update(getNameSpace() + "createAuthorityView", topoViewSqlBuilder.toString());
    }

    /*
     * @Add by Rod (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.network.dao.TopoFolderDao#insertTopoFolder(com.topvision.ems.network.domain
     * .TopoFolder)
     */
    @Override
    public Long insertTopoFolder(TopoFolder topoFolder) throws SQLException {
        getSqlSession().insert(getNameSpace() + "insertEntity", topoFolder);
        Long folderId = topoFolder.getFolderId();
        // String databaseType =
        // getDataSource().getConnection().getMetaData().getDatabaseProductName();
        String databaseType = getSqlSession().getConfiguration().getDatabaseId();
        createTopoFolderView(databaseType, folderId);
        createEntityTopoFolderTable(databaseType, folderId);
        return folderId;
    }

    /**
     * 创建地域视图与设备视图
     * 
     * @Add by Rod
     * 
     * @param databaseType
     * @param folderId
     */
    private void createEntityTopoFolderTable(String databaseType, Long folderId) {
        String entityTableName = "T_Entity_" + folderId;
        if (databaseType.equalsIgnoreCase(DataBaseConstants.MYSQL)) {
            getSqlSession().update(getNameSpace() + "createAuthorityTable", entityTableName);
        } else if (databaseType.equalsIgnoreCase(DataBaseConstants.ORACLE)) {
        }
        // 创建设备权限表
        // getSqlMapClientTemplate().update(getNameSpace() + "createAuthorityTable",
        // entityTableSqlBuilder.toString());
        // 创建设备权限索引
        // getSqlMapClientTemplate().update(getNameSpace() + "createAuthorityTableIndex",
        // entityTableIndexSqlBuilder.toString());
    }

    @Override
    public boolean hasUser(Long folderId) {
        List<Integer> userIds = getSqlSession().selectList(getNameSpace("statUserIdByFolderId"), folderId);
        if (userIds != null && userIds.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public TopoFolder queryFolderIdAndName(Long entityId) {
        return this.getSqlSession().selectOne(getNameSpace("getFolderIdAndName"), entityId);
    }

    @Override
    public void updateEntityTopoFolder(Long entityId, Integer folderId) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("entityId", entityId);
        params.put("folderId", folderId);
        getSqlSession().update(getNameSpace("updateEntityTopoFolder"), params);
    }

    @Override
    public List<TopoFolder> queryTopoFolderByEntityId(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("queryTopoFolderByEntityId"), entityId);
    }

    @Override
    public List<Long> getEntityLocatedFolderIds(Long entityId) {
        return this.getSqlSession().selectList(getNameSpace("getEntityLocatedFolderIds"), entityId);
    }

    @Override
    public List<Entity> loadEntitiesWithFolderInfo(Map<String, String> params) {
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return this.getSqlSession().selectList(getNameSpace("loadEntitiesWithFolderInfo"), params);
    }

    @Override
    public Long getEntityListCount(Map<String, String> params) {
        params.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return this.getSqlSession().selectOne(getNameSpace("getEntitiesWithFolderInfoCount"), params);
    }

    @Override
    public List<TopoFolder> getTopoFolderByIds(List<Long> userAutoFolderIds) {
        return this.getSqlSession().selectList(getNameSpace("getTopoFolderByIds"), userAutoFolderIds);
    }

    @Override
    public List<TopoFolder> loadAllFolders() {
        return this.getSqlSession().selectList(getNameSpace("loadAllFolders"));
    }

    @Override
    public List<TopoFolder> fetchUserAuthFolders(Long userId) {
        return this.getSqlSession().selectList(getNameSpace("fetchUserAuthFolders"), userId);
    }

    @Override
    public List<TopoFolder> fetchAllFolderWithCheckable(Long userId) {
        // 获取所有地域
        List<TopoFolder> folders = loadAllFolders();
        // 获取当前用户有权限访问的所有地域
        List<TopoFolder> authFolders = fetchUserAuthFolders(userId);
        // 将所有地域中，当前用户有权限访问的地域置为可选，不可访问置为不可选
        for (TopoFolder allFolder : folders) {
            boolean contains = false;
            for (TopoFolder authFolder : authFolders) {
                if (authFolder.getFolderId().equals(allFolder.getFolderId())) {
                    contains = true;
                    break;
                }
            }
            allFolder.setChkDisabled(!contains);
        }
        return folders;
    }

    @Override
    public List<Long> getUserAuthFolderIds(Long userId) {
        List<Long> folderIds = getSqlSession().selectList(getNameSpace("getUserAuthFolderIds"), userId);

        if (2L == userId) {
            List<TopoFolder> folders = loadAllFolders();
            for (TopoFolder folder : folders) {
                folderIds.add(folder.getFolderId());
            }
        }

        // 需要修复数据库不一致情况
        folderIds = statReportDao.getAuthFolderIds(folderIds);
        return folderIds;
    }

    @Override
    public List<String> getEntityNamesByIds(List<Long> entityIds) {
        return getSqlSession().selectList(getNameSpace("getEntityNamesByIds"), entityIds);
    }

    @Override
    public List<String> getFolderNamesByIds(List<Long> folderIds) {
        return getSqlSession().selectList(getNameSpace("getFolderNamesByIds"), folderIds);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.network.domain.TopoFolder";
    }

    @Override
    public String getTopoFolderNameById(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getTopoFolderNameById"), entityId);
    }

    @Override
    public FolderRelation selectFolderRelationById(Long folderId) {
        return getSqlSession().selectOne(getNameSpace("selectFolderRelationById"), folderId);
    }

    @Override
    public List<FolderRelation> selectFolderRelationBySuperId(Long superiorId) {
        return getSqlSession().selectList(getNameSpace("selectFolderRelationBySuperId"), superiorId);
    }

    @Override
    public List<FolderRelation> selectFolderRelationByEntityId(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectFolderRelationByEntityId"), entityId);
    }
}
