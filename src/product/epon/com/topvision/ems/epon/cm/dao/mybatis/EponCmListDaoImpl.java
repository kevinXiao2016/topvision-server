package com.topvision.ems.epon.cm.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.cm.dao.EponCmListDao;
import com.topvision.ems.epon.domain.EponCmNumStatic;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.performance.domain.CmtsCmNum;
import com.topvision.ems.epon.performance.domain.PonCmNum;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("eponCmListDao")
public class EponCmListDaoImpl extends MyBatisDaoSupport<Entity> implements EponCmListDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.cm.domain.EponCmList";
    }

    @Override
    public List<OltPonAttribute> loadPonOfOlt(Long entityId) {
        // 此方法不需要进行权限限制，因为OLT已经做了权限限制
        List<OltPonAttribute> pons = getSqlSession().selectList(getNameSpace("loadPonOfOlt"), entityId);
        return pons;
    }

    @Override
    public OltPonAttribute loadPonAttrByCmcId(Long cmcId) {
        OltPonAttribute pon = getSqlSession().selectOne(getNameSpace("loadPonAttrByCmcId"), cmcId);
        return pon;
    }

    @Override
    public EponCmNumStatic getEponCmNumStatic(Long entityId) {
        return getSqlSession().selectOne(getNameSpace("getEponCmNumStatic"), entityId);
    }

    @Override
    public List<PonCmNum> getPonCmNumList(Long entityId) {
        List<PonCmNum> pons = getSqlSession().selectList(getNameSpace("getPonCmNumList"), entityId);
        return pons;
    }

    @Override
    public List<CmtsCmNum> getCmtsCmNumList(Long entityId) {
        List<CmtsCmNum> cmtss = getSqlSession().selectList(getNameSpace("getCmtsCmNumList"), entityId);
        return cmtss;
    }

}