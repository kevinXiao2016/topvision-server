/***********************************************************************
 * $Id: UniRateBatchConfigDao.java,v1.0 2014-5-15 下午3:20:00 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.dao;

import java.util.List;

import com.topvision.ems.epon.onu.domain.UniRateLimitTemplate;
import com.topvision.framework.dao.BaseEntityDao;

/**
 * @author flack
 * @created @2014-5-15-下午3:20:00
 *
 */
public interface UniRateBatchConfigDao extends BaseEntityDao<Object> {
    /**
     * 查询UNI口限速模板列表
     * @param entityId
     * @return
     */
    public List<UniRateLimitTemplate> queryTemplateList(Long entityId);

    /**
     * 查询UNI口限速模板
     * @param entityId
     * @param templateId
     * @return
     */
    public UniRateLimitTemplate selectTemplateById(Long entityId, Integer templateId);

    /**
     * 添加UNI口限速模板
     * @param rateTemplate
     */
    public void insertTemplate(UniRateLimitTemplate rateTemplate);

    /**
     * 更新UNI口限速模板
     * @param rateTemplate
     */
    public void updateTemplate(UniRateLimitTemplate rateTemplate);

    /**
     * 删除UNI口限速模板
     * @param templateId
     */
    public void deleteTemplate(Integer templateId);

    /**
     * 删除UNI口限速参数
     * @param rateTemplate
     */
    public void updatePortRateLimit(UniRateLimitTemplate rateTemplate);
}
