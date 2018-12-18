/***********************************************************************
 * $Id: FlowUtil.java,v 1.1 Sep 11, 2009 11:08:13 AM kelers Exp $
 * 
 * @author: kelers
 * 
 * (c)Copyright 2011 Topoview All rights reserved.
 ***********************************************************************/
package com.topvision.ems.network.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topvision.ems.network.domain.PortPerf;
import com.topvision.ems.network.domain.PortStatistics;

/**
 * @Create Date Sep 11, 2009 11:08:13 AM
 * 
 * @author kelers
 * 
 */
public class FlowUtil {
    protected static Logger logger = LoggerFactory.getLogger(FlowUtil.class);
    private static final long MAX_COUNTER = 4294967295L;

    public static PortPerf getPortPerf(PortStatistics ps, PortStatistics prevPs, long entityId) {
        PortPerf perf = new PortPerf();
        perf.setCollectTime(ps.getCollectTime());
        perf.setEntityId(entityId);
        perf.setIfIndex(ps.getIfIndex());
        perf.setIfName(ps.getIfName());
        perf.setIfAdminStatus(ps.getIfAdminStatus());
        perf.setIfOperStatus(ps.getIfOperStatus());
        perf.setIntervalSeconds((int) ((ps.getCollectTime().getTime() - prevPs.getCollectTime().getTime()) / 1000));
        // PortPerf类会自动解决两次取值翻转的问题
        // In
        perf.setIfInDiscards(outOfCounter(ps.getIfInDiscards(), prevPs.getIfInDiscards()));
        perf.setIfInErrors(outOfCounter(ps.getIfInErrors(), prevPs.getIfInErrors()));
        perf.setIfInNUcastPkts(outOfCounter(ps.getIfInNUcastPkts(), prevPs.getIfInNUcastPkts()));
        perf.setIfInOctets(60.0 * outOfCounter(ps.getIfInOctets(), prevPs.getIfInOctets()) / perf.getIntervalSeconds());
        perf.setIfInUcastPkts(outOfCounter(ps.getIfInUcastPkts(), prevPs.getIfInUcastPkts()));
        // Out
        perf.setIfOutDiscards(outOfCounter(ps.getIfOutDiscards(), prevPs.getIfOutDiscards()));
        perf.setIfOutErrors(outOfCounter(ps.getIfOutErrors(), prevPs.getIfOutErrors()));
        perf.setIfOutNUcastPkts(outOfCounter(ps.getIfOutNUcastPkts(), prevPs.getIfOutNUcastPkts()));
        perf.setIfOutOctets(60.0 * outOfCounter(ps.getIfOutOctets(), prevPs.getIfOutOctets())
                / perf.getIntervalSeconds());
        perf.setIfOutUcastPkts(outOfCounter(ps.getIfOutUcastPkts(), prevPs.getIfOutUcastPkts()));
        // All
        perf.setIfUcastPkts(perf.getIfInUcastPkts() + perf.getIfOutUcastPkts());
        perf.setIfDiscards(perf.getIfInDiscards() + perf.getIfOutDiscards());
        perf.setIfErrors(perf.getIfInErrors() + perf.getIfOutErrors());
        perf.setIfNUcastPkts(perf.getIfInNUcastPkts() + perf.getIfOutNUcastPkts());
        perf.setIfOctets(perf.getIfInOctets() + perf.getIfOutOctets());

        double ifInPkts = perf.getIfInUcastPkts() + perf.getIfInNUcastPkts() + perf.getIfInDiscards()
                + perf.getIfInErrors();
        ifInPkts = Math.max(1, ifInPkts);
        double ifOutPkts = perf.getIfOutUcastPkts() + perf.getIfOutNUcastPkts() + perf.getIfOutDiscards()
                + perf.getIfOutErrors();
        ifOutPkts = Math.max(1, ifOutPkts);

        perf.setIfInDiscardsRate(1.0 * perf.getIfInDiscards() / ifInPkts);
        perf.setIfInErrorsRate(1.0 * perf.getIfInErrors() / ifInPkts);
        perf.setIfInOctetsRate(perf.getIfInOctets() / 7.5);// 7.5=8bit/60second

        perf.setIfOutDiscardsRate(1.0 * perf.getIfOutDiscards() / ifOutPkts);
        perf.setIfOutErrorsRate(1.0 * perf.getIfOutErrors() / ifOutPkts);
        perf.setIfOutOctetsRate(perf.getIfOutOctets() / 7.5);

        perf.setIfDiscardsRate(1.0 * perf.getIfDiscards() / (ifInPkts + ifOutPkts));
        perf.setIfErrorsRate(1.0 * perf.getIfErrors() / (ifInPkts + ifOutPkts));
        perf.setIfOctetsRate((perf.getIfInOctetsRate() + perf.getIfOutOctetsRate()));
        return perf;
    }

    public static Double outOfCounter(long d1, long d2) {
        long d = d1 - d2;
        if (d1 >= d2) {
            return Double.valueOf(d);
        } else {
            long abs = Math.abs(d);
            if (abs < 100) {
                logger.debug(d1 + "-" + d2 + "=" + (d1 - d2) + "=>" + abs);
                return Double.valueOf(abs);
            } else {
                logger.debug(d1 + "-" + d2 + "=" + (d1 - d2) + "=>" + (d1 - d2 + MAX_COUNTER));
                return Double.valueOf(d + MAX_COUNTER);
            }
        }
    }

    /**
     * 计算 EPON设备端口的流量
     * @param value
     * @param type
     * @return
     */
    public static String getFlowDisplayRest(Long value, Integer type) {
        if (value == null) {
            return "";
        }
        long param = 0;
        if (type == 0) {
            param = 30 / 8;
        } else if (type == 1) {
            param = 15 * 60 / 8;
        } else if (type == 2) {
            param = 24 * 60 * 60 / 8;
        }
        if (value < 1024 * param) {
            return (int) (value / param) + "bps";
        } else if (value < 1024 * 1024 * param) {
            return (int) (value / 1024 / param) + "Kbps";
        } else if (value < 1024 * 1024 * 1024 * param) {
            return (int) (value / 1024 / 1024 / param) + "Mbps";
        } else {
            return (int) (value / 1024 / 1024 / 1024 / param) + "Gbps";
        }
    }
}
