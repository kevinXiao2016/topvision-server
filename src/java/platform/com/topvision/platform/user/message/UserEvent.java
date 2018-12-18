/***********************************************************************
 * $Id: UserInfoChangeEvent.java,v1.0 2013年9月27日 上午11:37:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.user.message;

import com.topvision.platform.domain.User;
import com.topvision.platform.message.event.EmsEventObject;

/**
 * @author Bravin
 * @created @2013年9月27日-上午11:37:16
 *
 */
public class UserEvent extends EmsEventObject<User>{

    public UserEvent(Object source) {
        super(source);
    }

    private static final long serialVersionUID = -7309074227365497971L;

}
