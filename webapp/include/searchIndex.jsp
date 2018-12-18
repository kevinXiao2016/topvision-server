<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="zeta" uri="zetaframework-tags"%>
<%@include file="cssStyle.inc"%>
<fmt:setBundle basename="com.topvision.ems.resources.resources" var="resource"/>
<HTML><HEAD><TITLE>Search</TITLE>
  <link rel="stylesheet" type="text/css" href="../css/gui.css">
  <link rel="stylesheet" type="text/css" href="../css/<%= cssStyleName %>/mytheme.css">  
</HEAD><BODY class=BLANK_WND>
<center>
	<table width=600px>
		<tr><td><strong><fmt:message key="Common.whatAreYouFind" bundle="${resource}"/></strong></td></tr>
		<tr><td><a href="#"><fmt:message key="Common.searchEntity" bundle="${resource}"/>Common.searchEntity</a></td></tr>
		<tr><td><a href="#"><fmt:message key="Common.searchMonitor" bundle="${resource}"/>Common.searchMonitor</a></td></tr>
		<tr><td><a href="#"><fmt:message key="Common.searchEvent" bundle="${resource}"/>Common.searchEvent</a></td></tr>
	</table>
</center>
</BODY></HTML>
