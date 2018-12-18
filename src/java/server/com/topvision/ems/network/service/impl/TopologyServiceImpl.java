package com.topvision.ems.network.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.dao.EntityDao;
import com.topvision.ems.network.dao.LinkDao;
import com.topvision.ems.network.dao.MapNodeDao;
import com.topvision.ems.network.dao.TopoFolderDao;
import com.topvision.ems.network.dao.TopologyDao;
import com.topvision.ems.network.dao.VirtualNetDao;
import com.topvision.ems.network.domain.FolderRelation;
import com.topvision.ems.network.domain.FolderUserGroupRela;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.MapNode;
import com.topvision.ems.network.domain.ModuleParam;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.domain.TopoFolderEx;
import com.topvision.ems.network.domain.TopoFolderNum;
import com.topvision.ems.network.domain.TopoFolderStat;
import com.topvision.ems.network.domain.TopoLabel;
import com.topvision.ems.network.domain.VirtualNetAttribute;
import com.topvision.ems.network.service.GetTopoFolderNum;
import com.topvision.ems.network.service.NetworkConstants;
import com.topvision.ems.network.service.StarLayout;
import com.topvision.ems.network.service.TWaverLayout;
import com.topvision.ems.network.service.TopologyService;
import com.topvision.ems.report.service.StatReportService;
import com.topvision.ems.template.service.EntityTypeService;
import com.topvision.framework.event.MyResultHandler;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.dao.SystemLogDao;
import com.topvision.platform.dao.UserDao;
import com.topvision.platform.domain.SystemLog;
import com.topvision.platform.domain.UserEx;
import com.topvision.platform.domain.UserGroup;

public class TopologyServiceImpl extends BaseService implements TopologyService, BeanFactoryAware {
    private static Logger logger = LoggerFactory.getLogger(TopologyServiceImpl.class);
    private TopoFolderDao topoFolderDao;
    private SystemLogDao systemLogDao;
    private EntityDao entityDao;
    private VirtualNetDao virtualNetDao;
    private LinkDao linkDao;
    private TopologyDao topologyDao;
    private MapNodeDao mapNodeDao;
    private EntityTypeService entityTypeService;
    private final Map<String, List<TopoLabel>> topoLabelMapping = new Hashtable<String, List<TopoLabel>>();
    private final Map<Long, String> entityLabelMapping = new Hashtable<Long, String>();
    private final Map<Long, String> linkLabelMapping = new Hashtable<Long, String>();
    private static final List<ModuleParam> modules = new ArrayList<ModuleParam>();
    private ConcurrentHashMap<Long, Integer> folderLevel = new ConcurrentHashMap<Long, Integer>();
    protected BeanFactory beanFactory;
    private ResourceManager pltRM = ResourceManager.getResourceManager("com.topvision.platform.resources");
    @Autowired
    public UserDao userDao;
    @Autowired
    public StatReportService statReportService;

