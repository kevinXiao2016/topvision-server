/**
 * 
 */
package com.topvision.ems.network.message;

import com.topvision.platform.message.event.EmsEventObject;

/**
 * @author niejun
 * 
 */
public class TopologyEvent extends EmsEventObject<TopologyListener> {
    private static final long serialVersionUID = 2676683415278275388L;
    private int count;
    private int progress = 0;
    private String msg = null;

    public TopologyEvent() {
        super("Topology");
    }

    public TopologyEvent(Object obj) {
        super(obj);
    }

    public TopologyEvent(String msg, int progress) {
        super("Topology");
        this.msg = msg;
        this.progress = progress;
    }

    public TopologyEvent(String msg, int progress, int count) {
        this(msg, progress);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getMsg() {
        return msg;
    }

    public int getProgress() {
        return progress;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}