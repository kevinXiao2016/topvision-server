/***********************************************************************
 * $Id: OnuAuthDaoImpl.java,v1.0 2013年10月31日 下午5:15:32 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onuauth.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.olt.domain.OltAttribute;
import com.topvision.ems.epon.olt.domain.OltPonAttribute;
import com.topvision.ems.epon.onu.domain.OltTopOnuProductTable;
import com.topvision.ems.epon.onuauth.dao.OnuAuthDao;
import com.topvision.ems.epon.onuauth.domain.OltAuthentication;
import com.topvision.ems.epon.onuauth.domain.OltOnuBlockAuthen;
import com.topvision.ems.epon.onuauth.domain.OltOnuBlockExtAuthen;
import com.topvision.ems.epon.onuauth.domain.OltPonOnuAuthModeTable;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2013年10月31日-下午5:15:32
 * 
 */
@Repository("onuAuthDao")
public class OnuAuthDaoImpl extends MyBatisDaoSupport<Object> implements OnuAuthDao {
    @Override
    public void updateOnuAuthPolicy(Long entityId, Integer policy) {
        OltAttribute oltAttribute = new OltAttribute();
        oltAttribute.setEntityId(entityId);
        oltAttribute.setOnuAuthenticationPolicy(policy);
        getSqlSession().update(getNameSpace() + "updateOnuAuthPolicy", oltAttribute);
    }

    @Override
    public List<Long> getOnuAuthIdList(Long ponId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAuthIdList", ponId);
    }

    @Override
    public List<Long> getBlockOnuAuthIdList(Long ponId) {
        return getSqlSession().selectList(getNameSpace() + "getBlockOnuAuthIdList", ponId);
    }

