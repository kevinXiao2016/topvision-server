package com.topvision.ems.network.service.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.Link;
import com.topvision.ems.network.domain.MapNode;
import com.topvision.ems.network.domain.TopoFolder;
import com.topvision.ems.network.message.LinkEvent;
import com.topvision.ems.network.message.LinkListener;
import com.topvision.ems.network.message.TopologyEvent;
import com.topvision.ems.network.message.TopologyListener;
import com.topvision.platform.domain.SystemLog;
import com.topvision.platform.message.event.EntityEvent;
import com.topvision.platform.message.event.EntityListener;
import com.topvision.platform.message.service.MessageService;

/**
 * 使用缓存拓扑中的设备, 线路和节点等信息来加载拓扑图, 为了提高显示拓扑图的速度.
 * 
 */
public class CacheTopologyServiceImpl extends TopologyServiceImpl implements TopologyListener, EntityListener,
        LinkListener {
    private final Map<Long, List<Entity>> topoFolderMapping = new Hashtable<Long, List<Entity>>();
    private final Map<Long, List<Link>> linkMapping = new Hashtable<Long, List<Link>>();
    private final Map<Long, JSONObject> jsonVertexMapping = new Hashtable<Long, JSONObject>();
    private final Map<Long, JSONObject> jsonEdgeMapping = new Hashtable<Long, JSONObject>();
    private MessageService messageService = null;

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#attributeChanged(Long,
     *      java.lang.String[], java.lang.String[])
     */
    @Override
    // TODO long --> Long
    public void attributeChanged(long entityId, String[] attrNames, String[] attrValues) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#deleteMapNode(Long, Long)
     */
    @Override
    public void deleteMapNode(Long folderId, Long nodeId) {
        super.deleteMapNode(folderId, nodeId);
        synJsonVertextCache(folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityAdded(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void entityAdded(EntityEvent e) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityDiscovered(com.topvision
     * .ems.message .event.EntityEvent)
     */
    @Override
    public void entityDiscovered(EntityEvent e) {
        Entity entity = e.getEntity();
        if (entity.getFolderId() != null && topoFolderMapping.containsKey(entity.getFolderId())) {
            List<Entity> entities = topoFolderMapping.get(entity.getFolderId());
            for (Entity ee : entities) {
                if (ee.getEntityId() == entity.getEntityId()) {
                    return;
                }
            }
            entities.add(entity);
        }

        synJsonVertextCache(entity.getFolderId());
        synJsonEdgeCache(entity.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityChanged(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void entityChanged(EntityEvent event) {
        Entity entity = event.getEntity();
        if (entity.getFolderId() != null && topoFolderMapping.containsKey(entity.getFolderId())) {
            List<Entity> entities = topoFolderMapping.get(entity.getFolderId());
            if (entities != null) {
                int size = entities.size();
                for (int i = 0; i < size; i++) {
                    if (entities.get(i).getEntityId() == entity.getEntityId()) {
                        if (event.getAction() == EntityEvent.FIXED) {
                            entities.get(i).setFixed(entity.getFixed());
                        } else if (event.getAction() == EntityEvent.URL) {
                            entities.get(i).setUrl(entity.getUrl());
                        }
                        break;
                    }
                }
            }
        }

        synJsonVertextCache(entity.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#entityRemoved(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void entityRemoved(EntityEvent event) {
        Entity entity = event.getEntity();
        if (entity.getFolderId() != null && topoFolderMapping.containsKey(entity.getFolderId())) {
            List<Entity> entities = topoFolderMapping.get(entity.getFolderId());
            for (int i = entities.size() - 1; i >= 0; i--) {
                if (entities.get(i).getEntityId() == entity.getEntityId()) {
                    entities.remove(i);
                    break;
                }
            }
        }

        synJsonVertextCache(entity.getFolderId());
        synJsonEdgeCache(entity.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#getEdgeByFolderId(Long)
     */
    @Override
    public List<Link> getEdgeByFolderId(Long folderId) {
        List<Link> links = null;
        if (linkMapping.containsKey(folderId)) {
            links = linkMapping.get(folderId);
        } else {
            links = Collections.synchronizedList(super.getEdgeByFolderId(folderId));
            linkMapping.put(folderId, links);
        }

        return links;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#getVertexByFolderId(Long)
     */
    @Override
    public List<Entity> getVertexByFolderId(Long folderId) {
        List<Entity> entities = null;
        if (topoFolderMapping.containsKey(folderId)) {
            entities = topoFolderMapping.get(folderId);
        } else {
            entities = Collections.synchronizedList(super.getVertexByFolderId(folderId));
            topoFolderMapping.put(folderId, entities);
        }
        return entities;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#initialize()
     */
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(TopologyListener.class, this);
        messageService.addListener(EntityListener.class, this);
        messageService.addListener(LinkListener.class, this);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#insertMapNode(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void insertMapNode(MapNode mapNode) {
        super.insertMapNode(mapNode);
        synJsonVertextCache(mapNode.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.LinkListener#linkAdded(com.topvision.ems.message.event.LinkEvent)
     */
    @Override
    public void linkAdded(LinkEvent evt) {
        List<Link> links = linkMapping.get(evt.getFolderId());
        if (links != null) {
            links.add(evt.getLink());
        }

        synJsonEdgeCache(evt.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.LinkListener#linkRemoved(com.topvision.ems.message.event.LinkEvent)
     */
    @Override
    public void linkRemoved(LinkEvent evt) {
        // 同步清除缓存
        List<Link> links = linkMapping.get(evt.getFolderId());
        if (links != null) {
            for (int i = links.size() - 1; i >= 0; i--) {
                if (links.get(i).getLinkId() == evt.getLink().getLinkId()) {
                    links.remove(i);
                    break;
                }
            }
        }
        synJsonEdgeCache(evt.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#loadJSONCellByFolderId(Long)
     */
    @Override
    public JSONObject loadJSONCellByFolderId(Long folderId) throws Exception {
        JSONObject json = jsonVertexMapping.get(folderId);
        if (json == null) {
            json = super.loadJSONCellByFolderId(folderId);
            jsonVertexMapping.put(folderId, json);
        }
        return json;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.EntityListener#managerChanged(com.topvision.ems.message.event.EntityEvent)
     */
    @Override
    public void managerChanged(EntityEvent event) {
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#refreshTopoFolder(Long)
     */
    @Override
    public void refreshTopoFolder(Long folderId) {
        synJsonVertextCache(folderId);
        synJsonEdgeCache(folderId);
        linkMapping.remove(folderId);
        topoFolderMapping.remove(folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#removeEntityAndLink(Long,
     *      java.util.List, java.util.List)
     */
    @Override
    public List<Long> removeEntityAndLink(Long folderId, List<Long> entityIds, List<Long> linkIds) {
        List<Long> relaIds = super.removeEntityAndLink(folderId, entityIds, linkIds);
        if (folderId == null) {
            return null;
        }
        // 同步更新缓存
        List<Link> links = linkMapping.get(folderId);
        int size = linkIds == null ? 0 : linkIds.size();
        for (int i = 0; i < size; i++) {
            for (int j = links.size() - 1; j >= 0; j--) {
                if (linkIds.get(i) == null) {
                    continue;
                }
                if (linkIds.get(i) == links.get(j).getLinkId()) {
                    links.remove(j);
                    break;
                }
            }
        }

        List<Entity> entities = topoFolderMapping.get(folderId);
        size = entityIds == null ? 0 : entityIds.size();
        for (int i = 0; i < size; i++) {
            for (int j = entities.size() - 1; j >= 0; j--) {
                if (entityIds.get(i) == entities.get(j).getEntityId()) {
                    entities.remove(j);
                    break;
                }
            }
            for (int j = links.size() - 1; j >= 0; j--) {
                if (entityIds.get(i) == links.get(j).getSrcEntityId()
                        || entityIds.get(i) == links.get(j).getDestEntityId()) {
                    links.remove(j);
                    break;
                }
            }
        }

        synJsonVertextCache(folderId);
        synJsonEdgeCache(folderId);
        return relaIds;
    }

    /**
     * @param messageService
     *            the messageService to set
     */
    public final void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    private void synJsonEdgeCache(Long folderId) {
        if (folderId == null) {
            return;
        }
        jsonEdgeMapping.remove(folderId);
    }

    private void synJsonVertextCache(Long folderId) {
        if (folderId == null) {
            return;
        }
        jsonVertexMapping.remove(folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.TopologyListener#topologyProgressChanged(com.topvision.ems.message.event.TopologyEvent)
     */
    @Override
    public void topologyProgressChanged(TopologyEvent event) {
        if (event.getProgress() >= 100) {
            topoFolderMapping.clear();
            linkMapping.clear();
            jsonVertexMapping.clear();
            jsonEdgeMapping.clear();
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#txCopyEntity(com.topvision.platform.domain.SystemLog,
     *      Long, Long, java.util.List)
     */
    @Override
    public List<Entity> txCopyEntity(SystemLog sysLog, Long srcFolderId, Long destFolderId, List<Long> entityIds) {
        List<Entity> temps = super.txCopyEntity(sysLog, srcFolderId, destFolderId, entityIds);

        topoFolderMapping.remove(destFolderId);
        List<Entity> entities = Collections.synchronizedList(super.getVertexByFolderId(destFolderId));
        topoFolderMapping.put(destFolderId, entities);

        linkMapping.remove(destFolderId);
        List<Link> links = Collections.synchronizedList(super.getEdgeByFolderId(destFolderId));
        linkMapping.put(destFolderId, links);

        this.synJsonVertextCache(destFolderId);
        this.synJsonEdgeCache(destFolderId);
        return temps;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.TopologyService#txCutEntity(com.topvision.platform.domain.SystemLog,
     *      Long, Long, java.util.List)
     */
    @Override
    public List<Entity> txCutEntity(SystemLog sysLog, Long srcFolderId, Long destFolderId, List<Long> entityIds) {
        topoFolderMapping.clear();
        linkMapping.clear();
        jsonVertexMapping.clear();
        jsonEdgeMapping.clear();
        return super.txCutEntity(sysLog, srcFolderId, destFolderId, entityIds);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#txDeleteTopoFolder(com.topvision.platform.domain.SystemLog,
     *      com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void txDeleteTopoFolder(SystemLog sysLog, TopoFolder folder) {
        super.txDeleteTopoFolder(sysLog, folder);
        this.synJsonVertextCache(folder.getFolderId());
        this.synJsonEdgeCache(folder.getFolderId());
        this.synJsonVertextCache(folder.getSuperiorId());
        this.synJsonEdgeCache(folder.getSuperiorId());
    }

    /**
     * (non-Javadoc)
     * 
     * @throws SQLException
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#txInsertTopoFolder(com.topvision.platform.domain.SystemLog,
     *      com.topvision.ems.network.domain.TopoFolder, Long)
     */
    @Override
    public void txInsertTopoFolder(SystemLog sysLog, TopoFolder folder, Long userGroupId) throws SQLException {
        super.txInsertTopoFolder(sysLog, folder, userGroupId);
        synJsonVertextCache(folder.getSuperiorId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#txMoveEntity(Long,
     *      java.util.List)
     */
    @Override
    public void txMoveEntity(Long destFolderId, List<Long> entityIds) {
        super.txMoveEntity(destFolderId, entityIds);
        synJsonVertextCache(destFolderId);
        synJsonEdgeCache(destFolderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#txMoveEntity(com.topvision.platform.domain.SystemLog,
     *      Long, Long, java.util.List)
     */
    @Override
    public void txMoveEntity(SystemLog sysLog, Long srcFolderId, Long destFolderId, List<Long> entityIds) {
        super.txMoveEntity(sysLog, srcFolderId, destFolderId, entityIds);
        List<Entity> entities = topoFolderMapping.get(srcFolderId);
        if (topoFolderMapping.containsKey(destFolderId)) {
            List<Entity> destEntities = topoFolderMapping.get(destFolderId);
            int size = entityIds == null ? 0 : entityIds.size();
            for (int i = 0; i < size; i++) {
                for (int j = entities.size() - 1; j >= 0; j--) {
                    if (entityIds.get(i) == entities.get(j).getEntityId()) {
                        destEntities.add(entities.remove(j));
                        break;
                    }
                }
            }
        }

        synJsonVertextCache(srcFolderId);
        synJsonEdgeCache(srcFolderId);
        synJsonVertextCache(destFolderId);
        synJsonEdgeCache(destFolderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#txRenameEntity(com.topvision.ems.network.domain.Entity)
     */
    @Override
    public void txRenameEntity(Entity entity) {
        super.txRenameEntity(entity);
        if (topoFolderMapping.containsKey(entity.getFolderId())) {
            List<Entity> entities = topoFolderMapping.get(entity.getFolderId());
            if (entities != null) {
                int size = entities.size();
                for (int i = size - 1; i >= 0; i--) {
                    if (entities.get(i).getEntityId() == entity.getEntityId()) {
                        entities.get(i).setName(entity.getName());
                        entities.get(i).setNameInFolder(entity.getNameInFolder());
                        break;
                    }
                }
            }
        }

        synJsonVertextCache(entity.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#txRenameTopoFolder(com.topvision.platform.domain.SystemLog,
     *      com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void txRenameTopoFolder(SystemLog sysLog, TopoFolder folder) {
        super.txRenameTopoFolder(sysLog, folder);
        synJsonVertextCache(folder.getSuperiorId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#txSaveCoordinate(Long,
     *      java.util.List, java.util.List)
     */
    @Override
    public void txSaveCoordinate(Long folderId, List<Entity> entities, List<MapNode> nodes) {
        super.txSaveCoordinate(folderId, entities, nodes);
        // 同步更新缓存
        int size = entities == null ? 0 : entities.size();
        if (size > 0) {
            List<Entity> temps = topoFolderMapping.get(folderId);
            if (temps != null) {
                Entity entity = null;
                Entity entity1 = null;
                int count = temps.size();
                for (int i = 0; i < count; i++) {
                    entity = temps.get(i);
                    for (int j = 0; j < size; j++) {
                        entity1 = entities.get(j);
                        if (entity.getEntityId() == entity1.getEntityId()) {
                            entity.setX(entity1.getX());
                            entity.setY(entity1.getY());
                            break;
                        }
                    }
                }
            }
        }

        synJsonVertextCache(folderId);
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#txUpdateTopoFolderOutline(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void txUpdateTopoFolderOutline(TopoFolder folder) {
        super.txUpdateTopoFolderOutline(folder);
        synJsonVertextCache(folder.getSuperiorId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#updateEntityIcon(com.topvision.ems.network.domain.Entity)
     */
    @Override
    public void updateEntityIcon(Entity entity) {
        super.updateEntityIcon(entity);

        List<Entity> entities = topoFolderMapping.get(entity.getFolderId());

        if (entities != null) {
            int size = entities.size();
            for (int i = 0; i < size; i++) {
                if (entities.get(i).getEntityId() == entity.getEntityId()) {
                    entities.get(i).setIcon(entity.getIcon());
                    break;
                }
            }

            synJsonVertextCache(entity.getFolderId());
            synJsonEdgeCache(entity.getFolderId());
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#updateMapNodeDashedBorder(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeDashedBorder(MapNode mapNode) {
        super.updateMapNodeDashedBorder(mapNode);
        synJsonVertextCache(mapNode.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#updateMapNodeFillColor(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeFillColor(MapNode mapNode) {
        super.updateMapNodeFillColor(mapNode);
        synJsonVertextCache(mapNode.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#updateMapNodeFontColor(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeFontColor(MapNode mapNode) {
        super.updateMapNodeFontColor(mapNode);
        synJsonVertextCache(mapNode.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#updateMapNodeFontSize(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeFontSize(MapNode mapNode) {
        super.updateMapNodeFontSize(mapNode);
        synJsonVertextCache(mapNode.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#updateMapNodeShadow(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeShadow(MapNode mapNode) {
        super.updateMapNodeShadow(mapNode);
        synJsonVertextCache(mapNode.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#updateMapNodeSize(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeSize(MapNode mapNode) {
        super.updateMapNodeSize(mapNode);
        synJsonVertextCache(mapNode.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#updateMapNodeStrokeColor(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeStrokeColor(MapNode mapNode) {
        super.updateMapNodeStrokeColor(mapNode);
        synJsonVertextCache(mapNode.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#updateMapNodeStrokeWeight(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeStrokeWeight(MapNode mapNode) {
        super.updateMapNodeStrokeWeight(mapNode);
        synJsonVertextCache(mapNode.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#updateMapNodeText(com.topvision.ems.network.domain.MapNode)
     */
    @Override
    public void updateMapNodeText(MapNode mapNode) {
        super.updateMapNodeText(mapNode);
        synJsonVertextCache(mapNode.getFolderId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#updateTopoFolderFixed(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderFixed(TopoFolder folder) {
        super.updateTopoFolderFixed(folder);
        synJsonVertextCache(folder.getSuperiorId());
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.network.service.impl.TopologyServiceImpl#updateTopoFolderIcon(com.topvision.ems.network.domain.TopoFolder)
     */
    @Override
    public void updateTopoFolderIcon(TopoFolder folder) {
        super.updateTopoFolderIcon(folder);
        synJsonVertextCache(folder.getSuperiorId());
    }

    @Override
    public void entityReplaced(EntityEvent event) {
    }
}
