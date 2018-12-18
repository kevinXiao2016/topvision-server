package com.topvision.ems.cmc.cm.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.cmc.cm.dao.CcmtsCmListDao;
import com.topvision.ems.cmc.cm.domain.CmSignal;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.ems.facade.domain.Entity;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * 
 * @author YangYi
 * @created @2013-10-30-下午8:28:55 从CmDaoImpl拆分,CM级联列表
 * 
 */
@Repository("ccmtsCmListDao")
public class CcmtsCmListDaoImpl extends MyBatisDaoSupport<Entity> implements CcmtsCmListDao {

    @Override
    public List<CmAttribute> getCmById(Long cmId) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("cmId", cmId);
        return getSqlSession().selectList(getNameSpace("getCmAttributeById"), queryMap);
    }

    @Override
    public void updateCmStatus(Long cmcId, String mac, Integer status) {
        //MAC地址需要转换成标准的存储格式进行查询
        mac = MacUtils.convertToMaohaoFormat(mac);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("StatusMacAddress", mac);
        map.put("StatusValue", status);
        getSqlSession().update(getNameSpace("updateCmStatus"), map);
    }

    @Override
    public CmAttribute selectCmAttribute(Long cmcId, String mac) {
        //MAC地址需要转换成标准的存储格式进行查询
        mac = MacUtils.convertToMaohaoFormat(mac);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cmcId", cmcId);
        map.put("StatusMacAddress", mac);
        return getSqlSession().selectOne(getNameSpace("getCmAttributeByMac"), map);
    }

    @Override
    public List<CmAttribute> getCmByCpeIp(String cpeIp) {
        return getSqlSession().selectList(getNameSpace("getCmByCpeIp"), cpeIp);
    }

    @Override
    protected String getDomainName() {
        return "com.topvision.ems.cmc.cm.domain.CcmtsCmList";
    }

    @Override
    public void insertOrUpdateCmSignal(CmAttribute cmAttribute) {
        CmSignal cmSignal = getSqlSession().selectOne(getNameSpace("getCmSignalByCmId"), cmAttribute);
        if (cmSignal != null) {
            getSqlSession().update(getNameSpace("updateCmSignal"), cmAttribute);
        } else {
            getSqlSession().insert(getNameSpace("insertCmSignal"), cmAttribute);
        }
    }

    @Override
    public List<CmAttribute> queryCmListByCmcId(Long cmcId) {
        return this.getSqlSession().selectList(getNameSpace("queryCmListByCmcId"), cmcId);
    }

}