    @Override
    public List<OltAuthentication> getOnuAuthenPreConfigList(OltAuthentication oltAuthen) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAuthenPreConfigList", oltAuthen);
    }

    @Override
    public List<OltAuthentication> getOnuAuthenListBySlot(OltAuthentication oltAuthen) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAuthenListBySlot", oltAuthen);
    }

    @Override
    public List<OltAuthentication> getOnuAuthenListByEntity(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAuthenListByEntity", entityId);
    }

    @Override
    public void updateOnuAuthenPreConfig(OltAuthentication oltAuthen) {
        getSqlSession().update(getNameSpace() + "updateOnuAuthenPreConfig", oltAuthen);
    }

    @Override
    public void insertOnuAuthenPreConfig(OltAuthentication oltAuthen) {
        getSqlSession().insert(getNameSpace() + "insertOltAuthentication", oltAuthen);
    }

    @Override
    public void deleteAuthenPreConfig(Long ponId, Long onuIndex) {
        OltAuthentication oltAuthen = new OltAuthentication();
        oltAuthen.setPonId(ponId);
        oltAuthen.setOnuIndex(onuIndex);
        getSqlSession().delete(getNameSpace() + "deleteAuthentication", oltAuthen);
    }

    @Override
    public void deleteAuthenPreConfigByPonId(Long ponId) {
        getSqlSession().delete(getNameSpace() + "deleteAuthenticationByPonId", ponId);
    }

    @Override
    public List<OltOnuBlockAuthen> getOnuAuthenBlockList(Long ponId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAuthenBlockList", ponId);
    }

    @Override
    public List<OltOnuBlockAuthen> getOnuAuthenBlockListBySlot(Long slotId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAuthenBlockListBySlot", slotId);
    }

    @Override
    public List<OltOnuBlockAuthen> getOnuAuthenBlockListByOlt(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAuthenBlockListByOlt", entityId);
    }

    @Override
    public void refreshOnuAuthenBlockList(Long entityId, final List<OltOnuBlockAuthen> list) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOnuBlockAuth", entityId);
            for (OltOnuBlockAuthen blockAuthen : list) {
                // 通过构造一个pon属性对象，获得ONU对应的ponId
                OltPonAttribute oltPonAttribute = new OltPonAttribute();
                oltPonAttribute.setEntityId(blockAuthen.getEntityId());
                oltPonAttribute.setPonIndex(EponIndex.getPonIndex(EponIndex.getOnuIndexByMibIndex(blockAuthen
                        .getOnuMibIndex())));
                blockAuthen.setPonId((Long) sqlSession.selectOne("com.topvision.ems.epon.olt.domain.OltPon.getPonId",
                        oltPonAttribute));
                sqlSession.insert(getNameSpace() + "insertOnuBlockAuth", blockAuthen);
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
    public void deleteOnuAuthBlock(Long ponId, Long onuIndex) {
        OltOnuBlockAuthen blockAuthen = new OltOnuBlockAuthen();
        blockAuthen.setPonId(ponId);
        blockAuthen.setOnuIndex(onuIndex);
        getSqlSession().delete(getNameSpace() + "deleteOnuBlockAuth", blockAuthen);
    }

    @Override
    public void deleteOnuAuthBlockByMac(Long ponId, String mac) {
        OltOnuBlockAuthen blockAuthen = new OltOnuBlockAuthen();
        blockAuthen.setPonId(ponId);
        blockAuthen.setMacAddress(mac);
        getSqlSession().delete(getNameSpace() + "deleteOnuBlockAuthByMac", blockAuthen);
    }

    @Override
    public List<OltAuthentication> getOnuAuthMacList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAuthMacList", entityId);
    }

    @Override
    public List<OltAuthentication> getOnuAuthSnList(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAuthSnList", entityId);
    }

    @Override
    public void refreshOnuAuthenExtBlockList(final List<OltOnuBlockExtAuthen> onuBlockExtAuthenList) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltOnuBlockExtAuthen blockExtAuthen : onuBlockExtAuthenList) {
                sqlSession.update(getNameSpace() + "updateOnuAuthenExtBlock", blockExtAuthen);
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
     * @see com.topvision.ems.epon.dao.OltDao#refreshOnuAuthMode(java.lang.Long, java.util.List)
     */
    @Override
    public void refreshOnuAuthMode(Long entityId, final List<OltPonOnuAuthModeTable> oltPonOnuAuthModeTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteOltPonOnuAuthModeTable", entityId);
            for (OltPonOnuAuthModeTable oltPonOnuAuthModeTable : oltPonOnuAuthModeTables) {
                OltPonAttribute oltPonAttribute = new OltPonAttribute();
                oltPonAttribute.setEntityId(oltPonOnuAuthModeTable.getEntityId());
                oltPonAttribute.setPonIndex(oltPonOnuAuthModeTable.getPonIndex());
                Long ponId = (Long) sqlSession.selectOne("com.topvision.ems.epon.olt.domain.OltPon.getPonId",
                        oltPonAttribute);
                if (ponId != null) {
                    oltPonOnuAuthModeTable.setPonId(ponId);
                    sqlSession.insert(getNameSpace() + "insertOltPonOnuAuthModeTable", oltPonOnuAuthModeTable);
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
     * @see com.topvision.ems.epon.dao.OltDao#updateOnuAuthMode(com.topvision.ems
     * .epon.facade.domain. OltPonOnuAuthModeTable)
     */
    @Override
    public void updateOnuAuthMode(OltPonOnuAuthModeTable oltPonOnuAuthModeTable) {
        getSqlSession().update(getNameSpace() + "updateOnuAuthMode", oltPonOnuAuthModeTable);
    }

    @Override
    public List<OltPonOnuAuthModeTable> getOnuAuthMode(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAuthMode", entityId);
    }

    @Override
    public Integer getPonOnuAuthMode(Long entityId, Long ponIndex) {
        HashMap<String, Long> param = new HashMap<String, Long>();
        param.put("entityId", entityId);
        param.put("ponIndex", ponIndex);
        return getSqlSession().selectOne(getNameSpace() + "getPonOnuAuthMode", param);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.OltDao#refreshOnuAuthEnable(java.lang.Long, java.util.List)
     */
    @Override
    public void refreshOnuAuthEnable(Long entityId, final List<OltPonOnuAuthModeTable> oltPonOnuAuthModeTables) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteOltPonOnuAuthModeTable", entityId);
            for (OltPonOnuAuthModeTable oltPonOnuAuthModeTable : oltPonOnuAuthModeTables) {
                OltPonAttribute oltPonAttribute = new OltPonAttribute();
                oltPonAttribute.setEntityId(oltPonOnuAuthModeTable.getEntityId());
                oltPonAttribute.setPonIndex(oltPonOnuAuthModeTable.getPonIndex());
                Long ponId = (Long) sqlSession.selectOne("com.topvision.ems.epon.olt.domain.OltPon.getPonId",
                        oltPonAttribute);
                if (ponId != null) {
                    oltPonOnuAuthModeTable.setPonId(ponId);
                    sqlSession.insert(getNameSpace() + "insertOltPonOnuAuthModeTable", oltPonOnuAuthModeTable);
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
    public void updateOnuAuthEnable(OltPonOnuAuthModeTable oltPonOnuAuthModeTable) {
        OltPonAttribute oltPonAttribute = new OltPonAttribute();
        oltPonAttribute.setEntityId(oltPonOnuAuthModeTable.getEntityId());
        oltPonAttribute.setPonIndex(oltPonOnuAuthModeTable.getPonIndex());
        Long ponId = (Long) getSqlSession().selectOne("com.topvision.ems.epon.olt.domain.OltPon.getPonId",
                oltPonAttribute);
        if (ponId != null) {
            oltPonOnuAuthModeTable.setPonId(ponId);
            getSqlSession().update(getNameSpace() + "updateOnuAuthMode", oltPonOnuAuthModeTable);
        }
    }

    @Override
    public List<OltPonOnuAuthModeTable> getOnuAuthEnable(Long entityId) {
        return getSqlSession().selectList(getNameSpace() + "getOnuAuthMode", entityId);
    }

    @Override
    public void updateOltAuthentication(OltAuthentication oltAuthentication) {
        getSqlSession().update(getNameSpace() + "updateOltAuthentication", oltAuthentication);
    }

    @Override
    public void updateOnuPreType(Long entityId, Long onuIndex, String type) {
        OltTopOnuProductTable onuPre = new OltTopOnuProductTable();
        onuPre.setEntityId(entityId);
        onuPre.setOnuIndex(onuIndex);
        onuPre.setTopOnuProductType(type);
        onuPre.setTopOnuProductTypeNum(Integer.parseInt(type));
        getSqlSession().update("com.topvision.ems.epon.onu.domain.Onu.updateOnuPreType", onuPre);
        getSqlSession().update(getNameSpace() + "updateOnuAuthPreType", onuPre);
    }

    @Override
    public void updateAllOnuPreType(final List<OltTopOnuProductTable> onuPre) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltTopOnuProductTable p : onuPre) {
                sqlSession.update("com.topvision.ems.epon.onu.domain.Onu.updateOnuPreType", p);
                sqlSession.update(getNameSpace() + "updateOnuAuthPreType", p);
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
    public void batchInsertOltOnuBlockAuth(final List<OltOnuBlockAuthen> list) {
        SqlSession sqlSession = getBatchSession();
        try {
            for (OltOnuBlockAuthen blockAuthen : list) {
                // 通过构造一个pon属性对象，获得ONU对应的ponId
                OltPonAttribute oltPonAttribute = new OltPonAttribute();
                oltPonAttribute.setEntityId(blockAuthen.getEntityId());
                oltPonAttribute.setPonIndex(EponIndex.getPonIndex(EponIndex.getOnuIndexByMibIndex(blockAuthen
                        .getOnuMibIndex())));
                blockAuthen.setPonId((Long) sqlSession.selectOne("com.topvision.ems.epon.olt.domain.OltPon.getPonId",
                        oltPonAttribute));
                sqlSession.delete(getNameSpace() + "deleteOnuBlockAuth", blockAuthen);
                sqlSession.insert(getNameSpace() + "insertOnuBlockAuth", blockAuthen);
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
    public void batchInsertOltOnuAuthInfo(final List<OltAuthentication> list) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllOnuAuth", list.get(0).getEntityId());
            for (OltAuthentication authenInfo : list) {
                // 通过构造一个pon属性对象，获得ONU对应的ponId
                OltPonAttribute oltPonAttribute = new OltPonAttribute();
                oltPonAttribute.setEntityId(authenInfo.getEntityId());
                oltPonAttribute.setPonIndex(EponIndex.getPonIndex(authenInfo.getOnuIndex()));
                authenInfo.setPonId((Long) sqlSession.selectOne("com.topvision.ems.epon.olt.domain.OltPon.getPonId",
                        oltPonAttribute));
                sqlSession.insert(getNameSpace() + "insertOltAuthentication", authenInfo);
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
    public void deleteAllOnuAuth(Long entityId) {
        getSqlSession().delete(getNameSpace() + "deleteAllOnuAuth", entityId);
    }

    @Override
    public void insertOrUpdateOltAuthentication(final List<OltAuthentication> oltAuthentications, Long entityId) {
        SqlSession sqlSession = getBatchSession();
        try {
            sqlSession.delete(getNameSpace() + "deleteAllAuthentication", entityId);
            for (OltAuthentication oltAuthentication : oltAuthentications) {
                // 通过构造一个pon属性对象，获得ONU对应的ponId
                OltPonAttribute oltPonAttribute = new OltPonAttribute();
                oltPonAttribute.setEntityId(oltAuthentication.getEntityId());
                oltPonAttribute.setPonIndex(EponIndex.getPonIndex(oltAuthentication.getOnuIndex()));
                oltAuthentication.setPonId((Long) sqlSession.selectOne(
                        "com.topvision.ems.epon.olt.domain.OltPon.getPonId", oltPonAttribute));
                sqlSession.insert(getNameSpace() + "insertOltAuthentication", oltAuthentication);
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
    public void deletePonAuthRule(Long entityId, Long ponId) {
        HashMap<String, Long> param = new HashMap<String, Long>();
        param.put("entityId", entityId);
        param.put("ponId", ponId);
        getSqlSession().delete(getNameSpace() + "deletePonAuthRule", param);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.onuauth.domain.OnuAuth";
    }

    @Override
    public List<OltAuthentication> loadRejectedMacList(Long entityId, Long ponId) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("ponId", ponId);
        return getSqlSession().selectList(getNameSpace("selectRejectedMacList"), map);
    }

    @Override
    public OltAuthentication selectOltAuthenticationByIndex(Long entityId, Long onuIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", entityId);
        map.put("onuIndex", onuIndex);
        return getSqlSession().selectOne(getNameSpace("selectOltAuthenticationByIndex"), map);
    }

}
