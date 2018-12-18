package com.topvision.ems.network.action;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.network.message.TopologyEvent;
import com.topvision.ems.network.message.TopologyListener;

public class TopologyContext implements TopologyListener {
    private static Logger logger = LoggerFactory.getLogger(TopologyContext.class);
    public static final byte RUNNING = 0;
    public static final byte COMPLETED = 1;
    public static final byte WAITING = 2;
    private static final TopologyContext instance = new TopologyContext();
    private String username;
    private byte topologyState = WAITING;
    private Integer progress = 0;
    private final List<String> message = new ArrayList<String>();

    /**
     * 得到拓扑环境实例.
     * 
     * @return
     */
    public static TopologyContext getInstance() {
        return instance;
    }

    public String getAllMsg() {
        StringBuilder sb = new StringBuilder("");
        synchronized (message) {
            int size = message.size();
            for (int i = 0; i < size; i++) {
                sb.append(message.get(i));
                sb.append("<br>");
            }
            message.clear();
        }
        return sb.toString();
    }

    public void reset() {
        topologyState = WAITING;
        progress = 0;
        message.clear();
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.message.event.TopologyListener#topologyProgressChanged(com.topvision.ems.message.event.TopologyEvent)
     */
    @Override
    public void topologyProgressChanged(TopologyEvent event) {
        logger.info(event.getMsg());
        synchronized (message) {
            message.add(event.getMsg());
        }
        progress = event.getProgress();
        topologyState = (event.getProgress() >= 100 ? COMPLETED : RUNNING);
    }

    public Integer getProgress() {
        return progress;
    }

    public byte getTopologyState() {
        return topologyState;
    }

    public String getUsername() {
        return username;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void setTopologyState(byte topologyState) {
        this.topologyState = topologyState;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
