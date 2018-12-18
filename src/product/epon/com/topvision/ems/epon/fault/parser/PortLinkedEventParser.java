/***********************************************************************
 * $Id: PortLinkedEventParser.java,v1.0 2012-3-8 上午09:00:03 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.fault.parser;

import org.springframework.stereotype.Service;

import com.topvision.ems.epon.fault.EponCode;

/**
 * @author huqiao
 * @created @2012-3-8-上午09:00:03
 * 
 */
@Service("portLinkedEventParser")
public class PortLinkedEventParser extends PonAbstractEventParser {

    public PortLinkedEventParser() {
        this.threadName = "PortLinkedEventParser";
    }

    @Override
    protected int getPonAlarmCode() {
        return EponCode.PORT_LINK_DOWN;
    }

    @Override
    protected int getPonClearCode() {
        return EponCode.PORT_LINK_UP;
    }

}
