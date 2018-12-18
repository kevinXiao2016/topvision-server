package com.topvision.ems.mobile.dao.mybatis;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cm.pnmp.facade.domain.PnmpTargetThreshold;
import com.topvision.ems.cmc.domain.CmImportInfo;
import com.topvision.ems.mobile.dao.MCmDao;
import com.topvision.ems.mobile.domain.Cm;
import com.topvision.ems.mobile.domain.CmCmtsRelation;
import com.topvision.ems.mobile.domain.CmInCmList;
import com.topvision.ems.mobile.domain.CmRelative;
import com.topvision.ems.mobile.domain.CmtsCm;
import com.topvision.ems.mobile.domain.MtrSnrOverlap;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

@Repository("mCmDao")
public class MCmDaoImpl extends MyBatisDaoSupport<CmtsCm> implements MCmDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.mobile.domain.CmtsCm";
    }

    @Override
    public List<CmtsCm> getCmListByCmtsId(Map<String, Object> map) {
        List<CmtsCm> cmList = getSqlSession().selectList(getNameSpace("getCmListByCmcId"), map);
        return cmList;
    }

    @Override
    public Long getCmListSizeByCmtsId(Map<String, Object> map) {
        Long size = getSqlSession().selectOne(getNameSpace("getCmListSizeByCmtsId"), map);
        return size;
    }

    @Override
    public Cm getCmByCmId(Long cmId) {
        Cm cm = getSqlSession().selectOne(getNameSpace("getCmByCmId"), cmId);
        return cm;
    }

    @Override
    public CmCmtsRelation getCmCmtsRelation(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("getCmCmtsRelation"), cmId);
    }

    @Override
    public Long getOnlineCmListSizeByCmtsId(Map<String, Object> map) {
        Long size = getSqlSession().selectOne(getNameSpace("getOnlineCmListSizeByCmtsId"), map);
        return size;
    }

    @Override
    public List<CmInCmList> getCmList(Map<String, Object> map) {
        List<CmInCmList> list = getSqlSession().selectList(getNameSpace("getCmList"), map);
        return list;
    }

    @Override
    public Long getCmListCount(Map<String, Object> map) {
        Long size = getSqlSession().selectOne(getNameSpace("getCmListCount"), map);
        return size;
    }

    @Override
    public CmtsCm getCmBaseInfo(Long cmId) {
        return this.getSqlSession().selectOne(getNameSpace("getCmBaseInfo"), cmId);
    }

    @Override
    public List<CmtsCm> getCmtsCmList(Map<String, Object> map) {
        return this.getSqlSession().selectList(getNameSpace("getCmtsCmList"), map);
    }

    @Override
    public Integer getCmtsCmOnlineCount(Map<String, Object> map) {
        return this.getSqlSession().selectOne(getNameSpace("getCmtsCmOnlineCount"), map);
    }

    @Override
    public Integer getCmtsCmTotalCount(Map<String, Object> map) {
        return this.getSqlSession().selectOne(getNameSpace("getCmtsCmTotalCount"), map);
    }

    @Override
    public List<CmInCmList> getCmListWithRegion(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        List<CmInCmList> list = getSqlSession().selectList(getNameSpace("getCmListWithRegion"), map);
        return list;
    }

    @Override
    public Long getCmListCountWithRegion(Map<String, Object> map) {
        map.put("Authority", CurrentRequest.getUserAuthorityViewName());
        Long size = getSqlSession().selectOne(getNameSpace("getCmListCountWithRegion"), map);
        return size;
    }

    @Override
    public void updateOrInsertImg(Cm cm) {
        getSqlSession().update(getNameSpace("updateOrInsertImg"),cm);
    }

    @Override
    public String getCmProgramImg(String cmMac) {
        return getSqlSession().selectOne(getNameSpace("getCmProgramImg"), cmMac);
    }

    @Override
    public List<CmRelative> getRelavtiveCm(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("getRelavtiveCm"), queryMap);
    }

    @Override
    public List<MtrSnrOverlap> getMtrSnrGraph(String cmMac, long time, long time2) {
        Map<String,String>map=new HashMap<String,String>();
        SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH-mm-ss");
        map.put("cmMac", cmMac);
        map.put("startTime", sdf.format(new Date(time)));
        map.put("endTime", sdf.format(new Date(time2)));
        return getSqlSession().selectList(getNameSpace("getMtrSnrGraph"), map);
    }

    @Override
    public List<Integer> getUpchannelList(Long cmtsId) {
        return getSqlSession().selectList(getNameSpace("getUpchannelList"), cmtsId);
    }

    @Override
    public List<PnmpTargetThreshold> getMtrThresholds() {
        return getSqlSession().selectList(getNameSpace("getMtrThresholds"));
    }

    @Override
    public CmImportInfo getCmBossInfo(String cmMac) {
        return getSqlSession().selectOne(getNameSpace("getCmBossInfo"), cmMac);
    }

}
