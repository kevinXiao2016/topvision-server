package com.topvision.ems.message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.network.service.EntityService;
import com.topvision.ems.socketserver.domain.SocketRequest;
import com.topvision.ems.socketserver.domain.SocketResponse;
import com.topvision.ems.socketserver.socket.SocketRequestExecutor;
import com.topvision.ems.socketserver.websocket.TopWebSocketHandler;
import com.topvision.platform.domain.User;
import com.topvision.platform.service.UserService;

@Service("messagePusher")
public class MessagePusher implements SocketRequestExecutor, TopWebSocketHandler {
    private static Logger logger = LoggerFactory.getLogger(MessagePusher.class);
    private final Map<String, SocketResponse> connections = new HashMap<>();
    private final Map<String, String> hosts = new HashMap<>();
    private BlockingQueue<Message> dataQueue = null;
    private ExecutorService executorService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private UserService userService;

    /**
     * DWR中配置的初始化方法，开独线程推送消息，一个线程推送广播，一个推送
     */
    @PostConstruct
    public void initialize() {
        dataQueue = new ArrayBlockingQueue<Message>(1000);
        executorService = Executors.newSingleThreadExecutor();
        // ------开启一个DWR专用线程用于消息广播推送------//
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("MessagePusherThread");
                // ---------待推送的告警对象------//
                List<Message> readyList = new ArrayList<Message>();
                // ---------如果告警队列对象存在，线程就一直执行----//
                while (dataQueue != null) {
                    try {
                        // 获取并移除此告警队列的头部，在元素变得可用之前一直等待（如果有必要）。
                        Message data = dataQueue.take();
                        // Thread.sleep(2000);
                        readyList.add(data);
                        // ---- 最多从此队列中移除 9个 可用元素，并将这些元素添加到 准备推送队列 中。---//
                        dataQueue.drainTo(readyList, 19);
                        // -----如果当前有browser访问该页面----//
                        if (!connections.isEmpty()) {
                            Collection<SocketResponse> responses = connections.values();
                            while (!readyList.isEmpty()){
                                try {
                                    Message message = readyList.remove(0);
                                    String messageStr = JSONObject.fromObject(message).toString();
                                    String jconnectID = message.getJconnectID();
                                    if (jconnectID != null) {//单播
                                        SocketResponse response = connections.get(jconnectID);
                                        if (response == null || response.isClosed()) {
                                            connections.remove(jconnectID);
                                            continue;
                                        }
                                        response.write(messageStr);
                                    } else {//广播
                                        for (Iterator<SocketResponse> it = responses.iterator(); it.hasNext();) {
                                            SocketResponse $response = it.next();
                                            if ($response.isClosed()) {
                                                it.remove();
                                                continue;
                                            }
                                            if (Message.ALERT_TYPE.equals(message.getType())) {
                                                Long userId = $response.getLong(SocketRequest.USER_ID);
                                                User user = userService.getUserEx(userId);
                                                List<Entity> entities = entityService.getUserAuthorityEntity(user);
                                                for (Entity entity : entities) {
                                                    if (entity.getEntityId() == ((Alert) message.getData())
                                                            .getEntityId()) {
                                                        $response.write(messageStr);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                $response.write(messageStr);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    logger.warn("", e);
                                }
                            }
                        }
                    } catch (InterruptedException ie) {
                    } catch (Exception ex) {
                        logger.error("Send message by pusher.", ex);
                    } finally {
                        // 推送完毕清空准备列表，以待下一次发送，无论本次的处理结果如何，当前推送的数据都必须清空
                        readyList.clear();
                    }
                }
            }
        });
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdownNow();
        dataQueue.clear();
        dataQueue = null;
    }

    /**
     * 推送消息到前端页面
     * 
     * @param msg
     */
    public void sendMessage(Message msg) {
        try {
            dataQueue.add(msg);
            if (logger.isDebugEnabled()) {
                logger.debug("sendMessage dataQueue [" + dataQueue.size() + "].");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public boolean execute(SocketRequest socketRequest, SocketResponse socketResponse) {
        onConnected(socketRequest);
        return MAINTAIN_SOCKET;
    }

    @Override
    public void onConnected(SocketRequest socketRequest) {
        String connectID = socketRequest.getString("JCONNECTID");
        connections.put(connectID, socketRequest.getResponse());
        hosts.put(connectID, socketRequest.getRequestHost());
    }

    @Override
    public void onClose(SocketRequest socketRequest) {
    }

    @Override
    public void onMessage(SocketRequest socketRequest) {
    }
    
}