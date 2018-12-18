/***********************************************************************
 * $Id: PageContextLanguageAnalyzer.java,v1.0 2013-3-30 下午2:56:03 $
 * 
 * @author: Bravin
 * 
 * (c)Copyright 2011 Topvision All rights reserved.
 ***********************************************************************/
package com.topvision.platform.zetaframework.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import com.topvision.platform.SystemConstants;
import com.topvision.platform.domain.UserContext;

/**
 * 使用tag的时候如果遇到界面为JSP跳转，则无法通过Struts的ServletActionContext获取其Request
 * PageContextLanguageAnalyzer在各种zeta标签中共用，通过pageContext找出其合适的语言
 * @author Bravin
 * @created @2013-3-30-下午2:56:03
 *
 */
public class PageContextLanguageAnalyzer {
    /**
     * 找出界面中合适的语言，不依赖于Struts框架
     * @param pageContext
     * @return
     */
    public static String analyzeAppropriateLanguage(PageContext pageContext) {
        //为了避免强转出现nullPointException，故用object接，如果不为null，返回的时候再强转
        Object language = pageContext.getAttribute("lang");
        if (language != null) {
            return (String) language;
        }
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        UserContext uc = (UserContext) request.getSession().getAttribute(UserContext.KEY);
        return analyzeAppropriateLanguage(uc);
    }

    /**
     * 找出界面中合适的语言，不依赖于Struts框架
     * @param uc
     * @return
     */
    public static String analyzeAppropriateLanguage(UserContext uc) {
        String appropriateLanguage = SystemConstants.getInstance().getStringParam("language", "zh_CN");
        if (uc != null) {
            //@MARK by bravin:有的用户可能没有设置语言，返回结果就是 null,这个设计有误
            String uclanguage = uc.getUser().getLanguage();
            if (uclanguage != null) {
                appropriateLanguage = uclanguage;
            }
        }
        return appropriateLanguage;
    }
}
