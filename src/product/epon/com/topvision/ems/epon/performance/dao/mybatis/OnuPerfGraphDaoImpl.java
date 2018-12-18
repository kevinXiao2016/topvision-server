package com.topvision.ems.epon.performance.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.performance.dao.OnuPerfGraphDao;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.highcharts.domain.Point;

@Repository("onuPerfGraphDao")
public class OnuPerfGraphDaoImpl extends MyBatisDaoSupport<Entity> implements OnuPerfGraphDao {

    @Override
    public List<Point> selectOnuOptPerfPoints(Long onuId, String perfTarget, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("onuId", onuId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        List<Point> plist = new ArrayList<Point>();
        if (PerfTargetConstants.ONU_PON_TXPOWER.equals(perfTarget)) {
            plist = getSqlSession().selectList(getNameSpace() + "getOnuOptTxPowerPoints", paramMap);
        } else if (PerfTargetConstants.ONU_PON_REPOWER.equals(perfTarget)) {
            plist = getSqlSession().selectList(getNameSpace() + "getOnuOptRePowerPoints", paramMap);
        } else if (PerfTargetConstants.OLT_PON_REPOWER.equals(perfTarget)) {
            plist = getSqlSession().selectList(getNameSpace() + "getOltPonRePowerPoints", paramMap);
        }else if(PerfTargetConstants.ONU_CATV_REPOWER.equalsIgnoreCase(perfTarget)){
            plist = getSqlSession().selectList(getNameSpace() + "getOnuCATVRePowerPoints", paramMap);
        }
        return plist;
    }

    @Override
    public List<Point> queryOnuFlowPerfPoints(Map<String, Object> paramMap) {
        List<Point> plist = new ArrayList<Point>();
        int direction = (int) paramMap.get("direction");
        if (direction == PerfTargetConstants.PORTDIRECTION_IN) {
            plist = this.getSqlSession().selectList(getNameSpace("getOnuPortInSpeed"), paramMap);
        } else if (direction == PerfTargetConstants.PORTDIRECTION_OUT) {
            plist = this.getSqlSession().selectList(getNameSpace("getOnuPortOutSpeed"), paramMap);
        }
        return plist;
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.performance.domain.OnuPerfGraph";
    }

}
