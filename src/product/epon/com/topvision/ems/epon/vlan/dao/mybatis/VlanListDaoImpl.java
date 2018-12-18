/***********************************************************************
 * $Id: VlanListDaoImpl.java,v1.0 2016年6月8日 上午10:54:57 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.vlan.dao.mybatis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.vlan.dao.VlanListDao;
import com.topvision.ems.epon.vlan.domain.OltPortVlan;
import com.topvision.ems.epon.vlan.domain.OltPortVlanRelation;
import com.topvision.ems.epon.vlan.domain.PonVlanPortLocation;
import com.topvision.ems.epon.vlan.domain.UniPortVlan;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;

/**
 * @author Bravin
 * @created @2016年6月8日-上午10:54:57
 *
 */
@Repository("vlanListDao")
public class VlanListDaoImpl extends MyBatisDaoSupport<Object> implements VlanListDao {

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.vlan.domain.OltSniPortVlan";
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.vlan.dao.VlanListDao#loadSniPortVlanList(java.lang.Long)
     */
    @Override
    public List<OltPortVlan> selectSniPortVlanList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectSniPortVlanList"), entityId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.vlan.dao.VlanListDao#selectSniPortVlanRelationList(java.lang.Long)
     */
    @Override
    public List<OltPortVlanRelation> selectPortVlanRelation(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectPortVlanRelation"), entityId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.vlan.dao.VlanListDao#selectUniPortVlan(java.lang.Long)
     */
    @Override
    public List<UniPortVlan> selectUniPortVlan(Long onuId) {
        return getSqlSession().selectList(getNameSpace("selectUniPortVlan"), onuId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.vlan.dao.VlanListDao#selectPonPortVlanList(java.lang.Long)
     */
    @Override
    public List<OltPortVlan> selectPonPortVlanList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectPonPortVlanList"), entityId);
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.vlan.dao.VlanListDao#selectSlotPonList(java.lang.Long)
     */
    @Override
    public List<PonVlanPortLocation> selectSlotPonList(Long entityId) {
        return getSqlSession().selectList(getNameSpace("selectSlotPonList"), entityId);
    }
}
