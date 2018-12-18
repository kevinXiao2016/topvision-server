/***********************************************************************
 * $Id: UserInfoChangeListener.java,v1.0 2013年9月27日 上午11:38:14 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.user.message;

import com.topvision.platform.message.event.EmsListener;


/**
 * 监听用户信息改变
 * @author Bravin
 * @created @2013年9月27日-上午11:38:14
 *
 */
public interface UserInfoListener extends EmsListener {
    void userInfoChanged(UserEvent event);
}
