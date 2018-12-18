/***********************************************************************
 * $Id: TrapRegistryServiceImpl.java,v1.0 2017年1月4日 下午2:19:04 $
 * 
 * @author: lizongtian
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.ems.fault.execute;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topvision.ems.fault.message.TrapEvent;
import com.topvision.ems.fault.message.TrapListener;
import com.topvision.framework.common.ClassAware;
import com.topvision.framework.service.BaseService;
import com.topvision.platform.message.service.MessageService;
import com.topvision.platform.zetaframework.valuegetter.ZetaValueReflection;

/**
 * @author lizongtian
 * @created @2017年1月4日-下午2:19:04
 *
 */
@Service
public class TrapRegistryServiceImpl extends BaseService implements TrapListener {
    private static final HashMap<String, TrapExceutor> mapper = new HashMap<String, TrapExceutor>();
    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private MessageService messageService;

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
        messageService.removeListener(TrapListener.class, this);
    }

    @PostConstruct
    @Override
    public void initialize() {
        super.initialize();
        messageService.addListener(TrapListener.class, this);
    }

    @Override
    public void start() {
        @SuppressWarnings("unchecked")
        Set<Class<?>> clazzes = new ClassAware().scanAnnotation("com.topvision", EventExecute.class);
        for (Class<?> clazz : clazzes) {
            EventExecute annotation = clazz.getAnnotation(EventExecute.class);
            String value = annotation.code();
            String[] codes = value.split(",");
            for (String code : codes) {
                code = decode(code);
                Service serviceName = clazz.getAnnotation(Service.class);
                TrapExceutor service = beanFactory.getBean(serviceName.value(), TrapExceutor.class);
                if (service == null) {
                    logger.error("trap service executor:{}  not found!", serviceName.value());
                } else {
                    mapper.put(code, service);
                }
            }
        }
    }

    private String decode(String value) {
        // value = "event.code.{tt.xxsx}.{xx.xfffx}";
        Pattern pattern = Pattern.compile("\\{[a-zA-Z._0-9]{3,}\\}");
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            String expression = matcher.group();
            expression = expression.substring(1, expression.length() - 1);
            Object $value = ZetaValueReflection.getValue(expression);
            if ($value != null) {
                value = value.replaceAll("\\{" + expression + "\\}", String.valueOf($value));
            }
        }
        return value;
    }

    public TrapExceutor findExecutor(Integer eventCode) {
        return mapper.get("event.code." + eventCode);
    }

    public void execute(TrapEvent event) {
        TrapExceutor executor = findExecutor(event.getCode());
        if (executor != null) {
            executor.execute(event);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.topvision.ems.fault.message.TrapListener#onTrapMessage(com.topvision.ems.fault.message
     * .TrapEvent)
     */
    @Override
    public void onTrapMessage(TrapEvent evt) {
        execute(evt);
    }
}
