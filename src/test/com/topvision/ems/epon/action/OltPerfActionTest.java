package com.topvision.ems.epon.action;

import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import com.topvision.ems.epon.domain.OltPerf;
import com.topvision.ems.epon.performance.action.OltPerfAction;

/**
 * Created by IntelliJ IDEA. User: Administrator Date: 2011-11-21 Time: 15:20:06 To change this
 * template use File | Settings | File Templates.
 */

public class OltPerfActionTest {
    OltPerfAction oltPerfAction;

    @Test
    public void testLoadCurPerfRecord() throws Exception {
        List<OltPerf> oltPerfs = OltPerf.createPerfsList("SNI");
        String str = JSONArray.fromObject(oltPerfs).toString();
        System.out.println("str = " + str);
        JSONArray re = JSONArray.fromObject(str);
        for (Iterator iterator = re.iterator(); iterator.hasNext();) {
            JSONObject o = (JSONObject) iterator.next();
            OltPerf oltPerf = (OltPerf) JSONObject.toBean(o, OltPerf.class);
            System.out.println("oltPerf = " + oltPerf);
        }
    }
}