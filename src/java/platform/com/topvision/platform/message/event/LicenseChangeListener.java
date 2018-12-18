/***********************************************************************
 * $Id: LicenseChangeListener.java,v1.0 2014年10月21日 上午8:50:53 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author Victor
 * @created @2014年10月21日-上午8:50:53
 *
 */
public interface LicenseChangeListener extends EmsListener {
    void licenseChanged(LicenseChangeEvent evt);
}
