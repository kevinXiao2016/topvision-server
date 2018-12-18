package com.topvision.ems.mobile.service;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpCmData;
import com.topvision.ems.cm.pnmp.facade.domain.PnmpTargetThreshold;
import com.topvision.ems.cmc.domain.CmImportInfo;
import com.topvision.ems.mobile.domain.Cm;
import com.topvision.ems.mobile.domain.CmInCmList;
import com.topvision.ems.mobile.domain.CmRelative;
import com.topvision.ems.mobile.domain.CmtsCm;
import com.topvision.ems.mobile.domain.MtrSnrOverlap;
import com.topvision.platform.util.highcharts.domain.Point;

public interface MCmService {
    /**
     * 根据Cmts的Id查询Cmts的下的CM列表
     * 
     * @param map
     * @return
     */
    public List<CmtsCm> getCmListByCmtsId(Map<String, Object> map);

    /**
     * 根据Cmts的Id查询Cmts的下的CM总数
     * 
     * 
     * @param cmtsId
     * @return
     */
    public Long getCmListSizeByCmtsId(Map<String, Object> map);

    /**
     * 根据CmId获得Cm的具体信息
     * 
     * @param cmId
     * @return
     */
    public Cm getCmByCmId(Long cmId);

    /**
     * 刷新Cm的具体信息
     * 
     * @param cmId
     * @return
     */
    public void refreshCm(Long cmId);

    /**
     * 根据Cmts的Id查询Cmts下在线的CM总数
     * 
     * @param map
     * @return
     */
    public Long getOnlineCmListSizeByCmtsId(Map<String, Object> map);

    /**
     * 获取CM的DOCSIS方式
     * 
     * @param cmList
     * @return
     */
    public List<CmtsCm> getRealtimeData(List<CmtsCm> cmList, Long cmcId);

    public List<CmInCmList> getCmList(Map<String, Object> map);

    public Long getCmListCount(Map<String, Object> map);

    /**
     * 获取CM基本信息
     * @param cmId
     * @return
     */
    public CmtsCm getCmBaseInfo(Long cmId);

    /**
     * 获取CMTS下CM列表
     * @param map
     * @return
     */
    public List<CmtsCm> getCmtsCmList(Map<String, Object> map);

    /**
     * 获取CMTS下CM在线数量
     * @param map
     * @return
     */
    public Integer getCmtsCmOnlineCount(Map<String, Object> map);

    /**
     * CMTS下CM总数
     * @param map
     * @return
     */
    public Integer getCmtsCmTotalCount(Map<String, Object> map);

    public List<CmInCmList> getCmListWithRegion(Map<String, Object> map);

    public Long getCmListCountWithRegion(Map<String, Object> map);
    
    /**
     * PING DOCSIS
     * @param
     * @return String
     */
    public String pingDocsis(Long cmtsId,String cmMac);
    
    /**
     * 信道迁移
     * @param
     * @return String
     */
    public String moveCmChannel(Long cmtsId,Long channelId,String cmMac);

    public void updateOrInsertImg(Cm cm);

    public String getCmProgramImg(String cmMac);

    /**
     * 关联cm
     * @param
     * @return List<CmRelative>
     */
    public List<CmRelative> getRelavtiveCm(Map<String, Object> queryMap);

    PnmpCmData realPnmp(Long cmtsId, String cmMac);

    /**
     * mtr,snr叠加曲线
     * @param
     * @return List<Point>
     */
    public List<MtrSnrOverlap> getMtrSnrGraph(String cmMac, long time, long time2);

    public List<Integer> getUpchannelList(Long cmtsId);

    public List<PnmpTargetThreshold> getMtrThresholds();

    public CmImportInfo getCmBossInfo(String cmMac);
}
