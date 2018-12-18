/***********************************************************************
 * $Id: CmcSyslogParser.java,v1.0 2013-4-21 下午02:10:32 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.fault.syslog;

import com.topvision.ems.fault.parser.SyslogParser;
import com.topvision.framework.syslog.Syslog;

/**
 * @author Rod John
 * @created @2013-4-21-下午02:10:32
 *
 */
public class CmcSyslogParser implements SyslogParser {

    /* (non-Javadoc)
     * @see com.topvision.ems.fault.parser.SyslogParser#parse(java.lang.Long, com.topvision.framework.syslog.Syslog)
     */
    @Override
    public boolean parse(Long entityId, Syslog syslog) {
        if(syslog.getText().indexOf(CmcSyslogConstants.CMCMAC) != -1 || syslog.getText().indexOf(CmcSyslogConstants.CMTSMAC)!=-1){
            //TODO CMC Syslog Handle;
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.fault.parser.SyslogParser#getSyslogCos()
     */
    @Override
    public Integer getSyslogCos() {
        return null;
    }

}
