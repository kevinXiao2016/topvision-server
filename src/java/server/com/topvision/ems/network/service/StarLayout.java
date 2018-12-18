package com.topvision.ems.network.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.domain.Link;
import com.topvision.platform.SystemConstants;

class Device {
    double x;
    double y;
    double dx;
    double dy;
    boolean fixed;
    long id = -1;

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Device:" + x + "," + y + "\t" + dx + "," + dy;
    }
}

class Dimension {
    int width;
    int height;

    public Dimension(int w, int h) {
        this.width = w;
        this.height = h;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Dimension:" + width + "," + height;
    }
}

class Edge {
    int from;
    int to;
    double len;

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Edge:" + from + "," + to + "," + len;
    }
}

public class StarLayout {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Device[] nodes;
    private Edge[] edges;
    private boolean byNode = true;// opposite is by link
    private Map<Long, Link> linkEntity;
    private Map<Long, Entity> nodeEntity;
    private Dimension dm;
    private int nnodes = 0;
    private int nedges = 0;
    private double dxValue = 0;
    private double dyValue = 0;
    private int range = 0;// 连接长度
    private int nodeWidth = 0;// 节点宽度，为了没有连接的排列
    private int nodeHeight = 0;// 节点高度，为了没有连接的排列
    private int noLinkX = 0;
    private int noLinkY = 0;
    private int closest = 0;
    private int count = 0;
    private Map<Long, Entity> fixedNodeIdList;

    /**
     * 
     * @param from
     * @param to
     * @param len
     */
    private void addEdge(long from, long to, int len) {
        Edge e = new Edge();
        e.from = findNode(from);
        e.to = findNode(to);
        e.len = len;
        edges[nedges++] = e;
    }

    /**
     * 
     * @param myNode
     */
    public void addFixedNode(Entity myNode) {
        if (myNode == null) {
            return;
        }
        fixedNodeIdList.put(myNode.getEntityId(), myNode);
    }

    /**
     * 
     * @param id
     * @return
     */
    private int addNode(long id) {
        Device n = new Device();
        if (fixedNodeIdList.containsKey(String.valueOf(id))) {
            n.fixed = true;
            Entity node = fixedNodeIdList.get(String.valueOf(id));
            n.x = node.getX();
            n.y = node.getY();
        } else {
            n.x = noLinkX;
            n.y = noLinkY;
        }
        n.id = id;
        nodes[nnodes] = n;
        return nnodes++;
    }

    /**
     * clear.
     * 
     */
    public void clear() {
        nodes = null;
        edges = null;
        linkEntity = null;
        nodeEntity = null;
        fixedNodeIdList = null;
    }

    public void doLayout(int height, int width, List<Entity> fixeds, List<Link> links, List<Entity> entities) {
        init();
        dm.height = height;
        dm.width = width;
        for (int i = 0; i < fixeds.size(); i++) {
            addFixedNode(fixeds.get(i));
        }
        for (int i = 0; i < links.size(); i++) {
            Link link = links.get(i);
            linkEntity.put(link.getLinkId(), link);
        }
        for (int i = 0; i < entities.size(); i++) {
            Entity node = entities.get(i);
            nodeEntity.put(node.getEntityId(), node);
        }
        edges = new Edge[linkEntity.size()];
        nodes = new Device[linkEntity.size() * 2];
        layout();
        clear();
    }

    private int findNode(long id) {
        for (int i = 0; i < nnodes; i++) {
            if (nodes[i].id == id) {
                return i;
            }
        }
        return addNode(id);
    }

    /**
     * initialize variable
     */
    public void init() {
        nnodes = 0;
        nedges = 0;
        dxValue = 0;
        dyValue = 0;
        range = SystemConstants.getInstance().getIntParam("Topology.Map.Radius", 250);
        nodeWidth = SystemConstants.getInstance().getIntParam("Topology.Map.Node.Width", 90);
        nodeHeight = SystemConstants.getInstance().getIntParam("Topology.Map.Node.Height", 90);
        noLinkX = 0;
        noLinkY = 0;
        closest = 0;
        count = 0;
        byNode = true;
        dm = new Dimension(400, 400);
        linkEntity = new HashMap<Long, Link>();
        nodeEntity = new HashMap<Long, Entity>();
        fixedNodeIdList = new HashMap<Long, Entity>();
    }

