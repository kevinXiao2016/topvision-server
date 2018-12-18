/***********************************************************************
 * $ com.topvision.ems.cmc.dao.CmRefreshDao,v1.0 2013-9-25 9:09:07 $
 *
 * @author: jay
 *
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.cm.dao;

import java.util.List;

import com.topvision.ems.cmc.domain.CmCmcRelation;
import com.topvision.ems.cmc.facade.domain.CmAttribute;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author jay
 * @created @2013-9-25-9:09:07
 */
public interface CmRefreshDao extends BaseEntityDao<CmCmcRelation> {
    /**
     * 更新一台设备下的某个cc的所有cm列表
     * 
     * @param entityId
     *            设备的entityId
     * @param cmcId
     *            某个cmc的id
     * @param cmAttributes
     *            需要更新的Cm列表 此方法将替换掉如下几个方法，以下方法将作废 public void batchInsertCmAttribute(final
     *            List<CmAttribute> cmAttributeList, final Long entityId) public void
     *            batchInsertOrUpdateCmAttribute(final List<CmAttribute> cmAttributeList, final Long
     *            entityId) void batchInsertOrUpdateCmAttrOnCC(final List<CmAttribute>
     *            cmAttributeList, Long cmcId, Long entityId); public void
     *            batchInsertCmAttributeFor8800A(final List<CmAttribute> cmAttributes, Long cmcId,
     *            final Long entityId) public void batchInsertCmAttribute8800b(final
     *            List<CmAttribute> cmAttributeList, final Long cmcId, Long entityId);
     */
    void batchRefreshCmAttribute(Long entityId, Long cmcId, List<CmAttribute> cmAttributes);

    void refreshCmPortId(Long upPortId, Long downPortId, String mac);
}
