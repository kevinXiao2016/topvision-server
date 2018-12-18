package com.topvision.ems.epon.domain;

import java.util.TreeMap;

import org.junit.Test;

import com.topvision.ems.epon.performance.domain.PerfCurStatsTable;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 2011-11-21 Time: 20:10:05 To change this
 * template use File | Settings | File Templates.
 */

public class OltPerfTest {

    @Test
    public void testCreatePerfMap() throws Exception {
        PerfCurStatsTable pcst = new PerfCurStatsTable();
        // pcst.setCurStatsInBroadcastPkts(10L);
        // pcst.setCurStatsInMpcpFrames(11L);
        // pcst.setCurStatsInOAMOctets(12L);
        // pcst.setCurStatsInPkts256to511Octets(13L);
        // pcst.setCurStatsOutOAMOctets(14L);
        // pcst.setCurStatsOutPkts65to127Octets(15L);
        TreeMap<Integer, Long> re = new TreeMap<Integer, Long>(OltPerf.createPerfMap(pcst));
        for (Integer index : re.keySet()) {
            System.out.print("index = " + index);
            System.out.println("\t value = " + re.get(index));
        }
    }
}