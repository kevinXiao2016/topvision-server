/***********************************************************************
 * $Id: OltOpticalDaoImpl.java,v1.0 2013-10-26 上午09:30:29 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.optical.dao.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.optical.dao.OltOpticalDao;
import com.topvision.ems.epon.optical.domain.OltOnuOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.optical.domain.OltPonOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OltSniOptical;
import com.topvision.ems.epon.optical.domain.OltSysOpticalAlarm;
import com.topvision.ems.epon.optical.domain.OnuPonOptical;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author lizongtian
 * @created @2013-10-26-上午09:30:29
 *
 */
@Repository("oltOpticalDao")
public class OltOpticalDaoImpl extends MyBatisDaoSupport<Object> implements OltOpticalDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#getOltSniOptical(java.lang.Long)
     */
    @Override
    public OltSniOptical getOltSniOptical(Long sniId) {
        return getSqlSession().selectOne(getNameSpace("getOltSniOptical"), sniId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#getOltPonOptical(java.lang.Long)
     */
    @Override
    public OltPonOptical getOltPonOptical(Long ponId) {
        return getSqlSession().selectOne(getNameSpace("getOltPonOptical"), ponId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#getAllPonOptical(java.lang.Long)
     */
    @Override
    public List<OltPonOptical> getAllPonOptical(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getAllPonOptical"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#getAllSniOptical(java.lang.Long)
     */
    @Override
    public List<OltSniOptical> getAllSniOptical(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getAllSniOptical"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#addOltPonOptical(com.topvision.
     * ems.epon.facade.domain.OltPonOptical)
     */
    @Override
    public void addOltPonOptical(OltPonOptical oltPonOptical) {
        getSqlSession().insert(getNameSpace("addOltPonOptical"), oltPonOptical);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#updateOltPonOptical(com.topvision
     * .ems.epon.facade.domain.OltPonOptical)
     */
    @Override
    public void updateOltPonOptical(OltPonOptical oltPonOptical) {
        getSqlSession().update(getNameSpace("updateOltPonOptical"), oltPonOptical);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#addOltSniOptical(com.topvision.
     * ems.epon.facade.domain.OltSniOptical)
     */
    @Override
    public void addOltSniOptical(OltSniOptical oltSniOptical) {
        getSqlSession().insert(getNameSpace("addOltSniOptical"), oltSniOptical);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#updateOltSniOptical(com.topvision
     * .ems.epon.facade.domain.OltSniOptical)
     */
    @Override
    public void updateOltSniOptical(OltSniOptical oltSniOptical) {
        getSqlSession().update(getNameSpace("updateOltSniOptical"), oltSniOptical);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#batchInsertPonOptical(java.util .List)
     */
    @Override
    public void batchInsertPonOptical(final List<OltPonOptical> oltPonOpticals, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            List<OltPonOptical> oldPons = sqlSession.selectList(getNameSpace("getAllPonOptical"), entityId);
            List<Long> oldIndex = new ArrayList<Long>();
            if (oldPons != null && oldPons.size() > 0) {
                for (OltPonOptical old : oldPons) {
                    oldIndex.add(old.getPortIndex());
                }
            }
            for (OltPonOptical pon : oltPonOpticals) {
                if (oldIndex.contains(pon.getPortIndex())) {
                    sqlSession.update(getNameSpace("updateOltPonOptical"), pon);
                } else {
                    sqlSession.insert(getNameSpace("addOltPonOptical"), pon);
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

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#batchInsertSniOptical(java.util .List)
     */
    @Override
    public void batchInsertSniOptical(final List<OltSniOptical> oltSniOpticals, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            List<OltSniOptical> oldSnis = sqlSession.selectList(getNameSpace("getAllSniOptical"), entityId);
            List<Long> oldIndex = new ArrayList<Long>();
            if (oldSnis != null && oldSnis.size() > 0) {
                for (OltSniOptical old : oldSnis) {
                    oldIndex.add(old.getPortIndex());
                }
            }
            for (OltSniOptical sni : oltSniOpticals) {
                if (oldIndex.contains(sni.getPortIndex())) {
                    sqlSession.update(getNameSpace("updateOltSniOptical"), sni);
                } else {
                    sqlSession.insert(getNameSpace("addOltSniOptical"), sni);
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            logger.debug("batchInsertSniOptical error {}", e);
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#batchInsertOnuPonOptical(java.util .List,
     * java.lang.Long)
     */
    @Override
    public void batchInsertOnuPonOptical(final List<OnuPonOptical> onuPonOpticals, final Long entityId) {
        if (onuPonOpticals != null && onuPonOpticals.size() > 0) {
            SqlSession sqlSession = getBatchSession();
            try {
                List<OnuPonOptical> oldOnuPons = sqlSession.selectList(getNameSpace("getAllOnuOptical"), entityId);
                List<Long> oldIndex = new ArrayList<Long>();
                if (oldOnuPons != null && oldOnuPons.size() > 0) {
                    for (OnuPonOptical old : oldOnuPons) {
                        oldIndex.add(old.getOnuPonIndex());
                    }
                }
                for (OnuPonOptical op : onuPonOpticals) {
                    op.setOnuPonId((Long) sqlSession.selectOne(getNameSpace("getOnuPonId"), op));
                    if (oldIndex.contains(op.getOnuPonIndex())) {
                        sqlSession.update(getNameSpace("updateOnuPonOptical"), op);
                    } else {
                        sqlSession.insert(getNameSpace("addOnuPonOptical"), op);
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#getOnuPonOptical(java.lang.Long, java.lang.Long)
     */
    @Override
    public OnuPonOptical getOnuPonOptical(Long entityId, Long onuPonIndex) {
        OnuPonOptical onuPon = new OnuPonOptical();
        onuPon.setEntityId(entityId);
        onuPon.setOnuPonIndex(onuPonIndex);
        Long onuPonId = getSqlSession().selectOne(getNameSpace("getOnuPonId"), onuPon);
        return getSqlSession().selectOne(getNameSpace("getOnuPonOptical"), onuPonId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#addOnuPonOptical(com.topvision.
     * ems.epon.facade.domain.OnuPonOptical)
     */
    @Override
    public void addOnuPonOptical(OnuPonOptical onuPonOptical) {
        getSqlSession().insert(getNameSpace("addOnuPonOptical"), onuPonOptical);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#updateOnuPonOptical(com.topvision
     * .ems.epon.facade.domain.OnuPonOptical)
     */
    @Override
    public void updateOnuPonOptical(OnuPonOptical onuPonOptical) {
        getSqlSession().update(getNameSpace("updateOnuPonOptical"), onuPonOptical);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#getAllOnuOptical(java.lang.Long)
     */
    @Override
    public List<OnuPonOptical> getAllOnuOptical(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getAllOnuOptical"), entityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#getOnuPonId(java.lang.Long, java.lang.Long)
     */
    @Override
    public Long getOnuPonId(Long entityId, Long onuPonIndex) {
        OnuPonOptical onuPonOptical = new OnuPonOptical();
        onuPonOptical.setEntityId(entityId);
        onuPonOptical.setOnuPonIndex(onuPonIndex);
        return getSqlSession().selectOne(getNameSpace("getOnuPonId"), onuPonOptical);
    }

    @Override
    public OltSysOpticalAlarm querySysOpticalAlarm(OltSysOpticalAlarm sysOpticalAlarm) {
        return getSqlSession().selectOne(getNameSpace("getSysOpticalAlarm"), sysOpticalAlarm);
    }

    @Override
    public List<OltSysOpticalAlarm> querySysOpticalAlarmList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("getSysOpticalAlarmList"), entityId);
    }

    @Override
    public void insertSysOpticalAlarm(OltSysOpticalAlarm sysOpticalAlarm) {
        getSqlSession().insert(getNameSpace("insertSysOpticalAlarm"), sysOpticalAlarm);
    }

    @Override
    public void updateSysOpticalAlarm(OltSysOpticalAlarm sysOpticalAlarm) {
        getSqlSession().update(getNameSpace("updateSysOpticalAlarm"), sysOpticalAlarm);
    }

    @Override
    public void deleteSysOpticalAlarm(OltSysOpticalAlarm sysOpticalAlarm) {
        getSqlSession().delete(getNameSpace("delSysOpticalAlarm"), sysOpticalAlarm);
    }

    @Override
    public List<OltOnuOpticalAlarm> queryOnuOpticalAlarmList(OltOnuOpticalAlarm onuOptical) {
        return getSqlSession().selectList(getNameSpace("getOnuOpticalAlarmList"), onuOptical);
    }

    @Override
    public void insertOnuOpticalAlarm(OltOnuOpticalAlarm onuOpticalAlarm) {
        getSqlSession().insert(getNameSpace("insertOnuOpticalAlarm"), onuOpticalAlarm);
    }

    @Override
    public void batchInsertOnuOpticalAlarm(final List<OltOnuOpticalAlarm> onuOpticalAlarmList, final Long entityId) {
        getSqlSession().delete(getNameSpace("deleteAllOnuOpticalAlarm"), entityId);
        for (OltOnuOpticalAlarm onuOpticalAlarm : onuOpticalAlarmList) {
            onuOpticalAlarm.setEntityId(entityId);
            getSqlSession().insert(getNameSpace("insertOnuOpticalAlarm"), onuOpticalAlarm);
        }
    }

    @Override
    public List<OltPonOpticalAlarm> queryPonOpticalAlarmList(OltPonOpticalAlarm ponOptical) {
        return getSqlSession().selectList(getNameSpace("getPonOpticalAlarmList"), ponOptical);
    }

    @Override
    public void insertPonOpticalAlarm(OltPonOpticalAlarm ponOpticalAlarm) {
        getSqlSession().insert(getNameSpace("insertPonOpticalAlarm"), ponOpticalAlarm);
    }

    @Override
    public void batchInsertPonOpticalAlarm(final List<OltPonOpticalAlarm> ponOpticalAlarmList, final Long entityId) {
        getSqlSession().delete(getNameSpace("deleteAllPonOpticalAlarm"), entityId);
        for (OltPonOpticalAlarm ponOpticalAlarm : ponOpticalAlarmList) {
            ponOpticalAlarm.setEntityId(entityId);
            getSqlSession().insert(getNameSpace("insertPonOpticalAlarm"), ponOpticalAlarm);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#batchInsertOltOptical(java.util.List,
     * java.lang.Long)
     */
    @Override
    public void batchInsertOltOptical(final List<OltSysOpticalAlarm> oltPonOpticals, final Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace("deleteAllOltOptical"), entityId);
            for (OltSysOpticalAlarm alarm : oltPonOpticals) {
                alarm.setEntityId(entityId);
                sqlSession.insert(getNameSpace("insertSysOpticalAlarm"), alarm);
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#updateOltPonOptAlarm(java.util.List,
     * java.lang.Long)
     */
    @Override
    public void updateOltPonOptAlarm(final List<OltPonOpticalAlarm> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltPonOpticalAlarm thresh : list) {
                sqlSession.delete(getNameSpace("deletePonOpticalAlarm"), thresh);
                sqlSession.insert(getNameSpace("insertPonOpticalAlarm"), thresh);
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void deleteOltPonOptAlarm(OltPonOpticalAlarm thresh) {
        getSqlSession().delete(getNameSpace("deletePonOpticalAlarm"), thresh);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OpticalDao#updateOltOnuOptAlarm(java.util.List,
     * java.lang.Long)
     */
    @Override
    public void updateOltOnuOptAlarm(final List<OltOnuOpticalAlarm> list, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltOnuOpticalAlarm thresh : list) {
                sqlSession.delete(getNameSpace("deleteOltOnuOptAlarm"), thresh);
                sqlSession.insert(getNameSpace("insertOnuOpticalAlarm"), thresh);
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

    @Override
    public void deleteOnuOptAlarm(OltOnuOpticalAlarm thresh) {
        getSqlSession().delete(getNameSpace("deleteOltOnuOptAlarm"), thresh);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.optical.domain.Optical";
    }

}
