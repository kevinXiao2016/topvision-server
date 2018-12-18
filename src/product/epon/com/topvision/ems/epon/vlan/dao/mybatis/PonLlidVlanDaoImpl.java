/***********************************************************************
 * $Id: SniVlanDaoImpl.java,v1.0 2013-10-25 上午11:45:47 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.dao.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.onu.domain.OltOnuAttribute;
import com.topvision.ems.epon.vlan.dao.PonLlidVlanDao;
import com.topvision.ems.epon.vlan.domain.VlanLlidAggregationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidOnuQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidQinQRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTranslationRule;
import com.topvision.ems.epon.vlan.domain.VlanLlidTrunkRule;
import com.topvision.framework.common.MacUtils;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author flack
 * @created @2013-10-25-上午11:45:47
 *
 */
@Repository("ponLlidVlanDao")
public class PonLlidVlanDaoImpl extends MyBatisDaoSupport<Object> implements PonLlidVlanDao {

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#getOnuMacAddress(java.lang.Long)
     */
    @Override
    public List<Long> getOnuMacAddress(Long ponId) {
        return getSqlSession().selectList(getNameSpace("getOnuMacAddress"), ponId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.vlan.dao.PonLlidVlanDao#getOnuMacAndName(java.lang.Long)
     */
    @Override
    public List<OltOnuAttribute> getOnuMacAndName(Long ponId) {
        return this.getSqlSession().selectList(getNameSpace("getOnuMacAndName"), ponId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#getLlidTransList(java.lang.Long, java.lang.Long)
     */
    @Override
    public List<VlanLlidTranslationRule> getLlidTransList(Long ponId, String mac) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ponId", ponId);
        //add by fanzidong,需要在入库前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(mac);
        paramMap.put("topLlidTransDevMac", formattedMac);
        return getSqlSession().selectList(getNameSpace("getLlidTransList"), paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.PonVlanDao#addLlidTransRule(com.topvision.ems.epon.facade.domain
     * .VlanLlidTranslationRule)
     */
    @Override
    public void addLlidTransRule(VlanLlidTranslationRule vlanLlidTranslationRule) {
        //add by fanzidong,需要在入库前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(vlanLlidTranslationRule.getOnuMacString());
        vlanLlidTranslationRule.setOnuMacString(formattedMac);
        getSqlSession().insert(getNameSpace("insertOltTopVlanTrans"), vlanLlidTranslationRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#deleteLlidTransRule(java.lang.Long,
     * java.lang.Integer, java.lang.Long)
     */
    @Override
    public void deleteLlidTransRule(Long ponId, Integer vlanId, String mac) {
        VlanLlidTranslationRule vlanLlidTranslationRule = new VlanLlidTranslationRule();
        vlanLlidTranslationRule.setPortId(ponId);
        vlanLlidTranslationRule.setTopLlidTransVidIdx(vlanId);
        //add by fanzidong,需要在入库前格式化MAC地址
        //modify by lzt mac 是冒号分割的字符串不需要进行转换
        //String formattedMac = MacUtils.convertToMaohaoFormat(vlanLlidTranslationRule.getOnuMacString());
        vlanLlidTranslationRule.setOnuMacString(mac);
        getSqlSession().delete(getNameSpace("deleteOltTopVlanTrans"), vlanLlidTranslationRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#getLlidAggrList(java.lang.Long, java.lang.Long)
     */
    @Override
    public List<VlanLlidAggregationRule> getLlidAggrList(Long ponId, String mac) {
        VlanLlidAggregationRule vlanLlidAggregationRule = new VlanLlidAggregationRule();
        vlanLlidAggregationRule.setPortId(ponId);
        vlanLlidAggregationRule.setOnuMacString(MacUtils.convertToMaohaoFormat(mac));
        return getSqlSession().selectList(getNameSpace("getLlidAggrList"), vlanLlidAggregationRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.PonVlanDao#addLlidSvlanAggrRule(com.topvision.ems.epon.facade.
     * domain.VlanLlidAggregationRule)
     */
    @Override
    public void addLlidSvlanAggrRule(VlanLlidAggregationRule vlanLlidAggregationRule) {
        //add by fanzidong,需要在入库前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(vlanLlidAggregationRule.getOnuMacString());
        vlanLlidAggregationRule.setOnuMacString(formattedMac);
        getSqlSession().insert(getNameSpace("insertOltTopVlanAgg"), vlanLlidAggregationRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#deleteLlidSvlanAggrRule(java.lang.Long,
     * java.lang.Long, java.lang.Integer)
     */
    @Override
    public void deleteLlidSvlanAggrRule(Long ponId, String mac, Integer vlanId) {
        VlanLlidAggregationRule vlanLlidAggregationRule = new VlanLlidAggregationRule();
        vlanLlidAggregationRule.setPortId(ponId);
        //add by fanzidong,需要在入库前格式化MAC地址
        //String formattedMac = MacUtils.convertToMaohaoFormat(mac);
        vlanLlidAggregationRule.setOnuMacString(mac);
        vlanLlidAggregationRule.setLlidVlanAfterAggVid(vlanId);
        getSqlSession().insert(getNameSpace("deleteOltTopVlanAgg"), vlanLlidAggregationRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.PonVlanDao#modifyLlidCvlanAggrRule(com.topvision.ems.epon.facade
     * .domain.VlanLlidAggregationRule)
     */
    @Override
    public void modifyLlidCvlanAggrRule(VlanLlidAggregationRule vlanLlidAggregationRule) {
        //add by fanzidong,需要在入库前格式化MAC地址
        //String formattedMac = MacUtils.convertToMaohaoFormat(vlanLlidAggregationRule.getOnuMacString());
        // vlanLlidAggregationRule.setOnuMacString(formattedMac);
        getSqlSession().insert(getNameSpace("modifyLlidCvlanAggrRule"), vlanLlidAggregationRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#getLlidTrunkList(java.lang.Long, java.lang.Long)
     */
    @Override
    public VlanLlidTrunkRule getLlidTrunkList(Long ponId, String mac) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ponId", ponId);
        mac = MacUtils.convertToMaohaoFormat(mac);
        paramMap.put("llidVlanTrunkMacIdx", mac);
        return getSqlSession().selectOne(getNameSpace("getLlidTrunkList"), paramMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.PonVlanDao#addLlidTrunkRule(com.topvision.ems.epon.facade.domain
     * .VlanLlidTrunkRule)
     */
    @Override
    public void addLlidTrunkRule(VlanLlidTrunkRule vlanLlidTrunkRule) {
        //add by fanzidong,需要在入库前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(vlanLlidTrunkRule.getOnuMacString());
        vlanLlidTrunkRule.setOnuMacString(formattedMac);
        getSqlSession().insert(getNameSpace("insertOltTopVlanTrunk"), vlanLlidTrunkRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#deleteLlidTrunkRule(java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void deleteLlidTrunkRule(Long ponId, String mac) {
        VlanLlidTrunkRule vlanLlidTrunkRule = new VlanLlidTrunkRule();
        vlanLlidTrunkRule.setPortId(ponId);
        //add by fanzidong,需要在查询前格式化MAC地址
        //String formattedMac = MacUtils.convertToMaohaoFormat(vlanLlidTrunkRule.getOnuMacString());
        vlanLlidTrunkRule.setOnuMacString(mac);
        getSqlSession().delete(getNameSpace("deleteOltTopVlanTrunk"), vlanLlidTrunkRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.PonVlanDao#modifyLlidTrunkRule(com.topvision.ems.epon.facade.domain
     * .VlanLlidTrunkRule)
     */
    @Override
    public void modifyLlidTrunkRule(VlanLlidTrunkRule vlanLlidTrunkRule) {
        //add by fanzidong,需要在查询前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(vlanLlidTrunkRule.getOnuMacString());
        vlanLlidTrunkRule.setOnuMacString(formattedMac);
        getSqlSession().update(getNameSpace("modifyLlidTrunkRule"), vlanLlidTrunkRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#getLlidQinQList(java.lang.Long, java.lang.Long)
     */
    @Override
    public List<VlanLlidQinQRule> getLlidQinQList(Long ponId, String mac) {
        VlanLlidQinQRule vlanLlidQinQRule = new VlanLlidQinQRule();
        vlanLlidQinQRule.setPortId(ponId);
        //add by fanzidong,需要在入库前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(mac);
        vlanLlidQinQRule.setOnuMacString(formattedMac);
        return getSqlSession().selectList(getNameSpace("getLlidQinQList"), vlanLlidQinQRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.epon.dao.PonVlanDao#addLlidQinQRule(com.topvision.ems.epon.facade.domain
     * .VlanLlidQinQRule)
     */
    @Override
    public void addLlidQinQRule(VlanLlidQinQRule vlanLlidQinQRule) {
        //add by fanzidong,需要在入库前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(vlanLlidQinQRule.getOnuMacString());
        vlanLlidQinQRule.setOnuMacString(formattedMac);
        getSqlSession().insert(getNameSpace("insertOltTopVlanQinQ"), vlanLlidQinQRule);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.topvision.ems.epon.dao.PonVlanDao#deleteLlidQinQRule(java.lang.Long, java.lang.Long,
     * java.lang.Integer, java.lang.Integer)
     */
    @Override
    public void deleteLlidQinQRule(Long ponId, String mac, Integer startVlanId, Integer endVlanId) {
        VlanLlidQinQRule vlanLlidQinQRule = new VlanLlidQinQRule();
        vlanLlidQinQRule.setPortId(ponId);
        //add by fanzidong,需要在入库查询前格式化MAC地址
        String formattedMac = MacUtils.convertToMaohaoFormat(mac);
        vlanLlidQinQRule.setOnuMacString(formattedMac);
        vlanLlidQinQRule.setTopLqVlanStartCVid(startVlanId);
        vlanLlidQinQRule.setTopLqVlanEndCVid(endVlanId);
        getSqlSession().delete(getNameSpace("deleteOltTopVlanQinQ"), vlanLlidQinQRule);
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.vlan.domain.PonVlan";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.vlan.dao.PonLlidVlanDao#addLlidOnuQinQRule(com.topvision.ems.epon.vlan.domain.VlanLlidOnuQinQRule)
     */
    @Override
    public void addLlidOnuQinQRule(VlanLlidOnuQinQRule vlanLlidOnuQinQRule) {
        getSqlSession().insert(getNameSpace("insertOltTopVlanOnuQinQ"), vlanLlidOnuQinQRule);
    }

}