    private void initData() {
        if (byNode) {
            for (Iterator<Link> iter = linkEntity.values().iterator(); iter.hasNext();) {
                Link link = iter.next();
                long sId = link.getSrcEntityId();
                long dId = link.getDestEntityId();
                if (sId != -1 && dId != -1) {
                    addEdge(sId, dId, range);
                }
            }
        } else {
            for (Iterator<Link> iter = linkEntity.values().iterator(); iter.hasNext();) {
                Link myLink = iter.next();
                long sId = myLink.getSrcEntityId();
                long dId = myLink.getDestEntityId();
                if (sId != -1 && dId != -1) {
                    addEdge(sId, dId, range);
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("nnodes:" + nnodes);
            logger.debug("nedges:" + nedges);
        }
    }

    private boolean isLinkDevice(Entity myNode) {
        for (Device d : nodes) {
            if (d != null && d.id == myNode.getEntityId()) {
                return true;
            }
        }
        return false;
    }

    private void layout() {
        if (linkEntity == null || linkEntity.isEmpty()) {
            move2TopCenter();
            return;
        }

        initData();
        while (true) {
            count++;
            if (logger.isTraceEnabled()) {
                logger.trace("****[dx:" + dxValue + "]\n****[dy:" + dyValue + "]");
            }
            if (dxValue != 0 && dyValue != 0 && Math.abs(dxValue) < 0.2 && Math.abs(dyValue) < 0.2) {
                if (logger.isDebugEnabled()) {
                    logger.debug("count:" + count);
                }
                processData();
                break;
            }
            if (count == 1000) {
                processData();
                if (logger.isDebugEnabled()) {
                    logger.debug("count:" + count);
                }
                break;
            }
            relax();
        }
        move2TopCenter();

        count = 0;
    }

    private void move2TopCenter() {
        int minTop = Integer.MAX_VALUE;
        int minRight = Integer.MAX_VALUE;
        int maxRight = Integer.MIN_VALUE;
        for (Iterator<Entity> iter = nodeEntity.values().iterator(); iter.hasNext();) {
            Entity node = iter.next();
            if (node == null) {
                continue;
            }
            if (!isLinkDevice(node)) {
                continue;
            }
            minTop = Math.min(minTop, node.getY());
            minRight = Math.min(minRight, node.getX());
            maxRight = Math.max(maxRight, node.getX());
        }
        for (Iterator<Entity> iter = nodeEntity.values().iterator(); iter.hasNext();) {
            Entity myNode = iter.next();
            if (myNode == null) {
                continue;
            }
            if (!isLinkDevice(myNode)) {
                myNode.setX(noLinkX);
                myNode.setY(noLinkY);
                if (logger.isTraceEnabled()) {
                    logger.trace(myNode.getIp() + "==>" + noLinkX + "," + noLinkY);
                }
                noLinkX += nodeWidth;
                if (noLinkX > dm.width) {
                    noLinkY += nodeHeight;
                    noLinkX = 0;
                }
            }
        }
        for (Iterator<Entity> iter = nodeEntity.values().iterator(); iter.hasNext();) {
            Entity myNode = iter.next();
            if (myNode == null) {
                continue;
            }
            if (isLinkDevice(myNode)) {
                myNode.setX(myNode.getX() - minRight + 20);
                myNode.setY(myNode.getY() - minTop + noLinkY + nodeHeight + 20);
            }
        }
    }

    private void processData() {
        for (int i = 0; i < nnodes; i++) {
            Device n = nodes[i];
            Entity myNode = nodeEntity.get(n.id);
            if (myNode != null) {
                myNode.setX((int) n.x);
                myNode.setY((int) n.y);
            }
        }
    }

    private void relax() {
        for (int i = 0; i < nedges; i++) {
            Edge e = edges[i];
            double vx = nodes[e.to].x - nodes[e.from].x;
            double vy = nodes[e.to].y - nodes[e.from].y;
            double len = Math.sqrt(vx * vx + vy * vy);
            len = (len == 0) ? .0001 : len;
            double f = (edges[i].len - len) / (len * 8);
            double dx = f * vx;
            double dy = f * vy;
            nodes[e.to].dx += dx;
            nodes[e.to].dy += dy;
            nodes[e.from].dx += -dx;
            nodes[e.from].dy += -dy;
        }
        dxValue = -5;
        dyValue = -5;
        for (int i = 0; i < nnodes; i++) {
            Device n1 = nodes[i];
            double dx = 0;
            double dy = 0;
            for (int j = 0; j < nnodes; j++) {
                if (i == j) {
                    continue;
                }
                Device n2 = nodes[j];
                double vx = n1.x - n2.x;
                double vy = n1.y - n2.y;
                double len = vx * vx + vy * vy;
                if (len == 0) {
                    dx += Math.random();
                    dy += Math.random();
                } else if (len < range * 2 * range * 2) {
                    dx += vx / len;
                    dy += vy / len;
                }
            }
            double dlen = dx * dx + dy * dy;
            if (dlen > 0) {
                dlen = Math.sqrt(dlen) / 2;
                n1.dx += dx / dlen;
                n1.dy += dy / dlen;
            }
            dxValue = Math.max(dxValue, n1.dx);
            dyValue = Math.max(dyValue, n1.dy);
        }
        for (int i = 0; i < nnodes; i++) {
            Device n = nodes[i];
            if (!n.fixed) {
                n.x += Math.max(-10, Math.min(10, n.dx));
                n.y += Math.max(-10, Math.min(10, n.dy));
            }
            if (n.x < closest) {
                n.x = closest;
            } else if (n.x > dm.width) {
                n.x = dm.width;
            }
            if (n.y < closest) {
                n.y = closest;
            } else if (n.y > dm.height) {
                n.y = dm.height;
            }
            n.dx /= 2;
            n.dy /= 2;
        }
    }

    /**
     * setDimension.
     * 
     * @param dm
     *            Dimension
     */
    public void setDimension(Dimension dm) {
        this.dm = dm;
    }

    /**
     * 
     * @param range
     */
    public void setRange(int range) {
        this.range = range;
    }
}
