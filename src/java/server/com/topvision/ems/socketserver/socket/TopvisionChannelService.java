package com.topvision.ems.socketserver.socket;

import com.topvision.framework.service.Service;

/**
 * Created by jay on 17-4-22.
 */
public interface TopvisionChannelService extends Service {
    public String listSocketInfo();
    public String listConfigInfo();
    public void stopSocketWorker(String sessionId);
    public void stopAllSocketWorker();
}
