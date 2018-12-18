/***********************************************************************
 * $Id: UserListeners.java,v1.0 2014年3月6日 上午10:12:06 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.user.message;

import com.topvision.platform.message.event.EmsListener;

/**
 * @author Bravin
 * @created @2014年3月6日-上午10:12:06
 *
 */
public interface UserListener extends EmsListener {

    void userAdded(UserEvent ue);
}
