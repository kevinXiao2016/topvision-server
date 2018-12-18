package com.topvision.ems.network.mgmt;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.message.Message;
import com.topvision.ems.message.MessagePusher;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.user.message.UserEvent;
import com.topvision.platform.user.message.UserPreferencesListener;

import net.sf.json.JSONObject;

@Service("userPreferencesController")
public class UserPreferencesControllerImpl extends BaseService implements UserPreferencesController, UserPreferencesListener {

    @Autowired
    private MessagePusher messagePusher;
    @Autowired
    private MessageService messageService;

    @Override
    @PostConstruct
    public void initialize() {
        // addEventListeners
        messageService.addListener(UserPreferencesListener.class, this);
    }

    @Override
    @PreDestroy
    public void destroy() {
        messageService.removeListener(UserPreferencesListener.class, this);
    }

    @Override
    public void userPreferencesChanged(UserEvent envet) {
        // 通知拓扑树重载导航页
        JSONObject json = new JSONObject();
        json.put("data", "refresh");
        Message message = new Message("message");
        String id = ServletActionContext.getRequest().getSession().getId();
        message.addSessionId(id);
        message.setData(json.toString());
        messagePusher.sendMessage(message);
    }

}
