package com.topvision.platform.action;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.topvision.framework.annotation.OperationLogProperty;
import com.topvision.platform.ResourceManager;
import com.topvision.platform.ServerBeanFactory;
import com.topvision.platform.domain.OperationLog;
import com.topvision.platform.domain.UserContext;
import com.topvision.platform.service.OperationService;

import net.sf.json.JSONObject;

@Controller("systemInterceptor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SystemInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = -591912277160211193L;
    private static final String XML_HTTP_REQUEST_HEADER = "XMLHttpRequest";
    private static Logger logger = LoggerFactory.getLogger(SystemInterceptor.class);
    @Autowired
    protected ServerBeanFactory beanFactory;
    @Value("${language}")
    private String language;
    
    private ThreadLocal<Long> beginTime = new ThreadLocal<Long>();

    public boolean isXmlHttpRequst(HttpServletRequest request) {
        return XML_HTTP_REQUEST_HEADER.equalsIgnoreCase(request.getHeader("x-requested-with"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.interceptor.AbstractInterceptor#intercept(com
     * .opensymphony.xwork2.ActionInvocation)
     */
    @Override
    public String intercept(ActionInvocation invocation) {
        beginTime.set(System.currentTimeMillis());
        /*
         * added by @bravin: 通过invocation.getAction()拿到action，然后反射取到
         * request，response也可以，但是这个拦截器本身就是struts的， 所以，直接使用 ServletActionContext.getResponse()即可。
         */
        HttpServletResponse response = ServletActionContext.getResponse();
        HttpServletRequest request = ServletActionContext.getRequest();
        
        // 获取请求参数
        String methodName = request.getMethod();
        String url = request.getRequestURL().toString();
        Map<String, Object> parameterMap = request.getParameterMap();
        
        String invokeResult;
        Integer resultStatus;
        try {
            invokeResult = invocation.invoke();
            resultStatus = OperationLog.SUCCESS;
        } catch (Exception e) {
            logger.error("invoke action error :{}", e);
            resultStatus = OperationLog.FAILURE;
            /** 执行异常处理 ***/
            handleInvocationError(response, request, invocation, e);
            /** 对于普通请求的页面，重定向到error500.jsp页面，对于xmlhttprequest，需要处理异常，所以都直接返回none即可 */
            invokeResult = "none";
        }
        /** 执行操作日志的记录 **/
        recordOperationLog(request, response, invocation, resultStatus);
        
        if(!url.endsWith("/ping.tv")) {
            Long cost = System.currentTimeMillis() - beginTime.get();
            logger.info("[{}] {} 参数：{}", methodName, url, parameterMap.toString());
            logger.info("请求耗时：{}ms", cost);
        }
        return invokeResult;
    }

    /**
     * 记录操作日志
     * 
     * @param request
     * @param response
     * @param invocation
     * @param resultStatus
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void recordOperationLog(HttpServletRequest request, HttpServletResponse response,
            ActionInvocation invocation, Integer resultStatus) {
        // record log
        Class clazz = invocation.getAction().getClass();
        HttpSession httpSession = request.getSession();
        try {
            /**
             * 如果使用 invocation.getInvocationContext().getName()则强制要求 struts配置中 name和method必须相同！
             * 这里必须通过ActionProxy得到实际调用的method
             */
            Method ms = clazz.getMethod(invocation.getProxy().getMethod());
            if (ms.getAnnotation(OperationLogProperty.class) != null) {
                try {
                    // logInterceptor hack:如果存在operationResult并且不为null，则返回，表示由以前的操作日志系统处理过了
                    Method method = clazz.getMethod("getOperationResult");
                    // Field operationResult = clazz.getDeclaredField("operationResult");
                    if (method.invoke(invocation.getAction()) != null) {
                        return;
                    }
                } catch (Exception e) {
                    // 这部分是对原有操作日志系统的兼容，所有不需要处理异常
                }
                // record
                OperationLogProperty property = ms.getAnnotation(OperationLogProperty.class);
                String operationName = getString(property.operationName(), getOperationLogResourcePath(clazz
                        .getPackage().getName()));
                /*
                 * Field entityIdFiled = clazz.getDeclaredField("entityId");
                 * entityIdFiled.setAccessible(true); Long entityId = (Long)
                 * entityIdFiled.get(invocation.getAction());
                 */
                Method method = clazz.getMethod("getEntityId");
                Long entityId = (Long) method.invoke(invocation.getAction());
                // 用来匹配是否有需要在注释中替换的内容 暂时不考虑替换的内容中同样包括匹配符号
                int symbolStartIndex = operationName.indexOf("${");
                int symbolEndIndex = operationName.indexOf("}");
                while (symbolStartIndex != -1) {
                    String replaceEmsFieldString = operationName.substring(symbolStartIndex + 2, symbolEndIndex);
                    Field replaceEmsField = clazz.getDeclaredField(replaceEmsFieldString);
                    replaceEmsField.setAccessible(true);
                    operationName = operationName.replace("${" + replaceEmsFieldString + "}",
                            replaceEmsField.get(invocation.getAction()).toString());
                    symbolStartIndex = operationName.indexOf("${");
                    symbolEndIndex = operationName.indexOf("}");
                }
                UserContext uc = (UserContext) httpSession.getAttribute(UserContext.KEY);
                String username = uc.getUser().getUserName();
                OperationLog log = new OperationLog(username, request.getRemoteHost(), resultStatus, entityId,
                        operationName);
                OperationService operationService = (OperationService) beanFactory.getBean("operationService");
                operationService.insertOperationLog(log);
            }
        } catch (Exception e) {
            logger.error("record opertionlog error :{}", e);
        }
    }

    /**
     * invoke异常处理
     * 
     * @param response
     * @param request
     * @param invocation
     * @param e
     * @return
     */
    private void handleInvocationError(HttpServletResponse response, HttpServletRequest request,
            ActionInvocation invocation, Exception e) {
        /** output exception to browser 。 */
        String requestURI = request.getRequestURI();
        // 判断是否为ajax请求.如果x-requested-with存在并且为 XMLHttpRequest，则是ajax请求
        if (isXmlHttpRequst(request) || requestURI.endsWith(".mb")) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            try {
                JSONObject json = new JSONObject();
                Field[] fields = e.getClass().getDeclaredFields();
                for (Field field : fields) {
                    try {
                        field.setAccessible(true);
                        String data = (String) field.get(e);
                        json.put(field.getName(), data);
                    } catch (Exception e1) {
                    }
                }
                json.put("type", e.getClass().getName());
                json.put("simpleName", e.getClass().getSimpleName());
                json.put("exception", e.getLocalizedMessage());
                json.put("message", e.getMessage());
                json.write(response.getWriter());
            } catch (IOException e1) {
                logger.debug("output exception error : {}", e1);
            }
        } else {// 否则为传统请求，需要直接返回error
            try {
                if (requestURI.endsWith(".tv")) {
                    response.sendRedirect("/error500.jsp");
                }
            } catch (IOException e1) {
            }
        }
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

    // 取得资源文件的路径
    private String getOperationLogResourcePath(String packagePath) {
        String[] paStrings = packagePath.split("\\.");
        StringBuilder resourcePath = new StringBuilder();
        // jay com.topvision.ems.xxx 所有的都到这个路径下去取资源
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
}
