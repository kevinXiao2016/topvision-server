/***********************************************************************
 * $Id: UniRateBatchConfigService.java,v1.0 2014-5-15 下午3:36:33 $
 * 
 * @author: flack
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.epon.onu.service;

import java.util.List;

import com.topvision.ems.epon.onu.domain.UniRateLimitTemplate;
import com.topvision.framework.service.Service;

/**
 * @author flack
 * @created @2014-5-15-下午3:36:33
 *
 */
public interface UniRateBatchConfigService extends Service {
    /**
     * 查询UNI口限速模板列表
     * @param entityId
     * @return
     */
    public List<UniRateLimitTemplate> getTemplateList(Long entityId);

    /**
     * 查询UNI口限速模板列表
     * @param entityId
     * @param templateId
     * @return
     */
    public UniRateLimitTemplate getTemplateById(Long entityId, Integer templateId);

    /**
     * 添加UNI口限速模板
     * @param rateTemplate
     */
    public void addTemplate(UniRateLimitTemplate rateTemplate);

    /**
     * 更新UNI口限速模板
     * @param rateTemplate
     */
    public void modifyTemplate(UniRateLimitTemplate rateTemplate);

    /**
     * 删除UNI口限速模板
     * @param templateId
     */
    public void deleteTemplate(Integer templateId);

    /**
     * 批量更新UNI口限速
     * @param applyList
     * @param template
     * @return
     */
    public void updateProtRateLimit(List<UniRateLimitTemplate> applyList, UniRateLimitTemplate template);

}
