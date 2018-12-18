/***********************************************************************
 * $Id: TL1ExecutorRegistryService.java,v1.0 2017年1月7日 下午4:08:53 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.tl1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.framework.common.ClassAware;
import com.topvision.framework.service.BaseService;

/**
 * @author Bravin
 * @created @2017年1月7日-下午4:08:53
 *
 */
@Service
public class TL1ExecutorRegistryServiceImpl extends BaseService implements TL1ExecutorRegistryService {

    private Map<String, TL1ExecutorService<?>> registry = new HashMap<String, TL1ExecutorService<?>>();
    @Autowired
    private BeanFactory beanFactory;

    @Override
    public void start() {
        Set<Class<?>> clazzes = new ClassAware().scanAnnotation("com.topvision", TL1CommandCode.class);
        for (Class<?> clazz : clazzes) {
            try {
                TL1CommandCode annotation = clazz.getAnnotation(TL1CommandCode.class);
                String value = annotation.code();
                Service serviceName = clazz.getAnnotation(Service.class);
                TL1ExecutorService<?> service = beanFactory.getBean(serviceName.value(), TL1ExecutorService.class);
                if (service == null) {
                    logger.error("trap service executor:{}  not found!", serviceName.value());
                } else {
                    registry.put(value, service);
                }
            } catch (Exception e) {
                logger.info("bind tl1 command faild:{}", e);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.topvision.ems.tl1.TL1ExecutorRegistryService#getService(java.lang.String)
     */
    @Override
    public TL1ExecutorService<?> getService(String commandCode) {
        return registry.get(commandCode);
    }

}
