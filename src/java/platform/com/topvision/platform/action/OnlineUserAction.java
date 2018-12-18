/***********************************************************************
 * $Id: OnlineUserAction.java,v 1.1 Nov 6, 2009 5:07:46 PM kelers Exp $
 *
 * @author: kelers
 *
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.topvision.framework.web.struts2.BaseAction;
import com.topvision.platform.domain.User;
import com.topvision.platform.user.context.UserContextManager;

public class OnlineUserAction extends BaseAction {
    private static final long serialVersionUID = -5852084674583400260L;

    @Autowired
    private UserContextManager ucm;
    private List<String> userNames;
    private List<User> users;

    /**
     * 获取在线用户
     * 
     * @return
     */
    public String getOnlineUsers() {
        users = ucm.getOnlineUser();
        return NONE;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
