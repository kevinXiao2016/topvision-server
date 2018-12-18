package com.topvision.ems.epon.onu.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.dao.OnuAssemblyDao;
import com.topvision.ems.epon.onu.domain.OltOnuCapability;
import com.topvision.ems.epon.onu.domain.OltOnuCatv;
import com.topvision.ems.epon.onu.domain.OltOnuRstp;
import com.topvision.ems.epon.onu.domain.OltOnuVoip;
import com.topvision.ems.epon.onu.domain.OltTopOnuCapability;
import com.topvision.ems.epon.onu.domain.OltUniAttribute;
import com.topvision.ems.epon.onu.domain.OltUniExtAttribute;
import com.topvision.ems.epon.onu.domain.OltUniPortRateLimit;
import com.topvision.ems.epon.onu.domain.OnuInfo;
import com.topvision.ems.epon.onu.domain.OnuQualityInfo;
import com.topvision.ems.epon.onu.domain.UniPort;
import com.topvision.ems.epon.optical.domain.OltPonOptical;
import com.topvision.ems.epon.performance.domain.OnuLinkCollectInfo;
import com.topvision.ems.epon.univlanprofile.domain.UniVlanBindTable;
import com.topvision.ems.fault.domain.Alert;
import com.topvision.ems.fault.domain.AlertType;
import com.topvision.ems.fault.domain.HistoryAlert;
import com.topvision.ems.gpon.onu.domain.GponOnuQualityInfo;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

/**
 * 将原OnuDeviceDao中的方法移动到这里，原OnuServiceDao用来做ONU设备列表dao
 * 
 * @author w1992wishes
 * @created @2017年12月22日-上午9:41:01
 *
 */
@Repository
public class OnuAssemblyDaoImpl extends MyBatisDaoSupport<UniPort> implements OnuAssemblyDao {

    @Override
    protected String getDomainName() {
        return UniPort.class.getName();
    }

    @Override
    public OltPonOptical getOltOnuPonOptical(Long onuId) {
        Long portId = (Long) getSqlSession().selectOne("com.topvision.ems.epon.onu.domain.Onu.getPonIdByOnuId", onuId);
        OltPonOptical oltPonOptical = (OltPonOptical) getSqlSession().selectOne(
                "com.topvision.ems.epon.optical.domain.Optical.getOltPonOptical", portId);
        return oltPonOptical;
    }

    @Override
    public List<UniPort> selectUniList(Long onuId) {
        return getSqlSession().selectList(getNameSpace("selectUniList"), onuId);
    }

    @Override
    public List<OnuInfo> selectOnuList(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        return getSqlSession().selectList(getNameSpace("selectOnuList"), queryMap);
    }

    @Override
    public void updateOnuQuality(OnuQualityInfo onuQualityInfo) {
        getSqlSession().selectList(getNameSpace("updateOnuVersionInfo"), onuQualityInfo);
        int affectRows = getSqlSession().update(getNameSpace("updateOnuQuality"), onuQualityInfo);
        if (affectRows == 0) {
            getSqlSession().insert(getNameSpace("insertOnuQuality"), onuQualityInfo);
        }
    }

    @Override
    public void updateGponOnuQuality(GponOnuQualityInfo gponOnuQualityInfo) {
        getSqlSession().selectList(getNameSpace("updateGponOnuVersionInfo"), gponOnuQualityInfo);
        int affectRows = getSqlSession().update(getNameSpace("updateGponOnuQuality"), gponOnuQualityInfo);
        if (affectRows == 0) {
            getSqlSession().insert(getNameSpace("insertGponOnuQuality"), gponOnuQualityInfo);
        }
    }

    @Override
    public List<AlertType> selectOnuAlertTypes() {
        return getSqlSession().selectList(getNameSpace("selectOnuAlertTypes"));
    }

