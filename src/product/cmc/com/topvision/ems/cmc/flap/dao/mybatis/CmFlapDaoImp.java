package com.topvision.ems.cmc.flap.dao.mybatis;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.flap.dao.CmFlapDao;
import com.topvision.ems.cmc.performance.domain.CMFlapHis;
import com.topvision.ems.cmc.performance.facade.CmFlap;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

@Repository("cmFlapDao")
public class CmFlapDaoImp extends MyBatisDaoSupport<CMFlapHis> implements CmFlapDao {

    @Override
    public List<CMFlapHis> getCmFlapHis(String cmMac, Long startTime, Long endTime) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("cmMac", cmMac);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        param.put("startTime", dateFormat.format(new Date(startTime)));
        param.put("endTime", dateFormat.format(new Date(endTime)));

        List<CMFlapHis> flapHisList = getSqlSession().selectList(getNameSpace() + "selectOneCmFlapHisByTime", param);
        return flapHisList;
    }

    @Override
    public CmFlap queryForLastCmFlapByCmId(Long cmId) {
        return getSqlSession().selectOne(getNameSpace() + "getLastCmFlapByCmId", cmId);
    }

    @Override
    public void deleteCmFlapByEntityId(Long entityId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("entityId", entityId);
        getSqlSession().delete(getNameSpace() + "deleteCmFlapByCmcId", map);
    }

    @Override
    public void batchInsertOrUpdateCmFlap(final List<CmFlap> cmFlapList, final Long entityId) {
        this.deleteCmFlapByEntityId(entityId);
        SqlSession session = getBatchSession();
        try {
            for (CmFlap cmFlap : cmFlapList) {
                logger.debug("CM FLAP MAC=" + cmFlap.getTopCmFlagMacAddrString() + " FLAP INS="
                        + cmFlap.getTopCmFlapInsertionFailNum());
                String cmMac = cmFlap.getTopCmFlagMacAddrString();
                // add by fanzidong,在查询前需要对MAC地址进行格式化
                cmMac = MacUtils.convertToMaohaoFormat(cmMac);
                cmFlap.setTopCmFlagMacAddrString(cmMac);
                cmFlap.setDt(new Timestamp(System.currentTimeMillis()));
                // 通过cm的mac获取cmAttribute
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("cmMac", cmMac);
                map.put("entityId", entityId);
                CmAttribute cmAttribute = ((CmAttribute) getSqlSession().selectOne(
                        getNameSpace() + "getCmAttributeByCmMac", map));
                if (cmAttribute != null) {
                    cmFlap.setCmcId(cmAttribute.getCmcId());
                    cmFlap.setCmId(cmAttribute.getCmId());
                    cmFlap.setDt(new Timestamp(System.currentTimeMillis()));
                    if (cmFlap.getTopCmFlapInsertionFailNum() == null) {
                        cmFlap.setTopCmFlapInsertionFailNum(0);
                    }
                    if (cmFlap.getTopCmFlapHitNum() == null) {
                        cmFlap.setTopCmFlapHitNum(0);
                    }
                    if (cmFlap.getTopCmFlapMissNum() == null) {
                        cmFlap.setTopCmFlapMissNum(0);
                    }
                    if (cmFlap.getTopCmFlapCrcErrorNum() == null) {
                        cmFlap.setTopCmFlapCrcErrorNum(0);
                    }
                    if (cmFlap.getTopCmFlapPowerAdjLowerNum() == null) {
                        cmFlap.setTopCmFlapPowerAdjLowerNum(0);
                    }
                    if (cmFlap.getTopCmFlapPowerAdjHigherNum() == null) {
                        cmFlap.setTopCmFlapPowerAdjHigherNum(0);
                    }
                } else {
                    continue;
                }
                // this.wirteFlapToFile(cmFlap);
                if (((Long) getSqlSession().selectOne(getNameSpace() + "getCmFlapByCmMac", map)) != null) {
                    getSqlSession().update(getNameSpace() + "updateCmFlap", cmFlap);
                } else {
                    session.insert(getNameSpace() + "insertCmFlap", cmFlap);
                }
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.flap.domain.CmFlapHis";
    }

}
