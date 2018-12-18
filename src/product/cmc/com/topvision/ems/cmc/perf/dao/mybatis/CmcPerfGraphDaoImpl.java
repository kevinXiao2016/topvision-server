package com.topvision.ems.cmc.perf.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.domain.CmcChannelBasic;
import com.topvision.ems.cmc.perf.dao.CmcPerfGraphDao;
import com.topvision.ems.cmc.performance.facade.CmcTempQualityFor8800B;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.highcharts.domain.Point;

@Repository("cmcPerfGraphDao")
public class CmcPerfGraphDaoImpl extends MyBatisDaoSupport<Entity> implements CmcPerfGraphDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcPerfGraphDao#selectCmcChannelList(java.lang .Long,
     * java.lang.String)
     */

    @Override
    public List<CmcChannelBasic> selectCmcChannelList(Long entityId, String type) {
        List<CmcChannelBasic> channList = new ArrayList<CmcChannelBasic>();
        List<CmcChannelBasic> usChannelList = new ArrayList<CmcChannelBasic>();
        List<CmcChannelBasic> dsChannelList = new ArrayList<CmcChannelBasic>();
        if (PerfTargetConstants.CHANNEL_TYPE_ALL.equals(type)) {
            usChannelList = getSqlSession().selectList(getNameSpace() + "getUpChannelBasicList", entityId);
            for (CmcChannelBasic channelBasic : usChannelList) {
                channelBasic.setChannelType(PerfTargetConstants.CHANNEL_TYPE_US);
                channList.add(channelBasic);
            }
            dsChannelList = getSqlSession().selectList(getNameSpace() + "getDownChannelBasicList", entityId);
            for (CmcChannelBasic channelBasic : dsChannelList) {
                channelBasic.setChannelType(PerfTargetConstants.CHANNEL_TYPE_DS);
                channList.add(channelBasic);
            }
        } else if (PerfTargetConstants.CHANNEL_TYPE_US.equals(type)) {
            usChannelList = getSqlSession().selectList(getNameSpace() + "getUpChannelBasicList", entityId);
            for (CmcChannelBasic channelBasic : usChannelList) {
                channelBasic.setChannelType(PerfTargetConstants.CHANNEL_TYPE_US);
                channList.add(channelBasic);
            }
        }
        return channList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.dao.CmcPerfGraphDao#selectCmcCpuUsedPerfPoints(java.lang.Long,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> selectCmcCpuUsedPerfPoints(Long entityId, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getCmcCpuUsedPerfPoints", paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.dao.CmcPerfGraphDao#selectCmcMemUsedPerfPoints(java.lang.Long,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> selectCmcMemUsedPerfPoints(Long entityId, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getCmcMemUsedPerfPoints", paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.dao.CmcPerfGraphDao#selectCmcFlashUsedPerfPoints(java.lang.Long,
     * java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> selectCmcFlashUsedPerfPoints(Long entityId, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getCmcFlashUsedPerfPoints", paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.dao.CmcPerfGraphDao#selectCmcSnrPerfPoints(java.lang.Long,
     * java.lang.Long, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> selectCmcSnrPerfPoints(Long entityId, Long channelIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getCmcSignalSNRPoints", paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.dao.CmcPerfGraphDao#selectCmcCcerPerfPoints(java.lang.Long,
     * java.lang.Long, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> selectCmcCcerPerfPoints(Long entityId, Long channelIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getCmcCcerPerfPoints", paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.dao.CmcPerfGraphDao#selectCmcUcerPerfPoints(java.lang.Long,
     * java.lang.Long, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> selectCmcUcerPerfPoints(Long entityId, Long channelIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getCmcUcerPerfPoints", paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcPerfGraphDao#selectCmcChannelCmNumberPoints
     * (java.lang.Long, java.lang.String, java.lang.Long, java.lang.String, java.lang.String)
     */

    @Override
    public List<Point> selectCmcChannelCmNumberPoints(Long entityId, String perfTarget, Long channelIndex,
            String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        if (PerfTargetConstants.TOTALCM.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcChannelTotalCmNumberPoints", paramMap);
        } else if (PerfTargetConstants.ONLINECM.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcChannelOnlineCmNumberPoints", paramMap);
        } else if (PerfTargetConstants.OFFLINECM.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcChannelOfflineCmNumberPoints", paramMap);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcPerfGraphDao#selectCmcOptLinkPerfPoints( java.lang.Long,
     * java.lang.String, java.lang.String, java.lang.String)
     */

    @Override
    public List<Point> selectCmcOptLinkPerfPoints(Long entityId, String perfTarget, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        if (PerfTargetConstants.OPT_TEMP.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcOptLinkTempPoints", paramMap);
        } else if (PerfTargetConstants.OPT_TXPOWER.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcOptLinkTxPowerPoints", paramMap);
        } else if (PerfTargetConstants.OPT_RXPOWER.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcOptLinkRxPowerPoints", paramMap);
        } else if (PerfTargetConstants.OPT_CURRENT.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcOptLinkCurrentPoints", paramMap);
        } else if (PerfTargetConstants.OPT_VOLTAGE.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcOptLinkVoltagePoints", paramMap);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcPerfGraphDao#selectCmcUpLinkFlowPoints(java .lang.Long,
     * java.lang.Integer, java.lang.String, java.lang.String)
     */

    @Override
    public List<Point> selectCmcUpLinkFlowPoints(Long entityId, Integer index, Integer direction, String startTime,
            String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("portIndex", index);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        if (PerfTargetConstants.DIRECTION_IN == direction) {
            return getSqlSession().selectList(getNameSpace() + "getCmcUpLinkInFlowPoints", paramMap);
        } else if (PerfTargetConstants.DIRECTION_OUT == direction) {
            return getSqlSession().selectList(getNameSpace() + "getCmcUpLinkOutFlowPoints", paramMap);
        }
        return null;
    }

    @Override
    public List<Point> selectCmcUpLinkUsedPoints(Long entityId, Integer index, Integer direction, String startTime,
            String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("portIndex", index);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        if (PerfTargetConstants.DIRECTION_IN == direction) {
            return getSqlSession().selectList(getNameSpace() + "getCmcUpLinkInUsedPoints", paramMap);
        } else if (PerfTargetConstants.DIRECTION_OUT == direction) {
            return getSqlSession().selectList(getNameSpace() + "getCmcUpLinkOutUsedPoints", paramMap);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcPerfGraphDao#selectCmcChannelSpeedPoints (java.lang.Long,
     * java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
     */

    @Override
    public List<Point> selectCmcChannelSpeedPoints(Long entityId, Long channelIndex, String perfTarget,
            String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        if (PerfTargetConstants.CMC_CHANNELSPEED.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcChannelSpeedPoints", paramMap);
        } else if (PerfTargetConstants.CHANNEL_USED.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcChannelUsedPoints", paramMap);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcPerfGraphDao#selectCmcMacFlowPoints(java .lang.Long,
     * java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
     */

    @Override
    public List<Point> selectCmcMacFlowPoints(Long entityId, Long channelIndex, Integer direction, String startTime,
            String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        if (PerfTargetConstants.DIRECTION_IN == direction) {
            return getSqlSession().selectList(getNameSpace() + "getCmcMacInFlowPoints", paramMap);
        } else if (PerfTargetConstants.DIRECTION_OUT == direction) {
            return getSqlSession().selectList(getNameSpace() + "getCmcMacOutFlowPoints", paramMap);
        } else {
            // CC的ifSpeed暂时不支持，目前对mac域利用率采用速率/信道带宽之和
            return getSqlSession().selectList(getNameSpace() + "getCmcMacUsedPoints", paramMap);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcPerfGraphDao#selectCmcModuleTempPoints(java .lang.Long,
     * java.lang.String, java.lang.String, java.lang.String)
     */

    @Override
    public List<Point> selectCmcTempPoints(Long entityId, String perfTarget, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        if (PerfTargetConstants.US_TEMP.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcUsTempPoints", paramMap);
        } else if (PerfTargetConstants.DS_TEMP.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcDsTempPoints", paramMap);
        } else if (PerfTargetConstants.OUTSIDE_TEMP.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcOutsideTempPoints", paramMap);
        } else if (PerfTargetConstants.INSIDE_TEMP.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcInsideTempPoints", paramMap);
        } else if (PerfTargetConstants.CMC_DOROPTTEMP.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcDorOptTempPoints", paramMap);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcPerfGraphDao#selectCmcPowerModuleTemp(java .lang.Long,
     * java.lang.String, java.lang.String, java.lang.String)
     */

    @Override
    public List<CmcTempQualityFor8800B> selectCmcPowerModuleTemp(Long entityId, String perfTarget, String startTime,
            String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getCmcPowerTempPoints", paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.dao.CmcPerfGraphDao#selectCmcportList(java.lang .Long)
     */

    @Override
    public List<CmcPort> selectCmcportList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcPortList", entityId);
    }

    @Override
    public List<Point> queryCmcRelayPerfPoints(Long entityId, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getCmcRelayPerfPoints", paramMap);
    }

    @Override
    public List<CmcPhyConfig> queryCmcUpLinkPorts(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getCmcUpLinkPorts", entityId);
    }

    @Override
    public String getDomainName() {
        return "com.topvision.ems.cmc.perf.domain.CmcPerfGraph";
    }

    @Override
    public List<Point> queryCmcNetInfoPoints(Long entityId, String perfTarget, Long channelIndex, String startTime,
            String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        paramMap.put("channelIndex", channelIndex);
        if (PerfTargetConstants.CMC_CCUPFLOW.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcMacInFlowPoints", paramMap);
        } else if (PerfTargetConstants.CMC_CCDOWNFLOW.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcMacOutFlowPoints", paramMap);
        } else if (PerfTargetConstants.CMC_CMTXAVG.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcCmTxAvgPoints", paramMap);
        } else if (PerfTargetConstants.CMC_CMTXNOTINRANGE.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcCmTxNotInRangePoints", paramMap);
        } else if (PerfTargetConstants.CMC_CMREAVG.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcCmReAvgPoints", paramMap);
        } else if (PerfTargetConstants.CMC_CMRENOTINRANGE.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcCmReNotInRangePoints", paramMap);
        } else if (PerfTargetConstants.CMC_UPSNRAVG.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcUpSnrAvgPoints", paramMap);
        } else if (PerfTargetConstants.CMC_UPSNRNOTINRANGE.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcUpSnrNotInRangePoints", paramMap);
        } else if (PerfTargetConstants.CMC_DOWNSNRAVG.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcDownSnrAvgPoints", paramMap);
        } else if (PerfTargetConstants.CMC_DOWNSNRNOTINRANGE.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcDownSnrNotInRangePoints", paramMap);
        }
        return null;
    }

    @Override
    public List<Point> queryCmcOnlineCmPoints(Long entityId, Long cmcIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("cmcIndex", cmcIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getCmcOnlineCmPoint", paramMap);
    }

    @Override
    public List<Point> queryCmcAllCmPoints(Long entityId, Long cmcIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("cmcIndex", cmcIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectList(getNameSpace() + "getCmcAllCmPoints", paramMap);
    }

    @Override
    public List<Point> selectCmcVoltagePoints(Long entityId, String perfTarget, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        if (PerfTargetConstants.CMC_DORLINEPOWER.equals(perfTarget)) {
            return getSqlSession().selectList(getNameSpace() + "getCmcOptVoltagePoints", paramMap);
        }
        return null;
    }

}
