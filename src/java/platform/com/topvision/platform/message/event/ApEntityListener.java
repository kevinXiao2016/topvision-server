/***********************************************************************
 * $Id: ApEntityListener.java,v1.0 2013-1-22 下午4:59:48 $
 * 
 * @author: loyal
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author loyal
 * @created @2013-1-22-下午4:59:48
 *
 */
public interface ApEntityListener extends EmsListener{
    public void apAutoAdded(ApEntityEvent apEntityEvent);
}
