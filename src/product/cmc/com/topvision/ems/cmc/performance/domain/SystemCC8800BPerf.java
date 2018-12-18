/***********************************************************************
 * $Id: SystemPerf.java,v1.0 2012-7-11 下午02:15:40 $
 * 
 * @author: bryan
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.performance.domain;

/**
 * @author bryan
 * @created @2012-9-7-上午10:17:26
 * 
 */
public class SystemCC8800BPerf extends SystemPerf {
    private static final long serialVersionUID = -4923315755347924759L;

    public SystemCC8800BPerf() {
        super("systemPerfDBSaver", "systemCC8800BPerfScheduler", "CC_SYSTEM");
    }

    @Override
    public String[] makeOids() {
        oids = new String[3];
        // cpu利用率
        oids[0] = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.11.";
        // 内存利用率
        oids[1] = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.10.";
        //flash利用率
        // oids[2] = "1.3.6.1.4.1.32285.11.2.3.1.3.1.1.13";
        oids[2] = "1.3.6.1.4.1.32285.11.1.1.2.1.1.1.3.";
        return oids;
    }
}
