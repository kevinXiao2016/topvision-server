/***********************************************************************
 * $Id: OnuUpdateDaoImpl.java,v1.0 2013年10月31日 下午7:53:16 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.onu.dao.OnuUpdateDao;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgBand;
import com.topvision.ems.epon.onu.domain.OltOnuAutoUpgProfile;
import com.topvision.ems.epon.onu.domain.OltOnuUpgrade;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.utils.EponConstants;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2013年10月31日-下午7:53:16
 *
 */
@Repository("onuUpdateDao")
public class OnuUpdateDaoImpl extends MyBatisDaoSupport<Object> implements OnuUpdateDao {
    @Override
    public List<OltOnuAutoUpgBand> getOnuAutoUpgBand(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAutoUpgBand", entityId);
    }

    @Override
    public List<OltOnuAutoUpgProfile> getOnuAutoUpgProfile(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAutoUpgProfile", entityId);
    }

    @Override
    public void addOnuAutoUpgProfile(OltOnuAutoUpgProfile profile) {
        getSqlSession().insert(getNameSpace() + "addOnuAutoUpgProfile", profile);
    }

    @Override
    public void modifyOnuAutoUpgProfile(OltOnuAutoUpgProfile profile) {
        getSqlSession().update(getNameSpace() + "updateOnuAutoUpgProfile", profile);
    }

    @Override
    public void delOnuAutoUpgProfile(Long entityId, Integer profileId) {
        OltOnuAutoUpgProfile profile = new OltOnuAutoUpgProfile();
        profile.setEntityId(entityId);
        profile.setProfileId(profileId);
        getSqlSession().delete(getNameSpace() + "delOnuAutoUpgProfile", profile);
    }

    @Override
    public void bandOnuAutoUpgProfile(OltOnuAutoUpgBand band) {
        OltPonAttribute m = new OltPonAttribute();
        m.setEntityId(band.getEntityId());
        if (band.getPonIndex() != null && band.getPonIndex() > 0) {
            m.setPonIndex(band.getPonIndex());
        } else if (band.getSlotNo() != null && band.getPonNo() != null) {
            m.setPonIndex(EponIndex.getPonIndex(band.getSlotNo(), band.getPonNo()));
        }
        Long ponIdTmp = (Long) getSqlSession().selectOne("com.topvision.ems.epon.olt.domain.OltPon." + "getPonId", m);
        if (ponIdTmp != null) {
            band.setPonId(ponIdTmp);
        }
        getSqlSession().insert(getNameSpace() + "bandOnuAutoUpgProfile", band);
    }

    @Override
    public void unbandOnuAutoUpgProfile(OltOnuAutoUpgBand band) {
        getSqlSession().delete(getNameSpace() + "unbandOnuAutoUpgProfile", band);
    }

    @Override
    public void updateOnuAutoUpgBandStat(OltOnuAutoUpgProfile profile) {
        getSqlSession().update(getNameSpace() + "updateOnuAutoUpgBandStat", profile);
    }

    @Override
    public void refreshOnuAutoUpgProfile(final List<OltOnuAutoUpgProfile> profileList, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOnuAutoUpgProfile", entityId);
            if (profileList.size() > 0) {
                for (OltOnuAutoUpgProfile o : profileList) {
                    o.setEntityId(entityId);
                    String proOnuTypeStr = o.getProOnuTypeStr();
                    if (!proOnuTypeStr.equalsIgnoreCase("none")
                            && EponConstants.EPON_ONU_PRETYPE.containsKey(proOnuTypeStr)) {
                        o.setProOnuType(Integer.parseInt(proOnuTypeStr.substring(2, 3)) * 16
                                + Integer.parseInt(proOnuTypeStr.substring(3, 4)));
                    } else if (proOnuTypeStr.equalsIgnoreCase("CC8800A") || proOnuTypeStr.equalsIgnoreCase("8800")) {
                        o.setProOnuType(241);
                    } else {
                        o.setProOnuType(0);
                    }
                    sqlSession.insert(getNameSpace() + "addOnuAutoUpgProfile", o);
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void refreshOnuAutoUpgBand(final List<OltOnuAutoUpgBand> bandList, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOnuAutoUpgBand", entityId);
            if (bandList.size() > 0) {
                for (OltOnuAutoUpgBand o : bandList) {
                    o.setEntityId(entityId);
                    o.setPonIndex(EponIndex.getPonIndex(o.getSlotNo(), o.getPonNo()));
                    OltPonAttribute m = new OltPonAttribute();
                    m.setEntityId(entityId);
                    m.setPonIndex(o.getPonIndex());
                    Long ponIdTmp = (Long) sqlSession.selectOne("com.topvision.ems.epon.olt.domain.OltPon."
                            + "getPonId", m);
                    if (ponIdTmp != null) {
                        o.setPonId(ponIdTmp);
                    }
                    sqlSession.insert(getNameSpace() + "bandOnuAutoUpgProfile", o);
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.error("", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void insertOnuUpgrade(OltOnuUpgrade oltOnuUpgrade) {
        getSqlSession().insert(getNameSpace() + "insertOnuUpgrade", oltOnuUpgrade);
    }

    @Override
    public List<OltOnuUpgrade> getOnuUpgradeHistory(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuUpgradeHistory", entityId);
    }

    @Override
    public List<Long> getOnuIndexListByHwVeList(Long entityId, String hwVersion) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("hwVersion", hwVersion);
        return getSqlSession().selectList(getNameSpace() + "getOnuIndexListByHwVeList", paramMap);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onu.domain.OnuUpdate";
    }
}
