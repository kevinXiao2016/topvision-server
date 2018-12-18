/***********************************************************************
 * $Id: LogInterceptor.java,v1.0 2011-12-2 上午10:08:10 $
 * 
 * @author: huqiao
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.action;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.OperationService;

/**
 * @author huqiao
 * @created @2011-12-2-上午10:08:10
 * 
 */
@Controller("logInterceptorClass")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LogInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = -5997441109389814835L;
    @Autowired
    private OperationService operationService;
    @Value("${language}")
    private String language;

    
    /**
     * 获得日志国际化
     * 
     * @param key
     * @param clazz
     * @return
     */
    public String getString(String key, Class clazz) {
        return getString(key, getOperationLogResourcePath(clazz.getPackage().getName()));
    }
    
    
    /**
     * 国际化
     * 
     * @param key
     *            key
     * @return String
     */
    protected String getString(String key, String module) {
        try {
            ResourceManager resourceManager = ResourceManager.getResourceManager(module, language);
            // mark by @bravin: operation国际化 String operation = ZetaUtil.getOperationLog(key,
            // module, language);
            return resourceManager.getNotNullString(key);
        } catch (Exception e) {
            return key;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String result = invocation.invoke();
        Method ms;
        Class<?> clazz;
        Class<?> object;
        Map<?, ?> map;
        try {
            //修改获取session的方式
            map = invocation.getInvocationContext().getSession();
            /*object = invocation.getAction().getClass();
            Method getSession = object.getMethod("getSession");
            map = (Map<?, ?>) getSession.invoke(invocation.getAction());*/
            clazz = invocation.getAction().getClass();
            ms = invocation.getAction().getClass().getMethod(invocation.getInvocationContext().getName());
        } catch (NoSuchMethodException e) {
            // logger.error("",e);
            return result;
        }
        if (ms.getAnnotation(OperationLogProperty.class) != null) {
            OperationLogProperty property = ms.getAnnotation(OperationLogProperty.class);
            String operationName = getString(property.operationName(), getOperationLogResourcePath(clazz.getPackage()
                    .getName()));
            Method method = clazz.getMethod("getOperationResult");
            Integer operationResult = null;
            Object obj = method.invoke(invocation.getAction());
            if (obj != null) {
                operationResult = (Integer) obj;
            }
            if (method != null && operationResult != null) {
                // 用来匹配是否有需要在注释中替换的内容 暂时不考虑替换的内容中同样包括匹配符号
                int symbolStartIndex = operationName.indexOf("${");
                int symbolEndIndex = operationName.indexOf("}");
                while (symbolStartIndex != -1) {
                    String replaceEmsFieldString = operationName.substring(symbolStartIndex + 2, symbolEndIndex);
                    java.lang.reflect.Field replaceEmsField = invocation.getAction().getClass()
                            .getDeclaredField(replaceEmsFieldString);
                    replaceEmsField.setAccessible(true);
                    operationName = operationName.replace("${" + replaceEmsFieldString + "}",
                            replaceEmsField.get(invocation.getAction()).toString());
                    symbolStartIndex = operationName.indexOf("${");
                    symbolEndIndex = operationName.indexOf("}");
                }                
                if(operationName.equals(getString("operationLog.ClearCm","com.topvision.platform.resources"))){
                    Method method2 = clazz.getMethod("getEntityIds");
                    List<Long>entityIds=(List<Long>) method2.invoke(invocation.getAction());
                    for(Long ei:entityIds){
                        OperationLog log = new OperationLog(((UserContext) map.get(UserContext.KEY)).getUser().getUserName(),
                                ServletActionContext.getRequest().getRemoteHost(), operationResult, ei, operationName);
                        operationService.insertOperationLog(log); 
                    }
                }else{
                    Method method2 = clazz.getMethod("getEntityId");
                    Long entityId = (Long) method2.invoke(invocation.getAction());
                    OperationLog log = new OperationLog(((UserContext) map.get(UserContext.KEY)).getUser().getUserName(),
                            ServletActionContext.getRequest().getRemoteHost(), operationResult, entityId, operationName);
                    operationService.insertOperationLog(log); 
                }                
            }
        }
        return result;
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init() {
        super.init();
    }

    // 取得资源文件的路径
    private String getOperationLogResourcePath(String packagePath) {
        String[] paStrings = packagePath.split("\\.");
        StringBuilder resourcePath = new StringBuilder();
        //jay com.topvision.ems.xxx  所有的都到这个路径下去取资源
        if (paStrings.length >= 4) {
            for (int i = 0; i < 4; i++) {
                resourcePath.append(paStrings[i]);
                resourcePath.append(".");
            }
        }
        return resourcePath.append("operationLog").toString();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public OperationService getOperationService() {
        return operationService;
    }

    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }
}
