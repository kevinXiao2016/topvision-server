package com.topvision.ems.network.message;

import com.topvision.platform.message.event.EmsListener;

public interface TopologyListener extends EmsListener {
    /**
     * 拓扑进程发生变化.
     * 
     * @param event
     */
    void topologyProgressChanged(TopologyEvent event);
}