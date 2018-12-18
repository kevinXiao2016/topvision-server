package com.topvision.ems.mobile.dao;

import java.util.List;
import java.util.Map;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpTargetThreshold;
import com.topvision.ems.cmc.domain.CmImportInfo;
import com.topvision.ems.mobile.domain.Cm;
import com.topvision.ems.mobile.domain.CmCmtsRelation;
import com.topvision.ems.mobile.domain.CmInCmList;
import com.topvision.ems.mobile.domain.CmRelative;
import com.topvision.ems.mobile.domain.CmtsCm;
import com.topvision.ems.mobile.domain.MtrSnrOverlap;

public interface MCmDao {
    /**
     * 根据Cmts的Id查询Cmts下的CM列表
     * 
     * @param map
     * @return
     */
    public List<CmtsCm> getCmListByCmtsId(Map<String, Object> map);

    /**
     * 根据Cmts的Id查询Cmts下的CM总数
     * 
     * 
     * @param cmtsId
     * @return
     */
    public Long getCmListSizeByCmtsId(Map<String, Object> cmtsId);

    /**
     * 根据CmId获得Cm的具体信息
     * 
     * @param cmId
     * @return
     */
    public Cm getCmByCmId(Long cmId);

    /**
     * 查询Cm与Cmts的关系
     * 
     * @param cmId
     * @return
     */
    public CmCmtsRelation getCmCmtsRelation(Long cmId);

    /**
     * 根据Cmts的Id查询Cmts下在线的CM列表
     * 
     * @param map
     * @return
     */
    public Long getOnlineCmListSizeByCmtsId(Map<String, Object> map);

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

    public void updateOrInsertImg(Cm cm);

    public String getCmProgramImg(String cmMac);

    public List<CmRelative> getRelavtiveCm(Map<String, Object> queryMap);

    public List<MtrSnrOverlap> getMtrSnrGraph(String cmMac, long time, long time2);

    public List<Integer> getUpchannelList(Long cmtsId);

    public List<PnmpTargetThreshold> getMtrThresholds();

    public CmImportInfo getCmBossInfo(String cmMac);
}
