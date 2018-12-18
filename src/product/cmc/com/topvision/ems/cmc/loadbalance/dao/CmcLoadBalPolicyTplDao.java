/***********************************************************************
 * $Id: CmcLoadBalPolicyTplDao.java,v1.0 2013-4-25 上午10:09:36 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.cmc.loadbalance.dao;

import java.util.List;

import com.topvision.ems.cmc.loadbalance.domain.CmcLoadBalPolicyTpl;

/**
 * 负载均衡生效策略模板的dao
 * @author dengl
 * @created @2013-4-25-上午10:09:36
 *
 */
public interface CmcLoadBalPolicyTplDao {
    /**
     * 获取负载均衡的生效策略模板
     * @return
     */
    List<CmcLoadBalPolicyTpl> selectLoadBalPolicyTpl();

    /**
     * 根据id查询模策略板
     * @param policyTplId
     * @return
     */
    CmcLoadBalPolicyTpl selectLoadBalPolicyTplById(Long policyTplId);

    /**
     * 添加负载均衡的生效策略模板
     * @param balPolicyTpl
     */
    void insertLoadBalPolicyTpl(CmcLoadBalPolicyTpl balPolicyTpl);

    /**
     * 删除负载均衡生效策略模板
     * @param policyTplId
     */
    void deleteLoadBalPolicyTpl(Long policyTplId);

    /**
     * 更新负载均衡生效策略模板
     * @param balPolicyTpl
     */
    void updateLoadBalPolicyTpl(CmcLoadBalPolicyTpl balPolicyTpl);
}
