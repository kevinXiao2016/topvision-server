/***********************************************************************
 * $Id: EmsAdapter.java,v 1.1 Sep 17, 2009 9:25:17 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.platform.message.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Create Date Sep 17, 2009 9:25:17 AM
 * 
 * @author kelers
 * 
 */
public abstract class EmsAdapter implements EmsListener {
    protected Logger logger = LoggerFactory.getLogger(getClass());
}
