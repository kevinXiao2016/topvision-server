/***********************************************************************
 * $Id: LicenseService.java,v1.0 2012-7-12 下午3:47:53 $
 * 
 * @author: dengl
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.service;

import java.util.List;

import com.topvision.ems.facade.domain.Entity;
import com.topvision.ems.facade.domain.EntityType;
import com.topvision.framework.service.Service;
import com.topvision.license.common.domain.Module;
import com.topvision.platform.domain.LicenseView;

/**
 * License管理
 * 
 * @author dengl
 * @created @2012-7-12-下午3:47:53
 * 
 */
public interface LicenseService extends Service {
    /**
     * 获取license信息
     * 
     * @return
     */
    LicenseView getLicenseView();

    /**
     * 获取模块的license信息
     * 推荐使用entityTypeService的getXXXType方法获取type，然后使用LicenseModule getLicenseModule(Long type);获取LicenseModule
     * 
     * @param name
     * @return
     */
    @Deprecated
    Module getLicenseModule(String name);

    /**
     * 获取模块的license信息
     * 
     * @param type
     * @return
     */
    Module getLicenseModule(Long type);

    /**
     * 获取机器码
     * 
     * @return
     */
    byte[] getProductKey(LicenseView licenseView);

    /**
     * 导入license
     * 
     * @param lic
     */
    void importLicense(byte[] lic);

    /**
     * 验证license license
     * 
     * @return
     */
    boolean verifyLicense();

    /**
     * @return 获取所有设备类型及其父类型
     */
    List<EntityType> getAllEntityTypes();
    
    /**
     * 在线申请License
     * @param licenseView
     */
    void onlineApply(LicenseView licenseView);

    /**
     * 获取所有未授权的Entity
     * @return
     */
    List<Entity> getUnauthorizedEntities();
    
}
