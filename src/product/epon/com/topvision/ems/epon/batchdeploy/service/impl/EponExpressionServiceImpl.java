/***********************************************************************
 * $Id: EponExpressionServiceImpl.java,v1.0 2013年12月2日 下午2:33:59 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.batchdeploy.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.epon.batchdeploy.dao.EponExpressionDao;
import com.topvision.ems.epon.batchdeploy.domain.Expression;
import com.topvision.ems.epon.batchdeploy.domain.Target;
import com.topvision.ems.epon.batchdeploy.service.EponExpressionService;
import com.topvision.ems.epon.batchdeploy.util.ExpressionUtil;
import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * @created @2013年12月2日-下午2:33:59
 *
 */
@Service("eponExpressionService")
public class EponExpressionServiceImpl extends BaseService implements EponExpressionService {
    @Autowired
    private EponExpressionDao eponExpressionDao;

    /**
     * 依次解析每一个表达式
     * @param entityId
     * @param expression
     * @return
     */
    private List<Target> getTargetFromExpression(Long entityId, Expression expression) {
        String es = expression.getExpression();
        if ("".equals(es) || null == es) {
            return Collections.emptyList();
        }
        ExpressionUtil.parseExpresson(expression);
        final int level = expression.getLevel();
        List<Target> targetList = eponExpressionDao.selectSlotTarget(entityId, expression.getSlots());
        if (level == Expression.LV_SLOT) {
            return targetList;
        }
        //处理端口级别
        List<Long> ids = new ArrayList<>();
        for (Target target : targetList) {
            ids.add(target.getId());
        }
        //如果SLOT级别就没有可查询板卡,则直接返回一个空的list
        if (ids.isEmpty()) {
            return Collections.emptyList();
        } else {
            targetList = eponExpressionDao.selectPortTarget(ids, expression.getPorts(), level < Expression.LV_LLID);
        }
        if (level == Expression.LV_PORT) {
            return targetList;
        }
        //处理LLID级别,只会存在PON口的ID
        ids.clear();
        for (Target target : targetList) {
            ids.add(target.getId());
        }
        //如果PORT级别就没有可查询板卡,则直接返回一个空的list
        if (ids.isEmpty()) {
            return Collections.emptyList();
        } else {
            targetList = eponExpressionDao.selectLlidTarget(ids, expression.getLlids());
        }
        if (level == Expression.LV_LLID) {
            return targetList;
        }
        //处理UNI级别
        ids.clear();
        for (Target target : targetList) {
            ids.add(target.getId());
        }
        //如果LLID级别就没有可查询板卡,则直接返回一个空的list
        if (ids.isEmpty()) {
            return Collections.emptyList();
        } else {
            targetList = eponExpressionDao.selectUniTarget(ids, expression.getUnis());
        }
        if (level == Expression.LV_UNI) {
            return targetList;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.epon.batchdeploy.service.EponExpressionService#getBatchDeployTargets(java.util.Map, java.util.Map)
     */
    @Override
    public Map<Long, List<Target>> getBatchDeployTargets(Map<Long, List<String>> multiTargets,
            Map<Long, List<String>> multiExcludeList) {
        Map<Long, List<Target>> map = new HashMap<Long, List<Target>>();
        for (Long entityId : multiTargets.keySet()) {
            List<Target> targets = new ArrayList<>();
            List<String> l2 = multiTargets.get(entityId);
            if (l2 == null) {
                continue;
            }
            for (String es : l2) {
                Expression expression = new Expression(es);
                List<Target> l3 = getTargetFromExpression(entityId, expression);
                targets.addAll(l3);
            }
            //过滤出排除列表
            if (multiExcludeList != null) {
                List<String> l3 = multiExcludeList.get(entityId);
                if (l3 != null) {
                    for (String es : l3) {
                        Expression expression = new Expression(es);
                        List<Target> excludes = getTargetFromExpression(entityId, expression);
                        ///target必须重写了equals接口
                        targets.removeAll(excludes);

                    }
                }
            }
            map.put(entityId, targets);
        }
        return map;
    }

}
