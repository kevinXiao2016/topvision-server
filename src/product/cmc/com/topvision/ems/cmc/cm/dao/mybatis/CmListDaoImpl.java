package com.topvision.ems.cmc.cm.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.cm.dao.CmListDao;
import com.topvision.ems.cmc.cm.domain.CcmtsLocation;
import com.topvision.ems.cmc.cm.domain.Cm3Signal;
import com.topvision.ems.cmc.cm.domain.CmLocation;
import com.topvision.ems.cmc.cm.domain.CmSignal;
import com.topvision.ems.cmc.cm.domain.CmTopo;
import com.topvision.ems.cmc.cm.domain.OltCcmtsRela;
import com.topvision.ems.cmc.cm.domain.OltLocation;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.cmc.facade.domain.CmPartialSvcState;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.platform.util.CurrentRequest;

@Repository("cmListDao")
public class CmListDaoImpl extends MyBatisDaoSupport<Entity> implements CmListDao {

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.cm.domain.CmList";
    }

    @Override
    public List<Map<String, String>> loadDeviceListByType(Long type) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        queryMap.put("type", type);
        List<Map<String, String>> devices = getSqlSession().selectList(getNameSpace("loadDeviceListByType"), queryMap);
        return devices;
    }

    @Override
    public List<Map<String, String>> loadDeviceListByTypeId(Long typeId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        queryMap.put("typeId", typeId);
        List<Map<String, String>> devices = getSqlSession().selectList(getNameSpace("loadDeviceListByTypeId"),
                queryMap);
        return devices;
    }

    @Override
    public List<Map<String, String>> loadCcmtsOfPon(Long entityId, Long ponId) {
        // 此方法不需要进行权限限制，因为OLT已经做了权限限制
        Map<String, Long> queryMap = new HashMap<String, Long>();
        queryMap.put("entityId", entityId);
        queryMap.put("ponId", ponId);
        List<Map<String, String>> devices = getSqlSession().selectList(getNameSpace("loadCcmtsOfPon"), queryMap);
        return devices;
    }

    @Override
    public String selectCmcPortIfDescr(Long cmcId, Long channelIndex) {
        Map<String, Long> queryMap = new HashMap<String, Long>();
        queryMap.put("cmcId", cmcId);
        queryMap.put("channelIndex", channelIndex);
        String ifDescr = getSqlSession().selectOne(getNameSpace("selectCmcPortIfDescr"), queryMap);
        return ifDescr;
    }

    @Override
    public List<CmTopo> selectCmTopos(Long cmcId) {
        return getSqlSession().selectList(getNameSpace("getCmTopos"), cmcId);
    }

    @Override
    public Long selectCmcIdByCmId(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("selectCmcIdByCmId"), cmId);
    }

    @Override
    public Long selectOltIdByCmcId(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace("selectOltIdByCmcId"), cmcId);
    }

    @Override
    public Long selectOnuIndex(Long oltEntityId, Long cmcId) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", oltEntityId);
        map.put("cmcId", cmcId);
        return getSqlSession().selectOne(getNameSpace("selectOnuIndex"), map);
    }

    @Override
    public Long selectPonIndex(Long oltEntityId, Long cmcId) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", oltEntityId);
        map.put("cmcId", cmcId);
        return getSqlSession().selectOne(getNameSpace("selectPonIndex"), map);
    }

    @Override
    public OltLocation selectOltLocation(Long oltEntityId) {
        return getSqlSession().selectOne(getNameSpace("selectOltLocation"), oltEntityId);
    }

    @Override
    public Double selectPonOutSpeed(Long oltEntityId, Long ponIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", oltEntityId);
        map.put("ponIndex", ponIndex);
        return getSqlSession().selectOne(getNameSpace("selectPonOutSpeed"), map);
    }

    @Override
    public OltCcmtsRela selectOltCcmtsRelaForOnuPon(Long cmcId, Long onuPonIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("cmcId", cmcId);
        map.put("onuPonIndex", onuPonIndex);
        return getSqlSession().selectOne(getNameSpace("selectOltCcmtsRelaForOnuPon"), map);
    }

    @Override
    public OltCcmtsRela selectOltCcmtsRelaForPon(Long oltEntityId, Long ponIndex) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("entityId", oltEntityId);
        map.put("ponIndex", ponIndex);
        return getSqlSession().selectOne(getNameSpace("selectOltCcmtsRelaForPon"), map);
    }

    @Override
    public Integer selectMaxAlarmLevel(Long oltEntityId) {
        return getSqlSession().selectOne(getNameSpace("selectMaxAlarmLevel"), oltEntityId);
    }

    @Override
    public CcmtsLocation getCcmtsLocation(Long cmcId, Long cmId) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("cmcId", cmcId);
        map.put("cmId", cmId);
        return getSqlSession().selectOne(getNameSpace("getCcmtsLocation"), map);
    }

    @Override
    public Integer getCcmtsMaxAlarmLevel(Long cmcId, String mac) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("mac", mac);
        return getSqlSession().selectOne(getNameSpace("getCcmtsMaxAlarmLevel"), map);
    }

    @Override
    public Long selectCcmtsOutPower(Long cmcId, Long cmId) {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("cmcId", cmcId);
        map.put("cmId", cmId);
        return getSqlSession().selectOne(getNameSpace("selectCcmtsOutPower"), map);
    }

    @Override
    public Long selectCcmtsInPower(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("selectCcmtsInPower"), cmId);
    }

    @Override
    public CmSignal selectCmsignal(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("selectCmsignal"), cmId);
    }

    @Override
    public CmLocation selectCmLocation(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("selectCmLocation"), cmId);
    }

    @Override
    public Long selectCpeNum(Long cmId) {
        return getSqlSession().selectOne(getNameSpace("selectCpeNum"), cmId);
    }

    @Override
    public Integer getCmMaxAlarmLevel(String ip) {
        return getSqlSession().selectOne(getNameSpace("getCmMaxAlarmLevel"), ip);
    }

    @Override
    public List<CmAttribute> selectCmList(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        List<CmAttribute> cmList = getSqlSession().selectList(getNameSpace("selectCmList"), queryMap);
        return cmList;
    }

    @Override
    public Integer selectCmListNum(Map<String, Object> queryMap) {
        queryMap.put("Authority", CurrentRequest.getUserAuthorityViewName());
        Integer cmNum = getSqlSession().selectOne(getNameSpace("selectCmListNum"), queryMap);
        return cmNum;
    }

    @Override
    public CmAttribute selectOneCmByCmId(Long cmId) {
        CmAttribute cmAttribute = getSqlSession().selectOne(getNameSpace("selectOneCmByCmId"), cmId);
        return cmAttribute;
    }

    @Override
    public Integer getCmtsStateById(Long cmcId) {
        return getSqlSession().selectOne(getNameSpace("getCmtsStateById"), cmcId);
    }

    @Override
    public List<CmAttribute> getCmByCmcId(Map<String, Object> queryMap) {
        return getSqlSession().selectList(getNameSpace("getCmByCmcId"), queryMap);
    }

    @Override
    public Integer getCmNumByCmcId(Map<String, Object> queryMap) {
        return getSqlSession().selectOne(getNameSpace("getCmNumByCmcId"), queryMap);
    }

    @Override
    public List<Cm3Signal> getCmSignalByCmIds(List<Long> cmIds) {
        return getSqlSession().selectList(getNameSpace("getCmSignalByCmIds"), cmIds);
    }

    @Override
    public void updateCmPartialSvcState(Long entityId, final List<CmPartialSvcState> data) {
        SqlSession session = getBatchSession();
        try {
            // 先清空
            session.update(getNameSpace("deleteCmPartialByEntityId"), entityId);
            // 再插入
            for (CmPartialSvcState partial : data) {
                session.update(getNameSpace("updateCmPartialState"), partial);
            }
            session.commit();
        } catch (Exception e) {
            logger.error("", e);
            session.rollback();
        } finally {
            session.close();
        }
    }

}