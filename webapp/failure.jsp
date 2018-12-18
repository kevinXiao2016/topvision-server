<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML><HEAD>
<%@include file="include/cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.platform.resources" var="resource"/>
<TITLE><fmt:message bundle="${resource}" key="app.title"/> - <fmt:message bundle="${resource}" key="logon.failure"/></TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/gui.css"/>
<link rel="stylesheet" type="text/css" href="css/<%= cssStyleName %>/mytheme.css"/>
</HEAD><BODY>
<fmt:message bundle="${resource}" key="logon.failuredetail"/>
</body></html>
