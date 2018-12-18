/***********************************************************************
 * $Id: EponExpressionDaoImpl.java,v1.0 2013年12月4日 下午2:16:46 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.batchdeploy.dao.mybatis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.topvision.ems.epon.batchdeploy.dao.EponExpressionDao;
import com.topvision.ems.epon.batchdeploy.domain.Target;
import com.topvision.ems.epon.olt.dao.OltSlotDao;
import com.topvision.framework.dao.mybatis.MyBatisDaoSupport;
import com.topvision.framework.utils.EponIndex;

/**
 * @author Bravin
 * @created @2013年12月4日-下午2:16:46
 *
 */
@Repository("eponExpressionDao")
public class EponExpressionDaoImpl extends MyBatisDaoSupport<Target> implements EponExpressionDao {

    @Autowired
    private OltSlotDao oltSlotDao;

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.batchdeploy.dao.EponExpressionDao#selectSlotTarget(java.lang.Long, java.util.List)
     */
    @Override
    public List<Target> selectSlotTarget(Long entityId, List<Integer> slots) {
        Map<String, Object> map = new HashMap<>();
        if (slots.size() == 1 && Integer.MAX_VALUE == slots.get(0)) {
            ;
        } else {
            for (int i = 0; i < slots.size(); i++) {
                Integer slotNo = slots.get(i);
                if (slotNo.equals(0)) {
                    Long slotId = oltSlotDao.getSlotIdByIndex(entityId, 0L);
                    Long slotPhyNo = oltSlotDao.getSlotNoById(entityId, slotId);
                    slots.set(i, slotPhyNo.intValue());
                }
            }
            map.put("slotList", slots.toString().replaceAll("\\[|\\]", ""));
        }
        map.put("entityId", entityId);
        return getSqlSession().selectList(getNameSpace("selectSlotTarget"), map);
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.batchdeploy.dao.EponExpressionDao#selectPortTarget(java.util.List, java.util.List, boolean)
     */
    @Override
    public List<Target> selectPortTarget(List<Long> ids, final List<Integer> ports, boolean includeUplink) {
        Map<String, Object> map = new HashMap<>();
        map.put("slotList", ids.toString().replaceAll("\\[|\\]", ""));
        map.put("includeUplink", includeUplink);
        final boolean acceptAll;
        if (ports.size() == 1 && Integer.MAX_VALUE == ports.get(0)) {
            acceptAll = true;
        } else {
            acceptAll = false;
        }

        final List<Target> list = new ArrayList<>();
        getSqlSession().select(getNameSpace("selectPortTarget"), map, new ResultHandler() {

            @Override
            public void handleResult(ResultContext context) {
                Target target = (Target) context.getResultObject();
                int ponNo = EponIndex.getPonNo(target.getTargetIndex()).intValue();
                if (acceptAll || ports.contains(ponNo)) {
                    list.add(target);
                }
            }
        });
        return list;
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.batchdeploy.dao.EponExpressionDao#selectLlidTarget(java.util.List, java.util.List)
     */
    @Override
    public List<Target> selectLlidTarget(List<Long> ids, final List<Integer> llids) {
        Map<String, Object> map = new HashMap<>();
        map.put("ponList", ids.toString().replaceAll("\\[|\\]", ""));
        final boolean acceptAll;
        if (llids.size() == 1 && Integer.MAX_VALUE == llids.get(0)) {
            acceptAll = true;
        } else {
            acceptAll = false;
        }
        final List<Target> list = new ArrayList<>();
        getSqlSession().select(getNameSpace("selectLlidTarget"), map, new ResultHandler() {

            @Override
            public void handleResult(ResultContext context) {
                Target target = (Target) context.getResultObject();
                int llidNo = EponIndex.getOnuNo(target.getTargetIndex()).intValue();
                if (acceptAll || llids.contains(llidNo)) {
                    list.add(target);
                }
            }
        });
        return list;
    }

    /*
     * (non-Javadoc)
     * @see com.topvision.ems.epon.batchdeploy.dao.EponExpressionDao#selectUniTarget(java.util.List, java.util.List)
     */
    @Override
    public List<Target> selectUniTarget(List<Long> ids, final List<Integer> unis) {
        Map<String, Object> map = new HashMap<>();
        map.put("llidList", ids.toString().replaceAll("\\[|\\]", ""));
        final boolean acceptAll;
        if (unis.size() == 1 && Integer.MAX_VALUE == unis.get(0)) {
            acceptAll = true;
        } else {
            acceptAll = false;
        }
        final List<Target> list = new ArrayList<>();
        getSqlSession().select(getNameSpace("selectUniTarget"), map, new ResultHandler() {

            @Override
            public void handleResult(ResultContext context) {
                Target target = (Target) context.getResultObject();
                int uniNo = EponIndex.getUniNo(target.getTargetIndex()).intValue();
                if (acceptAll || unis.contains(uniNo)) {
                    list.add(target);
                }
            }
        });
        return list;
    }

    /* (non-Javadoc)
     * @see com.topvision.framework.dao.mybatis.MyBatisDaoSupport#getDomainName()
     */
    @Override
    protected String getDomainName() {
        return "com.topvision.ems.epon.batchdeploy.domain.OltBatchDeploy";
    }

}
