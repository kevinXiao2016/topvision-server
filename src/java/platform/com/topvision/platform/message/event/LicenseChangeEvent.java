/***********************************************************************
 * $Id: LicenseChangeEvent.java,v1.0 2014年10月21日 上午8:50:31 $
 * 
 * @author: Victor
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

/**
 * @author Victor
 * @created @2014年10月21日-上午8:50:31
 *
 */
public class LicenseChangeEvent extends EmsEventObject<LicenseChangeListener> {
    private static final long serialVersionUID = 3974590175511077391L;

    /**
     * @param source
     */
    public LicenseChangeEvent(Object source) {
        super(source);
    }
}