    @Override
    public List<Alert> selectOnuAlertList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectOnuAlertList"), map);
    }

    @Override
    public int selectOnuAlertListNum(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("selectOnuAlertListNum"), map);
    }

    @Override
    public List<HistoryAlert> selectOnuHistoryAlertList(Map<String, Object> map) {
        return getSqlSession().selectList(getNameSpace("selectOnuHistoryAlertList"), map);
    }

    @Override
    public int selectOnuHistoryAlertListNum(Map<String, Object> map) {
        return getSqlSession().selectOne(getNameSpace("selectOnuHistoryAlertListNum"), map);
    }

    @Override
    public int selectOnuCount(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace("selectOnuCount"), queryMap);
    }

    @Override
    public void modifyOnuMac(Long onuId, String onuMac) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("onuId", onuId);
        map.put("mac", onuMac);
        getSqlSession().update(getNameSpace("modifyOnuMac"), map);
    }

    @Override
    public void updateOnuTopCapacity(OltTopOnuCapability capability) {
        getSqlSession().update("com.topvision.ems.epon.onu.domain.Onu.updateTopOnuCapatility", capability);
    }

    @Override
    public void updateOnuCapacity(OltOnuCapability capability) {
        getSqlSession().update("com.topvision.ems.epon.onu.domain.Onu.updateOnuCapatility", capability);
    }

    @Override
    public void updateOltOnuRstp(OltOnuRstp oltOnuRstp) {
        getSqlSession().update("com.topvision.ems.epon.onu.domain.Onu.updateOnuRstp", oltOnuRstp);
    }

    @Override
    public void updateOltOnuCatv(OltOnuCatv oltOnuCatv) {
        getSqlSession().update("com.topvision.ems.epon.onu.domain.Onu.updateOnuCatv", oltOnuCatv);
    }

    @Override
    public void updateOltOnuVoip(OltOnuVoip oltOnuVoip) {
        // getSqlSession().update("com.topvision.ems.epon.onu.domain.Onu.updateOnuVoip",
        // oltOnuVoip);
    }

    @Override
    public void batchUpdateUniAttribute(Long entityId, List<OltUniAttribute> oltUniAttributeList) {
        SqlSession session = getBatchSession();
        try {
            for (OltUniAttribute oltUniAttribute : oltUniAttributeList) {
                oltUniAttribute.setEntityId(entityId);
                session.update("com.topvision.ems.epon.onu.domain.Uni.updateOnuUniAttribute", oltUniAttribute);
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
    public void batchUpdateUniPortRateLimit(Long onuId, List<OltUniPortRateLimit> oltUniPortRateLimitList) {
    }

    @Override
    public void batchUpdateUniExtAttribute(Long entityId, Long onuId, List<OltUniExtAttribute> oltUniExtAttributeList) {
        SqlSession session = getBatchSession();
        try {
            for (OltUniExtAttribute oltUniExtAttribute : oltUniExtAttributeList) {
                Long uniId = getUniIdByIndex(entityId, oltUniExtAttribute.getUniIndex());
                oltUniExtAttribute.setUniId(uniId);
                oltUniExtAttribute.setEntityId(entityId);
                session.update("com.topvision.ems.epon.onu.domain.Uni.updateOltUniExtAttribute", oltUniExtAttribute);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

    private Long getUniIdByIndex(Long entityId, Long uniIndex) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("entityId", entityId);
        paramMap.put("uniIndex", uniIndex);
        return getSqlSession().selectOne("com.topvision.ems.epon.onu.domain.Uni.getUniIdByIndex", paramMap);
    }

    @Override
    public void batchUpdateUniVlanTable(Long parentId, List<UniVlanBindTable> vlanList) {
        SqlSession session = getBatchSession();
        try {
            for (UniVlanBindTable uniVlanBindTable : vlanList) {
                uniVlanBindTable.setEntityId(parentId);
                getSqlSession().update("com.topvision.ems.epon.univlanprofile.domain.UniVlanProfile.updateUniBindInfo",
                        uniVlanBindTable);
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
    public List<OnuLinkCollectInfo> queryOnuRelaInfoList(List<String> onuIdList) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("onuIdList", onuIdList);
        return this.getSqlSession().selectList(getNameSpace("queryOnuRelaInfoList"), paramsMap);
    }

    @Override
    public void saveOnuServerLevel(Long onuId, Integer onuLevel) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("onuId", onuId);
        paramsMap.put("onuLevel", onuLevel);
        this.getSqlSession().update(getNameSpace("saveOnuServerLevel"), paramsMap);
    }

    @Override
    public Integer getOnuServerLevel(Long onuId) {
        return this.getSqlSession().selectOne(getNameSpace("getOnuServerLevel"), onuId);
    }

    @Override
    public void insertOrUpdateOnuTagRelation(Long onuId, Integer tagId) {
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("onuId", onuId);
        paramsMap.put("tagId", tagId);
        getSqlSession().update("com.topvision.ems.epon.onu.domain.Onu.insertOrUpdateOnuTagRelation", paramsMap);
    }

}
