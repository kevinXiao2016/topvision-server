<%-- 通用的jsp脚本及标签定义 --%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants" %>
<%@ page import="com.topvision.platform.service.ThemeManager" %>
<%@ page import="com.topvision.platform.domain.UserContext" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    response.addHeader("pragma", "no-cache");
    response.addHeader("cache-control", "no-cache");
    response.addDateHeader("expries", 0);
        
    SystemConstants sc = SystemConstants.getInstance();
    String lang = sc.getStringParam("language", "zh_CN");

    String cssStyleName = ThemeManager.getDefaultTheme();
    UserContext uc = null;
    if (session != null) {
        uc = (UserContext) session.getAttribute(UserContext.KEY);
        if (uc != null && uc.getStyleName() != null) {
            cssStyleName = uc.getStyleName();
        }
        if (uc != null && uc.getUser() != null && uc.getUser().getLanguage() != null) {
            lang = uc.getUser().getLanguage();
        }
    }
    pageContext.setAttribute("lang",lang);
%>
<fmt:setLocale value="<%=lang%>"/>
<fmt:setBundle basename="com.topvision.ems.resources.common" var="c"/>