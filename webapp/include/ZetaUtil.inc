<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.topvision.platform.SystemConstants" %>
<%@ page import="com.topvision.platform.service.ThemeManager" %>
<%@ page import="com.topvision.platform.domain.UserContext" %>

<meta   http-equiv="Expires"   CONTENT="0">
<meta   http-equiv="Cache-Control"   CONTENT="no-cache">
<meta   http-equiv="Pragma"   CONTENT="no-cache">  


<%
	SystemConstants sc = SystemConstants.getInstance();
	UserContext uc = null;
	if (session != null) {
		uc = (UserContext) session.getAttribute(UserContext.KEY);
    }
%>
