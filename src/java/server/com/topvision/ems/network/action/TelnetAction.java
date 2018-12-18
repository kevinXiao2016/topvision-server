package com.topvision.ems.network.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.network.service.EntityService;
import com.topvision.framework.web.struts2.BaseAction;

@Controller("telnetAction")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TelnetAction extends BaseAction {
    private static final long serialVersionUID = 9075827873733863181L;
    private static Logger logger = LoggerFactory.getLogger(TelnetAction.class);
    private long entityId;
    private Entity entity;
    private String host;
    private String port;
    private String username;
    private String passwd;
    private String loginPrompt;
    private String passwdprompt;
    private String displayPrompt = "$";
    @Autowired
    private EntityService entityService;

    public String showTelnet() {
        Entity entity = entityService.getEntity(entityId);
        logger.info(String.valueOf(entity.getEntityId()));
        return SUCCESS;
    }

    public String getDisplayPrompt() {
        return displayPrompt;
    }

    public Entity getEntity() {
        return entity;
    }

    public long getEntityId() {
        return entityId;
    }

    public String getHost() {
        return host;
    }

    public String getLoginPrompt() {
        return loginPrompt;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getPasswdprompt() {
        return passwdprompt;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public void setDisplayPrompt(String displayPrompt) {
        this.displayPrompt = displayPrompt;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public void setEntityService(EntityService entityService) {
        this.entityService = entityService;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setLoginPrompt(String loginPrompt) {
        this.loginPrompt = loginPrompt;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setPasswdprompt(String passwdprompt) {
        this.passwdprompt = passwdprompt;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
