package com.topvision.ems.network.service;

import java.util.HashMap;
import java.util.Map;

public final class NetworkConstants {
    public static final String TOPOLOGY_VIEW = "network.topology.view";
    public static final String TOPOLOGY_DISPLAYNAME = "network.topology.displayName";
    public static final String TOPOLOGY_DISPLAYGRID = "network.topology.displayGrid";
    public static final String TOPOLOGY_ARRANGE = "network.topology.arrangeType";
    public static final String TOPOLOGY_SORT = "network.topology.sortType";
    public static final String ENTITY_SORT = "network.entity.sortType";
    public static final String ENTITY_DISPLAYNAME = "network.entity.displayName";
    public static final String ENTITY_VIEW = "network.entity.view";
    public static final String ENTITY_PAGESIZE = "network.entity.pageSize";
    /**
     * 图元节点类型.
     */
    public static final int TYPE_NODE = 0;
    public static final int TYPE_ENTITY = 1;
    public static final int TYPE_FOLDER = 2;
    public static final int TYPE_SHAPE = 3;
    public static final int TYPE_LINK = 4;

    /**
     * 虚拟子网下的设备类型集合
     * 
     */
    public static final Map<Long, String> VIRTUALPRODUCTMAP_MAP = new HashMap<Long, String>() {
        /**
         * 
         */
        private static final long serialVersionUID = -98039928542113036L;
        {
        }
    };
}
