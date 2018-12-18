/***********************************************************************
 * $Id: CmcPerfCommonDapImpl.java,v1.0 2013-12-2 上午11:37:07 $
 * 
 * @author: Rod John
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.perf.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.perf.dao.CmcPerfCommonDao;
import com.topvision.ems.cmc.performance.domain.CmcChannelStaticInfo;
import com.topvision.ems.cmc.performance.domain.CmcPerfCommon;
import com.topvision.ems.cmc.performance.domain.CmcServiceQualityStatic;
import com.topvision.ems.cmc.performance.domain.CmcSignalQualityStatic;
import com.topvision.ems.cmc.performance.facade.CmcFlowQuality;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Rod John
 * @created @2013-12-2-上午11:37:07
 * 
 */
@Repository("cmcPerfCommonDao")
public class CmcPerfCommonDapImpl extends MyBatisDaoSupport<Object> implements CmcPerfCommonDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.dao.CmcPerfCommonDao#loadCmcServiceQuality(java.lang.Long)
     */
    @Override
    public CmcServiceQualityStatic loadCmcServiceQuality(Long cmcId) {
        List<CmcPerfCommon> perfCommons = getSqlSession().selectList(getNameSpace("loadCmcServiceQuality"), cmcId);
        CmcServiceQualityStatic cmcServiceQualityStatic = new CmcServiceQualityStatic();
        cmcServiceQualityStatic.setCmcId(cmcId);
        cmcServiceQualityStatic.setPerfCommons(perfCommons);
        return cmcServiceQualityStatic;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.dao.CmcPerfCommonDao#loadCmcSignalQuality(java.lang.Long)
     */
    @Override
    public List<CmcSignalQualityStatic> loadCmcSignalQuality(Long cmcId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cmcId", cmcId);
        paramMap.put("targetName", CmcSignalQualityStatic.SNR);
        // 根据需求支持SNR的性能接口
        List<CmcPerfCommon> snrPerfCommon = getSqlSession().selectList(getNameSpace("loadCmcSignalQuality"), paramMap);
        List<CmcSignalQualityStatic> statics = new ArrayList<CmcSignalQualityStatic>();
        for (CmcPerfCommon snrCommon : snrPerfCommon) {
            CmcSignalQualityStatic cmcSignalQualityStatic = new CmcSignalQualityStatic();
            cmcSignalQualityStatic.setCmcId(cmcId);
            cmcSignalQualityStatic.setChannelIndex(snrCommon.getChannelIndex());
            cmcSignalQualityStatic.setSnr(snrCommon);
            statics.add(cmcSignalQualityStatic);
        }
        return statics;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.dao.CmcPerfCommonDao#loadCmcSignalQuality(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public CmcSignalQualityStatic loadCmcSignalQuality(Long cmcId, Long channelIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cmcId", cmcId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("targetName", CmcSignalQualityStatic.SNR);
        // 根据需求支持SNR的性能接口
        CmcPerfCommon snrPerfCommon = getSqlSession().selectOne(getNameSpace("loadCmcSignalQualityByChannelIndex"),
                paramMap);
        CmcSignalQualityStatic cmcSignalQualityStatic = new CmcSignalQualityStatic();
        cmcSignalQualityStatic.setCmcId(cmcId);
        cmcSignalQualityStatic.setChannelIndex(channelIndex);
        cmcSignalQualityStatic.setSnr(snrPerfCommon);
        return cmcSignalQualityStatic;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.dao.CmcPerfCommonDao#loadCmcFlowQuality(java.lang.Long)
     */
    @Override
    public List<CmcFlowQuality> loadCmcFlowQuality(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("loadCmcFlowQuality"), cmcId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.dao.CmcPerfCommonDao#loadCmcFlowQuality(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public CmcFlowQuality loadCmcFlowQuality(Long cmcId, Long channelIndex) {
        Map<String, Long> paramMap = new HashMap<String, Long>();
        paramMap.put("cmcId", cmcId);
        paramMap.put("channelIndex", channelIndex);
        return getSqlSession().selectOne(getNameSpace("loadCmcFlowQualityByChannelIndex"), paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.cmc.perf.dao.CmcPerfCommonDao#loadCmcChannelFlowStatic(java.lang.Long,
     * java.lang.Long, java.lang.Long, java.lang.Long)
     */
    @Override
    public CmcChannelStaticInfo loadCmcChannelFlowStatic(Long cmcId, Long channelIndex, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cmcId", cmcId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectOne(getNameSpace("loadCmcChannelFlowStatic"), paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.cmc.perf.dao.CmcPerfCommonDao#loadCmcChannelSignalStatic(java.lang.Long,
     * java.lang.Long, java.lang.Long, java.lang.Long)
     */
    @Override
    public CmcChannelStaticInfo loadCmcChannelSignalStatic(Long cmcId, Long channelIndex, String startTime,
            String endTime) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("cmcId", cmcId);
        paramMap.put("channelIndex", channelIndex);
        paramMap.put("targetName", "snr");
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return getSqlSession().selectOne(getNameSpace("loadCmcChannelSignalStatic"), paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.perf.domain.CmcPerfCommon";
    }

}
