package com.topvision.ems.cm.cmtsInfoSummary.engine.dao.mybatis;

import java.util.List;

import com.topvision.ems.cm.cmtsInfoSummary.domain.CmMainChannel;
import com.topvision.ems.cm.cmtsInfoSummary.domain.CmtsInfoStatistics;
import com.topvision.ems.cm.cmtsInfoSummary.engine.dao.CmtsInfoSummaryDao;
import com.topvision.ems.cmc.cmtsInfo.domain.CmtsInfoThreshold;
import com.topvision.ems.engine.dao.EngineDaoSupport;

public class CmtsInfoSummaryDaoImpl extends EngineDaoSupport<CmtsInfoThreshold> implements CmtsInfoSummaryDao {

    @Override
    public CmtsInfoThreshold selectLocalThreshold() {
        return getSqlSession().selectOne(getNameSpace("selectCmtsInfoThreshold"));
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cm.cmtsInfoSummary.domain.CmtsInfoThreshold";
    }

    @Override
    public void insertCmtsInfoSummary(List<CmtsInfoStatistics> cmtsInfoStatistics) {
        for(CmtsInfoStatistics cis:cmtsInfoStatistics){
            getSqlSession().insert(getNameSpace("insertCmtsInfoSummary"),cis); 
        }        
    }

    @Override
    public void updateCmtsInfoSummaryLst(List<CmtsInfoStatistics> cmtsInfoStatistics) {
        getSqlSession().delete(getNameSpace("deletCmtsInfoSummaryLast"));
        for(CmtsInfoStatistics cis:cmtsInfoStatistics){
            getSqlSession().insert(getNameSpace("insertOrUpdateCmtsInfoSummary"),cis); 
        }
    }

    @Override
    public List<CmMainChannel> selectCmAttr() {
        return getSqlSession().selectList(getNameSpace("selectMainChannelFromCmattribute"));
    }

}