    @Override
    public void start() {
        super.start();
        reloadFolderLevel();
    };

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#arrangeTopoMap(Long, Integer, Integer,
     *      Integer)
     */
    @Override
    public List<Entity> arrangeTopoMap(Long folderId, Integer type, Integer mapWidth, Integer mapHeight) {
        List<Entity> entities = topologyDao.getNodeCoordByFolderId(folderId);
        List<Link> links = topologyDao.getEdgeByFolderId(folderId);
        if (type > 0 && type < 8) {
            try {
                TWaverLayout layout = new TWaverLayout();
                layout.doLayout(mapHeight, mapWidth, new ArrayList<Entity>(), links, entities, type);
            } catch (Exception e) {
                // 在linux非图形化模式下会报java.awt.HeadlessException
                logger.error(e.toString());
                logger.debug("arrangeTopoMap", e);
                StarLayout layout = new StarLayout();
                layout.doLayout(mapHeight, mapWidth, new ArrayList<Entity>(), links, entities);
            }
        } else {
            StarLayout layout = new StarLayout();
            layout.doLayout(mapHeight, mapWidth, new ArrayList<Entity>(), links, entities);
        }

        return entities;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#cancelTopology(Long)
     */
    @Override
    public void cancelTopology(Long requestId) {

    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#deleteMapNode(Long, java.util.List)
     */
    @Override
    public void deleteMapNode(Long folderId, List<Long> nodeIds) {
        mapNodeDao.deleteByPrimaryKey(nodeIds);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#deleteMapNode(Long, Long)
     */
    @Override
    public void deleteMapNode(Long folderId, Long nodeId) {
        mapNodeDao.deleteByPrimaryKey(nodeId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getChildTopoFolder(Long)
     */
    @Override
    public List<TopoFolder> getChildTopoFolder(Long superiorId) {
        return topoFolderDao.getChildTopoFolder(superiorId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getEdgeByFolderId(Long)
     */
    @Override
    public List<Link> getEdgeByFolderId(Long folderId) {
        return topologyDao.getEdgeByFolderId(folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getEdgeIdByFolderId(Long)
     */
    @Override
    public List<Long> getEdgeIdByFolderId(Long folderId) {
        return topologyDao.getEdgeIdByFolderId(folderId);
    }

    /**
     * @return the entityDao
     */
    public EntityDao getEntityDao() {
        return entityDao;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getEntityIdByFolderId(Long)
     */
    @Override
    public List<Long> getEntityIdByFolderId(Long folderId) {
        return entityDao.getEntityIdByFolderId(folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getEntityLabelType(Long)
     */
    @Override
    public String getEntityLabelType(Long folderId) {
        return entityLabelMapping.get(folderId);
    }

    /**
     * @return the linkDao
     */
    public LinkDao getLinkDao() {
        return linkDao;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getLinkIdByFolderId(Long)
     */
    @Override
    public List<Long> getLinkIdByFolderId(Long folderId) {
        return linkDao.getLinkIdByFolderId(folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getLinkLabelType(Long)
     */
    @Override
    public String getLinkLabelType(Long folderId) {
        return linkLabelMapping.get(folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getMapNode(Long)
     */
    @Override
    public MapNode getMapNode(Long nodeId) {
        return mapNodeDao.selectByPrimaryKey(nodeId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getTopoFolder(Long)
     */
    @Override
    public TopoFolder getTopoFolder(Long folderId) {
        TopoFolder folder = topoFolderDao.selectByPrimaryKey(folderId);
        entityLabelMapping.put(folderId, folder.getEntityLabel());
        linkLabelMapping.put(folderId, folder.getLinkLabel());

        return folder;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getTopoFolderByGroup(Long)
     */
    @Override
    public List<TopoFolder> getTopoFolderByGroup(Long userGroupId) {
        return topoFolderDao.getTopoFolderByGroup(userGroupId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getTopoFolderByIp(java.lang.String)
     */
    @Override
    public List<TopoFolderEx> getTopoFolderByIp(String ip) {
        return topoFolderDao.getTopoFolderByIp(ip);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getTopoLabel(Long, java.lang.String)
     */
    @Override
    public List<TopoLabel> getTopoLabel(Long folderId, String type) {
        return topoLabelMapping.get(type);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getVertexByFolderId(Long)
     */
    @Override
    public List<Entity> getVertexByFolderId(Long folderId) {
        return topologyDao.getVertexByFolderId(folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.service.BaseService#initialize()
     */
    @Override
    public void initialize() {
        super.initialize();
        try {
            List<TopoLabel> labels = new ArrayList<TopoLabel>();
            labels = topologyDao.getTopoLabels();

            TopoLabel label;
            List<TopoLabel> list;
            for (int i = 0; i < labels.size(); i++) {
                label = labels.get(i);
                if (topoLabelMapping.containsKey(label.getModule())) {
                    list = topoLabelMapping.get(label.getModule());
                } else {
                    list = new ArrayList<TopoLabel>();
                    topoLabelMapping.put(label.getModule(), list);
                }

                list.add(label);
            }
        } catch (DataAccessException dae) {
            logger.error("initialize error:", dae);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#insertMapNode(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void insertMapNode(MapNode mapNode) {
        mapNodeDao.insertEntity(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#loadJSONCellByFolderId(Long)
     */
    @Override
    public JSONObject loadJSONCellByFolderId(Long folderId) throws Exception {
        JSONObject json = new JSONObject();
        List<Entity> entities = getVertexByFolderId(folderId);
        int size = entities == null ? 0 : entities.size();
        JSONObject temp = null;
        if (size > 0) {
            Entity entity = null;
            JSONArray array = new JSONArray();
            for (int i = 0; i < size; i++) {
                temp = new JSONObject();
                entity = entities.get(i);
                temp.put("id", "cell" + entity.getEntityId());
                temp.put("nodeId", entity.getEntityId());
                temp.put("text", entity.getDisplayName());
                temp.put("x", entity.getX());
                temp.put("y", entity.getY());
                temp.put("icon", entity.getIcon());
                temp.put("url", entity.getUrl());
                temp.put("fixed", entity.getFixed());
                temp.put("groupId", entity.getGroupId());
                temp.put("objType", NetworkConstants.TYPE_ENTITY);
                temp.put("userObjId", entity.getEntityId());
                temp.put("status", entity.isStatus());
                temp.put("parentId", entity.getParentId());
                temp.put("name", entity.getShowName());
                String showSysName = entity.getShowSysName();
                if (showSysName == null || showSysName.equals("")) {
                    showSysName = pltRM.getString("noName");
                }
                temp.put("sysName", showSysName);
                String ip = entity.getIp();
                if (ip == null || ip.equals("")) {
                    ip = pltRM.getString("noIp");
                }
                temp.put("ip", ip);
                temp.put("mac", entity.getMac());
                temp.put("type", entityTypeService.getEntityNetworkGroupIdByEntityTypeId(entity.getTypeId()).intValue());
                temp.put("typeId", entity.getTypeId());
                temp.put("module", entity.getModule());
                temp.put("modulePath", entity.getModulePath());
                temp.put("snmpSupport", entity.isSnmpSupport());
                // temp.put("agentSupport", entity.isAgentSupport());
                temp.put("virtualNetworkStatus", entity.getVirtualNetworkStatus());
                array.add(temp);
            }
            json.put("entity", array);
        }

        List<MapNode> mapNodes = loadMapNodeByFolderId(folderId);
        size = mapNodes == null ? 0 : mapNodes.size();
        if (size > 0) {
            JSONArray array = new JSONArray();
            JSONArray array1 = new JSONArray();
            MapNode node = null;
            for (int i = 0; i < size; i++) {
                temp = new JSONObject();
                node = mapNodes.get(i);
                temp.put("id", "cell" + node.getNodeId());
                temp.put("nodeId", node.getNodeId());
                temp.put("text", node.getText());
                temp.put("url", node.getUrl());
                temp.put("x", node.getX());
                temp.put("y", node.getY());
                temp.put("icon", node.getIcon());
                temp.put("width", node.getWidth());
                temp.put("height", node.getHeight());
                temp.put("groupId", node.getGroupId());
                temp.put("fixed", node.getFixed());
                temp.put("name", node.getText());
                temp.put("objType", node.getUserObjType());
                temp.put("userObjId", node.getUserObjId());
                temp.put("type", node.getType());
                temp.put("strokeWeight", node.getStrokeWeight());
                temp.put("strokeColor", node.getStrokeColor());
                temp.put("fillColor", node.getFillColor());
                temp.put("gradient", node.getGradient());
                temp.put("gradientColor", node.getGradientColor());
                temp.put("fontColor", node.getTextColor());
                temp.put("fontSize", node.getFontSize());
                temp.put("shadow", node.getShadow());
                temp.put("dashed", node.getDashed());
                temp.put("collapsed", !node.getExpanded().booleanValue());

                if (node.getUserObjType() == NetworkConstants.TYPE_FOLDER) {
                    array1.add(temp);
                } else {
                    array.add(temp);
                }
            }
            json.put("node", array);
            json.put("folder", array1);
        }

        List<Link> links = getEdgeByFolderId(folderId);
        size = links == null ? 0 : links.size();
        if (size > 0) {
            JSONArray array = new JSONArray();
            for (int i = 0; i < size; i++) {
                temp = new JSONObject();
                Link link = links.get(i);
                temp.put("id", "edge" + link.getLinkId());
                temp.put("nodeId", link.getLinkId());
                temp.put("text", link.getName());
                temp.put("connectType", link.getConnectType());
                temp.put("objType", NetworkConstants.TYPE_LINK);
                temp.put("userObjId", link.getLinkId());
                temp.put("name", link.getName());
                temp.put("srcEntityId", link.getSrcEntityId());
                temp.put("destEntityId", link.getDestEntityId());
                temp.put("startArrow", link.isStartArrow());
                temp.put("endArrow", link.isEndArrow());
                temp.put("dashed", link.isDashed());
                array.add(temp);
            }
            json.put("link", array);
        }

        List<VirtualNetAttribute> virtualNetAttributeList = getVirtualNetByFolderId(folderId);
        size = virtualNetAttributeList == null ? 0 : virtualNetAttributeList.size();
        if (size > 0) {
            JSONArray array = new JSONArray();
            for (int i = 0; i < size; i++) {
                temp = new JSONObject();
                VirtualNetAttribute virtualNetAttribute = virtualNetAttributeList.get(i);
                if (1 == virtualNetAttribute.getVirtualType()) {
                    continue;
                }
                temp.put("id", "" + virtualNetAttribute.getVirtualNetId());
                temp.put("folderId", virtualNetAttribute.getFolderId());
                temp.put("nodeId", virtualNetAttribute.getVirtualNetId());
                temp.put("name", virtualNetAttribute.getVirtualName());
                temp.put("text", virtualNetAttribute.getVirtualName());
                temp.put("type", virtualNetAttribute.getVirtualType());
                temp.put("createTime", virtualNetAttribute.getCreateTime());
                temp.put("modifyTime", virtualNetAttribute.getModifyTime());
                temp.put("x", virtualNetAttribute.getX());
                temp.put("y", virtualNetAttribute.getY());
                temp.put("width", virtualNetAttribute.getWidth());
                temp.put("height", virtualNetAttribute.getHeight());
                temp.put("zoom", virtualNetAttribute.getZoom());
                temp.put("icon", virtualNetAttribute.getIcon());
                temp.put("fixed", virtualNetAttribute.getFixed());
                temp.put("visiable", virtualNetAttribute.getVisiable());
                temp.put("userObjId", virtualNetAttribute.getVirtualNetId());
                temp.put("objType", 3);
                array.add(temp);
            }
            json.put("virtualNet", array);
        }
        return json;
    }

    public List<VirtualNetAttribute> getVirtualNetByFolderId(Long folderId) {
        return virtualNetDao.getVirtualNetByFolderId(folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#loadMapNodeByFolderId(Long)
     */
    @Override
    public List<MapNode> loadMapNodeByFolderId(Long folderId) {
        return mapNodeDao.getMapNodes(folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#loadMyTopoFolder(Long, Integer)
     */
    @Override
    public List<TopoFolder> loadMyTopoFolder(Long userId, Integer type) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("categoryId", type);

        return topoFolderDao.getMyTopoFolder(map);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#loadSubnetFolder()
     */
    @Override
    public List<TopoFolder> loadSubnetFolder() {
        return topoFolderDao.loadSubnetFolder();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#loadTopoFolder()
     */
    @Override
    public List<TopoFolder> loadTopoFolder() {
        return topoFolderDao.loadTopoFolder();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#loadTopoFolder(com.topvision.framework.event.MyResultHandler)
     */
    @Override
    public void loadTopoFolder(MyResultHandler handler) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#loadTopoMap()
     */
    @Override
    public List<TopoFolder> loadTopoMap() {
        return topoFolderDao.loadTopoMap();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#refreshTopoFolder(Long)
     */
    @Override
    public void refreshTopoFolder(Long folderId) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#removeEntityAndLink(Long,
     *      java.util.List, java.util.List)
     */
    @Override
    public List<Long> removeEntityAndLink(Long folderId, List<Long> entityIds, List<Long> linkIds) {
        // 找到设备相关联的边
        List<Long> relaLinkIds = linkDao.getLinkIdByEntityIds(entityIds);
        if (linkIds == null) {
            linkIds = new ArrayList<Long>();
        }
        // 找出所有在当前地域的所有连线，如果设备相关联的边不在当前地域，则不删除
        List<Long> folderLinIds = linkDao.getLinkIdByFolderId(folderId);
        relaLinkIds.retainAll(folderLinIds);
        linkIds.addAll(relaLinkIds);
        // ----删除边----//
        linkDao.deleteByPrimaryKey(linkIds);// DELETE FROM Link WHERE linkId = #linkId#
        // ----删除设备，entityIds为null则返回
        List<Entity> entities = new ArrayList<Entity>();
        int size = entityIds == null ? 0 : entityIds.size();
        Entity ex = null;
        for (int i = 0; i < size; i++) {
            ex = new Entity();
            ex.setEntityId(entityIds.get(i));
            ex.setFolderId(folderId);
            entities.add(ex);
        }
        // ---如果entities为空则直接返回——---//
        // TODO 查看代码才发现，这个方法是删除设备与地域的关系的，不明白为什么命名为deleteEntity，错误导向太明显 by fanzidong
        entityDao.deleteEntity(entities);
        return relaLinkIds;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#saveUserGroupFolderPower(Long,
     *      java.util.List, java.util.List)
     */
    @Override
    public void saveUserGroupFolderPower(Long userGroupId, List<Long> folderIds, List<Integer> powers) {
        List<FolderUserGroupRela> relas = new ArrayList<FolderUserGroupRela>();
        int size = folderIds == null ? 0 : folderIds.size();
        FolderUserGroupRela rela = null;
        for (int i = 0; i < size; i++) {
            rela = new FolderUserGroupRela();
            rela.setFolderId(folderIds.get(i));
            rela.setUserGroupId(userGroupId);
            relas.add(rela);
        }

        // topoFolderDao.deleteFolderUserGroup(userGroupId);
        // topoFolderDao.insertFolderUserGroup(relas);
    }

    /**
     * @param entityDao
     *            the entityDao to set
     */
    public void setEntityDao(EntityDao entityDao) {
        this.entityDao = entityDao;
    }

    /**
     * @param linkDao
     *            the linkDao to set
     */
    public void setLinkDao(LinkDao linkDao) {
        this.linkDao = linkDao;
    }

    public void setMapNodeDao(MapNodeDao mapNodeDao) {
        this.mapNodeDao = mapNodeDao;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#setMapNodeExpaned(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void setMapNodeExpaned(MapNode node) {
    }

    /**
     * @param systemLogDao
     *            the systemLogDao to set
     */
    public void setSystemLogDao(SystemLogDao systemLogDao) {
        this.systemLogDao = systemLogDao;
    }

    /**
     * @param topoFolderDao
     *            the topoFolderDao to set
     */
    public void setTopoFolderDao(TopoFolderDao topoFolderDao) {
        this.topoFolderDao = topoFolderDao;
    }

    /**
     * @param topologyDao
     *            the topologyDao to set
     */
    public void setTopologyDao(TopologyDao topologyDao) {
        this.topologyDao = topologyDao;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#statTopoFolder()
     */
    @Override
    public List<TopoFolderStat> statTopoFolder() throws Exception {
        List<TopoFolderStat> re = topoFolderDao.getStatTopoFolder();
        Map<Long, List<TopoFolderNum>> mainResult = new HashMap<Long, List<TopoFolderNum>>();
        Collections.sort(modules);
        for (ModuleParam moduleParam : modules) {
            GetTopoFolderNum method = (GetTopoFolderNum) beanFactory.getBean(moduleParam.getBeanName());
            Map<Long, List<TopoFolderNum>> moduleResult = method.getTopoFolder(re);
            for (Long folderNum : moduleResult.keySet()) {
                List<TopoFolderNum> topoFolderNums;
                if (mainResult.containsKey(folderNum)) {
                    topoFolderNums = mainResult.get(folderNum);
                } else {
                    topoFolderNums = new ArrayList<TopoFolderNum>();
                    mainResult.put(folderNum, topoFolderNums);
                }
                topoFolderNums.addAll(moduleResult.get(folderNum));
            }
        }
        for (TopoFolderStat topoFolderStat : re) {
            if (mainResult.containsKey(topoFolderStat.getFolderId())) {
                topoFolderStat.setCols(mainResult.get(topoFolderStat.getFolderId()));
            }
        }
        return re;
    }

    @Override
    public List<String> getColNames() {
        List<String> re = new ArrayList<String>();
        Collections.sort(modules);
        for (ModuleParam moduleParam : modules) {
            GetTopoFolderNum method = (GetTopoFolderNum) beanFactory.getBean(moduleParam.getBeanName());
            List<String> moduleResult = method.getColNames();
            re.addAll(moduleResult);
        }
        return re;
    }

    @Override
    public void registerTopoFolderNumModuleParam(ModuleParam moduleParam) {
        modules.add(moduleParam);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#txCopyEntity(com.topvision.platform.domain.SystemLog,
     *      Long, Long, java.util.List)
     */
    @Override
    public List<Entity> txCopyEntity(SystemLog sysLog, Long srcFolderId, Long destFolderId, List<Long> entityIds) {
        // 获取目标文件夹没有的设备ID, 即需要被复制到目标文件夹下的设备ID
        List<Long> ids = entityDao.getEntityIdByFolderId(destFolderId);
        if (ids != null && ids.size() > 0) {
            entityIds.removeAll(ids);
        }
        int size = entityIds.size();
        if (size > 0) {
            // 获取需要被复制的设备信息
            List<Entity> entities = entityDao.getEntityByFolderId(entityIds, srcFolderId);
            List<Entity> entitiesEx = new ArrayList<Entity>();
            for (int i = 0; i < size; i++) {
                Entity entity = entities.get(i);
                entity.setFolderId(destFolderId);
                entitiesEx.add(entity);
            }
            // 插入被复制设备在目的文件夹下的关联
            entityDao.copyEntity(entitiesEx);

            return entities;
        }

        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#txCutEntity(com.topvision.platform.domain.SystemLog,
     *      Long, Long, java.util.List)
     */
    @Override
    public List<Entity> txCutEntity(SystemLog sysLog, Long srcFolderId, Long destFolderId, List<Long> entityIds) {
        // 获取目标文件夹没有的设备ID, 即需要被复制到目标文件夹下的设备ID
        List<Long> ids = entityDao.getEntityIdByFolderId(destFolderId);
        if (ids != null && ids.size() > 0) {
            entityIds.removeAll(ids);
        }
        int size = entityIds.size();
        if (size > 0) {
            // 获取需要被复制的设备信息
            List<Entity> entities = entityDao.getEntityByFolderId(entityIds, srcFolderId);
            List<Entity> entitiesEx = new ArrayList<Entity>();
            for (int i = 0; i < size; i++) {
                Entity entity = entities.get(i);
                entity.setFolderId(destFolderId);
                entitiesEx.add(entity);
            }
            // 插入被复制设备在目的文件夹下的关联
            entityDao.moveEntity(srcFolderId, destFolderId, entityIds);

            return entities;
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#txDeleteTopoFolder(com.topvision.platform.domain.SystemLog,
     *      com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void txDeleteTopoFolder(SystemLog sysLog, TopoFolder folder) {
        // String path = topoFolderDao.getFolderPath(folder.getFolderId());
        linkDao.deleteLinkByEntityId(folder.getFolderId());
        mapNodeDao.deleteMapNodeByFolder(folder.getFolderId());
        topoFolderDao.deleteTopoFolder(folder.getFolderId());
        reloadFolderLevel();
        if (sysLog != null) {
            sysLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
            systemLogDao.insertEntity(sysLog);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @throws SQLException
     * 
     * @see com.topvision.ems.network.service.TopologyService#txInsertTopoFolder(com.topvision.platform.domain.SystemLog,
     *      com.topvision.ems.network.domain.TopoFolder, Long)
     */
    @Override
    public void txInsertTopoFolder(SystemLog sysLog, TopoFolder folder, Long userGroupId) throws SQLException {
        Timestamp st = new Timestamp(System.currentTimeMillis());
        folder.setCreateTime(st);
        folder.setModifyTime(st);
        topoFolderDao.insertTopoFolder(folder);
        reloadFolderLevel();
        if (folder.getSuperiorId() > 0) {
            MapNode node = new MapNode();
            node.setUserObjType(NetworkConstants.TYPE_FOLDER);
            node.setText(folder.getName());
            node.setUserObjId(folder.getFolderId());
            node.setFolderId(folder.getSuperiorId());
            node.setIcon(folder.getIcon());
            node.setType(200);
            mapNodeDao.insertEntity(node);
        }

        FolderUserGroupRela rela = new FolderUserGroupRela();
        rela.setFolderId(folder.getFolderId());
        rela.setUserGroupId(UserGroup.DEFAULT_SUPER_GROUP);

        if (userGroupId == UserGroup.DEFAULT_SUPER_GROUP) {
            // topoFolderDao.insertFolderUserGroup(rela);
        } else {
            FolderUserGroupRela rela1 = new FolderUserGroupRela();
            rela.setFolderId(folder.getFolderId());
            rela.setUserGroupId(userGroupId);

            List<FolderUserGroupRela> relas = new ArrayList<FolderUserGroupRela>();
            relas.add(rela);
            relas.add(rela1);

            // topoFolderDao.insertFolderUserGroup(relas);
        }

        if (sysLog != null) {
            sysLog.setCreateTime(st);
            systemLogDao.insertEntity(sysLog);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#txMoveEntity(Long, java.util.List)
     */
    @Override
    public void txMoveEntity(Long destFolderId, List<Long> entityIds) {
        int size = entityIds == null ? 0 : entityIds.size();
        if (size == 0) {
            return;
        }
        List<Entity> entities = new ArrayList<Entity>();
        for (int i = 0; i < size; i++) {
            Entity entity = new Entity();
            entity.setEntityId(entityIds.get(i));
            entity.setFolderId(destFolderId);
            entity.setX((int) (Math.random() * 600));
            entity.setY((int) (Math.random() * 400));
            entities.add(entity);
        }

        entityDao.txMoveEntity(entities);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#txMoveEntity(com.topvision.platform.domain.SystemLog,
     *      Long, Long, java.util.List)
     */
    @Override
    public void txMoveEntity(SystemLog sysLog, Long srcFolderId, Long destFolderId, List<Long> entityIds) {
        // 删除源文件夹下的设备关联
        int size = entityIds == null ? 0 : entityIds.size();
        if (size == 0) {
            return;
        }
        List<Entity> entities = new ArrayList<Entity>();
        for (int i = 0; i < size; i++) {
            Entity entity = new Entity();
            entity.setEntityId(entityIds.get(i));
            entity.setFolderId(srcFolderId);
            entities.add(entity);
        }

        entityDao.deleteEntity(entities);

        // 获取需要移动到目的文件夹下的设备ID
        List<Long> ids = entityDao.getEntityIdByFolderId(destFolderId);
        if (ids != null && ids.size() > 0) {
            entityIds.removeAll(ids);
        }
        size = entityIds.size();
        if (size == 0) {
            return;
        }

        // 插入目的文件夹下的设备关联
        entities.clear();
        for (int i = 0; i < size; i++) {
            Entity entity = new Entity();
            entity.setEntityId(entityIds.get(i));
            entity.setFolderId(destFolderId);
            entity.setX((int) (Math.random() * 600));
            entity.setY((int) (Math.random() * 400));
            entities.add(entity);
        }
        entityDao.copyEntity(entities);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#txRenameEntity(com.topvision.ems.facade.domain.Entity)
     */
    @Override
    public void txRenameEntity(Entity entity) {
        entityDao.renameEntity(entity.getEntityId(), entity.getName());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#txRenameTopoFolder(com.topvision.platform.domain.SystemLog,
     *      com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void txRenameTopoFolder(SystemLog sysLog, TopoFolder folder) {
        Timestamp st = new Timestamp(System.currentTimeMillis());
        folder.setModifyTime(st);
        MapNode mapNode = new MapNode();
        mapNode.setText(folder.getName());
        mapNode.setUserObjId(folder.getFolderId());
        mapNode.setUserObjType(NetworkConstants.TYPE_FOLDER);

        topoFolderDao.renameTopoFolder(folder);
        mapNodeDao.updateUserObject(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#txSaveCoordinate(Long, java.util.List,
     *      java.util.List)
     */
    @Override
    public void txSaveCoordinate(Long folderId, List<Entity> entities, List<MapNode> nodes) {
        topologyDao.saveEntityCoordinate(entities);
        mapNodeDao.saveMapNodeCoordinate(nodes);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#txUpdateTopoFolderOutline(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void txUpdateTopoFolderOutline(TopoFolder folder) {
        Timestamp st = new Timestamp(System.currentTimeMillis());
        folder.setModifyTime(st);

        MapNode mapNode = new MapNode();
        mapNode.setUrl(folder.getUrl());
        mapNode.setText(folder.getName());
        mapNode.setUserObjId(folder.getFolderId());
        mapNode.setUserObjType(NetworkConstants.TYPE_FOLDER);

        topoFolderDao.updateTopoFolderOutline(folder);
        mapNodeDao.updateUserObject(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateDisplayAlertIcon(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateDisplayAlertIcon(TopoFolder folder) {
        topoFolderDao.updateDisplayAlertIcon(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateDisplayAllEntity(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateDisplayAllEntity(TopoFolder topoFolder) {
        topoFolderDao.updateDisplayAllEntity(topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateDisplayCluetip(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateDisplayCluetip(TopoFolder folder) {
        topoFolderDao.updateTopoFolderDisplayCluetip(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateDisplayLink(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateDisplayLink(TopoFolder topoFolder) {
        topoFolderDao.updateDisplayLink(topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateEntityGroup(com.topvision.ems.facade.domain.Entity)
     */
    @Override
    public void updateEntityGroup(Entity entity) {
        entityDao.updateEntityGroup(entity);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateEntityIcon(com.topvision.ems.facade.domain.Entity)
     */
    @Override
    public void updateEntityIcon(Entity entity) {
        entityDao.updateEntityIcon(entity);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeDashedBorder(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeDashedBorder(MapNode mapNode) {
        mapNodeDao.updateMapNodeDashedBorder(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeExpanded(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeExpanded(MapNode mapNode) {
        mapNodeDao.updateMapNodeExpanded(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeFillColor(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeFillColor(MapNode mapNode) {
        mapNodeDao.updateMapNodeFillColor(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeFixed(java.util.List)
     */
    @Override
    public void updateMapNodeFixed(List<MapNode> mapNodes) {
        mapNodeDao.updateMapNodeFixed(mapNodes);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeFixed(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeFixed(MapNode mapNode) {
        mapNodeDao.updateMapNodeFixed(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeFontColor(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeFontColor(MapNode mapNode) {
        mapNodeDao.updateMapNodeFontColor(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeFontSize(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeFontSize(MapNode mapNode) {
        mapNodeDao.updateMapNodeFontSize(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeGroup(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeGroup(MapNode mapNode) {
        mapNodeDao.updateMapNodeGroup(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeIcon(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeIcon(MapNode mapNode) {
        mapNodeDao.updateMapNodeIcon(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeShadow(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeShadow(MapNode mapNode) {
        mapNodeDao.updateMapNodeShadow(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeSize(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeSize(MapNode mapNode) {
        mapNodeDao.updateMapNodeSize(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeStrokeColor(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeStrokeColor(MapNode mapNode) {
        mapNodeDao.updateMapNodeStrokeColor(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeStrokeWeight(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeStrokeWeight(MapNode mapNode) {
        mapNodeDao.updateMapNodeStrokeWeight(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeText(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeText(MapNode mapNode) {
        mapNodeDao.updateMapNodeText(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMapNodeUrl(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeUrl(MapNode mapNode) {
        mapNodeDao.updateMapNodeUrl(mapNode);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateMarkerAlertMode(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateMarkerAlertMode(TopoFolder folder) {
        topoFolderDao.updateMarkerAlertMode(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolder(com.topvision.platform.domain.SystemLog,
     *      com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolder(SystemLog sysLog, TopoFolder folder) {
        Timestamp st = new Timestamp(System.currentTimeMillis());
        folder.setModifyTime(st);

        MapNode mapNode = new MapNode();
        mapNode.setUrl(folder.getUrl());
        mapNode.setText(folder.getName());
        mapNode.setUserObjId(folder.getFolderId());
        mapNode.setUserObjType(NetworkConstants.TYPE_FOLDER);

        topoFolderDao.updateEntity(folder);
        mapNodeDao.updateUserObject(mapNode);

        if (sysLog != null) {
            sysLog.setCreateTime(st);
            systemLogDao.insertEntity(sysLog);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderBgColor(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderBgColor(TopoFolder folder) {
        Timestamp st = new Timestamp(System.currentTimeMillis());
        folder.setModifyTime(st);

        topoFolderDao.updateTopoFolderBgColor(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderBgFlag(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderBgFlag(TopoFolder folder) {
        Timestamp st = new Timestamp(System.currentTimeMillis());
        folder.setModifyTime(st);

        topoFolderDao.updateTopoFolderBgFlag(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderBgImg(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderBgImg(TopoFolder folder) {
        Timestamp st = new Timestamp(System.currentTimeMillis());
        folder.setModifyTime(st);

        topoFolderDao.updateTopoFolderBgImg(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderBgPosition(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderBgPosition(TopoFolder topoFolder) {
        topoFolderDao.updateFolderBgPosition(topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderDisplayDesktop(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayDesktop(TopoFolder folder) {
        topoFolderDao.updateTopoFolderDisplayDesktop(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderDisplayEntityLabel(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayEntityLabel(TopoFolder folder) {
        topoFolderDao.updateTopoFolderDisplayEntityLabel(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderDisplayGrid(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayGrid(TopoFolder topoFolder) {
        topoFolderDao.updateTopoFolderDisplayGrid(topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderDisplayL3switch(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayL3switch(TopoFolder folder) {
        topoFolderDao.updateTopoFolderDisplayL3switch(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderDisplayLinkLabel(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayLinkLabel(TopoFolder topoFolder) {
        topoFolderDao.updateTopoFolderDisplayLinkLabel(topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderDisplayName(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayName(TopoFolder topoFolder) {
        topoFolderDao.updateTopoFolderDisplayName(topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderDisplayNoSnmp(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayNoSnmp(TopoFolder folder) {
        topoFolderDao.updateTopoFolderDisplayNoSnmp(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderDisplayOthers(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayOthers(TopoFolder folder) {
        topoFolderDao.updateTopoFolderDisplayOthers(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderDisplayRouter(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayRouter(TopoFolder folder) {
        topoFolderDao.updateTopoFolderDisplayRouter(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderDisplayServer(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplayServer(TopoFolder folder) {
        topoFolderDao.updateTopoFolderDisplayServer(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderDisplaySwitch(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderDisplaySwitch(TopoFolder folder) {
        topoFolderDao.updateTopoFolderDisplaySwitch(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderFixed(java.util.List)
     */
    @Override
    public void updateTopoFolderFixed(List<TopoFolder> folders) {
        topoFolderDao.updateFolderFixed(folders);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderFixed(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderFixed(TopoFolder folder) {
        topoFolderDao.updateFolderFixed(folder);
        MapNode mapNode = new MapNode();
        mapNode.setUserObjId(folder.getFolderId());
        mapNode.setUserObjType(NetworkConstants.TYPE_FOLDER);

        MapNode n = mapNodeDao.getMapNodeByObjId(mapNode);
        if (n != null) {
            mapNode.setNodeId(n.getNodeId());
            mapNode.setFixed(folder.getFixed());
            mapNodeDao.updateMapNodeFixed(mapNode);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderIcon(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderIcon(TopoFolder folder) {
        topoFolderDao.updateTopoFolderIcon(folder);

        MapNode mapNode = new MapNode();
        mapNode.setUserObjId(folder.getFolderId());
        mapNode.setUserObjType(NetworkConstants.TYPE_FOLDER);

        MapNode n = mapNodeDao.getMapNodeByObjId(mapNode);
        if (n != null) {
            mapNode.setNodeId(n.getNodeId());
            mapNode.setIcon(folder.getIcon());
            mapNodeDao.updateMapNodeIcon(mapNode);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderLinkColor(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderLinkColor(TopoFolder folder) {
        Timestamp st = new Timestamp(System.currentTimeMillis());
        folder.setModifyTime(st);

        topoFolderDao.updateTopoFolderLinkColor(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderLinkShadow(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderLinkShadow(TopoFolder topoFolder) {
        topoFolderDao.updateTopoFolderLinkShadow(topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderLinkWidth(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderLinkWidth(TopoFolder folder) {
        Timestamp st = new Timestamp(System.currentTimeMillis());
        folder.setModifyTime(st);

        topoFolderDao.updateTopoFolderLinkWidth(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderOrginEntity(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderOrginEntity(TopoFolder topoFolder) {
        topoFolderDao.updateTopoFolderOrginEntity(topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderRefreshInterval(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderRefreshInterval(TopoFolder folder) {
        Timestamp st = new Timestamp(System.currentTimeMillis());
        folder.setModifyTime(st);

        topoFolderDao.updateTopoFolderRefreshInterval(folder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoFolderZoom(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderZoom(TopoFolder topoFolder) {
        topoFolderDao.updateTopoFolderZoom(topoFolder);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#updateTopoLabel(Long,
     *      java.lang.String, java.util.List)
     */
    @Override
    public void updateTopoLabel(Long folderId, String type, List<TopoLabel> labels) {
        topologyDao.updateTopoLabel(labels);

        TopoFolder folder = new TopoFolder();
        folder.setFolderId(folderId);
        if (TopoLabel.TYPE_CPU.equals(type) || TopoLabel.TYPE_MEM.equals(type)) {
            folder.setEntityLabel(type);
            entityLabelMapping.put(folderId, type);
            topoFolderDao.updateFolderEntityLabel(folder);
        } else {
            linkLabelMapping.put(folderId, type);
            folder.setLinkLabel(type);
            topoFolderDao.updateFolderLinkLabel(folder);
        }
        @SuppressWarnings("unused")
        Object v = topoLabelMapping.remove(type);
        v = null;
        topoLabelMapping.put(type, labels);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getEntityByType(java .lang.Long)
     */
    @Override
    public List<Entity> getEntityByType(Long type) {
        return entityDao.getEntityByType(type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#hasChild(com.topvision
     * .ems.network.domain .TopoFolder)
     */
    @Override
    public boolean hasChild(TopoFolder folder) {
        if (topoFolderDao.getChildCount(folder.getFolderId()) > 0 || topoFolderDao.getEntityCountInFolder(folder) > 0) {
            return true;
        } else
            return false;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getDisplayNameType(java.lang.Long)
     */
    @Override
    public TopoFolder getDisplayNameType(Long folderId) {
        return topoFolderDao.getDisplayNameType(folderId);
    }

    @Override
    public List<TopoFolder> getTopoFolderList(TopoFolder folder) {
        return topoFolderDao.getTopoFolderList(folder);
    }

    /**
     * @return the virtualNetDao
     */
    public VirtualNetDao getVirtualNetDao() {
        return virtualNetDao;
    }

    /**
     * @param virtualNetDao
     *            the virtualNetDao to set
     */
    public void setVirtualNetDao(VirtualNetDao virtualNetDao) {
        this.virtualNetDao = virtualNetDao;
    }

    public BeanFactory getBeanFacotry() {
        return beanFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org
     * .springframework.beans .factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public boolean isRegionCanBeDeleted(TopoFolder folder) {
        // 判断该地域是否关联用户
        boolean hasUser = topoFolderDao.hasUser(folder.getFolderId());
        if (hasUser) {
            return false;
        }
        // 判断是否有子地域
        Integer subFolderNum = topoFolderDao.getChildCount(folder.getFolderId());
        if (subFolderNum > 0) {
            return false;
        }
        // 判断是否有关联设备
        Integer entityNum = topoFolderDao.getEntityCountInFolder(folder);
        if (entityNum > 0) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getOrInsertTopoFolderByName(java.lang.
     * String )
     */
    @Override
    public Long getOrInsertTopoFolderByName(String topoName) {
        Long superiorId = 10L;
        if (topoName == null) {
            return 10L;
        }
        if (topoName.length() == 0) {
            return 10L;
        }
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(topoName, "/");
            while (stringTokenizer.hasMoreElements()) {
                String token = stringTokenizer.nextToken();
                TopoFolder folder = new TopoFolder();
                folder.setSuperiorId(superiorId);
                folder.setName(token);
                folder = topoFolderDao.getTopoFolderByName(folder);
                if (folder != null) {
                    // 找到存在的地域
                    superiorId = folder.getFolderId();
                } else {
                    // 不存在对应的地域则新建
                    TopoFolder newFolder = new TopoFolder();
                    newFolder.setSuperiorId(superiorId);
                    newFolder.setName(token);
                    txInsertTopoFolder(null, newFolder, 1L);
                    superiorId = newFolder.getFolderId();
                }
            }
            return superiorId;
        } catch (Exception e) {
            logger.error("getOrInsertTopoFolderByName:", e);
            return 10L;
        }
    }

    @Override
    public List<String> getFolderNamesByIds(List<Long> folderIds) {
        if (folderIds == null || folderIds.size() == 0) {
            return null;
        }
        return topoFolderDao.getFolderNamesByIds(folderIds);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#getFolderIdByName(java.lang.String)
     */
    @Override
    public List<Long> getFolderIdByName(String topoName) {
        if (topoName == null) {
            return null;
        }
        if (topoName.length() == 0) {
            return null;
        }
        List<Long> folderIds = new ArrayList<>();
        String[] topoNames = topoName.split(",");
        Long folderId = null;
        for (String tmp : topoNames) {
            folderId = topoFolderDao.getFolderIdByName(tmp);
            if (folderId != null) {
                folderIds.add(folderId);
            }
        }
        return folderIds;
    }

    @Override
    public TopoFolder getFolderIdAndName(Long entityId) {
        return topoFolderDao.queryFolderIdAndName(entityId);
    }

    @Override
    public void updateEntityTopoFolder(Long entityId, Integer folderId) {
        List<Long> entityIdList = new ArrayList<Long>();
        entityIdList.add(entityId);
        topoFolderDao.updateEntityTopoFolder(entityId, folderId);
        entityDao.reOrganizedAuthority(entityIdList);
    }

    @Override
    public List<TopoFolder> getTopoFolderByEntityId(Long entityId) {
        return topoFolderDao.queryTopoFolderByEntityId(entityId);
    }

    @Override
    public List<Long> getEntityLocatedFolderIds(Long entityId) {
        List<Long> entityFolderIds = topoFolderDao.getEntityLocatedFolderIds(entityId);
        return entityFolderIds;
    }

    @Override
    public List<TopoFolder> fetchUserCurRootFolders(Long userId) {
        // 获取对应用户
        UserEx user = userDao.selectByPrimaryKey(userId);
        // 获取系统所有地域的集合
        List<Long> ids = new ArrayList<Long>();
        ids.add(user.getUserGroupId());
        List<TopoFolder> folders = checkFoldersOnAllFolders(userId, ids);
        return folders;
    }

    @Override
    public List<TopoFolder> fetchNetSegmentFolders(Long userId) {
        // 获取对应用户
        UserEx user = userDao.selectByPrimaryKey(userId);
        // 获取系统所有地域的集合
        List<TopoFolder> folders = topoFolderDao.loadAllFolders();
        // 获取用户当前根地域的权限集合
        List<Long> folderIds = new ArrayList<Long>();
        folderIds.add(user.getUserGroupId());
        List<Long> userAutoFolderIds = statReportService.getAuthFolderIds(folderIds);
        // 将可选地域置为可选
        boolean contains = false;
        for (TopoFolder folder : folders) {
            contains = userAutoFolderIds.contains(folder.getFolderId());
            folder.setChkDisabled(!contains);
        }
        return folders;
    }

    @Override
    public List<TopoFolder> fetchUserAuthFolders(Long userId) {
        return topoFolderDao.fetchUserAuthFolders(userId);
    }

    @Override
    public List<TopoFolder> checkFoldersOnAllFolders(Long userId, List<Long> folderIds) {
        // 获取指定用户权限下的所有地域集合
        List<TopoFolder> allFolders = topoFolderDao.fetchAllFolderWithCheckable(userId);
        // 遍历所有地域，勾选上传入地域
        for (TopoFolder allFolder : allFolders) {
            allFolder.setChecked(folderIds.contains(allFolder.getFolderId()));
        }
        return allFolders;
    }

    @Override
    public List<TopoFolder> fetchUserSwithableFolders(Long curUserId, Long editUserId) {
        // 获取系统所有地域的集合
        List<TopoFolder> folders = topoFolderDao.loadAllFolders();
        // 获取当前用户的可选根地域集合
        List<Long> authFolderIds = topoFolderDao.getUserAuthFolderIds(curUserId);
        // 获取待编辑用户的可选根地域集合
        List<Long> editUserFolderIds = topoFolderDao.getUserAuthFolderIds(editUserId);
        // 将当前用户可选根地域置为可选，其他地域置为不可选
        // 将待编辑用户的可选根地域勾选，其他地域不勾选
        for (TopoFolder folder : folders) {
            folder.setChkDisabled(!authFolderIds.contains(folder.getFolderId()));
            folder.setChecked(editUserFolderIds.contains(folder.getFolderId()));
        }
        return folders;
    }

    @Override
    public List<TopoFolder> fetchSwithRootFolders(Long userId) {
        // 获取对应用户
        UserEx user = userDao.selectByPrimaryKey(userId);
        // 获取系统所有地域的集合
        List<TopoFolder> folders = topoFolderDao.loadAllFolders();
        // 获取当前用户的可选根地域集合
        List<Long> authFolderIds = topoFolderDao.getUserAuthFolderIds(userId);
        // 将当前用户可选根地域置为可选，其他地域置为不可选
        // 将当前用户的当前根地域勾选，其他地域不勾选
        for (TopoFolder folder : folders) {
            folder.setChkDisabled(!authFolderIds.contains(folder.getFolderId()));
            folder.setChecked(user.getUserGroupId().equals(folder.getFolderId()));
        }
        return folders;
    }

    @Override
    public List<FolderRelation> getFolderRelationByEntityId(Long entityId) {
        List<FolderRelation> folderRelations = topoFolderDao.selectFolderRelationByEntityId(entityId);
        for (FolderRelation folderRelation : folderRelations) {
            if (folderLevel.containsKey(folderRelation.getFolderId())) {
                folderRelation.setLevel(folderLevel.get(folderRelation.getFolderId()));
            }
        }
        return folderRelations;
    }

    /**
     * 递归形成folder-level map
     * 
     * @param folderId
     * @param level
     * @return
     */
    private void recursiveFolder(Long folderId, Integer level) {
        // 根据folderId获取folderRelation
        FolderRelation node = topoFolderDao.selectFolderRelationById(folderId);
        node.setLevel(level);
        folderLevel.put(folderId, level);
        // 查询该folder下所有子folder
        List<FolderRelation> children = topoFolderDao.selectFolderRelationBySuperId(folderId);
        for (FolderRelation child : children) {
            recursiveFolder(child.getFolderId(), node.getLevel() + 1);
        }
    }

    private void reloadFolderLevel() {
        folderLevel.clear();
        recursiveFolder(10L, 1);// 默认地域folderId=10,优先级最高,level=1
        logger.info("folder-level-------" + folderLevel.toString() + "--------------");
    }

    public EntityTypeService getEntityTypeService() {
        return entityTypeService;
    }

    public void setEntityTypeService(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }
}
