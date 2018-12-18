/***********************************************************************
 * $ TestIp.java,v1.0 14-4-16 上午9:52 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc;

import java.io.Serializable;

import com.topvision.framework.common.IpUtils;

/**
 * @author jay
 * @created @14-4-16-上午9:52
 */
public class TestIp implements Serializable {
    private static final long serialVersionUID = 7342180206202447869L;

    public static void main(String[] args) {
        TestIp testIp = new TestIp();
        testIp.go();
    }

    private void go() {
        IpUtils ipUtils = new IpUtils("100.11.10.74");
        System.out.println("ipUtils.longValue() = " + ipUtils.longValue());
    }
}
