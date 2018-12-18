/***********************************************************************
 * $Id: CmcPerfGraphServiceImpl.java,v1.0 2013-8-8 上午10:00:33 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.topvision.ems.cmc.ccmts.facade.domain.CmcPort;
import com.topvision.ems.cmc.domain.CmcChannelBasic;
import com.topvision.ems.cmc.perf.dao.CmcPerfGraphDao;
import com.topvision.ems.cmc.perf.service.CmcPerfGraphService;
import com.topvision.ems.cmc.performance.facade.CmcTempQualityFor8800B;
import com.topvision.ems.cmc.sni.facade.domain.CmcPhyConfig;
import com.topvision.ems.performance.domain.PerfTargetConstants;
import com.topvision.framework.common.NumberUtils;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.util.highcharts.domain.Point;

/**
 * @author lizongtian
 * @created @2013-8-8-上午10:00:33
 * 
 */
@Service("cmcPerfGraphService")
public class CmcPerfGraphServiceImpl extends BaseService implements CmcPerfGraphService {
    @Resource(name = "cmcPerfGraphDao")
    private CmcPerfGraphDao cmcPerfGraphDao;
    private DecimalFormat df = new DecimalFormat("#.00");

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcPerfGraphService#queryCmcChannelList(java.lang.Long,
     * java.lang.String)
     */
    @Override
    public List<CmcChannelBasic> queryCmcChannelList(Long entityId, String type) {
        return cmcPerfGraphDao.selectCmcChannelList(entityId, type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcPerfGraphService#queryCmcServicePerfPoints(java.lang.Long,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> queryCmcServicePerfPoints(Long entityId, String perfTarget, String startTime, String endTime) {
        if (PerfTargetConstants.CMC_CPUUSED.equals(perfTarget)) {
            return cmcPerfGraphDao.selectCmcCpuUsedPerfPoints(entityId, startTime, endTime);
        }
        if (PerfTargetConstants.CMC_MEMUSED.equals(perfTarget)) {
            return cmcPerfGraphDao.selectCmcMemUsedPerfPoints(entityId, startTime, endTime);
        }
        if (PerfTargetConstants.CMC_FLASHUSED.equals(perfTarget)) {
            return cmcPerfGraphDao.selectCmcFlashUsedPerfPoints(entityId, startTime, endTime);
        }
        return new ArrayList<Point>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcPerfGraphService#queryCmcSignalQualityPoints(java.lang.Long,
     * java.lang.String, java.lang.Long, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> queryCmcSignalQualityPoints(Long entityId, String perfTarget, Long channelIndex,
            String startTime, String endTime) {
        if (PerfTargetConstants.CMC_SNR.equals(perfTarget)) {
            List<Point> snrPoints = cmcPerfGraphDao.selectCmcSnrPerfPoints(entityId, channelIndex, startTime, endTime);
            for (Point p : snrPoints) {
                p.setY(p.getY() / 10);
            }
            return snrPoints;
        }
        if (PerfTargetConstants.CCER.equals(perfTarget)) {
            return cmcPerfGraphDao.selectCmcCcerPerfPoints(entityId, channelIndex, startTime, endTime);
        }

        if (PerfTargetConstants.UCER.equals(perfTarget)) {
            return cmcPerfGraphDao.selectCmcUcerPerfPoints(entityId, channelIndex, startTime, endTime);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcPerfGraphService#queryCmcChannelCmNumberPoints(java.lang
     * .Long, java.lang.String, java.lang.Long, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> queryCmcChannelCmNumberPoints(Long entityId, String perfTarget, Long channelIndex,
            String startTime, String endTime) {
        return cmcPerfGraphDao.selectCmcChannelCmNumberPoints(entityId, perfTarget, channelIndex, startTime, endTime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcPerfGraphService#queryCmcOptLinkPerfPoints(java.lang.Long,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> queryCmcOptLinkPerfPoints(Long entityId, String perfTarget, String startTime, String endTime) {
        return cmcPerfGraphDao.selectCmcOptLinkPerfPoints(entityId, perfTarget, startTime, endTime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcPerfGraphService#queryCmcUpLinkFlowPoints(java.lang.Long,
     * java.lang.Integer, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> queryCmcUpLinkFlowPoints(Long entityId, Integer index, Integer direction, String startTime,
            String endTime, String perfTarget) {
        List<Point> pList = new ArrayList<Point>();
        if (PerfTargetConstants.UPLINK_IN_USED.equals(perfTarget)
                || PerfTargetConstants.UPLINK_OUT_USED.equals(perfTarget)) {
            pList = cmcPerfGraphDao.selectCmcUpLinkUsedPoints(entityId, index, direction, startTime, endTime);
        } else {
            pList = cmcPerfGraphDao.selectCmcUpLinkFlowPoints(entityId, index, direction, startTime, endTime);
        }
        for (Point p : pList) {
            if (!direction.equals(0)) {
                p.setY(Double.parseDouble(NumberUtils.TWODOT_FORMAT.format(p.getY())));
                if (p.getY() < 0) {
                    p.setY(-1D);
                }

            }
        }
        return pList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcPerfGraphService#queryCmcChannelSpeedPoints(java.lang.Long,
     * java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> queryCmcChannelSpeedPoints(Long entityId, Long channelIndex, String perfTarget,
            String startTime, String endTime) {
        List<Point> pList = cmcPerfGraphDao.selectCmcChannelSpeedPoints(entityId, channelIndex, perfTarget, startTime,
                endTime);
        if (PerfTargetConstants.CHANNEL_USED.equals(perfTarget)) {
            return pList;
        } else {
            for (Point p : pList) {
                if (p.getY() < 0) {
                    p.setY(-1D);
                }
            }
            return pList;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.service.CmcPerfGraphService#queryCmcMacFlowPoints(java.lang.Long,
     * java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> queryCmcMacFlowPoints(Long entityId, Long channelIndex, Integer direction, String perfTarget,
            String startTime, String endTime) {
        List<Point> pList = cmcPerfGraphDao.selectCmcMacFlowPoints(entityId, channelIndex, direction, startTime,
                endTime);
        List<CmcPort> ports = cmcPerfGraphDao.selectCmcportList(entityId);
        Double bandWidth = 0d;
        for (CmcPort p : ports) {
            // if (p.getIfAdminStatus() == 1) {
            bandWidth = bandWidth + p.getIfSpeed();
            // }
        }
        for (Point p : pList) {
            if (!direction.equals(0)) {
                if (p.getY() < 0 || p.getY() > bandWidth) {
                    p.setY(-1D);
                }
            }
        }
        if (PerfTargetConstants.MAC_USED.equals(perfTarget)) {
            for (Point p : pList) {
                // 转换为利用率
                if (bandWidth == 0) {
                    p.setY(0.0d);
                } else {
                    double value = Double.valueOf(df.format(100 * p.getY() / bandWidth));
                    if (value > 100) {// 超过100%为非正常值
                        p.setY(0.0d);
                    } else {
                        p.setY(value);
                    }
                }
            }
        }
        return pList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcPerfGraphService#queryCmcModuleTempPoints(java.lang.Long,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<Point> queryCmcTempPoints(Long entityId, String perfTarget, String startTime, String endTime) {
        List<Point> pList = cmcPerfGraphDao.selectCmcTempPoints(entityId, perfTarget, startTime, endTime);
        return pList;
    }

    @Override
    public List<Point> queryCmcVoltagePoints(Long entityId, String perfTarget, String startTime, String endTime) {
        List<Point> pList = cmcPerfGraphDao.selectCmcVoltagePoints(entityId, perfTarget, startTime, endTime);
        return pList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.service.CmcPerfGraphService#queryCmcPowerModuleTemp(java.lang.Long,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<CmcTempQualityFor8800B> queryCmcPowerModuleTemp(Long entityId, String perfTarget, String startTime,
            String endTime) {
        return cmcPerfGraphDao.selectCmcPowerModuleTemp(entityId, perfTarget, startTime, endTime);
    }

    @Override
    public List<Point> queryCmcRelayPerfPoints(Long entityId, String startTime, String endTime) {
        return cmcPerfGraphDao.queryCmcRelayPerfPoints(entityId, startTime, endTime);
    }

    @Override
    public List<CmcPhyConfig> queryCmcUpLinkPorts(Long entityId) {
        return cmcPerfGraphDao.queryCmcUpLinkPorts(entityId);
    }

    @Override
    public List<Point> queryCmcNetInfoPoints(Long entityId, String perfTarget, Long channelIndex, String startTime,
            String endTime) {
        return cmcPerfGraphDao.queryCmcNetInfoPoints(entityId, perfTarget, channelIndex, startTime, endTime);
    }

    @Override
    public List<Point> queryCmcOnlineCmPoints(Long entityId, Long cmcIndex, String startTime, String endTime) {
        return cmcPerfGraphDao.queryCmcOnlineCmPoints(entityId, cmcIndex, startTime, endTime);
    }

    @Override
    public List<Point> queryCmcAllCmPoints(Long entityId, Long cmcIndex, String startTime, String endTime) {
        return cmcPerfGraphDao.queryCmcAllCmPoints(entityId, cmcIndex, startTime, endTime);
    }

}
